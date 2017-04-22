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

package de.minigameslib.mgapi.impl.cmd.tool;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McPlayerInteractEvent;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mclib.api.objects.ComponentTypeId;
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.SignTypeId;
import de.minigameslib.mclib.api.objects.ZoneTypeId;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.obj.ArenaComponentHandler;
import de.minigameslib.mgapi.api.obj.ArenaSignHandler;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.impl.MglibPerms;
import de.minigameslib.mgapi.impl.arena.ArenaImpl;

/**
 * Admin tooling helper.
 * 
 * @author mepeisen
 */
public class AdminToolHelper
{

    /**
     * Registers the tooling to select a block for component creation.
     * @param player
     * @param arena
     * @param name
     * @param type
     * @param finish the finish action
     */
    public static void onCreateComponent(McPlayerInterface player, ArenaInterface arena, String name, ComponentTypeId type, McConsumer<ArenaComponentHandler> finish)
    {
        final ItemServiceInterface itemService = ItemServiceInterface.instance();
        itemService.prepareTool(CommonItems.App_Pinion, player, Messages.CreateComponent_Title)
            .onLeftClick((p, evt) -> onCreateComponent(evt, p, arena, name, type, finish))
            .onRightClick((p, evt) -> onCreateComponent(evt, p, arena, name, type, finish))
            .description(Messages.CreateComponent_Description, type.getPluginName() + '/' + type.name(), arena.getInternalName(), name)
            .build();
        player.sendMessage(Messages.CreateComponent_ClickBlock);
    }
    
    /**
     * Registers the tooling to select the block for component creation.
     * @param evt 
     * @param player
     * @param arena
     * @param name
     * @param type
     * @param finish the finish action
     * @throws McException 
     */
    private static void onCreateComponent(McPlayerInteractEvent evt, McPlayerInterface player, ArenaInterface arena, String name, ComponentTypeId type, McConsumer<ArenaComponentHandler> finish) throws McException
    {
        // security checks
        if (!(player.getBukkitPlayer().isOp() || player.checkPermission(MglibPerms.CommandAdminComp)))
        {
            throw new McException(CommonMessages.NoPermissionForCommand, "/mg2 admin comp create"); //$NON-NLS-1$
        }
        if (!arena.isMaintenance())
        {
            throw new McException(ArenaImpl.Messages.ModificationWrongState);
        }
        
        final Location loc = evt.getBukkitEvent().getClickedBlock().getLocation();
        final ArenaComponentHandler result = arena.createComponent(loc, type);
        result.setName(name);
        player.sendMessage(Messages.CreateComponent_Created);
        if (finish != null)
        {
            finish.accept(result);
        }
    }

    /**
     * Registers the tooling to select a block for changing zone lower bounds.
     * @param player
     * @param zone
     * @param finish the finish action
     */
    public static void onRelocateZoneLower(McPlayerInterface player, ArenaZoneHandler zone, McConsumer<ArenaZoneHandler> finish)
    {
        final ItemServiceInterface itemService = ItemServiceInterface.instance();
        itemService.prepareTool(CommonItems.App_Pinion, player, Messages.CreateZone_Title)
            .onLeftClick((p, evt) -> onRelocateZoneLower1(evt, p, zone, finish))
            .onRightClick((p, evt) -> onRelocateZoneLower1(evt, p, zone, finish))
            .description(Messages.RelocateZone_Description, zone.getName(), zone.getArena().getInternalName())
            .singleUse()
            .build();
        player.sendMessage(Messages.RelocateZone_ClickBlockLower);
    }

    /**
     * Registers the tooling to select a block for changing zone higher bounds.
     * @param player
     * @param zone
     * @param finish the finish action
     */
    public static void onRelocateZoneHigher(McPlayerInterface player, ArenaZoneHandler zone, McConsumer<ArenaZoneHandler> finish)
    {
        final ItemServiceInterface itemService = ItemServiceInterface.instance();
        itemService.prepareTool(CommonItems.App_Pinion, player, Messages.CreateZone_Title)
            .onLeftClick((p, evt) -> onRelocateZoneHigher1(evt, p, zone, finish))
            .onRightClick((p, evt) -> onRelocateZoneHigher1(evt, p, zone, finish))
            .description(Messages.RelocateZone_Description, zone.getName(), zone.getArena().getInternalName())
            .singleUse()
            .build();
        player.sendMessage(Messages.RelocateZone_ClickBlockHigher);
    }
    
