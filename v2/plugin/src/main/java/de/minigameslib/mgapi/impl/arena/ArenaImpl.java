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
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
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
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.api.util.function.McSupplier;
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
import de.minigameslib.mgapi.api.events.ArenaForceStartRequestedEvent;
import de.minigameslib.mgapi.api.events.ArenaStateChangedEvent;
import de.minigameslib.mgapi.api.match.ArenaMatchInterface;
import de.minigameslib.mgapi.api.match.MatchPlayerInterface;
import de.minigameslib.mgapi.api.obj.ArenaComponentHandler;
import de.minigameslib.mgapi.api.obj.ArenaSignHandler;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.api.obj.BasicComponentTypes;
import de.minigameslib.mgapi.api.obj.SpectatorSpawnComponentHandler;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.team.ArenaTeamInterface;
import de.minigameslib.mgapi.api.team.TeamIdType;
import de.minigameslib.mgapi.impl.MglibObjectTypes;
import de.minigameslib.mgapi.impl.MinigamesPlugin;
import de.minigameslib.mgapi.impl.arena.ArenaData.TeamData;
import de.minigameslib.mgapi.impl.internal.TaskManager;
import de.minigameslib.mgapi.impl.rules.AbstractRuleSetContainer;
import de.minigameslib.mgapi.impl.tasks.AsyncArenaRestartTask;
import de.minigameslib.mgapi.impl.tasks.AsyncArenaStartTask;

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

    /** the mclib object */
    ObjectInterface object;
    
    /** arena logger */
    private ArenaLogger logger;
    
    /** the current arena match. */
    private ArenaMatchImpl match;
    
    /** current random. */
    private Random random = new Random();
    
    /**
     * rule set container
     */
    private AbstractRuleSetContainer<ArenaRuleSetType, ArenaRuleSetInterface> ruleSets = new AbstractRuleSetContainer<ArenaRuleSetType, ArenaRuleSetInterface>() {

        @Override
        protected void checkModifications() throws McException
        {
            if (ArenaImpl.this.getState() != ArenaState.Maintenance && ArenaImpl.this.getState() != ArenaState.Booting)
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
            return ArenaImpl.this.calculateInCopiedContext(() -> {
                return MinigamesPlugin.instance().creator(ruleset).apply(ruleset, ArenaImpl.this);
            });
        }
    };
    
    /**
     * Constructor to create an arena by using given data file.
     * @param name 
     * @param dataFile
     */
    public ArenaImpl(String name, File dataFile)
    {
        this.dataFile = dataFile;
        this.state = ArenaState.Booting;
        this.arenaData = new ArenaData(name, null);
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
    
    @Override
    public void checkModifications() throws McException
    {
        if (this.getState() != ArenaState.Maintenance)
        {
            throw new McException(Messages.ModificationWrongState);
        }
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
            if (this.type == null)
            {
                throw new McException(CommonMessages.InternalError, "Missing arena type. Did you install the minigame plugin? Arena: " + this.getInternalName()); //$NON-NLS-1$
            }
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
                this.logger.log(Level.WARNING, "Error while resume", ex); //$NON-NLS-1$
                try
                {
                    this.object.delete();
                }
                catch (RuntimeException ex2)
                {
                    this.logger.log(Level.WARNING, "Error while deleting", ex2); //$NON-NLS-1$
                }
                this.object = null;
                throw ex;
            }
            catch (RuntimeException ex)
            {
                this.logger.log(Level.WARNING, "Error while resume", ex); //$NON-NLS-1$
                try
                {
                    this.object.delete();
                }
                catch (RuntimeException ex2)
                {
                    this.logger.log(Level.WARNING, "Error while deleting", ex2); //$NON-NLS-1$
                }
                this.object = null;
                throw new McException(CommonMessages.InternalError, ex, ex.getMessage());
            }
        }
        catch (IOException | RuntimeException e)
        {
            throw new McException(CommonMessages.InternalError, e, e.getMessage());
        }
        
        if (!this.arenaData.isEnabled())
        {
            final ArenaStateChangedEvent changedEvent = new ArenaStateChangedEvent(this, this.state, ArenaState.Disabled);
            this.state = ArenaState.Disabled;
            Bukkit.getPluginManager().callEvent(changedEvent);
        }
        else if (this.arenaData.isMaintenance())
        {
            final ArenaStateChangedEvent changedEvent = new ArenaStateChangedEvent(this, this.state, ArenaState.Maintenance);
            this.state = ArenaState.Maintenance;
            Bukkit.getPluginManager().callEvent(changedEvent);
        }
        else
        {
            final ArenaStateChangedEvent changedEvent = new ArenaStateChangedEvent(this, this.state, ArenaState.Starting);
            this.state = ArenaState.Starting;
            Bukkit.getPluginManager().callEvent(changedEvent);
        }
    }

    /**
     * @throws McException
     */
    private void resumeComponents() throws McException
    {
        for (final ComponentIdInterface id : this.getComponents())
        {
            final ComponentInterface comp = ObjectServiceInterface.instance().findComponent(id);
            if (comp == null)
            {
                this.logger.warning("Unable to find arena component " + id); //$NON-NLS-1$
            }
            else
            {
                final ArenaComponentHandler handler = (ArenaComponentHandler) comp.getHandler();
                handler.initArena(this);
            }
        }
    }

    /**
     * @throws McException
     */
    private void resumeSigns() throws McException
    {
        for (final SignIdInterface id : this.getSigns())
        {
            final SignInterface sign = ObjectServiceInterface.instance().findSign(id);
            if (sign == null)
            {
                this.logger.warning("Unable to find arena sign " + id); //$NON-NLS-1$
            }
            else
            {
                final ArenaSignHandler handler = (ArenaSignHandler) sign.getHandler();
                handler.initArena(this);
            }
        }
    }

    /**
     * @throws McException
     */
    private void resumeZones() throws McException
    {
        for (final ZoneIdInterface id : this.getZones())
        {
            final ZoneInterface zone = ObjectServiceInterface.instance().findZone(id);
            if (zone == null)
            {
                this.logger.warning("Unable to find arena zone " + id); //$NON-NLS-1$
            }
            else
            {
                final ArenaZoneHandler handler = (ArenaZoneHandler) zone.getHandler();
                handler.initArena(this);
            }
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
                this.saveDataInternal();
            }
            this.ruleSets.applyFixedRuleSet(ruleset);
        }
        for (final ArenaRuleSetType ruleset : oldfixed)
        {
            this.arenaData.getFixedRules().remove(ruleset);
            this.arenaData.getOptionalRules().add(ruleset);
            this.saveDataInternal();
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
        if (this.state == ArenaState.Booting)
        {
            // booting arenas will never be ready for manipulation or save.
            throw new McException(Messages.InvalidArenaType);
        }
        saveDataInternal();
    }

    /**
     * @throws McException
     */
    private void saveDataInternal() throws McException
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
        if (this.match == null)
        {
            throw new McException(Messages.CannotLeaveNotInArena, this.getDisplayName());
        }
        
        final MatchPlayerInterface mp = this.match.get(uuid);
        if (mp == null || (!mp.isPlaying() && !mp.isSpec()))
        {
            throw new McException(Messages.CannotLeaveNotInArena, this.getDisplayName());
        }
        
        this.match.leave(player);
        
        player.getMcPlayer().sendMessage(Messages.YouLeft, this.getDisplayName());
        // port to main lobby
        this.teleportRandom(player, this.getComponents(BasicComponentTypes.MainLobbySpawn));
    }

    /**
     * Port player to a random component taken from given list
     * @param player
     * @param components
     * @return {@code true} if player was ported
     */
    private boolean teleportRandom(ArenaPlayerInterface player, Collection<?> components)
    {
        if (components.size() > 0)
        {
            final Object id = components.stream().skip(this.random.nextInt(components.size())).findFirst().get();
            ComponentInterface comp = null;
            if (id instanceof ComponentInterface)
            {
                comp = (ComponentInterface) id;
            }
            else
            {
                comp = ObjectServiceInterface.instance().findComponent((ComponentIdInterface) id);
            }
            player.getMcPlayer().getBukkitPlayer().teleport(comp.getLocation());
            return true;
        }
        return false;
    }

    /**
     * Port player to a component
     * @param player
     * @param component
     */
    private void teleport(ArenaPlayerInterface player, ComponentIdInterface component)
    {
        if (component != null)
        {
            player.getMcPlayer().getBukkitPlayer().teleport(ObjectServiceInterface.instance().findComponent(component).getLocation());
        }
    }

    /**
     * Port player to a component
     * @param player
     * @param component
     */
    private void teleport(ArenaPlayerInterface player, ComponentInterface component)
    {
        if (component != null)
        {
            player.getMcPlayer().getBukkitPlayer().teleport(component.getLocation());
        }
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
        
        this.match.join(player);
        player.getMcPlayer().sendMessage(Messages.JoinedArena, this.getDisplayName());
        // port to main lobby
        this.teleportRandom(player, this.getComponents(BasicComponentTypes.JoinSpawn));
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
            case Booting:
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
                this.match.spectate(player);
                
                player.getMcPlayer().sendMessage(Messages.SpectatingArena, this.getDisplayName());
                this.teleportToSpectate(player);
                break;
        }
    }

    /**
     * Teleports given player to spectators
     * @param player
     */
    private void teleportToSpectate(ArenaPlayerInterface player)
    {
        if (this.isMatch())
        {
            // match in progress
            final TeamIdType team = this.match.getTeam(player.getPlayerUUID());
            if (team != null)
            {
                final ObjectServiceInterface osi = ObjectServiceInterface.instance();
                final Collection<ComponentInterface> teamspawns = this.getComponents(BasicComponentTypes.SpectatorSpawn).stream()
                        .map(osi::findComponent)
                        .filter(c -> ((SpectatorSpawnComponentHandler)c.getHandler()).getTeam() == team)
                        .collect(Collectors.toList());
                if (this.teleportRandom(player, teamspawns))
                {
                    // succeeded
                    return;
                }
            }
        }
        this.teleportRandom(player, this.getComponents(BasicComponentTypes.SpectatorSpawn));
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

        final ArenaStateChangedEvent changedEvent = new ArenaStateChangedEvent(this, this.state, ArenaState.Starting);
        this.state = ArenaState.Starting;
        Bukkit.getPluginManager().callEvent(changedEvent);
        TaskManager.instance().queue(new AsyncArenaStartTask(this));
    }

    /**
     * Non-api function to force disabled state, not meant to be called by minigames.
     * @throws McException
     */
    public void setDisabled0() throws McException
    {
        this.arenaData.setEnabled(false);
        this.saveDataInternal();
        final ArenaStateChangedEvent changedEvent = new ArenaStateChangedEvent(this, this.state, ArenaState.Disabled);
        this.match = null;
        this.state = ArenaState.Disabled;
        Bukkit.getPluginManager().callEvent(changedEvent);
    }

    /**
     * Non-api function to force maintenance state, not meant to be called by minigames.
     * @throws McException
     */
    public void setMaintenance0() throws McException
    {
        this.arenaData.setMaintenance(true);
        this.saveDataInternal();
        final ArenaStateChangedEvent changedEvent = new ArenaStateChangedEvent(this, this.state, ArenaState.Maintenance);
        this.match = null;
        this.state = ArenaState.Maintenance;
        Bukkit.getPluginManager().callEvent(changedEvent);
    }

    /**
     * Non-api function to force join state, not meant to be called by minigames.
     * @throws McException
     */
    public void setJoin0() throws McException
    {
        final ArenaStateChangedEvent changedEvent = new ArenaStateChangedEvent(this, this.state, ArenaState.Join);
        this.state = ArenaState.Join;
        final Set<TeamData> teams = this.arenaData.getTeams();
        this.match = new ArenaMatchImpl(this, teams.size() > 0);
        for (final TeamData team : teams)
        {
            this.match.createTeam(team);
        }
        Bukkit.getPluginManager().callEvent(changedEvent);
    }

    @Override
    public void setDisabledState(boolean force) throws McException
    {
        switch (this.state)
        {
            default:
            case Booting:
            case Disabled:
                throw new McException(Messages.DisableWrongState);
            case Maintenance:
            case Starting:
                this.arenaData.setMaintenance(false);
                this.arenaData.setEnabled(false);
                this.saveData();
                {
                    final ArenaStateChangedEvent changedEvent = new ArenaStateChangedEvent(this, this.state, ArenaState.Disabled);
                    this.match = null;
                    this.state = ArenaState.Disabled;
                    Bukkit.getPluginManager().callEvent(changedEvent);
                }
                break;
            case Join:
                this.arenaData.setEnabled(false);
                this.saveData();
                // abort current game to kick players being in waiting lobby.
                this.abortGame(Messages.KickReasonDisable);
                {
                    final ArenaStateChangedEvent changedEvent = new ArenaStateChangedEvent(this, this.state, ArenaState.Restarting);
                    this.state = ArenaState.Restarting;
                    Bukkit.getPluginManager().callEvent(changedEvent);
                    TaskManager.instance().queue(new AsyncArenaRestartTask(this));
                }
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
                    final ArenaStateChangedEvent changedEvent = new ArenaStateChangedEvent(this, this.state, ArenaState.Disabled);
                    this.state = ArenaState.Restarting;
                    Bukkit.getPluginManager().callEvent(changedEvent);
                    TaskManager.instance().queue(new AsyncArenaRestartTask(this));
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
        for (final UUID uuid : this.match.getParticipants(true))
        {
            this.kick(uuid, kickReason);
        }
    }

    /**
     * Kicks a single player with given reason
     * @param uuid
     * @param kickReason
     */
    private void kick(UUID uuid, LocalizedMessageInterface kickReason)
    {
        final McPlayerInterface player = ObjectServiceInterface.instance().getPlayer(uuid);
        final ArenaPlayerImpl arenaPlayerImpl = (ArenaPlayerImpl)MinigamesLibInterface.instance().getPlayer(player);
        if (this.match != null)
        {
            try
            {
                this.match.leave(arenaPlayerImpl);
            }
            catch (McException ex)
            {
                this.getLogger().log(Level.WARNING, "Problems kicking player", ex); //$NON-NLS-1$
            }
        }
        arenaPlayerImpl.switchArenaOrMode(null, false);
        player.sendMessage(Messages.YouWereKicked, kickReason);
        this.teleportRandom(arenaPlayerImpl, this.getComponents(BasicComponentTypes.MainLobbySpawn));
    }

    @Override
    public void setMaintenance(boolean force) throws McException
    {
        switch (this.state)
        {
            default:
            case Maintenance:
            case Booting:
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
                {
                    final ArenaStateChangedEvent changedEvent = new ArenaStateChangedEvent(this, this.state, ArenaState.Maintenance);
                    this.state = ArenaState.Maintenance;
                    Bukkit.getPluginManager().callEvent(changedEvent);
                }
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
                    final ArenaStateChangedEvent changedEvent = new ArenaStateChangedEvent(this, this.state, ArenaState.Restarting);
                    this.state = ArenaState.Restarting;
                    Bukkit.getPluginManager().callEvent(changedEvent);
                    TaskManager.instance().queue(new AsyncArenaRestartTask(this));
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
        final ArenaStateChangedEvent changedEvent = new ArenaStateChangedEvent(this, this.state, ArenaState.PreMatch);
        this.state = ArenaState.PreMatch;
        Bukkit.getPluginManager().callEvent(changedEvent);
        // TODO Start pre match phase, teleport to spawns etc.
    }

    @Override
    public void finish() throws McException
    {
        if (!this.isMatch())
        {
            throw new McException(Messages.FinishWrongState);
        }
        final ArenaStateChangedEvent changedEvent = new ArenaStateChangedEvent(this, this.state, ArenaState.Restarting);
        this.state = ArenaState.Restarting;
        this.match.finish();
        Bukkit.getPluginManager().callEvent(changedEvent);
        // TODO Teleport players back to main lobby
    }

    @Override
    public void abort() throws McException
    {
        if (!this.isMatch())
        {
            throw new McException(Messages.AbortWrongState);
        }
        final ArenaStateChangedEvent changedEvent = new ArenaStateChangedEvent(this, this.state, ArenaState.Restarting);
        this.state = ArenaState.Restarting;
        this.match.abort();
        Bukkit.getPluginManager().callEvent(changedEvent);
        // TODO Teleport players back to main lobby
    }

    @Override
    public void forceStart() throws McException
    {
        // raise event and let the gaming rules do the rest
        final ArenaForceStartRequestedEvent event = new ArenaForceStartRequestedEvent(this);
        Bukkit.getPluginManager().callEvent(event);
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
        this.setJoin0();
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
            case Booting:
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
        // TODO implement checkup
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
        if (this.match == null)
        {
            return 0;
        }
        return this.match.getParticipantCount(false);
    }

    @Override
    public int getActivePlayerCount()
    {
        if (this.match == null)
        {
            return 0;
        }
        return this.match.getPlayerCount();
    }

    @Override
    public int getSpectatorCount()
    {
        if (this.match == null)
        {
            return 0;
        }
        return this.match.getSpectators().size();
    }

    @Override
    public Collection<ArenaPlayerInterface> getPlayers()
    {
        if (this.match == null)
        {
            return Collections.emptyList();
        }
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        final MinigamesLibInterface mglib = MinigamesLibInterface.instance();
        return this.match.getParticipants(false).stream().map(osi::getPlayer).map(mglib::getPlayer).collect(Collectors.toList());
    }

    @Override
    public Collection<ArenaPlayerInterface> getActivePlayers()
    {
        if (this.match == null)
        {
            return Collections.emptyList();
        }
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        final MinigamesLibInterface mglib = MinigamesLibInterface.instance();
        return this.match.getPlayers().stream().map(osi::getPlayer).map(mglib::getPlayer).collect(Collectors.toList());
    }

    @Override
    public Collection<ArenaPlayerInterface> getSpectators()
    {
        if (this.match == null)
        {
            return Collections.emptyList();
        }
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        final MinigamesLibInterface mglib = MinigamesLibInterface.instance();
        return this.match.getSpectators().stream().map(osi::getPlayer).map(mglib::getPlayer).collect(Collectors.toList());
    }

    @Override
    public boolean isPlaying(McPlayerInterface player)
    {
        if (this.match == null)
        {
            return false;
        }
        final MatchPlayerInterface p = this.match.get(player.getPlayerUUID());
        return p == null ? false : p.isPlaying();
    }

    @Override
    public boolean isSpectating(McPlayerInterface player)
    {
        if (this.match == null)
        {
            return false;
        }
        final MatchPlayerInterface p = this.match.get(player.getPlayerUUID());
        return p == null ? false : p.isSpec();
    }

    @Override
    public boolean isPlaying(ArenaPlayerInterface player)
    {
        if (this.match == null)
        {
            return false;
        }
        final MatchPlayerInterface p = this.match.get(player.getPlayerUUID());
        return p == null ? false : p.isPlaying();
    }

    @Override
    public boolean isSpectating(ArenaPlayerInterface player)
    {
        if (this.match == null)
        {
            return false;
        }
        final MatchPlayerInterface p = this.match.get(player.getPlayerUUID());
        return p == null ? false : p.isSpec();
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
        if (ArenaImpl.this.getState() != ArenaState.Maintenance && ArenaImpl.this.getState() != ArenaState.Booting)
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
        // TODO implement available rule sets
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
        // TODO implement available rule sets
        return false;
    }

    @Override
    public void reconfigureRuleSets(ArenaRuleSetType... rulesets) throws McException
    {
        for (final ArenaRuleSetType t : rulesets)
        {
            this.ruleSets.reapplyRuleSet(t);
        }
    }

    @Override
    public void reconfigureRuleSet(ArenaRuleSetType ruleset) throws McException
    {
        this.ruleSets.reapplyRuleSet(ruleset);
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
    public void applyRuleSet(ArenaRuleSetType ruleset) throws McException
    {
        if (!this.ruleSets.isApplied(ruleset))
        {
            this.ruleSets.applyOptionalRuleSet(ruleset);
            this.arenaData.getOptionalRules().add(ruleset);
            this.saveData();
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
    public void removeRuleSet(ArenaRuleSetType ruleset) throws McException
    {
        if (this.ruleSets.isOptional(ruleset))
        {
            this.ruleSets.removeOptionalRuleSet(ruleset);
            this.arenaData.getOptionalRules().remove(ruleset);
            this.saveData();
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

    @Override
    public ArenaMatchInterface getCurrentMatch()
    {
        return this.match;
    }

    @Override
    public void setSinglePlayerMode() throws McException
    {
        if (this.getState() != ArenaState.Maintenance)
        {
            throw new McException(Messages.ModificationWrongState);
        }
        this.arenaData.getTeams().clear();
        this.saveData();
    }

    @Override
    public void addTeam(TeamIdType team, LocalizedConfigString name) throws McException
    {
        if (this.getState() != ArenaState.Maintenance)
        {
            throw new McException(Messages.ModificationWrongState);
        }
        if (team.isSpecial())
        {
            throw new McException(CommonMessages.InternalError, "Unable to add special teams"); //$NON-NLS-1$
        }
        this.arenaData.getTeams().removeIf(t -> t.getId() == team);
        this.arenaData.getTeams().add(new TeamData(team, name));
        this.saveData();
    }

    @Override
    public void removeTeam(TeamIdType team) throws McException
    {
        if (this.getState() != ArenaState.Maintenance)
        {
            throw new McException(Messages.ModificationWrongState);
        }
        this.arenaData.getTeams().removeIf(t -> t.getId() == team);
        this.saveData();
    }

    @Override
    public Collection<TeamIdType> getTeams()
    {
        return this.arenaData.getTeams().stream().map(TeamData::getId).collect(Collectors.toList());
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
            McLibInterface.instance().setContext(ArenaInterface.class, this);
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
            McLibInterface.instance().setContext(ArenaInterface.class, this);
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
            McLibInterface.instance().setContext(ArenaInterface.class, this);
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
            McLibInterface.instance().setContext(ArenaInterface.class, this);
            return runnable.get();
        });
    }

    @Override
    public ArenaTeamInterface getTeam(TeamIdType team)
    {
        final Optional<TeamData> data = this.arenaData.getTeams().stream().filter(t -> t.getId() == team).findFirst();
        return !data.isPresent() ? null : new ArenaTeamInterface() {
            
            @Override
            public void setName(LocalizedConfigString name) throws McException
            {
                if (ArenaImpl.this.getState() != ArenaState.Maintenance)
                {
                    throw new McException(Messages.ModificationWrongState);
                }
                if (team.isSpecial())
                {
                    throw new McException(CommonMessages.InternalError, "Unable to rename special teams"); //$NON-NLS-1$
                }
                data.get().setName(name);
                ArenaImpl.this.saveData();
            }
            
            @Override
            public LocalizedConfigString getName()
            {
                return data.get().getName();
            }
            
            @Override
            public TeamIdType getId()
            {
                return team;
            }
            
            @Override
            public ArenaInterface getArena()
            {
                return ArenaImpl.this;
            }
        };
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
        @MessageComment({"Cannot start match because arena is not in join mode"})
        StartWrongState,
        
        /**
         * Cannot abort match because arena is not in match mode
         */
        @LocalizedMessage(defaultMessage = "Cannot abort match because arena ist not running a match.", severity = MessageSeverityType.Error)
        @MessageComment({"Cannot abort match because arena is not running a match"})
        AbortWrongState,
        
        /**
         * Cannot finish match because arena is not in match mode
         */
        @LocalizedMessage(defaultMessage = "Cannot start match because arena ist not running a match.", severity = MessageSeverityType.Error)
        @MessageComment({"Cannot finish match because arena is not in match mode"})
        FinishWrongState,
        
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
        
        /**
         * There was an invalid modification after match was finished
         */
        @LocalizedMessage(defaultMessage = "Invalid modification within arena match (%1$s).", severity = MessageSeverityType.Error)
        @MessageComment(value = {"There was an invalid modification after match was finished"}, args = @Argument("arena display name"))
        InvalidModificationAfterFinish,
        
        /**
         * There was an invalid modification before match was started
         */
        @LocalizedMessage(defaultMessage = "Invalid modification before arena match starts (%1$s).", severity = MessageSeverityType.Error)
        @MessageComment(value = {"There was an invalid modification before match was started"}, args = @Argument("arena display name"))
        InvalidModificationBeforeStart,
        
        /**
         * There was an invalid team action on single player matches
         */
        @LocalizedMessage(defaultMessage = "Invalid team action in single player arena match (%1$s).", severity = MessageSeverityType.Error)
        @MessageComment(value = {"There was an invalid team action on single player matches"}, args = @Argument("arena display name"))
        InvalidTeamActionOnSinglePlayerMatch,
        
        /**
         * There was an invalid leave action
         */
        @LocalizedMessage(defaultMessage = "Invalid team leave action in arena match (%1$s).", severity = MessageSeverityType.Error)
        @MessageComment(value = {"There was an invalid leave action"}, args = @Argument("arena display name"))
        InvalidLeaveAction,
        
        /**
         * There was an invalid join action
         */
        @LocalizedMessage(defaultMessage = "Invalid team join action in arena match (%1$s).", severity = MessageSeverityType.Error)
        @MessageComment(value = {"There was an invalid join action"}, args = @Argument("arena display name"))
        InvalidJoinAction,
        
        /**
         * There was an invalid team switch
         */
        @LocalizedMessage(defaultMessage = "Invalid team switch action in arena match (%1$s).", severity = MessageSeverityType.Error)
        @MessageComment(value = {"There was an invalid team switch"}, args = @Argument("arena display name"))
        InvalidTeamSwitch,
        
        /**
         * Cannot rejoin same match
         */
        @LocalizedMessage(defaultMessage = "Unable to re-join a match you already played (%1$s).", severity = MessageSeverityType.Error)
        @MessageComment(value = {"Cannot rejoin same match"}, args = @Argument("arena display name"))
        CannotRejoin,
        
    }
    
}
