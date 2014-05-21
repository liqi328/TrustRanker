package hdnANDdgn;

import graph.AdjacencyGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import util.WriterUtil;
import diseasefamily.Disorder;
import diseasefamily.GeneDiseaseAssociation;

public class HumanDiseaseNetwork {
	private AdjacencyGraph network;
	
	public HumanDiseaseNetwork(){
		network = new AdjacencyGraph();
	}
	
	public AdjacencyGraph getAdjacencyGraph(){
		return network;
	}
	
	public void create(GeneDiseaseAssociation associations){
		Map<String, Disorder> disorderMap = associations.disorderMap;
		
		for(String disorderOmimId: disorderMap.keySet()){
			network.addNode(disorderOmimId);
		}
		
		int weight = 0;
		int maxWeight = 0;
		Set<String> calculatedDisorder = new HashSet<String>();
		Map<Integer, Integer> weightMap = new HashMap<Integer, Integer>();
		for(Disorder firstDisorder: disorderMap.values()){
			calculatedDisorder.add(firstDisorder.getOmimId());
			for(Disorder secondDisorder: disorderMap.values()){
				if(calculatedDisorder.contains(secondDisorder.getOmimId())){
					continue;
				}
				
				weight = commomDiseaseGeneBetweenTwoDisorder(firstDisorder, secondDisorder);
				if(weight == 0)continue;
				
				if(weightMap.get(weight) == null){
					weightMap.put(weight, 1);
				}else{
					weightMap.put(weight, weightMap.get(weight) + 1);
				}
				
				if(weight >= 9){
					System.out.println(firstDisorder.getOmimId() + "\t" + secondDisorder.getOmimId() + "\t" + weight);
				}
				
				if(weight > maxWeight)maxWeight = weight;
				network.addEdge(firstDisorder.getOmimId(), secondDisorder.getOmimId(), weight);
			}
		}
		System.out.println("HumanDiseaseNetwork.create(): max weight" + maxWeight);
		for(Integer w: weightMap.keySet()){
			System.out.println(w + ": " + weightMap.get(w));
		}
	}
	
	public String toString(){
		return network.graphToString();
	}
	
	private int commomDiseaseGeneBetweenTwoDisorder(Disorder firstDisorder, Disorder secondDisorder){
		Set<String> commonGeneSet = new HashSet<String>();
		commonGeneSet.addAll(firstDisorder.geneMap.keySet());
		
		int count = 0;
		
		for(String gene: secondDisorder.geneMap.keySet()){
			if(commonGeneSet.contains(gene)){
				++count;
			}
		}
		
		return count;
	}
	
	public static void main(String[] args){
		//String geneDiseaseAssociationFilepath = "E:/2013疾病研究/疾病数据/OMIM/geneOmimId_diseaseOmimId.txt";
		//String geneDiseaseAssociationFilepath = "E:/2013疾病研究/疾病数据/OMIM/geneEntrezId_diseaseOmimId.txt";
		String geneDiseaseAssociationFilepath = "E:/2013疾病研究/实验数据/SP_TrustRanker比较/input/orphanet_diseases.txt";
		
		GeneDiseaseAssociation associations = new GeneDiseaseAssociation();
		associations.read(geneDiseaseAssociationFilepath);
		
		HumanDiseaseNetwork network = new HumanDiseaseNetwork();
		network.create(associations);
		
		WriterUtil.write("E:/2013疾病研究/实验数据/SP_TrustRanker比较/input/humanDiseaseNetwork.txt",
				network.toString());
	}
}
