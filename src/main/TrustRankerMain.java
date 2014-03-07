package main;
import graph.AdjacencyGraph;
import graph.DirectedAdjacencyGraph;
import graph.GraphReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import pagerank.AvageStartProbabilityStrategy;
import pagerank.InversePageRankRunner;
import pagerank.PageRankRunner;
import pagerank.PriorStartProbabilityStrategy;
import rank.RankUtil;
import trustrank.SeedSelectionStrategy;
import trustrank.TrustRankRunner;
import util.MyLogger;
import util.WriterUtil;

class InputArgument{
	private Properties p = new Properties();
	
	public InputArgument(String configFilepath){
		try {
			InputStreamReader is = new InputStreamReader(new FileInputStream(configFilepath), "UTF-8");
			p.load(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getPpiFilename(){
		return p.getProperty("ppiFilename");
	}
	
	public String getDiseaseSeedFilename(){
		return p.getProperty("diseaseSeedFilename");
	}
	
	public String getOutputDir(){
		File outputDir = new File(p.getProperty("outputDir"));
		if(!outputDir.exists()){
			outputDir.mkdirs();
		}
		return p.getProperty("outputDir");
	}
}

public class TrustRankerMain {
	public static final int L = 100;
	public static void main(String[] args){		
		if(args.length != 1){
			System.out.println("Argument Error.");
			System.out.println("Using method: java -Xmx2048m -jar TrustRanker.jar ./input/config.txt");
			System.exit(-1);
		}
		InputArgument input = new InputArgument(args[0]);
		String ppiFilename = input.getPpiFilename();
		String diseaseSeedFilename = input.getDiseaseSeedFilename();
		
		AdjacencyGraph g = new DirectedAdjacencyGraph();
		GraphReader.read(ppiFilename, g);
		
		run_pagerank_avg(g, diseaseSeedFilename, input.getOutputDir());
		run_pagerank_priors(g, diseaseSeedFilename, input.getOutputDir());
		
		run_trustrank_inverse_priors(g, diseaseSeedFilename, input.getOutputDir());
		run_trustrank_inverse_avg(g, diseaseSeedFilename, input.getOutputDir());
		
		run_trustrank_high_priors(g, diseaseSeedFilename, input.getOutputDir());
		run_trustrank_high_avg(g, diseaseSeedFilename, input.getOutputDir());
	}
	
	private static void run_pagerank_avg(AdjacencyGraph g, String diseaseSeedFilename, String outputDir){
		System.out.println("----------Page Rank[avage] Running Start-----------------");
		MyLogger.log("----------Page Rank[avage] Running Start-----------------");
		
		PageRankRunner runner = new PageRankRunner(new AvageStartProbabilityStrategy());
		double[] rankScores = runner.run(g);
		
		WriterUtil.write(outputDir + "/pagerank_avg_ranks.txt",
				RankUtil.array2String(rankScores, g));
		
		MyLogger.log("----------Page Rank[avage] Running end-----------------");
		System.out.println("----------Page Rank[avage] Running end-----------------");
	}
	
	private static void run_pagerank_priors(AdjacencyGraph g, String diseaseSeedFilename, String outputDir){
		System.out.println("----------Page Rank[priors] Running Start-----------------");
		MyLogger.log("----------Page Rank[priors] Running Start-----------------");
		
		PageRankRunner runner = new PageRankRunner(new PriorStartProbabilityStrategy(diseaseSeedFilename));
		double[] rankScores = runner.run(g);
		
		WriterUtil.write(outputDir + "/pagerank_priors_ranks.txt",
				RankUtil.array2String(rankScores, g));
		
		MyLogger.log("----------Page Rank[priors] Running end-----------------");
		System.out.println("----------Page Rank[priors] Running end-----------------");
	}
	
	private static void run_trustrank_inverse_avg(AdjacencyGraph g, String diseaseSeedFilename, String outputDir){
		System.out.println("----------Inverse Trust Rank[avage] Running start-----------------");
		MyLogger.log("----------Inverse Trust Rank[avage] Running start-----------------");
		
		PageRankRunner pageRunner = new InversePageRankRunner(new AvageStartProbabilityStrategy());
		
		TrustRankRunner runner = new TrustRankRunner(new SeedSelectionStrategy(pageRunner), L);
		
		double[] rankScores = runner.run(g, diseaseSeedFilename);
		
		WriterUtil.write(outputDir + "/trustrank_inverse_avage_ranks.txt",
				RankUtil.array2String(rankScores, g));
		
		MyLogger.log("----------Inverse Trust Rank[avage] Running end-----------------");
		System.out.println("----------Inverse Trust Rank[avage] Running end-----------------");
	}
	
	private static void run_trustrank_inverse_priors(AdjacencyGraph g, String diseaseSeedFilename, String outputDir){
		System.out.println("----------Inverse Trust Rank[priors] Running start-----------------");
		MyLogger.log("----------Inverse Trust Rank[priors] Running start-----------------");
		
		PageRankRunner pageRunner = new InversePageRankRunner(new PriorStartProbabilityStrategy(diseaseSeedFilename));
		
		TrustRankRunner runner = new TrustRankRunner(new SeedSelectionStrategy(pageRunner), L);
		
		double[] rankScores = runner.run(g, diseaseSeedFilename);
		
		WriterUtil.write(outputDir + "/trustrank_inverse_priors_ranks.txt",
				RankUtil.array2String(rankScores, g));
		
		MyLogger.log("----------Inverse Trust Rank[priors] Running end-----------------");
		System.out.println("----------Inverse Trust Rank[priors] Running end-----------------");
	}
	
	private static void run_trustrank_high_avg(AdjacencyGraph g, String diseaseSeedFilename, String outputDir){
		System.out.println("----------High Trust Rank[avage] Running start-----------------");
		MyLogger.log("----------High Trust Rank[avage] Running start-----------------");
		
		PageRankRunner pageRunner = new PageRankRunner(new AvageStartProbabilityStrategy());
		TrustRankRunner runner = new TrustRankRunner(new SeedSelectionStrategy(pageRunner), L);
		
		double[] rankScores = runner.run(g, diseaseSeedFilename);
		
		WriterUtil.write(outputDir + "/trustrank_high_avage_ranks.txt",
				RankUtil.array2String(rankScores, g));
		
		MyLogger.log("----------High Trust Rank[avage] Running end-----------------");
		System.out.println("----------High Trust Rank[avage] Running end-----------------");
	}
	
	private static void run_trustrank_high_priors(AdjacencyGraph g, String diseaseSeedFilename, String outputDir){
		System.out.println("----------High Trust Rank[priors] Running start-----------------");
		MyLogger.log("----------High Trust Rank[priors] Running start-----------------");
		
		PageRankRunner pageRunner = new PageRankRunner(new PriorStartProbabilityStrategy(diseaseSeedFilename));
		TrustRankRunner runner = new TrustRankRunner(new SeedSelectionStrategy(pageRunner), L);
		
		double[] rankScores = runner.run(g, diseaseSeedFilename);
		
		WriterUtil.write(outputDir + "/trustrank_high_priors_ranks.txt",
				RankUtil.array2String(rankScores, g));
		
		MyLogger.log("----------High Trust Rank[priors] Running end-----------------");
		System.out.println("----------High Trust Rank[priors] Running end-----------------");
	}
}
