package me.spigotserver.server;

import java.io.File;

public class Server {
	
	private File folder, config;
	
	public File getFolder() {
		return folder;
	}

	public void setFolder(File folder) {
		this.folder = folder;
	}

	public File getConfig() {
		return config;
	}

	public void setConfig(File config) {
		this.config = config;
	}

	public Server(File folder)
	{
		this.folder = folder;
	}
}
