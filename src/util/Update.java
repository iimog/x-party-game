package util;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;

import start.X;

public class Update {
	private HashMap<String, String> response;
	private double currentVersion;
	private double latestVersion;
	private String downloadURL;

	public Update(double currentVersion) {
		this.currentVersion = currentVersion;
		getDataFromServer();
	}

	private void getDataFromServer() {
		response = new HashMap<String, String>();
		try {
			// Code to make a webservice HTTP request
			String responseString = "";
			String outputString = "";
			String wsURL = "http://iimog.org/X/ajax/Version";
			URL url = new URL(wsURL);
			URLConnection connection = url.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) connection;

			String SOAPAction = "http://litwinconsulting.com/webservices/GetWeather";
			// Set the appropriate HTTP parameters.
			httpConn.setRequestProperty("SOAPAction", SOAPAction);
			httpConn.setRequestMethod("POST");
			httpConn.setDoInput(true);

			// Ready with sending the request.

			// Read the response.
			InputStreamReader isr = new InputStreamReader(
					httpConn.getInputStream());
			BufferedReader in = new BufferedReader(isr);

			// Write the SOAP message response to a String.
			while ((responseString = in.readLine()) != null) {
				response.put(responseString.split("\t")[0],
						responseString.split("\t")[1]);
				outputString = outputString + responseString;
			}
			latestVersion = Double.parseDouble(response.get("Version"));
			downloadURL = response.get("Download");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double getLatestVersion() {
		return latestVersion;
	}

	public String getDownload() {
		return downloadURL;
	}

	public String getVersionInfoText() {
		String infoText = "";
		if (!isUpToDate()) {
			infoText = "Es ist ein Update verfügbar (aktuell:" + currentVersion
					+ ", neu:" + latestVersion + ") --> " + downloadURL;
		} else {
			infoText = "X ist aktuell (Version " + currentVersion + ")";
		}
		return infoText;
	}
	
	public boolean isUpToDate(){
		return currentVersion >= latestVersion;
	}
	
	public boolean downloadUpdateFile(){
		boolean success = false;
		try {
			URL website = new URL(getDownload());
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			File updateFile = getUpdateFile();
			updateFile.getParentFile().mkdirs();
			FileOutputStream fos = new FileOutputStream(updateFile);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
			success = true;
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return success;
	}
	
	public void installUpdate(){
		try {
			Desktop.getDesktop().open(getUpdateFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isUpdateFilePresent(){
		return getUpdateFile().exists();
	}
	
	public File getUpdateFile(){
		File updateFile = new File(X.getDataDir()+"update/X_version"+getLatestVersion()+".html");
		return updateFile;
	}
}
