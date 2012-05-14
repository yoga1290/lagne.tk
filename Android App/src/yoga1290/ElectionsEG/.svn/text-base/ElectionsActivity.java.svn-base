package yoga1290.ElectionsEG;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.security.KeyStore.LoadStoreParameter;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.RemoteViews.RemoteView;
import android.widget.TextView;
import android.widget.ViewFlipper;


// gr8 help from http://www.helloandroid.com/tutorials/how-use-canvas-your-android-apps-part-1
class node
{
	int X,Y,R,ind;
	Bitmap img=null;
	public node(int ind,int X,int Y,int R)
	{
		this.X=X;
		this.Y=Y;
		this.R=R;
		this.ind=ind;
	}
}


public class ElectionsActivity extends Activity implements android.view.View.OnClickListener{
	EditText et=null;
	CanvasView cv=null;
	RelativeLayout relative;
	Thread _URLThread=null;
	double lastLongitude,lastLatitude;
	Button ok;
	ElectionsActivity _this=this;
	GoogleAnalyticsTracker tracker;
	
	
	//After creating 
	boolean isResponseRvcd=false;
	String response="";//the response of the URL request thread
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        tracker = GoogleAnalyticsTracker.getInstance();
//         Start the tracker in manual dispatch mode...
        tracker.startNewSession("UA-XXXXXXXX", getApplication());
        tracker.trackPageView("/HomeScreen");
        
 
        
//	  Acquire a reference to the system Location Manager
	    LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

	    // Define a listener that responds to location updates
	    LocationListener locationListener = new LocationListener() 
	    {
	        public void onLocationChanged(Location location) {
	          // Called when a new location is found by the network location provider.
	        	lastLongitude=location.getLongitude();
	        	lastLatitude=location.getLatitude();
	        }

			public void onStatusChanged(String provider, int status, Bundle extras) {}

	        public void onProviderEnabled(String provider) {}

	        public void onProviderDisabled(String provider) {}
	      };

	      
	      
	    // Register the listener with the Location Manager to receive location updates
	      //Try getting location from the Network
	      try{
	    	  locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
	      }catch(Exception e){}
	      
	      // Try getting location from the GPS
	      try{
	    	  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
	      }catch(Exception e){}
	      
	      // Initially get the last known location from GPS, if there were no GPS, try getting it from the Network
	      try{
	    	  Location lastLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    	  lastLatitude=lastLocation.getLatitude();
	    	  lastLongitude=lastLocation.getLongitude();
	      }catch(Exception e)
	      {
	    	  try{
		    	  Location lastLocation=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		    	  lastLatitude=lastLocation.getLatitude();
		    	  lastLongitude=lastLocation.getLongitude();
		      }catch(Exception e2){}
	      }
	      
	      
	      

		    setContentView(R.layout.main);
	      ((Button) findViewById(R.id.button_OK)).setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		if(_URLThread==null)
	    {
			tracker.trackPageView("/request");
			
			_URLThread=new URLThread("http://yoga1290.awardspace.info/lagne.tk/getResponse.php?nid="+((EditText)(findViewById(R.id.TextField_NID))).getText(), this);
			_URLThread.start();
			
			
			//Don't wait for the response, just show the Canvas & wait for the isResponseRvcd flag to be true.
			if(cv==null)
				cv=new CanvasView(_this,_this);
			
			
			this.runOnUiThread(
					new Runnable()
					{
				            public void run()
				            {
				                setContentView(cv);
				            }
					}
			);
			
	    }
	}
	
	 @Override
	  protected void onDestroy() {
	    super.onDestroy();
	    // Stop the tracker when it is no longer needed.
	    tracker.dispatch();
	    tracker.stopSession();	    
	  }
	
}