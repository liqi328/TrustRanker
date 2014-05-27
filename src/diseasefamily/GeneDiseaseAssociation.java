package diseasefamily;

import graph.AdjacencyGraph;
import graph.DirectedAdjacencyGraph;
import graph.GraphReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import util.WriterUtil;

public class GeneDiseaseAssociation {
	public final Map<String, Gene> geneMap = new HashMap<String, Gene>();
	public final Map<String, Disorder> disorderMap = new HashMap<String, Disorder>();
	
	private String hprdIdMapppingFilepath = "E:/2013疾病研究/疾病数据/HumanPPI/HPRD_Release9_062910/FLAT_FILES_072010/HPRD_ID_MAPPINGS.txt";
	
	public GeneDiseaseAssociation(){

	}
	
	public GeneDiseaseAssociation(String hprdIdMapppingFilepath2){
		hprdIdMapppingFilepath = hprdIdMapppingFilepath2;
	}
	
	public void read(){
		//String geneDiseaseAssociationFilepath = "E:/2013疾病研究/疾病数据/OMIM/geneOmimId_diseaseOmimId_in_ppi.txt";
		String geneDiseaseAssociationFilepath = "E:/2013疾病研究/疾病数据/OMIM/geneOmimId_diseaseOmimId.txt";
		
		read(geneDiseaseAssociationFilepath);
	}
	
