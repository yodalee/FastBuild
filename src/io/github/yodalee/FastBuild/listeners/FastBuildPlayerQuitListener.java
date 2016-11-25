package io.github.yodalee.FastBuild.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.yodalee.FastBuild.FastBuild;

public class FastBuildPlayerQuitListener implements Listener {
  public FastBuild plugin;
  public FastBuildPlayerQuitListener (FastBuild instance){
    plugin = instance;
  }

  @EventHandler
  public void onBreak(final PlayerQuitEvent event) {
    plugin.playerQuit(event.getPlayer().getName());
  }
}
