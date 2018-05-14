package test.flightService.model;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class Connection {
	private String fromAirportCode;
	private String toAirportCode;
	private String airlineCode;

	private String id;
	public String GetId() {
		return this.id;
	}
	public void SetId(String value) {
		this.id = value;
	}
	
	public String getFromAirportCode(){
		return this.fromAirportCode;		
	}
	public void setFromAirportCode(String value){
		this.fromAirportCode = value;		
	}
	
	//private ZonedDateTime dateTime;	
	//
/*	public String getDate() {
		
		return dateTime.withZoneSameInstant(ZoneOffset.UTC).toString();
	}
	
	@JsonIgnore public ZonedDateTime getZonedDateTime() {
		return this.dateTime;
	}
		public void setDate(String value) {
		//DateTimeFormatter df = DateTimeFormatter.ISO_DATE_TIME;
		this.dateTime = ZonedDateTime.parse(value);		
	}
	*/
	
	public void setAirlineCode(String value) {
		this.airlineCode =value;
	}
	public String getAirlineCode() {
		return this.airlineCode;
	}
	
	

	public String getToAirportCode(){
		return this.toAirportCode;		
	}
	public void setToAirportCode(String value){
		this.toAirportCode = value;		
	}
	public Connection() {
	}
	public Connection(String id) {
		this.id = id;
	}
	/*
	public Model(String fromAirportCode,String  toAirportCode,String date)	{
		this.fromAirportCode = fromAirportCode;
		this.toAirportCode = toAirportCode;
		this.date = date;
	}*/
}
