/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.github.mce.minigames.impl.context;

import java.lang.reflect.Method;
import java.util.logging.Level;

import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.MinigameInterface;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.config.CommonConfig;
import com.github.mce.minigames.api.context.ContextResolverInterface;
import com.github.mce.minigames.api.context.MinigameContext;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;

/**
 * Default implementation of context resolve.
 * 
 * <p>
 * Resolves the following variables:
 * </p>
 * 
 * <ul>
 * <li><b>PERM:MGLIB</b> to {@link CommonConfig#PermissionPrefix}</li>
 * <li><b>PERM:MGLIB:KITS</b> to {@link CommonConfig#PermissionKitsPrefix}</li>
 * <li><b>PERM:MGLIB:GUNS</b> to {@link CommonConfig#PermissionGunsPrefix}</li>
 * <li><b>PERM:MGLIB:SHOPS</b> to {@link CommonConfig#PermissionShopsPrefix}</li>
 * <li><b>PERM:MINIGAME</b> to {@link CommonConfig#PermissionGamesPrefix} and current context minigame</li>
 * <li><b>PERM:MINIGAME:name</b> to {@link CommonConfig#PermissionGamesPrefix} and name string</li>
 * <li><b>OPT:path</b> to config.yml variable of given name and for current context minigame</li>
 * <li><b>OPT:name:path</b> to config.yml variable of given name and for minigame of given name</li>
 * <li><b>CTX:type:name</b> to a common context variable of given type and variable name.</li>
 * </ul>
 * 
 * <p>
 * Available context variable types are:
 * </p>
 * 
 * <ul>
 * <li><b>PLAYER</b> maps to the current {@link ArenaPlayerInterface}.</li>
 * <li><b>MINIGAME</b> maps to the current {@link MinigameInterface}.</li>
 * <li><b>ARENA</b> maps to the current {@link ArenaInterface}.</li>
 * </ul>
 * 
 * @author mepeisen
 */
public class DefaultResolver implements ContextResolverInterface
{
    
    @Override
    public String resolve(String varName, String[] args, MinigameContext context)
    {
        switch (varName)
        {
            case "PERM": //$NON-NLS-1$
                if (args.length > 0)
                {
                    switch (args[0])
                    {
                        case "MGLIB": //$NON-NLS-1$
                            if (args.length > 2)
                            {
                                // failed
                                return null;
                            }
                            if (args.length > 1)
                            {
                                switch (args[1])
                                {
                                    case "KITS": //$NON-NLS-1$
                                        return CommonConfig.PermissionKitsPrefix.getString();
                                    case "GUNS": //$NON-NLS-1$
                                        return CommonConfig.PermissionGunsPrefix.getString();
                                    case "SHOPS": //$NON-NLS-1$
                                        return CommonConfig.PermissionShopsPrefix.getString();
                                    default:
                                        // failed
                                        return null;
                                }
                            }
                            return CommonConfig.PermissionPrefix.getString();
                        case "MINIGAME": //$NON-NLS-1$
                            if (args.length > 2)
                            {
                                // failed
                                return null;
                            }
                            if (args.length > 1)
                            {
                                return CommonConfig.PermissionGamesPrefix.getString() + '.' + args[1];
                            }
                            final MinigameInterface minigame = context.getContext(MinigameInterface.class);
                            if (minigame == null)
                            {
                                // failed
                                return null;
                            }
                            return CommonConfig.PermissionGamesPrefix.getString() + '.' + minigame.getName();
                        default:
                            // failed
                            return null;
                    }
                }
                // failed
                return null;
            case "OPT": //$NON-NLS-1$
                if (args.length == 1)
                {
                    final MinigameInterface minigame = context.getContext(MinigameInterface.class);
                    if (minigame == null)
                    {
                        // failed
                        return null;
                    }
                    final String value = minigame.getConfig("config.yml").getString(args[0]); //$NON-NLS-1$
                    return value;
                }
                if (args.length == 2)
                {
                    final MinigameInterface minigame = MglibInterface.INSTANCE.get().getMinigame(args[0]);
                    if (minigame == null)
                    {
                        // failed
                        return null;
                    }
                    final String value = minigame.getConfig("config.yml").getString(args[1]); //$NON-NLS-1$
                    return value;
                }
                // failed
                return null;
            case "CTX": //$NON-NLS-1$
                if (args.length == 2)
                {
                    switch (args[0])
                    {
                        case "PLAYER": //$NON-NLS-1$
                            return resolveVar(context.getContext(ArenaPlayerInterface.class), args[1]);
                        case "MINIGAME": //$NON-NLS-1$
                            return resolveVar(context.getContext(MinigameInterface.class), args[1]);
                        case "ARENA": //$NON-NLS-1$
                            return resolveVar(context.getContext(ArenaInterface.class), args[1]);
                        default:
                            try
                            {
                                final Class<?> clazz = Class.forName(args[0]);
                                return resolveVar(context.getContext(clazz), args[1]);
                            }
                            catch (@SuppressWarnings("unused") Exception ex)
                            {
                                // ignore silently and let us return null (failed); maybe another resolver knows how to handle it
                            }
                            return null;
                    }
                }
                // failed
                return null;
            default:
                // failed
                return null;
        }
    }
    
    /**
     * Resolves a variable by invoking getters.
     * 
     * @param object
     *            the object to resolve the getter path against.
     * @param path
     *            getter path
     * @return resolved variable
     */
    private String resolveVar(Object object, String path)
    {
        Object cur = object;
        final String[] splitted = path.split("\\."); //$NON-NLS-1$
        int i = 0;
        cur = object;
        while (cur != null && i < splitted.length)
        {
            final String getterName = "get" + splitted[i].substring(0,  1).toUpperCase() + splitted[i].substring(1); //$NON-NLS-1$
            try
            {
                final Method mth = cur.getClass().getMethod(getterName);
                if (mth != null)
                {
                    cur = mth.invoke(cur);
                }
            }
            catch (Exception ex)
            {
                final MglibInterface lib = MglibInterface.INSTANCE.get();
                if (lib.debug())
                {
                    lib.getLogger().log(Level.FINE, "Problems invoking " + getterName + " on " + cur, ex); //$NON-NLS-1$ //$NON-NLS-2$
                }
                cur = null;
            }
            i++;
        }
        
        return cur == null ? "NULL" : cur.toString(); //$NON-NLS-1$
    }
    
}
