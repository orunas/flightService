package test.flightService;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jena.Jena;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import org.apache.jena.riot.RDFDataMgr;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Common {

	public static Model GetFlightModelForDate(String host,String fromGmtOff,String toGmtOff,Date dateRangeStart,Date dateRangeEnd,String fromAirportCode,String toAirportCode) throws ClientProtocolException, IOException{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String rangeStartDateString = sdf.format(dateRangeStart);
		String rangeEndDateString = sdf.format(dateRangeEnd);
		//System.out.println(String.format("getting %s", dateString));
		String uri =  String.format(
				"http://%s/api/FlightOffering/?origin=%s&destination=%s&originGtmOff=%s&destinationGtmOff=%s&departureDate=%s&returnDate=%s&departureDateRangeEnd=%s&returnDateRangeEnd=%s",
				host,fromAirportCode,toAirportCode,
				fromGmtOff,toGmtOff,
				rangeStartDateString,rangeStartDateString,rangeEndDateString,rangeEndDateString);
		//return RDFDataMgr.loadModel(uri);
		return Common.LoadModel(uri);
	}
	public static String ModelToString(Model model) throws IOException
	{
		StringWriter sr = new StringWriter();
		model.write(sr,"TTL");
		String res = sr.toString();
		sr.close();
		return res;
	}
	public static void PostDataStringToHTTPService(String url,String data,String contentType) throws ClientProtocolException, IOException{
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		if (contentType!=null)
			post.addHeader("Content-Type",contentType);
		post.setEntity(new StringEntity(data));
		HttpResponse response = httpClient.execute(post);
		int res = response.getStatusLine().getStatusCode();
		//response.getEntity().getContent();
	}
	public static void AddConnections(String sourceUrl,String dbUrl) throws IOException
	{
		//Model model = RDFDataMgr.loadModel(String.format("http://%s/api/Connection/",sourceUrl));
		Model model = Common.LoadModel(String.format("http://%s/api/Connection/",sourceUrl));
		String str = ModelToString(model);
		PostDataStringToHTTPService(dbUrl,str,"text/turtle; charset=utf-8");	
	}
	public static void AddFlights(String host,String fromGmtOff,String toGmtOff, Date dtStart,Date dtEnd,String fromAirportCode,String toAirportCode,String dbUrl) throws IOException
	{
		Model model = GetFlightModelForDate(host,fromGmtOff,toGmtOff,dtStart,dtEnd,fromAirportCode,toAirportCode);		
		String str = ModelToString(model);
		PostDataStringToHTTPService(dbUrl,str,"text/turtle; charset=utf-8");		
	}
	public static Model LoadModel(String urlString) throws ClientProtocolException, IOException {
		LocalDateTime started =LocalDateTime.now();
		System.out.println(String.format("Started: %s",started.toString() ));
		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    String line;
	    StringBuilder result = new StringBuilder();
	    while ((line = rd.readLine()) != null) {
	         result.append(line);
	    }
	    rd.close();
	    String modelInString = result.toString();
	    LocalDateTime dataReceived =LocalDateTime.now();
	    System.out.println(String.format("Data received: %s.  Time %d",dataReceived.toString(),Duration.between(started, dataReceived).toMillis()));
	    final Model model = ModelFactory.createDefaultModel();
	    model.read(new ByteArrayInputStream(modelInString.getBytes()), null, "JSON-LD" );
	    LocalDateTime modelLoaded =LocalDateTime.now();
	    System.out.println(String.format("Data loaded into model: %s.  Time %d",dataReceived.toString(),Duration.between(dataReceived, modelLoaded).toMillis()));
		return model;
	}
}
