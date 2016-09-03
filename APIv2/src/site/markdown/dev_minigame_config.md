# Minigames-Lib 2.0 - Development

## Configuration options

Every configuration option in minigames lib is declared within enumerations.

## Declaring a configuration option

To declare an option simply create an enumeration.

    /**
     * My plugin configuration options.
     *
     * @author mepeisen
     */
    @ConfigurationValues(path = "myplugin.config", fixed = true)
    public enum MyConfig implements ConfigurationValueInterface
    {
    
        /**
         * True if special animations are activated.
         */
        @ConfigurationBool(defaultValue = true)
        SpecialAnimationsEnabled,
        
    }
    
Your plugin provider now simply needs to return this class that minigames lib will do the rest.

    @Override
    public Iterable<Class<? extends Enum<?>>> getConfigurations()
    {
        final List<Class<? extends Enum<?>>> result = new ArrayList<>();
        result.add(MyConfig.class);
        return result;
    }
    
Hint: The string "myplugin.config" in the @ConfigurationValues annotation is the path name in config.yml.
If you are using multiple enumerations you should always ensure that they do not share the same path.

The path "core" is always preserved for minigames lib itself.

The path "config" comes from configuration sets (explained later on). You should not use this path because
on newer versions of the minigames lib some configuration option may be added and you will get into trouble.

## Configuration option name

The configuration option name is built from @ConfigurationValues path at enum class level.
The name of the enum constant is appended. Thus the above example will result in path
"myplugin.config.SpecialAnimationEnabled".

You can override the configuration options name in annotation:

    @ConfigurationBool(name = "special_animations.enabled", defaultValue = true)
    
This will result in path "myplugin.config.special_animations.enabled".

## Changing the file name

Per default all configuration options come from config.yml.

You can override this behavior by declaring another configuration file in @ConfigurationValues annotation at enum class level:

    @ConfigurationValues(path = "myplugin.config", file="otherconfig.yml")

Hint: That is the correct way other files are used in minigames lib. For example the shop configuration (shop.yml) actually
is a configuration enumeration pointing to this file.

## Using the configuration option

Using the option is fairly simple:

    MyConfig.SpecialAnimationsEnabled.getBool()

Setting a new value is similar:

    MyConfig.SpecialAnimationsEnabled.setBool(true);
    MyConfig.SpecialAnimationsEnabled.saveConfig();

Although invoked on a single configuration option the saveConfig will always save the whole configuration file.
So it is ok, to invoke setters on multiple configuration options and call save only once. This will persist all
as long as they are not spread over multiple configuration files.

## Option types

We support all java primitives and lists of java primitves.

We support following objects:

* Vector
* ArenaPlayerInterface (minigames player representation)
* Color
* ItemStack

As a special variant we support "objects". Those are classes implementing the Configurable interface of the minigames api.

To declare an configuration option of a specific type simply use the matching @Configurationxxx annotation, f.e. @ConfigurationPlayer.

## Using sections/ sub configuration values

TODO

## Special option type: Config sets

The configuration sets are predefined configuration options used within typical minigames.

TODO

## Administrator hints in config.yml

The minigames lib can add comments to your config.yml.

This helps administrators to manipulate your configuration options.

TODO

## fixed config vs. non-fixed config

In @ConfigurationValues annotation there is a boolean, called `fixed`. We did not explain it but in our example above we simply set it to true.
So all of our configuration variables became "FIXED". But what does it mean?

A fixed configuration option will always be connected to the minigame/ plugin that declared this option.

While a non-fixed configuration option will be resolved against the CONTEXT. The context is able to resolve the player, minigame or arena depending
on the original call. For example during a command call the context will contain the player that typed the command in their clients chat console
and the context will contain the arena the player is located in. Querying a non-fixed arena option will always be read from the minigames arena.yml and
from the arenas section of the current arena.

In other words: A non-fixed option value can be present in any minigame.

### When to use fixed configuration options

You will always use fixed configuration options when reading options from your (and only your) minigame.

### When to use non-fixed configuration options

You will always use non-fixed configuration options when reading options from any minigame. Typically when writing some kind of library or tool
that provides rule sets and other things so that other plugins can use them for their minigame. 

## path variable substitution

The ArenasConfig enumeration in the API uses the following annotation:

    @ConfigurationValues(path = "arenas.$CTX:ARENA:internalName$", file = "arenas.yml")
    
As you can see, the path is somehow cryptic. It contains a so called variable substitution.

Details on variable substitution are explained in chapter [Variable substitution](dev_advanced_variable_substitution.html).

## Revisions and migration

TODO
