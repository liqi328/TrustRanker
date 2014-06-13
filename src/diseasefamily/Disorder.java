package diseasefamily;

import java.util.HashMap;
import java.util.Map;

/* 疾病 */
public class Disorder{
	private String omimId;
	private String name;
	
	/* 该疾病所有的已知致病基因*/
	public final Map<String, Gene> geneMap;

	public Disorder(String omimId, String name){
		this.omimId = omimId;
		this.name = name;
		
		geneMap = new HashMap<String, Gene>();
	}

	public String getOmimId() {
		return omimId;
	}

	public String getName() {
		return name;
	}
	
	public String toString(){
		return omimId + "\t" + name;
	}
}