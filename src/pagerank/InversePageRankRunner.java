package pagerank;

import graph.AdjacencyGraph;
import graph.GraphUtil;

public class InversePageRankRunner extends PageRankRunner {
	public InversePageRankRunner(){
		
	}
	
	public InversePageRankRunner(PageRankStartProbabilityStrategy strategy){
		super(strategy);
	}
	
	public double[] run(AdjacencyGraph g){
		double[] start = strategy.getStartProbability(g);
		
		PageRank pr = new PageRank();
		pr.setMatrix(GraphUtil.getInverseTransitionMatrix(g));
		pr.setStartProbability(start);
		pr.run();
		
		return pr.getRankScores();
	}
}
