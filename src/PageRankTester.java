
public class PageRankTester {
	public static void main(String[] args){
		run_inverse_matrix();
		run_transition_matrix();
	}
	
	public static void run_inverse_matrix(){
		String ppiFilename = "E:/2013疾病研究/实验数据/page_rank/test/testrank.txt";
		AdjacencyGraph g = new DirectedAdjacencyGraph();
		GraphReader.read(ppiFilename, g);
		
		//print_info(g);
		System.out.println("run_inverse_matrix:");
		
		run(GraphUtil.getInverseTransitionMatrix(g));
		
		System.out.println("-------------------------------------");
	}
	
	public static void run_transition_matrix(){
		String ppiFilename = "E:/2013疾病研究/实验数据/page_rank/test/testrank.txt";
		AdjacencyGraph g = new DirectedAdjacencyGraph();
		GraphReader.read(ppiFilename, g);
		
		//print_info(g);
		
		System.out.println("run_transition_matrix:");
		
		run(GraphUtil.getTransitionMatrix(g));
		
		System.out.println("-------------------------------------");
	}
	
	private static void run(double[][] matrix){
		double[] rank = PageRankRunner.run(matrix);
		for(int i = 0; i < rank.length; ++i){
        	//System.out.println(rank[i]);
        	System.out.printf("%.3f\n", rank[i]);
        }
	}
	
	private static void print_info(AdjacencyGraph g){
		System.out.println(g);
		System.out.println("Adjacency Matrix:");
		printMatrix(g.getAdjacencyMatrix());
		
		double[][] transitionMatrix = GraphUtil.getTransitionMatrix(g);
		System.out.println("Transition Matrix:");
		printMatrix(transitionMatrix);
		
		double[][] inverseTransitionMatrix = GraphUtil.getInverseTransitionMatrix(g);
		System.out.println("Inverse Transition Matrix:");
		printMatrix(inverseTransitionMatrix);
	}
	
	private static String printMatrix(double[][] matrix){
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < matrix.length; ++i){
			for(int j = 0; j < matrix.length; ++j){
				sb.append(matrix[i][j]).append(", ");
			}
			sb.append("\n");
		}
		System.out.println(sb.toString());
		return sb.toString();
	}

}
