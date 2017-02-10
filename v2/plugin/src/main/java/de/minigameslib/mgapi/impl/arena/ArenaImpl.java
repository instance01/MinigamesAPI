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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.locale.LocalizedConfigLine;
import de.minigameslib.mclib.api.locale.LocalizedConfigString;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.ComponentTypeId;
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.EntityIdInterface;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.api.objects.EntityTypeId;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectHandlerInterface;
import de.minigameslib.mclib.api.objects.ObjectInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.SignIdInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.SignTypeId;
import de.minigameslib.mclib.api.objects.ZoneIdInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.objects.ZoneTypeId;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;
import de.minigameslib.mgapi.api.MinigameInterface;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;
import de.minigameslib.mgapi.api.arena.CheckFailure;
import de.minigameslib.mgapi.api.arena.CheckSeverity;
import de.minigameslib.mgapi.api.events.ArenaDeleteEvent;
import de.minigameslib.mgapi.api.events.ArenaDeletedEvent;
import de.minigameslib.mgapi.api.match.ArenaMatchInterface;
import de.minigameslib.mgapi.api.obj.ArenaComponentHandler;
import de.minigameslib.mgapi.api.obj.ArenaSignHandler;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.impl.MglibObjectTypes;
import de.minigameslib.mgapi.impl.MinigamesPlugin;
import de.minigameslib.mgapi.impl.internal.TaskManager;
import de.minigameslib.mgapi.impl.rules.AbstractRuleSetContainer;
import de.minigameslib.mgapi.impl.tasks.ArenaRestartTask;
import de.minigameslib.mgapi.impl.tasks.ArenaStartTask;

/**
 * Arena data.
 * 
 * @author mepeisen
 */
