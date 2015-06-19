package event;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

@Path("/events")
public class EventsResource {
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@GET 
	@Produces(MediaType.APPLICATION_JSON) 
	public Map<String, List<String>> getArtistBrowser() { 
		return EventDao.getModel(); 
	}
	
	@GET
	@Path("/events")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, List<String>> getArtists(){
		return EventDao.getModel();
	}
	
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount(){
		Set<String> keys = new HashSet<String>();
		keys = EventDao.getModel().keySet();
		int count = 0;
		for(String key : keys){
			count += EventDao.getModel().get(key).size();
		}
		return String.valueOf(count);
	}
	
	@SuppressWarnings("serial")
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newArtist(@FormParam("ArtistFacebookId") String ArtistFacebookId,
						@FormParam("FacebookLink") String FacebookLink,
						@Context HttpServletResponse servletResponse) throws IOException{
		if (!EventDao.getModel().containsKey(ArtistFacebookId)){
			EventDao.getModel().put(ArtistFacebookId, new LinkedList<String>(){{add(FacebookLink);}});
		} else {
			EventDao.getModel().get(ArtistFacebookId).add(FacebookLink);
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
            		+ " VALUES ('" + ArtistFacebookId + "', '" + FacebookLink + "')");
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
	}
	
	@POST
	@Path("/delete")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void delArtist(@FormParam("ArtistFacebookId") String ArtistFacebookId,
						@FormParam("FacebookLink") String FacebookLink,
						@Context HttpServletResponse servletResponse) throws IOException{
		try{
			EventDao.getModel().get(ArtistFacebookId).remove(FacebookLink);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		Connection connection = null;
        Statement stmt = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager
                .getConnection("jdbc:mysql://127.2.93.2:3306/artistrecommender", "adminevyJcBF", "Sx_qT2V3zzMv");
             
            stmt = connection.createStatement();
            stmt.execute("DELETE FROM `Events` WHERE ArtistFacebookId="+ArtistFacebookId+" AND FacebookLink="+FacebookLink);
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

	}	
	
	@Path("{event}")
	public EventResource getArtist(@PathParam("event") String id){
		return new EventResource(uriInfo, request, id);
	}
	
}
