package com.bfrc.framework.dao.hibernate3;

import java.io.*;
import java.util.*;

import com.sun.image.codec.jpeg.*;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.image.*;

import org.springframework.context.*;
import org.springframework.core.io.*;
import org.springframework.transaction.*;
import org.springframework.transaction.support.*;

import org.hibernate.Query;
import org.hibernate.Session;
import org.pdfbox.cos.*;
import org.pdfbox.pdfparser.PDFStreamParser;
import org.pdfbox.pdfwriter.ContentStreamWriter;
import org.pdfbox.pdmodel.*;
import org.pdfbox.pdmodel.common.PDStream;
import org.pdfbox.util.PDFOperator;

import com.bfrc.framework.dao.*;

import com.bfrc.*;
import com.bfrc.pojo.*;
import com.bfrc.pojo.partner.*;
import com.bfrc.framework.util.*;
public class PartnerDAOImpl extends UserDAOImpl implements PartnerDAO, ResourceLoaderAware {
	private ResourceLoader resourceLoader;
	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

    public List findCompaniesByKeyword(String keyword) {
        String hql = "from Company c where upper(c.name) like '%'||?||'%'";
        return getHibernateTemplate().find(hql, keyword.toUpperCase());
    }

	public void deleteCompany(Company c) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			c.setActive(false);
			getHibernateTemplate().update(c);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("PartnerDAO could not delete company ");
			if(c != null)
				System.err.print(c.toString());
			System.err.println();
		    throw ex;
		}
	}

	public void enrollCompany(Company c) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			c.setActive(true);
			c.setCreatedDate(new java.util.Date());
			getHibernateTemplate().save(c);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("PartnerDAO could not enroll company ");
			if(c != null)
				System.err.print(c.toString());
			System.err.println();
		    throw ex;
		}
	}

	public void updateSignupCode(Company c, String signupCode) throws Exception {	
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			c.setSignupCode(signupCode);
			getHibernateTemplate().update(c);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("PartnerDAO could not update signup code (" + signupCode + ") for company ");
			if(c != null)
				System.err.print(c.toString());
			System.err.println();
		    throw ex;
		}
	}

	public void updateLogo(Company c, byte[] image) throws Exception {
		Config config = getConfig();
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			image = resizeImage(image, config.getMaxLogoWidth().intValue(),config.getMaxLogoHeight().intValue());
			c.setImage(image);
			getHibernateTemplate().update(c);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("PartnerDAO could not update logo for company ");
			if(c != null)
				System.err.print(c.toString());
			System.err.println();
		    throw ex;
		}
	}

	public void updateTerms(Company c, boolean terms) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			Company company = findCompanyById(new Long(c.getId()));
			company.setAcceptTerms(new Boolean(terms));
			getHibernateTemplate().update(company);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("PartnerDAO could not update terms (" + terms + ") for company ");
			if(c != null)
				System.err.print(c.toString());
			System.err.println();
		    throw ex;
		}
	}

	public List getActiveCompanies() {
		return getActiveCompanies(false);
	}

	public List getActiveSignupCompanies() {
		return getActiveCompanies(true);
	}

	private List getActiveCompanies(boolean signup) {
		String hql = "from Company c where ";
		if(signup)
			hql += "c.signupCode is not null and "; 
		hql += "c.active=1 order by c.name";
		return getHibernateTemplate().find(hql);
	}

	public List getCompanies(String status, String sort) {
		String hql = "from Company c left join fetch c.signups ";
		hql += "where ";
		String filter = "";
		if("N".equals(status) || "A".equals(status)) {
			filter = "c.signupCode is ";
			if("A".equals(status))
				filter += "not ";
			filter += "null and c.active=1";
		}
		else if("I".equals(status))
			filter = "c.active=0";
		hql += filter + "order by c.";
		if("name".equals(sort))
			hql += "name";
		else if("count".equals(sort))
			hql += "signupCount desc";
		else if("terms".equals(sort))
			hql += "acceptTerms";
		else if("code".equals(sort))
			hql += "signupCode";
		else hql += "id desc";
		List l = getHibernateTemplate().find(hql);
		List out = new ArrayList();
		if(l == null)
			return null;
		/*
		for(int i=1;i<l.size();i++) {
			Company c1 = (Company)l.get(i-1);
			Company c2 = (Company)l.get(i);
			if(i==1)
				out.add(c1);
			if(c2.getId() != c1.getId())
				out.add(c2);
		}
		
		*/
		//de-dup first
		List ids = new ArrayList();
		for(int i=0;i<l.size();i++) {
			Company c1 = (Company)l.get(i);
			if(!ids.contains(new Long(c1.getId()))){
				out.add(c1);
				ids.add(new Long(c1.getId()));
			}
		}
		return out;
	}
	
	public List getCompanies(String status, String sort, int page) {
		Session session = getSession();
		List results = new ArrayList();
		String hql = "from Company c left join fetch c.signups ";
		hql += "where ";
		String filter = "";
		if("N".equals(status) || "A".equals(status)) {
			filter = "c.signupCode is ";
			if("A".equals(status))
				filter += "not ";
			filter += "null and c.active=1";
		}
		else if("I".equals(status))
			filter = "c.active=0";
		hql += filter + "order by c.";
		if("name".equals(sort))
			hql += "name";
		else if("count".equals(sort))
			hql += "signupCount desc";
		else if("terms".equals(sort))
			hql += "acceptTerms";
		else if("code".equals(sort))
			hql += "signupCode";
		else hql += "id desc";
		try {
			Query query = session.createQuery(hql);
			query.setFirstResult(((page*10)-10));
			query.setMaxResults(10);
			results = query.list();
		}catch(Throwable e) {
			e.printStackTrace();
		}finally {
			releaseSession(session);
		}
		return results;
	}
	
	public int getTotalCompanies(String status) {
		Session session = getSession();
		int cnt = 0 ;
		String hql = "select count(*) FROM Company ";
		hql += "where ";
		String filter = "";
		if("N".equals(status) || "A".equals(status)) {
			filter = "signupCode is ";
			if("A".equals(status))
				filter += "not ";
			filter += "null and active=1";
		}
		else if("I".equals(status))
			filter = "active=0";
		hql += filter + "";
		try{
			Query query = session.createQuery(hql);
			Long count = (Long)query.uniqueResult();
			cnt = count.intValue();
		}catch(Throwable e) {
			e.printStackTrace();
		}finally {
			releaseSession(session);
		}
		return cnt;
	}

	public List getRecords(String status, String sort) {
		String hql = "from Record r where r.status = ? order by r.";
		String order = "createdDate desc";
		if("name".equals(sort))
			order = "lastName, r.firstName";
		else if("company".equals(sort))
			order = "companyName";
		else if("source".equals(sort))
			order = "source";
		hql += order;
		return getHibernateTemplate().find(hql, status);
	}

	public Company findCompanyBySignupCode(String c) {
		List l = getHibernateTemplate().find(
				"from Company c where c.signupCode = ?", c);
		if(l == null || l.size() == 0)
			return null;
		return (Company)l.get(0);
	}

	public void insertRecord(Record r, User user, Company company) {
		r.setFirstName(user.getFirstName());
		r.setLastName(user.getLastName());
		r.setCompanyName(company.getName());
		r.setStatus("N");
		r.setCreatedDate(new java.util.Date());
		getHibernateTemplate().save(r);
	}

	public void deleteRecord(Long id) {
		getHibernateTemplate().delete(findRecordById(id));
	}

	public Record findRecordById(Long id) {
		List l = getHibernateTemplate().find(
				"from Record r where r.id=" + id);
		if(l == null || l.size() == 0)
			return null;
		return (Record)l.get(0);
	}

	public void updateRecordNote(Long id, String note, Date contactDate, String contactType) {
		Record r = findRecordById(id);
		r.setNote(note);
		r.setContactDate(contactDate);
		r.setContactType(contactType);
		r.setNoteDate(new Date());
		getHibernateTemplate().update(r);
	}

	public void updateRecordStatus(Long id, String status) {
		Record r = findRecordById(id);
		r.setStatus(status);
		getHibernateTemplate().update(r);
	}

	public void signupEmployee(User u, UserVehicle v, Company c) throws Exception {
		u.setUserTypeId(User.PPS);
		u.setCompany(c);
		if(v != null)
			u.setVehicle(v);
		signupUser(u, v);
	}

	public Company findCompanyById(Long id) {
		List l = getHibernateTemplate().find(
				"from Company c where c.id=" + id);
		if(l == null || l.size() == 0)
			return null;
		return (Company)l.get(0);
	}

	public PlatformTransactionManager getTxManager() {
		return this.txManager;
	}

	public void setTxManager(PlatformTransactionManager transactionManager) {
		this.txManager = transactionManager;
	}
