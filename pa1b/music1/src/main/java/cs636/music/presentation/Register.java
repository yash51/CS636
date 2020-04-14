package cs636.music.presentation;

import static cs636.music.dao.DBConstants.H2_DRIVER;
import static cs636.music.dao.DBConstants.MYSQL_DRIVER;
import static cs636.music.dao.DBConstants.ORACLE_DRIVER;

import java.io.IOException;
import java.sql.*;

public class Register {


public static void main(String args[]) throws IOException,SQLException{
	Connection conn = null;
	
	PreparedStatement stmt = null;

	Statement st=null;
	 
	String dbUrl = null;
	String usr = null;
	String pw = null;
	if (args.length == 0) {  // no args: run on H2 with test.dat
		 // default to H2, an embedded DB
		dbUrl = "jdbc:h2:~/test-music";
		usr ="test";
		pw="";
			
		// leave dbUrl null, for H2
	} else if (args.length == 3) {
		
		dbUrl = args[0];
		usr = args[1];
		pw = args[2];
	} else if (args.length == 4) {
		
		dbUrl = args[1];
		usr = args[2];
		pw = args[3];	
	} else {
		System.out
				.println("usage:java [<inputFile>] <dbURL> <user> <passwd> ");
		return;
	}
	
	String dbDriverName;
	if (dbUrl.contains("mysql"))
		dbDriverName = MYSQL_DRIVER;
	else if (dbUrl.contains("oracle"))
		dbDriverName = ORACLE_DRIVER;
	else if (dbUrl.contains("h2"))
		dbDriverName = H2_DRIVER;
	else throw new SQLException("Unknown DB URL "+dbUrl);		

	try {
		Class.forName(dbDriverName);   // as done with JDBC before v4
	} catch (Exception e) {
		System.out.println("can't find driver " + dbDriverName);
	}
	
		conn = DriverManager.getConnection(dbUrl,usr,pw);



		int id = 12;
		String fname = ("yash");
		String lname = ("mahant");
		String email = ("yashmahant51@gmail.com");





		try {

			stmt = conn.prepareStatement("Insert into site_user(USER_ID,FIRSTNAME,LASTNAME,EMAIL_ADDRESS,COMPANY_NAME,ADDRESS1,ADDRESS2,CITY,STATE,ZIP,COUNTRY,CREDITCARD_TYPE,CREDITCARD_NUMBER,CREDITCARD_EXPIRATIONDATE) VALUES ('" + id + "','" + fname + "','" + lname + "','" + email + "',null,null,null,null,null,null,null,null,null,null)");
			stmt.executeUpdate();
			st=conn.createStatement();

			ResultSet rs=st.executeQuery("select * from site_user where user_id=12");



			while(rs.next()) {

				System.out.println(rs.getString(1) +"\t" + rs.getString(2)+ "\t" + rs.getString(3));

			}


			System.out.println("success");
			stmt.close();
			conn.close();

		}
		catch(Exception e) {
			
			System.out.println("Something not good");
			
			
		}

		
	
	
	
	}

}

		
		