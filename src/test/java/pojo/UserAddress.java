package pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAddress {
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	//private int addressId;
	private Integer addressId; 
	
	
	private String plotNumber;
	private String street;
	private String state;
	private String country;
	private int zipCode;
	
	//getters and setters 
	
	public Integer getAddressId() {
		return addressId;
	}
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}
	public String getPlotNumber() {
		return plotNumber;
	}
	public void setPlotNumber(String plotNumber) {
		this.plotNumber = plotNumber;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getZipCode() {
		return zipCode;
	}
	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}
	
	
	
}
	
	
	
	









