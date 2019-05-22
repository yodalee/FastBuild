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
    Material.SNOW,
    // Flower
    Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET,
    Material.RED_TULIP, Material.ORANGE_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP, 
    Material.OXEYE_DAISY, Material.SUNFLOWER, Material.LILAC,
    // Other
    Material.VINE, Material.COBWEB,
    Material.GRASS, Material.TALL_GRASS,
    Material.SEAGRASS, Material.TALL_SEAGRASS,
    Material.DEAD_BUSH, Material.MELON_STEM, Material.PUMPKIN_STEM,
    Material.DRAGON_BREATH, Material.FIRE,
  };
  final private List<Material> replaceableList = Arrays.asList(replaceableEnum);
  public boolean checkReplaceable(Block block){
    Material type = block.getType();
    return block.isEmpty() || block.isLiquid() || replaceableList.contains(type);
  }

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

    Integer axis = face == BlockFace.UP || face == BlockFace.DOWN? 1: 0;
    
    // ignore n setting if player is sneaking
    n = 1;
    if (!player.isSneaking()) {
    	n = plugin.getPlayer(player.getName()).n[axis];
    }
    if (player.getGameMode() != GameMode.CREATIVE) {
    	n = Math.min(n, stackAmount);
    }

    //build
    if (face != null) {
      for ( i = 0 ; i < n-1 ; i++) {
        nextBlock = block.getRelative(face);
        if (checkReplaceable(nextBlock)) {
          nextBlock.setType(stackInHand.getType());
          nextBlock.setBlockData(block.getBlockData());
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
