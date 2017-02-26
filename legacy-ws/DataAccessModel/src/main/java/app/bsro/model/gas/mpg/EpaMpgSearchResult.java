package app.bsro.model.gas.mpg;

import java.util.List;

import com.bfrc.dataaccess.model.gas.BsroEpaMpgLookup;

public class EpaMpgSearchResult {

	private List<BsroEpaMpgLookup> epaMpgs;
	
	public EpaMpgSearchResult(){}
	public EpaMpgSearchResult(List<BsroEpaMpgLookup> result) {
		setEpaMpgs(result);
	}	
	
	public void setEpaMpgs(List<BsroEpaMpgLookup> result) {
		this.epaMpgs = result;
	}
	public List<BsroEpaMpgLookup> getEpaMpgs() {
		return epaMpgs;
	}
}
