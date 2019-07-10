package com.example.nebul.cnaapp;

/**
 * Created by admin on 2017-06-12.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bcirovic on 15-12-11.
 */
public class FileDownloader extends AsyncTask<String, Void, String> {
    String src = null;
    String urlString;
    AsyncResponse delegate;
    Context context;
    ProgressDialog progressDialog;

    public FileDownloader(String urlString, Context context) {
        this.urlString = urlString;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            src = readStream(con.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return src;
    }

    @Override
    protected void onPostExecute(String result) {
        /*
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        */

        delegate.processFinish(result);
    }


    @Override
    protected void onPreExecute() {
        /*
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        */
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

    public void setOnResultsListener(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        String line = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }
}

