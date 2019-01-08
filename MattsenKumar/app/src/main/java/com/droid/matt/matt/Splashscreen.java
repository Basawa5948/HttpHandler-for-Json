package com.droid.matt.matt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class Splashscreen extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        imageView = (ImageView) findViewById(R.id.imgView);

        Thread thread = new Thread(){
            public void run()
            {
                try
                {
                    sleep(4000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent intent=new Intent(Splashscreen.this,Login.class);
                    startActivity(intent);
                }
            }
        };
        thread.start();
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        finish();

    }
}

