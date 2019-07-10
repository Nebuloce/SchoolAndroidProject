package com.example.nebul.cnaapp;

import android.app.Activity;
import android.os.Bundle;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;


public class NewsSelect extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (mWifi.isConnected() == false && mMobile.isConnected() == false) {
            showErrorView();
        } else {
            setContentView(R.layout.news_select);
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            FileDownloader news = new FileDownloader("http://branko-cirovic.appspot.com/etcapp/news/news.xml", NewsSelect.this);
            news.setOnResultsListener(new AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    Intent newsScreen = new Intent(getApplicationContext(), NewsNewsActivity.class);
                    newsScreen.putExtra("xmlData", output);
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    startActivity(newsScreen);
                    finish();
                }
            });
            news.execute();
        }
    }

    private void showErrorView() {
        setContentView(R.layout.error_layout);
        TextView errorView = (TextView) findViewById(R.id.errorMessage);
        errorView.setText("App cannot connect to network. Check network settings and try again.");

    }
}