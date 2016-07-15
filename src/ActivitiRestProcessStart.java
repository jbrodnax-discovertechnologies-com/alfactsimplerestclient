//import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.time.format.DateTimeFormatter;

import org.apache.commons.codec.binary.Base64;

public class ActivitiRestProcessStart {

		//for use with the vagrant PB using demo hostname and everything else standard:
		// http://demo:9090/activiti-app/api/enterprise/process-instances
		public static void main(String[] args) {
			//REST authentication
			String username = "demo";
			String password = "abs";
			String userPassword = username + ":" + password;
			String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));;
			
		  try {

			URL url = new URL("http://demo:9090/activiti-app/api/enterprise/process-instances");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "Basic " + encoding);
			//processDefinitionId may change based upon version
			//name will take a specific format TBD
			//values include The initiator's name and the file name, needed variables in current process
			String input = "{\"processDefinitionId\":\"VACorrespondence:4:12576\","
					+ "\"name\":\"helloProcess\","
					+ "\"values\":{"
					+ "\"txtPersonName\":\"Hello Joe\","
					+ "\"txtFileName\":\"Doc01234577\""
					+ "}"
					+ "}";

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			}

// 	debug to console:
//			BufferedReader br = new BufferedReader(new InputStreamReader(
//					(conn.getInputStream())));
//
//			String output;
//			System.out.println("Output from Server .... \n");
//			while ((output = br.readLine()) != null) {
//				System.out.println(output);
//			}
//
//			conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		 }

	}

}
