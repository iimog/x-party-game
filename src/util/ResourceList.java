package util;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;
import java.security.*;

public class ResourceList {
	private static void collectURL(ResourceURLFilter f, Set<URL> s, URL u) {
		if (f == null || f.accept(u)) {
			s.add(u);
		}
	}

	private static void iterateFileSystem(File r, ResourceURLFilter f,
			Set<URL> s) throws MalformedURLException, IOException {
		File[] files = r.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				iterateFileSystem(file, f, s);
			} else if (file.isFile()) {
				collectURL(f, s, file.toURI().toURL());
			}
		}
	}

	private static void iterateJarFile(File file, ResourceURLFilter f,
			Set<URL> s) throws MalformedURLException, IOException {
		JarFile jFile = new JarFile(file);
		for (Enumeration<JarEntry> je = jFile.entries(); je.hasMoreElements();) {
			JarEntry j = je.nextElement();
			if (!j.isDirectory()) {
				collectURL(f, s,
						new URL("jar", "", file.toURI() + "!/" + j.getName()));
			}
		}
		jFile.close();
	}

	private static void iterateEntry(File p, ResourceURLFilter f, Set<URL> s)
			throws MalformedURLException, IOException {
		if (p.isDirectory()) {
			iterateFileSystem(p, f, s);
		} else if (p.isFile() && p.getName().toLowerCase().endsWith(".jar")) {
			iterateJarFile(p, f, s);
		}
	}

	public static Set<URL> getResourceURLs() throws IOException,
			URISyntaxException {
		return getResourceURLs((ResourceURLFilter) null);
	}

	public static Set<URL> getResourceURLs(Class<?> rootClass)
			throws IOException, URISyntaxException {
		return getResourceURLs(rootClass, (ResourceURLFilter) null);
	}

	public static Set<URL> getResourceURLs(ResourceURLFilter filter)
			throws IOException, URISyntaxException {
		Set<URL> collectedURLs = new HashSet<>();
		String pathSeparator = System
			    .getProperty("path.separator");
		String[] classPathEntries = System
		    .getProperty("java.class.path")
		    .split(pathSeparator);
		for(String file : classPathEntries) {
			iterateEntry(new File(file), filter, collectedURLs);
		}
		return collectedURLs;
	}

	public static Set<URL> getResourceURLs(Class<?> rootClass,
			ResourceURLFilter filter) throws IOException, URISyntaxException {
		Set<URL> collectedURLs = new HashSet<>();
		CodeSource src = rootClass.getProtectionDomain().getCodeSource();
		iterateEntry(new File(src.getLocation().toURI()), filter, collectedURLs);
		return collectedURLs;
	}

	public static void main(String args[]) {
		try {
			for (URL u : ResourceList.getResourceURLs()) {
				System.out.println(u);
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
}