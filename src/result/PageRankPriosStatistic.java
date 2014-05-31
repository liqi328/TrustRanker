package result;

import java.io.File;
import java.io.FileFilter;

public class PageRankPriosStatistic extends AbstractStatistic {
	public PageRankPriosStatistic(File[] resultDirs) {
		super(resultDirs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public FileFilter createFileFilter() {
		return new ResultFileFilter("PRP_validation");
	}

	@Override
	protected void printLogHeader() {
		System.out.println("---------------PageRankPrios statistic running -------------");
	}

	@Override
	protected void printLogFooter() {
		System.out.println("--------------- PageRankPrios statistic finished -------------");
	}
	
	@Override
	protected FileFilter createFileFilter(String filterString) {
		return new ResultFileFilter(filterString);
	}

	@Override
	protected String run_ranking_statistic(File dir, int top_k) {
		StringBuffer sb = new StringBuffer();
		
		return sb.toString();
	}
}
