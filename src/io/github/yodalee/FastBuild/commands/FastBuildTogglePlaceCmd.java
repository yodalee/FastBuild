package io.github.yodalee.FastBuild.commands;

import io.github.yodalee.FastBuild.FastBuild;
import io.github.yodalee.FastBuild.FastBuild.PlayerConfig;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FastBuildTogglePlaceCmd implements CommandExecutor {
  private FastBuild plugin;
  public FastBuildTogglePlaceCmd(FastBuild instance) {
    plugin = instance;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    //check toggle place command 
    if (!(sender instanceof Player)) {
      sender.sendMessage("This command can only be run by a player.");
      return false;
    }
    Player player = (Player)sender;
    togglePlace(player);
    return true;
  }

  private void togglePlace(Player player){
    PlayerConfig p = plugin.getPlayer(player.getName());
    p.also_use_inventory = !p.also_use_inventory;
    String str = "Your current setting is use in" + (p.also_use_inventory? "inventory" : "hand");
    player.sendMessage(str);
  }
}
