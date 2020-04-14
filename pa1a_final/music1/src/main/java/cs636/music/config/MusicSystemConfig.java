package cs636.music.config;

import java.io.PrintWriter;
import java.io.StringWriter;

import cs636.music.dao.AdminDAO;
import cs636.music.dao.DbDAO;
import cs636.music.service.AdminService;

/**
 * @author Betty O'Neil
 *
 * Configure the service objects, shut them down
 * 
 */

public class MusicSystemConfig {

	public static final String SOUND_BASE_URL = "http://www.cs.umb.edu/cs636/music1-setup/sound/";
	// the service objects in use, representing all lower layers to the app
	private static AdminService adminService;
	private static AdminDAO adminDao;	
	private static DbDAO dbDAO;  // contains Connection

//	private static UserService userService;
//	// the lower-level service objects-- you can vary this as desired
//	private static DownloadDAO downloadDao;
//	private static InvoiceDAO invoiceDao;
//	private static LineItemDAO lineItemDao;
//	private static ProductDAO productDao;
//	private static UserDAO userDao;

	// set up service API, data access objects
	public static void configureServices(String dbUrl, String usr, String pw)
		throws Exception {	
		// configure service layer and DAO objects--
		// The service objects get what they need at creation-time
		// This is known as "constructor injection" 

		try {
			if (dbUrl == null)
				System.out.println("configureServices: dbUrl is null (defaulting to H2)");
			else
				System.out.println("configureServices: dbUrl = "+ dbUrl +", usr =" + usr + "pw = "+ pw);
			System.out.println("TEMPORARY (remove this println): Stub implementation of configureServices, does not use DB fully yet");
		
			dbDAO = new DbDAO(dbUrl, usr, pw);
			adminDao= new AdminDAO(dbDAO);			
			adminService = new AdminService(dbDAO, adminDao);
			
			// just a sketch: change this as necessary--
//			productDao = new ProductDAO(dbDAO);
//			userDao = new UserDAO(dbDAO);
//			downloadDao = new DownloadDAO(dbDAO, userDao);		
//			lineItemDao = new LineItemDAO(dbDAO, productDao);	
//			invoiceDao = new InvoiceDAO(dbDAO,lineItemDao);
//			userService = new UserService(productDao,userDao,downloadDao,lineItemDao,invoiceDao);
		} catch (Exception e) {
			System.out.println(exceptionReport(e));
			// e.printStackTrace(); // causes lots of output
			System.out.println("Problem with contacting DB: " + e);
			throw (e); // rethrow to notify caller (caller should print
						// exception details)
		}
	}
	// Compose an exception report
	// and return the string for callers to use
	public static String exceptionReport(Exception e) {
		String message = e.toString(); // exception name + message
		if (e.getCause() != null) {
			message += "\n  cause: " + e.getCause().toString();
			if (e.getCause().getCause() != null)
				message += "\n    cause's cause: "
						+ e.getCause().getCause().toString();
			message += exceptionStackTraceString(e);
		}
		return message;
	}
	
	private static String exceptionStackTraceString(Throwable e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	// When the app exits, the shutdown happens automatically
	// For other cases, call this to free up the JDBC Connection
	public static void shutdownServices() throws Exception {
		dbDAO.close(); // close JDBC connection
	}

	// Let the apps get the business logic layer services
	public static AdminService getAdminService() {
		return adminService;
	}
//
//	public static UserService getUserService() {
//		return userService;
//	}
}
