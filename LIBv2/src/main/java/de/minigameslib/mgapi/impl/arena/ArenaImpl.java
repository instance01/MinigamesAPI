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

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedConfigLine;
import de.minigameslib.mclib.api.locale.LocalizedConfigString;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;
import de.minigameslib.mgapi.api.MinigameInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;
import de.minigameslib.mgapi.api.arena.CheckFailure;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.impl.internal.TaskManager;
import de.minigameslib.mgapi.impl.tasks.ArenaRestartTask;
import de.minigameslib.mgapi.impl.tasks.ArenaStartTask;

/**
 * Arena data.
 * 
 * @author mepeisen
 */
public class ArenaImpl implements ArenaInterface
{
    
    /**
     * The associated arena data
     */
    private ArenaData arenaData;
    
    /**
     * the arena data file.
     */
    private File dataFile;
    
    /** arena type. */
    private ArenaTypeInterface type;
    
    /** current arena state. */
    private ArenaState state;
    
    /** the players within this arena. */
    private final Set<UUID> players = new HashSet<>();
    
    /** the spectators. */
    private final Set<UUID> spectators = new HashSet<>();
    
    /**
     * Constructor to create an arena by using given data file.
     * @param dataFile
     * @throws McException thrown if data file is invalid.
     */
    public ArenaImpl(File dataFile) throws McException
    {
        this.dataFile = dataFile;
        try
        {
            final DataSection section = McLibInterface.instance().readYmlFile(dataFile);
            this.arenaData = section.getFragment(ArenaData.class, "data"); //$NON-NLS-1$
        }
        catch (IOException e)
        {
            throw new McException(CommonMessages.InternalError, e, e.getMessage());
        }
        
        if (!this.arenaData.isEnabled())
        {
            this.state = ArenaState.Disabled;
        }
        else if (this.arenaData.isMaintenance())
        {
            this.state = ArenaState.Maintenance;
        }
        else
        {
            this.state = ArenaState.Starting;
        }
    }
    
    /**
     * Constructor to create a new arena.
     * @param name 
     * @param type 
     * @param dataFile 
     * @throws McException thrown if data file is invalid.
     */
    public ArenaImpl(String name, ArenaTypeInterface type, File dataFile) throws McException
    {
        this.dataFile = dataFile;
        this.arenaData = new ArenaData(name, EnumServiceInterface.instance().getPlugin((Enum<?>) type).getName(), type.name());
        this.saveData();
    }
    
    /**
     * Checks if a match is pending; a flag to recover after server crashes.
     * @return {@code true} if a match is pending
     */
    public boolean isMatchPending()
    {
        return this.arenaData.isMatchPending();
    }

    @Override
    public String getInternalName()
    {
        return this.arenaData.getName();
    }
    
    @Override
    public ArenaTypeInterface getType()
    {
        return this.type;
    }

    @Override
    public LocalizedConfigString getDisplayName()
    {
        return this.arenaData.getDisplayName();
    }

    @Override
    public LocalizedConfigString getShortDescription()
    {
        return this.arenaData.getShortDescription();
    }

    @Override
    public LocalizedConfigLine getDescription()
    {
        return this.arenaData.getDescription();
    }

    @Override
    public LocalizedConfigLine getManual()
    {
        return this.arenaData.getManual();
    }

    @Override
    public void saveData() throws McException
    {
        final DataSection section = new MemoryDataSection();
        section.set("data", this.arenaData); //$NON-NLS-1$
        try
        {
            McLibInterface.instance().saveYmlFile(section, this.dataFile);
        }
        catch (IOException e)
        {
            throw new McException(CommonMessages.InternalError, e, e.getMessage());
        }
    }

    @Override
    public ArenaState getState()
    {
        return this.state;
    }

    @Override
    public void join(ArenaPlayerInterface player) throws McException
    {
        if (this.state != ArenaState.Join)
        {
            throw new McException(Messages.JoinWrongState);
        }
        
        this.players.add(player.getPlayerUUID());
        // TODO port to waiting lobby etc.
        player.getMcPlayer().sendMessage(Messages.JoinedArena, this.getDisplayName().toArg());
    }

    @Override
    public void spectate(ArenaPlayerInterface player) throws McException
    {
        switch (this.state)
        {
            case Disabled:
            case Join:
            case Maintenance:
            case Restarting:
            case Starting:
            default:
                throw new McException(Messages.SpectateWrongState);
            case Match:
            case PostMatch:
            case PreMatch:
                this.spectators.add(player.getPlayerUUID());
                // TODO port to spectator spawn etc.
                player.getMcPlayer().sendMessage(Messages.SpectatingArena, this.getDisplayName().toArg());
                break;
        }
    }

    @Override
    public void setEnabledState() throws McException
    {
        if (this.state != ArenaState.Disabled && this.state != ArenaState.Maintenance)
        {
            throw new McException(Messages.EnableWrongState);
        }
        
        this.arenaData.setEnabled(true);
        this.saveData();
        
        this.state = ArenaState.Starting;
        TaskManager.instance().queue(new ArenaStartTask(this));
    }

