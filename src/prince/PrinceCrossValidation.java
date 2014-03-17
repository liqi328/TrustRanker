package prince;

import graph.AdjacencyGraph;
import graph.DirectedAdjacencyGraph;
import graph.GraphReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import rank.RankUtil;
import util.FileUtil;
import util.WriterUtil;
import diseasefamily.DiseaseSimilarity;
import diseasefamily.Disorder;
import diseasefamily.Gene;
import diseasefamily.GeneDiseaseAssociation;


class InputArgument{
	private Properties p = new Properties();
	
	public InputArgument(String configFilepath){
		try {
			InputStreamReader is = new InputStreamReader(new FileInputStream(configFilepath), "UTF-8");
			p.load(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getPpiFilepath(){
		return p.getProperty("ppiFilepath");
	}
	
	public String getDiseaseSimilarityFilepath(){
		return p.getProperty("diseaseSimilarityFilepath");
	}
	
	public String getGeneDiseaseAssociationFilepath(){
		return p.getProperty("geneDiseaseAssociationFilepath");
	}
	
	public String getDiseaseFilepath(){
		return p.getProperty("diseaseFilepath");
	}
	
	public String getOutputDir(){
		File outputDir = new File(p.getProperty("outputDir"));
		if(!outputDir.exists()){
			outputDir.mkdirs();
		}
		return p.getProperty("outputDir");
	}
}

public class PrinceCrossValidation {
	private String ppiFilepath;
	private String diseaseSimilarityFilepath;
	private String geneDiseaseAssociationFilepath;
	private String diseaseFilepath;
	
	private String outputDir;
	
	public PrinceCrossValidation(String ppiFilepath, String diseaseSimilarityFilepath,
			String geneDiseaseAssociationFilepath, String diseaseFilepath, String outputDir){
		this.ppiFilepath = ppiFilepath;
		this.diseaseSimilarityFilepath = diseaseSimilarityFilepath;
		this.geneDiseaseAssociationFilepath = geneDiseaseAssociationFilepath;
		this.diseaseFilepath = diseaseFilepath;
		this.outputDir = outputDir;
	}
	
	public void batch_run(){
		AdjacencyGraph g = new DirectedAdjacencyGraph();
		GraphReader.read(ppiFilepath, g);
		
		double[][] normalizedMatrix = NormalizeWeightOfPPI.nomalizedWeightMatrix(g);
		
		DiseaseSimilarity diseaseSimilarity = new DiseaseSimilarity();
		diseaseSimilarity.read(diseaseSimilarityFilepath);
		
		GeneDiseaseAssociation associations = new GeneDiseaseAssociation();
		associations.read(geneDiseaseAssociationFilepath);
		
		PrinceStartProbabilityStrategy strategy = new PrinceStartProbabilityStrategy(g,
				diseaseSimilarity, associations);
		
		PrinceRunner runner = new PrinceRunner(normalizedMatrix, strategy);
		
		Set<String> diseaseOmimIdSet = readDiseaseFile();
		
		for(String diseaseOmimId: diseaseOmimIdSet){
			run_one_disease(diseaseOmimId, associations, runner, g);
		}
	}
	
	public void run_one_disease(String diseaseOmimId, GeneDiseaseAssociation associations,
			PrinceRunner runner, AdjacencyGraph g){
		System.out.println("Disease: " + diseaseOmimId);
		Map<String, Disorder> tmpDisorderMap = new HashMap<String, Disorder>();
		
		String outputDirPath = outputDir + "/" + diseaseOmimId;
		FileUtil.makeDir(new File(outputDirPath));
		
		System.out.println("genes: " + associations.disorderMap.get(diseaseOmimId).geneMap.size());
		
		for(Gene gene: associations.disorderMap.get(diseaseOmimId).geneMap.values()){
			System.out.println("-->" + gene.getHprdId());
			tmpDisorderMap.putAll(gene.disorderMap);
			gene.disorderMap.clear();
			
			double[] rankScores = runner.run(diseaseOmimId);
			
			String outputDirPath_2 = outputDirPath + "/" + gene.getHprdId();
			FileUtil.makeDir(new File(outputDirPath_2));
			
			WriterUtil.write(outputDirPath_2 + "/prince_ranks.txt",
					RankUtil.array2String(rankScores, g));
			
			gene.disorderMap.putAll(tmpDisorderMap);
			tmpDisorderMap.clear();
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
	
	
	public static void main(String[] args){
		if(args.length != 1){
			System.out.println("Argument Error.");
			System.out.println("Using method: java -Xmx2048m -jar prince.jar ./input/config.txt");
			System.exit(-1);
		}
		
		InputArgument input = new InputArgument(args[0]);
		
		PrinceCrossValidation validation = new PrinceCrossValidation(input.getPpiFilepath(),
				input.getDiseaseSimilarityFilepath(), input.getGeneDiseaseAssociationFilepath(),
				input.getDiseaseFilepath(), input.getOutputDir());
		System.out.println("Prince Validation starting...");
		validation.batch_run();
		System.out.println("Prince Validation finished...");
	}
	
}
