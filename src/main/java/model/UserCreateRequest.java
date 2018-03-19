package model;

public class UserCreateRequest {
    private String name;
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	private String email;
	
	private String password;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getServeyToken() {
		return serveyToken;
	}
	public void setServeyToken(String serveyToken) {
		this.serveyToken = serveyToken;
	}
	private String serveyToken;
}
