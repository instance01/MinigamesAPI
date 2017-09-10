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

package com.comze_instancelabs.minigamesapi.spigottest;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ImmutableSet;

/**
 * Simple plugin manager for testing.
 * 
 * @author mepeisen
 */
class DummyPluginManager implements PluginManager
{
    
    /** registered plugins per name. */
    private Map<String, JavaPlugin> plugins = new HashMap<>();
    
    private final Map<String, Permission> permissions = new HashMap<String, Permission>();
    private final Map<Boolean, Set<Permission>> defaultPerms = new LinkedHashMap<Boolean, Set<Permission>>();
    private final Map<String, Map<Permissible, Boolean>> permSubs = new HashMap<String, Map<Permissible, Boolean>>();
    private final Map<Boolean, Map<Permissible, Boolean>> defSubs = new HashMap<Boolean, Map<Permissible, Boolean>>();
    
    /**
     * 
     */
    public DummyPluginManager()
    {
        this.defaultPerms.put(Boolean.TRUE, new HashSet<>());
        this.defaultPerms.put(Boolean.FALSE, new HashSet<>());
    }
    
    @Override
    public void registerInterface(Class<? extends PluginLoader> loader) throws IllegalArgumentException
    {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Adds a mocked plugin.
     * @param plugin java plugin to be registered.
     */
    public void addMockedPlugin(JavaPlugin plugin)
    {
        this.plugins.put(plugin.getName().replace(' ', '_'), plugin);
    }
    
    @Override
    public Plugin getPlugin(String name)
    {
        return this.plugins.get(name.replace(' ' , '_'));
    }
    
    @Override
    public Plugin[] getPlugins()
    {
        return this.plugins.values().toArray(new Plugin[this.plugins.size()]);
    }
    
    @Override
    public boolean isPluginEnabled(String name)
    {
        return this.getPlugin(name) != null;
    }
    
    @Override
    public boolean isPluginEnabled(Plugin plugin)
    {
        return true;
    }
    
    @Override
    public Plugin loadPlugin(File file) throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Plugin[] loadPlugins(File directory)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void disablePlugins()
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clearPlugins()
    {
        this.plugins.clear();
        permissions.clear();
        if (defaultPerms.get(true) != null) defaultPerms.get(true).clear();
        if (defaultPerms.get(false) != null) defaultPerms.get(false).clear();
        HandlerList.unregisterAll();
    }
    
    @Override
    public void callEvent(Event event) throws IllegalStateException
    {
        for (final RegisteredListener listener : getEventListeners(getRegistrationClass(event.getClass())).getRegisteredListeners())
        {
            try
            {
                listener.callEvent(event);
            }
            catch (EventException ex)
            {
                ex.printStackTrace();
            }
        }
    }
    
    @Override
    public void registerEvents(Listener listener, Plugin plugin)
    {
        // code mostly taken from Spigot: JavaPluginLoader.createRegisteredListeners
        final Method[] publicMethods = listener.getClass().getMethods();
        final Method[] privateMethods = listener.getClass().getDeclaredMethods();
        final Set<Method> methods = new HashSet<>(publicMethods.length + privateMethods.length, 1.0f);
        for (Method method : publicMethods)
        {
            methods.add(method);
        }
        for (Method method : privateMethods)
        {
            methods.add(method);
        }
        for (final Method method : methods)
        {
            final EventHandler eh = method.getAnnotation(EventHandler.class);
            if (eh == null) continue;
            if (method.isBridge() || method.isSynthetic())
            {
                continue;
            }
            final Class<?> checkClass;
            if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0]))
            {
                continue;
            }
            final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
            method.setAccessible(true);
            final EventExecutor executor = new EventExecutor() {
                public void execute(Listener listener, Event event) throws EventException {
                    try {
                        if (!eventClass.isAssignableFrom(event.getClass())) {
                            return;
                        }
                        method.invoke(listener, event);
                    } catch (InvocationTargetException ex) {
                        throw new EventException(ex.getCause());
                    } catch (Throwable t) {
                        throw new EventException(t);
                    }
                }
            };
            getEventListeners(getRegistrationClass(eventClass)).register(new RegisteredListener(listener, executor, eh.priority(), plugin, eh.ignoreCancelled()));
        }
    }

    // taken from Spigot: SimplePluginManager
    private Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return clazz;
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null
                    && !clazz.getSuperclass().equals(Event.class)
                    && Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            } else {
                throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName() + ". Static getHandlerList method required!");
            }
        }
    }
    
    // taken from Spigot: SimplePluginManager
    private HandlerList getEventListeners(Class<? extends Event> type) {
        try {
            Method method = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
            method.setAccessible(true);
            return (HandlerList) method.invoke(null);
        } catch (Exception e) {
            throw new IllegalPluginAccessException(e.toString());
        }
    }
    
    @Override
    public void registerEvent(Class<? extends Event> event, Listener listener, EventPriority priority, EventExecutor executor, Plugin plugin)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void registerEvent(Class<? extends Event> event, Listener listener, EventPriority priority, EventExecutor executor, Plugin plugin, boolean ignoreCancelled)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void enablePlugin(Plugin plugin)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void disablePlugin(Plugin plugin)
    {
        throw new UnsupportedOperationException();
    }
    
    public Permission getPermission(String name) {
        return permissions.get(name.toLowerCase());
    }

    public void addPermission(Permission perm) {
        String name = perm.getName().toLowerCase();

        if (permissions.containsKey(name)) {
            throw new IllegalArgumentException("The permission " + name + " is already defined!");
        }

        permissions.put(name, perm);
        calculatePermissionDefault(perm);
    }

    public Set<Permission> getDefaultPermissions(boolean op) {
        return ImmutableSet.copyOf(defaultPerms.get(op));
    }

    public void removePermission(Permission perm) {
        removePermission(perm.getName());
    }

    public void removePermission(String name) {
        permissions.remove(name.toLowerCase());
    }

    public void recalculatePermissionDefaults(Permission perm) {
        if (perm != null && permissions.containsKey(perm.getName().toLowerCase())) {
            defaultPerms.get(true).remove(perm);
            defaultPerms.get(false).remove(perm);

            calculatePermissionDefault(perm);
        }
    }

    private void calculatePermissionDefault(Permission perm) {
        if ((perm.getDefault() == PermissionDefault.OP) || (perm.getDefault() == PermissionDefault.TRUE)) {
            defaultPerms.get(true).add(perm);
            dirtyPermissibles(true);
        }
        if ((perm.getDefault() == PermissionDefault.NOT_OP) || (perm.getDefault() == PermissionDefault.TRUE)) {
            defaultPerms.get(false).add(perm);
            dirtyPermissibles(false);
        }
    }

    private void dirtyPermissibles(boolean op) {
        Set<Permissible> permissibles = getDefaultPermSubscriptions(op);

        for (Permissible p : permissibles) {
            p.recalculatePermissions();
        }
    }

    public void subscribeToPermission(String permission, Permissible permissible) {
        String name = permission.toLowerCase();
        Map<Permissible, Boolean> map = permSubs.get(name);

        if (map == null) {
            map = new WeakHashMap<Permissible, Boolean>();
            permSubs.put(name, map);
        }

        map.put(permissible, true);
    }

    public void unsubscribeFromPermission(String permission, Permissible permissible) {
        String name = permission.toLowerCase();
        Map<Permissible, Boolean> map = permSubs.get(name);

        if (map != null) {
            map.remove(permissible);

            if (map.isEmpty()) {
                permSubs.remove(name);
            }
        }
    }

    public Set<Permissible> getPermissionSubscriptions(String permission) {
        String name = permission.toLowerCase();
        Map<Permissible, Boolean> map = permSubs.get(name);

        if (map == null) {
            return ImmutableSet.of();
        } else {
            return ImmutableSet.copyOf(map.keySet());
        }
    }

    public void subscribeToDefaultPerms(boolean op, Permissible permissible) {
        Map<Permissible, Boolean> map = defSubs.get(op);

        if (map == null) {
            map = new WeakHashMap<Permissible, Boolean>();
            defSubs.put(op, map);
        }

        map.put(permissible, true);
    }

    public void unsubscribeFromDefaultPerms(boolean op, Permissible permissible) {
        Map<Permissible, Boolean> map = defSubs.get(op);

        if (map != null) {
            map.remove(permissible);

            if (map.isEmpty()) {
                defSubs.remove(op);
            }
        }
    }

    public Set<Permissible> getDefaultPermSubscriptions(boolean op) {
        Map<Permissible, Boolean> map = defSubs.get(op);

        if (map == null) {
            return ImmutableSet.of();
        } else {
            return ImmutableSet.copyOf(map.keySet());
        }
    }

    public Set<Permission> getPermissions() {
        return new HashSet<Permission>(permissions.values());
    }
    
    @Override
    public boolean useTimings()
    {
        return false;
    }
    
}
