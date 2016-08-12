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

package com.github.mce.minigames.impl.cmd;

import static com.github.mce.minigames.api.cmd.CommandInterface.isPlayer;
import static com.github.mce.minigames.api.player.ArenaPlayerInterface.hasPerm;
import static com.github.mce.minigames.api.player.ArenaPlayerInterface.isInArena;

import com.github.mce.minigames.api.CommonErrors;
import com.github.mce.minigames.api.CommonMessages;
import com.github.mce.minigames.api.MglibInterface;
import com.github.mce.minigames.api.MinigameException;
import com.github.mce.minigames.api.arena.ArenaInterface;
import com.github.mce.minigames.api.cmd.CommandHandlerInterface;
import com.github.mce.minigames.api.cmd.CommandInterface;
import com.github.mce.minigames.api.perms.CommonPermissions;
import com.github.mce.minigames.api.player.ArenaPlayerInterface;

/**
 * A handler for the /start command.
 * 
 * @author mepeisen
 */
public class StartCommandHandler implements CommandHandlerInterface
{
    
    @Override
    public void handle(CommandInterface command) throws MinigameException
    {
        // only in-game
        command.when(isPlayer().negate()).thenThrow(CommonErrors.InvokeIngame);

        // check permission
        final ArenaPlayerInterface player = command.getPlayer();
        player.when(hasPerm(CommonPermissions.start).negate()).thenThrow(CommonErrors.NoPermissionForStart);
        
        // only inside arena
        player.when(isInArena().negate()).thenThrow(CommonErrors.StartNotWithinArena);
        
        // check if the arena can be started directly
        final ArenaInterface arena = player.getArena();
        arena.when(arena.canStart().negate()).thenThrow(CommonErrors.CannotStart);
        
        // start it, log and send success message
        MglibInterface.INSTANCE.get().getLogger().info("Arena " + arena.getInternalName() + " started because of start command from player " + player.getName()); //$NON-NLS-1$//$NON-NLS-2$
        arena.start();
        player.sendMessage(CommonMessages.ArenaStartedByCommand, arena.getDisplayName(), player.getName());
    }
    
}
