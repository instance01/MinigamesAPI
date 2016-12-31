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
 * Common command strings.
 * 
 * @author mepeisen
 */
public interface CommandStrings
{
    
    /**
     * &quot;/start&quot; command.
     */
    String START = "start"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;start&gt; ...
     */
    String GAME_START = "start"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;setspawn&gt; ...
     */
    String GAME_SET_SPAWN = "setspawn"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;setspecspawn&gt; ...
     */
    String GAME_SET_SPEC_SPAWN = "setspecspawn"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;setlobby&gt; ...
     */
    String GAME_SET_LOBBY = "setlobby"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;setmainlobby&gt; ...
     */
    String GAME_SET_MAINLOBBY = "setmainlobby"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;setbounds&gt; ...
     */
    String GAME_SET_BOUNDS = "setbounds"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;setlobbybounds&gt; ...
     */
    String GAME_SET_LOBBY_BOUNDS = "setlobbybounds"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;setspecbounds&gt; ...
     */
    String GAME_SET_SPEC_BOUNDS = "setspecbounds"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;savearena&gt; ...
     */
    String GAME_SAVE_ARENA = "savearena"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;save&gt; ...
     */
    @Deprecated
    String GAME_SAVE = "save"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;setmaxplayers&gt; ...
     */
    String GAME_SET_MAX_PLAYERS = "setmaxplayers"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;setminplayers&gt; ...
     */
    String GAME_SET_MIN_PLAYERS = "setminplayers"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;setarenavip&gt; ...
     */
    String GAME_SET_ARENA_VIP = "setarenavip"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;setvip&gt; ...
     */
    @Deprecated
    String GAME_SET_VIP = "setvip"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;join&gt; ...
     */
    String GAME_JOIN = "join"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;leave&gt; ...
     */
    String GAME_LEAVE = "leave"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;stop&gt; ...
     */
    String GAME_STOP = "stop"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;stopall&gt; ...
     */
    String GAME_STOP_ALL = "stopall"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;removearena&gt; ...
     */
    String GAME_REMOVE_ARENA = "removearena"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;removespawn&gt; ...
     */
    String GAME_REMOVE_SPAWN = "removespawn"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;setskull&gt; ...
     */
    String GAME_SET_SKULL = "setskull"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;setenabled&gt; ...
     */
    String GAME_SET_ENABLED = "setenabled"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;setshowscoreboard&gt; ...
     */
    String GAME_SET_SHOW_SCOREBOARD = "setshowscoreboard"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;reset&gt; ...
     */
    String GAME_RESET = "reset"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;setauthor&gt; ...
     */
    String GAME_SET_AUTHOR = "setauthor"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;setdescription&gt; ...
     */
    String GAME_SET_DESCRIPTION = "setdescription"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;setdisplayname&gt; ...
     */
    String GAME_SET_DISPLAYNAME = "setdisplayname"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;kit&gt; ...
     */
    String GAME_KIT = "kit"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;spectate&gt; ...
     */
    String GAME_SPECTATE = "spectate"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;shop&gt; ...
     */
    String GAME_SHOP = "shop"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;leaderboards&gt; ...
     */
    String GAME_LEADER_BOARDS = "leaderboards"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;lb&gt; ...
     */
    String GAME_LB = "lb"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;top&gt; ...
     */
    @Deprecated
    String GAME_TOP = "top"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;stats&gt; ...
     */
    String GAME_STATS = "stats"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;sethologram&gt; ...
     */
    String GAME_SET_HOLOGRAM = "sethologram"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;listholograms&gt; ...
     */
    String GAME_LIST_HOLOGRAMS = "listholograms"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;removehologram&gt; ...
     */
    String GAME_REMOVE_HOLOGRAM = "removehologram"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;help&gt; ...
     */
    String GAME_HELP = "help"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;list&gt; ...
     */
    String GAME_LIST = "list"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;reload&gt; ...
     */
    String GAME_RELOAD = "reload"; //$NON-NLS-1$
    
    // old commands
    
    /**
     * Command action: &quot;/minigame &lt;createarena&gt; ...
     * @deprecated removed in 1.5.0
     */
    @Deprecated
    String GAME_CREATE_ARENA = "createarena"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/minigame &lt;endall&gt; ...
     * @deprecated removed in 1.5.0
     */
    @Deprecated
    String GAME_END_ALL = "endall"; //$NON-NLS-1$

    // party commands
    
    /**
     * &quot;/party&quot; command.
     */
    String PARTY = "party"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/party &lt;invite&gt; ...
     */
    String PARTY_INVITE = "invite"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/party &lt;accept&gt; ...
     */
    String PARTY_ACCEPT = "accept"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/party &lt;kick&gt; ...
     */
    String PARTY_KICK = "kick"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/party &lt;list&gt; ...
     */
    String PARTY_LIST = "list"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/party &lt;disband&gt; ...
     */
    String PARTY_DISBAND = "disband"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/party &lt;leave&gt; ...
     */
    String PARTY_LEAVE = "leave"; //$NON-NLS-1$
    
    // minigames api commands

    /**
     * &quot;/mapi&quot; command.
     */
    String MAPI = "mapi"; //$NON-NLS-1$

    /**
     * &quot;/mgapi&quot; command.
     */
    String MGAPI = "mgapi"; //$NON-NLS-1$

    /**
     * &quot;/mglib&quot; command.
     */
    String MGLIB = "mglib"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/mglib &lt;info&gt; ...
     */
    String MGLIB_INFO = "info"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/mglib &lt;debug&gt; ...
     */
    String MGLIB_DEBUG = "debug"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/mglib &lt;list&gt; ...
     */
    String MGLIB_LIST = "list"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/mglib &lt;title&gt; ...
     */
    String MGLIB_TITLE = "title"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/mglib &lt;subtitle&gt; ...
     */
    String MGLIB_SUBTITLE = "subtitle"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/mglib &lt;signs&gt; ...
     */
    String MGLIB_SIGNS = "signs"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/mglib &lt;hologram&gt; ...
     */
    String MGLIB_HOLOGRAM = "hologram"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/mglib &lt;statshologram&gt; ...
     */
    String MGLIB_STATS_HOLOGRAM = "statshologram"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/mglib &lt;gamemodetest&gt; ...
     */
    String MGLIB_GAMEMODE_TEST = "gamemodetest"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/mglib &lt;bungeetest&gt; ...
     */
    String MGLIB_BUNGEE_TEST = "bungeetest"; //$NON-NLS-1$
    
    /**
     * Command action: &quot;/mglib &lt;join&gt; ...
     */
    String MGLIB_JOIN = "join"; //$NON-NLS-1$
    
}
