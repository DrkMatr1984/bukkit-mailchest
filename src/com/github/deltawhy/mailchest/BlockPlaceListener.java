package com.github.deltawhy.mailchest;

import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;

public class BlockPlaceListener implements Listener {
	private MailChest plugin;
	public BlockPlaceListener(MailChest plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (player == null)
			return;
		
		Block block = event.getBlock();
		if (block.getType() == Material.CHEST) {
			Block beneathBlock = block.getRelative(BlockFace.DOWN);
			if (beneathBlock != null && beneathBlock.getType() == Material.FENCE 
					&& plugin.getConfig().getBoolean("auto-create.fence")) {
				plugin.autoCreateMailbox(block, player);
			} else if (beneathBlock != null	&& beneathBlock.getType() == Material.COBBLE_WALL
					&& plugin.getConfig().getBoolean("auto-create.wall")) {
				plugin.autoCreateMailbox(block, player);
			}
		}
	}
}
