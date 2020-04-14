
package cs636.music.dao;

import static cs636.music.dao.DBConstants.PRODUCT_TABLE;
import static cs636.music.dao.DBConstants.TRACK_TABLE;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import cs636.music.domain.Product;
import cs636.music.domain.Track;

public class ProductDAO {
	private Connection connection;

	public ProductDAO(DbDAO db) {
		connection = db.getConnection();
	}

	public Set<Product> Products_List() throws SQLException {
		Set<Product> products_list = new HashSet<Product>();
		Statement stmt = connection.createStatement();
		try {
			ResultSet set = stmt.executeQuery(" select * from " + PRODUCT_TABLE);
			while (set.next()) {
				Product product = new Product(set.getInt("product_id"), set.getString("product_code"),
						set.getString("product_description"), set.getBigDecimal("product_price"), null);
				products_list.add(product);
			}
			set.close();
		} finally {
			stmt.close();
		}

		return products_list;
	}

	
	public Product ProductInfo_pid(long prod_id) throws SQLException {
		Product products = null;
		Statement stmt = connection.createStatement();
		try {

			ResultSet set = stmt.executeQuery(" select * from " + PRODUCT_TABLE + " p, " + TRACK_TABLE + " t "
					+ " where p.product_id = " + prod_id + " and p.product_id = t.product_id order by t.track_number");
			if (set.next()) { 
				products = new Product(set.getInt("product_id"), set.getString("product_code"),
						set.getString("product_description"), set.getBigDecimal("product_price"), null);
				Set<Track> tracks_list = new HashSet<Track>();
				Track track = new Track();
				track.setId(set.getInt("track_id"));
				track.setProduct(products);
				track.setSampleFilename(set.getString("sample_filename"));
				track.setTitle(set.getString("title"));
				track.setTrackNumber(set.getInt("track_number"));
				tracks_list.add(track);
				
				
				while (set.next()) {
					track = new Track();
					track.setId(set.getInt("track_id"));
					track.setProduct(products);
					track.setSampleFilename(set.getString("sample_filename"));
					track.setTitle(set.getString("title"));
					track.setTrackNumber(set.getInt("track_number"));
					tracks_list.add(track);
				}
				products.setTracks(tracks_list);
			}
			set.close();
		} finally {
			stmt.close();
		}

		return products;
	}

	public Product ProductInfo_pcode(String prodCode) throws SQLException {
		Product prod = null;
		Statement stmt = connection.createStatement();
		try {

			ResultSet set = stmt.executeQuery(" select * from " + PRODUCT_TABLE + " p, " + TRACK_TABLE + " t "
					+ " where p.product_code = '" + prodCode + "'"
					+ " and p.product_id = t.product_id  order by t.track_number");
			if (set.next()) { 
				prod = new Product(set.getInt("product_id"), set.getString("product_code"),
						set.getString("product_description"), set.getBigDecimal("product_price"), null);
				Set<Track> tracks = new HashSet<Track>();
				Track track = new Track();
				track.setId(set.getInt("track_id"));
				track.setProduct(prod);
				track.setSampleFilename(set.getString("sample_filename"));
				track.setTitle(set.getString("title"));
				track.setTrackNumber(set.getInt("track_number"));
				tracks.add(track);
				while (set.next()) {
					track = new Track();
					track.setId(set.getInt("track_id"));
					track.setProduct(prod);
					track.setSampleFilename(set.getString("sample_filename"));
					track.setTitle(set.getString("title"));
					track.setTrackNumber(set.getInt("track_number"));
					tracks.add(track);
				}
				prod.setTracks(tracks);
			}
			set.close();
		} finally {
			stmt.close();
		}

		return prod;
	}


	public Track TrackInfo_tid(int trackId) throws SQLException {
		Product products;
		Track track_list = null;
		Statement stmt = connection.createStatement();
		try {

			ResultSet set = stmt.executeQuery(" select * from " + PRODUCT_TABLE + " p, " + TRACK_TABLE + " t " + " where t.track_id = "
					+ trackId + " and p.product_id = t.product_id order by t.track_number");
			if (set.next()) {
				products = this.ProductInfo_pid(set.getInt("product_id"));
				if (products != null) {
					track_list = products.findTrackbyID(trackId);
				}
			}
			set.close();
		} finally {
			stmt.close();
		}

		return track_list;
	}

}
