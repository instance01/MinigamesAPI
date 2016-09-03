# Minigames-Lib 2.0 - Development

## Messages and Localization

Every message or name in minigames lib is localized. Users can choose their preferred localization. Language packs
can be used to add a language to your plugins.

Except messages passed to the java logger all messages sent to console/ players should always be using the message API.

## Declaring a message

To declare a message simply create an enumeration.

    /**
     * My own messages.
     *
     * @author mepeisen
     */
    @LocalizedMessages("myplugin.ingame")
    public enum IngameMessages implements LocalizedMessageInterface
    {
        /**
         * Player has made something special.
         *
         * <p>Arguments:</p>
         *
         * <ol>
         * <li>String: player name</li>
         * </ol>
         */
        @LocalizedMessage(defaultMessage = "Player %1$s did a looping", severity = MessageSeverityType.Success)
        PlayerDidLooping,
        
        /**
         * Player does not know hoe to play.
         *
         * <p>Arguments:</p>
         *
         * <ol>
         * <li>String: player name</li>
         * <li>String: some cause</li>
         * </ol>
         */
        @LocalizedMessage(defaultMessage = "Player %1$s seems to be a noob", defaultAdminMessage = "Player %1$s seems to be a noob. Cause: %2$s", severity = MessageSeverityType.Warning)
        PlayerIsNoob,
    }
    
Your plugin provider now simply needs to return this class that minigames lib will do the rest.

    @Override
    public Iterable<Class<? extends Enum<?>>> getMessageClasses()
    {
        final List<Class<? extends Enum<?>>> result = new ArrayList<>();
        result.add(IngameMessages.class);
        return result;
    }
    
Hint: The string "myplugin.ingame" in the @LocalizedMessages annotation is the path name in messages.yml.
If you are using multiple enumeration you should always ensure that they do not share the same path.

The path "core" is always preserved for minigames lib itself.

## Using parameters

The messages are parsed through javas System.format.

If a message can be individualized by parameters you can add those parameter tags. "%1$s" will display the first argument and assume it is a string.
Read the oracle javadoc of the java language for details.

## Editing the messages.yml

The above example will result in the following messages.yml

    myplugin:
      ingame:
        PlayerDidLooping:
          default_locale: en
          user:
            en: 'Player %1$s did a looping'
        PlayerIsNoob:
          default_locale: en
          user:
            en: 'Player %1$s seems to be a noob'
          admin:
            en: 'Player %1$s seems to be a noob. Cause: %2$s'

The user section holds all messages per locale display to a common user. The admin section holds the administrator message per locale.

To add a new locale simply create a new line on level of "en" and add the message.

## Overriding default locale

The messages use "en" for default locale. You can override this in @LocalizedMessages annotation at enum class level.
But it is recommended that you choose "en" for your favorite locale in open-source/ public available plugins. Mixing locales
can confuse the players.

You should always prefer to provide language packs for your plugin or let the user create the language packs.

## User vs admin

The minigames lib provides special behavior for messages. Usually administrators (=minecraft operators) like additional
information for analyzing problems. In our example above we do not like to display the noob cause to normal users.
But an administrator can see it.

To solve this problem you can declare a special administrator message.

## Using the messages in code.

Using the message is fairly simple.

Typically you will send the message to a given player. So you can invoke the sendMessage method on the ArenaPlayerInterface.

    otherPlayer.sendMessage(IngameMessages.PlayerIsNoob, someNoob.getName(), "Fell out of arena");

In this example we are not consequent. The constant String "Fell out of Arena" should be localized too. However the minigames lib
provides a solution for this too. We can convert a message to a nested argument:

    otherPlayer.sendMessage(IngameMessages.PlayerIsNoob, someNoob.getName(), IngameMessages.NoobCauseOutOfArena.toArg());

## Special message type: Error codes

There is a special variant of localized messages. The error codes behave exactly the same. The only difference is that
they implement interface MinigameErrorCode instead of LocalizedMessageInterface.

The error codes are mainly meant to be used in MinigameExceptions. Typically the represent messages or errors in command
handlers.

## Special message type: Message lists

A message list is a collection of multiple messages. It represents individual lines.
For example a help text in a command may be made of 4 lines in English and of 5 lines
in German. That the code does not get confused with these lines we support
this special message type.

To declare the localized message list first use the LocalizedMessageList annotation inside your enumeration.

    /**
     * Some multi-line message.
     * 
     * <p>Arguments:</p>
     *
     * <ol>
     * <li>String: player name</li>
     * </ol>
     */
    @LocalizedMessageList({
        "Welcome to this adventure, player %1$s!",
        "Can you find the Yeti?",
        "Sam and Max are absent.",
        "HELP!"
    })
    MultiLineMessage,

This will result in following entries in messages.yml

    myplugin:
      ingame:
        MultiLineMessage:
          default_locale: en
          user:
            en:
              - 'Welcome to this adventure, player %1$s!'
              - 'Can you find the Yeti?'
              - 'Sam and Max are absent.'
              - 'HELP!'

As you see the lines are a yaml list. If you have a language only containing three lines this can be done by changing
it to the following:

    myplugin:
      ingame:
        MultiLineMessage:
          default_locale: en
          user:
            en:
              - 'Welcome to this adventure, player %1$s!'
              - 'Can you find the Yeti?'
              - 'Sam and Max are absent.'
              - 'HELP!'
            de:
              - 'Willkommen bei deinem Abenteuer, Spieler %1$s!'
              - 'Findest du den Yeti?'
              - 'Sam und Max sind verschwunden. HILFE!'

## Text format and colors

Minigames lib uss some default colors based on message severity.

* error: Dark red
* information: white
* loser: red
* success: green
* warning: yellow
* winner: gold

You can override it by using either the literals (f.e. "§7" for gray) or by using the string constants in the
LocalizedMessage and LocalizedMessageList annotations.

Example:
    
    @LocalizedMessage(defaultMessage = LocalizedMessage.GRAY + "some gray text")

### formatting conventions

TODO

## Special message type: Message sets

The message sets are predefined messages used within typical minigames.

TODO

## Language packs

TODO

## Administrator hints in messages.yml

The minigames lib can add comments to your messages.yml.

This helps administrators to manipulate your messages and provide language packs.

TODO

## Referencing user defined messages

Messages may be defined outside the messages.yml within regular configuration variables.

We explain it on the `ArenasConfig.Description` option value. This represents an optional
description that administrators can set for users. This text is localized thus the users may
get a translation in their preferred language.

We declare this configuration option to be a localized message by using ConfigurationObject annotation.
    
    @ConfigurationObject(clazz = LocalizedConfigLine.class)
    Description

Using is it fairly simple:

    final LocalizedConfigLine description = ArenasConfig.Description.getObject()

If you want to use a single line message use the class `LocalizedConfigString`.

Details on how to declare and use configuration values are explained in the article [Configuration options](dev_minigame_config.html).

## Revisions and migration

TODO
