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

import org.bukkit.Location;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.api.util.function.McSupplier;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinedEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinedSpectatorsEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerLeftEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerLeftSpectatorsEvent;
import de.minigameslib.mgapi.api.obj.ArenaSignHandler;
import de.minigameslib.mgapi.api.obj.GenericSignHandler;
import de.minigameslib.mgapi.api.rules.SignRuleSetInterface;
import de.minigameslib.mgapi.api.rules.SignRuleSetType;
import de.minigameslib.mgapi.impl.MinigamesPlugin;

/**
 * @author mepeisen
 *
 */
public class GenericSign extends AbstractBaseArenaObjectHandler<SignRuleSetType, SignRuleSetInterface, GenericSignData> implements GenericSignHandler
{
    
    /** the underlying sign. */
    protected SignInterface sign;

    @Override
    public void initArena(ArenaInterface a) throws McException
    {
        super.initArena(a);
        this.dataFile = new File(MinigamesPlugin.instance().getDataFolder(), "arenas/" + this.arena.getInternalName() + '/' + this.sign.getSignId() + ".yml"); //$NON-NLS-1$ //$NON-NLS-2$
        if (this.dataFile.exists())
        {
            this.loadData();
        }
        else
        {
            this.saveData();
        }
        this.updateSign();
    }
    
    /**
     * Player joined event
     * @param evt
     */
    @McEventHandler
    public void onPlayerJoin(ArenaPlayerJoinedEvent evt)
    {
        if (evt.getArena() == this.getArena())
        {
            this.updateSign();
        }
    }
    
    /**
     * Player joined event
     * @param evt
     */
    @McEventHandler
    public void onPlayerSpecsJoin(ArenaPlayerJoinedSpectatorsEvent evt)
    {
        if (evt.getArena() == this.getArena())
        {
            this.updateSign();
        }
    }
    
    /**
     * Player left event
     * @param evt
     */
    @McEventHandler
    public void onPlayerLeft(ArenaPlayerLeftEvent evt)
    {
        if (evt.getArena() == this.getArena())
        {
            this.updateSign();
        }
    }
    
    /**
     * Player left event
     * @param evt
     */
    @McEventHandler
    public void onPlayerLeftSpecs(ArenaPlayerLeftSpectatorsEvent evt)
    {
        if (evt.getArena() == this.getArena())
        {
            this.updateSign();
        }
    }
    
    /**
     * Returns the sign text to set
     * @return sign text
     */
    protected String[] getLines()
    {
        // TODO join lines
        return new String[]{
                "GENERIC",
                this.getArena().getInternalName(),
                String.valueOf(System.currentTimeMillis())
        };
    }

    /**
     * Set sign text
     */
    private void updateSign()
    {
        final String[] lines = this.getLines();
        for (int i = 0; i < 4; i++)
        {
            if (i < lines.length)
            {
                this.sign.setLine(i, lines[i]);
            }
            else
            {
                this.sign.setLine(i, ""); //$NON-NLS-1$
            }
        }
    }

    @Override
    public void onCreate(SignInterface c) throws McException
    {
        this.sign = c;
    }

    @Override
    public void onResume(SignInterface c) throws McException
    {
        this.sign = c;
    }

    @Override
    public void onPause(SignInterface c)
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
    protected Class<GenericSignData> getDataClass()
    {
        return GenericSignData.class;
    }

    @Override
    protected GenericSignData createData()
    {
        return new GenericSignData();
    }

    @Override
    protected void applyListeners(SignRuleSetInterface listeners)
    {
        this.sign.registerHandlers(MinigamesPlugin.instance().getPlugin(), listeners);
    }

    @Override
    protected void removeListeners(SignRuleSetInterface listeners)
    {
        this.sign.unregisterHandlers(MinigamesPlugin.instance().getPlugin(), listeners);
    }

    @Override
    protected SignRuleSetInterface create(SignRuleSetType ruleset) throws McException
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
            McLibInterface.instance().setContext(ArenaSignHandler.class, this);
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
            McLibInterface.instance().setContext(ArenaSignHandler.class, this);
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
            McLibInterface.instance().setContext(ArenaSignHandler.class, this);
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
            McLibInterface.instance().setContext(ArenaSignHandler.class, this);
            return runnable.get();
        });
    }

    @Override
    public SignInterface getSign()
    {
        return this.sign;
    }

    @Override
    public void canChangeLocation(Location newValue) throws McException
    {
        this.checkModifications();
    }

    @Override
    public void onLocationChange(Location newValue)
    {
        // do nothing
    }
    
}
