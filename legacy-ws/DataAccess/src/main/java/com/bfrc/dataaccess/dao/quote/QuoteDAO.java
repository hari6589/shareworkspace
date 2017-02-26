package com.bfrc.dataaccess.dao.quote;

import com.bfrc.dataaccess.model.quote.Quote;
import com.bfrc.dataaccess.model.quote.TpTireQuotesLedger;

public interface QuoteDAO {
	public Quote findQuoteByIdAndSite(Long quoteId, String siteName);
	
	public void save(Quote quote);
	
	public void update(Quote quote);
	
	public TpTireQuotesLedger getTireQuotesLedger(Long quoteId);
	
	public void saveTpTireQuotesLedger(TpTireQuotesLedger tpTireQuotesLedger);
	
}
