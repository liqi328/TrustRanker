package result;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import util.FileUtil;

public class PrinceResultStatistic {
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
		if(args.length != 1){
			System.out.println("Argument Error.");
			System.out.println("Using method: java -jar TrustRanker_Prioritization.jar ./output");
			System.exit(-1);
		}
		
		PrinceResultPreProcess.run_one(args[0]);
		run_myprince(args[0]);
		//run_pagerankprios(args[0]);
		
		//run_myprince("E:/2013疾病研究/实验数据/SP_TrustRanker比较/TRer_output");

		//batch_run();
		//select();
	}
	public static void batch_run(){
		for(String dirName: dirNameArray){
			run_pagerankprios(dirName);
			run_myprince(dirName);
		}
	}
	
	public static void run(){
		//String dirName = "E:/2013疾病研究/实验数据/TrustRanker/omim_disease/output";
		String dirName = "E:/2013疾病研究/实验数据/SP_TrustRanker比较/TRer_output";
		File[] dirs = FileUtil.getDirectoryList(dirName);
		
		Map<String, StatisticResult> resultMap = new LinkedHashMap<String, StatisticResult>();
		
		AbstractStatistic statisticStrategy = new PrinceStatistic(dirs);
		StatisticResult result = statisticStrategy.run();
		resultMap.put("Prince", result);
		
		StatisticResultAnalysis.writeStatisticResultMap(dirName + File.separator + "statistic.txt", resultMap);
		
		StatisticResultAnalysis.calculateRankCutoff(dirName + File.separator + "TrustRanker_rank_cutoff.txt", resultMap);
	}
	
	public static void run_pagerankprios(String dirName){
		//String dirName = "E:/2013疾病研究/实验数据/Prince/pagerankprios_output";
		File[] dirs = FileUtil.getDirectoryList(dirName);
		
		Map<String, StatisticResult> resultMap = new LinkedHashMap<String, StatisticResult>();
		
		AbstractStatistic statisticStrategy = new PageRankPriosStatistic(dirs);
		StatisticResult result = statisticStrategy.run();
		resultMap.put("PRP", result);
		
		StatisticResultAnalysis.writeStatisticResultMap(dirName + File.separator + "prp_statistic.txt", resultMap);
		
		StatisticResultAnalysis.calculateRankCutoff(dirName + File.separator + "prp_rank_cutoff.txt", resultMap);
	}
	
	public static void run_myprince(String dirName){
		//String dirName = "E:/2013疾病研究/实验数据/Prince/myprince_output";
		//String dirName = "E:/2013疾病研究/gan/diabetes_validation";
		File[] dirs = FileUtil.getDirectoryList(dirName);
		
		Map<String, StatisticResult> resultMap = new LinkedHashMap<String, StatisticResult>();
		
		AbstractStatistic statisticStrategy = new TrustRankerStatistic(dirs);
		
		String[] alphaArray = new String[]{"1.0", "0.9", "0.8", "0.7", "0.6", 
				"0.5", "0.4", "0.3", "0.2", "0.1", "0.0"};
		
		for(String alpha : alphaArray){
			statisticStrategy.setAthreshhold(alpha);
			StatisticResult result = statisticStrategy.run();
			resultMap.put("TrustRanker_" + alpha, result);
		}
		
		StatisticResultAnalysis.writeStatisticResultMap(dirName + File.separator + "TrustRanker_statistic.txt", resultMap);
		
		StatisticResultAnalysis.calculateRankCutoff(dirName + File.separator + "TrustRanker_rank_cutoff.txt", resultMap);
	}
	
	private static void select(){
		String filename = "E:/2013疾病研究/实验数据/Prince/1.txt";
		String outFilename = "E:/2013疾病研究/实验数据/Prince/out.txt";
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			BufferedWriter out = new BufferedWriter(new FileWriter(outFilename));
			String line = in.readLine();
			out.write(line + "\n");
			int k = 97, i =0;
			int total = 9673;
			while((line = in.readLine()) != null){
				++i;
				if(i == k || i >= total){
					k += 97;
					out.write(line +"\n");
				}
			}
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
