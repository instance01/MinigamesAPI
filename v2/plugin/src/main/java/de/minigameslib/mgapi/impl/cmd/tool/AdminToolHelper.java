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
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.SignTypeId;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.obj.ArenaSignHandler;
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
    private static void onCreateSign(McPlayerInteractEvent evt, McPlayerInterface player, ArenaInterface arena, String name, SignTypeId type, McConsumer<ArenaSignHandler> finish) throws McException
    {
        // TODO set sign name
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
    }
    
}
