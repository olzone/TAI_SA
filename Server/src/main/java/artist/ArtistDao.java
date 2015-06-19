package artist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class ArtistDao {
	private static Map<String, Artist> contentProvider = new HashMap<String, Artist>();
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
			ResultSet res = st.executeQuery("SELECT * FROM  Artists");
			
			while (res.next()) {
				String name = res.getString("name");
				String photo = res.getString("photo");
				String facebookId = res.getString("facebookId");
				String genre = res.getString("genre");
				String description = res.getString("description");
				Artist artist = new Artist(name, photo, facebookId, genre);
				artist.setDescription(description);
				contentProvider.put(facebookId, artist);
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static Map<String, Artist> getModel() {
		return contentProvider; 
	}
}
