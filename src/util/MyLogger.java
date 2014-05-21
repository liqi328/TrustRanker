package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MyLogger {
	private static final String logFilename = "./log.txt";
	private static BufferedWriter out = null;
	static{
		try {
			out = new BufferedWriter(new FileWriter(new File(logFilename), true));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void log(String content){
		try {
			out.write(content + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
