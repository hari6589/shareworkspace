package com.bfrc.pojo.interstatebattery;
// Generated Dec 1, 2009 2:07:59 PM by Hibernate Tools 3.2.1.GA



/**
 * InterstateAutomobile generated by hbm2java
 */
public class InterstateAutomobile  implements java.io.Serializable {


     private long automobileId;
     private String productCode;
     private String productLine;
     private String make;
     private String model;
     private Short year;
     private String engine;
     private String ptr;
     private String note;
     private String note1;
     private String note2;
     private String note3;
     private String optn;
     private String bci;
     private String notes;
     private String cca;

    public InterstateAutomobile() {
    }

	
    public InterstateAutomobile(long automobileId) {
        this.automobileId = automobileId;
    }
    public InterstateAutomobile(long automobileId, String productCode, String productLine, String make, String model, Short year, String engine, String ptr, String note, String note1, String note2, String note3, String optn, String bci, String notes, String cca) {
       this.automobileId = automobileId;
       this.productCode = productCode;
       this.productLine = productLine;
       this.make = make;
       this.model = model;
       this.year = year;
       this.engine = engine;
       this.ptr = ptr;
       this.note = note;
       this.note1 = note1;
       this.note2 = note2;
       this.note3 = note3;
       this.optn = optn;
       this.bci = bci;
       this.notes = notes;
       this.cca = cca;
    }
   
    public long getAutomobileId() {
        return this.automobileId;
    }
    
    public void setAutomobileId(long automobileId) {
        this.automobileId = automobileId;
    }
    public String getProductCode() {
    	if (this.productCode == null || this.productCode.length()==0)
			return "NA";
		else
			return this.productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    public String getProductLine() {
        return this.productLine;
    }
    
    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }
    public String getMake() {
        return this.make;
    }
    
    public void setMake(String make) {
        this.make = make;
    }
    public String getModel() {
        return this.model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    public Short getYear() {
        return this.year;
    }
    
    public void setYear(Short year) {
        this.year = year;
    }
    public String getEngine() {
        return this.engine;
    }
    
    public void setEngine(String engine) {
        this.engine = engine;
    }
    public String getPtr() {
        return this.ptr;
    }
    
    public void setPtr(String ptr) {
        this.ptr = ptr;
    }
    public String getNote() {
        return this.note;
    }
    
    public void setNote(String note) {
        this.note = note;
    }
    public String getNote1() {
        return this.note1;
    }
    
    public void setNote1(String note1) {
        this.note1 = note1;
    }
    public String getNote2() {
        return this.note2;
    }
    
    public void setNote2(String note2) {
        this.note2 = note2;
    }
    public String getNote3() {
        return this.note3;
    }
    
    public void setNote3(String note3) {
        this.note3 = note3;
    }
    public String getOptn() {
        return this.optn;
    }
    
    public void setOptn(String optn) {
        this.optn = optn;
    }
    public String getBci() {
        return this.bci;
    }
    
    public void setBci(String bci) {
        this.bci = bci;
    }
    public String getNotes() {
        return this.notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getCca() {
        return this.cca;
    }
    
    public void setCca(String cca) {
        this.cca = cca;
    }




}


