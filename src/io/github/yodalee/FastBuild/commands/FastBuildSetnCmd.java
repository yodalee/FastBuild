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
    if (label.length() == 4 && args.length <= 2) {
      // setn
      setn(player, args, -1);
    } else if (args.length == 1) {
      // setn[xyz]
      switch (label.charAt(4)) {
        case 'h': {setn(player, args, 0); break;}
        case 'v': {setn(player, args, 1); break;}
        default : {return false;}
      }
    } else {
      return false;
    }
    return true;
  }

  // TODO implement absolute mode printing & printing
  public void setn(Player player, String[] args, Integer axis){
    FastBuild.PlayerConfig player_cfg = plugin.getPlayer(player.getName());
    Integer[] n = player_cfg.n;
    if (args.length == 0) {
      player.sendMessage(String.format("Your current setting is %d,%d", n[0], n[1]));
    } else {
      if (axis == -1) {
        for (Integer i = 0; i < args.length; ++i) {
          parse(args[i], player_cfg, i);
        }
      } else {
        parse(args[0], player_cfg, axis);
      }
      player.sendMessage(String.format("Set n to %d,%d", n[0], n[1]));
    }
  }

  private void parse(String arg, FastBuild.PlayerConfig cfg, Integer axis) {
    Integer v;
    try {
      v = Math.min(Math.max(Integer.parseInt(arg), 1), 64);
    } catch (NumberFormatException ex) {
      v = 1;
    }
    cfg.n[axis] = v;
  }
}
