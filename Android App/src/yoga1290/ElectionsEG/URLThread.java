package yoga1290.ElectionsEG;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;

public class URLThread extends Thread
{
	private String url,POSTdata="";
	private ElectionsActivity _ea;
	public URLThread(String url,ElectionsActivity _ea,String POSTdata)
	{
		this.url=url;
		this._ea=_ea;
		this.POSTdata=POSTdata;
	}
	@Override
	public void run()
	{
			boolean pass=true;
			String res="";
//			String urlParameters="nid=12345678901234";
			URL url;
		    HttpURLConnection connection = null;  
		    try {
		      //Create connection
		      url = new URL(this.url);
		      connection = (HttpURLConnection)url.openConnection();
		      connection.setRequestMethod("POST");
		      connection.setRequestProperty("Content-Type", 
		           "application/x-www-form-urlencoded");
					
		      connection.setRequestProperty("Content-Length", "" + 
		               Integer.toString(POSTdata.getBytes().length));
		      connection.setRequestProperty("Content-Language", "en-US");  
					
		      connection.setUseCaches (false);
		      connection.setDoInput(true);
		      connection.setDoOutput(true);

		      //Send request
		      DataOutputStream wr = new DataOutputStream (
		                  connection.getOutputStream ());
		      wr.writeBytes (POSTdata);
		      wr.flush ();
		      wr.close ();

		      //Get Response	
		      InputStream is = connection.getInputStream();
		      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		      String line;
		      StringBuffer response = new StringBuffer(); 
		      while((line = rd.readLine()) != null) {
		        response.append(line);
		        response.append('\r');
		      }
		      rd.close();
		      res= response.toString();

		    } catch (Exception e) {
		    	// Counter for Bad Requests (Google Analytics)
		    	_ea.tracker.trackPageView("/BadRequest");
		      e.printStackTrace();

		    } finally {

		      if(connection != null) {
		        connection.disconnect(); 
		      }
		    }
	    	_ea.response=res;
	    	_ea.isResponseRvcd=true;
	}
}