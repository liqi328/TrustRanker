package pagerank;

import graph.AdjacencyGraph;

import java.util.Arrays;
import java.util.Set;

public class PageRankPriorStartProbabilityStrategy {
	public double[] getStartProbability(AdjacencyGraph g, Set<String> indexNameSet) {
		double[] startProbability = new double[g.getNodeNum()];
		Arrays.fill(startProbability, 0.0);
		
		for(String indexName: indexNameSet){
			if(g.containsNode(indexName)){
				startProbability[g.getNodeIndex(indexName)] = 1.0 / indexNameSet.size();
			}
		}
		
		return startProbability;
	}
}
