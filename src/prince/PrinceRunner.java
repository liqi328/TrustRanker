package prince;

import diseasefamily.GeneDiseaseAssociation;
import diseasesimilarity.DiseaseDiseasePhenotypeSimilarity;
import graph.AdjacencyGraph;
import graph.DirectedAdjacencyGraph;
import graph.GraphReader;

public class PrinceRunner {
	private PrinceStartProbabilityStrategy strategy;
	private double[][] matrix;
	
	public PrinceRunner(double[][] matrix, PrinceStartProbabilityStrategy strategy){
		this.strategy = strategy;
		this.matrix = matrix;
	}
	
//	public double[] run(String ppiFilename, String diseaseOmimId){
//		AdjacencyGraph g = new DirectedAdjacencyGraph();
//		GraphReader.read(ppiFilename, g);
//		
//		return run(g.getAdjacencyMatrix(), diseaseOmimId);
//	}
	
	public double[] run(String diseaseOmimId){
		double[] start = strategy.getStartProbability(diseaseOmimId);
		
		Prince prince = new Prince();
		prince.setMatrix(matrix);
		prince.setStartProbability(start);
		prince.run();
		
		return prince.getRankScores();
	}
	
	public static void main(String[] args){
		String ppiFilename = "E:/2013�����о�/ʵ������/TrustRanker/testppi.txt";
		//String goodSeedFilename = "E:/2013�����о�/ʵ������/page_rank/test/seeds.txt";
		AdjacencyGraph g = new DirectedAdjacencyGraph();
		GraphReader.read(ppiFilename, g);
		
		double[][] normalizedMatrix = NormalizeWeightOfPPI.nomalizedWeightMatrix(g);
		
		DiseaseDiseasePhenotypeSimilarity diseaseSimilarity = new DiseaseDiseasePhenotypeSimilarity();
		GeneDiseaseAssociation associations = new GeneDiseaseAssociation();
		
		PrinceStartProbabilityStrategy strategy = new PrinceStartProbabilityStrategy(g,
				diseaseSimilarity, associations);
		
		PrinceRunner runner = new PrinceRunner(normalizedMatrix, strategy);
		
		
		//runner.run(normalizedMatrix, "");
	}
}
