package trustrank;
import graph.AdjacencyGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pagerank.InversePageRankRunner;
import pagerank.PageRankRunner;
import pagerank.PriorStartProbabilityStrategy;
import rank.Rank;
import rank.TopNAlgorithm;
import util.MyLogger;


public class SeedSelectionStrategy {
	protected PageRankRunner runner;
	
	public SeedSelectionStrategy(PageRankRunner runner){
		this.runner = runner;
	}
	
	
	public double[] selectSeeds(String goodSeedFilename, AdjacencyGraph g, int L) {
		double[] rankScores = runner.run(g);
		
		Set<String> goodSeedSet = readSeedFile(goodSeedFilename);
		
		List<Rank> rankList = TopNAlgorithm.run(rankScores, L);
		
		double[] seeds = new double[g.getNodeNum()];
		Arrays.fill(seeds, 0.0);
		
		int goodSeedNum = 0;
		for(Rank rank : rankList){
			if(goodSeedSet.contains(g.getNodeName(rank.index))){
				seeds[rank.index] = 1.0;
				++goodSeedNum;
				//System.out.println("+" + g.getNodeName(rank.index));
			}else{
				//System.out.println("-" + g.getNodeName(rank.index));
			}
		}
		
		MyLogger.log(goodSeedFilename + "]: " + goodSeedSet.size() + ", " + goodSeedNum);
		
		for(int i = 0; i < seeds.length; ++i){
			seeds[i] /= goodSeedNum; 
		}
		
		return seeds;
	}
	
	public static Set<String> readSeedFile(String goodSeedFilename){
		Set<String> goodSeedSet = new HashSet<String>();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(goodSeedFilename)));
			String line = null;
			
			while((line = in.readLine()) != null){
				goodSeedSet.add(line);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return goodSeedSet;
	}
}
