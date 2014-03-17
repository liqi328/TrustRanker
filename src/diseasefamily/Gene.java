package diseasefamily;

import java.util.HashMap;
import java.util.Map;

/* ���� */
public class Gene{
	private String omimId;
	private String symbol;
	private String name;
	private String hprdId;
	
	
	/* ��û����йص����еļ���*/
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
}