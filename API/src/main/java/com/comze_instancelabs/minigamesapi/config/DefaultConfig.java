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
package com.comze_instancelabs.minigamesapi.config;

import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.ArenaConfigStrings;

public class DefaultConfig
{
    
    JavaPlugin plugin;
    
    public DefaultConfig(final JavaPlugin plugin, final boolean custom)
    {
        this.plugin = plugin;
        DefaultConfig.init(plugin, custom);
    }
    
    public static void init(final JavaPlugin plugin, final boolean custom)
    {
        final FileConfiguration config = plugin.getConfig();
        config.options()
                .header("The default config. Check http://dev.bukkit.org/bukkit-plugins/instances-minigamesapi/#w-tutorials \n"
                        + "or https://github.com/instance01/MinigamesAPI/wiki/Default-Config-and-Item-Markup \n" + "for more information if you don't understand a config entry. \n"
                        + "You can find classes (kits) in classes.yml, all saved arenas in arenas.yml, all messages in messages.yml. \n" + "You can edit/disable achievements in achievements.yml.");
        if (!custom)
        {
            config.addDefault(ArenaConfigStrings.CONFIG_CLASS_SELECTION_ITEM, 399);
            config.addDefault(ArenaConfigStrings.CONFIG_EXIT_ITEM, 152);
            config.addDefault(ArenaConfigStrings.CONFIG_ACHIEVEMENT_ITEMS, 160);
            config.addDefault(ArenaConfigStrings.CONFIG_SPECTATOR_ITEM, 345);
            config.addDefault(ArenaConfigStrings.CONFIG_SHOP_SELECTION_ITEM, 388);
            config.addDefault(ArenaConfigStrings.CONFIG_CLASSES_GUI_ROWS, 3);
            config.addDefault(ArenaConfigStrings.CONFIG_SHOP_GUI_ROWS, 3);
            config.addDefault(ArenaConfigStrings.CONFIG_SPECTATOR_AFTER_FALL_OR_DEATH, true);
            config.addDefault(ArenaConfigStrings.CONFIG_SPECTATOR_MOVE_Y_LOCK, true);
            config.addDefault(ArenaConfigStrings.CONFIG_DEFAULT_MAX_PLAYERS, 4);
            config.addDefault(ArenaConfigStrings.CONFIG_DEFAULT_MIN_PLAYERS, 2);
            config.addDefault(ArenaConfigStrings.CONFIG_DEFAULT_MAX_GAME_TIME_IN_MINUTES, 30);
            config.addDefault(ArenaConfigStrings.CONFIG_LOBBY_COUNTDOWN, 30);
            config.addDefault(ArenaConfigStrings.CONFIG_INGAME_COUNTDOWN, 10);
            config.addDefault(ArenaConfigStrings.CONFIG_INGAME_COUNTDOWN_ENABLED, true);
            config.addDefault(ArenaConfigStrings.CONFIG_SKIP_LOBBY, false);
            config.addDefault(ArenaConfigStrings.CONFIG_CLEANINV_WHILE_INGAMECOUNTDOWN, false);
            
            config.addDefault(ArenaConfigStrings.CONFIG_CLASSES_ENABLED, true);
            config.addDefault(ArenaConfigStrings.CONFIG_SHOP_ENABLED, true);
            config.addDefault(ArenaConfigStrings.CONFIG_USE_CREADITS_INSTEAD_MONEY_FOR_KITS, false);
            config.addDefault(ArenaConfigStrings.CONFIG_RESET_INV_WHEN_LEAVING_SERVER, true);
            config.addDefault(ArenaConfigStrings.CONFIG_COLOR_BACKGROUND_WOOL, false);
            config.addDefault(ArenaConfigStrings.CONFIG_SHOW_CLASSES_WITHOUT_PERM, true);
            
            config.addDefault(ArenaConfigStrings.CONFIG_REWARDS_ECONOMY, true);
            config.addDefault(ArenaConfigStrings.CONFIG_REWARDS_ECONOMY_REWARD, 10);
            config.addDefault(ArenaConfigStrings.CONFIG_REWARDS_ITEM_REWARD, false);
            config.addDefault(ArenaConfigStrings.CONFIG_REWARDS_ITEM_REWARD_IDS, "264*1;11*1");
            config.addDefault(ArenaConfigStrings.CONFIG_REWARDS_COMMAND_REWARD, false);
            config.addDefault(ArenaConfigStrings.CONFIG_REWARDS_COMMAND, "pex user <player> add SKILLZ.*");
            config.addDefault(ArenaConfigStrings.CONFIG_REWARDS_ECONOMY_FOR_KILLS, true);
            config.addDefault(ArenaConfigStrings.CONFIG_REWARDS_ECONOMY_REWARD_FOR_KILLS, 5);
            config.addDefault(ArenaConfigStrings.CONFIG_REWARDS_COMMAND_REWARD_FOR_KILLS, false);
            config.addDefault(ArenaConfigStrings.CONFIG_REWARDS_COMMAND_FOR_KILLS, "pex user <player> add SKILLZ.*");
            config.addDefault(ArenaConfigStrings.CONFIG_REWARDS_ECONOMY_FOR_PARTICIPATION, false);
            config.addDefault(ArenaConfigStrings.CONFIG_REWARDS_ECONOMY_REWARD_FOR_PARTICIPATION, 5);
            config.addDefault(ArenaConfigStrings.CONFIG_REWARDS_COMMAND_REWARD_FOR_PARTICIPATION, false);
            config.addDefault(ArenaConfigStrings.CONFIG_REWARDS_COMMAND_FOR_PARTICIPATION, "pex user <player> add SKILLZ.*");
            
            config.addDefault(ArenaConfigStrings.CONFIG_STATS_POINTS_FOR_KILL, 2);
            config.addDefault(ArenaConfigStrings.CONFIG_STATS_POINTS_FOR_WIN, 10);
            
            config.addDefault(ArenaConfigStrings.CONFIG_ARCADE_ENABLED, true);
            config.addDefault(ArenaConfigStrings.CONFIG_ARCADE_MIN_PLAYERS, 1);
            config.addDefault(ArenaConfigStrings.CONFIG_ARCADE_MAX_PLAYERS, 16);
            config.addDefault(ArenaConfigStrings.CONFIG_ARCADE_ARENA_TO_PREFER_ENABLED, false);
            config.addDefault(ArenaConfigStrings.CONFIG_ARCADE_ARENA_TO_PREFER_ARENA, "arena1");
            config.addDefault(ArenaConfigStrings.CONFIG_ARCADE_LOBBY_COUNTDOWN, 20);
            config.addDefault(ArenaConfigStrings.CONFIG_ARCADE_SHOW_EACH_LOBBY_COUNTDOWN, false);
            config.addDefault(ArenaConfigStrings.CONFIG_ARCADE_INFINITE_ENABLED, false);
            config.addDefault(ArenaConfigStrings.CONFIG_ARCADE_INFINITE_SECONDS_TO_NEW_ROUND, 10);
            config.addDefault(ArenaConfigStrings.CONFIG_BUNGEE_GAME_ON_JOIN, false);
            config.addDefault(ArenaConfigStrings.CONFIG_BUNGEE_TELEPORT_ALL_TO_SERVER_ON_STOP_TP, false);
            config.addDefault(ArenaConfigStrings.CONFIG_BUNGEE_TELEPORT_ALL_TO_SERVER_ON_STOP_SERVER, "lobby");
            config.addDefault(ArenaConfigStrings.CONFIG_BUNGEE_WHITELIST_WHILE_GAME_RUNNING, false);
            config.addDefault(ArenaConfigStrings.CONFIG_EXECUTE_CMDS_ON_STOP, false);
            config.addDefault(ArenaConfigStrings.CONFIG_CMDS, "");
            config.addDefault(ArenaConfigStrings.CONFIG_CMDS_AFTER, "say SERVER STOPPING;stop");
            config.addDefault(ArenaConfigStrings.CONFIG_MAP_ROTATION, false);
            config.addDefault(ArenaConfigStrings.CONFIG_BROADCAST_WIN, true);
            config.addDefault(ArenaConfigStrings.CONFIG_BUY_CLASSES_FOREVER, true);
            config.addDefault(ArenaConfigStrings.CONFIG_DISABLE_COMMANDS_IN_ARENA, true);
            config.addDefault(ArenaConfigStrings.CONFIG_COMMAND_WHITELIST, "/msg,/pm,/help");
            config.addDefault(ArenaConfigStrings.CONFIG_LEAVE_COMMAND, "/leave");
            config.addDefault(ArenaConfigStrings.CONFIG_SPAWN_FIREWORKS_FOR_WINNERS, true);
            config.addDefault(ArenaConfigStrings.CONFIG_POWERUP_BROADCAST, false);
            config.addDefault(ArenaConfigStrings.CONFIG_POWERUP_FIREWORKS, false);
            config.addDefault(ArenaConfigStrings.CONFIG_USE_CUSTOM_SCOREBOARD, false);
            config.addDefault(ArenaConfigStrings.CONFIG_USE_SPECTATOR_SCOREBOARD, true);
            config.addDefault(ArenaConfigStrings.CONFIG_DELAY_ENABLED, false);
            config.addDefault(ArenaConfigStrings.CONFIG_DELAY_AMOUNT_SECONDS, 5);
            config.addDefault(ArenaConfigStrings.CONFIG_SEND_GAME_STARTED_MSG, false);
            config.addDefault(ArenaConfigStrings.CONFIG_AUTO_ADD_DEFAULT_KIT, true);
            config.addDefault(ArenaConfigStrings.CONFIG_LAST_MAN_STANDING_WINS, true);
            config.addDefault(ArenaConfigStrings.CONFIG_EFFECTS_BLOOD, true);
            config.addDefault(ArenaConfigStrings.CONFIG_EFFECTS_DMG_IDENTIFIER_HOLO, true);
            config.addDefault(ArenaConfigStrings.CONFIG_EFFECTS_DEAD_IN_FAKE_BED, true);
            config.addDefault(ArenaConfigStrings.CONFIG_EFFECTS_1_8_TITLES, true);
            config.addDefault(ArenaConfigStrings.CONFIG_EFFECTS_1_8_SPECTATOR_MODE, false);
            config.addDefault(ArenaConfigStrings.CONFIG_SOUNDS_LOBBY_COUNTDOWN, "none");
            config.addDefault(ArenaConfigStrings.CONFIG_SOUNDS_INGAME_COUNTDOWN, "SUCCESSFUL_HIT");
            config.addDefault(ArenaConfigStrings.CONFIG_CHAT_PER_ARENA_ONLY, false);
            config.addDefault(ArenaConfigStrings.CONFIG_CHAT_SHOW_SCORE_IN_ARENA, false);
            config.addDefault(ArenaConfigStrings.CONFIG_COMPASS_TRACKING_ENABLED, true);
            config.addDefault(ArenaConfigStrings.CONFIG_ALLOW_CLASSES_SELECTION_OUT_OF_ARENAS, false);
            config.addDefault(ArenaConfigStrings.CONFIG_SEND_STATS_ON_STOP, true);
            config.addDefault(ArenaConfigStrings.CONFIG_USE_XP_BAR_LEVEL, true);
            config.addDefault(ArenaConfigStrings.CONFIG_USE_OLD_RESET_METHOD, false);
            config.addDefault(ArenaConfigStrings.CONFIG_CHAT_ENABLED, true);
            
            config.addDefault(ArenaConfigStrings.CONFIG_EXTRA_LOBBY_ITEM_PREFIX + "item0" + ArenaConfigStrings.CONFIG_EXTRA_LOBBY_ITEM_ENABLED_SUFFIX, false);
            config.addDefault(ArenaConfigStrings.CONFIG_EXTRA_LOBBY_ITEM_PREFIX + "item0" + ArenaConfigStrings.CONFIG_EXTRA_LOBBY_ITEM_ITEM_SUFFIX, 9);
            config.addDefault(ArenaConfigStrings.CONFIG_EXTRA_LOBBY_ITEM_PREFIX + "item0" + ArenaConfigStrings.CONFIG_EXTRA_LOBBY_ITEM_NAME_SUFFIX, "Custom Name".replace("&", "§"));
            config.addDefault(ArenaConfigStrings.CONFIG_EXTRA_LOBBY_ITEM_PREFIX + "item0" + ArenaConfigStrings.CONFIG_EXTRA_LOBBY_ITEM_COMMAND_SUFFIX, "say This is a custom extra lobby item.");

            config.addDefault(ArenaConfigStrings.CONFIG_MYSQL_ENABLED, false);
            config.addDefault(ArenaConfigStrings.CONFIG_MYSQL_HOST, "127.0.0.1");
            config.addDefault(ArenaConfigStrings.CONFIG_MYSQL_USER, "root");
            config.addDefault(ArenaConfigStrings.CONFIG_MYSQL_PW, "root");
            config.addDefault(ArenaConfigStrings.CONFIG_MYSQL_DATABASE, "mcminigames");
            
            config.addDefault(ArenaConfigStrings.CONFIG_SQLITE_ENABLED, false);
            config.addDefault(ArenaConfigStrings.CONFIG_SQLITE_USER, "root");
            config.addDefault(ArenaConfigStrings.CONFIG_SQLITE_PW, "root");
            config.addDefault(ArenaConfigStrings.CONFIG_SQLITE_DATABASE, "mcminigames.sqlite");
            
            config.addDefault(ArenaConfigStrings.RESET_GAMEMMODE, true);
            config.addDefault(ArenaConfigStrings.RESET_INVENTORY, true);
            config.addDefault(ArenaConfigStrings.RESET_XP, true);
        }
        config.options().copyDefaults(true);
        plugin.saveConfig();
        
        try
        {
            DefaultConfig.convert(plugin);
        }
        catch (final Exception e)
        {
            // silently ignore
        }
    }
    
