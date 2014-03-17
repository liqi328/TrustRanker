package diseasefamily;

import java.util.HashMap;
import java.util.Map;

/* ���� */
public class Disorder{
	private String omimId;
	private String name;
	
	/* �ü������е���֪�²�����*/
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
}