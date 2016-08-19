# Minigames-Lib 2.0 - Development

## The gaming rules

A minigame is built on top of small gaming rules. Those gaming rules influence the behavior of the minigame as well as
the mechanic.

Under the hood the gaming rules are divided into two pieces:
* predicates listening on spigot events
* action handlers to do something

The predicates are helper functions (java 8 lamdas) that are queried once an event is detected.

### Execution order of events

The predicates of a rule are typically executed in following order:

* global/core predicate
* arena related predicate (inherited from arena types)
* arena state related predicate
* match state related predicate
* player state related predicate
* player related predicate

This is the default ordering of predicates. However if you add predicates to a rule you can set a numeric value (priority).
The lower this value is the later the predicate will be invoked.

In boolean logic this seems to be senseless. Let us look at the following sample:

    if (predicateA && predicateB) action;
    
    if (predicateB && predicateA) action;
    
If you think of predicates for boolean values it is senseless to order them. But predicates in minigames lib are more than
just boolean values or lambdas returning booleans.

Minigame predicates can return the following states:

* TRUE (-> test the next predicate of this rule in order)
* FALSE (-> abort the execution)
* CHAIN (-> invoke a special predicate chain)

#### Predicate chains

Predicate chains are a way to have more than one path within predicates and actions to be invoked. As soon as a predicate
returns to invoke a chain the execution will continue with this chain and not with the normal execution path.

Another word for chains can be "sub rules". A rule may not only be something that can be tested for true and then an action
gets executed. Rules may be more complex.

Some example:

TODO

### Type of fetched events

TODO

### Common predicates

TODO

### Common actions

TODO

### Common rules

TODO

### Common rule sets

TODO

### Common arena types

TODO

## Configurable rules

TODO

## Votable rules

TODO
