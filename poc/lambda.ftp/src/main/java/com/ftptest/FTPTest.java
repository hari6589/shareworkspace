package com.ftptest;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class FTPTest implements RequestHandler<Object, Object> {

	public Object handleRequest(Object input, Context context) {
		sftpTest();
		return "SUCCESS";
	}
	
	public void sftpTest() {
		//JSch.setLogger(new JSCHLogger()); // More on this below
		JSch jsch = new JSch();
		Session session = null;
		ChannelSftp channel = null;
		FileInputStream localFileStream = null;
		
		String YOUR_KEY_FILE_NAME = "src/main/resources/key.txt";
		String YOUR_SSH_SERVER_USER_NAME = "oracle";
		String YOUR_SSH_SERVER_NAME = "10.142.10.21";
		int YOUR_SSH_SERVER_PORT = 22;
		String YOUR_SSH_PASSWORD = "";
		String YOUR_SSH_SERVER_PASSWORD = "";
		String YOUR_SSH_SERVER_FILE_DIRECTORY = "/opt/home/oracle/test";
		String YOUR_LOCAL_FILE_TO_COPY = "D:/test.txt";
		String YOUR_REMOTE_FILE_NAME = "SFTP_TEST.txt";
		String YOUR_KEY_FILE_PASS_PHRASE="puttydsa";
		
		try
		{
			//Use key authentication if it is set, else use password auth
			if (YOUR_KEY_FILE_NAME != null && YOUR_KEY_FILE_NAME != "") {
				
			    Path path = Paths.get(YOUR_KEY_FILE_NAME);
			    byte[] prvKey = Files.readAllBytes(path);
			    
			    jsch.addIdentity(
			    		YOUR_SSH_SERVER_USER_NAME,    // String userName
			            prvKey,          // byte[] privateKey 
			            null,            // byte[] publicKey
			            YOUR_KEY_FILE_PASS_PHRASE.getBytes()  // byte[] passPhrase
			        );
				
				
				session = jsch.getSession(YOUR_SSH_SERVER_USER_NAME, YOUR_SSH_SERVER_NAME, YOUR_SSH_SERVER_PORT);
			} else if (YOUR_SSH_PASSWORD != null && YOUR_SSH_PASSWORD != "") {
				session = jsch.getSession(YOUR_SSH_SERVER_USER_NAME, YOUR_SSH_SERVER_NAME, YOUR_SSH_SERVER_PORT);
				session.setPassword(YOUR_SSH_SERVER_PASSWORD);
			}
			 
			//Make it so we do not do host key checking. Enabling this would require some extra code and maintenance, but would increase security.
			session.setConfig("StrictHostKeyChecking", "no");
			session.setTimeout(15000);
			System.out.println("Connecting...");
			session.connect();
			System.out.println("Connected!"); 
			channel = (ChannelSftp)session.openChannel("sftp");
			channel.connect();
			System.out.println("Channel Connected!");
			if (YOUR_SSH_SERVER_FILE_DIRECTORY != null && YOUR_SSH_SERVER_FILE_DIRECTORY != "") {
				channel.cd(YOUR_SSH_SERVER_FILE_DIRECTORY);
				System.out.println("Directory Changed!");  
			}
			
			File localFile = new File (YOUR_LOCAL_FILE_TO_COPY);
			if (localFile.exists()) {
				localFileStream = new FileInputStream(localFile);
				channel.put(localFileStream, YOUR_REMOTE_FILE_NAME);
				System.out.println("File Moved!");
			} else {
				System.out.println("Local file not found " + localFile.getAbsolutePath());
			}
			
		} catch(Exception e) {
			System.out.println("Exception : " + e.getMessage());
		}
	}
	
}
