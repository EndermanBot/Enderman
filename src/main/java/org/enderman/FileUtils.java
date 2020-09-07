package org.enderman;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
	private FileUtils() {}
	public static void writeTo(String path, String text) throws IOException {
		File file = new File(path);
		new File(file.getAbsolutePath()).getParentFile().mkdirs();
		file.createNewFile();
		FileWriter writer = new FileWriter(file);
		writer.write(text);
		writer.close();
	}
}
