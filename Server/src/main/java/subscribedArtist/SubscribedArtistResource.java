package subscribedArtist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import subscribedArtist.SubscribedArtistsDao;

public class SubscribedArtistResource {

	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String id;
	
	SubscribedArtistResource(UriInfo uriInfo, Request request, String id){
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;	
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getArtist(){
		List<String> artist = SubscribedArtistsDao.getModel().get(id);
		return artist;
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON})
	public List<String> getArtistHTML(){
		List<String> artist = SubscribedArtistsDao.getModel().get(id);
		return artist;
	}

	@SuppressWarnings({ "serial", "unused" })
	private Response putAndGetResponse(String user, String artist) {
		Response res;
		if (SubscribedArtistsDao.getModel().containsKey(user)){
			SubscribedArtistsDao.getModel().put(user, new LinkedList<String>(){{add(artist);}});
		} else {
			SubscribedArtistsDao.getModel().get(user).add(artist);
		}
		
		Connection connection = null;
        Statement stmt = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager
                .getConnection("jdbc:mysql://127.2.93.2:3306/artistrecommender", "adminevyJcBF", "Sx_qT2V3zzMv");
             
            stmt = connection.createStatement();
            stmt.execute("INSERT INTO `artistrecommender`.`SubscribedArtists` (`UserFacebookId`, `ArtistFacebookId`) "
            		+ " VALUES ('" + user + "', '" + artist + "')");
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
