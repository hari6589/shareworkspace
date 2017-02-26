package com.bfrc.dataaccess.dao.quote;

import com.bfrc.dataaccess.model.quote.QuoteType;

public interface QuoteTypeDAO {
	public QuoteType findQuoteTypeByFriendlyId(String friendlyId);
}
