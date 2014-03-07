package trustrank;

import graph.AdjacencyGraph;
import graph.DirectedAdjacencyGraph;
import graph.GraphReader;
import graph.GraphUtil;

public class TrustRankRunner {
	private SeedSelectionStrategy strategy;
	private int L; //limit of oracle invocations
	
	public TrustRankRunner(SeedSelectionStrategy strategy, int L){
		this.strategy = strategy;
		this.L = L;
	}
	
	public double[] run(String ppiFilename, String goodSeedFilename){
		AdjacencyGraph g = new DirectedAdjacencyGraph();
		GraphReader.read(ppiFilename, g);
		
		double[] seeds = strategy.selectSeeds(goodSeedFilename, g, L);
		
		return run(g, seeds);
	}
	
	public double[] run(AdjacencyGraph g, String goodSeedFilename){		
		double[] seeds = strategy.selectSeeds(goodSeedFilename, g, L);
		
		return run(g, seeds);
	}
	
	public double[] run(AdjacencyGraph g, double[] seeds){
		double[][] transitionMatrix = GraphUtil.getTransitionMatrix(g);
		
		TrustRank tr = new TrustRank();
		tr.setMatrix(transitionMatrix);
		tr.setSeed(seeds);
		tr.run();
		
		return tr.getRankScores();
	}
}
