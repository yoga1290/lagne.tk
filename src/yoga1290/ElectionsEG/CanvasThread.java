package yoga1290.ElectionsEG;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class CanvasThread extends Thread
{
	private SurfaceHolder _surfaceHolder;
    private CanvasView _CanvasView;
    private boolean _run = false;
    public CanvasThread(SurfaceHolder surfaceHolder, CanvasView panel) {
        _surfaceHolder = surfaceHolder;
        _CanvasView = panel;
    }
 
    public void setRunning(boolean run) {
        _run = run;
    }
 
    @Override
    public void run() 
    {
        Canvas c;
        while (_run) 
        {
            c = null;
            try 
            {
                c = _surfaceHolder.lockCanvas(null);
                synchronized (_surfaceHolder) 
                {
                    _CanvasView.onDraw(c);
                }
                
                try {
    				Thread.sleep(1);
    				if(!_CanvasView.done)
    					_CanvasView.zoom();
    				else
    					Thread.sleep(500); //relax keda showaya :P
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    _surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }
}