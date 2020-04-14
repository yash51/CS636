package cs636.music.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import cs636.music.dao.DownloadDAO;
import cs636.music.dao.InvoiceDAO;
import cs636.music.dao.ProductDAO;
import cs636.music.dao.UserDAO;
import cs636.music.domain.Cart;
import cs636.music.domain.CartItem;
import cs636.music.domain.Download;
import cs636.music.domain.Invoice;
import cs636.music.domain.LineItem;
import cs636.music.domain.Product;
import cs636.music.domain.Track;
import cs636.music.domain.User;
import cs636.music.service.ServiceException;
import cs636.music.service.data.CartItemData;
import cs636.music.service.data.InvoiceData;
import cs636.music.service.data.UserData;

public class UserService {

	private DownloadDAO downloadDb;
	private InvoiceDAO invoiceDb;
	private ProductDAO prodDb;
	private UserDAO userDb;

	public UserService(ProductDAO productDao, UserDAO userDao, DownloadDAO downloadDao, InvoiceDAO invoiceDao) {
		downloadDb = downloadDao;
		invoiceDb = invoiceDao;
		prodDb = productDao;
		userDb = userDao;
		
	}
	
	//User Register API Services

	public void registerUser(String first_name, String last_name, String email) throws ServiceException {
		User user = null;

		try {
			user = userDb.getUserInfoByEmail(email);
			if (user == null) { // this user has not registered yet
				user = new User();
				user.setFirstname(first_name);
				user.setLastname(last_name);
				user.setEmailAddress(email);
				userDb.insertNewUser(user);
			}
		} catch (SQLException e) {
			throw new ServiceException("Error: ", e);
		}
	}

	public UserData userInfobyEmail(String email) throws ServiceException {
		User user = null;
		UserData user_data = null;
		try {
			user = userDb.getUserInfoByEmail(email);
			user_data = new UserData(user);
		} catch (SQLException e) {
			throw new ServiceException("Error: ", e);
		}
		return user_data;
	}
	
	public UserData userInfobyUserId(long uId) throws ServiceException {
		User user = null;
		UserData user_data = null;
		try {
			user = userDb.getUserInfoByID(uId);
			user_data = new UserData(user);
		} catch (SQLException e) {
			throw new ServiceException("Error: ", e);
		}
		return user_data;
	}


	//Product API Services

	public Set<Product> findProduct_List() throws ServiceException {
		Set<Product> pro_list;
		try {
			pro_list = prodDb.Products_List();
		} catch (SQLException e) {
			throw new ServiceException("Error: ", e);
		}
		return pro_list;
	}

	public Product Product_Info(String prodCode) throws ServiceException {
		Product pro_info = null;
		try {
			pro_info = prodDb.ProductInfo_pcode(prodCode);
		} catch (SQLException e) {
			throw new ServiceException("Error: ", e);
		}
		return pro_info;
	}

	//Cart API Services

	public Cart createCart() {
		return new Cart();
	}

	public Set<CartItemData> FindCart_Info(Cart cart) throws ServiceException {
		Set<CartItemData> cart_items = new HashSet<CartItemData>();
		try {
			for (CartItem item : cart.getItems()) {
				Product pro_info = prodDb.ProductInfo_pid(item.getProductId());
				CartItemData item_Data = new CartItemData(item.getProductId(), item.getQuantity(), pro_info.getCode(),
						pro_info.getDescription(), pro_info.getPrice());
				cart_items.add(item_Data);
			}
		} catch (Exception e) {
			throw new ServiceException("Error: ", e);
		}
		return cart_items;

	}

	public void insertItem_Cart(Product prod, Cart cart, int quantity) {
		CartItem cart_item = cart.findItem(prod.getId());
		if (cart_item != null) {
			
			int qty = cart_item.getQuantity();
			cart_item.setQuantity(qty + quantity);
			
		} else {
			
			cart_item = new CartItem(prod.getId(), quantity);

			cart.getItems().add(cart_item);
		}
	}

	public void update_CartInfo(Product pro, Cart cart, int quantity) {
		CartItem cart_item = cart.findItem(pro.getId());
		if (cart_item != null) {
			if (quantity > 0) {
				cart_item.setQuantity(quantity);
			} else {
				cart.removeItem(pro.getId());
			}
		}
	}

	public void remove_CartItem(Product pro, Cart cart) {
		CartItem cart_item = cart.findItem(pro.getId());
		if (cart_item != null) {
			
			cart.removeItem(pro.getId());
		}
	}

	public InvoiceData checkout(Cart cart, long uId) throws ServiceException {
		Invoice invoice_data = null;
		try {
			User user = userDb.getUserInfoByID(uId);
			invoice_data = new Invoice(-1, user, new Date(), false, null, null);
			Set<LineItem> lineItems_list = new HashSet<LineItem>();
			for (CartItem item : cart.getItems()) {
				Product pro = prodDb.ProductInfo_pid(item.getProductId());
				LineItem li = new LineItem(pro, invoice_data, item.getQuantity());
				lineItems_list.add(li);
			}
			invoice_data.setLineItems(lineItems_list);
			invoice_data.setTotalAmount(invoice_data.calculateTotal());
			invoiceDb.insertInvoice(invoice_data);
		} catch (SQLException e) {
			throw new ServiceException("Error: ", e);
		}
		cart.clear();
		return new InvoiceData(invoice_data);
	}

	public void Insert_Download_Rec(long uId, Track track) throws ServiceException {
		try {
			Download downloads = new Download();
			User user = userDb.getUserInfoByID(uId);
			downloads.setUser(user);
			downloads.setTrack(track);
			downloadDb.insertDownload(downloads); 
		} catch (SQLException e) {
			throw new ServiceException("Error: ", e);
		}
	}
}
