package me.spigotserver.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

import me.spigotserver.SpigotServer;
import me.spigotserver.files.FileUtils;

public class ServerManager {
	private ArrayList<Server> servers;
	private File serverFolder;
	private Server seleted;
	
	public ServerManager()
	{
		servers = new ArrayList<Server>();
	}
	
	public void addServer(Server server)
	{
		servers.add(server);
	}

	public void setServerFolder(File folder) {
		if (!folder.exists()) {
			folder.mkdirs();
		}
		serverFolder = folder;
	}
	
	public File getServerFolder() {
		return serverFolder;
	}
	
	public ArrayList<Server> getServers()
	{
		return servers;
	}
	
	public ArrayList<String> getServerNames()
	{
		ArrayList<String> names = new ArrayList<String>();
		for(Server s : servers)
		{
			names.add(s.getFolder().getName());
		}
		return names;
	}

	public void loadServers() throws NullPointerException {
		servers.clear();
		File[] filesList = getServerFolder().listFiles();
        for(File f : filesList){
            if(f.isDirectory()) {
            	addServer(new Server(f));
            }
        }
        SpigotServer.frame.listServer.setListData(SpigotServer.getServerManager().getServerNames().toArray(new String[SpigotServer.getServerManager().getServerNames().size()]));
        System.out.println(servers.size());
	}

	public Server getSeletedServer() {
		return seleted;
	}

	public void setSeleted(int i) {
		seleted = servers.get(i);
		try {
			getServerConfig();
			Map<String, String> map = SpigotServer.getServerManager().getServerConfig();
			SpigotServer.frame.minRam.setText(map.get("minRam"));
			SpigotServer.frame.maxRam.setText(map.get("maxRam"));
		} catch (IOException e) {}
	}
	
	public Map<String, String> getServerConfig() throws IOException{
		File config = new File(seleted.getFolder().getAbsolutePath() + "\\config.ini");
		if (!config.exists()) {
			PrintWriter w = new PrintWriter(config);
			w.println("minRam: 512M");
			w.println("maxRam: 1G");
			w.close();
		}
		
		Map<String, String> map = new HashMap<String, String>();
		for (String line : Files.readAllLines(config.toPath())) {
			String[] parts = line.split("\\:");
			String items = "";
			for (int i = 1; i < parts.length; i++) {
				items = items + parts[i];
			}
			if (items.startsWith(" ")) {
				items = items.replaceFirst(" ", "");
			}
			map.put(parts[0], items);
			System.out.println("Put " + parts[0] + " as " + items);
		}
		return map;
	}
	
	public void setServerConfig(Map<String, String> map) throws IOException {
		File config = new File(seleted.getFolder().getAbsolutePath() + "\\config.ini");
		if (config.exists()) {
			config.delete();
		}
		PrintWriter w = new PrintWriter(config);
		for (Map.Entry<String, String> entry : map.entrySet())
		{
			w.println(entry.getKey() + ": " + entry.getValue());
		}
		w.close();
	}
	
