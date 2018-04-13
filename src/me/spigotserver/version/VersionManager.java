package me.spigotserver.version;

public class VersionManager {
	private static final String version = "0.1.0";
	private static final boolean isBeta = false;
	
	public String getVersion() {
		return version;
	}
	
	public boolean isBeta() {
		return isBeta;
	}
	
	public int compareVersionNumber(String v1, String v2) {
	    String[] components1 = v1.split("\\.");
	    String[] components2 = v2.split("\\.");
	    int length = Math.min(components1.length, components2.length);
	    for(int i = 0; i < length; i++) {
	        int result = new Integer(components1[i]).compareTo(Integer.parseInt(components2[i]));
	        if(result != 0) {
	            return result;
	        }
	    }
	    return Integer.compare(components1.length, components2.length);
	}
	
	public boolean compareVersions(String v1, String v2)
	{
		if (isBeta()) {
			return true;
		}
		if (compareVersionNumber(v1, v2) >= 0) {
			return true;
		}
		return false;
	}
	
}
