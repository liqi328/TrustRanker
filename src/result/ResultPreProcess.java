package result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.FileUtil;
import util.WriterUtil;

public class ResultPreProcess {
	private static String dirName = "E:/2013疾病研究/实验数据/TrustRanker/output";
	
	public static void main(String[] args){
		File[] diseasesDir = FileUtil.getDirectoryList(dirName);
		try {
			for(File d : diseasesDir){
				processOneDisease(d);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(diseasesDir.length);
	}
	
	private static void processOneDisease(File dir) throws IOException{
		//System.out.println(dir.getCanonicalPath());
		File[] targetDirArr = FileUtil.getDirectoryList(dir.getAbsolutePath());
		//System.out.println(targetDirArr.length);
		
		StringBuffer trustRanksInversePriorRanksBuffer = new StringBuffer();
		StringBuffer trustRanksInverseAvageRanksBuffer = new StringBuffer();
		StringBuffer trustRanksHighPriorRanksBuffer = new StringBuffer();
		StringBuffer trustRanksHighAvageRanksBuffer = new StringBuffer();
		StringBuffer pageRankPriorRanksBuffer = new StringBuffer();
		StringBuffer pageRankAvageRanksBuffer = new StringBuffer();
		
		for(File d: targetDirArr){
			trustRanksInversePriorRanksBuffer.append(processOneTargetGeneValidationFile(d, "trustrank_inverse_priors_ranks.txt"));
			trustRanksInverseAvageRanksBuffer.append(processOneTargetGeneValidationFile(d, "trustrank_inverse_avage_ranks.txt"));
			trustRanksHighPriorRanksBuffer.append(processOneTargetGeneValidationFile(d, "trustrank_high_priors_ranks.txt"));
			trustRanksHighAvageRanksBuffer.append(processOneTargetGeneValidationFile(d, "trustrank_high_avage_ranks.txt"));
			pageRankPriorRanksBuffer.append(processOneTargetGeneValidationFile(d, "pagerank_priors_ranks.txt"));
			pageRankAvageRanksBuffer.append(processOneTargetGeneValidationFile(d, "pagerank_avg_ranks.txt"));
		}
		
		WriterUtil.write(dir.getAbsolutePath() + "/trustRanksInversePrior_validation.txt", trustRanksInversePriorRanksBuffer.toString());
		WriterUtil.write(dir.getAbsolutePath() + "/trustRanksInverseAvage_validation.txt", trustRanksInverseAvageRanksBuffer.toString());
		WriterUtil.write(dir.getAbsolutePath() + "/trustRanksHighPrior_validation.txt", trustRanksHighPriorRanksBuffer.toString());
		WriterUtil.write(dir.getAbsolutePath() + "/trustRanksHighAvage_validation.txt", trustRanksHighAvageRanksBuffer.toString());
		WriterUtil.write(dir.getAbsolutePath() + "/pageRankPrior_validation.txt", pageRankPriorRanksBuffer.toString());
		WriterUtil.write(dir.getAbsolutePath() + "/pageRankAvage_validation.txt", pageRankAvageRanksBuffer.toString());
	}
	
	private static String processOneTargetGeneValidationFile(File dir, String filename){
		StringBuffer rankStr = new StringBuffer(dir.getName());
		File rankFile = new File(dir.getAbsolutePath() + "/" + filename);
		//System.out.println(rankFile.getAbsolutePath() + rankFile.exists());
		
		List<String> rankList = readRankFile(rankFile, 100);
		
		for(int i = 0; i <rankList.size(); ++i){
			if(rankList.get(i).split("\t")[0].equals(dir.getName())){
				rankStr.append("\t").append(i + 1).append("\t").append(rankList.get(i).split("\t")[1]).append("\n");
				break;
			}
		}
		//若该target不在前100,怎么办 Undo
		
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
}
