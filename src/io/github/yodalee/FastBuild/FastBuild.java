package io.github.yodalee.FastBuild;

import io.github.yodalee.FastBuild.commands.FastBuildSetnCmd;
import io.github.yodalee.FastBuild.commands.FastBuildTogglePlaceCmd;
import io.github.yodalee.FastBuild.listeners.FastBuildPlaceListener;
import io.github.yodalee.FastBuild.listeners.FastBuildPlayerQuitListener;
import io.github.yodalee.FastBuild.listeners.FastBuildBreakListener;

import java.util.HashMap;
import java.util.Arrays;
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
  public class PlayerConfig {
    public Integer[] n = {1, 1};
    public Boolean[] abs_mode = {false, false};
    public Boolean also_use_inventory = false;
  };
  public boolean isDebug = false;
  private Map<String, PlayerConfig> players = new HashMap<String, PlayerConfig>();

  public PlayerConfig getPlayer(String name){
    if (!players.containsKey(name)) {
      players.put(name, new PlayerConfig());
    }
    return players.get(name);
  }

  public void playerQuit(String name){
    players.remove(name);
  }

  @Override
  public void onEnable(){
    this.saveDefaultConfig();
    this.isDebug = this.getConfig().getBoolean("debug");

    // Register events
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(placeListener, this);
    pm.registerEvents(breakListener, this);
    pm.registerEvents(playerQuitListener, this);

    // Register command handler
    FastBuildSetnCmd setn_cmd = new FastBuildSetnCmd(this);
    getCommand("setn").setExecutor(setn_cmd);
    getCommand("setnh").setExecutor(setn_cmd);
    getCommand("setnv").setExecutor(setn_cmd);
    getCommand("togglePlaceMode").setExecutor(new FastBuildTogglePlaceCmd(this));

    // Output log file
    PluginDescriptionFile pdfFile = this.getDescription();
    getLogger().info(pdfFile.getName() + " version " +
        pdfFile.getVersion() + " enable.");
  }
  @Override
  public void onDisable(){
    players.clear();
    getLogger().info("fastbuild plugin disable.");
  }
}
