package diseasefamily;

/* »ùÒò */
public class Gene{
	private String omimId;
	private String symbol;
	private String name;

	public Gene(String omimId, String symbol, String name){
		this.omimId = omimId;
		this.symbol = symbol;
		this.name = name;
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
}