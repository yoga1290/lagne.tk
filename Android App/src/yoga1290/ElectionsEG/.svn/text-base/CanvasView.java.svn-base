package yoga1290.ElectionsEG;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.Queue;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CanvasView extends SurfaceView implements SurfaceHolder.Callback ,OnTouchListener//,OnClickListener
{
	private CanvasThread canvasthread;
	private ElectionsActivity _ElectionsActivity;
	private	LinearLayout	popView;
	private EditText	popupView_text;
	boolean requiresMap=false;

// connectivity[i]= {..children of parent#i..}
	private int	connectivity[][]=new int[][]{
			{1,2,3},
			{},
			{},
			{}
	};

	// each element of nodes will be of type (node)
	// each node will contain
	// [	index,
	//	centerX,centerY,
	//	Radius,
	//	Bitmap Image
	// ]
//	private LinkedList<node> nodes=new LinkedList<node>();
	private node nodes[];

	// Current Height and Width of the canvas...May change if rotation happens!
	private int H=0,W=0;

	//Function that initialize the X,Y,R values for all nodes
	private void init()
	{
		LinkedList<node> nodesLK=new LinkedList<node>();
		
		Queue<node> q=new LinkedList<node>();
		node cur,newnode;
		
		//Clear old nodes & re-generate them..incase of surfaceChange
		nodesLK=new LinkedList<node>();
	
		//Initiallizing the 1st node and it's bitmap image and add it to the queue to start Tree traveral
		cur=new node(0,W/3,H/3,	Math.min(W/3, H/3)	);
		cur.img=BitmapFactory.decodeResource(getResources(), R.drawable.box);
		nodesLK.add(cur);

		//initialize the queue with the root node
		q.add(cur);
		
		int i,dAngle;
		while(!q.isEmpty())
		{
			cur=q.remove();
			// dAngle : Angle difference between a node & another.
			dAngle=360/Math.max(connectivity[cur.ind].length,1);
			for(i=1;i<=connectivity[cur.ind].length;i++)
			{
				// (	node index,
				//	centerX,
				//	centerY,
				//	Radius
				// )
				newnode=new node(connectivity[cur.ind][i-1], (int)(cur.X+Math.cos(dAngle*i)*(cur.R+cur.R/2)), (int)(cur.Y+Math.sin(dAngle*i)*(cur.R+cur.R/2)), cur.R/2);

				//Assign image to some nodes
				if(newnode.ind==3)
					newnode.img=BitmapFactory.decodeResource(getResources(), R.drawable.pin);
				else if(newnode.ind==2)
					newnode.img=BitmapFactory.decodeResource(getResources(), R.drawable.small_website);
				else if(newnode.ind==1)
					newnode.img=BitmapFactory.decodeResource(getResources(), R.drawable.about);
				// Add the new node to the nodes list.
				nodesLK.add(newnode);

				//Add this node to the queue, to be processed,check it's children and so on..
				q.add(newnode);
			}
		}
		//from LinkedList to Array for faster access after the initialization
		this.nodes=new node[nodesLK.size()];
		nodesLK.toArray(this.nodes);
		
	}
	
	// The Constructor of the SurfaceView
	// "ElectionsActivity" in the main Application Activity & "_ElectionsActivity" is to bring it locally in this class.
	public CanvasView(Context context,ElectionsActivity ea)
	{
		super(context);
		_ElectionsActivity=ea;
		getHolder().addCallback(this);

		// CanvasThread works as a Animator timer to re-draw or refresh the canvas from time to time for Animation
		canvasthread = new CanvasThread(getHolder(), this);
//		setFocusable(true);
	}

	// surfaceChanged ,incase you rotate your Android:
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		done=true;
		H=holder.getSurfaceFrame().height();
		W=holder.getSurfaceFrame().width();
		init();
		zoom();
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		H=holder.getSurfaceFrame().height();
		W=holder.getSurfaceFrame().width();
		shiftFactor=Math.min(W, H)*5/100; //1%
		init();
		
		canvasthread.setRunning(true);
        canvasthread.start();
        setOnTouchListener(this);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		//Wait till the tread is dead!
		boolean retry = true;
        canvasthread.setRunning(false);
        while (retry) {
                try {
                        canvasthread.join();
                        retry = false;
                } catch (InterruptedException e) {
                        // we will try it again and again...
                }
        }
	}
	
	private int shiftFactor=1000,shiftX=0,shiftY=0,index=-1; //currently displayed index
	public boolean done=true;
	private float	zoomFactor=1;
	public synchronized void zoom()
	{
		if(index==-1)	return;
		done=false;
		int pass=0;
		if((nodes[index].X+shiftX+shiftFactor)*zoomFactor<W/2)
			shiftX+=shiftFactor;
		else if((nodes[index].X+shiftX-shiftFactor)*zoomFactor>W/2)
			shiftX-=shiftFactor;
		else	pass++;

		if((nodes[index].Y+shiftY+shiftFactor)*zoomFactor<H/2)
			shiftY+=shiftFactor;
		else if((nodes[index].Y+shiftY-shiftFactor)*zoomFactor>H/2)
			shiftY-=shiftFactor;
		else	pass++;


		if((nodes[index].R*(zoomFactor+0.1)*2<W/2)
				&& (nodes[index].R*(zoomFactor+0.1)*2<H/2))
			zoomFactor+=0.1;
		else if((nodes[index].R*(zoomFactor-0.1)*2>W/2)
				&& (nodes[index].R*(zoomFactor-0.1)*2>H/2))
			zoomFactor-=0.1;
		else	pass++;
		
		if(pass<3)
			postInvalidate(); // re-draw
		else
		{
			done=true;
			if(index==3)
				showMap(); //will show the map when it's ready
			else if(index==2)
			{
				 _ElectionsActivity.tracker.dispatch();
				_ElectionsActivity.tracker.stopSession();
				 canvasthread.setRunning(false);
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
		 				Uri.parse("http://elections.eg"));
				_ElectionsActivity.startActivity(intent);
				_ElectionsActivity.finish();
				
			}
			else if(index==1)
			{
				_ElectionsActivity.tracker.trackPageView("AboutView");
				_ElectionsActivity.tracker.dispatch();
				_ElectionsActivity.tracker.stopSession();
				 canvasthread.setRunning(false);
				_ElectionsActivity.runOnUiThread(
						new Runnable()
						{
					            public void run()
					            {
					            	_ElectionsActivity.setContentView(R.layout.about_view);
					            }
						}
				);
			}
		}
	}
	private int getSelectedIndex(int x,int y)
	{
		int nx=0,ny=0,nr=0;
		x=(int)((x/zoomFactor-shiftX));
		y=(int)((y/zoomFactor-shiftY));
		for(int i=0;i<nodes.length;i++)
		{
			nx=nodes[i].X;
			ny=nodes[i].Y;
			nr=nodes[i].R;
			nx=nx-x;
			nx=nx*nx;
			ny-=y;
			ny*=ny;
			nr*=nr;
			
			if(	nx+ny	<=	nr)
				return i;
		}
		return	-1;
	}
	@Override
    public void onDraw(Canvas canvas) {
           	super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            canvas.drawRect(0, 0,W, H, paint);
            
            paint.setColor(Color.GREEN);
			// set's paint's text size
		//	paint.setTextSize(25);
			
			// smooth's out the edges of what is being drawn
			paint.setAntiAlias(true);
			node cur;
			for(int i=nodes.length-1;i>=0;i--)
            {
            	cur=nodes[i];
            	paint.setColor(Color.RED);
            	canvas.drawCircle((cur.X+shiftX)*zoomFactor, (cur.Y+shiftY)*zoomFactor, cur.R*zoomFactor+10, paint);
            	paint.setColor(Color.WHITE);
            	canvas.drawCircle((cur.X+shiftX)*zoomFactor, (cur.Y+shiftY)*zoomFactor, (cur.R*zoomFactor), paint);
            	if(cur.img!=null 
            			&& cur.img.getWidth()<=cur.R*zoomFactor*2
            			&& cur.img.getHeight()<=cur.R*zoomFactor*2)
            	{
            		canvas.drawBitmap(cur.img,
            				(cur.X+shiftX)*zoomFactor-cur.img.getWidth()/2, (cur.Y+shiftY)*zoomFactor-cur.img.getHeight()/2
            				, paint);
            	}
            }
    }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(!done)	return false; //if not done zooming, do nothing
		index=getSelectedIndex((int)event.getX(),(int)event.getY());
		if(index==-1) return false;
		done=false;
		zoom();
		return false;
	}
	
	synchronized void  showMap()
	{
		double latitude_dest=0,longitude_dest=0;
		if(_ElectionsActivity.isResponseRvcd && index==3)
		{
			try{
			requiresMap=false;
			String tmp=_ElectionsActivity.response;
			tmp=tmp.substring(tmp.indexOf("locations"),tmp.length());
			tmp=tmp.substring(tmp.indexOf("lng"),tmp.length());
			tmp=tmp.substring(tmp.indexOf(":"),tmp.length());
                        tmp=tmp.substring(tmp.indexOf("\"")+1,tmp.length());
                        
			 longitude_dest=Double.parseDouble(	tmp.substring(0,tmp.indexOf("\"")) );
			tmp=tmp.substring(tmp.indexOf("lat"),tmp.length());
			tmp=tmp.substring(tmp.indexOf(":"),tmp.length());
                         tmp=tmp.substring(tmp.indexOf("\"")+1,tmp.length());
			 latitude_dest=Double.parseDouble(	tmp.substring(0,tmp.indexOf("\"")) );
			
			 
			 
			 
			 done=true;
			 
			 _ElectionsActivity.tracker.dispatch();
			 _ElectionsActivity.tracker.stopSession();
			 canvasthread.setRunning(false);
			 
			 
			 
			 if(_ElectionsActivity.lastLatitude!=0 && _ElectionsActivity.lastLongitude!=0)
			 {
				 	Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
				 				Uri.parse("http://maps.google.com/maps?saddr="+_ElectionsActivity.lastLatitude+","+_ElectionsActivity.lastLongitude+"&daddr="+latitude_dest+","+longitude_dest));			
				    _ElectionsActivity.startActivity(intent);
			 }
			 else
			 {
				 Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
			 				Uri.parse("http://maps.google.com/maps?saddr="+latitude_dest+","+longitude_dest));
			    _ElectionsActivity.startActivity(intent); 
			 }
			_ElectionsActivity.finish();
			
			
			}catch(Exception e){	
				
				final String err=e.getMessage();
				_ElectionsActivity.runOnUiThread(
						new Runnable()
						{
					            public void run()
					            {
					            	TextView tv=new TextView(getContext().getApplicationContext());
					            	tv.setText("Parsing Resonse Error...\n>"+err+">"+_ElectionsActivity.lastLatitude+","+_ElectionsActivity.lastLongitude);
					            	_ElectionsActivity.setContentView(tv);
					            }
						}
				);	
				
			}
			
			
		}
		else
			requiresMap=true;
	}

	
}
