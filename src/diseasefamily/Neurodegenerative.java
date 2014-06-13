package diseasefamily;

import graph.AdjacencyGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.WriterUtil;

class GeneDiseaseAssociation2 {
	public final Map<String, Gene> geneMap = new HashMap<String, Gene>();
	public final Map<String, Disorder> disorderMap = new HashMap<String, Disorder>();
	
	private String hprdIdMapppingFilepath = "E:/2013疾病研究/疾病数据/HumanPPI/HPRD_Release9_062910/FLAT_FILES_072010/HPRD_ID_MAPPINGS.txt";
	
	public GeneDiseaseAssociation2(){

	}
	
	public GeneDiseaseAssociation2(String hprdIdMapppingFilepath2){
		hprdIdMapppingFilepath = hprdIdMapppingFilepath2;
	}
	
	public void read(){
		//String geneDiseaseAssociationFilepath = "E:/2013疾病研究/疾病数据/OMIM/geneOmimId_diseaseOmimId_in_ppi.txt";
		String geneDiseaseAssociationFilepath = "E:/2013疾病研究/疾病数据/OMIM/geneOmimId_diseaseOmimId.txt";
		
		read(geneDiseaseAssociationFilepath);
	}
	
	public void read(String geneDiseaseAssociationFilepath){
	
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(geneDiseaseAssociationFilepath)));
			String line = null;
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
				
				gene = geneMap.get(cols[0]);
				if(gene == null){
					gene = new Gene(cols[0], "", "");
				}
				
				gene.disorderMap.put(disorder.getOmimId(), disorder);
				disorder.geneMap.put(gene.getOmimId(), gene);
				
				geneMap.put(gene.getOmimId(), gene);
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
}

public class Neurodegenerative {
	private AdjacencyGraph network = new AdjacencyGraph();
	
