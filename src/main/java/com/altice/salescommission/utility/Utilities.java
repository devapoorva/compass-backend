package com.altice.salescommission.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class Utilities {
	public Date getStringToDate(String dt) throws ParseException {

		System.out.println("dt = " + dt);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = formatter.parse(dt);
		System.out.println("date = " + date);
		
//		Format f = new SimpleDateFormat("MM-dd-yyyy");
//	    String strDate = f.format(date);
//	    
//	   
//	    
//	    System.out.println("strDate = " + strDate);
//	    
//	    DateFormat formatter1 = new SimpleDateFormat("MM-dd-yyyy");
//		Date date1 = formatter1.parse(strDate);

		return date;
	}
}
