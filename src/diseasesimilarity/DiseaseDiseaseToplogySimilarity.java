package diseasesimilarity;

import graph.AdjacencyGraph;
import hdnANDdgn.HumanDiseaseNetwork;

import java.util.HashMap;
import java.util.Map;

import diseasefamily.GeneDiseaseAssociation;

/*
 * 利用Human Disease Network的拓扑计算疾病与疾病之间的相似性
 * 
 * */
public class DiseaseDiseaseToplogySimilarity implements DiseaseDiseaseSimilarity{
	private Map<String, Integer> omimId2IndexMap;
	private Map<Integer, String> index2OmimIdMap;
	
	protected double[][] similarityMatrix;	//邻接矩阵
	
	public DiseaseDiseaseToplogySimilarity(HumanDiseaseNetwork humanDiseaseNetwork){
		AdjacencyGraph g = humanDiseaseNetwork.getAdjacencyGraph();
		omimId2IndexMap = new HashMap<String, Integer>();
		index2OmimIdMap = new HashMap<Integer, String>();
		
		for(int i = 0; i < g.getNodeNum(); ++i){
			omimId2IndexMap.put(g.getNodeName(i), i);
			index2OmimIdMap.put(i, g.getNodeName(i));
		}
		
		allocMemory(g.getNodeNum());
		
		calculateSimilarity(g);
	}
	
	public double getTwoDiseaseSimilarity(String omimId1, String omimId2){
		Integer i = omimId2IndexMap.get(omimId1);
		Integer j = omimId2IndexMap.get(omimId2);
		
		if(i == null || j == null){
			return 0.0;
		}
		
		return similarityMatrix[i][j];
	}
	
	/*
	 * similarity = (1 - alpha) * toplogySimilarity + alpha * pheotypeSimilarity
	 * */
	public void addDiseaseDiseaseSimilarity(DiseaseDiseaseSimilarity anotherDiseaseSimilarity, double alpha){
		double pSimilarity = 0.0;
		for(int i = 0; i < similarityMatrix.length; ++i){
			for(int j = i+1; j < similarityMatrix.length; ++j){
				pSimilarity = anotherDiseaseSimilarity.getTwoDiseaseSimilarity(index2OmimIdMap.get(i), index2OmimIdMap.get(j));
				similarityMatrix[i][j] = (1.0 - alpha) * similarityMatrix[i][j] + alpha * pSimilarity;
				
				similarityMatrix[j][i] = similarityMatrix[i][j];
			}
		}
	}
	
	private void allocMemory(int len){
		if(similarityMatrix == null){
			similarityMatrix = new double[len][len];
			for(int i = 0; i < len; ++i){
				for(int j = 0; j < len; ++j){
					similarityMatrix[i][j] = 0.0;
				}
			}
		}
	}
	
	private void calculateSimilarity(AdjacencyGraph g ){
		double[][] adjacencyMatrix = g.getAdjacencyMatrix();
		
		double max = 0.0, min = 10000000;
		
		for(int i = 0; i < similarityMatrix.length; ++i){
			similarityMatrix[i][i] = 1.0;
			for(int j = i + 1; j < similarityMatrix.length; ++j){
				if(Double.doubleToLongBits(adjacencyMatrix[i][j]) > Double.doubleToLongBits(0.0)){
					similarityMatrix[i][j] = calculateSimilarity_connected(i, j, adjacencyMatrix);
					similarityMatrix[j][i] = similarityMatrix[i][j];
					
					if(Double.doubleToLongBits(similarityMatrix[i][j]) > Double.doubleToLongBits(1.0)){
						similarityMatrix[i][j] = 1.0;
					}
					
					if(similarityMatrix[i][j] > max){
						max = similarityMatrix[i][j];
					}
					
					if(similarityMatrix[i][j] < min){
						min = similarityMatrix[i][j];
					}
//					
//					if(Double.doubleToLongBits(similarityMatrix[i][j]) > Double.doubleToLongBits(1.0)){
//						similarityMatrix[i][j] = 1.0;
//						System.out.println(index2OmimIdMap.get(i) + "\t" + index2OmimIdMap.get(j) + "\t" + similarityMatrix[i][j]);
//						System.out.println("-->"+ Double.doubleToLongBits(similarityMatrix[i][j]));
//					}
				}
			}
		}
		//System.out.println("max: " + max + ", min: " + min);
	}
	
	/**
	 * 计算直接相连两个疾病之间的相似性
	 */
	private double calculateSimilarity_connected(int u, int v, double[][] matrix){
		double similarity = 0.0;
		
		double sumAB = 2 * matrix[u][v];
		double sumA2 = 1.0, sumB2 = 1.0;
		
		for(int i = 0; i < matrix.length; ++i){
			if(Double.doubleToLongBits(matrix[u][i]) > Double.doubleToLongBits(0.0) 
					&& Double.doubleToLongBits(matrix[v][i]) > Double.doubleToLongBits(0.0)){
				sumAB += matrix[u][i] * matrix[v][i];
			}
			
			if(Double.doubleToLongBits(matrix[u][i]) > Double.doubleToLongBits(0.0)){
				sumA2 += matrix[u][i] * matrix[u][i];
			}
			if(Double.doubleToLongBits(matrix[v][i]) > Double.doubleToLongBits(0.0)){
				sumB2 += matrix[v][i] * matrix[v][i];
			}
		}
		
		similarity = sumAB / (Math.sqrt(sumA2) * Math.sqrt(sumB2));
		return similarity;
	}
	
	public static void main(String[] args){
		String geneDiseaseAssociationFilepath = "E:/2013疾病研究/疾病数据/OMIM/geneOmimId_diseaseOmimId.txt";
		GeneDiseaseAssociation associations = new GeneDiseaseAssociation();
		associations.read(geneDiseaseAssociationFilepath);
		
		HumanDiseaseNetwork humanDiseaseNetwork = new HumanDiseaseNetwork();
		humanDiseaseNetwork.create(associations);
		
		DiseaseDiseaseToplogySimilarity toplogySimilarity = new DiseaseDiseaseToplogySimilarity(humanDiseaseNetwork);
		
	}
}