	public void read(String geneDiseaseAssociationFilepath){
		HprdIdMappingUtil.setHprdIdMappingFilePath(hprdIdMapppingFilepath);
		Map<String, HprdIdMapping> entrezIdIndexedIdMappingMap = HprdIdMappingUtil.getEntrezIdIndexedIdMapping();
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
				
				disorder = disorderMap.get(cols[1]);
				if(disorder == null){
					disorder = new Disorder(cols[1], cols[2]);
				}
				String flag = "ENTREZ_ID";
				hprdIdMapping = entrezIdIndexedIdMappingMap.get(cols[0]);
				if(hprdIdMapping == null){
					hprdIdMapping = omimIdIndexedIdMappingMap.get(cols[0]);
					flag = "OMIM_ID";
					
					if(hprdIdMapping == null){
						//System.out.println(line);
						continue;
					}
				}
				
				gene = geneMap.get(cols[0]);
				if(gene == null){
					gene = new Gene(hprdIdMapping.getOmimId(), hprdIdMapping.getGeneSymbol(), hprdIdMapping.getMainName(),
							hprdIdMapping.getHrpdId());
					if("ENTREZ_ID".equals(flag)){
						gene.setGeneNameInPPI(cols[0]);
						//System.out.println("ENTREZ_ID");
					}else if("OMIM_ID".equals(flag)){
						gene.setGeneNameInPPI(gene.getHprdId());
						//System.out.println("OMIM_ID");
					}
					
				}
				
				gene.disorderMap.put(disorder.getOmimId(), disorder);
				disorder.geneMap.put(gene.getGeneNameInPPI(), gene);
				
				geneMap.put(gene.getGeneNameInPPI(), gene);
				disorderMap.put(disorder.getOmimId(), disorder);
			}
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readXX(String geneDiseaseAssociationFilepath){
		
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
			sb.append(gene.disorderMap.size()).append("\t");
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
			sb.append(disorder.geneMap.size()).append("\t");
			for(Gene gene: disorder.geneMap.values()){
				sb.append(gene.getHprdId()).append(",");
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public String disorderMap2String3(){
		StringBuffer sb = new StringBuffer();
		Map<String, HprdIdMapping> hprdIdIndexedIdMappingMap = HprdIdMappingUtil.getHprdIdIndexIdMapping();
		
		for(Disorder disorder: disorderMap.values()){
			if(disorder.geneMap.size() < 3)continue;
			sb.append(disorder.getOmimId()).append("\t");
			sb.append(disorder.getName()).append("\t");
			sb.append(disorder.geneMap.size()).append("\t");
			for(Gene gene: disorder.geneMap.values()){
				//sb.append(gene.getHprdId()).append(",");
				//sb.append(hprdIdIndexedIdMappingMap.get(gene.getHprdId()).getGeneSymbol()).append(",");
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public void writeDisorderMap2File(){
		
		Map<String, HprdIdMapping> hprdIdIndexedIdMappingMap = HprdIdMappingUtil.getHprdIdIndexIdMapping();
		
		Set<String> selectedDisorder = new HashSet<String>();
		selectedDisorder.add("125853");
		selectedDisorder.add("176807");
		selectedDisorder.add("211980");
		selectedDisorder.add("104300");
		selectedDisorder.add("114480");
		selectedDisorder.add("114500");
		selectedDisorder.add("601626");
		
		String output = "E:/2013疾病研究/疾病数据/OMIM/disease/";
		
		for(Disorder disorder: disorderMap.values()){
			if(selectedDisorder.contains(disorder.getOmimId())){
				StringBuffer sb = new StringBuffer();
//				sb.append(disorder.getOmimId()).append("\t");
//				sb.append(disorder.getName()).append("\t");
//				sb.append(disorder.geneMap.size()).append("\n");
				for(Gene gene: disorder.geneMap.values()){
					//sb.append(gene.getHprdId()).append("\t");
					sb.append(hprdIdIndexedIdMappingMap.get(gene.getHprdId()).getEntrezGeneId()).append("\n");
				}
				
				WriterUtil.write(output + disorder.getOmimId() + ".txt", 
						sb.toString());
			}
			
		}
	}
	
	
	public String disorderMap2String2(){
		StringBuffer sb = new StringBuffer();
		
		for(Disorder disorder: disorderMap.values()){
			for(Gene gene: disorder.geneMap.values()){
				sb.append(disorder.getOmimId()).append("\t");
				sb.append(gene.getHprdId()).append("\n");
			}
		}
		
		return sb.toString();
	}
	
	public void selectedDisorder2String(){
		Set<String> selectedDiseasesSet = new HashSet<String>();
		selectedDiseasesSet.add("104300");
		selectedDiseasesSet.add("176807");
		selectedDiseasesSet.add("125853");
		
		selectedDisorder2String(selectedDiseasesSet);
	}
	
	public void selectedDisorder2String(Set<String> selectedDiseasesSet){
		String outputFilepath = "E:/2013疾病研究/实验数据/Prince/input/";
		
		for(String diseaseOmimId: selectedDiseasesSet){
			Disorder disorder = disorderMap.get(diseaseOmimId);
			for(Gene gene: disorder.geneMap.values()){
				File file = new File(outputFilepath + disorder.getOmimId());
				if(!file.exists()){
					file.mkdirs();
				}
				
				WriterUtil.write(outputFilepath + disorder.getOmimId() + "/" + gene.getHprdId() + ".txt", 
						removeOneDiseaseGeneAssociation(gene));
			}
		}
	}
	
	private String removeOneDiseaseGeneAssociation(Gene removedGene){
		StringBuffer sb = new StringBuffer();
		
		for(Disorder disorder: disorderMap.values()){
			for(Gene gene: disorder.geneMap.values()){
				if(gene.getHprdId().equals(removedGene.getHprdId()))continue;
				
				sb.append(disorder.getOmimId()).append("\t");
				sb.append(gene.getHprdId()).append("\n");
			}
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
	
	private void processGeneDiseaseAssociationFile2entrezID(){
		String geneDiseaseAssociationFilepath = "E:/2013疾病研究/疾病数据/OMIM/geneOmimId_diseaseOmimId.txt";
		Map<String, HprdIdMapping> omimIdIndexedIdMappingMap = HprdIdMappingUtil.getOmimIdIndexedIdMapping();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(geneDiseaseAssociationFilepath)));
			String line = in.readLine();
			StringBuffer sb = new StringBuffer();
			String[] cols = null;
			HprdIdMapping hprdIdMapping = null;
			
			while((line = in.readLine()) != null){
				cols = line.split("\t");
				hprdIdMapping = omimIdIndexedIdMappingMap.get(cols[0]);
				if(hprdIdMapping == null){
					System.out.println(line);
					continue;
				}
				
				sb.append(hprdIdMapping.getEntrezGeneId()).append("\t");
				sb.append(cols[1]).append("\t");
				sb.append(cols[2]).append("\n");
			}
			
			in.close();
			
			WriterUtil.write("E:/2013疾病研究/疾病数据/OMIM/geneEntrezId_diseaseOmimId.txt", sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		GeneDiseaseAssociation associations = new GeneDiseaseAssociation();
		//associations.read();
		//associations.read("E:/2013疾病研究/疾病数据/OMIM/geneEntrezId_diseaseOmimId.txt");
		associations.read("E:/2013疾病研究/实验数据/SP_TrustRanker比较/input/orphanet_diseases.txt");
		
		System.out.println("Gene number: " + associations.geneMap.size());
		System.out.println("Disorder number: " + associations.disorderMap.size());
		
		
		StringBuffer sb = new StringBuffer();
		
		for(Disorder disorder: associations.disorderMap.values()){
			//if(disorder.geneMap.size() < 3)continue;
			sb.append(disorder.getName()).append("\n");
			//sb.append(disorder.geneMap.size()).append("\n");
		}
		
		WriterUtil.write("E:/2013疾病研究/实验数据/SP_TrustRanker比较/input/orphanet_diseasesName.txt",
				sb.toString());
		
//		WriterUtil.write("E:/2013疾病研究/疾病数据/OMIM/gene2disorder.txt",
//				associations.geneMap2String());
//		
//		WriterUtil.write("E:/2013疾病研究/疾病数据/OMIM/disorder2gene.txt",
//				associations.disorderMap2String3());
//		
//		WriterUtil.write("E:/2013疾病研究/疾病数据/OMIM/disorder2gene_2.txt",
//				associations.disorderMap2String2());
		
		//associations.selectedDisorder2String();
		
		//associations.processGeneDiseaseAssociationFile();
		
		//associations.writeDisorderMap2File();
		//associations.processGeneDiseaseAssociationFile2entrezID();
	}
}
