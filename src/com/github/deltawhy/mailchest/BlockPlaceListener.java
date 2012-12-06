package com.github.deltawhy.mailchest;

import org.bukkit.ChatColor;
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
	
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		if (event.getLine(0).equals("[" + plugin.getConfig().getString("sign-text") + "]")) {
			Player player = event.getPlayer();
			Block signBlock = event.getBlock();
			BlockFace[] directions = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
			Block chest = null;
			
			for (BlockFace direction : directions) {
				if (signBlock.getRelative(direction).getType() == Material.CHEST) {
					chest = signBlock.getRelative(direction);
					break;
				}
			}
			
			if (chest == null) {
				player.sendMessage(ChatColor.RED + "[MailChest] No chest found.");
				event.setCancelled(true);
				return;
			}
			
			if (plugin.createMailbox(chest, player)) {
				//attach sign to chest
				if (signBlock.getType() == Material.SIGN_POST) {
					signBlock.setType(Material.WALL_SIGN);
					switch (signBlock.getFace(chest)) {
					case NORTH:
						signBlock.setData((byte) 5);
						break;
					case SOUTH:
						signBlock.setData((byte) 4);
						break;
					case EAST:
						signBlock.setData((byte) 3);
						break;
					case WEST:
						signBlock.setData((byte) 2);
						break;
					default:
						break;
					}
					Sign sign = (Sign)signBlock.getState();
					sign.setLine(0, event.getLine(0));
					sign.setLine(1, event.getLine(1));
					sign.setLine(2, event.getLine(2));
					sign.setLine(3, event.getLine(3));
					sign.update(true);
				}
			} else {
				event.setCancelled(true);
			}
		}
	}
}
