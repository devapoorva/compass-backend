package com.altice.salescommission.serviceImpl;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl {

	public boolean sendEmail(String subject, String message,String to)
	{
		boolean f = false;
		String from ="justcricket2323@gmail.com";
		//variable for gmail
		String host = "smtp.gmail.com";
		
		//get the system properties
		Properties properties=System.getProperties();
		System.out.println("PROPERTIES"+properties);
		
		//Setting Important information to properties object
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		//Step 1 : To get the session object
		Session session=Session.getInstance(properties, new Authenticator()
				{
					@Override
					protected PasswordAuthentication getPasswordAuthentication()
					{
						return new PasswordAuthentication(from, "justcricket2323@gmail.com");
					}
				});
		
			session.setDebug(true);
			
			//Step 2 : compose the message 
			MimeMessage m = new MimeMessage(session);
			
			try {
				//from email
				m.setFrom(from);
				
				//adding recepient to message
				m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				
				//adding subject to message
				m.setSubject(subject);
				
				//adding text to message
				m.setText(message);
				
				//send
				
				//Step 3 : send the message using Transport class
				Transport.send(m);
				
				System.out.println("Sent success.......");
				f=true;
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			return f;
	}
}
