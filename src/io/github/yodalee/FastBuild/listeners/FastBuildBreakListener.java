package io.github.yodalee.FastBuild.listeners;

import io.github.yodalee.FastBuild.FastBuild;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;
import java.util.Collection;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class FastBuildBreakListener implements Listener {
  public FastBuild plugin;
  public FastBuildBreakListener (FastBuild instance){
    plugin = instance;
  }
  private BlockFace face;
  // block ID of tools: axe, shovel, hoe and pickaxe
  final Material[] toolEnum = {
		  Material.WOOD_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE,
		  Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE,
		  Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE,
		  Material.WOOD_SPADE, Material.STONE_SPADE, Material.IRON_SPADE,
		  Material.GOLD_SPADE, Material.DIAMOND_SPADE,
		  Material.WOOD_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLD_HOE, Material.DIAMOND_HOE,
		  };
  //swords: sword
  final Material[] swordEnum = {
		  Material.WOOD_SWORD,
		  Material.STONE_SWORD,
		  Material.IRON_SWORD,
		  Material.GOLD_SWORD,
		  Material.DIAMOND_SWORD};
  final private List<Material> toolList = Arrays.asList(toolEnum);
  final private List<Material> swordList = Arrays.asList(swordEnum);

  @EventHandler
  public void onInteract(final PlayerInteractEvent event){
    face = event.getBlockFace().getOppositeFace();
  }

  //create drops at block location
  private void createDrops(Block block, Collection<ItemStack> drops){
    Iterator<ItemStack> drop = drops.iterator();
    while (drop.hasNext()) {
      block.getWorld().dropItemNaturally(block.getLocation(), drop.next());
    }
  }

  //create random between a,b
  private int getRandom(int a, int b){
    return (int)(Math.random()*(b-a+1)+a);
  }

  //create exp at block location depends on block
  private void createExps(Player player, Block block, Material mat){
    World world = block.getWorld();
    int exp;
    switch (mat) {
      case COAL_ORE:
        exp = getRandom(0,2);
        break;
      case DIAMOND_ORE:
      case EMERALD_ORE:
        exp = getRandom(3,7);
        break;
      case LAPIS_ORE:
      case QUARTZ_ORE:
        exp = getRandom(2,5);
        break;
      case REDSTONE_ORE:
        exp = getRandom(1,5);
        break;
      case MOB_SPAWNER:
        exp = getRandom(15,23);
        break;
       default:
    	 exp = 0;
    	 break;
    }
    if (plugin.isDebug) {
      player.sendMessage("break " + mat.toString() + " get exp: " + exp);
    }
    if (exp != 0) {
      ((ExperienceOrb)world.spawn(block.getLocation(), ExperienceOrb.class)).setExperience( exp );
    }
  }

  //generate drops collection with tool and block
  private Collection<ItemStack> getDrops(ItemStack tool, Block block) {
    Collection<ItemStack> dropList = block.getDrops(tool);
    if (tool.getEnchantmentLevel(Enchantment.SILK_TOUCH) == 0) {
      return dropList;
    } else {
      if (!dropList.isEmpty()) {
        Collection<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(block.getType()));
        return drops;
      } else {
        return dropList;
      }
    }
  }

  private boolean durabilityRandom(int durabilityLevel){
    // tool got 100/(level+1) % probability to reduce their durabilityLevel
    // according to Minecraft Wiki
    return (Math.random() < 1.0/(durabilityLevel+1));
  }

  private boolean reduceDurability(ItemStack tool, Player player){
    //short currentValue = tool.getDurability();
    //tool.setDurability(currentValue);
    //check player using pickaxe, shovel, or axe, these tool add durability 1
    boolean isTool = false;
    int unbreakingLevel = 0;
    if (toolList.contains(tool.getType()) || swordList.contains(tool.getType())) {
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
        if (toolList.contains(tool.getType())) {
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

  private boolean canContinue(Material originType, Material nextType) {
	  if(originType == nextType) { return true; }
	  if((originType == Material.GRASS && nextType == Material.DIRT) ||
		  (originType == Material.DIRT && nextType == Material.GRASS)) {
		 return true;
	  }
	  if(originType == Material.GLOWING_REDSTONE_ORE && nextType == Material.REDSTONE_ORE) {
		 return true;
	  }
	  return false;
  }

  @EventHandler
  public void onBreak(final BlockBreakEvent event) {
    Player player = event.getPlayer();
    Block block = event.getBlock();
    Block nextBlock = null;
    Material originType = block.getType();
    ItemStack tool = player.getInventory().getItemInMainHand();

    Integer axis = face == BlockFace.UP || face == BlockFace.DOWN? 1: 0;
    int n = plugin.getPlayer(player.getName()).n[axis];
    boolean isCreative = (player.getGameMode() == GameMode.CREATIVE);
    if (plugin.isDebug) {
      player.sendMessage("Hit block: " + block.getType().toString() + " at face: " + face.getOppositeFace().toString());
    }

    for (int i = 0; i < n-1; i++) {
      nextBlock = block.getRelative(face);
      //currently only deal with same type block
      if (canContinue(originType, nextBlock.getType())) {
        Collection<ItemStack> drops = getDrops(tool, nextBlock);
        nextBlock.setType(Material.AIR);
        if (!isCreative) {
          // drops
          createDrops(nextBlock, drops);
          // drop exp
          createExps(player, nextBlock, originType);
          // durability
          if(!reduceDurability(tool,player)) {
            break;
          }
        }
      } else {
        break;
      }
      block = nextBlock;
    }
  }
}
