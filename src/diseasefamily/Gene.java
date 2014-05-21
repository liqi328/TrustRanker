package diseasefamily;

import java.util.HashMap;
import java.util.Map;

/* 基因 */
public class Gene{
	private String omimId;
	private String symbol;
	private String name;
	private String hprdId;
	
	private String geneNameInPPI; //与PPI网络使用相同的ID类型的值
	
	
	/* 与该基因有关的所有的疾病*/
	public final Map<String, Disorder> disorderMap;

	public Gene(String omimId, String symbol, String name){
		this(omimId, symbol, name, "");
	}
	
	public Gene(String omimId, String symbol, String name, String hprdId){
		this.omimId = omimId;
		this.symbol = symbol;
		this.name = name;
		this.hprdId = hprdId;
		
		disorderMap = new HashMap<String, Disorder>();
	}
	
	public String getOmimId() {
		return omimId;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getName() {
		return name;
	}

	public String getHprdId() {
		return hprdId;
	}

	public String getGeneNameInPPI() {
		return geneNameInPPI;
	}

	public void setGeneNameInPPI(String geneNameInPPI) {
		this.geneNameInPPI = geneNameInPPI;
	}
}