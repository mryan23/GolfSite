package com.example.golfapp;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.util.Log;

class DownloadFileFromURL extends AsyncTask<String, String, String> {
	 
    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //showDialog(progress_bar_type);
    }
    
    String destination;
    public DownloadFileFromURL(String dest)
    {
    	destination = dest;
    }
 
    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();
 
            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
 
            // Output stream to write file
            OutputStream output = new FileOutputStream(destination);
 
            byte data[] = new byte[1024];
 
            long total = 0;
 
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress(""+(int)((total*100)/lenghtOfFile));
 
                // writing data to file
                output.write(data, 0, count);
            }
 
            // flushing output
            output.flush();
 
            // closing streams
            output.close();
            input.close();
 
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
 
        return null;
    }
 
    /**
     * Updating progress bar
     * */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        //pDialog.setProgress(Integer.parseInt(progress[0]));
   }
 
    /**
     * After completing background task
     * Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after the file was downloaded
        //dismissDialog(progress_bar_type);
 
        // Displaying downloaded image into image view
        // Reading image path from sdcard
        // setting downloaded into image view
        //my_image.setImageDrawable(Drawable.createFromPath(imagePath));
    }
 
}