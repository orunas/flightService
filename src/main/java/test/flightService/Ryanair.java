package test.flightService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import test.flightService.model.Connection;

@Path("FR")
public class Ryanair {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Connection AddFlights(Connection model) throws Exception 
	{
		//Common.AddFlights("localhost:32403", model.getZonedDateTime(), model.getFromAirportCode(), model.getToAirportCode(), "http://localhost:3030/Test2");
		return model;
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Connection GetFlight() throws Exception	{
		Connection m = new Connection();
	//	m.setDate("2017-09-06T15:27:57+01:00");
		m.setFromAirportCode("VNO");
		m.setToAirportCode("LTN");
		return m;
	}

}
