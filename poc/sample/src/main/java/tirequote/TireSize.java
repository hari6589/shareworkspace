package tirequote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class TireSize {
	
	public String crossSection;
	public String aspectRation;
	public String rimSize;
	
	public TireSize() {
		
	}

	public String getCrossSection() {
		return crossSection;
	}

	public void setCrossSection(String crossSection) {
		this.crossSection = crossSection;
	}

	public String getAspectRation() {
		return aspectRation;
	}

	public void setAspectRation(String aspectRation) {
		this.aspectRation = aspectRation;
	}

	public String getRimSize() {
		return rimSize;
	}

	public void setRimSize(String rimSize) {
		this.rimSize = rimSize;
	}

	@Override
	public String toString() {
		return "TireSize [crossSection=" + crossSection + ", aspectRation="
				+ aspectRation + ", rimSize=" + rimSize + "]";
	}
	
	

}
