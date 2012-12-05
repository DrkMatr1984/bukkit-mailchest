package com.github.deltawhy.mailchest;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class MailChest extends JavaPlugin {
	@Override
	public void onEnable() {
		getLogger().info("MailChest was enabled!");
	}
	
	@Override
	public void onDisable() {
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (command.getName().equalsIgnoreCase("mailchest")) {
			getLogger().info("/mailchest command was used!");
			return true;
		}
		return false;
	}
	
	
}
