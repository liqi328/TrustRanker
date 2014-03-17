package result;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import util.FileUtil;

public class PrinceResultStatistic {
	public static void main(String[] args){
		run();
	}
	
	public static void run(){
		String dirName = "E:/2013疾病研究/实验数据/TrustRanker/omim_disease/output";
		
		File[] dirs = FileUtil.getDirectoryList(dirName);
		
		Map<String, StatisticResult> resultMap = new LinkedHashMap<String, StatisticResult>();
		
		AbstractStatistic statisticStrategy = new PrinceStatistic(dirs);
		StatisticResult result = statisticStrategy.run();
		resultMap.put("Prince", result);
		
		StatisticResultAnalysis.writeStatisticResultMap(dirName + File.separator + "statistic.txt", resultMap);
		
		StatisticResultAnalysis.calculateRankCutoff(dirName + File.separator + "rank_cutoff.txt", resultMap);
	}
}
