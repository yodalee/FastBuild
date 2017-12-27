package io.github.yodalee.FastBuild.listeners;

import io.github.yodalee.FastBuild.FastBuild;

import java.lang.Math;
import java.util.Arrays;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class FastBuildPlaceListener implements Listener {
  public FastBuild plugin;
  public FastBuildPlaceListener (FastBuild instance){
    plugin = instance;
  }
  final Material[] replaceableEnum = {
    //isEmpty method:
    // Material.AIR
    //isLiquid method
	// Material.WATER, Material.STATIONARY_WATER, Material.LAVA, Material.STATIONARY_LAVA
    Material.SNOW, Material.VINE, Material.WATER_LILY,
    Material.WEB, Material.LONG_GRASS, Material.CROPS,
    Material.DOUBLE_PLANT, Material.YELLOW_FLOWER,
    Material.DEAD_BUSH, Material.RED_ROSE,
    Material.MELON_STEM, Material.PUMPKIN_STEM,
    Material.DRAGONS_BREATH, Material.FIRE,
  };
  final private List<Material> replaceableList = Arrays.asList(replaceableEnum);
  public boolean checkReplaceable(Block block){
    Material type = block.getType();
    return block.isEmpty() || block.isLiquid() || replaceableList.contains(type);
  }

  @SuppressWarnings("deprecation")
@EventHandler
  public void onPlace(final BlockPlaceEvent event) {
    if (!event.canBuild()) {
      return;
    }
    Player player = event.getPlayer();
    EquipmentSlot hand = event.getHand();
    Block block = event.getBlockPlaced();
    Block againstBlock = event.getBlockAgainst();
    BlockFace face = againstBlock.getFace(block);
    Block nextBlock = null;
    ItemStack stackInHand = event.getItemInHand();

    int stackAmount = stackInHand.getAmount();
    int n,i;
    int reallyBuild = 1;
    boolean canBuild = block.getType().isSolid();

    if (plugin.isDebug) {
      player.sendMessage("Place a block: " + block.getType().toString() + " , canBuild = " + canBuild);
    }
    if (!canBuild) {
      return;
    }

    //get n
    Integer axis = face == BlockFace.UP || face == BlockFace.DOWN? 1: 0;
    n = plugin.getPlayer(player.getName()).n[axis];
    if (player.getGameMode() != GameMode.CREATIVE) {
      n = Math.min(n, stackAmount);
    }

    //build
    if (face != null) {
      for ( i = 0 ; i < n-1 ; i++) {
        nextBlock = block.getRelative(face);
        if (checkReplaceable(nextBlock)) {
          nextBlock.setType(stackInHand.getType());
          nextBlock.setData(block.getData());
          reallyBuild += 1;
          block = nextBlock;
        } else {
          break;
        }
      }
    }

    // reduce itemStack in hand
    if (player.getGameMode() != GameMode.CREATIVE && block.getType() == stackInHand.getType()) {
      stackInHand.setAmount(stackInHand.getAmount() - reallyBuild);
      PlayerInventory inventory = player.getInventory();
      if (hand == EquipmentSlot.HAND) {
        inventory.setItemInMainHand(stackInHand);
      } else if (hand == EquipmentSlot.OFF_HAND) {
        inventory.setItemInOffHand(stackInHand);
      }
    }
  }
}
