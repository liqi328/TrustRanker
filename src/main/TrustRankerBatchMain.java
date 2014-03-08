package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrustRankerBatchMain {
	public static void main(String[] args){
		if(args.length != 1){
			System.out.println("Argument Error.");
			System.out.println("Using method: java -Xmx2048m -jar Prioritizing.jar ./input/all_config.txt");
			System.exit(-1);
		}
		
		List<String> configFilenames = readConfigFilenames(args[0]);
		
		run_validation(configFilenames);
	}
	
	public static void run_validation(List<String> configFilenames){
		int i = 1;
		for(String config : configFilenames){
			System.out.println("" + (i++) + "--------------------"+ config +"-------------------");
			TrustRankerMain.main(new String[]{config});
		}
	}
		
	private static List<String> readConfigFilenames(String filename){
		List<String> configFilenames = new ArrayList<String>();
		String line = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
			while((line = in.readLine()) != null){
				configFilenames.add(line.trim());
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return configFilenames;
	}
}
