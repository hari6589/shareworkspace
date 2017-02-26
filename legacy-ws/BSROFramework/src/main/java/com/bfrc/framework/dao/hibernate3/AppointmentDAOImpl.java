package com.bfrc.framework.dao.hibernate3;


import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.bfrc.dataaccess.model.appointment.AppointmentData;
import com.bfrc.framework.dao.AppointmentDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.pojo.appointment.Appointment;
import com.bfrc.pojo.appointment.AppointmentChoice;
import com.bfrc.pojo.appointment.AppointmentCount;
import com.bfrc.pojo.appointment.AppointmentCustomer;
import com.bfrc.pojo.appointment.AppointmentID;
import com.bfrc.pojo.appointment.AppointmentLog;
import com.bfrc.pojo.appointment.AppointmentMetadata;
import com.bfrc.pojo.appointment.AppointmentSentStatus;
import com.bfrc.pojo.appointment.AppointmentService;
import com.bfrc.pojo.appointment.AppointmentServiceAndCat;
import com.bfrc.pojo.appointment.AppointmentServiceDesc;

public class AppointmentDAOImpl extends HibernateDAOImpl implements AppointmentDAO {
	
    private PlatformTransactionManager txManager;
	
	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public void logAppointment(AppointmentLog apptLog)
			throws DataAccessException {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			getHibernateTemplate().save(apptLog);
			this.txManager.commit(status);
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			this.txManager.rollback(status);
			System.err.print("AppointmentDAO could not log appointment ");
			if(apptLog != null)
				System.err.print(apptLog.getLogValue());
		}
	}
	
	public Appointment findAppointmentById(Long appointmentId) {
		return (Appointment)getHibernateTemplate().get(Appointment.class,appointmentId);
	}
	
	public List<AppointmentService> findServicesForAppointment(Long appointmentId){
		Object[] params = {appointmentId};
		return(List<AppointmentService>)getHibernateTemplate().find("from AppointmentService where appointmentId = ?", params);
		
	}
	
	public String getServiceDesc(Integer id) throws DataAccessException {
		String sql = "select SERVICE_DESC from APPOINTMENT_SERVICE_DESC where SERVICE_ID=" + id;
		String desc = null;
		Session s = getSession();
		try {
			SQLQuery q = s.createSQLQuery(sql);
			q.addScalar("SERVICE_DESC", Hibernate.STRING);
			desc = (String)q.uniqueResult();
		} finally {
			//s.close();
			this.releaseSession(s);
		}
		return desc;
	}

	public void addAppointment(Appointment appt) {
		// Note: Hibernate3's merge operation does not reassociate the object with the
		// current Hibernate Session. Instead, it will always copy the state over to
		// a registered representation of the entity. In case of a new entity, it will
		// register a copy as well, but will not update the id of the passed-in object.
		// To still update the ids of the original objects too, we need to register
		// Spring's IdTransferringMergeEventListener on our SessionFactory.
		getHibernateTemplate().saveOrUpdate(appt);
	}

	public void updateAppointment(Appointment appt) {
		getHibernateTemplate().update(appt);
	}
	public void deleteAppointment(Appointment appt) {
		getHibernateTemplate().delete(appt);
	}
	
	public void addAppointmentService(AppointmentService apptService) {
		getHibernateTemplate().save(apptService);
	}

	public void addAppointmentChoice(AppointmentChoice apptChoice) {
		getHibernateTemplate().save(apptChoice);
	}

	public Integer getServiceId(String serviceDesc) {
		String sql = "select SERVICE_ID from APPOINTMENT_SERVICE_DESC where trim(SERVICE_DESC)='" + serviceDesc + "'";
		Session s = getSession();
		SQLQuery q = s.createSQLQuery(sql);
		q.addScalar("SERVICE_ID", Hibernate.INTEGER);
		Integer serviceId = (Integer) q.list().iterator().next();
		s.close();
		return serviceId;
	}
	//cxs
	public void createAppointmentChoice(AppointmentChoice appointmentChoice) {
		getHibernateTemplate().save(appointmentChoice);
	}
	public void updateAppointmentChoice(AppointmentChoice appointmentChoice) {
		getHibernateTemplate().update(appointmentChoice);
	}
	public void deleteAppointmentChoice(AppointmentChoice appointmentChoice) {
		getHibernateTemplate().delete(appointmentChoice);
	}
	
	public void createAppointmentService(AppointmentService appointmentService) {
		getHibernateTemplate().save(appointmentService);
	}
	public void updateAppointmentService(AppointmentService appointmentService) {
		getHibernateTemplate().update(appointmentService);
	}
	public void deleteAppointmentService(AppointmentService appointmentService) {
		getHibernateTemplate().delete(appointmentService);
	}
	
	public AppointmentServiceDesc findAppointmentServiceDescById(Object id){
		if(id == null) return null;
		return (AppointmentServiceDesc)getHibernateTemplate().get(AppointmentServiceDesc.class,new Integer(id.toString()));
	}
	public void createAppointmentServiceDesc(AppointmentServiceDesc appointmentServiceDesc){
		getHibernateTemplate().save(appointmentServiceDesc);
	}
	public void updateAppointmentServiceDesc(AppointmentServiceDesc appointmentServiceDesc){
		getHibernateTemplate().update(appointmentServiceDesc);
	}
	public void deleteAppointmentServiceDesc(AppointmentServiceDesc appointmentServiceDesc){
		getHibernateTemplate().delete(appointmentServiceDesc);
	}
	public void deleteAppointmentServiceDesc(Object id){
		getHibernateTemplate().delete(findAppointmentServiceDescById(id));
	}
	public List getAllAppointmentServiceDescs(){
		
		String hql = "from AppointmentServiceDesc t where t.serviceType=1 order by t.sortOrder";
		List<AppointmentServiceDesc> l = getHibernateTemplate().find(hql);
		return l;
	}
	public Map getMappedAppointmentServiceDescs(){
		List<AppointmentServiceDesc> l = getAllAppointmentServiceDescs();
		Map<String, String> m = new LinkedHashMap<String, String>();
		for(AppointmentServiceDesc item: l){
			m.put(String.valueOf(item.getServiceId()), item.getServiceDesc() != null ? item.getServiceDesc().trim() : "");
		}
		return m;
	}
	public List<AppointmentServiceAndCat> getAllAppointmentServiceDescsAndCategories(){
/*		
		List l = getHibernateTemplate().findByNamedQuery("listAppointmentServicesAndCat");
		List<AppointmentServiceAndCat> realList = new ArrayList();
		for (int i=0; i<l.size();i++){
			Object[] o = (Object[])l.get(i);
			AppointmentServiceAndCat a = new AppointmentServiceAndCat();
			Integer j = (Integer)o[0];
			a.setServiceId(j.byteValue());
			a.setServiceDesc((String) o[1]);
			a.setServiceCategory((String) o[2]);
			realList.add(a);
		}
		return realList;
*/
		StringBuffer sb = new StringBuffer();
		sb.append("Select svc.service_id as serviceId, svc.service_desc as serviceDesc, svc.vehicle_required_ind as vehicleRequiredInd, ");
		sb.append("cat.category_desc as serviceCategory ");
		sb.append("from appointment_service_desc svc left outer join appointment_serv_cat_map cmap on (svc.service_id = cmap.service_id) ");
		sb.append("left outer join appointment_service_category cat on (cmap.category_id=cat.category_id) ");
		sb.append("where svc.service_id <> 20 ");
		sb.append("order by svc.service_id");
		
		final String queryString = sb.toString(); 
//		final String queryString = "select " +
//				"sd.SERVICE_ID as serviceId, sd.SERVICE_DESC as serviceDesc, sd.VEHICLE_REQUIRED_IND as vehicleRequiredInd, sc.CATEGORY_DESC as serviceCategory " +
//				"from appointment_service_desc sd, " +
//			    "appointment_serv_cat_map scm, " +
//				"appointment_service_category sc " +
//			"where sd.service_id = scm.service_id and scm.category_id = sc.category_id " +
//			"and sd.service_id <> 20 " +
//			"order by sd.service_id";
//					
			@SuppressWarnings("unchecked")
			List<AppointmentServiceAndCat> services = (List<AppointmentServiceAndCat>) getHibernateTemplate().execute(new HibernateCallback(){

				@Override
				public Object doInHibernate(Session session) throws HibernateException,
						SQLException {
					SQLQuery query = session.createSQLQuery(queryString);
					query.addScalar("serviceId", Hibernate.INTEGER)
					.addScalar("serviceDesc", Hibernate.STRING)
					.addScalar("vehicleRequiredInd", Hibernate.STRING)
					.addScalar("serviceCategory", Hibernate.STRING)
					.setResultTransformer(Transformers.aliasToBean(AppointmentServiceAndCat.class));
					return query.list();
				}
				
			});		
			
			return services;
	}

	public List<AppointmentServiceAndCat> getAllAppointmentServiceSorted(){
		final String queryString = "select " +
				"sd.SERVICE_ID as serviceId, sd.SERVICE_DESC as serviceDesc, sd.VEHICLE_REQUIRED_IND as vehicleRequiredInd, sc.CATEGORY_DESC as serviceCategory " +
				"from appointment_service_desc sd, " +
			    "appointment_serv_cat_map scm, " +
				"appointment_service_category sc " +
			"where sd.service_id = scm.service_id and scm.category_id = sc.category_id " +
			"and sd.service_id <> 20 " +
			"order by sd.SERVICE_DESC";
					
			@SuppressWarnings("unchecked")
			List<AppointmentServiceAndCat> services = (List<AppointmentServiceAndCat>) getHibernateTemplate().execute(new HibernateCallback(){

				@Override
				public Object doInHibernate(Session session) throws HibernateException,
						SQLException {
					SQLQuery query = session.createSQLQuery(queryString);
					query.addScalar("serviceId", Hibernate.INTEGER)
					.addScalar("serviceDesc", Hibernate.STRING)
					.addScalar("vehicleRequiredInd", Hibernate.STRING)
					.addScalar("serviceCategory", Hibernate.STRING)
					.setResultTransformer(Transformers.aliasToBean(AppointmentServiceAndCat.class));
					return query.list();
				}
				
			});		
			
			return services;
	}
	
	public List<AppointmentServiceAndCat> getAllAppointmentServicesBySortOrder() {
		return getAllAppointmentServicesBySortOrder(1);
	}
	
	public List<AppointmentServiceAndCat> getAllAppointmentServicesBySortOrder(int serviceType) {
		final String queryString = "select " +
				"isc.SERVICE_ID as serviceId, isc.SERVICE_DESC as serviceDesc, isc.VEHICLE_REQUIRED_IND as vehicleRequiredInd, sc.CATEGORY_DESC as serviceCategory, isc.sort_order as sortOrder " +
				"from (SELECT sd.SERVICE_ID, sd.SERVICE_DESC, sd.VEHICLE_REQUIRED_IND, sd.sort_order, scm.CATEGORY_ID " +
				"FROM appointment_service_desc sd left join appointment_serv_cat_map scm ON sd.SERVICE_ID = scm.SERVICE_ID where sd.SERVICE_TYPE="+serviceType+") isc " +
			    "left join appointment_service_category sc ON sc.CATEGORY_ID = isc.CATEGORY_ID " +
				"order by isc.sort_order";
					
			@SuppressWarnings("unchecked")
			List<AppointmentServiceAndCat> services = (List<AppointmentServiceAndCat>) getHibernateTemplate().execute(new HibernateCallback(){

				@Override
				public Object doInHibernate(Session session) throws HibernateException,
						SQLException {
					SQLQuery query = session.createSQLQuery(queryString);
					query.addScalar("serviceId", Hibernate.INTEGER)
					.addScalar("serviceDesc", Hibernate.STRING)
					.addScalar("vehicleRequiredInd", Hibernate.STRING)
					.addScalar("serviceCategory", Hibernate.STRING)
					.addScalar("sortOrder", Hibernate.INTEGER)
					.setResultTransformer(Transformers.aliasToBean(AppointmentServiceAndCat.class));
					return query.list();
				}
				
			});		
			
			return services;
	}
	
	public List<Appointment> getAppointmentsToRetry() throws DataAccessException{

		final String hql = "from Appointment app "+
						   "where app.appointmentId IN (select ast.appointmentId from AppointmentSentStatus ast "+
				           "where ast.status = 'R') "+
				  		   "and app.createdDate > to_date(sysdate()-1) ";
		
		List<Appointment> appointments = (List<Appointment>) getHibernateTemplate().find(hql);
		System.out.println("Retry Appointments.size =====> "+appointments.size());

		
		return appointments;
	}
	
	public AppointmentID[] getErrorAppointmentID() throws DataAccessException {
		Session s = getSession();
		Query q = s.getNamedQuery("ErrorAppointmentID");
		AppointmentID[] apptId = new AppointmentID[q.list().size()];
		if (q.list().size()!=0) {
			apptId = (AppointmentID[]) q.list().toArray(apptId);
		}
		s.close();		
		return apptId;
	}

	public AppointmentCount[] getErrorAppointmentCount() throws DataAccessException {
		Session s = getSession();
		Query q = s.getNamedQuery("ErrorAppointmentCount");
		AppointmentCount[] apptCount = new AppointmentCount[q.list().size()];
		if (q.list().size()!=0) {
			apptCount = (AppointmentCount[]) q.list().toArray(apptCount);
		}
		s.close();		
		return apptCount;
	}
	
	
	@Override
	public void saveAppointmentSentStatus(AppointmentSentStatus apptSentStatus) {
		getHibernateTemplate().saveOrUpdate(apptSentStatus);
		
	}

	public void addAppointmentCustomer(AppointmentCustomer apptCust) {
		// Note: Hibernate3's merge operation does not reassociate the object with the
		// current Hibernate Session. Instead, it will always copy the state over to
		// a registered representation of the entity. In case of a new entity, it will
		// register a copy as well, but will not update the id of the passed-in object.
		// To still update the ids of the original objects too, we need to register
		// Spring's IdTransferringMergeEventListener on our SessionFactory.
		getHibernateTemplate().save(apptCust);
	}
	public void updateAppointmentCustomer(AppointmentCustomer apptCust) {
		getHibernateTemplate().update(apptCust);
	}
	public void deleteAppointmentCustomer(AppointmentCustomer apptCust) {
		getHibernateTemplate().delete(apptCust);
	}
	
	public String getAppointmentConfirmationMessage(String storeType) throws DataAccessException {
		String sql = "select MESSAGE from APPOINTMENT_CONFIRMATION_TEXT where STORE_TYPE='" + storeType + "'";
		String message = null;
		Session s = getSession();
		try {
			SQLQuery q = s.createSQLQuery(sql);
			q.addScalar("MESSAGE", Hibernate.STRING);
			message = (String)q.uniqueResult();
		} finally {
			//s.close();
			this.releaseSession(s);
		}
		return message;
	}
	
	public void updateSentStatus (Long apptId, String status, Long confirmId, String trackingId, 
			String statusMessage) throws DataAccessException{
		try {
			AppointmentSentStatus apptSentStatus = new AppointmentSentStatus();
	    	apptSentStatus.setAppointmentId(apptId);
	    	apptSentStatus.setStatus(status);
	    	apptSentStatus.setUpdateDate(new Date());
	    	apptSentStatus.setBookingConfirmationId(String.valueOf(confirmId));
	    	apptSentStatus.setEmailTrackingNumber(trackingId);
	    	apptSentStatus.setEmailStatusMessage(statusMessage);
	    	getHibernateTemplate().update(apptSentStatus);
	    } catch (Exception e){
	    	e.printStackTrace();
	    }
	}
	
	public AppointmentData createAppointmentDataBean(Appointment appt){
		AppointmentData sendAppt = new AppointmentData();

		sendAppt.setAppointmentId(appt.getAppointmentId());
		sendAppt.setStoreNumber(appt.getStoreNumber());

		sendAppt.setVehicleYear(String.valueOf(appt.getVehicleYear()));
		sendAppt.setVehicleMake(appt.getVehicleMake());
		sendAppt.setVehicleModel(appt.getVehicleModel());
		sendAppt.setVehicleSubmodel(appt.getVehicleSubmodel() == null ? "":appt.getVehicleSubmodel());
		sendAppt.setMileage(appt.getMileage() == null ? "":String.valueOf(appt.getMileage()));
		sendAppt.setComments(appt.getComments());
		sendAppt.setCustomerNotes(appt.getCustomerComments());
		sendAppt.setCustomerFirstName(appt.getFirstName());
		sendAppt.setCustomerLastName(appt.getLastName());
		sendAppt.setCustomerAddress1(appt.getAddress1());
		sendAppt.setCustomerAddress2(appt.getAddress2());
		sendAppt.setCustomerCity(appt.getCity());
		sendAppt.setCustomerState(appt.getState());
		sendAppt.setCustomerZipCode(appt.getZip());
		sendAppt.setCustomerDayTimePhone(appt.getDaytimePhone());
		sendAppt.setCustomerEveningPhone(appt.getEveningPhone());
		sendAppt.setCustomerCellPhone(appt.getCellPhone());
		sendAppt.setCustomerEmailAddress(appt.getEmailAddress());
		sendAppt.setEmployeeId(appt.getEmployeeId());
		sendAppt.setLocationId(appt.getLocationId());
		sendAppt.setAppointmentStatusId(appt.getAppointmentStatusId());
		sendAppt.setAppointmentStatusDesc(appt.getAppointmentStatusDesc());
		sendAppt.setQuoteId(String.valueOf(appt.getBatteryQuoteId()));
		//sendAppt.setReceivePromos(appt.getEmailSignup());
		sendAppt.setAppointmentType("New");
		if(!appt.getChoices().isEmpty()){
			for(int i=0; i < appt.getChoices().size(); i++){
				AppointmentChoice choice = (AppointmentChoice)appt.getChoices().get(i);
				if(choice != null){
					com.bfrc.dataaccess.model.appointment.AppointmentChoice apptChoice 
					= new com.bfrc.dataaccess.model.appointment.AppointmentChoice();
					apptChoice.setChoice(1);
					apptChoice.setAppointmentChoiceId(choice.getAppointmentChoiceId());
					apptChoice.setAppointmentId(choice.getAppointmentId());
					apptChoice.setDropWaitOption(choice.getDropWaitOption());
					apptChoice.setDatetime(choice.getDatetime());
					sendAppt.setChoice(apptChoice);
				}
			}
		}
		
		sendAppt.setSelectedServices(appt.getServiceIds());
		sendAppt.setWebsiteName(appt.getWebSite());
		return sendAppt;
	}
	
	public void addAppointmentMetadata(AppointmentMetadata metadata) throws DataAccessException{
		getHibernateTemplate().save(metadata);
	}
	
	public AppointmentMetadata getAppointmentMetadata(Long appointmentId) throws DataAccessException{
		return getHibernateTemplate().get(AppointmentMetadata.class, appointmentId);
	}
	
}