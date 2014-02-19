package io.github.yodalee.FastBuild.listeners;

import io.github.yodalee.FastBuild.FastBuild;

import java.lang.Math;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


@SuppressWarnings("unused")
public class FastBuildBreakListener implements Listener {
  public FastBuild plugin;
  public FastBuildBreakListener (FastBuild instance){
    plugin = instance;
  }
  private BlockFace face;

  @EventHandler
  public void onInteract(final PlayerInteractEvent event){
    face = event.getBlockFace().getOppositeFace();
  }

  private boolean reduceDurability(ItemStack tool){
    return true;
  }

  @EventHandler
  public void onBreak(final BlockBreakEvent event) {
    Player player = event.getPlayer();
    Block block = event.getBlock();
    Block nextBlock = null;
    Material originType = block.getType();
    ItemStack tool = player.getItemInHand();

    int n = plugin.getn(player);
    boolean isCreative = player.getGameMode() == GameMode.CREATIVE;
    if (plugin.isDebug) {
      player.sendMessage("Hit block: " + block.getType().toString() + " at face: " + face.getOppositeFace().toString());
      if (!isCreative) {
        player.sendMessage("Using tool: " + tool.toString());
      } 
    } 

    boolean toolIsGood = true;
    for (int i = 0; i < n-1; i++) {
      nextBlock = block.getRelative(face);
      //currently only deal with same type block
      if (nextBlock.getType() == originType) {
        if (plugin.isDebug) {
          player.sendMessage("Next block: " + nextBlock.getType().toString());
        } 
        nextBlock.setType(Material.AIR);
        if (!isCreative) {
          toolIsGood = reduceDurability(tool);
        }
        if (!toolIsGood) {
          break;
        } 
      } else {
        break;
      }
      block = nextBlock;
    }
  } 
}
