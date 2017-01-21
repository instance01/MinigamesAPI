# Minigames-Lib 2.0 - Game rules

## Existing rules from v1.0

TODO

## Existing features from v1.0

TODO

* Sign updates

## Existing commands from v1.0

### /start

#### isSenderPlayer:
            if (!(sender instanceof Player))
            {
                sender.sendMessage(Messages.getString("MinigamesAPI.ExecuteIngame", LOCALE)); //$NON-NLS-1$
                return true;
            }

#### hasPermission
            if (!sender.hasPermission(PermissionStrings.MINIGAMES_START))
            {
                sender.sendMessage(Messages.getString("MinigamesAPI.NoPermissionForStart", LOCALE)); //$NON-NLS-1$
                return true;
            }
            
#### ifNotInArena
            for (final PluginInstance pli : MinigamesAPI.pinstances.values())
            {
                if (pli.containsGlobalPlayer(p.getName()))
                {
                    .... (return)
                }
            }
            sender.sendMessage(Messages.getString("MinigamesAPI.StartNotWithinArena", LOCALE)); //$NON-NLS-1$
            
#### ifInArena
            for (final PluginInstance pli : MinigamesAPI.pinstances.values())
            {
                if (pli.containsGlobalPlayer(p.getName()))
                {
                    .... (return)
                }
            }