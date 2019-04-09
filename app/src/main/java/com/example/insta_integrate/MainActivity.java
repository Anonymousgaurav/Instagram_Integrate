package com.example.insta_integrate;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    LinearLayout llafterloginview;
    Button btnviewinfo, connect;
    InstagramApp mApp;
    HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == InstagramApp.WHAT_FINALIZE) {
                userInfoHashmap = mApp.getUserInfo();
            } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
                Toast.makeText(MainActivity.this, "Check your network.", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApp = new InstagramApp(MainActivity.this, AppConfig.CLIENT_ID, AppConfig.CLIENT_SECRET, AppConfig.CALLBACK_URL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {
            @Override
            public void onSuccess() {
                llafterloginview.setVisibility(View.VISIBLE);
                connect.setVisibility(View.GONE);
                mApp.fetchUserName(handler);
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        llafterloginview = findViewById(R.id.llafterloginview);
        btnviewinfo = findViewById(R.id.btnviewinfo);
        connect = findViewById(R.id.connect);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mApp.authorize();
            }
        });

        btnviewinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Profile Info");

                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = layoutInflater.inflate(R.layout.profile_view, null);
                builder.setView(view);

                TextView tvusername = view.findViewById(R.id.tvusername);
                TextView nooffollowers = view.findViewById(R.id.nooffollowers);
                TextView nooffollowings = view.findViewById(R.id.nooffollowings);

                tvusername.setText(userInfoHashmap.get(InstagramApp.TAG_USERNAME));

                nooffollowers.setText(userInfoHashmap.get(InstagramApp.TAG_FOLLOWS));

                nooffollowings.setText(userInfoHashmap.get(InstagramApp.TAG_FOLLOWED_BY));
                builder.create().show();


            }
        });
    }
}