	public void createServer(String name, File jar) throws IOException {
		File folder = new File(getServerFolder().getAbsolutePath() + "\\" + name);
		if (folder.exists())
		{
			JOptionPane.showMessageDialog(null, "A server with the same name already exist!", "Error", JOptionPane.ERROR_MESSAGE);
		}else {
			if (!jar.getAbsolutePath().endsWith(".jar")) {
				JOptionPane.showMessageDialog(null, "Server isn't a jar file!", "Error", JOptionPane.ERROR_MESSAGE);
			}else {
				folder.mkdirs();
				FileUtils.copyFile(jar, new File(folder.getAbsolutePath() + "\\server.jar"));
				
				//EULA
				boolean acceptEULA;
				switch(JOptionPane.showConfirmDialog(null, "Do you accept the Mojang's EULA?", "Info", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
				case JOptionPane.YES_OPTION:
					acceptEULA = true;
					break;
				default:
					acceptEULA = false;
				}
				PrintWriter writerEULA = new PrintWriter(new File(folder.getAbsolutePath() + "\\eula.txt"));
				if (acceptEULA) {
					writerEULA.println("eula = true");
				}else writerEULA.println("eula = false");
				writerEULA.close();
				try {
					loadServers();
				}catch (NullPointerException e) {
					
				}
				JOptionPane.showMessageDialog(null, "Setup is done!", "Congrets!", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	public String[] getConfig() throws IOException {
		if (seleted == null) {
			return null;
		}
		File config = new File(seleted.getFolder().getAbsolutePath() + "\\server.properties");
		if (!config.exists()) {
			return null;
		}
		return Files.readAllLines(config.toPath()).toArray(new String[Files.readAllLines(config.toPath()).size()]);
	}
	
	public String getConfigData(String s) throws IOException {
		if (getConfig() == null) {
			return "";
		}
		for (String line : getConfig())
		{
			if (line.startsWith(s)) {
				return line.replaceAll(s + "=", "");
			}
		}
		return "";
	}
	
	public void refreshServerDetail()
	{
		try {
			SpigotServer.frame.listServer.setSize(200, 30 * SpigotServer.getServerManager().getServerNames().size());
			if (seleted != null) {
				SpigotServer.frame.lblServerName.setText(seleted.getFolder().getName());
				SpigotServer.frame.lblServerLocation.setText("Server Location: " + seleted.getFolder().getAbsolutePath());
				if (new File(seleted.getFolder().getAbsolutePath() + "\\server.properties").exists()) {
					SpigotServer.frame.lblRunFirst.setVisible(false);
				}else {
					SpigotServer.frame.lblRunFirst.setVisible(true);
				}
				if (SpigotServer.frame.started == "true") {
					SpigotServer.frame.btnStart.setText("Stop");
					SpigotServer.frame.btnStart.setEnabled(true);
					SpigotServer.frame.consoleCommand.setEnabled(true);
					if (new File(seleted.getFolder().getAbsolutePath() + "\\server.properties").exists()) {
						SpigotServer.frame.btnRefresh.setEnabled(true);
					}else {
						SpigotServer.frame.btnRefresh.setEnabled(false);
					}
					SpigotServer.frame.btnSave.setEnabled(false);
					SpigotServer.frame.btnResetWorld.setEnabled(false);
					SpigotServer.frame.btnResetSettings.setEnabled(false);
					SpigotServer.frame.btnRefreshRam.setEnabled(false);
					SpigotServer.frame.btnSetRam.setEnabled(false);
				}else {
					SpigotServer.frame.btnRefreshRam.setEnabled(true);
					SpigotServer.frame.btnSetRam.setEnabled(true);
					if (SpigotServer.frame.started == "false") {
						SpigotServer.frame.btnStart.setText("Start");
						SpigotServer.frame.btnStart.setEnabled(true);
						SpigotServer.frame.consoleCommand.setEnabled(false);
						if (new File(seleted.getFolder().getAbsolutePath() + "\\server.properties").exists()) {
							SpigotServer.frame.btnRefresh.setEnabled(true);
							SpigotServer.frame.btnSave.setEnabled(true);
						}else {
							SpigotServer.frame.btnRefresh.setEnabled(false);
							SpigotServer.frame.btnSave.setEnabled(false);
						}
						SpigotServer.frame.btnResetWorld.setEnabled(true);
						SpigotServer.frame.btnResetSettings.setEnabled(true);
					}else {
						SpigotServer.frame.btnStart.setText("Stopping...");
						SpigotServer.frame.btnStart.setEnabled(false);
						SpigotServer.frame.consoleCommand.setEnabled(false);
						SpigotServer.frame.btnSave.setEnabled(false);
						SpigotServer.frame.btnRefresh.setEnabled(false);
						SpigotServer.frame.btnResetWorld.setEnabled(false);
						SpigotServer.frame.btnResetSettings.setEnabled(false);
					}
				}
				File pluginFolder = new File(seleted.getFolder().getAbsolutePath() + "\\plugins");
				List<String> plugins = new ArrayList<>();
				if (pluginFolder.exists()) {
					File[] list = pluginFolder.listFiles();
					for (File file : list) {
						if (!file.isDirectory()) {
							plugins.add(file.getName());
						}
					}
				}
				List<String> list = new ArrayList<>();
				ListModel<String> model = SpigotServer.frame.pluginsList.getModel();
				for(int i=0; i < model.getSize(); i++){
				     list.add(model.getElementAt(i));
				}
				System.out.println(plugins.size() + " + " + list.size());
				if (list.size() != plugins.size()) {
					SpigotServer.frame.pluginsList.setListData(plugins.toArray(new String[plugins.size()]));
				}
				SpigotServer.frame.btnDeleteSelected.setEnabled(!(list.isEmpty() || SpigotServer.frame.started != "false"));
				SpigotServer.frame.btnDeleteAll.setEnabled(!(list.isEmpty() || SpigotServer.frame.started != "false"));
				SpigotServer.frame.fileInput.setEnabled(!(SpigotServer.frame.started != "false"));
				SpigotServer.frame.lblDragFilesHere.setText("Drag Files Here");
			}else {
				SpigotServer.frame.lblDragFilesHere.setText("Select a Server First");
				SpigotServer.frame.lblRunFirst.setVisible(false);
				SpigotServer.frame.btnRefreshRam.setEnabled(false);
				SpigotServer.frame.btnSetRam.setEnabled(false);
				SpigotServer.frame.btnStart.setEnabled(false);
				SpigotServer.frame.consoleCommand.setEnabled(false);
				SpigotServer.frame.btnSave.setEnabled(false);
				SpigotServer.frame.btnRefresh.setEnabled(false);
				SpigotServer.frame.btnResetWorld.setEnabled(false);
				SpigotServer.frame.btnResetSettings.setEnabled(false);
				SpigotServer.frame.btnDeleteSelected.setEnabled(false);
				SpigotServer.frame.btnDeleteAll.setEnabled(false);
				SpigotServer.frame.fileInput.setEnabled(false);
				List<String> Null = new ArrayList<>();
				SpigotServer.frame.pluginsList.setListData(Null.toArray(new String[Null.size()]));
			}
			SpigotServer.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}catch (NullPointerException e) {
			
		}
	}
	
	public void saveConfigData(String s, String value) throws IOException {
		if (seleted == null) {
			return;
		}
		File config = new File(seleted.getFolder().getAbsolutePath() + "\\server.properties");
		if (!config.exists()) {
			return;
		}
		String[] configData = getConfig();
		config.delete();
		PrintWriter writer = new PrintWriter(config);
		for (String line : configData) {
			if (line.startsWith(s)) {
				if (value.equalsIgnoreCase("<empty>")) {
					line = s + "=";
				}else {
					line = s + "=" + value;
				}
			}
			writer.println(line);
		}
		writer.close();
	}
}
