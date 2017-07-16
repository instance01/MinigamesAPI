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
 * Strings for arenas config.
 * 
 * @author mepeisen
 */
public interface ArenaConfigStrings
{
    
    /** arenas lower boundary. */
    String BOUNDS_LOW = "bounds.low"; //$NON-NLS-1$
    
    /** arenas higher boundary. */
    String BOUNDS_HIGH = "bounds.high"; //$NON-NLS-1$
    
    /** arenas lobby lower boundary. */
    String LOBBY_BOUNDS_LOW = "lobbybounds.bounds.low"; //$NON-NLS-1$
    
    /** arenas lobby higher boundary. */
    String LOBBY_BOUNDS_HIGH = "lobbybounds.bounds.high"; //$NON-NLS-1$
    
    /** arenas spectator lower boundary. */
    String SPEC_BOUNDS_LOW = "specbounds.bounds.low"; //$NON-NLS-1$
    
    /** arenas spectator higher boundary. */
    String SPEC_BOUNDS_HIGH = "specbounds.bounds.high"; //$NON-NLS-1$
    
    /** arenas spectator spawn location. */
    String SPEC_SPAWN = "specspawn"; //$NON-NLS-1$
    
    
    
    /** prefix for arenas config. */
    String ARENAS_PREFIX = "arenas."; //$NON-NLS-1$
    
    /** suffix for arenas display name. */
    String DISPLAYNAME_SUFFIX = ".displayname"; //$NON-NLS-1$
    
    /** suffix for arenas author. */
    String AUTHOR_SUFFIX = ".author"; //$NON-NLS-1$
    
    /** suffix for arenas description. */
    String DESCRIPTION_SUFFIX = ".description"; //$NON-NLS-1$
    
    /** suffix for smart reset activation. */
    String SMART_RESET_SUFFIX = ".smart_reset"; //$NON-NLS-1$
    
    
    
    /** TODO: describe config option. */
    String CONFIG_CLASS_SELECTION_ITEM = "config.selection_items.classes_selection_item"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_EXIT_ITEM = "config.selection_items.exit_item"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_ACHIEVEMENT_ITEMS = "config.selection_items.achievement_item"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_SPECTATOR_ITEM = "config.selection_items.spectator_item"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_SHOP_SELECTION_ITEM = "config.selection_items.shop_selection_item"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_CLASSES_GUI_ROWS = "config.GUI.classes_gui_rows"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_SHOP_GUI_ROWS = "config.GUI.shop_gui_rows"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_SPECTATOR_AFTER_FALL_OR_DEATH = "config.spectator.spectator_after_fall_or_death"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_SPECTATOR_MOVE_Y_LOCK = "config.spectator.spectator_move_y_lock"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_DEFAULT_MAX_PLAYERS = "config.defaults.default_max_players"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_DEFAULT_MIN_PLAYERS = "config.defaults.default_min_players"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_DEFAULT_MAX_GAME_TIME_IN_MINUTES = "config.defaults.default_max_game_time_in_minutes"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_LOBBY_COUNTDOWN = "config.countdowns.lobby_countdown"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_INGAME_COUNTDOWN = "config.countdowns.ingame_countdown"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_INGAME_COUNTDOWN_ENABLED = "config.countdowns.ingame_countdown_enabled"; //$NON-NLS-1$
    
    /** TODO: describe config option. */
    String CONFIG_SKIP_LOBBY = "config.countdowns.skip_lobby"; //$NON-NLS-1$
    
    /** TODO: describe config option. */
    String CONFIG_CLEANINV_WHILE_INGAMECOUNTDOWN = "config.countdowns.clearinv_while_ingamecountdown"; //$NON-NLS-1$
    
    /** TODO: describe config option. */
    String CONFIG_CLASSES_ENABLED = "config.classes_enabled"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_SHOP_ENABLED = "config.shop_enabled"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_USE_CREADITS_INSTEAD_MONEY_FOR_KITS = "config.use_credits_instead_of_money_for_kits"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_RESET_INV_WHEN_LEAVING_SERVER = "config.reset_inventory_when_players_leave_server"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_COLOR_BACKGROUND_WOOL = "config.color_background_wool_of_signs"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_SHOW_CLASSES_WITHOUT_PERM = "config.show_classes_without_usage_permission"; //$NON-NLS-1$
    
