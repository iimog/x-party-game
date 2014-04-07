package util;

public class Version {
	private int major;
	private int minor;

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

	public Version(int major, int minor) {
		this.major = major;
		this.minor = minor;
	}

	public Version(String version) {
		this.major = 0;
		this.minor = 0;
		try {
			String[] parts = version.split("[.]");
			this.major = Integer.parseInt(parts[0]);
			this.minor = Integer.parseInt(parts[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean is_older_than(Version version) {
		if(version == null){
			System.err.println("There was an error checking the Version!");
			return false;
		}
		return is_older_than(version.getMajor(), version.getMinor());
	}

	public boolean is_older_than(int major, int minor) {
		if (this.major < major)
			return true;
		if ((this.major == major) && (this.minor < minor))
			return true;
		return false;
	}

	@Override
	public String toString() {
		return major + "." + minor;
	}
}
