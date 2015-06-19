package moderator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class ModResource {

	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String id;
	
	ModResource(UriInfo uriInfo, Request request, String id){
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;	
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getArtist(){
		String artist = ModDao.getModel().get(id);
		return artist;
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON})
	public String getArtistHTML(){
		String artist = ModDao.getModel().get(id);
		return artist;
	}

	@SuppressWarnings("unused")
	private Response putAndGetResponse(String artist) {
		Response res;
		if (!ModDao.getModel().containsKey(artist)){
			ModDao.getModel().put(artist, artist);
		}
		
		Connection connection = null;
        Statement stmt = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager
                .getConnection("jdbc:mysql://127.2.93.2:3306/artistrecommender", "adminevyJcBF", "Sx_qT2V3zzMv");
             
            stmt = connection.createStatement();
            stmt.execute("INSERT INTO `artistrecommender`.`Moderators` (`UserFacebookId`) VALUES ('"+artist+"')");
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                stmt.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		
		return Response.created(uriInfo.getAbsolutePath()).build();
	}
	
	
}
