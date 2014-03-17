package diseasefamily;

import graph.AdjacencyGraph;
import graph.DirectedAdjacencyGraph;
import graph.GraphReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import util.WriterUtil;

public class GeneDiseaseAssociation {
	public final Map<String, Gene> geneMap = new HashMap<String, Gene>();
	public final Map<String, Disorder> disorderMap = new HashMap<String, Disorder>();
	
	public void read(){
		String geneDiseaseAssociationFilepath = "E:/2013疾病研究/疾病数据/OMIM/geneOmimId_diseaseOmimId_in_ppi.txt";
		
		read(geneDiseaseAssociationFilepath);
	}
	
	public void read(String geneDiseaseAssociationFilepath){
		
		Map<String, HprdIdMapping> omimIdIndexedIdMappingMap = HprdIdMappingUtil.getOmimIdIndexedIdMapping();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(geneDiseaseAssociationFilepath)));
			String line = null;
			HprdIdMapping hprdIdMapping = null;
			String[] cols = null;
			line = in.readLine();
			
			Disorder disorder = null;
			Gene gene = null;
			
			while((line = in.readLine()) != null){
				cols = line.split("\t");
				hprdIdMapping = omimIdIndexedIdMappingMap.get(cols[0]);
				if(hprdIdMapping == null){
					continue;
				}
				
				disorder = disorderMap.get(cols[1]);
				if(disorder == null){
					disorder = new Disorder(cols[1], cols[2]);
				}
				
				gene = geneMap.get(hprdIdMapping.getHrpdId());
				if(gene == null){
					gene = new Gene(cols[0], hprdIdMapping.getGeneSymbol(), hprdIdMapping.getMainName(),
							hprdIdMapping.getHrpdId());
				}
				
				gene.disorderMap.put(disorder.getOmimId(), disorder);
				disorder.geneMap.put(gene.getHprdId(), gene);
				
				geneMap.put(gene.getHprdId(), gene);
				disorderMap.put(disorder.getOmimId(), disorder);
			}
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String geneMap2String(){
		StringBuffer sb = new StringBuffer();
		
		for(Gene gene: geneMap.values()){
			sb.append(gene.getHprdId()).append("\t");
			sb.append(gene.getOmimId()).append("\t");
			for(Disorder disorder: gene.disorderMap.values()){
				sb.append(disorder.getOmimId()).append(",");
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public String disorderMap2String(){
		StringBuffer sb = new StringBuffer();
		
		for(Disorder disorder: disorderMap.values()){
			if(disorder.geneMap.size() < 3)continue;
			sb.append(disorder.getOmimId()).append("\t");
			sb.append(disorder.getName()).append("\t");
			for(Gene gene: disorder.geneMap.values()){
				sb.append(gene.getOmimId()).append(",");
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	private void processGeneDiseaseAssociationFile(){
		String geneDiseaseAssociationFilepath = "E:/2013疾病研究/疾病数据/OMIM/geneOmimId_diseaseOmimId.txt";
		Map<String, HprdIdMapping> omimIdIndexedIdMappingMap = HprdIdMappingUtil.getOmimIdIndexedIdMapping();
		
		String ppiFilename = "E:/2013疾病研究/实验数据/TrustRanker/omim_disease/input/HPRD_ppi_weighted.txt";
		AdjacencyGraph g = new DirectedAdjacencyGraph();
		GraphReader.read(ppiFilename, g);
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(geneDiseaseAssociationFilepath)));
			String line = null;
			StringBuffer sb = new StringBuffer();
			String[] cols = null;
			HprdIdMapping hprdIdMapping = null;
			
			while((line = in.readLine()) != null){
				cols = line.split("\t");
				hprdIdMapping = omimIdIndexedIdMappingMap.get(cols[0]);
				if(hprdIdMapping == null){
					continue;
				}
				
				if(!g.containsNode(hprdIdMapping.getHrpdId())){
					continue;
				}
				
				sb.append(line).append("\n");
			}
			
			in.close();
			
			WriterUtil.write("E:/2013疾病研究/疾病数据/OMIM/geneOmimId_diseaseOmimId_in_ppi.txt", sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		GeneDiseaseAssociation associations = new GeneDiseaseAssociation();
		associations.read();
		
		System.out.println("Gene number: " + associations.geneMap.size());
		System.out.println("Disorder number: " + associations.disorderMap.size());
		
		WriterUtil.write("E:/2013疾病研究/疾病数据/OMIM/gene2disorder.txt",
				associations.geneMap2String());
		
		WriterUtil.write("E:/2013疾病研究/疾病数据/OMIM/disorder2gene.txt",
				associations.disorderMap2String());
		
		//associations.processGeneDiseaseAssociationFile();
	}
}
