package hdnANDdgn;

import java.util.Map;

import util.WriterUtil;
import diseasefamily.GeneDiseaseAssociation;

public class DiseasomeBipartiteNetwork {
	private DiseaseGeneNetwork diseaseGeneNetwork;
	private HumanDiseaseNetwork humanDiseaseNetwork;
	
	private GeneDiseaseAssociation associations;
	
	private Map<String, Map<String, Double>> m;
	
	public DiseasomeBipartiteNetwork(DiseaseGeneNetwork diseaseGeneNetwork, HumanDiseaseNetwork humanDiseaseNetwork){
		this.diseaseGeneNetwork = diseaseGeneNetwork;
		this.humanDiseaseNetwork = humanDiseaseNetwork;
	}
	
	public DiseasomeBipartiteNetwork(GeneDiseaseAssociation associations){
		this.associations = associations;
	}
	
	public void create(GeneDiseaseAssociation associations){

	}
	
	public static void main(String[] args){
		String geneDiseaseAssociationFilepath = "E:/2013疾病研究/疾病数据/OMIM/geneOmimId_diseaseOmimId.txt";
		GeneDiseaseAssociation associations = new GeneDiseaseAssociation();
		associations.read(geneDiseaseAssociationFilepath);
		
		HumanDiseaseNetwork network = new HumanDiseaseNetwork();
		network.create(associations);
		
		WriterUtil.write("E:/2013疾病研究/实验数据/TrustRanker/hdnAnddgn/humanDiseaseNetwork.txt",
				network.toString());
	}
}
