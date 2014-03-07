package pagerank;


import graph.AdjacencyGraph;


interface PageRankStartProbabilityStrategy{
	public double[] getStartProbability(AdjacencyGraph g);
}
