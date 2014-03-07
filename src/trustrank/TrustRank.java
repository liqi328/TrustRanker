package trustrank;
public class TrustRank {
	private double[][] transitionMatrix;
	private double alpha = 0.85;
	private int numberOfIteration = 20;
	
	private double[] d;
	
	private double[] rankScores;
	
	public void setMatrix(double[][] matrix){
		this.transitionMatrix = matrix;
	}
	
	public void setNumberOfIteration(int iterations){
		numberOfIteration = iterations;
	}
	
	public void setSeed(double[] seeds){
		d = new double[seeds.length];
		for(int i = 0; i < d.length; ++i){
			d[i] = seeds[i];
		}
	}
	
	public double[] getRankScores(){
		return rankScores;
	}
	
	public void run(){

		double[][] r = new double[d.length][];
		for(int i = 0; i < d.length; ++i){
			r[i] = new double[2];
			r[i][0] = d[i];
			r[i][1] = 0;
		}
		
        for(int i= 0; i < numberOfIteration; ++i)
        {
            int j = i%2;
            int k = (i+1)%2;
            for(int m = 0; m < d.length; ++m)
            {
                r[m][k] = 0.0;
                for(int n = 0; n < d.length; ++n)
                {
                        r[m][k] += alpha * transitionMatrix[m][n] * r[n][j];
                }
                r[m][k] += (1 - alpha) * d[m];
            }

        }
        
        rankScores = new double[d.length];
        int k = (numberOfIteration + 1) % 2;
        for(int i = 0; i < d.length; ++i){
        	rankScores[i] = r[i][k];
        	//System.out.println(r[i][k]);
        }
	}
	
	public static void main(String[] args){
		double[][] matrix={
                {0,     0,      0,      0,      0,      0,      0},
                {1,     0,      1,      0,      0,      0,      0},
                {0,     0.5,    0,      0,      0,      1,      0},
                {0,     0.5,    0,      0,      0,      0,      0},
                {0,     0,      0,      1,      0,      0,      0},
                {0,     0,      0,      0,      0.5,    0,      0},
                {0,     0,      0,      0,      0.5,    0,      0},
        };
		
		double[] seeds ={0,0.5,0,0.5,0,0,0};
		
		TrustRank tr = new TrustRank();
		tr.setMatrix(matrix);
		tr.setSeed(seeds);
		tr.run();
		
		double[] rank = tr.getRankScores();
		for(int i = 0; i < rank.length; ++i){
        	System.out.println(rank[i]);
        }
	}
}
