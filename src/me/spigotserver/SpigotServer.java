package me.spigotserver;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import me.spigotserver.server.ServerManager;
import me.spigotserver.version.VersionManager;
import me.spigotserver.view.View;

public class SpigotServer {
	
	private static ServerManager sm;
	public static View frame;

	public static void main(String[] args) {
		String latestVersion = null;
		try {
            URL game = new URL("https://raw.githubusercontent.com/shedaniel/servermaker/master/version.txt");
            URLConnection connection = game.openConnection();
            BufferedReader in = new BufferedReader(new
            InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
            	latestVersion = inputLine;
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		//Set look and feel
		try {
			//UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (Exception e) {}
		
		//Version
		if (latestVersion != null) {
			VersionManager vm = new VersionManager();
			if (!vm.compareVersions(vm.getVersion(), latestVersion)) {
				JOptionPane.showMessageDialog(null, "New update is here!\nGo and download version " + latestVersion);
			}
		}
		
		File settings = new File("settings.ini");
		if (!settings.exists()) {
			//Load
			JFileChooser fc = new JFileChooser(new File(System.getProperty("user.dir")));
			fc.setDialogTitle("Select Servers Folder");
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.showOpenDialog(null);
			if (fc.getSelectedFile() == null) {
				JOptionPane.showMessageDialog(null, "Can't load Selected File!", "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			try {
				PrintWriter writer = new PrintWriter(settings);
				writer.print("directory: \"" + fc.getSelectedFile() + "\"");
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		//Load Stuff
		sm = new ServerManager();
		loadGUI();
		loadServers();
		
		Thread t = null;
		t = new Thread(new Runnable() {
			
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				while (true)
				{
					getServerManager().refreshServerDetail();
					try {
						t.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}

	private static void loadGUI() {
		try {
			Thread gui = new Thread(new Runnable() {
				
				@Override
				public void run() {
					frame = new View();
					frame.setVisible(true);
					frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
					frame.addWindowListener(new WindowAdapter() {
						@SuppressWarnings("deprecation")
						public void windowClosing(WindowEvent e) {
			            	if (frame.started == "false") {
			            		frame.dispose();
			            		System.exit(0);
			            	}
			            	frame.serverThread.stop();
		            		frame.dispose();
			            	showWarningMessage();
		            		System.exit(0);
						}
					});
				}
			});
			gui.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void showWarningMessage() {
        JOptionPane.showMessageDialog(frame,
                "The server is still running!\nForce killing it may lead to world data loss!",
                "Warning",
                JOptionPane.WARNING_MESSAGE);    
    }

	private static void loadServers() {
		String path = "";
		try {
			List<String> settings = Files.readAllLines(new File("settings.ini").getAbsoluteFile().toPath());
			System.out.println(new File("settings.ini").getAbsoluteFile().toPath());
			for (String line : settings) {
				System.out.println(line);
				if(line.startsWith("directory: ")) {
					path = line;
					path = path.replaceFirst("directory: ", "");
					path = path.replaceAll("\"", "");
				}
			}
			if (path == "" || !new File(path).exists()) {
				JOptionPane.showMessageDialog(null, "Can't find server folder\n" + path, "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		sm.setServerFolder(new File(path));
		try {
			sm.loadServers();
		}catch (NullPointerException e) {
			
		}
	}

	public static ServerManager getServerManager()
	{
		return sm;
	}

}
