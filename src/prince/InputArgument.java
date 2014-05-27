package prince;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class InputArgument{
	private Properties p = new Properties();
	
	public InputArgument(String configFilepath){
		try {
			InputStreamReader is = new InputStreamReader(new FileInputStream(configFilepath), "UTF-8");
			p.load(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getPpiFilepath(){
		return p.getProperty("ppiFilepath");
	}
	
	public String getDiseaseSimilarityFilepath(){
		return p.getProperty("diseaseSimilarityFilepath");
	}
	
	public String getGeneDiseaseAssociationFilepath(){
		return p.getProperty("geneDiseaseAssociationFilepath");
	}
	
	public String getDiseaseFilepath(){
		return p.getProperty("diseaseFilepath");
	}
	
	
	public String getHprdIdMappingsFileName(){
		return p.getProperty("hprd_id_mappings");
	}
	
	
	/**
	 * alpha²ÎÊý
	 * @return
	 */
	public String[] getAthreshholdArray(){
		//System.out.println("alpha_array = " + Arrays.toString(p.getProperty("a_threshhold_array").split(",")));
		return p.getProperty("alpha_array").split(",");
	}
	
	public String getOutputDir(){
		File outputDir = new File(p.getProperty("outputDir"));
		if(!outputDir.exists()){
			outputDir.mkdirs();
		}
		return p.getProperty("outputDir");
	}
}