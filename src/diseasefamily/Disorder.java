package diseasefamily;

/* ¼²²¡ */
public class Disorder{
	private String omimId;
	private String name;

	public Disorder(String omimId, String name){
		this.omimId = omimId;
		this.name = name;
	}

	public String getOmimId() {
		return omimId;
	}

	public String getName() {
		return name;
	}
}