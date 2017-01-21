# Minigames-Lib 2.0 - Development

## Variable Substitution

The variable substitution is a way to replace content of a string at runtime.

Per default the configuration variable paths and the permission paths are using variable substitution.

For manually get a string with variable substitution simply call

    String resultString = MglibInterface.INSTANCE.get().resolveContextVar(sourceString);

## Default string substitution

### $PERM:MGLIB$

Resolves to value of CommonConfig.PermissionsPrefix

### PERM:MGLIB:KITS$

Resolves to value of CommonConfig.PermissionKitsPrefix

### $PERM:MGLIB:GUNS$

Resolves to value of CommonConfig.PermissionGunsPrefix

### $PERM:MGLIB:SHOPS$

Resolves to value of CommonConfig.PermissionShopsPrefix

### $PERM:MINIGAME$

Resolves to value of CommonConfig.PermissionGamesPrefix appended with current context minigame

### $PERM:MINIGAME:name$

Resolves to value of CommonConfig.PermissionGamesPrefix appended with "name" text

### $OPT:path$

Resolve to string option value of config.yml for the current context minigame

### $OPT:name:path$

Resolve to string option value of config.yml for the minigame with given name

### $CTX:type:name$

Resolves to a context variable of given type and by resolving getter with given name.

If name contains dots it invokes deep getters. Some example 
    
    $CTX:com.github.mce.minigames.api.player.ArenaPlayerInterface:bukkitPlayer.playerListName$
    
will resolve in the following call:
    
    getContext(ArenaPlayerInterface.class).getBukkitPlayer().getPlayerListName()
    
