package com.bsro.api.rest.ws.test;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.bsro.model.appointment.AppointmentChoice;
import app.bsro.model.appointment.CreateAppointment;

import com.bsro.api.rest.ws.appointment.AppointmentWebService;
import com.bsro.core.security.RequireValidToken;

@Component
public class TestWebServiceImpl implements TestWebService {

	@Autowired
	private AppointmentWebService appointmentWs;
	
	private String[] devRequests = {
			"/appointment/services",
			"/maintenance/history/search?phone=3121234612&year=1997&make=Dodge&model=Caravan",
			"/maintenance/history/489?sinceDt=1245784875315",
			"/maintenance/scheduled/categories/121916",
			"/maintenance/scheduled/types/121916",
			"/maintenance/scheduled/required/121916",
			"/maintenance/scheduled/periodic/121916",
			"/maintenance/scheduled/checks/121916",
			"/maintenance/scheduled/milestones/121916/ALL",
			"/promotions/specialoffers/FCAC" };
	
	private String[] testRequests = {
			"/appointment/services",
			"/maintenance/history/search?storeId=013854&invoiceId=139214&make=toyota&model=corolla+ce",
			"/maintenance/history/search?phone=5592771545&year=2005&make=ford&model=ranger",
			"/maintenance/history/901041103",
			"/maintenance/scheduled/categories/121916",
			"/maintenance/scheduled/types/121916",
			"/maintenance/scheduled/required/121916",
			"/maintenance/scheduled/periodic/121916",
			"/maintenance/scheduled/checks/121916",
			"/maintenance/scheduled/milestones/121916/ALL",
			"/promotions/specialoffers/FCAC" };
	
	private String[] prodRequests = {

	};

	@Override
	@RequireValidToken
	public Response testAll(HttpHeaders headers, HttpServletRequest request) {

		String env = StringUtils.trimToEmpty(request.getParameter("env"));

		String[] requests = null;
		if ("prod".equals(env))
			requests = prodRequests;
		else if ("test".equals(env))
			requests = testRequests;
		else
			requests = devRequests;

		StringBuilder result = new StringBuilder();
		MultivaluedMap<String, String> mvm = headers.getRequestHeaders();
		String ctxPath = request.getContextPath();
		String host = mvm.getFirst("host");
		for (String r : requests) {
			result.append("<p>");
			String url = "http://" + host + ctxPath + r;
			result.append("<b>" + r + "</b><br/>");
			String res = connect(url);
			result.append(res + "</p>");

		}

		ResponseBuilder response = Response.status(Status.ACCEPTED);
		response.entity(result.toString());
		return response.build();
	}
	
	@Override
	@RequireValidToken
	public Response testAppt(HttpHeaders headers, HttpServletRequest request) {
		
		List<AppointmentChoice> choices = new ArrayList<AppointmentChoice>();
		choices.add(new AppointmentChoice("January", "13", "14:00", "drop"));
		
		List<String> services = new ArrayList<String>();
		services.add("1");
		
		CreateAppointment appt = new CreateAppointment();
		appt.setAddress1("200 & 100 S. State");
		appt.setCity("City > Name");
		appt.setFirstName("First < Name <script>alert('test');</script>");
		appt.setLastName("O'Toole");
		appt.setChoices(choices);
		appt.setAcesVehicleId("121916");
		appt.setMileage("10000");
		appt.setServices(services);
		appt.setStoreId("8605");
		
		return appointmentWs.createAppointment(appt, headers);
		
	}

	private String connect(String url) {
		HttpClient client = new HttpClient();
		String responseBody = "";
		GetMethod method = new GetMethod(url);
		try {
			method.addRequestHeader("tokenId",
					"20338ccf-89e9-40c1-bd61-ebfbe3bb0cab");
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				return ("Method failed: " + method.getStatusLine());
			}

			byte[] responseBytes = method.getResponseBody();

			responseBody = new String(responseBytes);

		} catch (Exception E) {

		} finally {
			method.releaseConnection();
		}
		return responseBody;
	}

}
