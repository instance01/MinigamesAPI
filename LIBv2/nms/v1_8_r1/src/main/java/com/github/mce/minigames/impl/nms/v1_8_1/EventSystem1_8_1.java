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

package com.github.mce.minigames.impl.nms.v1_8_1;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PigZapEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.bukkit.event.entity.SheepRegrowWoolEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerChannelEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.vehicle.VehicleUpdateEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import com.github.mce.minigames.impl.nms.EventSystemInterface;

/**
 * The event system implementation.
 * 
 * @author mepeisen
 */
public class EventSystem1_8_1 implements EventSystemInterface
{

    /**
     * Event handler for MinigameAsyncPlayerChatEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameBlockBreakEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameBlockBurnEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockBurnEvent(BlockBurnEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameBlockCanBuildEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockCanBuildEvent(BlockCanBuildEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameBlockDamageEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockDamageEvent(BlockDamageEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameBlockDispenseEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockDispenseEvent(BlockDispenseEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameBlockExpEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockExpEvent(BlockExpEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameBlockFadeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockFadeEvent(BlockFadeEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameBlockFormEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockFormEvent(BlockFormEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameBlockFromToEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockFromToEvent(BlockFromToEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameBlockGrowEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockGrowEvent(BlockGrowEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameBlockIgniteEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockIgniteEvent(BlockIgniteEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameBlockMultiPlaceEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockMultiPlaceEvent(BlockMultiPlaceEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameBlockPhysicsEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockPhysicsEvent(BlockPhysicsEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameBlockPistonExtendEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockPistonExtendEvent(BlockPistonExtendEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameBlockPistonRetractEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockPistonRetractEvent(BlockPistonRetractEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameBlockPlaceEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameBlockRedstoneEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockRedstoneEvent(BlockRedstoneEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameBlockSpreadEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockSpreadEvent(BlockSpreadEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameBrewEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBrewEvent(BrewEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameCraftItemEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onCraftItemEvent(CraftItemEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameCreatureSpawnEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onCreatureSpawnEvent(CreatureSpawnEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameCreeperPowerEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onCreeperPowerEvent(CreeperPowerEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEnchantItemEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEnchantItemEvent(EnchantItemEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityBlockFormEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityBlockFormEvent(EntityBlockFormEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityBreakDoorEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityBreakDoorEvent(EntityBreakDoorEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityChangeBlockEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityChangeBlockEvent(EntityChangeBlockEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityCombustByBlockEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityCombustByBlockEvent(EntityCombustByBlockEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityCombustByEntityEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityCombustByEntityEvent(EntityCombustByEntityEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityCombustEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityCombustEvent(EntityCombustEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityCreatePortalEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityCreatePortalEvent(EntityCreatePortalEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityDamageByBlockEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityDamageByBlockEvent(EntityDamageByBlockEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityDamageByEntityEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityDamageEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityDeathEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityExplodeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityExplodeEvent(EntityExplodeEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityInteractEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityInteractEvent(EntityInteractEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityPortalEnterEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityPortalEnterEvent(EntityPortalEnterEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityPortalEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityPortalEvent(EntityPortalEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityPortalExitEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityPortalExitEvent(EntityPortalExitEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityRegainHealthEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityRegainHealthEvent(EntityRegainHealthEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityShootBowEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityShootBowEvent(EntityShootBowEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntitySpawnEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntitySpawnEvent(EntitySpawnEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityTameEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityTameEvent(EntityTameEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityTargetEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityTargetEvent(EntityTargetEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityTargetLivingEntityEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityTargetLivingEntityEvent(EntityTargetLivingEntityEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityTeleportEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityTeleportEvent(EntityTeleportEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameEntityUnleashEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityUnleashEvent(EntityUnleashEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameExpBottleEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onExpBottleEvent(ExpBottleEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameExplosionPrimeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onExplosionPrimeEvent(ExplosionPrimeEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameFoodLevelChangeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameFurnaceBurnEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onFurnaceBurnEvent(FurnaceBurnEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameFurnaceSmeltEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onFurnaceSmeltEvent(FurnaceSmeltEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameHangingBreakByEntityEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onHangingBreakByEntityEvent(HangingBreakByEntityEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameHangingBreakEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onHangingBreakEvent(HangingBreakEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameHangingPlaceEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onHangingPlaceEvent(HangingPlaceEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameHorseJumpEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onHorseJumpEvent(HorseJumpEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameInventoryClickEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameInventoryCloseEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameInventoryCreativeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onInventoryCreativeEvent(InventoryCreativeEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameInventoryDragEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onInventoryDragEvent(InventoryDragEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameInventoryEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onInventoryEvent(InventoryEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameInventoryInteractEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onInventoryInteractEvent(InventoryInteractEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameInventoryMoveItemEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onInventoryMoveItemEvent(InventoryMoveItemEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameInventoryOpenEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameInventoryPickupItemEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onInventoryPickupItemEvent(InventoryPickupItemEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameItemDespawnEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onItemDespawnEvent(ItemDespawnEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameItemSpawnEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onItemSpawnEvent(ItemSpawnEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameLeavesDecayEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onLeavesDecayEvent(LeavesDecayEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameNotePlayEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onNotePlayEvent(NotePlayEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePigZapEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPigZapEvent(PigZapEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerAchievementAwardedEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerAchievementAwardedEvent(PlayerAchievementAwardedEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerAnimationEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerAnimationEvent(PlayerAnimationEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerBedEnterEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerBedEnterEvent(PlayerBedEnterEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerBedLeaveEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerBedLeaveEvent(PlayerBedLeaveEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerBucketEmptyEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerBucketFillEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerBucketFillEvent(PlayerBucketFillEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerChangedWorldEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerChannelEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerChannelEvent(PlayerChannelEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerChatEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerChatEvent(PlayerChatEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerChatTabCompleteEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerChatTabCompleteEvent(PlayerChatTabCompleteEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerCommandPreprocessEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerDeathEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerDropItemEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerEditBookEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerEditBookEvent(PlayerEditBookEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerEggThrowEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerEggThrowEvent(PlayerEggThrowEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerExpChangeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerExpChangeEvent(PlayerExpChangeEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerFishEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerFishEvent(PlayerFishEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerGameModeChangeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerInteractAtEntityEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerInteractEntityEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerInteractEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerInventoryEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerInventoryEvent(PlayerInventoryEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerItemBreakEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerItemBreakEvent(PlayerItemBreakEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerItemConsumeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerItemDamageEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerItemDamageEvent(PlayerItemDamageEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerItemHeldEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerJoinEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerKickEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerLeashEntityEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerLeashEntityEvent(PlayerLeashEntityEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerLevelChangeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerLevelChangeEvent(PlayerLevelChangeEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerLoginEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerMoveEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerPickupItemEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerPortalEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerPortalEvent(PlayerPortalEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerQuitEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerRegisterChannelEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerRegisterChannelEvent(PlayerRegisterChannelEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerRespawnEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerShearEntityEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerShearEntityEvent(PlayerShearEntityEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerSpawnLocationEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerSpawnLocationEvent(PlayerSpawnLocationEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerStatisticIncrementEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerStatisticIncrementEvent(PlayerStatisticIncrementEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerTeleportEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerTeleportEvent(PlayerTeleportEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerToggleFlightEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerToggleFlightEvent(PlayerToggleFlightEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerToggleSneakEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerToggleSprintEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerToggleSprintEvent(PlayerToggleSprintEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerUnregisterChannelEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerUnregisterChannelEvent(PlayerUnregisterChannelEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePlayerVelocityEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerVelocityEvent(PlayerVelocityEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePotionSplashEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPotionSplashEvent(PotionSplashEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePrepareItemCraftEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPrepareItemCraftEvent(PrepareItemCraftEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigamePrepareItemEnchantEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPrepareItemEnchantEvent(PrepareItemEnchantEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameProjectileHitEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onProjectileHitEvent(ProjectileHitEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameProjectileLaunchEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onProjectileLaunchEvent(ProjectileLaunchEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameSheepDyeWoolEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onSheepDyeWoolEvent(SheepDyeWoolEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameSheepRegrowWoolEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onSheepRegrowWoolEvent(SheepRegrowWoolEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameSignChangeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onSignChangeEvent(SignChangeEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameSlimeSplitEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onSlimeSplitEvent(SlimeSplitEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameSpawnerSpawnEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onSpawnerSpawnEvent(SpawnerSpawnEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameVehicleBlockCollisionEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onVehicleBlockCollisionEvent(VehicleBlockCollisionEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameVehicleCreateEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onVehicleCreateEvent(VehicleCreateEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameVehicleDamageEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onVehicleDamageEvent(VehicleDamageEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameVehicleDestroyEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onVehicleDestroyEvent(VehicleDestroyEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameVehicleEnterEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onVehicleEnterEvent(VehicleEnterEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameVehicleEntityCollisionEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onVehicleEntityCollisionEvent(VehicleEntityCollisionEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameVehicleExitEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onVehicleExitEvent(VehicleExitEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameVehicleMoveEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onVehicleMoveEvent(VehicleMoveEvent evt)
    {
        // TODO
    }


    /**
     * Event handler for MinigameVehicleUpdateEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onVehicleUpdateEvent(VehicleUpdateEvent evt)
    {
        // TODO
    }

}
