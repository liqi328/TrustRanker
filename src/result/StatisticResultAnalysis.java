package result;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import util.WriterUtil;

public class StatisticResultAnalysis {
	/**
	 * 计算比例
	 * @param filename
	 * @param resultMap
	 */
	public static void calculateRankCutoff2(String filename, Map<String, StatisticResult> resultMap){
		StringBuffer sb = new StringBuffer();
		sb.append("Rank");
		
		int totalValidation = 0;
		Map<String, Integer> enrichmentMap = new LinkedHashMap<String, Integer>();
		Iterator<String> keyItr = resultMap.keySet().iterator();
		while(keyItr.hasNext()){
			String key = keyItr.next();
			sb.append("\t").append(key);
			
			if(resultMap.get(key).totalValidation > totalValidation){
				totalValidation = resultMap.get(key).totalValidation;
			}
			
			enrichmentMap.put(key, 0);
		}
		sb.append("\n");
		
		NumberFormat format = new DecimalFormat("0.00");
		StatisticResult result = null;
		String key = null;
		for(int i = 0; i < StatisticResult.RANK; ++i){
			sb.append((i+1));
			Iterator<Entry<String, StatisticResult>> itr = resultMap.entrySet().iterator();
			Entry<String, StatisticResult> entry = null;
			while(itr.hasNext()){
				entry = itr.next();
				key = entry.getKey();
				result = entry.getValue();
				sb.append("\t" + format.format(((enrichmentMap.get(key)+ result.rankArray[i]) / (totalValidation + 0.0))));
				
				enrichmentMap.put(entry.getKey(), enrichmentMap.get(entry.getKey()) + result.rankArray[i]);
			}
			sb.append("\n");
		}
		
		WriterUtil.write(filename, sb.toString());
	}
	
	/**
	 * 计算累加值
	 * @param filename
	 * @param resultMap
	 */
	public static void calculateRankCutoff(String filename, Map<String, StatisticResult> resultMap){
		StringBuffer sb = new StringBuffer();
		sb.append("Rank");
		
		int totalValidation = 0;
		Map<String, Integer> enrichmentMap = new LinkedHashMap<String, Integer>();
		Iterator<String> keyItr = resultMap.keySet().iterator();
		while(keyItr.hasNext()){
			String key = keyItr.next();
			sb.append("\t").append(key);
			
			if(resultMap.get(key).totalValidation > totalValidation){
				totalValidation = resultMap.get(key).totalValidation;
			}
			
			enrichmentMap.put(key, 0);
		}
		sb.append("\n");
		
		NumberFormat format = new DecimalFormat("0.00");
		StatisticResult result = null;
		String key = null;
		for(int i = 0; i < StatisticResult.RANK; ++i){
			sb.append((i+1));
			Iterator<Entry<String, StatisticResult>> itr = resultMap.entrySet().iterator();
			Entry<String, StatisticResult> entry = null;
			while(itr.hasNext()){
				entry = itr.next();
				key = entry.getKey();
				result = entry.getValue();
				sb.append("\t" + (enrichmentMap.get(key)+ result.rankArray[i]));
				
				enrichmentMap.put(entry.getKey(), enrichmentMap.get(entry.getKey()) + result.rankArray[i]);
			}
			sb.append("\n");
		}
		
		WriterUtil.write(filename, sb.toString());
	}
	
	
	public static void writeStatisticResultMap(String filename, Map<String, StatisticResult> resultMap){
		StringBuffer sb = new StringBuffer();
		sb.append("Rank");
		
		Map<String, Integer> enrichmentMap = new LinkedHashMap<String, Integer>();
		Iterator<String> keyItr = resultMap.keySet().iterator();
		while(keyItr.hasNext()){
			String key = keyItr.next();
			sb.append("\t").append(key);
			
			enrichmentMap.put(key, 0);
		}
		sb.append("\n");
		
		StatisticResult result = null;
		for(int i = 0; i < StatisticResult.RANK; ++i){
			sb.append((i+1));
			Iterator<Entry<String, StatisticResult>> itr = resultMap.entrySet().iterator();
			Entry<String, StatisticResult> entry = null;
			while(itr.hasNext()){
				entry = itr.next();
				result = entry.getValue();
				sb.append("\t" + result.rankArray[i]);
				
				enrichmentMap.put(entry.getKey(), enrichmentMap.get(entry.getKey()) + (i+1) * result.rankArray[i]);
			}
			sb.append("\n");
		}
		
		sb.append("Enrichment:");
		for(Integer i : enrichmentMap.values()){
			sb.append("\t"+ i);
		}
		sb.append("\n");
		sb.append("total validation = " + result.totalValidation).append("\n");
		
		WriterUtil.write(filename, sb.toString());
	}
}
