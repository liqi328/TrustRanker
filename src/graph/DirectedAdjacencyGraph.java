package graph;

public class DirectedAdjacencyGraph extends AdjacencyGraph{
	
	@Override
	public void addEdge(String from, String to, double w){
		allocMemory();
		
		int fromId = name2IndexMap.get(from);
		int toId = name2IndexMap.get(to);

		adjacencyMatrix[fromId][toId] = w;
	}
	
	@Override
	public int getEdgeNum(){
		if(adjacencyMatrix == null){
			return 0;
		}
		
		int len = name2IndexMap.size();
		int edgeNum = 0;
		
		for(int i = 0; i < len; ++i){
			for(int j = 0; j < len; ++j){
				if(adjacencyMatrix[i][j] != INF){
					++edgeNum;
				}
			}
		}
		return edgeNum; 
	}
}
