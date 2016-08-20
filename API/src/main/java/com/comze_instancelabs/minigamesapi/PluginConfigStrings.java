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

package com.comze_instancelabs.minigamesapi;

/**
 * Strings for plugins config.
 * 
 * @author mepeisen
 */
public interface PluginConfigStrings
{
    
    /**
     * debug flag for minigames lib.
     */
    String DEBUG = "config.debug"; //$NON-NLS-1$
    
    /**
     * flag to support the party command (playing with friends).
     */
    String PARTY_COMMAND_ENABLED = "config.party_command_enabled"; //$NON-NLS-1$
    
    /**
     * the update interval for arena signs.
     */
    String SIGNS_UPDATE_TIME = "signs_updating_time"; //$NON-NLS-1$
    
    /**
     * flag for controlling auto updates.
     */
    String AUTO_UPDATING = "config.auto_updating"; //$NON-NLS-1$
    
    /**
     * flag for controlling metrics.
     */
    String POST_METRICS = "config.post_metrics"; //$NON-NLS-1$
    
    /**
     * flag for enabling motd manipulation.
     */
    String MOTD_ENABLED = "config.motd.enabled"; //$NON-NLS-1$
    
    /**
     * seconds to rotate from arena to arena.
     */
    String MOTD_ROTATION_SECONDS = "config.motd.rotation"; //$NON-NLS-1$
    
    /**
     * string to set for motd
     */
    String MOTD_TEXT = "config.motd.text"; //$NON-NLS-1$
    
    /**
     * string for motd state text
     */
    String MOTD_STATE_JOIN = "config.motd.state.join"; //$NON-NLS-1$
    
    /**
     * string for motd state text
     */
    String MOTD_STATE_STARTING = "config.motd.state.starting"; //$NON-NLS-1$
    
    /**
     * string for motd state text
     */
    String MOTD_STATE_INGAME = "config.motd.state.ingame"; //$NON-NLS-1$
    
    /**
     * string for motd state text
     */
    String MOTD_STATE_RESTARTING = "config.motd.state.restarting"; //$NON-NLS-1$
    
    /**
     * string for motd state text
     */
    String MOTD_STATE_DISABLED = "config.motd.state.disabled"; //$NON-NLS-1$
    
    /**
     * Prefix for core permissions.
     */
    String PERMISSION_PREFIX = "config.permissions_prefix"; //$NON-NLS-1$
    
    /**
     * Prefix for core permissions.
     */
    String PERMISSION_KITS_PREFIX = "config.permissions_kits_prefix"; //$NON-NLS-1$
    
    /**
     * Prefix for gun permissions.
     */
    String PERMISSION_GUN_PREFIX = "config.permissions_gun_prefix"; //$NON-NLS-1$
    
    /**
     * Prefix for shop-item permissions.
     */
    String PERMISSION_SHOP_PREFIX = "config.permissions_shop_prefix"; //$NON-NLS-1$
    
    /**
     * Prefix for game permissions.
     */
    String PERMISSION_GAME_PREFIX = "config.permissions_game_prefix"; //$NON-NLS-1$
    
}
