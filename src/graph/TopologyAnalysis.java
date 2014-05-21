package graph;

import hdnANDdgn.DiseaseGeneNetwork;
import hdnANDdgn.HumanDiseaseNetwork;
import diseasefamily.GeneDiseaseAssociation;

public class TopologyAnalysis {
	public static void componentSize(AdjacencyGraph g){
		System.out.println(GraphHelper.countConnectedComponent(g));
	}
	
	
	public static void humanDiseaseNetworkAnalysis(){
		//String geneDiseaseAssociationFilepath = "E:/2013疾病研究/疾病数据/OMIM/geneOmimId_diseaseOmimId.txt";
		String geneDiseaseAssociationFilepath = "E:/2013疾病研究/实验数据/SP_TrustRanker比较/input/orphanet_diseases.txt";
		GeneDiseaseAssociation associations = new GeneDiseaseAssociation();
		associations.read(geneDiseaseAssociationFilepath);
		
		HumanDiseaseNetwork humanNetwork = new HumanDiseaseNetwork();
		humanNetwork.create(associations);
		
		componentSize(humanNetwork.getAdjacencyGraph());
		
		System.out.println(GraphHelper.degreeDistribution2String(humanNetwork.getAdjacencyGraph()));
	}
	
	public static void diseaseGeneNetworkAnalysis(){
		String geneDiseaseAssociationFilepath = "E:/2013疾病研究/疾病数据/OMIM/geneOmimId_diseaseOmimId.txt";
		GeneDiseaseAssociation associations = new GeneDiseaseAssociation();
		associations.read(geneDiseaseAssociationFilepath);
		
		DiseaseGeneNetwork network = new DiseaseGeneNetwork();
		network.create(associations);
		
		//componentSize(network.getAdjacencyGraph());
		
		System.out.println(GraphHelper.degreeDistribution2String(network.getAdjacencyGraph()));
	}
	
	public static void main(String[] args){
		humanDiseaseNetworkAnalysis();
		//diseaseGeneNetworkAnalysis();
	}
}
