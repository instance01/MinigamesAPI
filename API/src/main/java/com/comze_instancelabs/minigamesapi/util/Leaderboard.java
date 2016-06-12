package com.comze_instancelabs.minigamesapi.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.configuration.file.FileConfiguration;
 
public class Leaderboard {
       
        protected Object convertUUID(String s) {
                if (s.matches("[0-9a-f]{8}-[0-9a-f]{4}-4[0-9]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
                        return UUID.fromString(s);
                } else {
                        return s;
                }
        }
       
         protected static Map<Integer, String> sortByComparator(Map<String, Integer> unsortMap, final boolean order) {
                 List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());
                 
                 Collections.sort(list, new Comparator<Entry<String, Integer>>() {
                         public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                                 if (order) {
                                         return o1.getValue().compareTo(o2.getValue());
                                 } else {
                                         return o2.getValue().compareTo(o1.getValue());
                                 }
                         }
                 });
                 
                 Map<Integer, String> sortedMap = new LinkedHashMap<Integer, String>();
                 for (Entry<String, Integer> entry : list) {
                         for (int i = 1; i < 4; i++) {
                                 if (!sortedMap.containsKey(i)) {
                                         sortedMap.put(i, entry.getKey());
                                         break;
                                 }
                         }
                 }
                 
                 return sortedMap;
         }
 
         
         @SuppressWarnings("deprecation")
         public Leaderboard(Location firstPlace, Location secondPlace, Location thirdPlace, Material firstBlockType, Material secondBlockType, Material thirdBlockType, BlockFace direction, FileConfiguration configurationFile, String configurationFormat) {
                 String[] a = configurationFormat.split("\\.");
                 
                 int playerConfigLocation = -1;
                 
                 for (int i = 0; i < a.length; i++) {
                         if (a[i].equals("player")) {
                                 playerConfigLocation = i;
                         }
                 }
                 
                 if (playerConfigLocation == -1) return;
               
                 ArrayList<String> b = new ArrayList<String>();
               
                 for (String ba : a) {
                         if (!ba.equals("player")) {
                                 b.add(ba);
                         } else {
                                 break;
                         }
                 }
               
                 ArrayList<String> c = new ArrayList<String>();
                 
                 for (String ca : a) {
                         if (!ca.equals("integer")) {
                                 c.add(ca);
                         } else {
                                 break;
                         }
                 }
               
                 HashMap<String, Integer> playersEdit = new HashMap<String, Integer>();
                 HashMap<String, Integer> playersKeep = new HashMap<String, Integer>();
               
                 for (String player : configurationFile.getConfigurationSection(b.toString().replace("[", "").replace("]", "").replace(", ", ".")).getKeys(false)) {
                         playersEdit.put(player, configurationFile.getInt(c.toString().replace("[", "").replace("]", "").replace(", ", ".").replace("player", player)));
                         playersKeep.put(player, configurationFile.getInt(c.toString().replace("[", "").replace("]", "").replace(", ", ".").replace("player", player)));
                 }
               
                 Map<Integer, String> leaderboard = sortByComparator(playersEdit, false);
                 
                 firstPlace.getBlock().setType(firstBlockType);
                 secondPlace.getBlock().setType(secondBlockType);
                 thirdPlace.getBlock().setType(thirdBlockType);
                 
                 ArrayList<BlockState> signBlockState = new ArrayList<BlockState>();
                 
                 Block firstSign = firstPlace.getBlock().getRelative(direction);
                 Block secondSign = secondPlace.getBlock().getRelative(direction);
                 Block thirdSign = thirdPlace.getBlock().getRelative(direction);
                 
                 switch (direction) {
                case EAST:
                        firstSign.setTypeIdAndData(68, (byte) 0x5, true);
                        secondSign.setTypeIdAndData(68, (byte) 0x5, true);
                        thirdSign.setTypeIdAndData(68, (byte) 0x5, true);
                        break;
                case NORTH:
                        firstSign.setTypeIdAndData(68, (byte) 0x2, true);
                        secondSign.setTypeIdAndData(68, (byte) 0x2, true);
                        thirdSign.setTypeIdAndData(68, (byte) 0x2, true);
                        break;
                case SOUTH:
                        firstSign.setTypeIdAndData(68, (byte) 0x3, true);
                        secondSign.setTypeIdAndData(68, (byte) 0x3, true);
                        thirdSign.setTypeIdAndData(68, (byte) 0x3, true);
                        break;
                case WEST:
                        firstSign.setTypeIdAndData(68, (byte) 0x4, true);
                        secondSign.setTypeIdAndData(68, (byte) 0x4, true);
                        thirdSign.setTypeIdAndData(68, (byte) 0x4, true);
                        break;
                default:
                        break;
                 
                 }
                 
                 signBlockState.add(firstSign.getState());
                 signBlockState.add(secondSign.getState());
                 signBlockState.add(thirdSign.getState());
               
                 for (BlockState bs : signBlockState) {
                         if (bs instanceof Sign) {
                                 Sign s = (Sign) bs;
                                 
                                 if (leaderboard.containsKey(signBlockState.indexOf(bs) + 1)) {
                                         if (convertUUID(leaderboard.get(signBlockState.indexOf(bs) + 1)) instanceof UUID) {
                                                 s.setLine(1, Bukkit.getPlayer((UUID) convertUUID(leaderboard.get(signBlockState.indexOf(bs) + 1))).getName());
                                         } else {
                                                 s.setLine(1, leaderboard.get(signBlockState.indexOf(bs) + 1));
                                         }
                                         
                                         switch (signBlockState.indexOf(bs) + 1) {
                                         case 1:
                                                 s.setLine(0, ChatColor.AQUA + "" + ChatColor.BOLD + "1st");
                                                 break;
                                         case 2:
                                                 s.setLine(0, ChatColor.YELLOW + "" + ChatColor.BOLD + "2st");
                                                 break;
                                         case 3:
                                                 s.setLine(0, ChatColor.DARK_RED + "" + ChatColor.BOLD + "3st");
                                                 break;
                                         }
                                 }
                                 s.setLine(2, playersKeep.get(leaderboard.get(signBlockState.indexOf(bs) + 1)) + " Points");
                                 bs.getBlock().getChunk().load();
                                 s.update(true);
                         }
                }
                 
                 Location firstSkull = firstPlace.getBlock().getLocation().add(0, 1, 0);
                 Location secondSkull = secondPlace.getBlock().getLocation().add(0, 1, 0);
                 Location thirdSkull = thirdPlace.getBlock().getLocation().add(0, 1, 0);
                 
                 firstSkull.getBlock().setType(Material.SKULL);
                 secondSkull.getBlock().setType(Material.SKULL);
                 thirdSkull.getBlock().setType(Material.SKULL);
                 
                 ArrayList<BlockState> skullBlockState = new ArrayList<BlockState>();
               
                 skullBlockState.add(firstSkull.getBlock().getState());
                 skullBlockState.add(secondSkull.getBlock().getState());
                 skullBlockState.add(thirdSkull.getBlock().getState());
                 
                 for (BlockState bs : skullBlockState) {
                         if (bs instanceof Skull) {
                                 Skull s = (Skull) bs;
                                 s.setSkullType(SkullType.PLAYER);
                                 s.setRawData((byte) 1);
                                 s.setRotation(direction);
                                 
                                 if (leaderboard.containsKey(skullBlockState.indexOf(bs) + 1)) {
                                         if (convertUUID(leaderboard.get(skullBlockState.indexOf(bs) + 1)) instanceof UUID) {
                                                 s.setOwner(Bukkit.getPlayer((UUID) convertUUID(leaderboard.get(skullBlockState.indexOf(bs) + 1))).getName());
                                         } else {
                                                 s.setOwner(leaderboard.get(skullBlockState.indexOf(bs) + 1));
                                         }
                                 }
                                 
                                 bs.getBlock().getChunk().load();
                                 s.update(true);
                         }
                 }
        }
}