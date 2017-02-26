package com.bfrc.framework.dao;

import java.util.Date;
import java.util.Map;

public interface DSTDAO {

    public Map<String,Date> getStartAndEndDateForYear(String year);
}
