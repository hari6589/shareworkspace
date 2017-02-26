package com.bfrc.dataaccess.dao.quote;

import com.bfrc.dataaccess.model.quote.QuoteItemType;

public interface QuoteItemTypeDAO {
	public QuoteItemType findQuoteItemTypeByFriendlyId(String friendlyId);
}
