package me.spigotserver.view;

import me.spigotserver.SpigotServer;

public class UpdateView {
	
	public static void tick() {
		SpigotServer.getServerManager().refreshServerDetail();
		SpigotServer.frame.getMenuPane().setSize(200, SpigotServer.frame.getHeight() - 39);
		SpigotServer.frame.listServer.setSize(200, SpigotServer.frame.getHeight() - 148);
		SpigotServer.frame.lblCreateServer.setLocation(10, SpigotServer.frame.getHeight() - 82);
		SpigotServer.frame.tabbedPane.setSize(SpigotServer.frame.getWidth() - 214, SpigotServer.frame.getHeight() - 39);
		SpigotServer.frame.consoleCommand.setBounds(10, SpigotServer.frame.getHeight() - 98, SpigotServer.frame.getWidth() - 241, 20);
		SpigotServer.frame.propertyPane.setSize(SpigotServer.frame.getWidth() - 221, SpigotServer.frame.getHeight() - 130);
	}
}
