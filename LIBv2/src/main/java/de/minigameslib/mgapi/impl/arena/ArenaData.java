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

package de.minigameslib.mgapi.impl.arena;

import de.minigameslib.mclib.api.locale.LocalizedConfigLine;
import de.minigameslib.mclib.api.locale.LocalizedConfigString;
import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * @author mepeisen
 */
public class ArenaData extends AnnotatedDataFragment
{
    
    /**
     * Arena name.
     */
    @PersistentField
    private String name;
    
    /**
     * Display name
     */
    @PersistentField
    private LocalizedConfigString displayName = new LocalizedConfigString();
    
    /**
     * Short arena description
     */
    @PersistentField
    private LocalizedConfigString shortDescription = new LocalizedConfigString();
    
    /**
     * long (multiline) arena description
     */
    @PersistentField
    private LocalizedConfigLine description = new LocalizedConfigLine();
    
    /**
     * arena manual
     */
    @PersistentField
    private LocalizedConfigLine manual = new LocalizedConfigLine();
    
    /**
     * plugin name
     */
    @PersistentField
    private String pluginName;
    
    /**
     * arena type
     */
    @PersistentField
    private String arenaType;
    
    /**
     * flag to control if arena is enabled.
     */
    @PersistentField
    private boolean isEnabled;
    
    /**
     * flag to control if arena is in maintenance mode.
     */
    @PersistentField
    private boolean isMaintenance;

    /**
     * @param name
     * @param pluginName
     * @param arenaType
     */
    public ArenaData(String name, String pluginName, String arenaType)
    {
        this.name = name;
        this.pluginName = pluginName;
        this.arenaType = arenaType;
    }

    /**
     * @return the displayName
     */
    public LocalizedConfigString getDisplayName()
    {
        return this.displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(LocalizedConfigString displayName)
    {
        this.displayName = displayName;
    }

    /**
     * @return the shortDescription
     */
    public LocalizedConfigString getShortDescription()
    {
        return this.shortDescription;
    }

    /**
     * @param shortDescription the shortDescription to set
     */
    public void setShortDescription(LocalizedConfigString shortDescription)
    {
        this.shortDescription = shortDescription;
    }

    /**
     * @return the description
     */
    public LocalizedConfigLine getDescription()
    {
        return this.description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(LocalizedConfigLine description)
    {
        this.description = description;
    }

    /**
     * @return the manual
     */
    public LocalizedConfigLine getManual()
    {
        return this.manual;
    }

    /**
     * @param manual the manual to set
     */
    public void setManual(LocalizedConfigLine manual)
    {
        this.manual = manual;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @return the pluginName
     */
    public String getPluginName()
    {
        return this.pluginName;
    }

    /**
     * @return the arenaType
     */
    public String getArenaType()
    {
        return this.arenaType;
    }

    /**
     * @return the isEnabled
     */
    public boolean isEnabled()
    {
        return this.isEnabled;
    }

    /**
     * @param isEnabled the isEnabled to set
     */
    public void setEnabled(boolean isEnabled)
    {
        this.isEnabled = isEnabled;
    }

    /**
     * @return the isMaintenance
     */
    public boolean isMaintenance()
    {
        return this.isMaintenance;
    }

    /**
     * @param isMaintenance the isMaintenance to set
     */
    public void setMaintenance(boolean isMaintenance)
    {
        this.isMaintenance = isMaintenance;
    }
    
}
