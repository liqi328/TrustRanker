package diseasefamily;

import java.util.HashMap;
import java.util.Map;

public class DiseaseFamily{
	private String name;
	private String description;
	
	/* geneOmimId - disorderOmimId*/
	private Map<String, String> gene2disorderMap;
	
	private Map<String, Gene> geneMap;
	private Map<String, Disorder> disorderMap;
	
	public DiseaseFamily(String name, String description){
		this.name = name;
		this.description = description;
		
		gene2disorderMap = new HashMap<String, String>();
		geneMap = new HashMap<String, Gene>();
		disorderMap = new HashMap<String, Disorder>();
	}
	
	/* 添加基因与疾病的关系, add gene-disease association*/
	public void addGene2DisorderAssociation(Gene gene, Disorder disorder){
		gene2disorderMap.put(gene.getOmimId(), disorder.getOmimId());
		
		geneMap.put(gene.getOmimId(), gene);
		disorderMap.put(disorder.getOmimId(), disorder);
	}
	
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public Map<String, String> getGene2disorderMap() {
		return gene2disorderMap;
	}
	public Map<String, Gene> getGeneMap() {
		return geneMap;
	}
	public Map<String, Disorder> getDisorderMap() {
		return disorderMap;
	}
}