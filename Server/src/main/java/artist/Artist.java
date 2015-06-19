package artist;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Artist {

	private String name;
	private String photo;
	private String facebookId;
	private String genre;
	private String description;
	
	Artist(){}
	Artist(String name, String photo, String facebookId, String genre){
		this.name = name;
		this.photo = photo;
		this.facebookId = facebookId;
		this.genre = genre;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
