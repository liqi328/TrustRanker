package pagerank;

import graph.AdjacencyGraph;
import graph.DirectedAdjacencyGraph;
import graph.GraphReader;
import graph.GraphUtil;

public class PageRankRunner {
	protected StartProbabilityStrategy strategy = null;
	
	public PageRankRunner(){
		strategy = new AvageStartProbabilityStrategy();
	}
	
	public PageRankRunner(StartProbabilityStrategy strategy){
		this.strategy = strategy;
	}
	
	public double[] run(String ppiFilename){
		AdjacencyGraph g = new DirectedAdjacencyGraph();
		GraphReader.read(ppiFilename, g);
		
		System.out.println(g.getNodeNum() == g.getAdjacencyMatrix().length);
		return run(g);
	}
	
	public double[] run(AdjacencyGraph g){
		double[] start = strategy.getStartProbability(g);
		
		PageRank pr = new PageRank();
		pr.setMatrix(GraphUtil.getTransitionMatrix(g));
		pr.setStartProbability(start);
		pr.run();
		
		return pr.getRankScores();
	}

	
}
