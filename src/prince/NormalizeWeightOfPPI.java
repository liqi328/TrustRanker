package prince;

import graph.AdjacencyGraph;
import graph.GraphReader;

/*
 * PPI(蛋白质相互作用网络)中边的权值归一化
 * 参考文章：Prince: Associating genes and protein complex with disease via network propagation
 * */
public class NormalizeWeightOfPPI {
	public static double[][] nomalizedWeightMatrix(AdjacencyGraph g){
		double[][] matrix = new double[g.getNodeNum()][g.getNodeNum()];
		
		double[][] weightMatrix = g.getAdjacencyMatrix();
		
		double[] sumMatrix = new double[matrix.length];
		for(int i = 0; i < matrix.length; ++i){
			sumMatrix[i] = sumOfRowI(weightMatrix[i]);
		}
		
		double sumA = 0.0;
		double sumB = 0.0;
		for(int i = 0; i < matrix.length; ++i){
			sumA = sumMatrix[i];
			if(Double.doubleToLongBits(sumMatrix[i]) == Double.doubleToLongBits(0.0d)){
				sumA = 1.0;
			}
			
			for(int j = 0; j < matrix.length; ++j){
				sumB = sumMatrix[j];
				if(Double.doubleToLongBits(sumMatrix[j]) == Double.doubleToLongBits(0.0d)){
					sumB = 1.0;
				}
				matrix[i][j] = weightMatrix[i][j] / Math.sqrt(sumA * sumB);
			}
		}
		
		return matrix;
	}
	
	private static double sumOfRowI(double[] row){
		double sum = 0.0;
		
		for(int i=0; i < row.length; ++i){
			sum += row[i];
		}
		
		return sum;
	}
	
	public static void main(String[] args){
		double[][] row = {{0.2, 0.5, 0.6}, {0.5,0.3,0.6}};
		for(int i = 0; i < row.length; ++i)
			System.out.println(sumOfRowI(row[i]));
		
		String ppiFilepath = "E:/2013疾病研究/实验数据/TrustRanker/testppi.txt";
		AdjacencyGraph g = new AdjacencyGraph();
		
		GraphReader.read(ppiFilepath, g);
		
		for(int i = 0; i < g.getNodeNum(); ++i)
			System.out.println(sumOfRowI(g.getAdjacencyMatrix()[i]));
		
		System.out.println(g.edgesToString());
		System.out.println("-----------------------------");
		double[][] matrix = nomalizedWeightMatrix(g);
		for(int i = 0; i < g.getNodeNum(); ++i){
			for(int j = 0; j < g.getNodeNum(); ++j){
				System.out.print(matrix[i][j]);
				System.out.print("\t");
			}
			System.out.println();
		}
		
		System.out.println(Double.doubleToLongBits(0.12356d) == Double.doubleToLongBits(0.0));
		System.out.println(Double.doubleToLongBits(0.0000000000000000001d) == Double.doubleToLongBits(0.0));
		System.out.println(Double.doubleToLongBits(0.00d) == Double.doubleToLongBits(0.0));
	}
}
