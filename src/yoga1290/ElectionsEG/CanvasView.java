package yoga1290.ElectionsEG;

import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.LinearLayout;

public class CanvasView extends SurfaceView implements SurfaceHolder.Callback ,OnTouchListener//,OnClickListener
{
	private CanvasThread canvasthread;
	private ElectionsActivity _ElectionsActivity;
	private	LinearLayout	popView;
	private EditText	popupView_text;
	private int	connectivity[][]=new int[][]{
			{1,5,3},
			{2,6},
			{7},
			{},
			{},
			{},
			{},
			{}
	};
	private LinkedList<node> nodes=new LinkedList<node>();
	private int H=0,W=0;
	private void init()
	{
		Queue<node> q=new LinkedList<node>();
		//Clear old nodes & re-generate them..incase of surfaceChange
		nodes=new LinkedList<node>();
		nodes.add(new node(0,W/2,H/2,	Math.min(W/3, H/3)	));
		q.add(new node(0,W/2,H/2,	Math.min(W/3, H/3)	));
		node cur,newnode;
		int i,dAngle;
		while(!q.isEmpty())
		{
			cur=q.remove();
			dAngle=360/Math.max(connectivity[cur.ind].length,1);
			for(i=1;i<=connectivity[cur.ind].length;i++)
			{
				newnode=new node(connectivity[cur.ind][i-1], (int)(cur.X+Math.cos(dAngle*i)*(cur.R+cur.R/2)), (int)(cur.Y+Math.sin(dAngle*i)*(cur.R+cur.R/2)), cur.R/2);
				if(newnode.ind==3 || newnode.ind==0)
					newnode.img=BitmapFactory.decodeResource(getResources(), R.drawable.pin);
				nodes.add(newnode);
				q.add(newnode);
			}
		}
		
	}
	public CanvasView(Context context,ElectionsActivity ea)
	{
		super(context);
		_ElectionsActivity=ea;
//		init();
		getHolder().addCallback(this);
		canvasthread = new CanvasThread(getHolder(), this);
		setFocusable(true);
		setOnTouchListener(this);
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		H=holder.getSurfaceFrame().height();
		W=holder.getSurfaceFrame().width();
		nodes=new LinkedList<node>();
		init();
		postInvalidate();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		H=holder.getSurfaceFrame().height();
		W=holder.getSurfaceFrame().width();
		init();
		canvasthread.setRunning(true);
        canvasthread.start();
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
	
	private int shiftX=0,shiftY=0,index=-1; //currently displayed index
	public boolean done=true;
	private float	zoomFactor=1;
	public void zoom()
	{
		done=false;
		int pass=0;
		if((nodes.get(index).X+shiftX+1)*zoomFactor<W/2)
			shiftX++;
		else if((nodes.get(index).X+shiftX-1)*zoomFactor>W/2)
			shiftX--;
		else	pass++;

		if((nodes.get(index).Y+shiftY+1)*zoomFactor<H/2)
			shiftY++;
		else if((nodes.get(index).Y+shiftY-1)*zoomFactor>H/2)
			shiftY--;
		else	pass++;


		if((nodes.get(index).R*(zoomFactor+0.1)*2<W/1.5)
				&& (nodes.get(index).R*(zoomFactor+0.1)*2<H/1.5))
			zoomFactor+=0.1;
		else if((nodes.get(index).R*(zoomFactor-0.1)*2>W/1.5)
				&& (nodes.get(index).R*(zoomFactor-0.1)*2>H/1.5))
			zoomFactor-=0.1;
		else	pass++;
		
		if(pass<3)
		{
//			canvasthread.setRunning(true);
			postInvalidate();
		}
	}
	private int getSelectedIndex(int x,int y)
	{
		int nx=0,ny=0,nr=0;
		x=(int)((x/zoomFactor-shiftX));
		y=(int)((y/zoomFactor-shiftY));
		for(int i=0;i<nodes.size();i++)
		{
			nx=nodes.get(i).X;
			ny=nodes.get(i).Y;
			nr=nodes.get(i).R;
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
           	//super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            canvas.drawRect(0, 0,W, H, paint);
            
            paint.setColor(Color.GREEN);
			// set's paint's text size
		//	paint.setTextSize(25);
			
			// smooth's out the edges of what is being drawn
			paint.setAntiAlias(true);
			node cur;
			for(int i=nodes.size()-1;i>=0;i--)
            {
            	cur=nodes.get(i);
            	if(cur.ind==index) continue;
            	
            	paint.setColor(Color.RED);
            	canvas.drawCircle((cur.X+shiftX)*zoomFactor, (cur.Y+shiftY)*zoomFactor, cur.R*zoomFactor, paint);
            	paint.setColor(Color.WHITE);
            	canvas.drawCircle((cur.X+shiftX)*zoomFactor, (cur.Y+shiftY)*zoomFactor, (cur.R*zoomFactor-15), paint);
            	if(cur.img!=null 
            			&& cur.img.getWidth()<=cur.R*zoomFactor*2
            			&& cur.img.getHeight()<=cur.R*zoomFactor*2)
            	{
            		canvas.drawBitmap(cur.img,
            				(cur.X+shiftX)*zoomFactor-cur.img.getWidth()/2, (cur.Y+shiftY)*zoomFactor-cur.img.getHeight()/2
            				, paint);
            	}
            }
            
            if(!done)
            	zoom();
            else
            {
            	if(index==3)
            	{
            		//TODO : node[3] is selected, what action to do?
//            		EntryActivity ea=new EntryActivity();
//            		Intent i = new Intent();
//            		i.setAction(Intent.ACTION_MAIN);
//            		i.addCategory(Intent.CATEGORY_LAUNCHER);
//            		EntryActivity cn = new EntryActivity(_ElectionsActivity.getApplicationContext());//, ElectionsActivity.class);
//            		i.setComponent(cn);
//            		startActivity(i);
            		
            		Intent newActivity=new Intent(_ElectionsActivity,EntryActivity.class);
            		_ElectionsActivity.startActivity(newActivity);
            		
            	}
            }
    }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		index=getSelectedIndex((int)event.getX(),(int)event.getY());
		if(index==-1) return false;
		done=false;
		zoom();
		return false;
	}
}