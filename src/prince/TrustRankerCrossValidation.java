package prince;

import graph.AdjacencyGraph;
import graph.DirectedAdjacencyGraph;
import graph.GraphReader;
import hdnANDdgn.HumanDiseaseNetwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import rank.RankUtil;
import util.FileUtil;
import util.MyLogger;
import util.WriterUtil;
import diseasefamily.Disorder;
import diseasefamily.Gene;
import diseasefamily.GeneDiseaseAssociation;
import diseasesimilarity.CombinedDiseaseDiseaseSimilarity;
import diseasesimilarity.DiseaseDiseasePhenotypeSimilarity;
import diseasesimilarity.DiseaseDiseaseToplogySimilarity;

public class TrustRankerCrossValidation {
	private String ppiFilepath;
	private String diseaseSimilarityFilepath;
	private String geneDiseaseAssociationFilepath;
	private String diseaseFilepath;
	private String[] alphaArray;
	private String outputDir;
	
	public TrustRankerCrossValidation(String ppiFilepath, String diseaseSimilarityFilepath,
			String geneDiseaseAssociationFilepath, String diseaseFilepath, String outputDir, String[] alphaArray){
		this.ppiFilepath = ppiFilepath;
		this.diseaseSimilarityFilepath = diseaseSimilarityFilepath;
		this.geneDiseaseAssociationFilepath = geneDiseaseAssociationFilepath;
		this.diseaseFilepath = diseaseFilepath;
		this.alphaArray = alphaArray;
		this.outputDir = outputDir;
	}
	
	public void batch_run(){
		AdjacencyGraph g = new DirectedAdjacencyGraph();
		GraphReader.read(ppiFilepath, g);
		
		double[][] normalizedMatrix = NormalizeWeightOfPPI.nomalizedWeightMatrix(g);
		
		GeneDiseaseAssociation associations = new GeneDiseaseAssociation();
		associations.read(geneDiseaseAssociationFilepath);
		
		HumanDiseaseNetwork humanDiseaseNetwork = new HumanDiseaseNetwork();
		humanDiseaseNetwork.create(associations);
		DiseaseDiseaseToplogySimilarity toplogySimilarity = new DiseaseDiseaseToplogySimilarity(humanDiseaseNetwork);
		
		DiseaseDiseasePhenotypeSimilarity phenotypeSimilarity = new DiseaseDiseasePhenotypeSimilarity(diseaseSimilarityFilepath);
		
		//phenotypeSimilarity.addDiseaseDiseaseSimilarity(toplogySimilarity, 0.5);
		
		Set<String> diseaseOmimIdSet = readDiseaseFile();
		
		//String[] alphaArray = new String[]{"1.0", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9"};
		
		for(String diseaseOmimId: diseaseOmimIdSet){
			for(String alpha: alphaArray){
				CombinedDiseaseDiseaseSimilarity combinedDiseaseSimilarity = new CombinedDiseaseDiseaseSimilarity(
						phenotypeSimilarity, toplogySimilarity, Double.parseDouble(alpha));
				PrinceStartProbabilityStrategy strategy = new PrinceStartProbabilityStrategy(g,
						combinedDiseaseSimilarity, associations);
				
				PrinceRunner runner = new PrinceRunner(normalizedMatrix, strategy);
				run_one_disease(diseaseOmimId, associations, runner, g, alpha);
			}
		}
	}
	
