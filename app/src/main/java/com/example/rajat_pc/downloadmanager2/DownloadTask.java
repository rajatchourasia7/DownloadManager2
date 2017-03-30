package com.example.rajat_pc.downloadmanager2;

import android.content.Context;
import android.widget.TextView;

import java.net.URL;

/**
 * Created by RAJAT-PC on 28-03-2017.
 */
public class DownloadTask {
    private String filename;
    private int size;
    private TextView textView;
    private Integer downloaded;
    private int status;
    private DownloadManager downloadManager;
    private DownloadRunnable downloadRunnable;
    private URL downloadURL;
    private Thread currentThread;

    public DownloadTask(String downloadURL,Context context){
        downloadRunnable = new DownloadRunnable(this);
        downloadManager = DownloadManager.getInstance();
        downloaded = 0;
        status = downloadManager.DOWNLOAD_NOT_STARTED;
        filename = downloadURL;
        textView = new TextView(context);
        textView.setText(downloaded.toString());
    }

    public void setCurrentThread(Thread thread){
        currentThread = thread;
    }

    public Thread getCurrentThread(){
        return currentThread;
    }


    public int getStatus(){
        return status;
    }

    public void setStatus(int s){
        status = s;
    }

    public DownloadManager getDownloadManager(){
        return downloadManager;
    }

    public String getURL(){
        return filename;
    }

    public Integer getSize(){
        return size;
    }

    public void updateDownload(int dl){
        downloaded = dl;
        downloadManager.updateProgress(this);
    }

    public String getFileName(){
        return filename;
    }

    public TextView getTextView(){
        return textView;
    }

    public void setDownloaded(Integer dl){
        downloaded = dl;
    }

    public DownloadRunnable getDownloadRunnable(){
        return downloadRunnable;
    }
    public Integer getDownloaded(){
        return downloaded;
    }
}
