
package cs636.music.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cs636.music.dao.DbDAO;
import cs636.music.domain.User;
import static cs636.music.dao.DBConstants.USER_TABLE;
import static cs636.music.dao.DBConstants.SYS_TABLE;

public class UserDAO {

	private Connection connection;

	public UserDAO(DbDAO db) {
		connection = db.getConnection();
	}
	
	//adding new user into Database 
	public void insertNewUser(User user) throws SQLException {
		Statement stmt = connection.createStatement();
		int uId = getNextUserID();
		user.setId(uId);
		try {

			String query = "insert into " + USER_TABLE + " (user_id, firstname, lastname, email_address) values ("
					+ user.getId() + ", '" + user.getFirstname() + "', '" + user.getLastname() + "', '"
					+ user.getEmailAddress() + "') ";
			stmt.execute(query);

		} finally {
			stmt.close();
		}
	}
	//finding user information by User ID
	public User getUserInfoByID(long uId) throws SQLException {
		User user = null;
		Statement stmt = connection.createStatement();
		try {
			ResultSet set = stmt.executeQuery(" select * from " + USER_TABLE + " where user_id = " + uId);
			if (set.next()) {
				user = new User();
				user.setId(set.getInt("user_id"));
				user.setFirstname(set.getString("firstname"));
				user.setLastname(set.getString("lastname"));
				user.setEmailAddress(set.getString("email_address"));
				set.close();
			}
		} finally {
			stmt.close();
		}
		return user;
	}
	//finding user information by Email ID
	public User getUserInfoByEmail(String email) throws SQLException {
		User user = null;

		Statement stmt = connection.createStatement();
		try {
			ResultSet set = stmt
					.executeQuery(" select * from " + USER_TABLE + " where email_address = '" + email + "'");
			if (set.next()) {
				user = new User();
				user.setId(set.getInt("user_id"));
				user.setFirstname(set.getString("firstname"));
				user.setLastname(set.getString("lastname"));
				user.setEmailAddress(set.getString("email_address"));
				set.close();
			}
		} finally {
			stmt.close();
		}
		return user;
	}
	
	//incrementing user id in db
	private void advanceUserID() throws SQLException {
		Statement stmt = connection.createStatement();
		try {
			stmt.executeUpdate(" update " + SYS_TABLE + " set user_id = user_id + 1");
		} finally {
			stmt.close();
		}
	}
	//fetching the last userID from db
	public int getNextUserID() throws SQLException {
		int nextUID;
		Statement stmt = connection.createStatement();
		try {
			ResultSet set = stmt.executeQuery(" select user_id from " + SYS_TABLE);
			set.next();
			nextUID = set.getInt("user_id");
		} finally {
			stmt.close();
		}
		advanceUserID(); // the id has been taken, so set +1 for next one
		return nextUID;
	}

}