    /**
     * Registers the tooling to select the lower block for zone relocation.
     * @param evt 
     * @param player
     * @param zone 
     * @param finish the finish action
     * @throws McException 
     */
    private static void onRelocateZoneLower1(McPlayerInteractEvent evt, McPlayerInterface player, ArenaZoneHandler zone, McConsumer<ArenaZoneHandler> finish) throws McException
    {
        // security checks
        if (!(player.getBukkitPlayer().isOp() || player.checkPermission(MglibPerms.CommandAdminZone)))
        {
            throw new McException(CommonMessages.NoPermissionForCommand, "/mg2 admin zone create"); //$NON-NLS-1$
        }
        if (!zone.getArena().isMaintenance())
        {
            throw new McException(ArenaImpl.Messages.ModificationWrongState);
        }
        
        final Location loc = evt.getBukkitEvent().getClickedBlock().getLocation();
        zone.getZone().setCuboid(new Cuboid(loc, zone.getZone().getCuboid().getHighLoc()));
        zone.getZone().saveConfig();
        player.sendMessage(Messages.RelocateZone_Relocated);
        if (finish != null)
        {
            finish.accept(zone);
        }
    }
    
    /**
     * Registers the tooling to select the higher block for zone relocation.
     * @param evt 
     * @param player
     * @param zone 
     * @param finish the finish action
     * @throws McException 
     */
    private static void onRelocateZoneHigher1(McPlayerInteractEvent evt, McPlayerInterface player, ArenaZoneHandler zone, McConsumer<ArenaZoneHandler> finish) throws McException
    {
        // security checks
        if (!(player.getBukkitPlayer().isOp() || player.checkPermission(MglibPerms.CommandAdminZone)))
        {
            throw new McException(CommonMessages.NoPermissionForCommand, "/mg2 admin zone create"); //$NON-NLS-1$
        }
        if (!zone.getArena().isMaintenance())
        {
            throw new McException(ArenaImpl.Messages.ModificationWrongState);
        }
        
        final Location loc = evt.getBukkitEvent().getClickedBlock().getLocation();
        zone.getZone().setCuboid(new Cuboid(zone.getZone().getCuboid().getLowLoc(), loc));
        zone.getZone().saveConfig();
        player.sendMessage(Messages.RelocateZone_Relocated);
        if (finish != null)
        {
            finish.accept(zone);
        }
    }

    /**
     * Registers the tooling to select a block for zone creation.
     * @param player
     * @param arena
     * @param name
     * @param type
     * @param finish the finish action
     */
    public static void onCreateZone(McPlayerInterface player, ArenaInterface arena, String name, ZoneTypeId type, McConsumer<ArenaZoneHandler> finish)
    {
        final ItemServiceInterface itemService = ItemServiceInterface.instance();
        itemService.prepareTool(CommonItems.App_Pinion, player, Messages.CreateZone_Title)
            .onLeftClick((p, evt) -> onCreateZone1(evt, p, arena, name, type, finish))
            .onRightClick((p, evt) -> onCreateZone1(evt, p, arena, name, type, finish))
            .description(Messages.CreateZone_Description, type.getPluginName() + '/' + type.name(), arena.getInternalName(), name)
            .build();
        player.sendMessage(Messages.CreateZone_ClickBlockLower);
    }
    
