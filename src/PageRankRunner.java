
public class PageRankRunner {
	public static double[] run(String ppiFilename){
		AdjacencyGraph g = new DirectedAdjacencyGraph();
		GraphReader.read(ppiFilename, g);
		
		return run(GraphUtil.getTransitionMatrix(g));
	}
	
	public static double[] run(double[][] matrix){
		double[] start = getStartProbability(matrix.length);
		
		PageRank pr = new PageRank();
		pr.setMatrix(matrix);
		pr.setStartProbability(start);
		pr.run();
		
		return pr.getRankScores();
	}
	
	private static double[] getStartProbability(int n){
		double[] start = new double[n];
		for(int i = 0; i < start.length; ++i){
			start[i] = 1.0 / n;
		}
		return start;
	}
}
