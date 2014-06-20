package com.comze_instancelabs.minigamesapi.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.io.Serializable;
 
public class ArenaBlock implements Serializable
{
    private static final long serialVersionUID = -1894759842709524780L;
    
    public int x, y, z;
    public String world;
    public Material m;
 
    public ArenaBlock(Block b)
    {
    	m = b.getType();
        x = b.getX();
        y = b.getY();
        z = b.getZ();
        world = b.getWorld().getName();
    }
 
    public Block getBlock()
    {
        World w = Bukkit.getWorld(world);
        if (w == null)
            return null;
        Block b = w.getBlockAt(x, y, z);
        return b;
    }
    
    public Material getMaterial(){
    	return m;
    }
 
}