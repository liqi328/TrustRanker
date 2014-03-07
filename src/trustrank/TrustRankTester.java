package trustrank;

import graph.AdjacencyGraph;
import graph.DirectedAdjacencyGraph;
import graph.GraphReader;
import graph.GraphUtil;
import pagerank.PageRankRunner;
import rank.RankUtil;

public class TrustRankTester {
	public static void main(String[] args){
		run_1();
		run_2();
		run_3();
	}
	
	public static void run_1(){
		String ppiFilename = "E:/2013疾病研究/实验数据/page_rank/test/testrank.txt";
		AdjacencyGraph g = new DirectedAdjacencyGraph();
		GraphReader.read(ppiFilename, g);
		
		double[] seeds ={0,0.5,0,0.5,0,0,0};
		
		print_info(g);
		
		double[] rankScores = new TrustRankRunner(null, 0).run(g, seeds);
		for(int i = 0; i < rankScores.length; ++i){
        	//System.out.println(rank[i]);
        	System.out.printf("%.3f\n", rankScores[i]);
        }
	}
	
	public static void run_2(){
		System.out.println("------Inverse Page Rank Seed Selection------------");
		
		String ppiFilename = "E:/2013疾病研究/实验数据/page_rank/test/testrank.txt";
		String goodSeedFilename = "E:/2013疾病研究/实验数据/page_rank/test/seeds.txt";
		
		PageRankRunner pageRunner = new PageRankRunner();
		
		SeedSelectionStrategy strategy = new InversePageRankSeedSelectionStrategy(pageRunner);
		TrustRankRunner runner = new TrustRankRunner(strategy, 3);
		
		double[] rankScores = runner.run(ppiFilename, goodSeedFilename);
		for(int i = 0; i < rankScores.length; ++i){
        	System.out.printf("%.3f\n", rankScores[i]);
        }
		
		System.out.println("---------------------------------------------");
	}
	
	public static void run_3(){
		System.out.println("------High Page Rank Seed Selection------------");
		
		String ppiFilename = "E:/2013疾病研究/实验数据/page_rank/test/testrank.txt";
		String goodSeedFilename = "E:/2013疾病研究/实验数据/page_rank/test/seeds.txt";
		AdjacencyGraph g = new DirectedAdjacencyGraph();
		GraphReader.read(ppiFilename, g);
		
		
		PageRankRunner pageRunner = new PageRankRunner();
		SeedSelectionStrategy strategy = new HighPageRankSeedSelectionStrategy(pageRunner);
		TrustRankRunner runner = new TrustRankRunner(strategy, 3);
		
		double[] rankScores = runner.run(g, goodSeedFilename);
		
		System.out.println(RankUtil.array2String(rankScores, g));
		
		System.out.println("------------------------------------------");
	}
	
	public static String printMatrix(double[][] matrix){
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
}
