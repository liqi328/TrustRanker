package result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import prince.InputArgument;
import util.FileUtil;
import util.WriterUtil;
import diseasefamily.Disorder;
import diseasefamily.Gene;
import diseasefamily.GeneDiseaseAssociation;
import diseasefamily.HprdIdMapping;
import diseasefamily.HprdIdMappingUtil;

public class PrioritizationResult {
	//private static String dirName = "E:/2013疾病研究/实验数据/Prince/myprince_prioritizing";
	//private static String dirName = "E:/2013疾病研究/gan/diabetes_validation";
	//private static Map<String, HprdIdMapping> hprdIdIndexedIdMappingMap = HprdIdMappingUtil.getHprdIdIndexIdMapping();
	private static Map<String, HprdIdMapping> hprdIdIndexedIdMappingMap;
	
	private static void initHprdIdMapping(String hprdIdMapppingFilepath){
		HprdIdMappingUtil.setHprdIdMappingFilePath(hprdIdMapppingFilepath);
		hprdIdIndexedIdMappingMap = HprdIdMappingUtil.getHprdIdIndexIdMapping();
	}
	
	public static void run(String dirName,String hprdIdMapppingFilepath, String geneDiseaseAssociationFilepath){
		initHprdIdMapping(hprdIdMapppingFilepath);
		
		GeneDiseaseAssociation associations = new GeneDiseaseAssociation(hprdIdMapppingFilepath);
		associations.read(geneDiseaseAssociationFilepath);
		
		File[] diseasesDir = FileUtil.getDirectoryList(dirName);
		try {
			StringBuffer allRanksBuffer = new StringBuffer();
			                                                                                          
			for(File d : diseasesDir){
				allRanksBuffer.append(d.getName()).append("\t");
				allRanksBuffer.append(associations.disorderMap.get(d.getName()).getName()).append("\t");
				Disorder disorder = associations.disorderMap.get(d.getName());
				allRanksBuffer.append(disorder.geneMap.size()).append("\n");
				allRanksBuffer.append("Known disease-causing genes: ");
				for(Gene gene: disorder.geneMap.values()){
					allRanksBuffer.append(hprdIdIndexedIdMappingMap.get(gene.getHprdId()).getGeneSymbol()).append(",");
				}
				allRanksBuffer.append("\n\n");
				
				allRanksBuffer.append(processOneDisease(d));
				
				allRanksBuffer.append("\n--------------------------------------------------\n");
			}
			
			WriterUtil.write(dirName + "/TrustRanker_top200.txt", allRanksBuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(diseasesDir.length);
	}
	
	private static String processOneDisease(File dir) throws IOException{
		String[] alphaArray = new String[]{"0.0", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9","1.0"};
		StringBuffer ranksBuffer = new StringBuffer();
		
		for(String alpha: alphaArray){
			ranksBuffer.append(alpha).append("\t");
			ranksBuffer.append(readTopN(dir.getAbsolutePath()+"/TRer_ranks[p]_" + alpha + ".txt", 200));
			ranksBuffer.append("\n");
		}
		//WriterUtil.write(dir.getAbsolutePath() + "/all_top200.txt", ranksBuffer.toString());
		
		return ranksBuffer.toString();
	}
	
	private static String readTopN(String filename, int topN){
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String line = null;
			while((line = in.readLine()) != null && topN > 0){
				sb.append(hprdIdIndexedIdMappingMap.get(line.split("\t")[0]).getGeneSymbol()).append(",");
				topN--;
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static void main(String[] args){
		if(args.length != 1){
			System.out.println("Argument Error.");
			System.out.println("Using method: java -jar TrustRanker_TopPrediction.jar ./TrustRanker_config.txt");
			System.exit(-1);
		}
		InputArgument input = new InputArgument(args[0]);
		run(input.getOutputDir(), input.getHprdIdMappingsFileName(), input.getGeneDiseaseAssociationFilepath());
	}
}
