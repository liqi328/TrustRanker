package pagerank;

import graph.AdjacencyGraph;
import graph.DirectedAdjacencyGraph;
import graph.GraphReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import prince.InputArgument;
import prince.NormalizeWeightOfPPI;
import rank.RankUtil;
import util.FileUtil;
import util.WriterUtil;
import diseasefamily.Gene;
import diseasefamily.GeneDiseaseAssociation;


public class PageRankPriosCrossValidation {
	private String ppiFilepath;
	private String geneDiseaseAssociationFilepath;
	private String diseaseFilepath;
	
	private String outputDir;
	private String hprdIdMapppingFilepath;
	
	public PageRankPriosCrossValidation(String ppiFilepath,
			String geneDiseaseAssociationFilepath, String diseaseFilepath, String outputDir,
			String hprdIdMapppingFilepath){
		this.ppiFilepath = ppiFilepath;
		this.geneDiseaseAssociationFilepath = geneDiseaseAssociationFilepath;
		this.diseaseFilepath = diseaseFilepath;
		this.outputDir = outputDir;
		this.hprdIdMapppingFilepath = hprdIdMapppingFilepath;
	}
	
	public void batch_run(){
		AdjacencyGraph g = new DirectedAdjacencyGraph();
		GraphReader.read(ppiFilepath, g);
		
		double[][] normalizedMatrix = NormalizeWeightOfPPI.nomalizedWeightMatrix(g);
		
		GeneDiseaseAssociation associations = new GeneDiseaseAssociation(hprdIdMapppingFilepath);
		associations.read(geneDiseaseAssociationFilepath);
		
		Set<String> diseaseOmimIdSet = readDiseaseFile();
		
		for(String diseaseOmimId: diseaseOmimIdSet){
			run_one_disease(diseaseOmimId, associations, normalizedMatrix, g);
		}
	}
	
	public void run_one_disease(String diseaseOmimId, GeneDiseaseAssociation associations, double[][] normalizedMatrix, AdjacencyGraph g){
		System.out.println("Disease: " + diseaseOmimId);
		
		String outputDirPath = outputDir + "/" + diseaseOmimId;
		FileUtil.makeDir(new File(outputDirPath));
		
		System.out.println("genes: " + associations.disorderMap.get(diseaseOmimId).geneMap.size());
		
		PageRankPriorStartProbabilityStrategy strategy = new PageRankPriorStartProbabilityStrategy();
		
		
		Set<String> remainedGeneIdSet = new HashSet<String>();
		//remainedGeneIdSet.addAll(associations.geneMap.keySet());
		remainedGeneIdSet.addAll(associations.disorderMap.get(diseaseOmimId).geneMap.keySet());
		
		for(Gene gene: associations.disorderMap.get(diseaseOmimId).geneMap.values()){
			//若该基因不在PPI网络上，则无需做交叉验证
			if(!g.containsNode(gene.getGeneNameInPPI()))continue;
			
			System.out.println(gene.getGeneNameInPPI());
			
			remainedGeneIdSet.remove(gene.getGeneNameInPPI());
			
			double[] start = strategy.getStartProbability(g, remainedGeneIdSet);
			
			PageRank pr = new PageRank();
			pr.setMatrix(normalizedMatrix);
			pr.setStartProbability(start);
			pr.run();
			double[] rankScores = pr.getRankScores();
			
			String outputDirPath_2 = outputDirPath + "/" + gene.getGeneNameInPPI();
			FileUtil.makeDir(new File(outputDirPath_2));
			
			WriterUtil.write(outputDirPath_2 + "/PRP_ranks[v].txt",
					RankUtil.array2String(rankScores, g));
			
			remainedGeneIdSet.add(gene.getGeneNameInPPI());
		}
	}
	
	private Set<String> readDiseaseFile(){
		Set<String> diseaseOmimIdSet = new HashSet<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(diseaseFilepath)));
			String line = null;
			
			while((line = in.readLine()) != null){
				diseaseOmimIdSet.add(line);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return diseaseOmimIdSet;
	}
	
	public void prioritizing(){
		AdjacencyGraph g = new DirectedAdjacencyGraph();
		GraphReader.read(ppiFilepath, g);
		
		double[][] normalizedMatrix = NormalizeWeightOfPPI.nomalizedWeightMatrix(g);
		
		GeneDiseaseAssociation associations = new GeneDiseaseAssociation(hprdIdMapppingFilepath);
		associations.read(geneDiseaseAssociationFilepath);
		
		Set<String> diseaseOmimIdSet = readDiseaseFile();
		
		for(String diseaseOmimId: diseaseOmimIdSet){
			prioritizing_one_disease(diseaseOmimId, associations, normalizedMatrix, g);
		}
	}
	
	public void prioritizing_one_disease(String diseaseOmimId, GeneDiseaseAssociation associations, double[][] normalizedMatrix, AdjacencyGraph g){
		System.out.println("Disease OmimId: " + diseaseOmimId);
		
		PageRankPriorStartProbabilityStrategy strategy = new PageRankPriorStartProbabilityStrategy();
		double[] start = strategy.getStartProbability(g, associations.disorderMap.get(diseaseOmimId).geneMap.keySet());
		
		PageRank pr = new PageRank();
		pr.setMatrix(normalizedMatrix);
		pr.setStartProbability(start);
		pr.run();
		double[] rankScores = pr.getRankScores();
		
		String outputDirPath = outputDir + "/" + diseaseOmimId;
		FileUtil.makeDir(new File(outputDirPath));
		
		WriterUtil.write(outputDirPath + "/PRP_ranks[p].txt",
				RankUtil.array2String(rankScores, g));
	}
	
	
	public static void main(String[] args){
		if(args.length != 1){
			System.out.println("Argument Error.");
			System.out.println("Using method: java -Xmx2048m -jar PRP_Prioritization.jar ./input/config.txt");
			System.exit(-1);
		}
		
		InputArgument input = new InputArgument(args[0]);
		
		PageRankPriosCrossValidation validation = new PageRankPriosCrossValidation(input.getPpiFilepath(),
				input.getGeneDiseaseAssociationFilepath(),
				input.getDiseaseFilepath(), input.getOutputDir(), input.getHprdIdMappingsFileName());
		
		System.out.println("PageRankPrios Prioritization starting...");
		
		//validation.batch_run();
		validation.prioritizing();
		
		//MyLogger.log(args[0] + "---> completed.");
		System.out.println("PageRankPrios Prioritization finished...");
	}
	
}
