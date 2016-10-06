package gouv.education.contact.snatchsource.model.beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gouv.education.contact.snatchsource.model.service.FileUtils;

public class Source extends Instruction {
	String name;
	String rawSource;
	List<String> sourceLines;
	List<Instruction> instructionList;
	Pattern instructionPattern = Pattern.compile("(.*?);", Pattern.DOTALL);
	Matcher instructionMatcher;
	
	public Source(String path) {
		System.out.println("Creation du source");
		try {
			sourceLines = FileUtils.readAllLines(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		parse();
		/*
		 * setRawSource(s);
		 * 
		 * findName(); System.out.println("Nom du package : " + getName());
		 * setInstructionMatcher(); parse();
		 */
	}
	
	public Source(List<String> readAllLines) {
		System.out.println("Creation du source");
		sourceLines = readAllLines;
		parse();
		System.out.println(toString());
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getRawSource() {
		return rawSource;
	}
	
	public void setRawSource(String rawSource) {
		Pattern p = Pattern.compile("(//.*)");
		Matcher m = p.matcher(rawSource);
		while (m.find()) {
			System.out.println(m.group());
		}
		this.rawSource = rawSource;
	}
	
	public List<Instruction> getInstructionList() {
		return instructionList;
	}
	
	public void setInstructionList(List<Instruction> instructionList) {
		this.instructionList = instructionList;
	}
	
	public Pattern getInstructionPattern() {
		return instructionPattern;
	}
	
	public void setInstructionPattern(Pattern instructionPattern) {
		this.instructionPattern = instructionPattern;
	}
	
	public Matcher getInstructionMatcher() {
		return instructionMatcher;
	}
	
	public void setInstructionMatcher() {
		this.instructionMatcher = getInstructionPattern()
		        .matcher(getRawSource());
	}
	
	private void parse() {
		removeComments();
	}
	
	private void removeComments() {
		List<String> newList = new ArrayList<String>();
		boolean isMultiline = false;
		for (String s : sourceLines) {
			if (!isMultiline) {
				if (s.contains("--")) {
					s = s.substring(0, s.indexOf("--"));
				} else if (Pattern.matches("(/\\*.*\\*/", s)) {
					do {
						s = s.substring(0, s.indexOf("/*"));
						
					} while (1 == 2);
					isMultiline = true;
				}
				newList.add(s);
			} else {
				if (s.contains("*/")) {
					isMultiline = false;
					s = s.substring(s.indexOf("*/") + 2);
					newList.add(s);
				}
			}
		}
		sourceLines = newList;
		
	}
	
	private List<String> extractFunctions() {
		List<String> list = new ArrayList<String>();
		
		return list;
	}
	
	private void findName() {
		Pattern p = Pattern.compile("(?<=create(?: or replace) )(.*)(?= is)",
		        Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(rawSource);
		if (m.find()) {
			String[] tab = m.group().replace("\"", "").split(" ");
			setName(tab[tab.length - 1]);
		}
		
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (String s : sourceLines) {
			sb.append(s);
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
}
