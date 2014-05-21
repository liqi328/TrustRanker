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
import java.util.Random;
import java.util.Set;

import util.FileUtil;
import util.WriterUtil;

public class TrustRankerReustlRandom99 {
	private static String trerOutputDir = "E:/2013疾病研究/实验数据/Prince/myprince_output/";
	private static String prpOutputDir = "E:/2013疾病研究/实验数据/Prince/pagerankprios_output/";
	
	public static void main(String[] args){
		process_trustraner();
		process_prp();
	}
	
	
	private static void process_prp(){
		File[] diseasesDir = FileUtil.getDirectoryList(prpOutputDir);
//		try {
//			for(File d : diseasesDir){
//				processOneDisease_PRP(d);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		System.out.println("Disease number: " + diseasesDir.length);
		
		run_pagerankprios(diseasesDir);
	}
	private static void process_trustraner(){
		File[] diseasesDir = FileUtil.getDirectoryList(trerOutputDir);
//		try {
//			for(File d : diseasesDir){
//				processOneDisease_TRer(d);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		System.out.println("Disease number: " + diseasesDir.length);
		
		run_myprince(diseasesDir);
	}
	
	public static void run_myprince(File[] dirs){
		//File[] dirs = FileUtil.getDirectoryList(trerOutputDir);
		
		Map<String, StatisticResult> resultMap = new LinkedHashMap<String, StatisticResult>();
		
		AbstractStatistic statisticStrategy = new MyPrinceStatistic(dirs);
		
		String[] alphaArray = new String[]{"1.0", "0.9", "0.8", "0.7", "0.6", 
				"0.5", "0.4", "0.3", "0.2", "0.1", "0.0"};
		
		for(String alpha : alphaArray){
			statisticStrategy.setAthreshhold(alpha);
			StatisticResult result = statisticStrategy.run();
			resultMap.put("TRer_" + alpha, result);
		}
		
		StatisticResultAnalysis.writeStatisticResultMap(trerOutputDir + File.separator + "statistic_TRer0514.txt", resultMap);
		
		StatisticResultAnalysis.calculateRankCutoff(trerOutputDir + File.separator + "rank_cutoff_TRer0514.txt", resultMap);
	}
	
	
	public static void run_pagerankprios(File[] dirs){
		
		Map<String, StatisticResult> resultMap = new LinkedHashMap<String, StatisticResult>();
		
		AbstractStatistic statisticStrategy = new PageRankPriosStatistic(dirs);
		StatisticResult result = statisticStrategy.run();
		resultMap.put("PRP", result);
		
		StatisticResultAnalysis.writeStatisticResultMap(prpOutputDir + File.separator + "statistic_prp0514.txt", resultMap);
		
		StatisticResultAnalysis.calculateRankCutoff(prpOutputDir + File.separator + "rank_cutoff_prp0514.txt", resultMap);
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
	
	private static void processOneDisease_PRP(File dir) throws IOException{
		//System.out.println(dir.getCanonicalPath());
		File[] targetDirArr = FileUtil.getDirectoryList(dir.getAbsolutePath());
		//System.out.println(targetDirArr.length);
		
		StringBuffer princeRanksBuffer = new StringBuffer();

		for(File d: targetDirArr){
			princeRanksBuffer.append(processOneTargetGeneValidationFile(d, "pagerankprios_ranks.txt", dir.getName()));
		}
		
		WriterUtil.write(dir.getAbsolutePath() + "/pagerankprios_validation.txt", princeRanksBuffer.toString());
//		
//		StringBuffer princeRanksBuffer2 = new StringBuffer();
//
//		for(File d: targetDirArr){
//			princeRanksBuffer2.append(processOneTargetGeneValidationFile(d, "pagerankprios2.txt", dir.getName()));
//		}
//		
//		WriterUtil.write(dir.getAbsolutePath() + "/pagerankprios2_validation.txt", princeRanksBuffer2.toString());
	}
	
	private static String processOneTargetGeneValidationFile(File dir, String filename, String diseaseOmimId){
		StringBuffer rankStr = new StringBuffer(dir.getName());
		File rankFile = new File(dir.getAbsolutePath() + "/" + filename);
		//System.out.println(rankFile.getAbsolutePath() + rankFile.exists());
		
		if(!rankFile.exists())return null;
		
		List<String> rankList = readRankFile(rankFile, 9673);
		if(rankList.size() <=0) return null;
		Set<String> randomGeneSet = readRandomCandidateGene(dir.getName(), rankList);
		
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
	
	private static Set<String> readRandomCandidateGene(String geneOmimId, List<String> rankList){
		Set<String> randomGeneSet = new HashSet<String>();
		int nodeNum = rankList.size();
		int index = 0;
		Random rnd = new Random();
		for(int i = 0; i < 99;){
			index = rnd.nextInt(nodeNum);
			String omimId = rankList.get(index).split("\t")[0];
			if(geneOmimId.equalsIgnoreCase(omimId) || randomGeneSet.contains(omimId)){
				continue;
			}
			randomGeneSet.add(omimId);
			++i;
		}
		return randomGeneSet;
	}
}
