package diseasefamily;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DiseaseFamilyReader {
	public static Map<String, DiseaseFamily> read(){
		String diseaseFilePath = "E:/2013疾病研究/疾病数据/OMIM/110DiseaseFamily/110diseaseFamily.txt";
		
		return read(diseaseFilePath);
	}
	
	public static Map<String, DiseaseFamily> read(String diseaseFamilyFilepath){
		Map<String, DiseaseFamily> diseaseFamilyMap = new HashMap<String, DiseaseFamily>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(diseaseFamilyFilepath)));
			String line = null;
			String[] cols = null;
			int i = 0;
			String description = null;
			DiseaseFamily family = null;
			while((line = in.readLine()) != null){
				if(line.startsWith("[")){
					//System.out.println(++i + ". " + line.split("]")[1].trim());
					description = in.readLine();
					
					family = new DiseaseFamily(line.split("]")[1].trim(), description);
					diseaseFamilyMap.put(family.getName(), family);
					
					in.readLine();
				}else{
					cols = line.split("\t");
					Gene gene = new Gene(cols[4], cols[0], cols[1]);
					Disorder disorder = new Disorder(cols[3], cols[2]);
					family.addGene2DisorderAssociation(gene, disorder);
					
					if(cols.length < 5){
						System.out.println(++i + ". " + line);
					}
				}
				
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return diseaseFamilyMap;
	}
	
	public static void main(String[] args){
		read();
	}
}
