package artist;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import artist.ArtistDao;

public class ArtistResource {

	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String id;
	
	ArtistResource(UriInfo uriInfo, Request request, String id){
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;	
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Artist getArtist(){
		Artist artist = ArtistDao.getModel().get(id);
		return artist;
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON})
	public Artist getArtistHTML(){
		Artist artist = ArtistDao.getModel().get(id);
		return artist;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putArtist(JAXBElement<Artist> artist){
		Artist a = artist.getValue();
		return putAndGetResponse(a);
	}
	
	@DELETE
	public void deleteArtist(){
		@SuppressWarnings("unused")
		Artist a = ArtistDao.getModel().remove(id);
	}

	private Response putAndGetResponse(Artist artist) {
		Response res;
		if (ArtistDao.getModel().containsKey(artist.getName())){
			res = Response.noContent().build();
		} else {
			res = Response.created(uriInfo.getAbsolutePath()).build();
		}
		ArtistDao.getModel().put(artist.getName(), artist);
		
		return res;
	}
	
	
}
