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
    if (args.length <= 1) {
      setn(player, args);
      return true;
    }
    return false;
  }

  public void setn(Player player, String[] args){
    if (args.length == 0) {
      if (plugin.playerN.containsKey(player)) {
        player.sendMessage("Your current setting is " + plugin.playerN.get(player));
      } else {
        player.sendMessage("Your current setting is 1");
      }
    } else {
      try {
        // parse input parameter
        int inputN = Integer.parseInt(args[0]);
        if (inputN <= 0 ) {
          throw new NumberFormatException();
        } 
        if (inputN > 64) {
          inputN = 64;
        } 
        // save input parameter to player hash
        plugin.playerN.put(player.getName(), inputN);
        player.sendMessage("set n to " + inputN);
      } catch (NumberFormatException ex) {
        player.sendMessage("Given number " + 
            args[0] + " is invalid number");
      }
    }
  }
}
