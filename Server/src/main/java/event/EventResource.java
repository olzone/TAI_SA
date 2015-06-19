package event;

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

public class EventResource {

	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String id;
	
	EventResource(UriInfo uriInfo, Request request, String id){
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;	
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getArtist(){
		List<String> artist = EventDao.getModel().get(id);
		return artist;
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON})
	public List<String> getArtistHTML(){
		List<String> artist = EventDao.getModel().get(id);
		return artist;
	}

	@SuppressWarnings({ "serial", "unused" })
	private Response putAndGetResponse(String artist, String link) {
		Response res;
		if (EventDao.getModel().containsKey(artist)){
			EventDao.getModel().put(artist, new LinkedList<String>(){{add(link);}});
		} else {
			EventDao.getModel().get(artist).add(link);
		}
		
		Connection connection = null;
        Statement stmt = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager
                .getConnection("jdbc:mysql://127.2.93.2:3306/artistrecommender", "adminevyJcBF", "Sx_qT2V3zzMv");
             
            stmt = connection.createStatement();
            stmt.execute("INSERT INTO `artistrecommender`.`Events` (`ArtistFacebookId`, `FacebookLink`) "
            		+ " VALUES ('" + artist + "', '" + link + "')");
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
