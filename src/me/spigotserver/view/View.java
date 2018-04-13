package me.spigotserver.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import me.spigotserver.SpigotServer;
import me.spigotserver.files.FileDrop;
import me.spigotserver.files.FileUtils;
import me.spigotserver.run.RunServer;

public class View extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8710024863311300354L;
	private JPanel contentPane, menu;
	public JList<String> listServer;
	private JPanel advancedPanel;
	public JLabel lblServerName;
	public JLabel lblCreateServer;
	public JLabel lblServerLocation;
	public JTextPane console;
	public JButton btnStart;
	public String started = "false";
	Runnable serverRun;
	public JTextField textField;
	public JTabbedPane tabbedPane;
	public JScrollPane propertyPane;
	private JPanel settings;
	private JLabel label_14;
	private JLabel label_15;
	private JLabel label_16;
	private JLabel label_17;
	private JLabel label_18;
	private JLabel label_19;
	private JLabel label_20;
	private JLabel label_21;
	private JLabel label_22;
	private JLabel label_23;
	private JLabel label_24;
	private JLabel label_25;
	private JLabel label_26;
	private JLabel label_27;
	public JLabel lblRunFirst;
	private JLabel lblSpawnVillager;
	private JLabel lblViewDistance;
	private JLabel lblTechnical;
	private JLabel lblMotd;
	private JLabel lblServerPort;
	private JTextField maxPlayers;
	private JCheckBox forceGamemode;
	private JCheckBox hardcoreMode;
	private JCheckBox allowFlight;
	private JCheckBox onlineMode;
	private JCheckBox commandBlock;
	private JTextField spawnProtection;
	private JCheckBox villager;
	private JTextField worldSeed;
	private JTextField buildLimit;
	private JCheckBox allowNether, whitelist;
	private JTextField motd;
	private JTextField viewDistance;
	private JTextField serverPort;
	private JTextField serverIP;
	String[] gamemodes = {"Survival (0)", "Creative (1)", "Adventure (2)", "Spectator (3)"};
	String[] difficulties = {"Peaceful (0)", "Easy (1)", "Normal (2)", "Hard (3)"};
	private JCheckBox pvp;
	public JButton btnRefresh, btnRefreshRam, btnSetRam;
	public JButton btnSave;
	private JComboBox<String> gamemode, difficulty;
	private JLabel lblMoreSettings;
	public JButton btnResetWorld;
	public JButton btnResetSettings;
	private JLabel lblConsole;
	public JTextField textField_1;
	public JTextField textField_2;
	private JLabel lblPlugins;
	private JLabel lblInputPlugins;
	public JList<String> pluginsList;
	public JButton btnDeleteSelected;
	public JButton btnDeleteAll;
	public JPanel fileInput;
	public JLabel lblDragFilesHere;

	/**
	 * Create the frame.
	 */
	public View() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Server Maker");
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		menu = new JPanel();
		menu.setBackground(new Color(255, 204, 255));
		menu.setBounds(0, 0, 200, 571);
		contentPane.add(menu);
		menu.setLayout(null);
		
		JLabel lblServers = new JLabel("Servers");
		lblServers.setForeground(new Color(51, 51, 102));
		lblServers.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 40));
		lblServers.setBounds(10, 11, 178, 38);
		menu.add(lblServers);
		
		lblCreateServer = new JLabel("Create Server");
		lblCreateServer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String name = JOptionPane.showInputDialog(null, "Input Server Name", "Info", JOptionPane.QUESTION_MESSAGE);
				if (name != "") {
					JFileChooser fc = new JFileChooser();
					fc.setCurrentDirectory(SpigotServer.getServerManager().getServerFolder());
					fc.setDialogTitle("Select Server Jar");
					fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					fc.setFileFilter(new FileNameExtensionFilter("Jar Files", "jar"));
					fc.showOpenDialog(null);
					if (fc.getSelectedFile() != null)
						try {
							SpigotServer.getServerManager().createServer(name, fc.getSelectedFile());
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}
		});
		lblCreateServer.setForeground(new Color(0, 51, 255));
		lblCreateServer.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 30));
		lblCreateServer.setBackground(new Color(255, 255, 255));
		lblCreateServer.setBounds(10, 528, 178, 32);
		menu.add(lblCreateServer);
		
		listServer = new JList<String>();
		listServer.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				SpigotServer.getServerManager().setSeleted(listServer.getSelectedIndex());
				try {
					refreshServerData();
				} catch (IOException ep) {
					ep.printStackTrace();
				}
			}
		});
		listServer.setCellRenderer(new SelectedListCellRenderer());
		listServer.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 18));
		listServer.setForeground(new Color(0, 0, 102));
		listServer.setBackground(new Color(255, 204, 255));
		listServer.setListData(SpigotServer.getServerManager().getServerNames().toArray(new String[SpigotServer.getServerManager().getServerNames().size()]));
		listServer.setBounds(0, 60, 200, 456);
		menu.add(listServer);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(200, 0, 594, 571);
		contentPane.add(tabbedPane);
		
		JPanel basicPanel = new JPanel();
		basicPanel.setBackground(new Color(255, 255, 255));
		tabbedPane.addTab("General", null, basicPanel, null);
		basicPanel.setLayout(null);
		
		lblServerName = new JLabel("*");
		lblServerName.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 30));
		lblServerName.setBounds(10, 11, 559, 41);
		basicPanel.add(lblServerName);
		
		lblServerLocation = new JLabel("Server Location: *");
		lblServerLocation.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		lblServerLocation.setBounds(10, 60, 559, 21);
		basicPanel.add(lblServerLocation);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (!(started == "true")) {
					try {
						Map<String, String> map = SpigotServer.getServerManager().getServerConfig();
						File batchFile = new File(SpigotServer.getServerManager().getSeletedServer().getFolder() + "\\run.bat");
						if (batchFile.exists()) {batchFile.delete();}
						PrintWriter writer = new PrintWriter(batchFile);
						writer.println("@echo off");
						writer.println("cd \"" + SpigotServer.getServerManager().getSeletedServer().getFolder() + "\"");
						writer.println("java -Xms" + map.get("minRam") + " -Xmx" + map.get("maxRam") + " -jar server.jar");
						writer.close();
						batchFile.deleteOnExit();
						
						info("Running in " + batchFile.getAbsolutePath() + "\n");
						serverRun = new RunServer(batchFile);
						Thread thread = new Thread(serverRun);
						thread.start();
					}catch (IOException ep) {
						ep.printStackTrace();
					}
				}else {
					started = "closing";
					RunServer run = (RunServer) serverRun;
					run.inputCommand("stop");
				}
			}
		});
		btnStart.setBounds(10, 206, 89, 23);
		basicPanel.add(btnStart);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 240, 559, 262);
		basicPanel.add(scrollPane);
		
		console = new JTextPane();
		scrollPane.setViewportView(console);
		console.setEditable(false);
		console.setBackground(new Color(255, 255, 204));
		console.setText("Server Console");
		console.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (started == "true") {
					if (textField.getText() == "stop") {
						started = "closing";
						RunServer run = (RunServer) serverRun;
						run.inputCommand("stop");
						textField.setText("");
					}else {
						RunServer run = (RunServer) serverRun;
						run.inputCommand(textField.getText());
						textField.setText("");
					}
				}
			}
		});
		textField.setBounds(10, 502, 559, 20);
		basicPanel.add(textField);
		textField.setColumns(10);
		
		lblMoreSettings = new JLabel("Reset");
		lblMoreSettings.setBounds(10, 92, 559, 32);
		basicPanel.add(lblMoreSettings);
		lblMoreSettings.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 24));
		
		btnResetWorld = new JButton("Reset World");
		btnResetWorld.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(JOptionPane.showConfirmDialog(null, "Do you really want to reset the World?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
					try {
						System.out.println("Deleting");
						FileUtils.deleteDirectory(new File(SpigotServer.getServerManager().getSeletedServer().getFolder().getAbsolutePath() + "\\world"));
						FileUtils.deleteDirectory(new File(SpigotServer.getServerManager().getSeletedServer().getFolder().getAbsolutePath() + "\\world_nether"));
						FileUtils.deleteDirectory(new File(SpigotServer.getServerManager().getSeletedServer().getFolder().getAbsolutePath() + "\\world_the_end"));
						JOptionPane.showMessageDialog(null, "Successfully reseted the world!", "Done", JOptionPane.INFORMATION_MESSAGE);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		});
		btnResetWorld.setBounds(10, 135, 137, 21);
		basicPanel.add(btnResetWorld);
		
		btnResetSettings = new JButton("Reset Server Settings");
		btnResetSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File property = new File(SpigotServer.getServerManager().getSeletedServer().getFolder().getAbsolutePath() + "\\server.properties");
				if (property.exists()) {
					property.delete();
					try {
						refreshServerData();
					} catch (IOException e) {}
					JOptionPane.showMessageDialog(null, "Successfully reseted the server property!\nPlease start the server to generate it again!", "Done", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnResetSettings.setBounds(157, 135, 137, 21);
		basicPanel.add(btnResetSettings);
		
		lblConsole = new JLabel("Console");
		lblConsole.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 24));
		lblConsole.setBounds(10, 167, 89, 32);
		basicPanel.add(lblConsole);
		
		JLabel lblMinimumRam = new JLabel("Minimum Ram:");
		lblMinimumRam.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		lblMinimumRam.setBounds(109, 206, 98, 21);
		basicPanel.add(lblMinimumRam);
		
		textField_1 = new JTextField();
		textField_1.setBounds(217, 207, 86, 20);
		basicPanel.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblMaximumRam = new JLabel("Maximum Ram:");
		lblMaximumRam.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		lblMaximumRam.setBounds(313, 206, 98, 21);
		basicPanel.add(lblMaximumRam);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(421, 207, 86, 20);
		basicPanel.add(textField_2);
		
		btnRefreshRam = new JButton("Refresh Ram");
		btnRefreshRam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Map<String, String> map = SpigotServer.getServerManager().getServerConfig();
					textField_1.setText(map.get("minRam"));
					textField_2.setText(map.get("maxRam"));
				} catch (IOException e) {}
			}
		});
		btnRefreshRam.setBounds(109, 178, 98, 23);
		basicPanel.add(btnRefreshRam);
		
		btnSetRam = new JButton("Set Ram");
		btnSetRam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Map<String, String> map = SpigotServer.getServerManager().getServerConfig();
					map.replace("minRam", textField_1.getText());
					map.replace("maxRam", textField_2.getText());
					SpigotServer.getServerManager().setServerConfig(map);
				} catch (IOException e) {}
			}
		});
		btnSetRam.setBounds(217, 178, 89, 23);
		basicPanel.add(btnSetRam);
		
		advancedPanel = new JPanel();
		advancedPanel.setBackground(new Color(255, 255, 255));
		tabbedPane.addTab("Advanced", null, advancedPanel, null);
		advancedPanel.setLayout(null);
		
		JLabel lblServerProperties = new JLabel("Server Properties");
		lblServerProperties.setBounds(10, 11, 230, 41);
		lblServerProperties.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 30));
		advancedPanel.add(lblServerProperties);
		
		propertyPane = new JScrollPane();
		propertyPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		propertyPane.setBounds(0, 63, 589, 480);
		propertyPane.getVerticalScrollBar().setUnitIncrement(20);
		propertyPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		advancedPanel.add(propertyPane);
		
		settings = new JPanel();
		settings.setLayout(null);
		settings.setBackground(Color.WHITE);
		settings.setPreferredSize(new Dimension(589, 600));
		propertyPane.setViewportView(settings);
		
		label_14 = new JLabel("Max Players:");
		label_14.setToolTipText("The maximum number of players that can play on the server at the same time. Note that if more players are on the server it will use more resources.");
		label_14.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		label_14.setBounds(10, 43, 128, 22);
		settings.add(label_14);
		
		label_15 = new JLabel("Gamemode:");
		label_15.setToolTipText("Defines the mode of gameplay.");
		label_15.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		label_15.setBounds(10, 76, 128, 22);
		settings.add(label_15);
		
		label_16 = new JLabel("Force Gamemode:");
		label_16.setToolTipText("Force players to join in the default game mode.");
		label_16.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		label_16.setBounds(213, 76, 128, 22);
		settings.add(label_16);
		
		label_17 = new JLabel("Difficulty:");
		label_17.setToolTipText("Defines the difficulty (such as damage dealt by mobs and the way hunger and poison affects players) of the server.");
		label_17.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		label_17.setBounds(10, 109, 128, 22);
		settings.add(label_17);
		
		label_18 = new JLabel("Hardcore Mode:");
		label_18.setToolTipText("If set to true, players will be set to spectator mode if they die.");
		label_18.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		label_18.setBounds(213, 109, 128, 22);
		settings.add(label_18);
		
		label_19 = new JLabel("PvP:");
		label_19.setToolTipText("Enable PvP on the server. Players shooting themselves with arrows will only receive damage if PvP is enabled.");
		label_19.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		label_19.setBounds(10, 142, 128, 22);
		settings.add(label_19);
		
		label_20 = new JLabel("Whitelist:");
		label_20.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		label_20.setBounds(10, 175, 96, 22);
		settings.add(label_20);
		
		label_21 = new JLabel("Command Block:");
		label_21.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		label_21.setBounds(10, 241, 128, 22);
		settings.add(label_21);
		
		label_22 = new JLabel("Spawn Protection:");
		label_22.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		label_22.setBounds(10, 274, 128, 22);
		settings.add(label_22);
		
		label_23 = new JLabel("General");
		label_23.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		label_23.setBounds(10, 11, 128, 22);
		settings.add(label_23);
		
		label_24 = new JLabel("World");
		label_24.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		label_24.setBounds(10, 208, 128, 22);
		settings.add(label_24);
		
		label_25 = new JLabel("World Seed:");
		label_25.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		label_25.setBounds(213, 241, 128, 22);
		settings.add(label_25);
		
		label_26 = new JLabel("Build Limit:");
		label_26.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		label_26.setBounds(213, 274, 128, 22);
		settings.add(label_26);
		
		label_27 = new JLabel("Online Mode:");
		label_27.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		label_27.setBounds(213, 175, 128, 22);
		settings.add(label_27);
		
		lblSpawnVillager = new JLabel("Spawn Villager:");
		lblSpawnVillager.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		lblSpawnVillager.setBounds(10, 307, 128, 22);
		settings.add(lblSpawnVillager);
		
		lblViewDistance = new JLabel("View Distance:");
		lblViewDistance.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		lblViewDistance.setBounds(213, 373, 128, 22);
		settings.add(lblViewDistance);
		
		lblTechnical = new JLabel("Technical");
		lblTechnical.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		lblTechnical.setBounds(10, 340, 128, 22);
		settings.add(lblTechnical);
		
		lblMotd = new JLabel("MOTD:");
		lblMotd.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		lblMotd.setBounds(10, 373, 128, 22);
		settings.add(lblMotd);
		
		lblServerPort = new JLabel("Server Port:");
		lblServerPort.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		lblServerPort.setBounds(10, 406, 128, 22);
		settings.add(lblServerPort);
		
		JLabel lblAllowNether = new JLabel("Allow Nether:");
		lblAllowNether.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		lblAllowNether.setBounds(213, 307, 128, 22);
		settings.add(lblAllowNether);
		
		JLabel lblServerIp = new JLabel("Server IP:");
		lblServerIp.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		lblServerIp.setBounds(213, 406, 128, 22);
		settings.add(lblServerIp);
		
		JLabel lblAllowFlight = new JLabel("Allow Flight:");
		lblAllowFlight.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		lblAllowFlight.setBounds(213, 142, 128, 22);
		settings.add(lblAllowFlight);
		
		maxPlayers = new JTextField();
		maxPlayers.setBounds(109, 45, 86, 22);
		settings.add(maxPlayers);
		maxPlayers.setColumns(10);
		
		gamemode = new JComboBox<String>();
		for (String s : gamemodes) {
			gamemode.addItem(s);
		}
		gamemode.setBounds(109, 76, 86, 22);
		settings.add(gamemode);
		
		difficulty = new JComboBox<String>();
		for (String s : difficulties) {
			difficulty.addItem(s);
		}
		difficulty.setBounds(109, 109, 86, 22);
		settings.add(difficulty);
		
		whitelist = new JCheckBox("");
		whitelist.setBackground(Color.WHITE);
		whitelist.setBounds(174, 174, 21, 23);
		settings.add(whitelist);
		
		forceGamemode = new JCheckBox("");
		forceGamemode.setBackground(Color.WHITE);
		forceGamemode.setBounds(347, 75, 21, 23);
		settings.add(forceGamemode);
		
		hardcoreMode = new JCheckBox("");
		hardcoreMode.setBackground(Color.WHITE);
		hardcoreMode.setBounds(347, 108, 21, 23);
		settings.add(hardcoreMode);
		
		allowFlight = new JCheckBox("");
		allowFlight.setBackground(Color.WHITE);
		allowFlight.setBounds(347, 141, 21, 23);
		settings.add(allowFlight);
		
		onlineMode = new JCheckBox("");
		onlineMode.setBackground(Color.WHITE);
		onlineMode.setBounds(347, 174, 21, 23);
		settings.add(onlineMode);
		
		commandBlock = new JCheckBox("");
		commandBlock.setBackground(Color.WHITE);
		commandBlock.setBounds(174, 240, 21, 23);
		settings.add(commandBlock);
		
		spawnProtection = new JTextField();
		spawnProtection.setBounds(148, 278, 47, 20);
		settings.add(spawnProtection);
		spawnProtection.setColumns(10);
		
		villager = new JCheckBox("");
		villager.setBackground(Color.WHITE);
		villager.setBounds(174, 306, 21, 23);
		settings.add(villager);
		
		worldSeed = new JTextField();
		worldSeed.setColumns(10);
		worldSeed.setBounds(351, 244, 86, 22);
		settings.add(worldSeed);
		
		buildLimit = new JTextField();
		buildLimit.setColumns(10);
		buildLimit.setBounds(351, 277, 86, 22);
		settings.add(buildLimit);
		
		allowNether = new JCheckBox("");
		allowNether.setBackground(Color.WHITE);
		allowNether.setBounds(351, 307, 21, 23);
		settings.add(allowNether);
		
		motd = new JTextField();
		motd.setColumns(10);
		motd.setBounds(109, 376, 86, 22);
		settings.add(motd);
		
		viewDistance = new JTextField();
		viewDistance.setColumns(10);
		viewDistance.setBounds(351, 376, 86, 22);
		settings.add(viewDistance);
		
		serverPort = new JTextField();
		serverPort.setColumns(10);
		serverPort.setBounds(109, 410, 86, 22);
		settings.add(serverPort);
		
		serverIP = new JTextField();
		serverIP.setColumns(10);
		serverIP.setBounds(351, 410, 86, 22);
		settings.add(serverIP);
		
		pvp = new JCheckBox("");
		pvp.setBackground(Color.WHITE);
		pvp.setBounds(174, 141, 21, 23);
		settings.add(pvp);
		
		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					refreshServerData();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnRefresh.setBounds(250, 11, 89, 23);
		advancedPanel.add(btnRefresh);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					SpigotServer.getServerManager().saveConfigData("max-players", maxPlayers.getText());
					SpigotServer.getServerManager().saveConfigData("gamemode", String.valueOf(gamemode.getSelectedIndex()));
					SpigotServer.getServerManager().saveConfigData("difficulty", String.valueOf(difficulty.getSelectedIndex()));
					SpigotServer.getServerManager().saveConfigData("force-gamemode", String.valueOf(forceGamemode.isSelected()));
					SpigotServer.getServerManager().saveConfigData("hardcore", String.valueOf(hardcoreMode.isSelected()));
					SpigotServer.getServerManager().saveConfigData("pvp", String.valueOf(pvp.isSelected()));
					SpigotServer.getServerManager().saveConfigData("allow-flight", String.valueOf(allowFlight.isSelected()));
					SpigotServer.getServerManager().saveConfigData("white-list", String.valueOf(whitelist.isSelected()));
					SpigotServer.getServerManager().saveConfigData("online-mode", String.valueOf(onlineMode.isSelected()));
					SpigotServer.getServerManager().saveConfigData("enable-command-block", String.valueOf(commandBlock.isSelected()));
					SpigotServer.getServerManager().saveConfigData("level-seed", worldSeed.getText());
					SpigotServer.getServerManager().saveConfigData("spawn-protection", spawnProtection.getText());
					SpigotServer.getServerManager().saveConfigData("max-build-height", buildLimit.getText());
					SpigotServer.getServerManager().saveConfigData("spawn-npcs", String.valueOf(villager.isSelected()));
					SpigotServer.getServerManager().saveConfigData("allow-nether", String.valueOf(allowNether.isSelected()));
					SpigotServer.getServerManager().saveConfigData("motd", motd.getText());
					SpigotServer.getServerManager().saveConfigData("view-distance", viewDistance.getText());
					SpigotServer.getServerManager().saveConfigData("server-port", serverPort.getText());
					SpigotServer.getServerManager().saveConfigData("server-ip", serverIP.getText());
					refreshServerData();
					JOptionPane.showMessageDialog(null, "Successfully Saved Config");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnSave.setBounds(349, 11, 89, 23);
		advancedPanel.add(btnSave);
		
		lblRunFirst = new JLabel("Please run the server for the first time so the property file will generate!");
		lblRunFirst.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblRunFirst.setBounds(250, 39, 329, 13);
		advancedPanel.add(lblRunFirst);
		
		JPanel plugins = new JPanel();
		plugins.setBackground(Color.WHITE);
		tabbedPane.addTab("Plugins", null, plugins, null);
		plugins.setLayout(null);
		
		lblPlugins = new JLabel("Plugins");
		lblPlugins.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 30));
		lblPlugins.setBounds(10, 11, 102, 41);
		plugins.add(lblPlugins);
		
		lblInputPlugins = new JLabel("Input Plugins");
		lblInputPlugins.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		lblInputPlugins.setBounds(10, 63, 127, 22);
		plugins.add(lblInputPlugins);
		
		fileInput = new JPanel();
		fileInput.setBorder(new LineBorder(new Color(0, 0, 0)));
		fileInput.setBounds(10, 96, 569, 114);
		new FileDrop(fileInput, new FileDrop.Listener() {
			@Override
			public void filesDropped(File[] files) {
				File pluginFolder = new File(SpigotServer.getServerManager().getSeletedServer().getFolder().getAbsolutePath() + "\\plugins");
				if (pluginFolder.exists()) {
					File[] list = pluginFolder.listFiles();
					for (File f : files) {
						if (FileUtils.contains(f, list)) {
							int answer = JOptionPane.showConfirmDialog(null, "Plugin " + f.getName() + " already exist!\nDo you want to override it?", "Info", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
							if (answer == JOptionPane.YES_OPTION) {
								try {
									FileUtils.copyFile(f, new File(pluginFolder.getAbsoluteFile() + "\\" + f.getName()));
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}else {
							try {
								FileUtils.copyFile(f, new File(pluginFolder.getAbsoluteFile() + "\\" + f.getName()));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
		plugins.add(fileInput);
		fileInput.setLayout(null);
		
		lblDragFilesHere = new JLabel("Drag Files Here");
		lblDragFilesHere.setFont(new Font("Segoe UI", Font.PLAIN, 36));
		lblDragFilesHere.setBounds(1, 1, 567, 112);
		lblDragFilesHere.setHorizontalAlignment(SwingConstants.CENTER);
		fileInput.add(lblDragFilesHere);
		
		JLabel lblPluginsList = new JLabel("Plugins List");
		lblPluginsList.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		lblPluginsList.setBounds(10, 221, 127, 22);
		plugins.add(lblPluginsList);
		
		btnDeleteSelected = new JButton("Delete Selected");
		btnDeleteSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File plugin = new File(SpigotServer.getServerManager().getSeletedServer().getFolder().getAbsolutePath() + "\\plugins\\" + pluginsList.getSelectedValue());
				if (plugin.exists()) {
					plugin.delete();
				}
			}
		});
		btnDeleteSelected.setBounds(10, 508, 116, 23);
		plugins.add(btnDeleteSelected);
		
		btnDeleteAll = new JButton("Delete All");
		btnDeleteAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(null, "Do you really want to delete all your plugins?", "Warning", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
					return;
				}
				List<String> list = new ArrayList<>();
				ListModel<String> model = pluginsList.getModel();
				for(int i=0; i < model.getSize(); i++){
				     list.add(model.getElementAt(i));
				}
				for (String s : list) {
					File plugin = new File(SpigotServer.getServerManager().getSeletedServer().getFolder().getAbsolutePath() + "\\plugins\\" + s);
					if (plugin.exists()) {
						plugin.delete();
					}
				}
			}
		});
		btnDeleteAll.setBounds(136, 508, 116, 23);
		plugins.add(btnDeleteAll);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 254, 569, 243);
		plugins.add(scrollPane_1);
		
		pluginsList = new JList<String>();
		scrollPane_1.setViewportView(pluginsList);
		pluginsList.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		setSize(new Dimension(800, 600));
		setLocationRelativeTo(null);
	}
	
	public void info(String s) {
		info(s, true);
	}
	
	public void info(char c) {
		Document doc = console.getDocument();
		try {
			doc.insertString(doc.getLength(), String.valueOf(c), console.getStyle("large"));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void infoChar(String s) {
		Document doc = console.getDocument();
		try {
			doc.insertString(doc.getLength(), s, console.getStyle("large"));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void info(String s, boolean newLine) {
		if (newLine) {
			infoChar("\n");
		}
		for (char c : s.toCharArray()) {
			info(c);
		}
	}
	
	public JPanel getContentPane() {
		return contentPane;
	}
	
	public JPanel getMenuPane() {
		return menu;
	}
	
	public JList<String> getServerList() {
		return listServer;
	}
	
	public void refreshServerData() throws IOException {
		setCheckboxSelected(pvp, "pvp");
		setCheckboxSelected(onlineMode, "online-mode");
		setCheckboxSelected(allowFlight, "allow-flight");
		setCheckboxSelected(allowNether, "allow-nether");
		setCheckboxSelected(commandBlock, "enable-command-block");
		setCheckboxSelected(forceGamemode, "force-gamemode");
		setCheckboxSelected(hardcoreMode, "hardcore");
		setCheckboxSelected(villager, "spawn-npcs");
		setCheckboxSelected(whitelist, "white-list");
		setPropertyText(maxPlayers, "max-players");
		setPropertyText(spawnProtection, "spawn-protection");
		setPropertyText(worldSeed, "level-seed");
		setPropertyText(buildLimit, "max-build-height");
		setPropertyText(motd, "motd");
		setPropertyText(viewDistance, "view-distance");
		setPropertyText(serverIP, "server-ip");
		setPropertyText(serverPort, "server-port");
		gamemode.setSelectedIndex(Integer.parseInt(SpigotServer.getServerManager().getConfigData("gamemode")));
		difficulty.setSelectedIndex(Integer.parseInt(SpigotServer.getServerManager().getConfigData("difficulty")));
	}

	private void setCheckboxSelected(JCheckBox checkbox, String string) throws IOException {
		String data = SpigotServer.getServerManager().getConfigData(string);
		if (data.equalsIgnoreCase("false")) {
			checkbox.setSelected(false);
		}else if (data.equalsIgnoreCase("true")) {
			checkbox.setSelected(true);
		}else {
			checkbox.setSelected(false);
		}
	}
	
	private void setPropertyText(JTextField text, String string) throws IOException {
		String data = SpigotServer.getServerManager().getConfigData(string);
		if (data.equals("")) {
			text.setText("<empty>");
		}else {
			text.setText(data);
		}
	}
	
	public class SelectedListCellRenderer extends DefaultListCellRenderer {
	     /**
		 * 
		 */
		private static final long serialVersionUID = 3014146190484792611L;

		@SuppressWarnings("rawtypes")
		@Override
	     public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	         Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	         if (isSelected) {
	             c.setBackground(new Color(255, 97, 213));
	         }
	         return c;
	     }
	}
}
