package result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.FileUtil;
import util.WriterUtil;

public class PrinceResultPreProcess {
private static String dirName = "E:/2013疾病研究/实验数据/TrustRanker/omim_disease/output";
	
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
		
		StringBuffer princeRanksBuffer = new StringBuffer();

		
		for(File d: targetDirArr){
			princeRanksBuffer.append(processOneTargetGeneValidationFile(d, "prince_ranks.txt"));
		}
		
		WriterUtil.write(dir.getAbsolutePath() + "/prince_validation.txt", princeRanksBuffer.toString());
	}
	
	private static String processOneTargetGeneValidationFile(File dir, String filename){
		StringBuffer rankStr = new StringBuffer(dir.getName());
		File rankFile = new File(dir.getAbsolutePath() + "/" + filename);
		//System.out.println(rankFile.getAbsolutePath() + rankFile.exists());
		
		List<String> rankList = readRankFile(rankFile, 100);
		
		boolean flag = false;
		for(int i = 0; i <rankList.size(); ++i){
			if(rankList.get(i).split("\t")[0].equals(dir.getName())){
				rankStr.append("\t").append(i + 1).append("\t").append(rankList.get(i).split("\t")[1]).append("\n");
				flag = true;
				break;
			}
		}
		//若该target不在前100,怎么办 Undo
		if(!flag){
			rankStr.append("\t").append("101").append("\t0.0\n");
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
}
