package io.github.yodalee.FastBuild.commands;

import io.github.yodalee.FastBuild.FastBuild;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FastBuildSetnCmd implements CommandExecutor {
  private FastBuild plugin;
  public FastBuildSetnCmd(FastBuild instance) {
    plugin = instance;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    //check setn command 
    if (!(sender instanceof Player)) {
      sender.sendMessage("This command can only be run by a player.");
      return false;
    }
    Player player = (Player)sender;
    player.sendMessage(label);
    if (args.length <= 1) {
      setn(player, args);
      return true;
    }
    return false;
  }

  public void setn(Player player, String[] args){
    FastBuild.PlayerConfig player_cfg = plugin.getPlayer(player.getName());
    if (args.length == 0) {
      player.sendMessage(String.format("Your current setting is %d", player_cfg.n));
    } else {
      try {
        Integer inputN = Math.min(Math.max(Integer.parseInt(args[0]), 1), 64);
        player_cfg.n = inputN;
        player.sendMessage("Set n to " + inputN);
      } catch (NumberFormatException ex) {
        player.sendMessage("Given number " + 
            args[0] + " is invalid number");
      }
    }
  }
}
