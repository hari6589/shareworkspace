package com.bsro.api.rest.ws.contact;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import app.bsro.model.appointment.CreateAppointment;
import app.bsro.model.contact.ContactUs;
import app.bsro.model.webservice.BSROWebServiceResponse;

import com.bfrc.dataaccess.model.ValueTextBean;
import com.bfrc.dataaccess.model.appointment.Appointment;
import javax.validation.constraints.NotNull;

/**
 * Controls the creation of appointments with the sending of the appointment to MAI.
 * @author Brad Balmer
 *
 */
@Path(ContactUsWebService.URL)
public interface ContactUsWebService {
	public static final String URL = "/contact";
	
	/**              
	 * Returns all subjects that a contact us can be created for.<br/>
	 * <b>URL: </b>/{URI}/contact/subjects<br/>
	 * <b>Type: </b>GET
	 * @return List<ValueTextBean> a list of ValueTextBean
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/subjects")
	public List<ValueTextBean> subjectList(@Context HttpHeaders headers);
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/us")
	@Produces(MediaType.APPLICATION_JSON)
	public BSROWebServiceResponse createContactUs(ContactUs contactUs, 
			@QueryParam("storeId") final String storeId,
			@QueryParam("acesVehicleId") final Long acesVehicleId,
			@QueryParam("siteName") final String siteName,
			@Context HttpHeaders headers);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/them")
	public ContactUs createContactUsTest(@Context HttpHeaders headers);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/categories/{siteName}")
	public BSROWebServiceResponse categoriesList(@PathParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/inquiries/{siteName}")
	public BSROWebServiceResponse inquiriesList(@PathParam("siteName") final String siteName, @QueryParam("categoryId") String categoryId, @Context HttpHeaders headers);
	
	@POST
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/email-signup")
	public BSROWebServiceResponse emailSignup( @Context HttpHeaders headers, 
			@Context HttpServletRequest request, @QueryParam("siteName") String siteName,
			@NotNull final String data);
			
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/email-signup-get")
	public BSROWebServiceResponse emailSignupFetch( @Context HttpHeaders headers, 
			@Context HttpServletRequest request, @QueryParam("signUpId") String signUpId, 
			@QueryParam("siteName") String siteName);
	
	
	@GET
	@Produces(MediaType.TEXT_PLAIN) 
	@Path("/save-wifi-contact")
	public String saveWifiContact(@Context HttpHeaders headers, @Context HttpServletRequest request, @Context HttpServletResponse response,
			@QueryParam("fname") String firstName, @QueryParam("lname") String lastName, @QueryParam("emailaddress") String emailAddress,
			@QueryParam("source") String source, @QueryParam("callback") String callback, @QueryParam("optin") String optin);
	
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/email-signup-unsubscribe")
	public BSROWebServiceResponse emailSignupUnsubscribe( @Context HttpHeaders headers, 
			@Context HttpServletRequest request, @QueryParam("emailId") String emailId, 
			@QueryParam("siteName") String siteName);
	
}