    public static void convert(final JavaPlugin plugin) throws IOException
    {
        final FileConfiguration config = plugin.getConfig();
        if (!config.isSet("config.version"))
        {
            // TODO new config
            config.set(ArenaConfigStrings.CONFIG_CLASS_SELECTION_ITEM, config.get("config.classes_selection_item"));
            config.set(ArenaConfigStrings.CONFIG_EXIT_ITEM, config.get("config.exit_item"));
            config.set(ArenaConfigStrings.CONFIG_ACHIEVEMENT_ITEMS, config.get("config.achievement_item"));
            config.set(ArenaConfigStrings.CONFIG_SPECTATOR_ITEM, config.get("config.exit_item"));
            config.set(ArenaConfigStrings.CONFIG_SHOP_SELECTION_ITEM, config.get("config.shop_selection_item"));
            config.set(ArenaConfigStrings.CONFIG_CLASSES_GUI_ROWS, config.get("config.classes_gui_rows"));
            config.set(ArenaConfigStrings.CONFIG_SHOP_GUI_ROWS, config.get("config.shop_gui_rows"));
            config.set(ArenaConfigStrings.CONFIG_SPECTATOR_AFTER_FALL_OR_DEATH, config.get("config.spectator_after_fall_or_death"));
            config.set(ArenaConfigStrings.CONFIG_SPECTATOR_MOVE_Y_LOCK, config.get("config.spectator_move_y_lock"));
            config.set(ArenaConfigStrings.CONFIG_DEFAULT_MAX_PLAYERS, config.get("config.default_max_players"));
            config.set(ArenaConfigStrings.CONFIG_DEFAULT_MIN_PLAYERS, config.get("config.default_min_players"));
            config.set(ArenaConfigStrings.CONFIG_DEFAULT_MAX_GAME_TIME_IN_MINUTES, config.get("config.default_max_game_time_in_minutes"));
            config.set(ArenaConfigStrings.CONFIG_LOBBY_COUNTDOWN, config.get("config.lobby_countdown"));
            config.set(ArenaConfigStrings.CONFIG_INGAME_COUNTDOWN, config.get("config.ingame_countdown"));
            config.set(ArenaConfigStrings.CONFIG_INGAME_COUNTDOWN_ENABLED, config.get("config.ingame_countdown_enabled"));
            config.set(ArenaConfigStrings.CONFIG_SKIP_LOBBY, config.get("config.skip_lobby"));
            config.set(ArenaConfigStrings.CONFIG_CLEANINV_WHILE_INGAMECOUNTDOWN, config.get("config.clearinv_while_ingamecountdown"));
            
            config.set("config.classes_selection_item", null);
            config.set("config.exit_item", null);
            config.set("config.achievement_item", null);
            config.set("config.shop_selection_item", null);
            config.set("config.classes_gui_rows", null);
            config.set("config.shop_gui_rows", null);
            config.set("config.clearinv_while_ingamecountdown", null);
            config.set("config.spectator_after_fall_or_death", null);
            config.set("config.spectator_move_y_lock", null);
            config.set("config.default_max_players", null);
            config.set("config.default_min_players", null);
            config.set("config.default_max_game_time_in_minutes", null);
            config.set("config.lobby_countdown", null);
            config.set("config.ingame_countdown", null);
            config.set("config.ingame_countdown_enabled", null);
            config.set("config.skip_lobby", null);
            
            config.set("config.version", 1);
            plugin.saveConfig();
        }
    }
    
}
