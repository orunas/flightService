package test.flightService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

import test.flightService.model.*;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

@Path("/W6")
public class Wizzair {
	
	@POST 
	@Path("/Connections")
	public String AddConnections(String body) throws Exception {
		
		String dbUrl = "http://localhost:3030/Test2";
        //Common.PostDataStringToHTTPService(dbUrl,body,"text/turtle; charset=utf-8");
		Common.AddConnections("localhost:62386", dbUrl);
		return body;
	}
	
	//@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.APPLICATION_JSON)
	@POST @Path("/Flights")
	public String AddFlights(String body) throws Exception
	//public ConnectionUpdate AddFlights(String body) throws Exception 
	{
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		final Model model = ModelFactory.createDefaultModel();
		//RDFDataMgr.re
        model.read(new ByteArrayInputStream(body.getBytes()), null,"TTL");
        StmtIterator i = model.listStatements();
        Connection conn = null;
        String fromGmtOff = null;
        String toGmtOff = null;
        ConnectionUpdate upd = null;
        Resource mainR = null;
        while (i.hasNext()) {
        	Statement s = i.next();        	
        	Triple t = s.asTriple();     	
        	if (t.getSubject().getNameSpace().equals("http://travelplanning.ex/ConnectionUpdate/")) {
        		if (mainR == null) 
        			mainR = s.getSubject();
        		if (upd==null) {
        			upd = new ConnectionUpdate(t.getSubject().toString()); 
        		}
        		else {
        			if (upd.getId() != t.getSubject().toString())
        			{ 
        				throw new Exception();
        			}        			
        		}
        		if (t.getPredicate().toString().equals("http://travelplanning.ex/RangeStart"))
        			upd.setRangeStart(f.parse(t.getObject().getLiteral().getValue().toString()));
        		if (t.getPredicate().toString().equals("http://travelplanning.ex/RangeEnd"))
        			upd.setRangeEnd(f.parse(t.getObject().getLiteral().getValue().toString()));
        		if (t.getPredicate().toString().equals("http://travelplanning.ex/RequestStartedDate")) {
        			String v = t.getObject().getLiteral().getValue().toString();        			        			
        			upd.setRequestStartedDate(ZonedDateTime.parse(v));
        		}        			
        		if (t.getPredicate().toString().equals("http://travelplanning.ex/FromAirportTimezoneUTCOffset"))
        			fromGmtOff = t.getObject().getLiteral().getValue().toString();
        		if (t.getPredicate().toString().equals("http://travelplanning.ex/ToAirportTimezoneUTCOffset"))
        			toGmtOff = t.getObject().getLiteral().getValue().toString();
        		if (t.getPredicate().toString().equals("http://travelplanning.ex/ConnectionUpdateConnection")) {
        			String cId = t.getObject().toString();
        			String cCode = cId.replace("http://travelplanning.ex/Connection/", "");
        			conn = new Connection(cId);
        			String[] codes = cCode.split("_");
        			conn.setAirlineCode(codes[0]);
        			conn.setFromAirportCode(codes[1]);
        			conn.setToAirportCode(codes[2]);
        			upd.setConnection(conn);
        		}     			
		
        	}
//        	String ns = t.getSubject().getNameSpace();
//        	String r1 = t.getSubject().toString(model);
        }
        String dbUrl = "http://localhost:3030/Test2";
        Common.PostDataStringToHTTPService(dbUrl,body,"text/turtle; charset=utf-8");
		Common.AddFlights("localhost:62386", fromGmtOff,toGmtOff, upd.getRangeStart(), upd.getRangeEnd(), upd.getConnection().getFromAirportCode(), upd.getConnection().getToAirportCode(), dbUrl);
		ZonedDateTime end = ZonedDateTime.now();
		mainR.addProperty(
				model.createProperty("http://travelplanning.ex/", "RequestEndedDate"), 
				model.createTypedLiteral(Calendar.getInstance(TimeZone.getTimeZone("UTC"))));
		body = Common.ModelToString(model);
		Common.PostDataStringToHTTPService(dbUrl,body,"text/turtle; charset=utf-8");
        //return upd;
		return body;
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public test.flightService.model.Connection GetFlight() throws Exception	{
		test.flightService.model.Connection m = new test.flightService.model.Connection();
	//	m.setDate("2017-09-06T15:27:57+01:00");
		m.setFromAirportCode("VNO");
		m.setToAirportCode("LTN");
		return m;
	}

}
