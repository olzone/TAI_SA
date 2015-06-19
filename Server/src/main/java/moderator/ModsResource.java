package moderator;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Map;

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

@Path("/moderators")
public class ModsResource {
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@GET 
	@Produces(MediaType.APPLICATION_JSON) 
	public Map<String, String> getArtistBrowser() { 
		return ModDao.getModel(); 
	}
	
	@GET
	@Path("/moderators")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, String> getArtists(){
		return ModDao.getModel();
	}
	
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount(){
		int count = ModDao.getModel().size();
		return String.valueOf(count);
	}
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newArtist(@FormParam("UserFacebookId") String UserFacebookId,
						@Context HttpServletResponse servletResponse) throws IOException{
		
		if (!ModDao.getModel().containsKey(UserFacebookId)){
			ModDao.getModel().put(UserFacebookId, UserFacebookId);
		}
		
		Connection connection = null;
        Statement stmt = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager
                .getConnection("jdbc:mysql://127.2.93.2:3306/artistrecommender", "adminevyJcBF", "Sx_qT2V3zzMv");
             
            stmt = connection.createStatement();
            stmt.execute("INSERT INTO `artistrecommender`.`Moderators` (`UserFacebookId`) VALUES ('"+UserFacebookId+"')");
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
	public void delArtist(@FormParam("UserFacebookId") String UserFacebookId,
						@Context HttpServletResponse servletResponse) throws IOException{
		try{
			ModDao.getModel().remove(UserFacebookId);
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
            stmt.execute("DELETE FROM `Moderators` WHERE UserFacebookId="+UserFacebookId);
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
	
	@Path("{moderator}")
	public ModResource getArtist(@PathParam("moderator") String id){
		return new ModResource(uriInfo, request, id);
	}
	
}
