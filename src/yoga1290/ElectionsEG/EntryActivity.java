package yoga1290.ElectionsEG;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

public class EntryActivity extends Activity {
	private EditText te;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll=new LinearLayout(this);
        te=new EditText(this);
        te.setText("smth");
        
        ll.addView(te);
        
		setContentView(te);
    }
}