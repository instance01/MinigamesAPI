/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-signed commercial license from minigames.de
    you are not allowed to use this software in any way except using
    GPL (see below).

------

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

package de.minigameslib.mgapi.impl.obj;

import java.io.File;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.api.util.function.McSupplier;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.api.obj.JoinZoneHandler;
import de.minigameslib.mgapi.api.rules.ZoneRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ZoneRuleSetType;
import de.minigameslib.mgapi.impl.MinigamesPlugin;

/**
 * @author mepeisen
 *
 */
public class JoinZone extends AbstractBaseArenaObjectHandler<ZoneRuleSetType, ZoneRuleSetInterface, JoinZoneData> implements JoinZoneHandler
{
    
    /** the underlying Zone. */
    protected ZoneInterface zone;

    @Override
    public void initArena(ArenaInterface a) throws McException
    {
        super.initArena(a);
        this.dataFile = new File(MinigamesPlugin.instance().getDataFolder(), "arenas/" + this.arena.getInternalName() + '/' + this.zone.getZoneId() + ".yml"); //$NON-NLS-1$ //$NON-NLS-2$
        if (this.dataFile.exists())
        {
            this.loadData();
        }
        else
        {
            this.saveData();
        }
    }

    @Override
    public void onCreate(ZoneInterface c) throws McException
    {
        this.zone = c;
    }

    @Override
    public void onResume(ZoneInterface c) throws McException
    {
        this.zone = c;
    }

    @Override
    public void onPause(ZoneInterface c)
    {
        // do nothing
    }

    @Override
    public void canDelete() throws McException
    {
        this.checkModifications();
    }

    @Override
    public void onDelete()
    {
        if (this.dataFile.exists())
        {
            this.dataFile.delete();
        }
    }

    @Override
    public void canChangeCuboid(Cuboid newValue) throws McException
    {
        this.checkModifications();
    }

    @Override
    public void onCuboidChange(Cuboid newValue)
    {
        // do nothing
    }

    @Override
    public void read(DataSection section)
    {
        // no additional data in mclib files
    }

    @Override
    public void write(DataSection section)
    {
        // no additional data in mclib files
    }

    @Override
    public boolean test(DataSection section)
    {
        // no additional data in mclib files
        return true;
    }

    @Override
    protected Class<JoinZoneData> getDataClass()
    {
        return JoinZoneData.class;
    }

    @Override
    protected JoinZoneData createData()
    {
        return new JoinZoneData();
    }

    @Override
    protected void applyListeners(ZoneRuleSetInterface listeners)
    {
        this.zone.registerHandlers(MinigamesPlugin.instance().getPlugin(), listeners);
    }

    @Override
    protected void removeListeners(ZoneRuleSetInterface listeners)
    {
        this.zone.unregisterHandlers(MinigamesPlugin.instance().getPlugin(), listeners);
    }

    @Override
    protected ZoneRuleSetInterface create(ZoneRuleSetType ruleset) throws McException
    {
        return calculateInCopiedContext(() -> {
            return MinigamesPlugin.instance().creator(ruleset).apply(ruleset, this);
        });
    }
    
    /**
     * Runs the code in new context; changes made inside the runnable will be undone.
     * 
     * @param runnable
     *            the runnable to execute.
     * @throws McException
     *             rethrown from runnable.
     */
    void runInNewContext(McRunnable runnable) throws McException
    {
        McLibInterface.instance().runInNewContext(() -> {
            McLibInterface.instance().setContext(ArenaInterface.class, this.arena);
            McLibInterface.instance().setContext(ArenaZoneHandler.class, this);
            runnable.run();
        });
    }
    
    /**
     * Runs the code in new context but copies all context variables before; changes made inside the runnable will be undone.
     * 
     * @param runnable
     *            the runnable to execute.
     * @throws McException
     *             rethrown from runnable.
     */
    void runInCopiedContext(McRunnable runnable) throws McException
    {
        McLibInterface.instance().runInCopiedContext(() -> {
            McLibInterface.instance().setContext(ArenaInterface.class, this.arena);
            McLibInterface.instance().setContext(ArenaZoneHandler.class, this);
            runnable.run();
        });
    }
    
    /**
     * Runs the code in new context; changes made inside the runnable will be undone.
     * 
     * @param runnable
     *            the runnable to execute.
     * @return result from runnable
     * @throws McException
     *             rethrown from runnable.
     * @param <T>
     *            Type of return value
     */
    <T> T calculateInNewContext(McSupplier<T> runnable) throws McException
    {
        return McLibInterface.instance().calculateInNewContext(() -> {
            McLibInterface.instance().setContext(ArenaInterface.class, this.arena);
            McLibInterface.instance().setContext(ArenaZoneHandler.class, this);
            return runnable.get();
        });
    }
    
    /**
     * Runs the code but copies all context variables before; changes made inside the runnable will be undone.
     * 
     * @param runnable
     *            the runnable to execute.
     * @return result from runnable
     * @throws McException
     *             rethrown from runnable.
     * @param <T>
     *            Type of return value
     */
    <T> T calculateInCopiedContext(McSupplier<T> runnable) throws McException
    {
        return McLibInterface.instance().calculateInCopiedContext(() -> {
            McLibInterface.instance().setContext(ArenaInterface.class, this.arena);
            McLibInterface.instance().setContext(ArenaZoneHandler.class, this);
            return runnable.get();
        });
    }

    @Override
    public ZoneInterface getZone()
    {
        return this.zone;
    }
    
}
