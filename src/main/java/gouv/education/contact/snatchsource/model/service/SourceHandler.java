package gouv.education.contact.snatchsource.model.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import gouv.education.contact.snatchsource.model.beans.Source;

public class SourceHandler {
	public Source getSource(File f) {
		Source s = null;
		
		try {
			s = new Source(
			        FileUtils.readFile(f.getPath(), Charset.defaultCharset()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return s;
	}
	
	public Source getSource(String path) {
		Source s = null;
		
		try {
			s = new Source(FileUtils.readAllLines(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return s;
	}
}
