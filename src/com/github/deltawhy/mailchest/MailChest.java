package com.github.deltawhy.mailchest;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.block.*;
import org.bukkit.entity.Player;

public class MailChest extends JavaPlugin {
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
	}
	
	@Override
	public void onDisable() {
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (command.getName().equalsIgnoreCase("mailchest")) {
			getLogger().info("auto-create: " + this.getConfig().getBoolean("auto-create"));
			getLogger().info("sign-text: " + this.getConfig().getString("sign-text"));
			return true;
		}
		return false;
	}
	
	public void autoCreateMailbox(Block chest, Player player) {
		if (player.hasPermission("mailchest.autocreate")) {
			player.sendMessage(ChatColor.GOLD + "[MailChest] Created a mailbox!");
		}
	}
}
