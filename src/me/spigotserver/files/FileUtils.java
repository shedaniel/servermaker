package me.spigotserver.files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtils {
	
	public static boolean contains(Object o, File[] list) {
		for (File f : list) {
			if (f.equals(o)) {
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings({ "rawtypes", "resource" })
	public static void unzip(File zipFile, File extractFolder) {
		try
	    {
	        int BUFFER = 2048;
	        File file = zipFile;
	        ZipFile zip = new ZipFile(file);
	        String newPath = extractFolder.getAbsolutePath();
	        new File(newPath).mkdir();
	        Enumeration zipFileEntries = zip.entries();
	        while (zipFileEntries.hasMoreElements())
	        {
	            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
	            String currentEntry = entry.getName();
	            File destFile = new File(newPath, currentEntry);
	            File destinationParent = destFile.getParentFile();
	            destinationParent.mkdirs();
	            if (!entry.isDirectory())
	            {
	                BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
	                int currentByte;
	                byte data[] = new byte[BUFFER];
	                FileOutputStream fos = new FileOutputStream(destFile);
	                BufferedOutputStream dest = new BufferedOutputStream(fos,
	                BUFFER);
	                while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
	                    dest.write(data, 0, currentByte);
	                }
	                dest.flush();
	                dest.close();
	                is.close();
	            }
	        }
	    }catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	public static void deleteDirectory(File file) throws FileNotFoundException {
	    File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	        	deleteDirectory(f);
	        }
	    }
	    file.delete();
	}
	
	public static void copyFile(File sourceFile, File destFile) throws IOException {
		InputStream oInStream = new FileInputStream(sourceFile);
        OutputStream oOutStream = new FileOutputStream(destFile);
        byte[] oBytes = new byte[1024];
        int nLength;
        BufferedInputStream oBuffInputStream = new BufferedInputStream( oInStream );
        while ((nLength = oBuffInputStream.read(oBytes)) > 0) 
        {
            oOutStream.write(oBytes, 0, nLength);
        }
        oInStream.close();
        oOutStream.close();
	}
	
	public static void copyDirectory(File source, File destination)
	{
	    if (source.isDirectory())
	    {
	        if (!destination.exists())
	        {
	            destination.mkdirs();
	        }
	        String files[] = source.list();
	        for (String file : files)
	        {
	            File srcFile = new File(source, file);
	            File destFile = new File(destination, file);
	            copyDirectory(srcFile, destFile);
	        }
	    }
	    else
	    {
	        InputStream in = null;
	        OutputStream out = null;
	        try
	        {
	            in = new FileInputStream(source);
	            out = new FileOutputStream(destination);
	            byte[] buffer = new byte[1024];
	            int length;
	            while ((length = in.read(buffer)) > 0)
	            {
	                out.write(buffer, 0, length);
	            }
	        }
	        catch (Exception e)
	        {
	            try
	            {
	                in.close();
	            }
	            catch (IOException e1)
	            {
	                e1.printStackTrace();
	            }
	            try
	            {
	                out.close();
	            }
	            catch (IOException e1)
	            {
	                e1.printStackTrace();
	            }
	        }
	    }
	}
	
	@SuppressWarnings("unused")
	private static void renameDir(File oldDir, File newName) throws IOException {
		if (!newName.exists()) {
			newName.mkdirs();
			Files.move(oldDir.toPath(), newName.toPath(), StandardCopyOption.ATOMIC_MOVE);
			if (oldDir.exists()) {
				oldDir.delete();
			}
		}else {
			
		}
	}
}
