package io.github.yodalee.FastBuild;

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

  @EventHandler
  public void onPlace(BlockPlaceEvent event) {
    Player player = event.getPlayer();
	Block block = event.getBlockPlaced();
    Block againstBlock = event.getBlockAgainst();
    BlockFace face = againstBlock.getFace(block);
    Block nextBlock = null;
    ItemStack stackInHand = event.getItemInHand();
    int stackAmount = stackInHand.getAmount();
    int n,i;

    //get n
    if (plugin.playerN.containsKey(player)) {
      n = plugin.playerN.get(player);
    } else {
      n = 1;
    }
    n = Math.min(n, stackAmount);

    stackInHand.setAmount(stackInHand.getAmount() -1);
    //build
    if (face != null) {
      for ( i = 0 ; i < n-1 ; i++) {
        nextBlock = block.getRelative(face);
        if (checkReplaceable(nextBlock)) {
          nextBlock.setType(stackInHand.getType());
          block = nextBlock;
        } else {
          break;
        }
      }
    }

    // reduce itemStack in hand
    if (player.getGameMode() != GameMode.CREATIVE) {
      stackInHand.setAmount(stackInHand.getAmount() - n);
      player.setItemInHand(stackInHand);
    } 
  }
}
