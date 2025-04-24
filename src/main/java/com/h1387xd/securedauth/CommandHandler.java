package com.h1387xd.securedauth;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import java.util.UUID;

public class CommandHandler implements CommandExecutor {
    private Plugin plugin;

    public void SetPlugin(Plugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (!(sender instanceof Player)){
            sender.sendMessage("§cOnly players can use these commands!");
            return true;
        }
        if (command.getName().equalsIgnoreCase("register")){
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            String uuidstr = uuid.toString();

            String val = plugin.getConfig().getString("users." + uuidstr + ".value");

            if (val!=null){
                player.sendMessage("§cYou are already registered!");  
                return true;         
            }

            if (args.length < 2){
                player.sendMessage("§cUsage: /register <password> <confirm>!");
                return true;
            }
            String password = args[0];
            String compare = args[1];
            if (!password.equals(compare)){
                player.sendMessage("§cPasswords are different!");
            }
            else{
                player.sendMessage("§aRegistered successfully.");
                plugin.getLoggedInPlayers().add(uuid);
                plugin.getConfig().set("users." + uuidstr + ".value", password);
                plugin.saveConfig();
            }
        }else if(command.getName().equalsIgnoreCase("login")){
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();

            String val = plugin.getConfig().getString("users." + uuid.toString() + ".value");
            if (val==null){
                player.sendMessage("§cYou are not registered!");
                player.sendMessage("§cUsage: /register <password> <confirm>!"); 
                return true;         
            }
            if (args.length < 1){
                player.sendMessage("§cUsage: /login <password>!");
                return true;
            }
            String password = args[0];
            if (password.equals(val)) {
                player.sendMessage("§aLogged in.");
                plugin.getLoggedInPlayers().add(uuid);
                player.setGameMode(GameMode.SURVIVAL);
            }else{
                player.sendMessage("§cWrong password!");
            }
        }else if(command.getName().equalsIgnoreCase("unregister")){
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();

            String val = plugin.getConfig().getString("users." + uuid.toString() + ".value");
            if (val==null){
                player.sendMessage("§cYou are not registered!");
                return true;         
            }
            if (args.length < 1){
                player.sendMessage("§cUsage: /unregister <password>!");
                return true;
            }
            String password = args[0];
            if (password.equals(val)) {
                player.sendMessage("§aSuccessfully unregistered.");
                plugin.getLoggedInPlayers().remove(uuid);
                plugin.getConfig().set("users."+uuid.toString()+".value", null);
            }else{
                player.sendMessage("§cWrong password!");
            }
        }else if (command.getName().equalsIgnoreCase("changepass")){
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            String uuidstr = uuid.toString();

            String val = plugin.getConfig().getString("users." + uuidstr + ".value");

            if (val==null){
                player.sendMessage("§cYou are not registered!");
                return true;         
            }

            if (args.length < 2){
                player.sendMessage("§cUsage: /changepass <oldpass> <newpass>!");
                return true;
            }
            String oldpassword = args[0];
            String newpassword = args[1];
            if (!oldpassword.equals(val)){
                player.sendMessage("§cWrong password!");
            }
            else{
                player.sendMessage("§aChanged password to "+newpassword+" successfully.");
                plugin.getConfig().set("users." + uuidstr + ".value", newpassword);
                plugin.saveConfig();
            }
        }
        return true;
    }
}
