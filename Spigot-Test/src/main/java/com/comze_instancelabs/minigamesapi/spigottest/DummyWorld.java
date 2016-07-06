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

package com.comze_instancelabs.minigamesapi.spigottest;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.BlockChangeDelegate;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 * @author mepeisen
 */
public class DummyWorld implements World
{

    /* (non-Javadoc)
     * @see org.bukkit.plugin.messaging.PluginMessageRecipient#sendPluginMessage(org.bukkit.plugin.Plugin, java.lang.String, byte[])
     */
    @Override
    public void sendPluginMessage(Plugin paramPlugin, String paramString, byte[] paramArrayOfByte)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.plugin.messaging.PluginMessageRecipient#getListeningPluginChannels()
     */
    @Override
    public Set<String> getListeningPluginChannels()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.metadata.Metadatable#setMetadata(java.lang.String, org.bukkit.metadata.MetadataValue)
     */
    @Override
    public void setMetadata(String paramString, MetadataValue paramMetadataValue)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.metadata.Metadatable#getMetadata(java.lang.String)
     */
    @Override
    public List<MetadataValue> getMetadata(String paramString)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.metadata.Metadatable#hasMetadata(java.lang.String)
     */
    @Override
    public boolean hasMetadata(String paramString)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.metadata.Metadatable#removeMetadata(java.lang.String, org.bukkit.plugin.Plugin)
     */
    @Override
    public void removeMetadata(String paramString, Plugin paramPlugin)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getBlockAt(int, int, int)
     */
    @Override
    public Block getBlockAt(int paramInt1, int paramInt2, int paramInt3)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getBlockAt(org.bukkit.Location)
     */
    @Override
    public Block getBlockAt(Location paramLocation)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getBlockTypeIdAt(int, int, int)
     */
    @Override
    public int getBlockTypeIdAt(int paramInt1, int paramInt2, int paramInt3)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getBlockTypeIdAt(org.bukkit.Location)
     */
    @Override
    public int getBlockTypeIdAt(Location paramLocation)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getHighestBlockYAt(int, int)
     */
    @Override
    public int getHighestBlockYAt(int paramInt1, int paramInt2)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getHighestBlockYAt(org.bukkit.Location)
     */
    @Override
    public int getHighestBlockYAt(Location paramLocation)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getHighestBlockAt(int, int)
     */
    @Override
    public Block getHighestBlockAt(int paramInt1, int paramInt2)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getHighestBlockAt(org.bukkit.Location)
     */
    @Override
    public Block getHighestBlockAt(Location paramLocation)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getChunkAt(int, int)
     */
    @Override
    public Chunk getChunkAt(int paramInt1, int paramInt2)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getChunkAt(org.bukkit.Location)
     */
    @Override
    public Chunk getChunkAt(Location paramLocation)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getChunkAt(org.bukkit.block.Block)
     */
    @Override
    public Chunk getChunkAt(Block paramBlock)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#isChunkLoaded(org.bukkit.Chunk)
     */
    @Override
    public boolean isChunkLoaded(Chunk paramChunk)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getLoadedChunks()
     */
    @Override
    public Chunk[] getLoadedChunks()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#loadChunk(org.bukkit.Chunk)
     */
    @Override
    public void loadChunk(Chunk paramChunk)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#isChunkLoaded(int, int)
     */
    @Override
    public boolean isChunkLoaded(int paramInt1, int paramInt2)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#isChunkInUse(int, int)
     */
    @Override
    public boolean isChunkInUse(int paramInt1, int paramInt2)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#loadChunk(int, int)
     */
    @Override
    public void loadChunk(int paramInt1, int paramInt2)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#loadChunk(int, int, boolean)
     */
    @Override
    public boolean loadChunk(int paramInt1, int paramInt2, boolean paramBoolean)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#unloadChunk(org.bukkit.Chunk)
     */
    @Override
    public boolean unloadChunk(Chunk paramChunk)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#unloadChunk(int, int)
     */
    @Override
    public boolean unloadChunk(int paramInt1, int paramInt2)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#unloadChunk(int, int, boolean)
     */
    @Override
    public boolean unloadChunk(int paramInt1, int paramInt2, boolean paramBoolean)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#unloadChunk(int, int, boolean, boolean)
     */
    @Override
    public boolean unloadChunk(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#unloadChunkRequest(int, int)
     */
    @Override
    public boolean unloadChunkRequest(int paramInt1, int paramInt2)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#unloadChunkRequest(int, int, boolean)
     */
    @Override
    public boolean unloadChunkRequest(int paramInt1, int paramInt2, boolean paramBoolean)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#regenerateChunk(int, int)
     */
    @Override
    public boolean regenerateChunk(int paramInt1, int paramInt2)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#refreshChunk(int, int)
     */
    @Override
    public boolean refreshChunk(int paramInt1, int paramInt2)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#dropItem(org.bukkit.Location, org.bukkit.inventory.ItemStack)
     */
    @Override
    public Item dropItem(Location paramLocation, ItemStack paramItemStack)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#dropItemNaturally(org.bukkit.Location, org.bukkit.inventory.ItemStack)
     */
    @Override
    public Item dropItemNaturally(Location paramLocation, ItemStack paramItemStack)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#spawnArrow(org.bukkit.Location, org.bukkit.util.Vector, float, float)
     */
    @Override
    public Arrow spawnArrow(Location paramLocation, Vector paramVector, float paramFloat1, float paramFloat2)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#spawnArrow(org.bukkit.Location, org.bukkit.util.Vector, float, float, java.lang.Class)
     */
    @Override
    public <T extends Arrow> T spawnArrow(Location paramLocation, Vector paramVector, float paramFloat1, float paramFloat2, Class<T> paramClass)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#generateTree(org.bukkit.Location, org.bukkit.TreeType)
     */
    @Override
    public boolean generateTree(Location paramLocation, TreeType paramTreeType)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#generateTree(org.bukkit.Location, org.bukkit.TreeType, org.bukkit.BlockChangeDelegate)
     */
    @Override
    public boolean generateTree(Location paramLocation, TreeType paramTreeType, BlockChangeDelegate paramBlockChangeDelegate)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#spawnEntity(org.bukkit.Location, org.bukkit.entity.EntityType)
     */
    @Override
    public Entity spawnEntity(Location paramLocation, EntityType paramEntityType)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#strikeLightning(org.bukkit.Location)
     */
    @Override
    public LightningStrike strikeLightning(Location paramLocation)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#strikeLightningEffect(org.bukkit.Location)
     */
    @Override
    public LightningStrike strikeLightningEffect(Location paramLocation)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getEntities()
     */
    @Override
    public List<Entity> getEntities()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getLivingEntities()
     */
    @Override
    public List<LivingEntity> getLivingEntities()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getEntitiesByClass(java.lang.Class[])
     */
    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... paramArrayOfClass)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getEntitiesByClass(java.lang.Class)
     */
    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> paramClass)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getEntitiesByClasses(java.lang.Class[])
     */
    @Override
    public Collection<Entity> getEntitiesByClasses(Class<?>... paramArrayOfClass)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getPlayers()
     */
    @Override
    public List<Player> getPlayers()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getNearbyEntities(org.bukkit.Location, double, double, double)
     */
    @Override
    public Collection<Entity> getNearbyEntities(Location paramLocation, double paramDouble1, double paramDouble2, double paramDouble3)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getName()
     */
    @Override
    public String getName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getUID()
     */
    @Override
    public UUID getUID()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getSpawnLocation()
     */
    @Override
    public Location getSpawnLocation()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#setSpawnLocation(int, int, int)
     */
    @Override
    public boolean setSpawnLocation(int paramInt1, int paramInt2, int paramInt3)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getTime()
     */
    @Override
    public long getTime()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#setTime(long)
     */
    @Override
    public void setTime(long paramLong)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getFullTime()
     */
    @Override
    public long getFullTime()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#setFullTime(long)
     */
    @Override
    public void setFullTime(long paramLong)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#hasStorm()
     */
    @Override
    public boolean hasStorm()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#setStorm(boolean)
     */
    @Override
    public void setStorm(boolean paramBoolean)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getWeatherDuration()
     */
    @Override
    public int getWeatherDuration()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#setWeatherDuration(int)
     */
    @Override
    public void setWeatherDuration(int paramInt)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#isThundering()
     */
    @Override
    public boolean isThundering()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#setThundering(boolean)
     */
    @Override
    public void setThundering(boolean paramBoolean)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getThunderDuration()
     */
    @Override
    public int getThunderDuration()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#setThunderDuration(int)
     */
    @Override
    public void setThunderDuration(int paramInt)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#createExplosion(double, double, double, float)
     */
    @Override
    public boolean createExplosion(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#createExplosion(double, double, double, float, boolean)
     */
    @Override
    public boolean createExplosion(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat, boolean paramBoolean)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#createExplosion(double, double, double, float, boolean, boolean)
     */
    @Override
    public boolean createExplosion(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat, boolean paramBoolean1, boolean paramBoolean2)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#createExplosion(org.bukkit.Location, float)
     */
    @Override
    public boolean createExplosion(Location paramLocation, float paramFloat)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#createExplosion(org.bukkit.Location, float, boolean)
     */
    @Override
    public boolean createExplosion(Location paramLocation, float paramFloat, boolean paramBoolean)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getEnvironment()
     */
    @Override
    public Environment getEnvironment()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getSeed()
     */
    @Override
    public long getSeed()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getPVP()
     */
    @Override
    public boolean getPVP()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#setPVP(boolean)
     */
    @Override
    public void setPVP(boolean paramBoolean)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getGenerator()
     */
    @Override
    public ChunkGenerator getGenerator()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#save()
     */
    @Override
    public void save()
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getPopulators()
     */
    @Override
    public List<BlockPopulator> getPopulators()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#spawn(org.bukkit.Location, java.lang.Class)
     */
    @Override
    public <T extends Entity> T spawn(Location paramLocation, Class<T> paramClass) throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#spawnFallingBlock(org.bukkit.Location, org.bukkit.Material, byte)
     */
    @Override
    public FallingBlock spawnFallingBlock(Location paramLocation, Material paramMaterial, byte paramByte) throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#spawnFallingBlock(org.bukkit.Location, int, byte)
     */
    @Override
    public FallingBlock spawnFallingBlock(Location paramLocation, int paramInt, byte paramByte) throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#playEffect(org.bukkit.Location, org.bukkit.Effect, int)
     */
    @Override
    public void playEffect(Location paramLocation, Effect paramEffect, int paramInt)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#playEffect(org.bukkit.Location, org.bukkit.Effect, int, int)
     */
    @Override
    public void playEffect(Location paramLocation, Effect paramEffect, int paramInt1, int paramInt2)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#playEffect(org.bukkit.Location, org.bukkit.Effect, java.lang.Object)
     */
    @Override
    public <T> void playEffect(Location paramLocation, Effect paramEffect, T paramT)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#playEffect(org.bukkit.Location, org.bukkit.Effect, java.lang.Object, int)
     */
    @Override
    public <T> void playEffect(Location paramLocation, Effect paramEffect, T paramT, int paramInt)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getEmptyChunkSnapshot(int, int, boolean, boolean)
     */
    @Override
    public ChunkSnapshot getEmptyChunkSnapshot(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#setSpawnFlags(boolean, boolean)
     */
    @Override
    public void setSpawnFlags(boolean paramBoolean1, boolean paramBoolean2)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getAllowAnimals()
     */
    @Override
    public boolean getAllowAnimals()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getAllowMonsters()
     */
    @Override
    public boolean getAllowMonsters()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getBiome(int, int)
     */
    @Override
    public Biome getBiome(int paramInt1, int paramInt2)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#setBiome(int, int, org.bukkit.block.Biome)
     */
    @Override
    public void setBiome(int paramInt1, int paramInt2, Biome paramBiome)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getTemperature(int, int)
     */
    @Override
    public double getTemperature(int paramInt1, int paramInt2)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getHumidity(int, int)
     */
    @Override
    public double getHumidity(int paramInt1, int paramInt2)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getMaxHeight()
     */
    @Override
    public int getMaxHeight()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getSeaLevel()
     */
    @Override
    public int getSeaLevel()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getKeepSpawnInMemory()
     */
    @Override
    public boolean getKeepSpawnInMemory()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#setKeepSpawnInMemory(boolean)
     */
    @Override
    public void setKeepSpawnInMemory(boolean paramBoolean)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#isAutoSave()
     */
    @Override
    public boolean isAutoSave()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#setAutoSave(boolean)
     */
    @Override
    public void setAutoSave(boolean paramBoolean)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#setDifficulty(org.bukkit.Difficulty)
     */
    @Override
    public void setDifficulty(Difficulty paramDifficulty)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getDifficulty()
     */
    @Override
    public Difficulty getDifficulty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getWorldFolder()
     */
    @Override
    public File getWorldFolder()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getWorldType()
     */
    @Override
    public WorldType getWorldType()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#canGenerateStructures()
     */
    @Override
    public boolean canGenerateStructures()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getTicksPerAnimalSpawns()
     */
    @Override
    public long getTicksPerAnimalSpawns()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#setTicksPerAnimalSpawns(int)
     */
    @Override
    public void setTicksPerAnimalSpawns(int paramInt)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getTicksPerMonsterSpawns()
     */
    @Override
    public long getTicksPerMonsterSpawns()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#setTicksPerMonsterSpawns(int)
     */
    @Override
    public void setTicksPerMonsterSpawns(int paramInt)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getMonsterSpawnLimit()
     */
    @Override
    public int getMonsterSpawnLimit()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#setMonsterSpawnLimit(int)
     */
    @Override
    public void setMonsterSpawnLimit(int paramInt)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getAnimalSpawnLimit()
     */
    @Override
    public int getAnimalSpawnLimit()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#setAnimalSpawnLimit(int)
     */
    @Override
    public void setAnimalSpawnLimit(int paramInt)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getWaterAnimalSpawnLimit()
     */
    @Override
    public int getWaterAnimalSpawnLimit()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#setWaterAnimalSpawnLimit(int)
     */
    @Override
    public void setWaterAnimalSpawnLimit(int paramInt)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getAmbientSpawnLimit()
     */
    @Override
    public int getAmbientSpawnLimit()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#setAmbientSpawnLimit(int)
     */
    @Override
    public void setAmbientSpawnLimit(int paramInt)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#playSound(org.bukkit.Location, org.bukkit.Sound, float, float)
     */
    @Override
    public void playSound(Location paramLocation, Sound paramSound, float paramFloat1, float paramFloat2)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#playSound(org.bukkit.Location, java.lang.String, float, float)
     */
    @Override
    public void playSound(Location paramLocation, String paramString, float paramFloat1, float paramFloat2)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getGameRules()
     */
    @Override
    public String[] getGameRules()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getGameRuleValue(java.lang.String)
     */
    @Override
    public String getGameRuleValue(String paramString)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#setGameRuleValue(java.lang.String, java.lang.String)
     */
    @Override
    public boolean setGameRuleValue(String paramString1, String paramString2)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#isGameRule(java.lang.String)
     */
    @Override
    public boolean isGameRule(String paramString)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#spigot()
     */
    @Override
    public Spigot spigot()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#getWorldBorder()
     */
    @Override
    public WorldBorder getWorldBorder()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#spawnParticle(org.bukkit.Particle, org.bukkit.Location, int)
     */
    @Override
    public void spawnParticle(Particle paramParticle, Location paramLocation, int paramInt)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#spawnParticle(org.bukkit.Particle, double, double, double, int)
     */
    @Override
    public void spawnParticle(Particle paramParticle, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#spawnParticle(org.bukkit.Particle, org.bukkit.Location, int, java.lang.Object)
     */
    @Override
    public <T> void spawnParticle(Particle paramParticle, Location paramLocation, int paramInt, T paramT)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#spawnParticle(org.bukkit.Particle, double, double, double, int, java.lang.Object)
     */
    @Override
    public <T> void spawnParticle(Particle paramParticle, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt, T paramT)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#spawnParticle(org.bukkit.Particle, org.bukkit.Location, int, double, double, double)
     */
    @Override
    public void spawnParticle(Particle paramParticle, Location paramLocation, int paramInt, double paramDouble1, double paramDouble2, double paramDouble3)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#spawnParticle(org.bukkit.Particle, double, double, double, int, double, double, double)
     */
    @Override
    public void spawnParticle(Particle paramParticle, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt, double paramDouble4, double paramDouble5, double paramDouble6)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#spawnParticle(org.bukkit.Particle, org.bukkit.Location, int, double, double, double, java.lang.Object)
     */
    @Override
    public <T> void spawnParticle(Particle paramParticle, Location paramLocation, int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, T paramT)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#spawnParticle(org.bukkit.Particle, double, double, double, int, double, double, double, java.lang.Object)
     */
    @Override
    public <T> void spawnParticle(Particle paramParticle, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt, double paramDouble4, double paramDouble5, double paramDouble6,
            T paramT)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#spawnParticle(org.bukkit.Particle, org.bukkit.Location, int, double, double, double, double)
     */
    @Override
    public void spawnParticle(Particle paramParticle, Location paramLocation, int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#spawnParticle(org.bukkit.Particle, double, double, double, int, double, double, double, double)
     */
    @Override
    public void spawnParticle(Particle paramParticle, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt, double paramDouble4, double paramDouble5, double paramDouble6,
            double paramDouble7)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#spawnParticle(org.bukkit.Particle, org.bukkit.Location, int, double, double, double, double, java.lang.Object)
     */
    @Override
    public <T> void spawnParticle(Particle paramParticle, Location paramLocation, int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, T paramT)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.bukkit.World#spawnParticle(org.bukkit.Particle, double, double, double, int, double, double, double, double, java.lang.Object)
     */
    @Override
    public <T> void spawnParticle(Particle paramParticle, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt, double paramDouble4, double paramDouble5, double paramDouble6,
            double paramDouble7, T paramT)
    {
        // TODO Auto-generated method stub
        
    }
    
}
