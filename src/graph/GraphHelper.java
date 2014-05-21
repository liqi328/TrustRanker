package graph;


import java.util.HashSet;
import java.util.Set;

public class GraphHelper {
	public static String degreeDistribution2String(AdjacencyGraph g){
		StringBuffer sb = new StringBuffer();
		
		double[][] matrix = g.getAdjacencyMatrix();
		for(int i = 0; i < matrix.length; ++ i){
			sb.append(g.getNodeName(i)).append("\t");
			sb.append(g.getDegreeOfNode(i)).append("\n");
		}
		
		return sb.toString();
	}
	
	
	/**
	 * 判断图是否连通
	 * @param g
	 * @return
	 */
	public static boolean isConnected(AdjacencyGraph g){
		Set<Integer> visited = new HashSet<Integer>();
		dfs(g, 0, visited);
		
		if(visited.size() < g.getNodeNum()){
			return false;
		}
		return true;
	}
	
	/**
	 * 计算图的连通分量的数目
	 * @param g
	 * @return
	 */
	public static int countConnectedComponent(AdjacencyGraph g){
		Set<Integer> visited = new HashSet<Integer>();
		Set<Integer> preVisited = new HashSet<Integer>();
		int count = 0;
		for(int i = 0; i < g.getNodeNum(); ++i){
			if(visited.contains(i))continue;
			dfs(g, i, visited);
			++count;
			System.out.println("连通分量"+ count + "\t" + (visited.size() - preVisited.size()));
			preVisited.addAll(visited);
		}
		
		return count;
	}
	
	private static void dfs(AdjacencyGraph g, Integer v, Set<Integer> visited){
		visited.add(v);
		double[][] matrix = g.getAdjacencyMatrix();
		for(int i = 0; i < matrix.length; ++ i){
			if(matrix[v][i] != AdjacencyGraph.INF && !visited.contains(i)){
				dfs(g, i, visited);
			}
		}
	}
	
	public static void main(String[] args){
		String ppiFilename = "E:/2013疾病研究/实验数据/page_rank/test/testgraph.txt";
		AdjacencyGraph g = new AdjacencyGraph();
		GraphReader.read(ppiFilename, g);
		
		System.out.println(countConnectedComponent(g));
	}
}
