package io.github.yodalee.FastBuild.listeners;

import io.github.yodalee.FastBuild.FastBuild;

import java.lang.Math;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public class FastBuildPlaceListener implements Listener {
  public FastBuild plugin;
  public FastBuildPlaceListener (FastBuild instance){
    plugin = instance;
  }
  public boolean checkReplaceable(Block block){
    return !block.getType().isSolid();
  }

  @SuppressWarnings("deprecation")
  @EventHandler
  public void onPlace(final BlockPlaceEvent event) {
    if (!event.canBuild()) {
      return;
    } 
    Player player = event.getPlayer();
	Block block = event.getBlockPlaced();
    Block againstBlock = event.getBlockAgainst();
    BlockFace face = againstBlock.getFace(block);
    Block nextBlock = null;
    ItemStack stackInHand = event.getItemInHand();

    int stackAmount = stackInHand.getAmount();
    int n,i;
    int reallyBuild;
    boolean canBuild = block.getType().isSolid();

    if (plugin.isDebug) { 
      player.sendMessage("Place a block: " + block.getType().toString() + " , canBuild = " + canBuild);
    } 
    if (!canBuild) {
      return;
    } 

    //get n
    n = plugin.getn(player.getName());
    if (player.getGameMode() != GameMode.CREATIVE) {
      n = Math.min(n, stackAmount);
    } 
    reallyBuild = 1;

    //build
    if (face != null) {
      for ( i = 0 ; i < n-1 ; i++) {
        nextBlock = block.getRelative(face);
        if (checkReplaceable(nextBlock)) {
          nextBlock.setType(stackInHand.getType());
          nextBlock.setData(block.getData());
          reallyBuild = reallyBuild + 1;
          block = nextBlock;
        } else {
          break;
        }
      }
    }

    // reduce itemStack in hand
    if (player.getGameMode() != GameMode.CREATIVE && block.getType() == stackInHand.getType()) {
      stackInHand.setAmount(stackInHand.getAmount() - reallyBuild);
      player.setItemInHand(stackInHand);
    } 
  }
}
