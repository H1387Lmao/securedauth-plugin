package com.h1387xd.securedauth;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.UUID;

public class Plugin extends JavaPlugin
{
  private static final Logger LOGGER=Logger.getLogger("securedauth");
  private CommandHandler commandHandler;
  private final Set<UUID> loggedInPlayers = new HashSet<>();

  public Set<UUID> getLoggedInPlayers() {
    return loggedInPlayers;
  }
  public void onEnable()
  {
    LOGGER.info("securedauth enabled");

    commandHandler = new CommandHandler();
    commandHandler.SetPlugin(this);
    getServer().getPluginManager().registerEvents(new FreezeListener(this), this);
    this.getCommand("register").setExecutor(commandHandler);
    this.getCommand("login").setExecutor(commandHandler);
    this.getCommand("unregister").setExecutor(commandHandler);
    this.getCommand("changepass").setExecutor(commandHandler);
  }

  public void onDisable()
  {
    LOGGER.info("securedauth disabled");
  }
}
