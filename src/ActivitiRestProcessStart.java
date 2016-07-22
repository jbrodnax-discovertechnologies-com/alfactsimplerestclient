//java:
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import org.activiti.engine.impl.util.json.JSONObject;

//apache commons:
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

public class ActivitiRestProcessStart {

		//for use with the vagrant PB using demo hostname and everything else standard:
		// http://demo:9090/activiti-app/api/enterprise/process-instances
		@SuppressWarnings("deprecation")
		public static void main(String[] args) {
			//REST authentication
			String username = "demo";
			String password = "abs";
			String userPassword = username + ":" + password;
			String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));;
			String boundary = "===" + System.currentTimeMillis() + "===";
		  try {
			//URL url = new URL("http://demo:9090/activiti-app/api/enterprise/process-instances");
			//URL url = new URL("http://pbdemo.eastus2.cloudapp.azure.com:9090/activiti-app/api/enterprise/process-instances");
			
			  //URL management:
			String url = "http://pbdemo.eastus2.cloudapp.azure.com:9090/activiti-app/api/enterprise/";
			//connection instance:
			CloseableHttpClient client = HttpClients.createDefault();
			HttpPost postConn = new HttpPost(url + "content/raw");
			postConn.setHeader("Authorization", "Basic " + encoding);
			MultipartEntityBuilder entity = MultipartEntityBuilder.create();
			entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			//upload content:
			FileBody fb = new FileBody(new File("C:\\Users\\jbrodnax\\workspace4\\AlfActSimpleRestClient\\bin\\testfile.docx"));
			//add parts
			entity.addPart("file", fb);
			entity.addPart("filename", new StringBody("testfile.docx"));
			HttpEntity postEntity = entity.build();
			postConn.setEntity(postEntity);
			HttpResponse response = client.execute(postConn);

			HttpEntity responseEntity = response.getEntity();
            InputStream instream = responseEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            String result = sb.toString();
			JSONObject myObject = new JSONObject(result);
			String contentId = myObject.getString("id");
			
			CloseableHttpClient pclient = HttpClients.createDefault();
			HttpPost ppostConn = new HttpPost(url + "process-instances");
			ppostConn.setHeader("Authorization", "Basic " + encoding);
			StringEntity params = new StringEntity("{\"processDefinitionId\":"
					+ "\"Correspondence:9:15510\","
					+ "\"name\":\"Does it work?\","
					+ "\"values\":{"
					+ "\"fupCorrespondence\":\"" + contentId +"\","
					+ "\"txtPersonName\":\"Hello Joe\","
					+ "\"txtFileName\":\"Doc01234577\""
					+ "}"
					+ "}");
			ppostConn.addHeader("Content-Type", "application/json");
			ppostConn.setEntity(params);
			HttpResponse presponse = client.execute(ppostConn);
//			HttpEntity responseEntity2 = response.getEntity();
//            InputStream instream2 = responseEntity2.getContent();
//            BufferedReader reader2 = new BufferedReader(new InputStreamReader(instream));
//            String line2 = null;
//            StringBuilder sb2 = new StringBuilder();
//            while ((line = reader2.readLine()) != null) {
//                sb2.append(line2 + "\n");
//            }
//            String result2 = sb2.toString();
//			JSONObject myObject2 = new JSONObject(result);
			System.out.println("Status code:" + presponse.getStatusLine().toString() + " | " + "Content ID: " + contentId) ;

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		 }

	}

}



//
//HashMap<String, Object> variables = new HashMap<String, Object>();
//HashMap<String, Object> values = new HashMap<String, Object>();
//variables.put("name", "helloProcess");
//values.put("txtPersonName", "Hello Joe");
//values.put("txtFileName", "Doc01234577");
//variables.put("values", values);
//Save your file in a separate place using a "blob store" service and pass the URL as a parameter to the submitStartFormData method. 
//You can then use listeners and/or a a system task to create the attachment.
//ProcessEngine processEngine = ProcessEngineConfiguration
//		   .createProcessEngineConfigurationFromResourceDefault()
//		   .buildProcessEngine();
//RuntimeService runtimeService = processEngine.getRuntimeService();
//
//
//ProcessInstanceBuilder pi = runtimeService.createProcessInstanceBuilder();
//pi.addVariable("name","helloProcessName");
//pi.addVariable("values", values);
//ProcessInstance processInstance = pi.processDefinitionId("Correspondence:9:15510").start(); 
//String piID = processInstance.getProcessInstanceId();
//TaskService taskService = processEngine.getTaskService();
//Task task = taskService.newTask();
//