/*
	public List getCompaniesWithSignupCount(String status, String sort) {
		List l = getCompanies(status, sort, true);
		if(l != null) {
			Iterator i = l.iterator();
			while(i.hasNext()) {
				Company c = (Company)i.next();
				Set s = c.getSignups();
				if(s == null)
					c.setSignupCount(0);
				else c.setSignupCount(s.size());
			}
		}
		return l;
	}
*/
	public byte[] getWelcomePDF(Company c) throws Exception {
		String replaceCode = "XXXX";
		String replaceCompany = "ZZZZ";
		// load PDF
		ByteArrayOutputStream baos = null;
		Resource pdf = resourceLoader.getResource("WEB-INF/pdf/welcome.pdf");
		InputStream is = pdf.getInputStream();
		PDDocument doc = null;
		try {
			doc = PDDocument.load(is);
			is.close();
            List pages = doc.getDocumentCatalog().getAllPages();
            for( int i=0; i<pages.size(); i++ )
            {
                PDPage page = (PDPage)pages.get( i );
                PDStream contents = page.getContents();
                PDFStreamParser parser = new PDFStreamParser(contents.getStream() );
                parser.parse();
                List tokens = parser.getTokens();
                for( int j=0; j<tokens.size(); j++ )
                {
                    Object next = tokens.get( j );
                    if( next instanceof PDFOperator )
                    {
                        PDFOperator op = (PDFOperator)next;
                        //Tj and TJ are the two operators that display
                        //strings in a PDF
                        if( op.getOperation().equals( "Tj" ) )
                        {
                            //Tj takes one operator and that is the string
                            //to display so lets update that operator
                            COSString previous = (COSString)tokens.get( j-1 );
                            String string = previous.getString();
							if(string != null && string.indexOf(replaceCode) > -1) {
								string = string.replaceFirst(replaceCode, c.getSignupCode());
//System.out.println("DAO found and replaced signup code");
							}
							else if(string != null && string.indexOf(replaceCompany) > -1) {
								string = string.replaceFirst(replaceCompany, c.getName());
//System.out.println("DAO found and replaced company name (Tj)");
							}
                            previous.reset();
                            previous.append( string.getBytes() );
                        }
                        else if( op.getOperation().equals( "TJ" ) )
                        {
                            COSArray previous = (COSArray)tokens.get( j-1 );
                            for( int k=0; k<previous.size(); k++ )
                            {
                                Object arrElement = previous.getObject( k );
                                if( arrElement instanceof COSString )
                                {
                                    COSString cosString = (COSString)arrElement;
                                    String string = cosString.getString();
									if(string != null && string.indexOf(replaceCompany) > -1) {
										string = string.replaceFirst(replaceCompany, c.getName());
										// skip 6 lines
//System.out.println("DAO found and replaced company name (TJ)");
									}
                                    cosString.reset();
                                    cosString.append( string.getBytes() );                                    
                                }
                            }
                        }
                    }
                }
                //now that the tokens are updated we will replace the 
                //page content stream.
                PDStream updatedStream = new PDStream(doc);
                OutputStream out = updatedStream.createOutputStream();
                ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
                tokenWriter.writeTokens( tokens );
                page.setContents( updatedStream );
            }
            // save PDF
            baos = new ByteArrayOutputStream();
            doc.save(baos);
		} finally {
			if(doc != null)
				doc.close();
		}
		byte[] bytes = baos.toByteArray();
		baos.close();
		return bytes;
	}

	public void updateEmailAddress(Company c, String email) throws Exception {	
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			c.setEmailAddress(email);
			getHibernateTemplate().update(c);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("PartnerDAO could not update e-mail address (" + email + ") for company ");
			if(c != null)
				System.err.print(c.toString());
			System.err.println();
		    throw ex;
		}
	}
	
	private byte[] resizeImage(byte[] in, int thumbWidth, int thumbHeight) throws Exception {
		Image image = Toolkit.getDefaultToolkit().createImage(in);
	    MediaTracker mediaTracker = new MediaTracker(new Container());
	    mediaTracker.addImage(image, 0);
	    mediaTracker.waitForID(0);
	    // determine thumbnail size from WIDTH and HEIGHT
	    double thumbRatio = (double)thumbWidth / (double)thumbHeight;
	    int imageWidth = image.getWidth(null);
	    int imageHeight = image.getHeight(null);
	    double imageRatio = (double)imageWidth / (double)imageHeight;
	    if (thumbRatio < imageRatio) {
	      thumbHeight = (int)(thumbWidth / imageRatio);
	    } else {
	      thumbWidth = (int)(thumbHeight * imageRatio);
	    }
	    // draw original image to thumbnail image object and
	    // scale it to the new size on-the-fly
	    BufferedImage thumbImage = new BufferedImage(thumbWidth, 
	      thumbHeight, BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics2D = thumbImage.createGraphics();
	    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	      RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
	    // save thumbnail image to OUTFILE
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
	    JPEGEncodeParam param = encoder.
	      getDefaultJPEGEncodeParam(thumbImage);
	    int quality = getConfig().getThumbnailQuality().intValue();
	    quality = Math.max(0, Math.min(quality, 100));
	    param.setQuality((float)quality / 100.0f, false);
	    encoder.setJPEGEncodeParam(param);
	    encoder.encode(thumbImage);
	    return out.toByteArray();
	}

	public List getSignupHistory(Long filter) {
//		String hql = "from Company c left join c.signups ";
		return null;
	}

	public List getSignupPercentages() {
		// TODO Auto-generated method stub
		return null;
	}
	public void updateCompany(Company c) throws Exception {
		updateCompany(c, null);
	}
	public void updateCompany(Company c, byte[] image) throws Exception {
		Config config = getConfig();
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		if(image != null){
			try {
				image = resizeImage(image,  config.getMaxLogoWidth().intValue(),config.getMaxLogoHeight().intValue());
				c.setImage(image);
				getHibernateTemplate().update(c);
				this.txManager.commit(status);
			} catch (Exception ex) {
				this.txManager.rollback(status);
				System.err.print("PartnerDAO could not update logo for company ");
				if(c != null)
					System.err.print(c.toString());
				System.err.println();
			    throw ex;
			}
		}else{
			try {
				getHibernateTemplate().update(c);
				this.txManager.commit(status);
			} catch (Exception ex) {
				this.txManager.rollback(status);
				System.err.print("PartnerDAO could not update logo for company ");
				if(c != null)
					System.err.print(c.toString());
				System.err.println();
			    throw ex;
			}
		}
		
	}
	
	public List searchRecords(String firstName, String lastName, String companyName, String emailAddress,String sort) {

		String hql = "from Record r where";
		boolean flag = false;
		List vals = new ArrayList();
		
		if(!ServerUtil.isNullOrEmpty(firstName)){
			if(!flag) 
				hql += " upper(r.firstName) like '%'||?||'%' ";
			else
				hql += " and upper(r.firstName) like '%'||?||'%' ";
			vals.add(firstName.toUpperCase());
			flag = true;
		}
		if(!ServerUtil.isNullOrEmpty(lastName)){
			if(!flag) 
				hql += " upper(r.lastName) like '%'||?||'%' ";
			else
				hql += " and upper(r.lastName) like '%'||?||'%' ";
			vals.add(lastName.toUpperCase());
			flag = true;
		}
		
		if(!ServerUtil.isNullOrEmpty(companyName)){
			if(!flag) 
				hql += " upper(r.companyName) like '%'||?||'%' ";
			else
				hql += " and upper(r.companyName) like '%'||?||'%' ";
			vals.add(companyName.toUpperCase());
			flag = true;
		}
		if(!ServerUtil.isNullOrEmpty(emailAddress)){
			if(!flag) 
				hql += " upper(r.data) like '%E-MAIL ADDRESS:%'||?||'%' ";
			else
				hql += " and upper(r.data) like '%E-MAIL ADDRESS:%'||?||'%' ";
			vals.add(emailAddress.toUpperCase());
			flag = true;
		}
		hql += " order by ";
		/*
		" upper(r.firstName) like '%'||?||'%' "+
		             " and upper(r.lastName) like '%'||?||'%'"+
		             " and upper(r.companyName) like '%'||?||'%'"+
		             " and upper(r.data) like '%E-MAIL ADDRESS:%'||?||'%ADDRESS:%'"+
		           " order by r.";
		   */
		String order = " r.createdDate desc";
		if("name".equals(sort))
			order = " upper(r.lastName), upper(r.firstName)";
		else if("company".equals(sort))
			order = " upper(r.companyName) ";
		else if("source".equals(sort))
			order = " upper(r.source) ";
		hql += order;
		
		return getHibernateTemplate().find(hql, vals.toArray());
	}
}
