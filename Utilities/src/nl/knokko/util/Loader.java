package nl.knokko.util;

import java.io.File;
import java.io.InputStream;

public final class Loader {
	
	/**
	 * Loads a resource that should be located inside the jar.
	 * @param path The package name + the 'file'name.
	 * @return an InputStream for the given path.
	 */
	public static InputStream loadInternalResource(String path){
		InputStream input = Loader.class.getClassLoader().getResourceAsStream(path);
		if(input == null)
			throw new IllegalArgumentException("Can't load path " + path);
		return input;
	}
	
	public static File getRelativeFile(String path){
		return new File(path);
	}
	
	public static File getRelativeFile(String... path){
		StringBuilder builder = new StringBuilder();
		for(String p : path){
			if(builder.length() > 0)
				builder.append(File.separatorChar);
			builder.append(p);
		}
		return getRelativeFile(builder.toString());
	}
}
