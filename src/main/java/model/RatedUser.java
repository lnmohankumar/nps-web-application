package model;

public class RatedUser {

	@Override
	public String toString() {
		return "RatedUser [email=" + email + ", name=" + name + ", rating=" + rating + "]";
	}
	String email;
	String name;
	int rating;
	
	public RatedUser(String email, String name, int rating) {
		super();
		this.email = email;
		this.name = name;
		this.rating = rating;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
}
