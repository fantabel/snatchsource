package gouv.education.contact.snatchsource.model.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileUtils {
	public static String readFile(String path, Charset encoding)
	        throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	public static List<String> readAllLines(String path) throws IOException {
		
		return Files.readAllLines(Paths.get(new File(path).getAbsolutePath()),
		        Charset.defaultCharset());
	}
}
