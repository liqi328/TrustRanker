package result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.FileUtil;
import diseasefamily.HprdIdMapping;
import diseasefamily.HprdIdMappingUtil;

class StatisticResult{
	public static final int RANK = 102;
	public int totalValidation = 0;
	public int[] rankArray = new int[RANK];
}

public abstract class AbstractStatistic{
	protected String a_threshhold = "0.8";
	protected File[] resultDirs;
	
	public AbstractStatistic(File[] resultDirs){
		this.resultDirs = resultDirs;
	}
	
	public void setAthreshhold(String a_threshhold){
		this.a_threshhold = a_threshhold;
	}
	
	public final StatisticResult run(){
		printLogHeader();
		
		StatisticResult result = run_2();
		
		printLogFooter();
		
		return result;
	}
	
	private StatisticResult run_2(){
		StatisticResult result = new StatisticResult();
		int count = 0;
		List<File> retFileList = parseResultFiles();
		for(File file : retFileList){
			System.out.println(file);
			
			List<Rank> rankList = readRankList(file);
			if(!isCanStatistic(rankList)){
				++count;
				continue;
			}
			
			for(Rank rank : rankList){
				result.rankArray[rank.getRank() - 1]++;
				result.totalValidation++;
			}
		}
		System.out.println("< 4 total = " + count);
		
		return result;
	}
	
	private boolean isCanStatistic(List<Rank> rankList){
		if(rankList.size() < 4){
			return false;
		}
		return true;
	}
	
	protected List<Rank> readRankList(File file){
		List<Rank> rankList = new ArrayList<Rank>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line = null;
			Rank rank = null;
			String[] cols = null;
			while((line = in.readLine()) != null){
				cols = line.split("\t");
				rank = new Rank(-1, Double.parseDouble(cols[2]));
				rank.setName(cols[0]);
				rank.setRank(Integer.parseInt(cols[1]));
				rankList.add(rank);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rankList;
	}
	
	protected String readRankingGene(File file, int top_k){
		StringBuffer sb = new StringBuffer();
		
		Map<String, HprdIdMapping> hprdIdIndexedIdMappingMap = HprdIdMappingUtil.getHprdIdIndexIdMapping();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line = null;
			String[] cols = null;
			int i = 0;
			
			HprdIdMapping hprdMapping = null;
			while((line = in.readLine()) != null && i < top_k){
				cols = line.split("\t");
				hprdMapping = hprdIdIndexedIdMappingMap.get(cols[0]);
				sb.append(hprdMapping.getGeneSymbol()).append(",");
				//sb.append(cols[0]).append("] ");
				++i;
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	protected final List<File> parseResultFiles(){
		List<File> retFileList = new ArrayList<File>();
		for(File dir: resultDirs){
			File[] files = FileUtil.getFileList(dir.getPath(), createFileFilter());
			
			for(File file : files){
				retFileList.add(file);
			}
		}
		return retFileList;
	}
	
	protected abstract void printLogHeader();
	protected abstract void printLogFooter(); 
	
	protected abstract FileFilter createFileFilter();
	
	protected abstract FileFilter createFileFilter(String filterString);
	
	
	
	/* 候选基因排序--统计 */
	
	protected abstract String run_ranking_statistic(File dir, int top_k);
}

class ResultFileFilter implements FileFilter{
	private String filterString;
	
	public ResultFileFilter(String filterString){
		this.filterString = filterString;
	}
	
	@Override
	public boolean accept(File pathname) {
		if(pathname.isDirectory()){
			return false;
		}
		if(pathname.getName().startsWith(filterString)){
			return true;
		}
		return false;
	}
}