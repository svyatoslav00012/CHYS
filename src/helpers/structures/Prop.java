package helpers.structures;

public class Prop {
	private String key;
	private String lengName;

	public Prop(String key, String lengName) {
		this.key = key;
		this.lengName = lengName;
	}

	public String getKey() {
		return key;
	}

	public String getLengName() {
		return lengName;
	}
}
