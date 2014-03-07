package main;

import pagerank.PageRankRunner;
import graph.AdjacencyGraph;
import graph.DirectedAdjacencyGraph;
import graph.GraphReader;
import rank.RankUtil;
import trustrank.InversePageRankSeedSelectionStrategy;
import trustrank.TrustRankRunner;
import util.WriterUtil;

public class Main {
	public static void main(String[] args){
		String ppiFilename = "E:/2013疾病研究/实验数据/TrustRanker/input/HPRD_ppi.txt";
		String seedFilename = "E:/2013疾病研究/实验数据/TrustRanker/input/neurodegenerative_seeds.txt";
		
		AdjacencyGraph g = new DirectedAdjacencyGraph();
		GraphReader.read(ppiFilename, g);
		
		PageRankRunner pageRunner = new PageRankRunner();
		TrustRankRunner runner = new TrustRankRunner(new InversePageRankSeedSelectionStrategy(pageRunner), 20);
		
		double[] rankScores = runner.run(g, seedFilename);
		
		WriterUtil.write("E:/2013疾病研究/实验数据/TrustRanker/ranks.txt",
				RankUtil.array2String(rankScores, g));
	}
	
	
}
