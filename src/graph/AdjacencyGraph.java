package graph;


import java.util.HashMap;
import java.util.Map;

public class AdjacencyGraph{
	private static enum INFINITY{ 
		ZERO(0), INF(Integer.MAX_VALUE);
		
		public int getValue(){
			return value;
		}
		
		private INFINITY(int v){
			value = v;
		}
		private int value;
	};
	
	public static final int INF = INFINITY.ZERO.getValue();
	
	protected Map<String, Integer> name2IndexMap = new HashMap<String, Integer>();
	protected Map<Integer, String> index2NameMap = new HashMap<Integer, String>();
	
	protected double[][] adjacencyMatrix;	//邻接矩阵
	
	private int index = 0;
	
	public double[][] getAdjacencyMatrix(){
		return adjacencyMatrix;
	}
	
	/**
	 * 返回图的邻接矩阵，若withWeighed = false, 则将边的权值置为1
	 * @param withWeighed	是否需要权值
	 * @return
	 */
	public double[][] getAdjacencyMatrix(boolean withWeighed){
		if(withWeighed){
			return adjacencyMatrix;
		}
		int len = adjacencyMatrix.length;
		double[][] matrix_ret = new double[len][len];
		for(int i = 0; i < len; ++i){
			for(int j = 0; j < len; ++j){
				matrix_ret[i][j] = adjacencyMatrix[i][j] != INF ? 1 : INF;
			}
		}
		return matrix_ret;
	}
	
	public int getNodeNum(){
		return name2IndexMap.size();
	}
	
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
		return edgeNum >> 1; 
	}
	
	public void addNode(String nodeName){
		if(!name2IndexMap.containsKey(nodeName)){
			name2IndexMap.put(nodeName, index);
			index2NameMap.put(index, nodeName);
			++index;
		}
	}
	public void addEdge(String from, String to){
		this.addEdge(from, to, 1.0);
	}
	
	public void addEdge(String from, String to, double w){
		allocMemory();
		
		int fromId = name2IndexMap.get(from);
		int toId = name2IndexMap.get(to);

		adjacencyMatrix[fromId][toId] = w;
		adjacencyMatrix[toId][fromId] = w;
	}
	
	public boolean containsEdge(String a, String b){
		if(adjacencyMatrix[name2IndexMap.get(a)][name2IndexMap.get(b)] == INF){
			return true;
		}
		return false;
	}
	
	public String getNodeName(int index) {
		return index2NameMap.get(index);
		
	}

	public Integer getNodeIndex(String name) {
		return name2IndexMap.get(name);
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("node number: " + getNodeNum()).append("\n");
		sb.append("edge number: " + getEdgeNum()).append("\n");
		return sb.toString();
	}
	
	protected void allocMemory(){
		if(adjacencyMatrix == null){
			int len = name2IndexMap.size();
			adjacencyMatrix = new double[len][len];
			
			for(int i = 0; i < len; ++i){
				for(int j = 0; j < len; ++j){
					adjacencyMatrix[i][j] = INF;
				}
			}
		}
	}
	
}
