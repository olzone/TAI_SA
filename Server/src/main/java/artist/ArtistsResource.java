package artist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

@Path("/artists")
public class ArtistsResource {
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@GET 
	@Produces(MediaType.APPLICATION_JSON) 
	public List<Artist> getArtistBrowser() { 
		List<Artist> artists = new ArrayList<Artist>(); 
		artists.addAll(ArtistDao.getModel().values()); 
		return artists;
	}
	
	@GET
	@Path("/artists")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Artist> getArtists(){
		List<Artist> artists = new ArrayList<Artist>(); 
		artists.addAll(ArtistDao.getModel().values()); 
		return artists; 
	}
	
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount(){
		int count = ArtistDao.getModel().size();
		return String.valueOf(count);
	}
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newArtist(@FormParam("name") String name,
						@FormParam("photo") String photo,
						@FormParam("facebookId") String facebookId,
						@FormParam("genre") String genre,
						@FormParam("description") String description,
						@Context HttpServletResponse servletResponse) throws IOException{
		Artist artist = new Artist(name, photo, facebookId, genre);
		if (description != null){
			artist.setDescription(description);
		}
		ArtistDao.getModel().put(name, artist);
		servletResponse.sendRedirect("../index.html");
	}
	
	@Path("{artist}")
	public ArtistResource getArtist(@PathParam("artist") String id){
		return new ArtistResource(uriInfo, request, id);
	}
	
}