public class ArenaImpl implements ArenaInterface, ObjectHandlerInterface
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
    private ArenaState state = ArenaState.Disabled;
    
    /** the players within this arena. */
    private final Set<UUID> players = new HashSet<>();
    
    /** the spectators. */
    private final Set<UUID> spectators = new HashSet<>();

    /** the mclib object */
    ObjectInterface object;
    
    /** arena logger */
    private ArenaLogger logger;
    
    /** the current arena match. */
    private ArenaMatchImpl match;
    
    /**
     * rule set container
     */
    private AbstractRuleSetContainer<ArenaRuleSetType, ArenaRuleSetInterface> ruleSets = new AbstractRuleSetContainer<ArenaRuleSetType, ArenaRuleSetInterface>() {

        @Override
        protected void checkModifications() throws McException
        {
            if (ArenaImpl.this.getState() != ArenaState.Maintenance)
            {
                throw new McException(Messages.ModificationWrongState);
            }
        }

        @Override
        protected void applyListeners(ArenaRuleSetInterface listeners)
        {
            ArenaImpl.this.object.registerHandlers(getPlugin(), listeners);
        }

        @Override
        protected void removeListeners(ArenaRuleSetInterface listeners)
        {
            ArenaImpl.this.object.unregisterHandlers(getPlugin(), listeners);
        }

        @Override
        protected ArenaRuleSetInterface create(ArenaRuleSetType ruleset) throws McException
        {
            return MinigamesPlugin.instance().creator(ruleset).apply(ruleset, ArenaImpl.this);
        }
    };
    
    /**
     * Constructor to create an arena by using given data file.
     * @param dataFile
     */
    public ArenaImpl(File dataFile)
    {
        this.dataFile = dataFile;
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
        this.logger = new ArenaLogger(name);
        this.dataFile = dataFile;
        this.arenaData = new ArenaData(name, type);
        this.arenaData.setMaintenance(true);
        this.state = ArenaState.Maintenance;
        this.object = ObjectServiceInterface.instance().createObject(MglibObjectTypes.Arena, this, false);
        try
        {
            for (final ArenaRuleSetType ruleset : type.safeCreateProvider().getFixedArenaRules())
            {
                this.ruleSets.applyFixedRuleSet(ruleset);
                this.arenaData.getFixedRules().add(ruleset);
            }
        }
        catch (McException ex)
        {
            this.object.delete();
            this.object = null;
            throw ex;
        }
        this.saveData();
    }
    
    /**
     * Resume the arena and load arena data file.
     * @throws McException thrown if data file is invalid.
     */
    public void resume() throws McException
    {
        try
        {
            final DataSection section = McLibInterface.instance().readYmlFile(this.dataFile);
            this.arenaData = section.getFragment(ArenaData.class, "data"); //$NON-NLS-1$
            this.logger = new ArenaLogger(this.getInternalName());
            this.type = this.arenaData.getArenaType();
            this.object = ObjectServiceInterface.instance().createObject(MglibObjectTypes.Arena, this, false);
            try
            {
                resumeRuleSets();
                resumeComponents();
                resumeSigns();
                resumeZones();
            }
            catch (McException ex)
            {
                this.object.delete();
                this.object = null;
                throw ex;
            }
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
     * @throws McException
     */
    private void resumeComponents() throws McException
    {
        for (final ComponentIdInterface id : this.getComponents())
        {
            final ArenaComponentHandler handler = (ArenaComponentHandler) ObjectServiceInterface.instance().findComponent(id);
            handler.initArena(this);
        }
    }

    /**
     * @throws McException
     */
    private void resumeSigns() throws McException
    {
        for (final SignIdInterface id : this.getSigns())
        {
            final ArenaSignHandler handler = (ArenaSignHandler) ObjectServiceInterface.instance().findSign(id);
            handler.initArena(this);
        }
    }

    /**
     * @throws McException
     */
    private void resumeZones() throws McException
    {
        for (final ZoneIdInterface id : this.getZones())
        {
            final ArenaZoneHandler handler = (ArenaZoneHandler) ObjectServiceInterface.instance().findZone(id);
            handler.initArena(this);
        }
    }

    /**
     * @throws McException
     */
    private void resumeRuleSets() throws McException
    {
        final Set<ArenaRuleSetType> oldfixed = new HashSet<>(this.arenaData.getFixedRules());
        for (final ArenaRuleSetType ruleset : this.type.safeCreateProvider().getFixedArenaRules())
        {
            if (!oldfixed.remove(ruleset))
            {
                this.arenaData.getOptionalRules().remove(ruleset);
                this.arenaData.getFixedRules().add(ruleset);
                this.saveData();
            }
            this.ruleSets.applyFixedRuleSet(ruleset);
        }
        for (final ArenaRuleSetType ruleset : oldfixed)
        {
            this.arenaData.getFixedRules().remove(ruleset);
            this.arenaData.getOptionalRules().add(ruleset);
            this.saveData();
        }
        for (final ArenaRuleSetType ruleset :  this.arenaData.getOptionalRules())
        {
            this.ruleSets.applyOptionalRuleSet(ruleset);
        }
    }
    
    @Override
    public Logger getLogger()
    {
        return this.logger;
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
    public void leave(ArenaPlayerInterface player) throws McException
    {
        final UUID uuid = player.getPlayerUUID();
        if (this.players.contains(uuid))
        {
            this.leaveMatch(player);
        }
        else if (this.spectators.contains(uuid))
        {
            this.leaveSpec(player);
        }
        else
        {
            throw new McException(Messages.CannotLeaveNotInArena, this.getDisplayName());
        }
    }

    /**
     * Leave spectator mode
     * @param player
     */
    private void leaveSpec(ArenaPlayerInterface player)
    {
        ((ArenaPlayerImpl)player).switchArenaOrMode(null, false);
        // TODO port to main lobby....
        this.spectators.remove(player.getPlayerUUID());
        player.getMcPlayer().sendMessage(Messages.YouLeft, this.getDisplayName());
    }

    /**
     * Leaves a running match
     * @param player
     */
    private void leaveMatch(ArenaPlayerInterface player)
    {
        ((ArenaPlayerImpl)player).switchArenaOrMode(null, false);
        // TODO port to main lobby....
        this.players.remove(player.getPlayerUUID());
        player.getMcPlayer().sendMessage(Messages.YouLeft, this.getDisplayName());
    }

    @Override
    public void join(ArenaPlayerInterface player) throws McException
    {
        if (player.inArena())
        {
            throw new McException(Messages.AlreadyInArena, player.getArena().getDisplayName());
        }
        if (this.state != ArenaState.Join)
        {
            throw new McException(Messages.JoinWrongState);
        }
        
        this.players.add(player.getPlayerUUID());
        ((ArenaPlayerImpl)player).switchArenaOrMode(this.getInternalName(), false);
        // TODO port to waiting lobby etc.
        player.getMcPlayer().sendMessage(Messages.JoinedArena, this.getDisplayName());
    }

    @Override
    public void spectate(ArenaPlayerInterface player) throws McException
    {
        if (player.inArena())
        {
            throw new McException(Messages.AlreadyInArena, player.getArena().getDisplayName());
        }
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
                ((ArenaPlayerImpl)player).switchArenaOrMode(this.getInternalName(), true);
                // TODO port to spectator spawn etc.
                player.getMcPlayer().sendMessage(Messages.SpectatingArena, this.getDisplayName());
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
        if (this.arenaData.getArenaType() == null)
        {
            throw new McException(Messages.InvalidArenaType);
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
                this.arenaData.setEnabled(false);
                this.saveData();
                // abort current game to kick players being in waiting lobby.
                this.abortGame(Messages.KickReasonDisable);
                this.state = ArenaState.Restarting;
                TaskManager.instance().queue(new ArenaRestartTask(this));
                break;
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
        // TODO port to main lobby....
        ((ArenaPlayerImpl)MinigamesLibInterface.instance().getPlayer(player)).switchArenaOrMode(null, false);
        player.sendMessage(Messages.YouWereKicked, kickReason);
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
                if (this.arenaData.getArenaType() == null)
                {
                    throw new McException(Messages.InvalidArenaType);
                }
                //$FALL-THROUGH$
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
    public void start() throws McException
    {
        if (this.state != ArenaState.Join)
        {
            throw new McException(Messages.StartWrongState);
        }
        this.state = ArenaState.PreMatch;
        // TODO Start pre match phase, teleport to spawns etc.
    }

    @Override
    public void setTestState() throws McException
    {
        if (this.state != ArenaState.Maintenance)
        {
            throw new McException(Messages.TestWrongState);
        }
        for (final CheckFailure failure : this.check())
        {
            if (failure.getSeverity() == CheckSeverity.Error)
            {
                throw new McException(Messages.TestCheckFailure);
            }
        }
        this.state = ArenaState.Join;
    }

    @Override
    public boolean isMaintenance()
    {
        return this.arenaData.isMaintenance();
    }

    @Override
    public boolean isDisabled()
    {
        return !this.arenaData.isEnabled();
    }

    @Override
    public boolean isMatch()
    {
        switch (this.state)
        {
            case Join:
            case Disabled:
            case Maintenance:
            case Restarting:
            case Starting:
            default:
                return false;
            case Match:
            case PostMatch:
            case PreMatch:
                return true;
        }
    }

    @Override
    public void delete() throws McException
    {
        this.object.delete();
    }

    /**
     * Do deletion
     */
    private void delete0()
    {
        try
        {
            final ObjectServiceInterface osi = ObjectServiceInterface.instance();
            for (final ComponentIdInterface id : this.getComponents())
            {
                osi.findComponent(id).delete();
            }
            for (final SignIdInterface id : this.getSigns())
            {
                osi.findSign(id).delete();
            }
            for (final ZoneIdInterface id : this.getZones())
            {
                osi.findZone(id).delete();
            }
            for (final EntityIdInterface id : this.getEntities())
            {
                osi.findEntity(id).delete();
            }
        }
        catch (McException ex)
        {
            // should never happen because we checked deletion in canDelete
            throw new IllegalStateException(ex);
        }
        
        final ArenaDeletedEvent deletedEvent = new ArenaDeletedEvent(this);
        Bukkit.getPluginManager().callEvent(deletedEvent);
    }

    @Override
    public Collection<CheckFailure> check()
    {
        // TODO Auto-generated method stub
        return Collections.emptyList();
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

    @Override
    public int getPlayerCount()
    {
        return this.players.size();
    }

    @Override
    public int getSpectatorCount()
    {
        return this.spectators.size();
    }

    @Override
    public Collection<ArenaPlayerInterface> getPlayers()
    {
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        final MinigamesLibInterface mglib = MinigamesLibInterface.instance();
        return this.players.stream().map(osi::getPlayer).map(mglib::getPlayer).collect(Collectors.toList());
    }

    @Override
    public Collection<ArenaPlayerInterface> getSpectators()
    {
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        final MinigamesLibInterface mglib = MinigamesLibInterface.instance();
        return this.spectators.stream().map(osi::getPlayer).map(mglib::getPlayer).collect(Collectors.toList());
    }

    @Override
    public boolean isPlaying(McPlayerInterface player)
    {
        return this.players.contains(player.getPlayerUUID());
    }

    @Override
    public boolean isSpectating(McPlayerInterface player)
    {
        return this.spectators.contains(player.getPlayerUUID());
    }

    @Override
    public boolean isPlaying(ArenaPlayerInterface player)
    {
        return this.players.contains(player.getPlayerUUID());
    }

    @Override
    public boolean isSpectating(ArenaPlayerInterface player)
    {
        return this.spectators.contains(player.getPlayerUUID());
    }

    @Override
    public void read(DataSection section)
    {
        // not used
    }

    @Override
    public void write(DataSection section)
    {
        // not used
    }

    @Override
    public boolean test(DataSection section)
    {
        // not used
        return false;
    }

    @Override
    public void canDelete() throws McException
    {
        if (ArenaImpl.this.getState() != ArenaState.Maintenance)
        {
            throw new McException(Messages.ModificationWrongState);
        }
        
        final ArenaDeleteEvent deleteEvent = new ArenaDeleteEvent(this);
        Bukkit.getPluginManager().callEvent(deleteEvent);
        if (deleteEvent.isCancelled())
        {
            throw new McException(deleteEvent.getVetoReason(), deleteEvent.getVetoReasonArgs());
        }
        
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        for (final ComponentIdInterface id : this.getComponents())
        {
            osi.findComponent(id).getHandler().canDelete();
        }
        for (final SignIdInterface id : this.getSigns())
        {
            osi.findSign(id).getHandler().canDelete();
        }
        for (final ZoneIdInterface id : this.getZones())
        {
            osi.findZone(id).getHandler().canDelete();
        }
        for (final EntityIdInterface id : this.getEntities())
        {
            osi.findEntity(id).getHandler().canDelete();
        }
    }

    @Override
    public void onCreate(ObjectInterface arg0) throws McException
    {
        this.object = arg0;
    }

    @Override
    public void onDelete()
    {
        this.delete0();
    }

    @Override
    public void onPause(ObjectInterface arg0)
    {
        // not used
    }

    @Override
    public void onResume(ObjectInterface arg0) throws McException
    {
        // not used
    }

    @Override
    public ObjectInterface getObject()
    {
        return this.object;
    }

    @Override
    public ArenaRuleSetInterface getRuleSet(ArenaRuleSetType t)
    {
        return this.ruleSets.getRuleSet(t);
    }

    @Override
    public Collection<ArenaRuleSetType> getAppliedRuleSetTypes()
    {
        return this.ruleSets.getAppliedRuleSetTypes();
    }

    @Override
    public Collection<ArenaRuleSetType> getAvailableRuleSetTypes()
    {
        // TODO Auto-generated method stub
        return Collections.emptyList();
    }

    @Override
    public boolean isFixed(ArenaRuleSetType ruleset)
    {
        return this.ruleSets.isFixed(ruleset);
    }

    @Override
    public boolean isOptional(ArenaRuleSetType ruleset)
    {
        return this.ruleSets.isOptional(ruleset);
    }

    @Override
    public boolean isApplied(ArenaRuleSetType ruleset)
    {
        return this.ruleSets.isApplied(ruleset);
    }

    @Override
    public boolean isAvailable(ArenaRuleSetType ruleset)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void reconfigure(ArenaRuleSetType... rulesets) throws McException
    {
        for (final ArenaRuleSetType t : rulesets)
        {
            this.ruleSets.reapplyRuleSet(t);
        }
    }

    @Override
    public void applyRuleSets(ArenaRuleSetType... rulesets) throws McException
    {
        for (final ArenaRuleSetType t : rulesets)
        {
            if (!this.ruleSets.isApplied(t))
            {
                this.ruleSets.applyOptionalRuleSet(t);
                this.arenaData.getOptionalRules().add(t);
                this.saveData();
            }
        }
    }

    @Override
    public void removeRuleSets(ArenaRuleSetType... rulesets) throws McException
    {
        for (final ArenaRuleSetType t : rulesets)
        {
            if (this.ruleSets.isOptional(t))
            {
                this.ruleSets.removeOptionalRuleSet(t);
                this.arenaData.getOptionalRules().remove(t);
                this.saveData();
            }
        }
    }

    @Override
    public Collection<ComponentIdInterface> getComponents()
    {
        return new ArrayList<>(this.arenaData.getComponents());
    }

    @Override
    public Collection<ZoneIdInterface> getZones()
    {
        return new ArrayList<>(this.arenaData.getZones());
    }

    @Override
    public Collection<SignIdInterface> getSigns()
    {
        return new ArrayList<>(this.arenaData.getSigns());
    }

    @Override
    public Collection<EntityIdInterface> getEntities()
    {
        return new ArrayList<>(this.arenaData.getEntities());
    }

    @Override
    public Collection<ComponentIdInterface> getComponents(ComponentTypeId... types)
    {
        final Set<ComponentIdInterface> ids = this.arenaData.getComponents();
        return ObjectServiceInterface.instance().findComponents(types).stream().map(ComponentInterface::getComponentId).filter(ids::contains).collect(Collectors.toList());
    }

    @Override
    public Collection<ZoneIdInterface> getZones(ZoneTypeId... types)
    {
        final Set<ZoneIdInterface> ids = this.arenaData.getZones();
        return ObjectServiceInterface.instance().findZones(types).stream().map(ZoneInterface::getZoneId).filter(ids::contains).collect(Collectors.toList());
    }

    @Override
    public Collection<SignIdInterface> getSigns(SignTypeId... types)
    {
        final Set<SignIdInterface> ids = this.arenaData.getSigns();
        return ObjectServiceInterface.instance().findSigns(types).stream().map(SignInterface::getSignId).filter(ids::contains).collect(Collectors.toList());
    }

    @Override
    public Collection<EntityIdInterface> getEntities(EntityTypeId... types)
    {
        final Set<EntityIdInterface> ids = this.arenaData.getEntities();
        return ObjectServiceInterface.instance().findEntities(types).stream().map(EntityInterface::getEntityId).filter(ids::contains).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ArenaComponentHandler> T getHandler(ComponentIdInterface id)
    {
        return (T) ObjectServiceInterface.instance().findComponent(id).getHandler();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ArenaSignHandler> T getHandler(SignIdInterface id)
    {
        return (T) ObjectServiceInterface.instance().findSign(id).getHandler();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ArenaZoneHandler> T getHandler(ZoneIdInterface id)
    {
        return (T) ObjectServiceInterface.instance().findZone(id).getHandler();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ArenaComponentHandler> T createComponent(Location location, ComponentTypeId t) throws McException
    {
        if (this.getState() != ArenaState.Maintenance)
        {
            throw new McException(Messages.ModificationWrongState);
        }
        final ArenaComponentHandler handler = MinigamesPlugin.instance().creator(t).get();
        final ComponentInterface component = ObjectServiceInterface.instance().createComponent(t, location, handler, true);
        handler.initArena(this);
        this.arenaData.getComponents().add(component.getComponentId());
        this.saveData();
        return (T) handler;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ArenaSignHandler> T createSign(Sign sign, SignTypeId t) throws McException
    {
        if (this.getState() != ArenaState.Maintenance)
        {
            throw new McException(Messages.ModificationWrongState);
        }
        final ArenaSignHandler handler = MinigamesPlugin.instance().creator(t).get();
        final SignInterface mcsign = ObjectServiceInterface.instance().createSign(t, sign, handler, true);
        handler.initArena(this);
        this.arenaData.getSigns().add(mcsign.getSignId());
        this.saveData();
        return (T) handler;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ArenaZoneHandler> T createZone(Cuboid cuboid, ZoneTypeId t) throws McException
    {
        if (this.getState() != ArenaState.Maintenance)
        {
            throw new McException(Messages.ModificationWrongState);
        }
        final ArenaZoneHandler handler = MinigamesPlugin.instance().creator(t).get();
        final ZoneInterface zone = ObjectServiceInterface.instance().createZone(t, cuboid, handler, true);
        handler.initArena(this);
        this.arenaData.getZones().add(zone.getZoneId());
        this.saveData();
        return (T) handler;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaInterface#getCurrentMatch()
     */
    @Override
    public ArenaMatchInterface getCurrentMatch()
    {
        // TODO Auto-generated method stub
        return null;
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
         * Invalid arena type detected
         */
        @LocalizedMessage(defaultMessage = "Invalid arena type detected. Did you uninstall/deactivate the minigame plugin?", severity = MessageSeverityType.Error)
        @MessageComment({"Invalid arena type detected"})
        InvalidArenaType,
        
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
         * Cannot start test match because arena is not in maintenance mode
         */
        @LocalizedMessage(defaultMessage = "Cannot start test match because arena ist not in maintenance.", severity = MessageSeverityType.Error)
        @MessageComment({"Cannot start test match because arena is not in maintenance mode"})
        TestWrongState,
        
        /**
         * Cannot modify arena because of wrong state
         */
        @LocalizedMessage(defaultMessage = "Cannot modify arena because of wrong state.", severity = MessageSeverityType.Error)
        @MessageComment({"Cannot modify arena because of wrong state"})
        ModificationWrongState,
        
        /**
         * Cannot start match because arena is not in join mode
         */
        @LocalizedMessage(defaultMessage = "Cannot start match because arena ist not in join mode.", severity = MessageSeverityType.Error)
        @MessageComment({"Cannot start test match because arena is not in join mode"})
        StartWrongState,
        
        /**
         * Cannot start test match because arena has errors
         */
        @LocalizedMessage(defaultMessage = "Cannot start test match because arena has errors.", severity = MessageSeverityType.Error)
        @MessageComment({"Cannot start test match because arena has errors"})
        TestCheckFailure,
        
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
        @MessageComment(value = {"You were kicked"}, args = @Argument("reason text"))
        YouWereKicked,
        
        /**
         * You cannot leave because not in arena
         */
        @LocalizedMessage(defaultMessage = "You cannot leave because you are not within arena %1$s.", severity = MessageSeverityType.Error)
        @MessageComment(value = {"You cannot leave because not in arena"}, args = @Argument("arena display name"))
        CannotLeaveNotInArena,
        
        /**
         * You are already in arena
         */
        @LocalizedMessage(defaultMessage = "You cannot join because you are in arena %1$s.", severity = MessageSeverityType.Error)
        @MessageComment(value = {"You are already in arena"}, args = @Argument("arena display name"))
        AlreadyInArena,
        
        /**
         * You left the arena
         */
        @LocalizedMessage(defaultMessage = "You left arena %1$s.", severity = MessageSeverityType.Error)
        @MessageComment(value = {"You left the arena"}, args = @Argument("arena display name"))
        YouLeft,
        
    }
    
}