	public void run_one_disease(String diseaseOmimId, GeneDiseaseAssociation associations,
			PrinceRunner runner, AdjacencyGraph g, String alpha){
		System.out.println("Disease OmimId: " + diseaseOmimId + ", alpha = " + alpha);
		Map<String, Disorder> tmpDisorderMap = new HashMap<String, Disorder>();
		
		String outputDirPath = outputDir + "/" + diseaseOmimId;
		FileUtil.makeDir(new File(outputDirPath));                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
		
		//System.out.println("Gene number: " + associations.disorderMap.get(diseaseOmimId).geneMap.size());
		
		for(Gene gene: associations.disorderMap.get(diseaseOmimId).geneMap.values()){
			//若该基因不在PPI网络上，则无需做交叉验证
			if(!g.containsNode(gene.getGeneNameInPPI()))continue;
			
			tmpDisorderMap.putAll(gene.disorderMap);
			gene.disorderMap.clear();
			
			double[] rankScores = runner.run(diseaseOmimId);
			
			String outputDirPath_2 = outputDirPath + "/" + gene.getGeneNameInPPI();
			FileUtil.makeDir(new File(outputDirPath_2));
			
			WriterUtil.write(outputDirPath_2 + "/TRer_ranks[v]_" + alpha + ".txt",
					RankUtil.array2String(rankScores, g));
			
			gene.disorderMap.putAll(tmpDisorderMap);
			tmpDisorderMap.clear();
		}
	}
	
	public void prioritizing(){
		AdjacencyGraph g = new DirectedAdjacencyGraph();
		GraphReader.read(ppiFilepath, g);
		
		double[][] normalizedMatrix = NormalizeWeightOfPPI.nomalizedWeightMatrix(g);
		
		GeneDiseaseAssociation associations = new GeneDiseaseAssociation();
		associations.read(geneDiseaseAssociationFilepath);
		
		HumanDiseaseNetwork humanDiseaseNetwork = new HumanDiseaseNetwork();
		humanDiseaseNetwork.create(associations);
		DiseaseDiseaseToplogySimilarity toplogySimilarity = new DiseaseDiseaseToplogySimilarity(humanDiseaseNetwork);
		
		DiseaseDiseasePhenotypeSimilarity phenotypeSimilarity = new DiseaseDiseasePhenotypeSimilarity(diseaseSimilarityFilepath);
		
		Set<String> diseaseOmimIdSet = readDiseaseFile();
		
		for(String diseaseOmimId: diseaseOmimIdSet){
			for(String alpha: alphaArray){
				CombinedDiseaseDiseaseSimilarity combinedDiseaseSimilarity = new CombinedDiseaseDiseaseSimilarity(
						phenotypeSimilarity, toplogySimilarity, Double.parseDouble(alpha));
				PrinceStartProbabilityStrategy strategy = new PrinceStartProbabilityStrategy(g,
						combinedDiseaseSimilarity, associations);
				
				PrinceRunner runner = new PrinceRunner(normalizedMatrix, strategy);
				prioritizing_one_disease(diseaseOmimId, associations, runner, g, alpha);
			}
		}
	}
	
	

	
	public void prioritizing_one_disease(String diseaseOmimId, GeneDiseaseAssociation associations,
			PrinceRunner runner, AdjacencyGraph g, String alpha){
		System.out.println("Disease OmimId: " + diseaseOmimId + ", alpha = " + alpha);
		
		String outputDirPath = outputDir + "/" + diseaseOmimId;
		FileUtil.makeDir(new File(outputDirPath));
		
		double[] rankScores = runner.run(diseaseOmimId);
		
		WriterUtil.write(outputDirPath + "/TRer_ranks[p]_" + alpha + ".txt",
				RankUtil.array2String(rankScores, g));
		
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
	
	
	public static void main(String[] args){
		if(args.length != 1){
			System.out.println("Argument Error.");
			System.out.println("Using method: java -Xmx2048m -jar TRer.jar ./input/config.txt");
			System.exit(-1);
		}
		//String config = "E:/2013疾病研究/实验数据/TrustRanker/   omim_disease/input/myprinceconfig.txt";
		InputArgument input = new InputArgument(args[0]);
//		for(String alpha: input.getAthreshholdArray()){
//			System.out.println(alpha); 
//		}
		TrustRankerCrossValidation validation = new TrustRankerCrossValidation(input.getPpiFilepath(),
				input.getDiseaseSimilarityFilepath(), input.getGeneDiseaseAssociationFilepath(),
				input.getDiseaseFilepath(), input.getOutputDir(), input.getAthreshholdArray());
		System.out.println("MyPrince Validation starting...");
		
		validation.batch_run();
		//validation.prioritizing();
		//MyLogger.log(args[0] + "---> completed.");
		System.out.println("MyPrince Validation finished...");
	}
}
