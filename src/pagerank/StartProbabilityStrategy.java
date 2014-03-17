package pagerank;


import graph.AdjacencyGraph;


public interface StartProbabilityStrategy{
	public double[] getStartProbability(AdjacencyGraph g);
}
