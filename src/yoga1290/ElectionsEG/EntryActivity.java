package yoga1290.ElectionsEG;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class EntryActivity extends Activity {
	private EditText te;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        te=new EditText(this);
        te.setText("smth");
		setContentView(te);
    }
}