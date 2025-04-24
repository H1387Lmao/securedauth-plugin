package com.h1387xd.securedauth;

import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class FreezeListener implements Listener {
	private Plugin plugin;

	public FreezeListener(Plugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (!plugin.getLoggedInPlayers().contains(player.getUniqueId())) {
			Location from = event.getFrom();
			Location to = event.getTo();
			if (from.getX() != to.getX() || from.getZ() != to.getZ()) {
				Location frozen = new Location(
					from.getWorld(),
					Math.floor(from.getX())+0.5,
					to.getY(), // allow vertical movement
					Math.floor(from.getZ())+0.5,
					to.getYaw(),
					to.getPitch()
				);
				event.setTo(frozen);
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		player.setGameMode(GameMode.ADVENTURE);

		plugin.getLoggedInPlayers().remove(player.getUniqueId());
		player.sendMessage("§ePlease /login or /register to begin.");
		player.teleport(player.getWorld().getHighestBlockAt(player.getLocation()).getLocation().add(0, 1, 0));
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		plugin.getLoggedInPlayers().remove(player.getUniqueId());
	}

	@EventHandler
	public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();

		if (!plugin.getLoggedInPlayers().contains(player.getUniqueId())) {
			String msg = event.getMessage().toLowerCase();

			if (!(msg.startsWith("/r") || msg.startsWith("/l"))) {
				player.sendMessage("§cYou must logged in or registered to use commands.");
				event.setCancelled(true);
			}
		}
	}
}
