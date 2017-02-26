package com.bfrc.framework.dao;

import com.bfrc.pojo.zipcode.ZipCodeData;

public interface ZipCodeDataDAO {

  public ZipCodeData getZipCodeDataByZip(String zip);
  public String getStateByZip(String zip);
}
