package result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.FileUtil;
import util.WriterUtil;

public class TRerReustlRandom99 {
//	private static String spgorankerOutputDir = "E:/2013疾病研究/实验数据/prioritizing_candidate_gene/orphanet_experiment/VS_SP_ICN/交叉验证/output_hprd0726/";
//	private static String trerOutputDir = "E:/2013疾病研究/实验数据/SP_TrustRanker比较/TRer_output";
	
	private static String spgorankerOutputDir = "E:/2013疾病研究/实验数据/SP_TrustRanker比较/output/";
	private static String trerOutputDir = "E:/2013疾病研究/实验数据/Prince/myprince_output/";
	public static void main(String[] args){
		run_one();
	}
	
	private static void run_one(){
		Set<String> diseaseOmimIdSet = getDiseaseOmimIdSet();
		
		List<File> diseaseList = new ArrayList<File>();
		File[] diseasesDir = FileUtil.getDirectoryList(trerOutputDir);
		try {
			for(File d : diseasesDir){
				if(diseaseOmimIdSet.contains(d.getName())){
					//processOneDisease_TRer(d);
					diseaseList.add(d);
					//System.out.println(d.getName());
				}else{
					//System.out.println("-->"+d.getName());
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Disease number: " + diseasesDir.length);
		
		
		System.out.println("Validation Disease number:" + diseaseList.size());
		run_myprince(diseaseList.toArray(new File[]{}));
	}
	
	public static void run_myprince(File[] dirs){
		//File[] dirs = FileUtil.getDirectoryList(trerOutputDir);
		
		Map<String, StatisticResult> resultMap = new LinkedHashMap<String, StatisticResult>();
		
		AbstractStatistic statisticStrategy = new TrustRankerStatistic(dirs);
		
		String[] alphaArray = new String[]{"1.0", "0.9", "0.8", "0.7", "0.6", 
				"0.5", "0.4", "0.3", "0.2", "0.1", "0.0"};
		
		for(String alpha : alphaArray){
			statisticStrategy.setAthreshhold(alpha);
			StatisticResult result = statisticStrategy.run();
			resultMap.put("TRer_" + alpha, result);
			//break;
		}
		
		StatisticResultAnalysis.writeStatisticResultMap(trerOutputDir + File.separator + "statistic_omim.txt", resultMap);
		
		StatisticResultAnalysis.calculateRankCutoff(trerOutputDir + File.separator + "rank_cutoff_omim.txt", resultMap);
	}
	
	
	private static Set<String> getDiseaseOmimIdSet(){
		Set<String> diseaseOmimIdSet = new HashSet<String>();
		File[] diseasesDir = FileUtil.getDirectoryList(spgorankerOutputDir);
		for(File d: diseasesDir){
			//diseaseOmimIdSet.add(d.getName().split("_")[0]);
			diseaseOmimIdSet.add(d.getName());
			//System.out.println("==>"+d.getName().split("_")[0]);
			//System.out.println("==>"+d.getName());
		}
		
		return diseaseOmimIdSet;
	}
	
	private static void processOneDisease_TRer(File dir) throws IOException{
		File[] targetDirArr = FileUtil.getDirectoryList(dir.getAbsolutePath());
		
		String[] alphaArray = new String[]{"0.0", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9","1.0"};
		
		for(String alpha: alphaArray){
			StringBuffer princeRanksBuffer = new StringBuffer();
			for(File d: targetDirArr){
				princeRanksBuffer.append(processOneTargetGeneValidationFile(d, "myprince_ranks_" + alpha + ".txt", dir.getName()));
			}
			WriterUtil.write(dir.getAbsolutePath() + "/myprince_validation_" + alpha + ".txt", princeRanksBuffer.toString());
		}
	}
	
	
	private static String processOneTargetGeneValidationFile(File dir, String filename, String diseaseOmimId){
		StringBuffer rankStr = new StringBuffer(dir.getName());
		File rankFile = new File(dir.getAbsolutePath() + "/" + filename);
		//System.out.println(rankFile.getAbsolutePath() + rankFile.exists());
		
		if(!rankFile.exists())return null;
		
		List<String> rankList = readRankFile(rankFile, 9673);
		Set<String> randomGeneSet = readRandomCandidateGene(diseaseOmimId);
		
		boolean flag = false;
		int rank = 0;
		for(int i = 0; i <rankList.size(); ++i){
			if(randomGeneSet.contains(rankList.get(i).split("\t")[0])){
				++rank;
			}else if(rankList.get(i).split("\t")[0].equals(dir.getName())){
				rankStr.append("\t").append(rank+1).append("\t").append(rankList.get(i).split("\t")[1]).append("\n");
				flag = true;
				break;
			}
		}
		//若该target不在前100,怎么办 Undo
		if(!flag){
			//rankStr.append("\t").append("101").append("\t0.0\n");
			System.out.println(rankFile.getAbsolutePath()+ "\tError rank: " + dir.getName());
		}
		return rankStr.toString();
	}
	
	private static List<String> readRankFile(File rankFile, int topN){
		List<String> rankList = new ArrayList<String>();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(rankFile));
			String line = null;
			while((line = in.readLine()) != null && topN > 0){
				rankList.add(line);
				topN--;
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return rankList;
	}
	
	private static Set<String> readRandomCandidateGene(String diseaseOmimId){
		Set<String> geneSet = new HashSet<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(spgorankerOutputDir + diseaseOmimId + "/random_candidate_gene.txt"));
			String line = null;
			while((line = in.readLine()) != null){
				geneSet.add(line);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return geneSet;
	}
}
