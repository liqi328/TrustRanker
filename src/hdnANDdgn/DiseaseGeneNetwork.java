package hdnANDdgn;

import graph.AdjacencyGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import util.WriterUtil;
import diseasefamily.Gene;
import diseasefamily.GeneDiseaseAssociation;

public class DiseaseGeneNetwork {
	private AdjacencyGraph network;
	
	public DiseaseGeneNetwork(){
		network = new AdjacencyGraph();
	}
	
	public AdjacencyGraph getAdjacencyGraph(){
		return network;
	}
	
	public void create(GeneDiseaseAssociation associations){
		Map<String, Gene> geneMap = associations.geneMap;
		
		for(Gene gene: geneMap.values()){
			network.addNode(gene.getHprdId());
		}
		
		int weight = 0;
		int maxWeight = 0;
		Set<String> calculatedGene = new HashSet<String>();
		Map<Integer, Integer> weightMap = new HashMap<Integer, Integer>();
		for(Gene firstGene: geneMap.values()){
			calculatedGene.add(firstGene.getHprdId());
			for(Gene secondGene: geneMap.values()){
				if(calculatedGene.contains(secondGene.getHprdId())){
					continue;
				}
				
				weight = commomDisorderBetweenTwoGene(firstGene, secondGene);
				if(weight == 0)continue;
				
				if(weightMap.get(weight) == null){
					weightMap.put(weight, 1);
				}else{
					weightMap.put(weight, weightMap.get(weight) + 1);
				}
				
				if(weight >= 2){
					//System.out.println(firstGene.getHprdId() + "\t" + secondGene.getHprdId() + "\t" + weight);
				}
				
				if(weight > maxWeight)maxWeight = weight;
				network.addEdge(firstGene.getHprdId(), secondGene.getHprdId(), weight);
			}
		}
		System.out.println(maxWeight);
		for(Integer w: weightMap.keySet()){
			System.out.println(w + ": " + weightMap.get(w));
		}
	}
	
	public String toString(){
		return network.graphToString();
	}
	
	private int commomDisorderBetweenTwoGene(Gene firstGene, Gene secondGene){
		Set<String> commonDisorderSet = new HashSet<String>();
		commonDisorderSet.addAll(firstGene.disorderMap.keySet());
		
		int count = 0;
		
		for(String disorder: secondGene.disorderMap.keySet()){
			if(commonDisorderSet.contains(disorder)){
				++count;
			}
		}
		
		return count;
	}
	
	public static void main(String[] args){
		String geneDiseaseAssociationFilepath = "E:/2013疾病研究/疾病数据/OMIM/geneOmimId_diseaseOmimId.txt";
		GeneDiseaseAssociation associations = new GeneDiseaseAssociation();
		associations.read(geneDiseaseAssociationFilepath);
		
		DiseaseGeneNetwork network = new DiseaseGeneNetwork();
		network.create(associations);
		
		WriterUtil.write("E:/2013疾病研究/实验数据/TrustRanker/hdnAnddgn/diseaseGeneNetwork.txt",
				network.toString());
	}
}
