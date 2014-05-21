package result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import util.FileUtil;
import util.WriterUtil;
import diseasefamily.Disorder;
import diseasefamily.Gene;
import diseasefamily.GeneDiseaseAssociation;
import diseasefamily.HprdIdMapping;
import diseasefamily.HprdIdMappingUtil;

public class PrioritizingResult_20140508 {
	//private static String dirName = "E:/2013疾病研究/实验数据/Prince/myprince_prioritizing";
	private static String dirName = "E:/2013疾病研究/gan/diabetes_validation";
	private static Map<String, HprdIdMapping> entrezIdIndexedIdMappingMap = HprdIdMappingUtil.getEntrezIdIndexedIdMapping();
	private static String[] dirNameArray = {
		"E:/2013疾病研究/gan/output/diabetes_validation",
		"E:/2013疾病研究/gan/output/skinprostate_validation",
		"E:/2013疾病研究/gan/output/prosprostate_validation",
		"E:/2013疾病研究/gan/output/lung_validation",
		"E:/2013疾病研究/gan/output/alzheimer_validation",
		"E:/2013疾病研究/gan/output/skinbreast_validation",
		"E:/2013疾病研究/gan/output/ovarybreast_validation",
		"E:/2013疾病研究/gan/output/prosbreast_validation",
		"E:/2013疾病研究/gan/output/livercolorectal_validation",
		"E:/2013疾病研究/gan/output/ovarycolorectal_validation",
		"E:/2013疾病研究/gan/output/skinleukemia_validation",
		"E:/2013疾病研究/gan/output/bcellleukemia_validation",
	};
	
	public static void run(String dirName2){
		GeneDiseaseAssociation associations = new GeneDiseaseAssociation();
		associations.read("E:/2013疾病研究/疾病数据/OMIM/geneEntrezId_diseaseOmimId.txt");
		
		File[] diseasesDir = FileUtil.getDirectoryList(dirName2);
		try {
			StringBuffer allRanksBuffer = new StringBuffer();
			                                                                                          
			for(File d : diseasesDir){
				allRanksBuffer.append(d.getName()).append("\t");
				allRanksBuffer.append(associations.disorderMap.get(d.getName()).getName()).append("\t");
				Disorder disorder = associations.disorderMap.get(d.getName());
				allRanksBuffer.append(disorder.geneMap.size()).append("\n");
				allRanksBuffer.append("Known disease-causing genes: ");
				for(Gene gene: disorder.geneMap.values()){
					allRanksBuffer.append(entrezIdIndexedIdMappingMap.get(gene.getGeneNameInPPI()).getGeneSymbol()).append(",");
				}
				allRanksBuffer.append("\n\n");
				
				allRanksBuffer.append(processOneDisease_TRer(d, disorder));
				allRanksBuffer.append("\n");
				allRanksBuffer.append(processOneDisease_PRP(d, disorder));
				
				allRanksBuffer.append("\n--------------------------------------------------\n");
			}
			
			WriterUtil.write(dirName2 + "/all_top10.txt", allRanksBuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(diseasesDir.length);
	}
	
	private static String processOneDisease_TRer(File dir, Disorder disorder) throws IOException{
		String[] alphaArray = new String[]{"0.0", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9","1.0"};
		StringBuffer ranksBuffer = new StringBuffer();
		
		for(String alpha: alphaArray){
			ranksBuffer.append("TRer[" + alpha + "]").append("\t");
			ranksBuffer.append(readTopN(dir.getAbsolutePath()+"/TRer_ranks[p]_" + alpha + ".txt", 200));
			ranksBuffer.append("\n\t").append(getKnownDiseaseCausingGeneRanks(disorder, dir.getAbsolutePath()+"/TRer_ranks[p]_" + alpha + ".txt"));
			ranksBuffer.append("\n");
		}
		WriterUtil.write(dir.getAbsolutePath() + "/TRer_top10.txt", ranksBuffer.toString());
		
		return ranksBuffer.toString();
	}
	
	private static String processOneDisease_PRP(File dir, Disorder disorder){
		StringBuffer ranksBuffer = new StringBuffer();
		
		ranksBuffer.append("PRP").append("\t");
		ranksBuffer.append(readTopN(dir.getAbsolutePath()+"/PRP_ranks[p].txt", 200));
		ranksBuffer.append("\n\t").append(getKnownDiseaseCausingGeneRanks(disorder, dir.getAbsolutePath()+"/PRP_ranks[p].txt"));
		ranksBuffer.append("\n");
		WriterUtil.write(dir.getAbsolutePath() + "/prp_top10.txt", ranksBuffer.toString());
		
		return ranksBuffer.toString();
	}
	
	private static String getKnownDiseaseCausingGeneRanks(Disorder disorder, String filename){
		StringBuffer sb = new StringBuffer();
		Set<String> knownDiseaseGeneSet = new HashSet<String>();
		knownDiseaseGeneSet.addAll(disorder.geneMap.keySet());
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String line = null;
			HprdIdMapping hprdIDMapping = null;
			int i = 0;
			while((line = in.readLine()) != null){
				++i;
				if(knownDiseaseGeneSet.contains(line.split("\t")[0])){
					hprdIDMapping = entrezIdIndexedIdMappingMap.get(line.split("\t")[0]);
					if(hprdIDMapping == null){
						sb.append(line.split("\t")[0]).append("[" + i + "]");
					}else{
						sb.append(entrezIdIndexedIdMappingMap.get(line.split("\t")[0]).getGeneSymbol()).append("[" + i + "],");
					}
				}
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	private static String readTopN(String filename, int topN){
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String line = null;
			HprdIdMapping hprdIDMapping = null;
			while((line = in.readLine()) != null && topN > 0){
				//System.out.println(entrezIdIndexedIdMappingMap.get(line.split("\t")[0]) + "\t"+line.split("\t")[0]);
				hprdIDMapping = entrezIdIndexedIdMappingMap.get(line.split("\t")[0]);
				if(hprdIDMapping == null){
					sb.append(line.split("\t")[0]).append(",");
				}else{
					sb.append(entrezIdIndexedIdMappingMap.get(line.split("\t")[0]).getGeneSymbol()).append(",");
				}
				
				topN--;
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static void main(String[] args){
		for(String dirName2: dirNameArray){
			run(dirName2);
		}
		
	}
}
