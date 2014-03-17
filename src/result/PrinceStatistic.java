package result;

import java.io.File;
import java.io.FileFilter;

import util.FileUtil;

public class PrinceStatistic extends AbstractStatistic{
	public PrinceStatistic(File[] resultDirs) {
		super(resultDirs);
	}

	@Override
	protected FileFilter createFileFilter() {
		return new ResultFileFilter("prince_validation");
	}

	@Override
	protected void printLogHeader() {
		System.out.println("--------------- Prince statistic running -------------");
	}

	@Override
	protected void printLogFooter() {
		System.out.println("--------------- Prince statistic finished -------------");
	}

	@Override
	protected FileFilter createFileFilter(String filterString) {
		return new ResultFileFilter("Prince_rank");
	}
	
	public String run_ranking_statistic(File dir, int top_k){
		StringBuffer sb = new StringBuffer();
		
		File[] files = FileUtil.getFileList(dir.getPath(), createFileFilter(""));
		sb.append("Prince\t");
		for(File file : files){
			sb.append(readRankingGene(file, top_k));
		}
		return sb.toString();
	}
}
