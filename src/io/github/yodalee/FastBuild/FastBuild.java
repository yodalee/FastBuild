package io.github.yodalee.FastBuild;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

/* fastbuild plugin by Yodalee
 */

public class FastBuild extends JavaPlugin{
  public Map<Player, Integer> playerN = new HashMap<Player, Integer>();

  @Override
  public void onEnable(){

    //register command handler
    getCommand("setn").setExecutor(new FastBuildSetnCmd(this));

    //Output log file
    PluginDescriptionFile pdfFile = this.getDescription();
  	getLogger().info(pdfFile.getName() + " version " + 
        pdfFile.getVersion() + " enable.");
  }
  @Override
  public void onDisable(){
  	getLogger().info("fastbuild plugin disable.");
  }
}
