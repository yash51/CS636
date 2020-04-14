package cs636.music.config;

import java.io.PrintWriter;
import java.io.StringWriter;

import cs636.music.dao.AdminDAO;
import cs636.music.dao.DbDAO;
import cs636.music.dao.DownloadDAO;
import cs636.music.dao.InvoiceDAO;
import cs636.music.dao.LineItemDAO;
import cs636.music.dao.ProductDAO;
import cs636.music.dao.UserDAO;
import cs636.music.service.AdminService;
import cs636.music.service.UserService;

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
	
	private static UserService userService;
	// the lower-level service objects--
	private static DownloadDAO downloadDao;
	private static InvoiceDAO invoiceDao;
	private static LineItemDAO lineItemDao;
	private static ProductDAO productDao;
	private static UserDAO userDao;

	// set up service API, data access objects
	public static void configureServices(String dbUrl, String usr, String pw)
		throws Exception {	
		// configure service layer and DAO objects--
		// The service objects get what they need at creation-time
		// This is known as "constructor injection" 

		try {
			if (dbUrl == null)
				System.out
						.println("configureServices: dbUrl is null, defaulting to H2");
			else
				System.out.println("configureServices: dbUrl = " + dbUrl
						+ ", usr =" + usr + "pw = " + pw);
			dbDAO = new DbDAO(dbUrl, usr, pw);
			adminDao= new AdminDAO(dbDAO);					
			productDao = new ProductDAO(dbDAO);
			userDao = new UserDAO(dbDAO);
			downloadDao = new DownloadDAO(dbDAO, userDao, productDao);
			lineItemDao = new LineItemDAO(dbDAO);
			invoiceDao = new InvoiceDAO(dbDAO, lineItemDao, userDao, productDao);
			// Now have DAOs, provide them to service APIs--
			adminService = new AdminService(dbDAO, downloadDao, invoiceDao, adminDao);
			userService = new UserService(productDao, userDao, downloadDao, invoiceDao);
		} catch (Exception e) {
			System.out.println(exceptionReport(e));
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

	public static UserService getUserService() {
		return userService;
	}
}
