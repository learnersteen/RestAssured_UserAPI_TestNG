package pojo;

import java.time.OffsetDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDetails {
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	//private int userId;
	private Integer userId;
	 
    private String userFirstName;
    private String userLastName;
    private Long userContactNumber;
    private String userEmailId;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime creationTime;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime lastModTime;
    
    private UserAddress userAddress;
    
    //Getters and setters 
    
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserFirstName() {
		return userFirstName;
	}
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	public String getUserLastName() {
		return userLastName;
	}
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	public Long getUserContactNumber() {
		return userContactNumber;
	}
	public void setUserContactNumber(Long userContactNumber) {
		this.userContactNumber = userContactNumber;
	}
	public String getUserEmailId() {
		return userEmailId;
	}
	public void setUserEmailId(String userEmailId) {
		this.userEmailId = userEmailId;
	}
	public OffsetDateTime getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(OffsetDateTime creationTime) {
		this.creationTime = creationTime;
	}
	public OffsetDateTime getLastModTime() {
		return lastModTime;
	}
	public void setLastModTime(OffsetDateTime lastModTime) {
		this.lastModTime = lastModTime;
	}
	public UserAddress getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(UserAddress userAddress) {
		this.userAddress = userAddress;
	}
	

	
	

}
