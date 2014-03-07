package pagerank;

import rank.RankUtil;
import graph.AdjacencyGraph;
import graph.DirectedAdjacencyGraph;
import graph.GraphReader;
import graph.GraphUtil;

public class PageRankTester {
	public static void main(String[] args){
		run_inverse_matrix();
		run_transition_matrix();
	}
	
	public static void run_inverse_matrix(){
		String ppiFilename = "E:/2013�����о�/ʵ������/page_rank/test/testrank.txt";
		String goodSeedFilename = "E:/2013�����о�/ʵ������/page_rank/test/seeds.txt";
		AdjacencyGraph g = new DirectedAdjacencyGraph();
		GraphReader.read(ppiFilename, g);
		
		//print_info(g);
		System.out.println("run_inverse_matrix:");
		
		double[] rankScores = new InversePageRankRunner(new PriorStartProbabilityStrategy(goodSeedFilename)).run(g);
		System.out.println(RankUtil.array2String(rankScores, g));

		System.out.println("-------------------------------------");
	}
	
	public static void run_transition_matrix(){
		String ppiFilename = "E:/2013�����о�/ʵ������/page_rank/test/testrank.txt";
		String goodSeedFilename = "E:/2013�����о�/ʵ������/page_rank/test/seeds.txt";
		AdjacencyGraph g = new DirectedAdjacencyGraph();
		GraphReader.read(ppiFilename, g);
		
		//print_info(g);
		
		System.out.println("run_transition_matrix:");
		
		double[] rank = new PageRankRunner(new PriorStartProbabilityStrategy(goodSeedFilename)).run(g);
		for(int i = 0; i < rank.length; ++i){
        	//System.out.println(rank[i]);
        	System.out.printf("%.3f\n", rank[i]);
        }
		System.out.println("-------------------------------------");
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
