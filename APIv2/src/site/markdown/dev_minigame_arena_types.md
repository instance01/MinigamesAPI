# Minigames-Lib 2.0 - Development

## Arena types

Every arena type in minigames lib is declared within enumerations.

## Declaring a configuration option

To declare an arena type simply create an enumeration.

    /**
     * My arena types.
     *
     * @author mepeisen
     */
    @ArenaTypes
    public enum MyArenaType implements ArenaTypeInterface
    {
    
        /**
         * Our default arena type.
         */
        @ArenaType
        Default,
        
        /**
         * A team play mode.
         */
        @ArenaType
        Teams
        
    }
    
While initializing your minigames plugin you are able to create the arena types:

    final MinigamePluginInterface minigame = MglibInterface.INSTANCE.get().register(new MyMinigame(this));
    
    // register arena types
    final ArenaTypeBuilderInterface defaultType = minigame.createArenaType("Default", MyArenaType.Default, true);
    // TODO init default type
    
    final ArenaTypeBuilderInterface teamsType = minigame.createArenaType("Teams", MyArenaType.Teams, false);
    // TODO init teams type
    
    minigame.init();
    
Hint: The strings "Default" and "Teams" must not be changed. They are written to arenas.yml. Changing it in a later plugin version will
cause existing arenas to be corrupt. They cannot be loaded any more. The strings are presented to administrators too while arena creation.

## Configure the arena type

### Inherit an existing arena type

To inherit and override an existing arena type you need to inherit the rules and match phases.