    /**
     * Registers the tooling to select the lower block for zone creation.
     * @param evt 
     * @param player
     * @param arena
     * @param name
     * @param type
     * @param finish the finish action
     * @throws McException 
     */
    private static void onCreateZone1(McPlayerInteractEvent evt, McPlayerInterface player, ArenaInterface arena, String name, ZoneTypeId type, McConsumer<ArenaZoneHandler> finish) throws McException
    {
        // security checks
        if (!(player.getBukkitPlayer().isOp() || player.checkPermission(MglibPerms.CommandAdminZone)))
        {
            throw new McException(CommonMessages.NoPermissionForCommand, "/mg2 admin zone create"); //$NON-NLS-1$
        }
        if (!arena.isMaintenance())
        {
            throw new McException(ArenaImpl.Messages.ModificationWrongState);
        }
        
        final Location lower = evt.getBukkitEvent().getClickedBlock().getLocation();
        final ItemServiceInterface itemService = ItemServiceInterface.instance();
        itemService.prepareTool(CommonItems.App_Pinion, player, Messages.CreateZone_Title)
            .onLeftClick((p, evt2) -> onCreateZone2(evt2, p, arena, name, type, lower, finish))
            .onRightClick((p, evt2) -> onCreateZone2(evt2, p, arena, name, type, lower, finish))
            .description(Messages.CreateZone_Description, type.getPluginName() + '/' + type.name(), arena.getInternalName(), name)
            .singleUse()
            .build();
        player.sendMessage(Messages.CreateZone_ClickBlockHigher);
    }
    
    /**
     * Registers the tooling to select the higher block for zone creation.
     * @param evt 
     * @param player
     * @param arena
     * @param name
     * @param type
     * @param lower 
     * @param finish the finish action
     * @throws McException 
     */
    private static void onCreateZone2(McPlayerInteractEvent evt, McPlayerInterface player, ArenaInterface arena, String name, ZoneTypeId type, Location lower, McConsumer<ArenaZoneHandler> finish) throws McException
    {
        // security checks
        if (!(player.getBukkitPlayer().isOp() || player.checkPermission(MglibPerms.CommandAdminZone)))
        {
            throw new McException(CommonMessages.NoPermissionForCommand, "/mg2 admin zone create"); //$NON-NLS-1$
        }
        if (!arena.isMaintenance())
        {
            throw new McException(ArenaImpl.Messages.ModificationWrongState);
        }
        
        final Location higher = evt.getBukkitEvent().getClickedBlock().getLocation();
        
        final ArenaZoneHandler result = arena.createZone(new Cuboid(lower, higher), type);
        result.setName(name);
        player.sendMessage(Messages.CreateZone_Created);
        if (finish != null)
        {
            finish.accept(result);
        }
    }

    /**
     * Registers the tooling to select a block for sign creation.
     * @param player
     * @param arena
     * @param name
     * @param type
     * @param finish the finish action
     */
    public static void onCreateSign(McPlayerInterface player, ArenaInterface arena, String name, SignTypeId type, McConsumer<ArenaSignHandler> finish)
    {
        final ItemServiceInterface itemService = ItemServiceInterface.instance();
        itemService.prepareTool(CommonItems.App_Pinion, player, Messages.CreateSign_Title)
            .onLeftClick((p, evt) -> onCreateSign(evt, p, arena, name, type, finish))
            .onRightClick((p, evt) -> onCreateSign(evt, p, arena, name, type, finish))
            .description(Messages.CreateSign_Description, type.getPluginName() + '/' + type.name(), arena.getInternalName(), name)
            .singleUse()
            .build();
        player.sendMessage(Messages.CreateSign_ClickBlock);
    }
    
