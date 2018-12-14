package com.carpark.mls.carparking.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.carpark.mls.carparking.AppConfig.Navigator;
import com.carpark.mls.carparking.AppConfig.Utils;
import com.carpark.mls.carparking.BuildConfig;
import com.carpark.mls.carparking.R;

public class InfoActivity extends AppCompatActivity {

    private TextView codebounds;
    private TextView version;
    private TextView appName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Información");
        setContentView(R.layout.activity_info);

        onBind();
    }

    private void onBind(){
        codebounds = (TextView)findViewById(R.id.codebounds_text_info);
        version = (TextView)findViewById(R.id.version_info);
        appName = (TextView)findViewById(R.id.appName);


        appName.setTypeface(Utils.setFont(this,"playfair",false));
        codebounds.setTypeface(Utils.setFont(this,"sofia",false));

        version.setText("Version: " + BuildConfig.VERSION_NAME);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Navigator.NavigateToMain(InfoActivity.this);
        return true;
    }
}
