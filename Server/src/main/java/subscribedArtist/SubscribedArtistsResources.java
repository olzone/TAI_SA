package subscribedArtist;

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

import event.EventDao;

@Path("/subartists")
public class SubscribedArtistsResources {
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@GET 
	@Produces(MediaType.APPLICATION_JSON) 
	public Map<String, List<String>> getArtistBrowser() { 
		return SubscribedArtistsDao.getModel(); 
	}
	
	@GET
	@Path("/subartists")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, List<String>> getArtists(){
		return SubscribedArtistsDao.getModel();
	}
	
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount(){
		Set<String> keys = new HashSet<String>();
		keys = SubscribedArtistsDao.getModel().keySet();
		int count = 0;
		for(String key : keys){
			count += SubscribedArtistsDao.getModel().get(key).size();
		}
		return String.valueOf(count);
	}
	
	@SuppressWarnings("serial")
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newArtist(@FormParam("userFacebookId") String userFacebookId,
						@FormParam("artistFacebookId") String artistFacebookId,
						@Context HttpServletResponse servletResponse) throws IOException{
		if (!SubscribedArtistsDao.getModel().containsKey(userFacebookId)){
			SubscribedArtistsDao.getModel().put(userFacebookId, new LinkedList<String>(){{add(artistFacebookId);}});
		} else {
			SubscribedArtistsDao.getModel().get(userFacebookId).add(artistFacebookId);
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
            		+ " VALUES ('" + userFacebookId + "', '" + artistFacebookId + "')");
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
	public void delArtist(@FormParam("userFacebookId") String userFacebookId,
						@FormParam("artistFacebookId") String artistFacebookId,
						@Context HttpServletResponse servletResponse) throws IOException{
		try{
			SubscribedArtistsDao.getModel().get(userFacebookId).remove(artistFacebookId);
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
            stmt.execute("DELETE FROM `SubscribedArtists` WHERE UserFacebookId="+userFacebookId+" AND ArtistFacebookId="+artistFacebookId);
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

	@Path("{subartist}")
	public SubscribedArtistResource getArtist(@PathParam("subartist") String id){
		return new SubscribedArtistResource(uriInfo, request, id);
	}
	
}
