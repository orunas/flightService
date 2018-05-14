package test.flightService.model;

import java.time.ZonedDateTime;
import java.util.Date;

public class ConnectionUpdate {
	
	private Connection connection;
	private Date rangeStart;
	private Date rangeEnd;
	private ZonedDateTime requestStartedDate;
	
	public ConnectionUpdate() {}
	public ConnectionUpdate(String id) {
		this.id = id;
	}
	private String id;
	public String getId() {
		return this.id;
	}
	public void setId(String value) {
		this.id = value;
	}
	public Connection getConnection() {
		return this.connection;
	}
	public void setConnection(Connection value) {
		this.connection = value;		
	}
	public Date getRangeStart() {
		return this.rangeStart;		
	}
	public void setRangeStart(Date value) {
		this.rangeStart = value;
	}
	public Date getRangeEnd() {
		return this.rangeEnd;
	}
	public void setRangeEnd(Date value) {
		this.rangeEnd = value;
	}
	public ZonedDateTime getRequestStartedDate() {
		return this.requestStartedDate;
	}
	public void setRequestStartedDate(ZonedDateTime value) {
		this.requestStartedDate = value;
	}
	

}