    /**
     * Registers the tooling to select a block for sign creation.
     * @param evt 
     * @param player
     * @param arena
     * @param name
     * @param type
     * @param finish the finish action
     * @throws McException 
     */
    @SuppressWarnings("deprecation")
    private static void onCreateSign(McPlayerInteractEvent evt, McPlayerInterface player, ArenaInterface arena, String name, SignTypeId type, McConsumer<ArenaSignHandler> finish) throws McException
    {
        // security checks
        if (!(player.getBukkitPlayer().isOp() || player.checkPermission(MglibPerms.CommandAdminSign)))
        {
            throw new McException(CommonMessages.NoPermissionForCommand, "/mg2 admin sign create"); //$NON-NLS-1$
        }
        if (!arena.isMaintenance())
        {
            throw new McException(ArenaImpl.Messages.ModificationWrongState);
        }
        
        Block target = null;
        byte opposite;
        switch (evt.getBukkitEvent().getBlockFace())
        {
            case DOWN:
                throw new McException(Messages.CreateSign_CannotCreateDownFace);
                
            case EAST:
            case EAST_NORTH_EAST:
            case EAST_SOUTH_EAST:
                target = evt.getBukkitEvent().getClickedBlock().getRelative(BlockFace.EAST);
                opposite = (byte) 0x05;
                break;
                
            case NORTH:
            case NORTH_EAST:
            case NORTH_NORTH_EAST:
            case NORTH_NORTH_WEST:
            case NORTH_WEST:
                target = evt.getBukkitEvent().getClickedBlock().getRelative(BlockFace.NORTH);
                opposite = (byte) 0x02;
                break;
                
            case SOUTH:
            case SOUTH_EAST:
            case SOUTH_SOUTH_EAST:
            case SOUTH_SOUTH_WEST:
            case SOUTH_WEST:
                target = evt.getBukkitEvent().getClickedBlock().getRelative(BlockFace.SOUTH);
                opposite = (byte) 0x03;
                break;

            case UP:
                target = evt.getBukkitEvent().getClickedBlock().getRelative(BlockFace.UP);
                double i = ((player.getBukkitPlayer().getLocation().getYaw() + 180.0f) * 16.0f / 360.0f) + 0.5d;
                int j = (int) i;
                if (i < j)
                {
                    j = j - 1;
                }
                opposite = (byte) (j & 15);
                break;

            case WEST:
            case WEST_NORTH_WEST:
            case WEST_SOUTH_WEST:
                target = evt.getBukkitEvent().getClickedBlock().getRelative(BlockFace.WEST);
                opposite = (byte) 0x04;
                break;
                
            case SELF:
            default:
                throw new McException(Messages.CreateSign_CannotCreateBlocked);
        }
        
        if (!target.isEmpty())
        {
            throw new McException(Messages.CreateSign_CannotCreateBlocked);
        }
        
        if (evt.getBukkitEvent().getBlockFace() == BlockFace.UP)
        {
            target.setType(Material.SIGN_POST);
            target.setData(opposite);
        }
        else
        {
            target.setType(Material.WALL_SIGN);
            target.setData(opposite);
        }
        
        final Sign sign = (Sign) target.getState();
        final ArenaSignHandler result = arena.createSign(sign, type);
        result.setName(name);
        player.sendMessage(Messages.CreateSign_Created);
        if (finish != null)
        {
            finish.accept(result);
        }
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admintool")
    public enum Messages implements LocalizedMessageInterface
    {
        
        // signs
        
        /**
         * Title for create sign
         */
        @LocalizedMessage(defaultMessage = "Creating sign")
        @MessageComment({"Create sign title"})
        CreateSign_Title,
        
        /**
         * Description for create sign
         */
        @LocalizedMessageList({
            "Sign type: " + LocalizedMessage.CODE_COLOR + "%1$s",
            "Arena: " + LocalizedMessage.CODE_COLOR + "%2$s",
            "Name: " + LocalizedMessage.CODE_COLOR + "%3$s"})
        @MessageComment(value = "Create sign description", args = {
            @Argument("sign type name"),
            @Argument("arena internal name"),
            @Argument("new sign name")
        })
        CreateSign_Description,
        
        /**
         * Cannot create sign down face
         */
        @LocalizedMessage(defaultMessage = "Cannot create sign below blocks")
        @MessageComment({"Cannot create sign down face"})
        CreateSign_CannotCreateDownFace,
        
        /**
         * Cannot create sign because block in way
         */
        @LocalizedMessage(defaultMessage = "Cannot replace solid blocks with signs")
        @MessageComment({"Cannot create solid blocks with signs"})
        CreateSign_CannotCreateBlocked,
        
        /**
         * Message to advice the user to click the block
         */
        @LocalizedMessage(defaultMessage = "Use the Pinion tool and click a block to create the sign")
        @MessageComment({"Message to advice the user to click the block"})
        CreateSign_ClickBlock,
        
        /**
         * Sign was created
         */
        @LocalizedMessage(defaultMessage = "Sign created", severity = MessageSeverityType.Success)
        @MessageComment({"Sign was created"})
        CreateSign_Created,
        
        // zones
        
        /**
         * Title for create zone
         */
        @LocalizedMessage(defaultMessage = "Creating zone")
        @MessageComment({"Create zone title"})
        CreateZone_Title,
        
        /**
         * Title for relocating zone
         */
        @LocalizedMessage(defaultMessage = "Relocating zone")
        @MessageComment({"Relocate zone title"})
        RelocateZone_Title,
        
        /**
         * Description for create zone
         */
        @LocalizedMessageList({
            "Zone type: " + LocalizedMessage.CODE_COLOR + "%1$s",
            "Arena: " + LocalizedMessage.CODE_COLOR + "%2$s",
            "Name: " + LocalizedMessage.CODE_COLOR + "%3$s"})
        @MessageComment(value = "Create zone description", args = {
            @Argument("zone type name"),
            @Argument("arena internal name"),
            @Argument("new zone name")
        })
        CreateZone_Description,
        
        /**
         * Description for relocate zone
         */
        @LocalizedMessageList({
            "Zone: " + LocalizedMessage.CODE_COLOR + "%1$s",
            "Arena: " + LocalizedMessage.CODE_COLOR + "%2$s"})
        @MessageComment(value = "Create zone description", args = {
            @Argument("zone name"),
            @Argument("arena internal name")
        })
        RelocateZone_Description,
        
        /**
         * Message to advice the user to click the block
         */
        @LocalizedMessage(defaultMessage = "Use the Pinion tool and click the lower bound of your zone")
        @MessageComment({"Message to advice the user to click the block"})
        CreateZone_ClickBlockLower,
        
        /**
         * Message to advice the user to click the block
         */
        @LocalizedMessage(defaultMessage = "Use the Pinion tool and click the higher bound of your zone")
        @MessageComment({"Message to advice the user to click the block"})
        CreateZone_ClickBlockHigher,
        
        /**
         * Message to advice the user to click the block
         */
        @LocalizedMessage(defaultMessage = "Use the Pinion tool and click the new lower bound of your zone")
        @MessageComment({"Message to advice the user to click the block"})
        RelocateZone_ClickBlockLower,
        
        /**
         * Message to advice the user to click the block
         */
        @LocalizedMessage(defaultMessage = "Use the Pinion tool and click the new higher bound of your zone")
        @MessageComment({"Message to advice the user to click the block"})
        RelocateZone_ClickBlockHigher,
        
        /**
         * Zone was created
         */
        @LocalizedMessage(defaultMessage = "Zone created", severity = MessageSeverityType.Success)
        @MessageComment({"Zone was created"})
        CreateZone_Created,
        
        /**
         * Zone was relocated
         */
        @LocalizedMessage(defaultMessage = "Zone relocated", severity = MessageSeverityType.Success)
        @MessageComment({"Zone was relocated"})
        RelocateZone_Relocated,
        
        // components
        
        /**
         * Title for create component
         */
        @LocalizedMessage(defaultMessage = "Creating component")
        @MessageComment({"Create component title"})
        CreateComponent_Title,
        
        /**
         * Description for create component
         */
        @LocalizedMessageList({
            "Component type: " + LocalizedMessage.CODE_COLOR + "%1$s",
            "Arena: " + LocalizedMessage.CODE_COLOR + "%2$s",
            "Name: " + LocalizedMessage.CODE_COLOR + "%3$s"})
        @MessageComment(value = "Create component description", args = {
            @Argument("component type name"),
            @Argument("arena internal name"),
            @Argument("new component name")
        })
        CreateComponent_Description,
        
        /**
         * Message to advice the user to click the block
         */
        @LocalizedMessage(defaultMessage = "Use the Pinion tool and click a block to create the component")
        @MessageComment({"Message to advice the user to click the block"})
        CreateComponent_ClickBlock,
        
        /**
         * Component was created
         */
        @LocalizedMessage(defaultMessage = "Component created", severity = MessageSeverityType.Success)
        @MessageComment({"Component was created"})
        CreateComponent_Created,
    }
    
}
