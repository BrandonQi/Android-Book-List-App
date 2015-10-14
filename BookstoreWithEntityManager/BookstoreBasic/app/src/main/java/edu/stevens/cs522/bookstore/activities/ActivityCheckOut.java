package edu.stevens.cs522.bookstore.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import edu.stevens.cs522.bookstore.R;

public class ActivityCheckOut extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_checkout, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.checkout_order: {
                Intent intent = getIntent();
                Toast.makeText(this, "check out finished.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
            case R.id.checkout_cancle: {
                setResult(RESULT_CANCELED, getIntent());
                finish();
                return true;
            }
        }
        return false;
    }

}