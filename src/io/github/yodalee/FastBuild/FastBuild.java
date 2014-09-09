package io.github.yodalee.FastBuild;

import io.github.yodalee.FastBuild.commands.FastBuildSetnCmd;
import io.github.yodalee.FastBuild.commands.FastBuildTogglePlaceCmd;
import io.github.yodalee.FastBuild.listeners.FastBuildPlaceListener;
import io.github.yodalee.FastBuild.listeners.FastBuildBreakListener;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;

/* fastbuild plugin by Yodalee
 */

public class FastBuild extends JavaPlugin{
  private final FastBuildPlaceListener placeListener = new FastBuildPlaceListener(this);
  private final FastBuildBreakListener breakListener = new FastBuildBreakListener(this);
  public Map<String, Integer> playerN = new HashMap<String, Integer>();
  public Map<String, Boolean> playerPlaceMode = new HashMap<String, Boolean>();
  public boolean isDebug = false;

  public int getn(Player player){
    if (playerN.containsKey(player.getName())) {
      return playerN.get(player.getName());
    } else {
      playerN.put(player.getName(), 1);
      return 1;
    }
  }

  public boolean getPlaceMode(Player player){
    if (playerPlaceMode.containsKey(player.getName())) {
      return playerPlaceMode.get(player.getName());
    } else {
      playerPlaceMode.put(player.getName(), false);
      return false;
    } 
  }

  @Override
  public void onEnable(){
    this.saveDefaultConfig();
    this.isDebug = this.getConfig().getBoolean("debug");
	 
    //Register events
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(placeListener, this);
    pm.registerEvents(breakListener, this);

    //register command handler
    getCommand("setn").setExecutor(new FastBuildSetnCmd(this));
    getCommand("togglePlaceMode").setExecutor(new FastBuildTogglePlaceCmd(this));

    //Output log file
    PluginDescriptionFile pdfFile = this.getDescription();
  	getLogger().info(pdfFile.getName() + " version " + 
        pdfFile.getVersion() + " enable.");
  }
  @Override
  public void onDisable(){
    playerN.clear();
  	getLogger().info("fastbuild plugin disable.");
  }
}
