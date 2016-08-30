# Minigames-Lib 2.0 - Development

## Context

About 99% of methods in a minigame need to answer the following queries:

* What happened?
* Who (=player/team) is involved?
* Which minigame is involved?
* Which arena is involved?
* Which component inside the arena is involved?

Analyzing the existing minigames code of version 1 showed up that it is very difficult to hack a new feature as
this may influence many methods and code fragments. Hundreds of code lines need to be analyzed before adding a new
feature.

In Minigames lib v2 we use a plain but good design. We provide a simple method to store and receive objects
associated with the current method invocation.

There are five methods you will like:

    MglibInterface.INSTANCE.get().getCurrentPlayer()
    MglibInterface.INSTANCE.get().getCurrentMinigame()
    MglibInterface.INSTANCE.get().getCurrentArena()
    MglibInterface.INSTANCE.get().getCurrentZone()
    MglibInterface.INSTANCE.get().getCurrentComponent()

These methods are meant to return the corresponding objects. No more need to wonder where you get the player
that caused an event.

### Theory behind context and storage: Aspect oriented programming

While creating the concepts of version 2 we had to decide what to do. What we describe here is already a
well known concept for experienced java developers: aspect oriented programming or java beans.

At runtime you do not know where to get the information from. The framework (=minigames lib) will decide where
to get the instances from.

Typically aspect oriented programming heavily manipulates the byte code and class structure of your application.
In aspect oriented programming you will define injection points (private variables), for example:

    @Inject
    @CommandSender
    ArenaPlayerInterface commandSender;

The APO-Framework now does some magic.

That's really nice and a clean way of programming. There are only some points why we did not use AOP for the minigames
lib.

#### Performance

Having a very generic minigames library already costs us some performance. Lightwight games like simple pvp shooters have
some overhead using the minigames library.

Adding a complete AOP framework is too much. Maybe we will have performance issues in future and although AOP framweworks
like weld already are good it is better to not depend on them.

#### Light library

The minigames library itself should be light. No unneeded features and no code that is behaving too complex. We decided to
have some clear basic rules what is part of the context and what should not be stored within contexts.

#### Too many magic

We are fans of aspect oriented programming. But it is really too many magic. In minecraft servers we have a very clean
system. There are three main calls from the server into any plugin:

* Console/chat commands
* Events
* Tasks

We do not expect there will be ever more. Nor do we expect that there is need to support even more.

AOP is very flexible in the way it is used. You do not care where the call is from or what the callee wants to do.
Many design aspects are goging around the question: Where do we get an object from, which context is the object living
and where is the factory to get it?

In minecraft servers we never ask this questions. All we need to have is a clean API to get the objects we are interested
in. We always know who is generating the object and we never plan to influence the library. No plugin should really
be interested in creating their own ArenaPlayer object...

#### Enterprise architecture

Although AOP can be used in other situations the main goal is tu use it in java enterprise architecture. It is designed
to work best in (web) application servers with service oriented architecture and several business layers.

We do not say this is sad. But it is simple: That is not the way minecraft servers are working. So why should we try to
build on a framework that is used in other situations?

### Lifetime of the context

The context lives within the current invocation point. As we said above we have three main invocation points:

* Console/chat commands
* Events
* Tasks

Every time the minigames library is invoked on one of the three invocation points it creates a new context. After
the invocation point is left (=the method returns) the context will be cleared.

You can manually create a new context by invoking one of the following methods on the minigames lib:

     runInNewContext(...)
     runInCopiedContext(...)
     calculateInNewContext(...)
     calculateInCopiedContext(...)

### Storing your own objects

You can store and receive your own objects within the context. Simply invoke `setContext` with any object you like.

The objects are identified by class. Invoking the setContext twice with same class will overwrite the object.

## Storage

At multiple points in the API you will see methods returning `MinigameStorage` objects.

The behave similar to the context. You can store objects by class name inside them and receive them. The objects
must implement `Configurable` interface.

The storages are meant to hold long-lifed objects. The most simple example is the following storage:

    ArenaPlayerInterface.getSessionStorage()

This methods introduces a storage that lives as long as the user itself is online. Once the server stops or the
player goes offline this storage is removed. NOTICE: If the user switches to another server in a bungee coord
network he goes offline and looses it's session storage too.

Another example:

    ArenaPlayerInterface.getPersistentStorage()

This values will be stored in files or databases. It is meant to be available even if the player is offline and
once the player is visiting the server once again you can read the values and do something.
