package io.github.yodalee.FastBuild.listeners;

import io.github.yodalee.FastBuild.FastBuild;

import java.lang.Math;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
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
  int[] toolId = {270, 274, 257, 285, 278, 271, 275, 258, 286, 279, 269, 273, 256, 284, 277};
  int[] swordId = {268, 272, 267, 283, 276};
  private List<Integer> toolList = Arrays.asList(ArrayUtils.toObject(toolId));
  private List<Integer> swordList = Arrays.asList(ArrayUtils.toObject(swordId));

  @EventHandler
  public void onInteract(final PlayerInteractEvent event){
    face = event.getBlockFace().getOppositeFace();
  }

  private boolean durabilityRandom(int durabilityLevel){
    return (Math.random() < 1.0/(durabilityLevel+1));
  }

  @SuppressWarnings("deprecation")
  private boolean reduceDurability(ItemStack tool, Player player){
    //short currentValue = tool.getDurability();
    //tool.setDurability(currentValue);
    //check player using pickaxe, shovel, or axe, these tool add durability 1
    boolean isTool = false;
    int unbreakingLevel = 0;
    if (toolList.contains(tool.getTypeId()) || swordList.contains(tool.getTypeId())) {
      isTool = true;
    }

    if (isTool) {
      unbreakingLevel = tool.getEnchantmentLevel(Enchantment.DURABILITY);
      //check if contains unbreaking enchant
      if (plugin.isDebug) {
        String str = "using tool " + tool.toString();
        if (unbreakingLevel != 0) {
          str += " with unbreaking level " + unbreakingLevel;
        } 
        player.sendMessage(str);
      }
      // reduce durability
      if (durabilityRandom(unbreakingLevel)) {
        if (toolList.contains(tool.getTypeId())) {
          tool.setDurability((short)(tool.getDurability()+1));
        } else {
          tool.setDurability((short)(tool.getDurability()+2));
        }
      } 
      // check tool is OK
      if (tool.getDurability() >= tool.getType().getMaxDurability()) {
        return false;
      } else {
        return true;
      }
    } else {
      return true;
    }
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
    } 

    boolean toolIsGood = true;
    for (int i = 0; i < n-1; i++) {
      nextBlock = block.getRelative(face);
      //currently only deal with same type block
      if (nextBlock.getType() == originType) {
        nextBlock.setType(Material.AIR);
        if (!isCreative) {
          toolIsGood = reduceDurability(tool,player);
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
