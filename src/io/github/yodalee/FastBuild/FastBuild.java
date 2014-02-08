package io.github.yodalee.FastBuild;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

/* fastbuild plugin by Yosdalee
 */

public class FastBuild extends JavaPlugin{
  private final FastBuildPlaceListener placeListener = new FastBuildPlaceListener(this);
  public Map<Player, Integer> playerN = new HashMap<Player, Integer>();

  @Override
  public void onEnable(){
    //Register events
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(placeListener, this);

    //register command handler
    getCommand("setn").setExecutor(new FastBuildSetnCmd(this));

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
