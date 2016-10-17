package io.github.yodalee.FastBuild;

import io.github.yodalee.FastBuild.commands.FastBuildSetnCmd;
import io.github.yodalee.FastBuild.commands.FastBuildTogglePlaceCmd;
import io.github.yodalee.FastBuild.listeners.FastBuildPlaceListener;
import io.github.yodalee.FastBuild.listeners.FastBuildPlayerQuitListener;
import io.github.yodalee.FastBuild.listeners.FastBuildBreakListener;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/* fastbuild plugin by Yodalee
 */

public class FastBuild extends JavaPlugin{
  private final FastBuildPlaceListener placeListener = new FastBuildPlaceListener(this);
  private final FastBuildBreakListener breakListener = new FastBuildBreakListener(this);
  private final FastBuildPlayerQuitListener playerQuitListener = new FastBuildPlayerQuitListener(this);
  public Map<String, Integer> playerN = new HashMap<String, Integer>();
  public Map<String, Boolean> playerPlaceMode = new HashMap<String, Boolean>();
  public boolean isDebug = false;

  public int getn(String name){
    if (playerN.containsKey(name)) {
      return playerN.get(name);
    } else {
      playerN.put(name, 1);
      return 1;
    }
  }

  public boolean getPlaceMode(String name){
    if (playerPlaceMode.containsKey(name)) {
      return playerPlaceMode.get(name);
    } else {
      playerPlaceMode.put(name, false);
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
    pm.registerEvents(playerQuitListener, this);

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