    @Override
    public void setDisabledState(boolean force) throws McException
    {
        switch (this.state)
        {
            default:
            case Disabled:
                throw new McException(Messages.DisableWrongState);
            case Maintenance:
            case Starting:
                this.arenaData.setMaintenance(false);
                this.arenaData.setEnabled(false);
                this.saveData();
                this.state = ArenaState.Disabled;
                break;
            case Join:
            case Match:
            case PostMatch:
            case PreMatch:
            case Restarting:
                this.arenaData.setEnabled(false);
                this.saveData();
                if (force)
                {
                    // abort current game.
                    this.abortGame(Messages.KickReasonDisable);
                    this.state = ArenaState.Restarting;
                    TaskManager.instance().queue(new ArenaRestartTask(this));
                }
                break;
        }
    }

    /**
     * Aborts a current game and kicks all players
     * @param kickReason
     */
    private void abortGame(LocalizedMessageInterface kickReason)
    {
        for (final UUID uuid : this.players)
        {
            this.kick(uuid, kickReason);
        }
        this.players.clear();
        
        for (final UUID uuid : this.spectators)
        {
            this.kick(uuid, kickReason);
        }
        this.spectators.clear();
    }

    /**
     * Kicks a single player with given reason
     * @param uuid
     * @param kickReason
     */
    private void kick(UUID uuid, LocalizedMessageInterface kickReason)
    {
        final McPlayerInterface player = ObjectServiceInterface.instance().getPlayer(uuid);
        player.sendMessage(Messages.YouWereKicked, kickReason.toArg());
    }

    @Override
    public void setMaintenance(boolean force) throws McException
    {
        switch (this.state)
        {
            default:
            case Maintenance:
                throw new McException(Messages.MaintenanceWrongState);
            case Disabled:
            case Starting:
                this.arenaData.setMaintenance(true);
                this.arenaData.setEnabled(true);
                this.saveData();
                this.state = ArenaState.Maintenance;
                break;
            case Join:
            case Match:
            case PostMatch:
            case PreMatch:
            case Restarting:
                this.arenaData.setMaintenance(true);
                this.saveData();
                if (force)
                {
                    // abort current game.
                    this.abortGame(Messages.KickReasonMaintenance);
                    this.state = ArenaState.Restarting;
                    TaskManager.instance().queue(new ArenaRestartTask(this));
                }
                break;
        }
    }

    @Override
    public boolean isMaintenance()
    {
        return this.arenaData.isMaintenance();
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaInterface#check()
     */
    @Override
    public Collection<CheckFailure> check()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns the owning plugin.
     * @return owning plugin
     */
    public Plugin getPlugin()
    {
        return this.type.getPlugin();
    }

    @Override
    public MinigameInterface getMinigame()
    {
        return this.type.getMinigame();
    }
    
    /**
     * The arena messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "arena")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Cannot join because of wrong state
         */
        @LocalizedMessage(defaultMessage = "Cannot join because arena is unavailable or a match is already running.", severity = MessageSeverityType.Error)
        @MessageComment({"Cannot join because of wrong state"})
        JoinWrongState,
        
        /**
         * Join Succeeded
         */
        @LocalizedMessage(defaultMessage = "You joined arena %1$s.", severity = MessageSeverityType.Success)
        @MessageComment(value = {"Join Succeeded"}, args = {@Argument("arena display name")})
        JoinedArena,
        
        /**
         * Cannot join because of wrong state
         */
        @LocalizedMessage(defaultMessage = "Cannot spectate because arena is unavailable or there is no pending match.", severity = MessageSeverityType.Error)
        @MessageComment({"Cannot spectate because of wrong state"})
        SpectateWrongState,
        
        /**
         * Spectate Succeeded
         */
        @LocalizedMessage(defaultMessage = "You are spectating arena %1$s.", severity = MessageSeverityType.Success)
        @MessageComment(value = {"Spectate Succeeded"}, args = {@Argument("arena display name")})
        SpectatingArena,
        
        /**
         * Cannot enable because arena is not suspended
         */
        @LocalizedMessage(defaultMessage = "Cannot enable arena because it is not suspended.", severity = MessageSeverityType.Error)
        @MessageComment({"Cannot spectate because of wrong state"})
        EnableWrongState,
        
        /**
         * Cannot disable because arena is already disabled
         */
        @LocalizedMessage(defaultMessage = "Cannot disable arena because it is already disabled.", severity = MessageSeverityType.Error)
        @MessageComment({"Cannot disable because arena is already disabled"})
        DisableWrongState,
        
        /**
         * Kick reason: Arena was disabled by admin
         */
        @LocalizedMessage(defaultMessage = "Arena was disabled by admin")
        @MessageComment({"Kick reason: arena was disabled by admin"})
        KickReasonDisable,
        
        /**
         * Cannot maintain because arena is already under maintenance
         */
        @LocalizedMessage(defaultMessage = "Cannot maintain arena because it is already under maintenance.", severity = MessageSeverityType.Error)
        @MessageComment({"Cannot maintain because arena is already under maintenance"})
        MaintenanceWrongState,
        
        /**
         * Kick reason: Arena is maintained by admin
         */
        @LocalizedMessage(defaultMessage = "Arena is going into maintenance")
        @MessageComment({"Kick reason: arena is maintained by admin"})
        KickReasonMaintenance,
        
        /**
         * You were kicked
         */
        @LocalizedMessage(defaultMessage = "You were kicked. Reason: %1$s", severity = MessageSeverityType.Error)
        @MessageComment({"You were kicked"})
        YouWereKicked,
        
    }
    
}
