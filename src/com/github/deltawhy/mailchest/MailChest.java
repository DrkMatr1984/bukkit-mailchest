package com.github.deltawhy.mailchest;

import java.util.HashMap;

import java.io.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.block.*;
import org.bukkit.entity.Player;

public class MailChest extends JavaPlugin {
	private HashMap<MailboxLocation, Mailbox> mailboxes;
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
		readMailboxData();
	}

	@SuppressWarnings("unchecked")
	private void readMailboxData() {
		try {
			File mailboxFile = new File(getDataFolder(), "mailboxes.dat");
			if (mailboxFile.exists()) {
				FileInputStream fileIn = new FileInputStream(mailboxFile);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				Object read = in.readObject();
				if (read instanceof HashMap<?,?>) {
					mailboxes = (HashMap<MailboxLocation, Mailbox>) read;
				} else {
					getLogger().warning("[MailChest] Could not read data file!");
				}
				in.close();
				fileIn.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().warning("Could not read data file!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			getLogger().warning("Could not read data file!");
		}
		
		if (mailboxes == null) {
			mailboxes = new HashMap<MailboxLocation, Mailbox>();
		}
		
		getLogger().info(mailboxes.toString());
	}
	
	private void writeMailboxData() {
		try {
			File mailboxFile = new File(getDataFolder(), "mailboxes.dat");
			FileOutputStream fileOut = new FileOutputStream(mailboxFile);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(mailboxes);
			out.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().warning("Could not write data file!");
		}
		
		getLogger().info(mailboxes.toString());
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
	
	public boolean autoCreateMailbox(Block chest, Player player) {
		if (player.hasPermission("mailchest.autocreate")) {
			Mailbox box = new Mailbox(player.getName());
			mailboxes.put(new MailboxLocation(chest.getLocation()), box);
			writeMailboxData();
			player.sendMessage(ChatColor.GOLD + "[MailChest] Created a mailbox!");
			return true;
		} else {
			return false;
		}
	}
	
 	public boolean createMailbox(Block chest, Player player) {
		if (player.hasPermission("mailchest.create")) {
			Mailbox box = new Mailbox(player.getName());
			mailboxes.put(new MailboxLocation(chest.getLocation()), box);
			writeMailboxData();
			player.sendMessage(ChatColor.GOLD + "[MailChest] Created a mailbox!");
			return true;
		} else {
			player.sendMessage(ChatColor.RED + "[MailChest] You don't have permission to create mailboxes.");
			return false;
		}
 	}
}
