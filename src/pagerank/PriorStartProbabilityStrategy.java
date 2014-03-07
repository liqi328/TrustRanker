package pagerank;

import graph.AdjacencyGraph;

import java.util.Set;

import trustrank.SeedSelectionStrategy;

public class PriorStartProbabilityStrategy implements PageRankStartProbabilityStrategy{
	private String seedFilename;
	
	public PriorStartProbabilityStrategy(String seedFilename){
		this.seedFilename = seedFilename;
	}
	
	@Override
	public double[] getStartProbability(AdjacencyGraph g){
		int n = g.getNodeNum();
		
		Set<String> seedSet = SeedSelectionStrategy.readSeedFile(seedFilename);
		double[] start = new double[n];
		for(int i = 0; i < start.length; ++i){
			start[i] = 0.0;
			if(seedSet.contains(g.getNodeName(i))){
				start[i] = 1.0 / seedSet.size();
				//System.out.println(g.getNodeName(i) + ", " + seedSet.size() + ", " + start[i]);
			}
		}
		return start;
	}
}