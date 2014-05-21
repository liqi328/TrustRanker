package diseasefamily;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.WriterUtil;


/* 专门读取OMIM数据库中morbidmap文件,
 * morbidmap中的一行，表示基因与疾病的关系
 * 
 * For the file morbidmap, the fields are, in order:
1  - Disorder, <disorder MIM no.> (<phene mapping key>)
2  - Gene/locus symbols
3  - Gene/locus MIM no.
4  - cytogenetic location
 * */
public class OmimGeneDiseaseAssociationReader{
	
	public static void read(String filename) {
		String regEx = "[0-9]{6}"; //表示a或F
		Pattern pat = Pattern.compile(regEx);
		Map<String, HprdIdMapping> symbolIndexedIdMappingMap = HprdIdMappingUtil.getSymbolIdIndexedIdMapping();
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
			String line = null;
			StringBuffer sb = new StringBuffer();
			String[] cols = null;
			
			HprdIdMapping hprdIdMapping = null;
			
			while((line = in.readLine()) != null){
				cols = line.split("\\|");
				Matcher mat = pat.matcher(cols[0]);
				//System.out.println(cols[1].split(",")[0]);
				hprdIdMapping = symbolIndexedIdMappingMap.get(cols[1].split(",")[0]);
				if(hprdIdMapping == null){
					//System.out.println(line);
					continue;
				}
				int i = 0;
				while(mat.find()){  
					++i;
					//sb.append(line).append("|").append(mat.group()).append("\n");
					sb.append(hprdIdMapping.getEntrezGeneId()).append("\t");
					sb.append(mat.group()).append("\t");
					sb.append(cols[0]).append("\n");
					//break;
			    }  
				if(i > 1){
					System.out.println(line);
				}
			}
			
			WriterUtil.write("E:/2013疾病研究/疾病数据/OMIM/geneEntrezId_diseaseOmimId.txt", sb.toString());
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
		
	
	
	
	public static void main(String[] args){
		//test();
		String s = "17,20-lyase deficiency, isolated, 202110 (3)";
		String regEx = "[0-9]{6}"; //表示a或F
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(s);
		boolean rs = mat.find(); 
		
		OmimGeneDiseaseAssociationReader.read("E:/2013疾病研究/疾病数据/OMIM/morbidmap");
	}


}
