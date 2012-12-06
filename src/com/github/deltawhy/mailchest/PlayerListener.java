package com.github.deltawhy.mailchest;

import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {
	private MailChest plugin;
	
	public PlayerListener(MailChest plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Block chest = event.getClickedBlock();
		
		if (chest == null || chest.getType() != Material.CHEST || !plugin.isMailbox(chest)) return;
		
		Player player = event.getPlayer();
		if (player.equals(plugin.getMailboxOwner(chest)) || (player.hasPermission("mailchest.snoop") && player.isSneaking())) {
			return;
		} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
			event.setCancelled(true);
			plugin.openMailbox(player, chest);
		}
	}
}