    /** {@code true} to gain economy rewards on win. */
    String CONFIG_REWARDS_ECONOMY = "config.rewards.economy"; //$NON-NLS-1$
    /** Amount of money to give for win. */
    String CONFIG_REWARDS_ECONOMY_REWARD = "config.rewards.economy_reward"; //$NON-NLS-1$
    /** {@code true} to gain item rewards on win. */
    String CONFIG_REWARDS_ITEM_REWARD = "config.rewards.item_reward"; //$NON-NLS-1$
    /** the items wo give for win. */
    String CONFIG_REWARDS_ITEM_REWARD_IDS = "config.rewards.item_reward_ids"; //$NON-NLS-1$
    /** {@code true} to execute a command on win. */
    String CONFIG_REWARDS_COMMAND_REWARD = "config.rewards.command_reward"; //$NON-NLS-1$
    /** the command to execute for win. */
    String CONFIG_REWARDS_COMMAND = "config.rewards.command"; //$NON-NLS-1$
    /** {@code true} to gain economy rewards on kills. */
    String CONFIG_REWARDS_ECONOMY_FOR_KILLS = "config.rewards.economy_for_kills"; //$NON-NLS-1$
    /** money to give per kill. */
    String CONFIG_REWARDS_ECONOMY_REWARD_FOR_KILLS = "config.rewards.economy_reward_for_kills"; //$NON-NLS-1$
    /** {@code true} to gain a command for kills. */
    String CONFIG_REWARDS_COMMAND_REWARD_FOR_KILLS = "config.rewards.command_reward_for_kills"; //$NON-NLS-1$
    /** command to execute for kills. */
    String CONFIG_REWARDS_COMMAND_FOR_KILLS = "config.rewards.command_for_kills"; //$NON-NLS-1$
    /** {@code true} to gain economy rewards for participation. */
    String CONFIG_REWARDS_ECONOMY_FOR_PARTICIPATION = "config.rewards.economy_for_participation"; //$NON-NLS-1$
    /** money to give for participation. */
    String CONFIG_REWARDS_ECONOMY_REWARD_FOR_PARTICIPATION = "config.rewards.economy_reward_for_participation"; //$NON-NLS-1$
    /** {@code true} to execute a command for participation. */
    String CONFIG_REWARDS_COMMAND_REWARD_FOR_PARTICIPATION = "config.rewards.command_reward_for_participation"; //$NON-NLS-1$
    /** command to execute for participation. */
    String CONFIG_REWARDS_COMMAND_FOR_PARTICIPATION = "config.rewards.command_for_participation"; //$NON-NLS-1$
    
    /** TODO: describe config option. */
    String CONFIG_STATS_POINTS_FOR_KILL = "config.stats.points_for_kill"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_STATS_POINTS_FOR_WIN = "config.stats.points_for_win"; //$NON-NLS-1$
    
    /** TODO: describe config option. */
    String CONFIG_ARCADE_ENABLED = "config.arcade.enabled"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_ARCADE_MIN_PLAYERS = "config.arcade.min_players"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_ARCADE_MAX_PLAYERS = "config.arcade.max_players"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_ARCADE_ARENA_TO_PREFER_ENABLED = "config.arcade.arena_to_prefer.enabled"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_ARCADE_ARENA_TO_PREFER_ARENA = "config.arcade.arena_to_prefer.arena"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_ARCADE_LOBBY_COUNTDOWN = "config.arcade.lobby_countdown"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_ARCADE_SHOW_EACH_LOBBY_COUNTDOWN = "config.arcade.show_each_lobby_countdown"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_ARCADE_INFINITE_ENABLED = "config.arcade.infinite_mode.enabled"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_ARCADE_INFINITE_SECONDS_TO_NEW_ROUND = "config.arcade.infinite_mode.seconds_to_new_round"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_BUNGEE_GAME_ON_JOIN = "config.bungee.game_on_join"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_BUNGEE_TELEPORT_ALL_TO_SERVER_ON_STOP_TP = "config.bungee.teleport_all_to_server_on_stop.tp"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_BUNGEE_TELEPORT_ALL_TO_SERVER_ON_STOP_SERVER = "config.bungee.teleport_all_to_server_on_stop.server"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_BUNGEE_WHITELIST_WHILE_GAME_RUNNING = "config.bungee.whitelist_while_game_running"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_EXECUTE_CMDS_ON_STOP = "config.execute_cmds_on_stop"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_CMDS = "config.cmds"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_CMDS_AFTER = "config.cmds_after"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_MAP_ROTATION = "config.map_rotation"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_BROADCAST_WIN = "config.broadcast_win"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_BUY_CLASSES_FOREVER = "config.buy_classes_forever"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_DISABLE_COMMANDS_IN_ARENA = "config.disable_commands_in_arena"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_COMMAND_WHITELIST = "config.command_whitelist"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_LEAVE_COMMAND = "config.leave_command"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_SPAWN_FIREWORKS_FOR_WINNERS = "config.spawn_fireworks_for_winners"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_POWERUP_BROADCAST = "config.powerup_spawning.broadcast"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_POWERUP_FIREWORKS = "config.powerup_spawning.spawn_firework"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_USE_CUSTOM_SCOREBOARD = "config.use_custom_scoreboard"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_USE_SPECTATOR_SCOREBOARD = "config.use_spectator_scoreboard"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_DELAY_ENABLED = "config.delay.enabled"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_DELAY_AMOUNT_SECONDS = "config.delay.amount_seconds"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_SEND_GAME_STARTED_MSG = "config.send_game_started_msg"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_AUTO_ADD_DEFAULT_KIT = "config.auto_add_default_kit"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_LAST_MAN_STANDING_WINS = "config.last_man_standing_wins"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_EFFECTS_BLOOD = "config.effects.blood"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_EFFECTS_DMG_IDENTIFIER_HOLO = "config.effects.damage_identifier_holograms"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_EFFECTS_DEAD_IN_FAKE_BED = "config.effects.dead_in_fake_bed"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_EFFECTS_1_8_TITLES = "config.effects.1_8_titles"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_EFFECTS_1_8_SPECTATOR_MODE = "config.effects.1_8_spectator_mode"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_SOUNDS_LOBBY_COUNTDOWN = "config.sounds.lobby_countdown"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_SOUNDS_INGAME_COUNTDOWN = "config.sounds.ingame_countdown"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_CHAT_PER_ARENA_ONLY = "config.chat_per_arena_only"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_CHAT_SHOW_SCORE_IN_ARENA = "config.chat_show_score_in_arena"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_COMPASS_TRACKING_ENABLED = "config.compass_tracking_enabled"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_ALLOW_CLASSES_SELECTION_OUT_OF_ARENAS = "config.allow_classes_selection_out_of_arenas"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_SEND_STATS_ON_STOP = "config.send_stats_on_stop"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_USE_XP_BAR_LEVEL = "config.use_xp_bar_level"; //$NON-NLS-1$
    
