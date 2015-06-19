package moderator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class ModDao {
	private static Map<String, String> contentProvider = new HashMap<String, String>();
	static {
		String url = "jdbc:mysql://127.2.93.2:3306/";
		String dbName = "artistrecommender";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "adminevyJcBF"; 
		String password = "Sx_qT2V3zzMv";
		
		try {
			Class.forName(driver).newInstance(); 
			Connection conn = DriverManager.getConnection(url+dbName,userName,password);
			Statement st = conn.createStatement();
			ResultSet res = st.executeQuery("SELECT * FROM  Moderators");
			
			while (res.next()) {
				String UserFacebookId = res.getString("UserFacebookId");
				contentProvider.put(UserFacebookId, UserFacebookId);
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static Map<String, String> getModel() {
		return contentProvider; 
	}
}
