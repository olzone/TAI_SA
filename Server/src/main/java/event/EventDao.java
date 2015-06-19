package event;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class EventDao {
	private static Map<String, List<String>> contentProvider = new HashMap<String, List<String>>();
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
			ResultSet res = st.executeQuery("SELECT * FROM  Events");
			
			while (res.next()) {
				String ArtistFacebookId = res.getString("ArtistFacebookId");
				LinkedList<String> FacebookLink = new LinkedList<String>(){{add(res.getString("FacebookLink"));}};
				
				
				if(!contentProvider.containsKey(ArtistFacebookId)){
					contentProvider.put(ArtistFacebookId, FacebookLink);
				}
				else{
					contentProvider.get(ArtistFacebookId).addAll(FacebookLink);
				}

			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static Map<String, List<String>> getModel() {
		return contentProvider; 
	}
}