    /** 
     * {@code true} for using old reset (from files); {@code false} for using smart reset
     * @deprecated will be removed in 1.4.10
     */
    @Deprecated
    String CONFIG_USE_OLD_RESET_METHOD = "config.use_old_reset_method"; //$NON-NLS-1$
    
    /** TODO: describe config option. */
    String CONFIG_CHAT_ENABLED = "config.chat_enabled"; //$NON-NLS-1$
    
    /** TODO: describe config option. */
    String CONFIG_EXTRA_LOBBY_ITEM_PREFIX = "config.extra_lobby_item."; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_EXTRA_LOBBY_ITEM_ENABLED_SUFFIX = ".enabled"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_EXTRA_LOBBY_ITEM_ITEM_SUFFIX = ".item"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_EXTRA_LOBBY_ITEM_NAME_SUFFIX = ".name"; //$NON-NLS-1$
    /** TODO: describe config option. */
    String CONFIG_EXTRA_LOBBY_ITEM_COMMAND_SUFFIX = ".command"; //$NON-NLS-1$
    
    /** flag to enable mysql */
    String CONFIG_MYSQL_ENABLED = "mysql.enabled"; //$NON-NLS-1$
    /** host name of mysql server */
    String CONFIG_MYSQL_HOST = "mysql.host"; //$NON-NLS-1$
    /** user name of mysql user */
    String CONFIG_MYSQL_USER = "mysql.user"; //$NON-NLS-1$
    /** password to connect to mysql */
    String CONFIG_MYSQL_PW = "mysql.pw"; //$NON-NLS-1$
    /** mysql database name */
    String CONFIG_MYSQL_DATABASE = "mysql.database"; //$NON-NLS-1$
    
    /** flag to enable sqlite */
    String CONFIG_SQLITE_ENABLED = "sqlite.enabled"; //$NON-NLS-1$
    /** sqlite database user name */
    String CONFIG_SQLITE_USER = "sqlite.user"; //$NON-NLS-1$
    /** sqlite database password */
    String CONFIG_SQLITE_PW = "sqlite.pw"; //$NON-NLS-1$
    /** sqlite database name/file. */
    String CONFIG_SQLITE_DATABASE = "sqlite.database"; //$NON-NLS-1$
    
    /**
     * Activation of inventory reset on leave.
     */
    String RESET_INVENTORY = "config.reset_on_leave.inventory"; //$NON-NLS-1$

    /**
     * Activation of xp reset on leave.
     */
    String RESET_XP = "config.reset_on_leave.xp"; //$NON-NLS-1$

    /**
     * Activation of gamemode reset on leave.
     */
    String RESET_GAMEMMODE = "config.reset_on_leave.gamemode"; //$NON-NLS-1$
    
}
