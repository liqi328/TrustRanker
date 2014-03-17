package pagerank;

import graph.AdjacencyGraph;

public class AvageStartProbabilityStrategy implements StartProbabilityStrategy{
	@Override
	public double[] getStartProbability(AdjacencyGraph g){
		int n = g.getNodeNum();
		
		double[] start = new double[n];
		for(int i = 0; i < start.length; ++i){
			start[i] = 1.0 / n;
		}
		return start;
	}
}