	public  void create(GeneDiseaseAssociation2 associations){
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
	
	private int commomDiseaseGeneBetweenTwoDisorder(Disorder firstDisorder, Disorder secondDisorder){
		Set<String> firstDisorderGeneSet = new HashSet<String>();
		firstDisorderGeneSet.addAll(firstDisorder.geneMap.keySet());
		
		int count = 0;
		
		for(String gene: secondDisorder.geneMap.keySet()){
			if(firstDisorderGeneSet.contains(gene)){
				++count;
			}
		}
		
		return count;
	}
	
	private Set<String> commomDiseaseGeneBetweenTwoDisorder2(Disorder firstDisorder, Disorder secondDisorder){
		Set<String> firstDisorderGeneSet = new HashSet<String>();
		firstDisorderGeneSet.addAll(firstDisorder.geneMap.keySet());
		
		Set<String> commonGeneSet = new HashSet<String>();
		
		for(String gene: secondDisorder.geneMap.keySet()){
			if(firstDisorderGeneSet.contains(gene)){
				commonGeneSet.add(gene);
			}
		}
		
		return commonGeneSet;
	}
	
	public static void run1(){
		GeneDiseaseAssociation2 associations = new GeneDiseaseAssociation2();
		associations.read("E:/2013疾病研究/实验数据/神经退行性疾病20140603/geneOmimId_diseaseOmimId.txt");
		
		System.out.println("Gene number: " + associations.geneMap.size());
		System.out.println("Disorder number: " + associations.disorderMap.size());
		
		
		StringBuffer sb = new StringBuffer();
		
		Set<String> diseaseOmimIdSet = new HashSet<String>();
		diseaseOmimIdSet.add("104300");
		diseaseOmimIdSet.add("105400");
		diseaseOmimIdSet.add("253300");
		diseaseOmimIdSet.add("168600");
		diseaseOmimIdSet.add("608768");
		
		String[] diseaseOmimIdArray = {
				"104300", "105400", "253300", "168600", "608768"
		};

		Neurodegenerative neurodegenerative = new Neurodegenerative();
		neurodegenerative.create(associations);
		
		for(Disorder disorder: associations.disorderMap.values()){
			if(diseaseOmimIdSet.contains(disorder.getOmimId())){
				sb.append(disorder.getName()).append("\n");
				sb.append(disorder.geneMap.size()).append("\n");
			}
		}
		
		Map<String, Disorder> disorderMap = associations.disorderMap;
		for(int i = 0; i < diseaseOmimIdArray.length; ++i){
			for(int j = i+1; j < diseaseOmimIdArray.length; ++j){
				Set<String> commonGeneSet = neurodegenerative.commomDiseaseGeneBetweenTwoDisorder2(disorderMap.get(diseaseOmimIdArray[i]), disorderMap.get(diseaseOmimIdArray[j]));
				System.out.println(diseaseOmimIdArray[i] + "--" + diseaseOmimIdArray[j]);
				System.out.println("\t" + commonGeneSet);
			}
		}
		System.out.println("--------------------------------------");
		for(int i = 0; i < diseaseOmimIdArray.length; ++i){
			for(Disorder disorder: associations.disorderMap.values()){
				if(disorder.getOmimId().equals(diseaseOmimIdArray[i])){
					continue;
				}
				Set<String> commonGeneSet = neurodegenerative.commomDiseaseGeneBetweenTwoDisorder2(disorderMap.get(diseaseOmimIdArray[i]), disorder);
				if(commonGeneSet.size() > 0){
					System.out.println(diseaseOmimIdArray[i] + "--" + disorder.getOmimId() + ": " + commonGeneSet);
				}
			}
		}
		
		
		System.out.println(sb.toString());
		
//		WriterUtil.write("E:/2013疾病研究/实验数据/SP_TrustRanker比较/input/orphanet_diseasesName.txt",
//				sb.toString());
	}
	
	public static void run3(){
		GeneDiseaseAssociation2 associations = new GeneDiseaseAssociation2();
		associations.read("E:/2013疾病研究/实验数据/神经退行性疾病20140603/geneOmimId_diseaseOmimId.txt");
		
		List<String> lines = readLines();
		
		Set<String> disorderOmimIdSet = new HashSet<String>();
		disorderOmimIdSet.addAll(associations.disorderMap.keySet());
		
		Set<String> disorderClassSet = new HashSet<String>();
		Map<String, String> classesMap = new HashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		
		for(Disorder disorder : associations.disorderMap.values()){
			for(String line: lines){
				if(line.contains(disorder.getOmimId())){
					String subStr = line.substring(line.lastIndexOf(" "));
					sb.append(disorder).append("\t").append(subStr).append("\n");
					
					classesMap.put(disorder.getOmimId(), subStr);
					disorderClassSet.add(subStr);
					break;
				}
			}
		}
		
//		for(String c: disorderClassSet){
//			System.out.println(c);
//		}
//		
//		
		WriterUtil.write("E:/2013疾病研究/实验数据/神经退行性疾病20140603/2.txt", sb.toString());
		
		
		Neurodegenerative neurodegenerative = new Neurodegenerative();
		neurodegenerative.create(associations);
		
		String[] diseaseOmimIdArray = {
				"104300", "105400", "253300", "168600", "608768"
		};
		
		Map<String, Integer> countMap = new HashMap<String, Integer>();
		Map<String, Set<String>> commonGeneMap = new HashMap<String, Set<String>>();
		for(String c: disorderClassSet){
			countMap.put(c, 0);
			commonGeneMap.put(c, new HashSet<String>());
		}

		for(int i = 0; i < diseaseOmimIdArray.length; ++i){
			for(Disorder disorder: associations.disorderMap.values()){
				if(disorder.getOmimId().equals(diseaseOmimIdArray[i])){
					continue;
				}
				Set<String> commonGeneSet = neurodegenerative.commomDiseaseGeneBetweenTwoDisorder2(associations.disorderMap.get(diseaseOmimIdArray[i]), disorder);
				if(commonGeneSet.size() > 0){
					countMap.put(classesMap.get(disorder.getOmimId()), 
							countMap.get(classesMap.get(disorder.getOmimId()) + commonGeneSet.size()));
					if(classesMap.get(disorder.getOmimId()) != null){
						commonGeneMap.get(classesMap.get(disorder.getOmimId())).addAll(commonGeneSet);
					}else{
						System.out.println(diseaseOmimIdArray[i] + "\t" + disorder.getOmimId()+"\t" + disorder.getName() + "\t: " + commonGeneSet);
					}
				}
			}
		}
		
		for(String key : commonGeneMap.keySet()){
			System.out.println(key + "\t" + commonGeneMap.get(key).size() + "\t" + commonGeneMap.get(key));
		}
	}
	
	public static List<String> readLines(){
		String filename = "E:/2013疾病研究/实验数据/神经退行性疾病20140603/22种疾病分类.txt";
		
		List<String> lines = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String line = in.readLine();
			int count = 1;
			while((line = in.readLine()) != null){
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}
	
	public static void main(String[] args){
		run3();
	}
}
