package graph;

public class GraphUtil {
	/*
	 * get the transition matrix(T) corresponding to the graph g
	 * */
	public static double[][] getTransitionMatrix(AdjacencyGraph g){
		double[][] adjacencyMatrix = g.getAdjacencyMatrix();
		double[][] transitionMatrix = new double[adjacencyMatrix.length][adjacencyMatrix.length];
		
		for(int i = 0; i < adjacencyMatrix.length; ++i){
			int outdegree = 0;
			for(int j = 0; j < adjacencyMatrix.length; ++j){
				if(adjacencyMatrix[i][j] != AdjacencyGraph.INF){
					++outdegree;
				}
			}
			
			outdegree = outdegree == 0 ? 1 : outdegree;
			
			for(int j = 0; j < adjacencyMatrix.length; ++j){
				transitionMatrix[j][i] = adjacencyMatrix[i][j] / outdegree;
			}
			
		}
		
		return transitionMatrix;
	}
	
	/*
	 * get the inverse transition matrix(U) corresponding to the graph g
	 * */
	public static double[][] getInverseTransitionMatrix(AdjacencyGraph g){
		double[][] adjacencyMatrix = g.getAdjacencyMatrix();
		double[][] inverseTransitionMatrix = new double[adjacencyMatrix.length][adjacencyMatrix.length];
		
		for(int i = 0; i < adjacencyMatrix.length; ++i){
			for(int j = 0; j < adjacencyMatrix.length; ++j){
				int indegree = 0;
				if(adjacencyMatrix[i][j] != AdjacencyGraph.INF){
					for(int r = 0; r < adjacencyMatrix.length; ++r){
						if(adjacencyMatrix[r][j] != AdjacencyGraph.INF){
							++indegree;
						}
					}
				}
				indegree = indegree == 0 ? 1 : indegree;
				inverseTransitionMatrix[i][j] = adjacencyMatrix[i][j] / indegree;
			}
		}
		
		return inverseTransitionMatrix;
	}

}
