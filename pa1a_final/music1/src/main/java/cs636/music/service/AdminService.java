package cs636.music.service;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import cs636.music.dao.AdminDAO;
import cs636.music.dao.DbDAO;
import cs636.music.service.data.DownloadData;
import cs636.music.service.data.InvoiceData;

/**
 * 
 * Provide admin level services to user app through accessing DAOs 
 * @author Chung-Hsien (Jacky) Yu
 */
public class AdminService {
	
	private DbDAO db;
	private AdminDAO adminDb;
	
	/**
	 * construct a admin service provider 
	 * @param dbDao
	 */
	public AdminService(DbDAO dbDao, AdminDAO adminDao /* TODO add other Dao's here */) {
		db = dbDao;	
		adminDb = adminDao;
	}
	
	/**
	 * Clean all user table, not product and system table to empty
	 * and then set the index numbers back to 1
	 * @throws ServiceException
	 */
	public void initializeDB()throws ServiceException {
		try {
			db.initializeDb();
		} catch (SQLException e)
		{
			throw new ServiceException("Can't initialize DB: ", e);
		}	
	}
	
	/**
	 * process the invoice
	 * @param invoiceId
	 * @throws ServiceException
	 */
	public void processInvoice(long invoiceId) throws ServiceException {
		System.out.println("TEMP: processing invoice");
	}

	/**
	 * Get a list of all invoices
	 * @return list of all invoices in InvoiceData objects
	 * @throws ServiceException
	 */
	public Set<InvoiceData> getListofInvoices() throws ServiceException {
		System.out.println("TEMP: getting invoices");
		return new HashSet<InvoiceData>();
	}
	
	/**
	 * Get a list of all unprocessed invoices
	 * @return list of all unprocessed invoices in InvoiceData objects
	 * @throws ServiceException
	 */
	public Set<InvoiceData> getListofUnprocessedInvoices() throws ServiceException {
		System.out.println("TEMP: getting unprocessed invoices");
		return new HashSet<InvoiceData>();
	}
	
	/**
	 * Get a list of all downloads
	 * @return list of all downloads
	 * @throws ServiceException
	 */
	public Set<DownloadData> getListofDownloads() throws ServiceException {
		System.out.println("TEMP: getting downloads");
		return new HashSet<DownloadData>();
	}
	
	
	/**
	 * Check login user
	 * @param username
	 * @param password
	 * @return true if useranme and password exist, otherwise return false
	 * @throws ServiceException
	 */
	public Boolean checkLogin(String username,String password) throws ServiceException {
		try {
			return adminDb.findAdminUser(username, password);
		} catch (SQLException e)
		{
			throw new ServiceException("Check login error: ", e);
		}
	}
	
}
