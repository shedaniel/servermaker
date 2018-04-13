package me.spigotserver.run;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import me.spigotserver.SpigotServer;

public class RunServer implements Runnable {
	
	private File batchFile;
	private PrintWriter out;
	
	public RunServer(File file) {
		batchFile = file;
	}

	@Override
	public void run() {
		try {
			SpigotServer.frame.started = "true";
			Runtime runtime = Runtime.getRuntime();
			Process p1 = runtime.exec("cmd /c \"" + batchFile.getAbsolutePath() + "\"");
			InputStream is = p1.getInputStream();
			out = new PrintWriter(p1.getOutputStream());
			int i = 0;
			while( (i = is.read() ) != -1)
			{
				SpigotServer.frame.info((char) i);
				System.out.print((char)i);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		SpigotServer.frame.started = "false";
	}
	
	public void inputCommand(String command)
	{
		out.println(command);
		out.flush();
		SpigotServer.frame.info(">> " + command + "\n", false);
	}

}
