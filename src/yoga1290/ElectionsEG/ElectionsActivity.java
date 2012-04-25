package yoga1290.ElectionsEG;



import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;


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

public class ElectionsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(new CanvasView(this,this));
    }
}