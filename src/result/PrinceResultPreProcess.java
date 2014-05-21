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
	//private static String dirName = "E:/2013疾病研究/实验数据/TrustRanker/omim_disease/output";
	//private static String dirName = "E:/2013疾病研究/实验数据/Prince/myprince_output";
	//private static String dirName = "E:/2013疾病研究/实验数据/Prince/pagerankprios_output";
	//private static String dirName = "E:/2013疾病研究/gan/diabetes_validation";
	private static String dirName = "E:/2013疾病研究/实验数据/SP_TrustRanker比较/TRer_output";
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
	
	public static void main(String[] args){
		//batch_run();
		run_one();
	}
	
	private static void run_one(){
		File[] diseasesDir = FileUtil.getDirectoryList(dirName);
		try {
			for(File d : diseasesDir){
				processOneDisease_TRer(d);
				//processOneDisease_PRP(d);
				//testcomplete(d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(diseasesDir.length);
	}
	
	public static void batch_run(){
		for(String dirName: dirNameArray){
			File[] diseasesDir = FileUtil.getDirectoryList(dirName);
			try {
				for(File d : diseasesDir){
					processOneDisease_TRer(d);
					processOneDisease_PRP(d);
					//testcomplete(d);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//System.out.println(dirName+ "\t" + diseasesDir.length);
		}
	}
	
	private static void testcomplete(File dir){
		File[] targetDirArr = FileUtil.getDirectoryList(dir.getAbsolutePath());
		
		for(File file: targetDirArr){
			File[] f = FileUtil.getFileList(file.getAbsolutePath());
			if(f.length < 12){
				System.out.println(dir+ "\t" + file.getName() + ", " + f.length);
				//return;
			}
		}
	}
	
	private static void processOneDisease_TRer(File dir) throws IOException{
		//System.out.println(dir.getCanonicalPath());
		File[] targetDirArr = FileUtil.getDirectoryList(dir.getAbsolutePath());
		//System.out.println(targetDirArr.length);
		
		String[] alphaArray = new String[]{"0.0", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9","1.0"};
		
		for(String alpha: alphaArray){
			StringBuffer princeRanksBuffer = new StringBuffer();
			for(File d: targetDirArr){
				princeRanksBuffer.append(processOneTargetGeneValidationFile(d, "TRer_ranks[v]_" + alpha + ".txt"));
			}
			WriterUtil.write(dir.getAbsolutePath() + "/TRer_validation_" + alpha + ".txt", princeRanksBuffer.toString());
		}
	}
	
	private static void processOneDisease_PRP(File dir) throws IOException{
		//System.out.println(dir.getCanonicalPath());
		File[] targetDirArr = FileUtil.getDirectoryList(dir.getAbsolutePath());
		//System.out.println(targetDirArr.length);
		
		StringBuffer princeRanksBuffer = new StringBuffer();

		for(File d: targetDirArr){
			princeRanksBuffer.append(processOneTargetGeneValidationFile(d, "PRP_ranks[v].txt"));
		}
		
		WriterUtil.write(dir.getAbsolutePath() + "/PRP_validation.txt", princeRanksBuffer.toString());
	}
	
	private static String processOneTargetGeneValidationFile(File dir, String filename){
		StringBuffer rankStr = new StringBuffer(dir.getName());
		File rankFile = new File(dir.getAbsolutePath() + "/" + filename);
		//System.out.println(rankFile.getAbsolutePath() + rankFile.exists());
		
		if(!rankFile.exists())return null;
		
		List<String> rankList = readRankFile(rankFile, 9673);
		
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
}
