package pagerank;

public class PageRank {
	private double[][] transitionMatrix;
	private double alpha = 0.85;
	private int numberOfIteration = 20;	
	
	private double[] startProbability;//每个点的初始概率
	
	private double[] rankScores;
	
	public void setMatrix(double[][] matrix){
		this.transitionMatrix = matrix;
	}
	
	public void setStartProbability(double[] start){
		startProbability = new double[start.length];
		for(int i = 0; i < startProbability.length; ++i){
			startProbability[i] = start[i];
		}
	}
	
	public void setNumberOfIteration(int iterations){
		numberOfIteration = iterations;
	}
	
	public double[] getRankScores(){
		return rankScores;
	}
	
	public void run(){

		double[][] r = new double[startProbability.length][];
		for(int i = 0; i < startProbability.length; ++i){
			r[i] = new double[2];
			r[i][0] = startProbability[i];
			r[i][1] = 0;
		}
		
        for(int i= 0; i < numberOfIteration; ++i)
        {
            int j = i%2;
            int k = (i+1)%2;
            for(int m = 0; m < startProbability.length; ++m)
            {
                r[m][k] = 0.0;
                for(int n = 0; n < startProbability.length; ++n)
                {
                        r[m][k] += alpha * transitionMatrix[m][n] * r[n][j];
                }
                r[m][k] += (1 - alpha) * startProbability[m];
            }

        }
        
        rankScores = new double[startProbability.length];
        int k = (numberOfIteration + 1) % 2;
        for(int i = 0; i < startProbability.length; ++i){
        	rankScores[i] = r[i][k];
        	//System.out.println(r[i][k]);
        }
	}
	
	public static void main(String[] args){
		double[][] matrix={
			{0,     0.5,    0,      0,      0,      0,      0},
			{0,     0,      0.5,    1,      0,      0,      0},
			{0,     0.5,    0,      0,      0,      0,      0},
			{0,     0,      0,      0,      1,      0,      0},
			{0,     0,      0,      0,      0,      1,      1},
			{0,     0,      0.5,    0,      0,      0,      0},
			{0,     0,      0,      0,      0,      0,      0},
        };
		
		double[] start ={1.0/7,1.0/7,1.0/7,1.0/7,1.0/7,1.0/7,1.0/7};
		
		PageRank pr = new PageRank();
		pr.setMatrix(matrix);
		pr.setStartProbability(start);
		pr.run();
		
		double[] rank = pr.getRankScores();
		for(int i = 0; i < rank.length; ++i){
        	System.out.println(rank[i]);
        }
	}
}
