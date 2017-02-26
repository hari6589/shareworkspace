package com.bsro.api.rest.ws.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import app.bsro.model.webservice.BSROWebServiceRequest;
import app.bsro.model.webservice.BSROWebServiceResponse;

import com.bfrc.dataaccess.model.user.WebSiteUser;
import com.bfrc.dataaccess.svc.user.WebSiteUserService;
import com.bsro.api.rest.ws.user.WebSiteUserWebService;
import com.bsro.api.rest.ws.user.WebSiteUserWebServiceImpl;
import com.bsro.core.model.HttpHeader;

public class WebSiteUserWebServiceImplTest {
	WebSiteUserWebService service;
	WebSiteUserService webSiteUserService;
	
	@Before
	public void setup() {
		service = new WebSiteUserWebServiceImpl();
		webSiteUserService = EasyMock.createMock(WebSiteUserService.class);
		ReflectionTestUtils.setField(service, "webSiteUserService", webSiteUserService);
	}
	
	@After
	public void teardown() {
		webSiteUserService = null;
	}
	
	@Test
	public void testCreateLoginUserFromAnonymousUserBadPassword() throws IOException {
		String email = "barry@sanchez.com";
		String passwordOne = "assword123"; 
		String passwordTwo = "Password123";
		Long userId = Long.valueOf(96);
		
		EasyMock.expect(webSiteUserService.getUser(EasyMock.anyLong())).andReturn(createAnonymousUser());
		
		HttpHeaders headers = EasyMock.createMock(HttpHeaders.class);
		
		BSROWebServiceResponse response = service.createLoginUserFromAnonymousUser(headers, userId, email, passwordOne, passwordTwo);
		
		//Assert.assertEquals("The passwords provided did not match.", response.getErrors().getGlobalErrors().get(0));
	}
	
	
	@Test
	public void testCreateLoginUserFromAnonymousUserDuplicateAccount() throws IOException {
		String email = "barry@sanchez.com";
		String passwordOne = "Password123"; 
		String passwordTwo = "Password123";
		Long userId = Long.valueOf(96);
		
		BSROWebServiceRequest request = new BSROWebServiceRequest();
		request.setRequestPayload(createWebSiteUser());
		
		HttpHeaders headers = EasyMock.createMock(HttpHeaders.class);
		EasyMock.expect(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue())).andReturn(new ArrayList<String>());
		List<String> list = new ArrayList<String>();
		list.add("FCAC");
		EasyMock.expect(headers.getRequestHeader(EasyMock.anyObject(String.class))).andReturn(list);
		EasyMock.replay(headers);
		
		EasyMock.expect(webSiteUserService.getUser((String)EasyMock.anyObject(), (String)EasyMock.anyObject())).andReturn(createWebSiteUser());
		EasyMock.expect(webSiteUserService.createUserFromAnonymous(createWebSiteUser(), email, passwordOne)).andReturn(createWebSiteUser());
		EasyMock.replay(webSiteUserService);
		
		BSROWebServiceResponse response = service.createLoginUserFromAnonymousUser(headers, userId, email, passwordOne, passwordTwo);
		
		//Assert.assertEquals("An account with that email already exists.", response.getErrors().getGlobalErrors().get(0));
	}
	

	private WebSiteUser createWebSiteUser() {
		WebSiteUser user = new WebSiteUser();
		user.setFirstName("Barry");
		user.setLastName("Sanchez");
		user.setAddress("123 Granny Ave.");
		user.setCity("Kenosha");
		user.setState("WI");
		user.setZip("53143");
		user.setPassword("password");
		user.setEmailAddress("barry@sanchez.com");
		user.setUserTypeId(WebSiteUser.LOGGED_IN_USER_TYPE_ID);
		user.setWebSiteUserId(Long.valueOf(1002));
		
		return user;
	}
	
	private WebSiteUser createAnonymousUser() {
		WebSiteUser user = new WebSiteUser();
		user.setWebSiteUserId(Long.valueOf(96));
		user.setUserTypeId(WebSiteUser.COOKIE_USER_TYPE_ID);
		return user;
	}
}
