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
	private InventoryListener inventoryListener;
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		getServer().getPluginManager().registerEvents(new BlockListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		inventoryListener = new InventoryListener(this);
		getServer().getPluginManager().registerEvents(inventoryListener, this);
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
		/*if (command.getName().equalsIgnoreCase("mailchest")) {
			getLogger().info("auto-create: " + this.getConfig().getBoolean("auto-create"));
			getLogger().info("sign-text: " + this.getConfig().getString("sign-text"));
			return true;
		}*/
		return false;
	}
	
	public boolean autoCreateMailbox(Block chest, Player player) {
		if (isMailbox(chest)) {
			player.sendMessage(ChatColor.RED + "[MailChest] That's already a mailbox!");
			return false;
		} else if (!player.hasPermission("mailchest.autocreate")) {
			return false;
		} else {
			Mailbox box = new Mailbox(player.getName());
			mailboxes.put(new MailboxLocation(chest.getLocation()), box);
			writeMailboxData();
			player.sendMessage(ChatColor.GOLD + "[MailChest] Created a mailbox!");
			getLogger().info(player.getName() + " created a mailbox.");
			return true;
		}
	}
	
 	public boolean createMailbox(Block chest, Player player) {
 		if (isMailbox(chest)) {
			player.sendMessage(ChatColor.RED + "[MailChest] That's already a mailbox!");
			return false;
		} else if (!player.hasPermission("mailchest.create")) {
			player.sendMessage(ChatColor.RED + "[MailChest] You don't have permission to create mailboxes.");
			return false;
		} else {
			Mailbox box = new Mailbox(player.getName());
			mailboxes.put(new MailboxLocation(chest.getLocation()), box);
			writeMailboxData();
			player.sendMessage(ChatColor.GOLD + "[MailChest] Created a mailbox!");
			getLogger().info(player.getName() + " created a mailbox.");
			return true;
		}
 	}
 	
 	public boolean isMailbox(Block block) {
 		return mailboxes.containsKey(new MailboxLocation(block.getLocation()));
 	}
 	
 	public Mailbox getMailbox(Block block) {
 		return mailboxes.get(new MailboxLocation(block.getLocation()));
 	}
 	
 	public Player getMailboxOwner(Block block) {
 		Mailbox mailbox = getMailbox(block);
 		if (mailbox == null) return null;
 		return getServer().getPlayerExact(mailbox.getOwnerName());
 	}
 	
 	public Player getMailboxOwner(Mailbox mailbox) {
 		return getServer().getPlayerExact(mailbox.getOwnerName());
 	}

	public boolean destroyMailbox(Player player, Block block) {
		Mailbox box = getMailbox(block);
		if (box == null) return true;
		if (player == null) return !getConfig().getBoolean("protect-mailboxes");
		if (player.getName().equals(box.getOwnerName()) || player.hasPermission("mailchest.destroy")) {
			mailboxes.remove(new MailboxLocation(block.getLocation()));
			writeMailboxData();
			player.sendMessage(ChatColor.GOLD + "[MailChest] Destroyed a mailbox.");
			if (player.getName().equals(box.getOwnerName())) {
				getLogger().info(player.getName() + " destroyed their mailbox.");
			} else {
				getLogger().info(player.getName() + " destroyed " + box.getOwnerName() + "'s mailbox.");
			}
			return true;
		} else {
			player.sendMessage(ChatColor.RED + "[MailChest] You don't have permission to destroy this mailbox.");
			return false;
		}
	}

	public void openMailbox(Player player, Block block) {
		Mailbox box = getMailbox(block);
		Chest chest = (Chest)block.getState();
		
		if (!player.hasPermission("mailchest.send")) {
			player.sendMessage(ChatColor.RED + "[MailChest] You don't have permission to send mail.");
		} else {
			inventoryListener.add(box, chest);
			player.openInventory(box.getInventory());
		}
	}
}
