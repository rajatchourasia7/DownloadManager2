package com.example.rajat_pc.downloadmanager2;

import android.content.Context;
import android.os.Looper;

import java.sql.Time;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by RAJAT-PC on 28-03-2017.
 */
public class DownloadManager {
    private static final int CORE_POOL_SIZE = 8;
    private static final int MAXIMUM_POOL_SIZE = 8;
    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT;
    public final int DOWNLOAD_NOT_STARTED = 0;
    public final int DOWNLOAD_RUNNING = 1;
    public final int DOWNLOAD_PAUSED = 2;
    public final int DOWNLOAD_COMPLETED = 3;
    public final int DOWNLOAD_FAILED = 4;
    public final int DOWNLOAD_CANCELLED = 5;
    private static DownloadManager downloadManager = null;
    private final BlockingQueue<Runnable> downloadRunnableQueue;
    private final ThreadPoolExecutor downloadThreadPool;
    private Handler handler;
    private ListView downloadListView;
    private Context context;
    static {
        KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
        downloadManager = new DownloadManager();
    }
    public static DownloadManager getInstance(){
        return downloadManager;
    }

    public void setDownloadListView(ListView listView,Context c){
        downloadListView = listView;
        context = c;
    }

    private DownloadManager(){
        downloadRunnableQueue = new LinkedBlockingQueue<Runnable>();
        downloadThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME,
                                                        KEEP_ALIVE_TIME_UNIT, downloadRunnableQueue);
        handler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message input_message){
                DownloadTask downloadTask = (DownloadTask) input_message.obj;
                switch(input_message.what){
                    case DOWNLOAD_NOT_STARTED :
                        break;
                    case DOWNLOAD_RUNNING :
                        TextView tv = downloadTask.getTextView();
                        tv.setText(downloadTask.getDownloaded().toString());
                        int start = downloadListView.getFirstVisiblePosition();
                        int i,j;
                        for(i = start,j = downloadListView.getLastVisiblePosition();i<=j;i++){
                            if(downloadTask == downloadListView.getItemAtPosition(i)){
                                View view = downloadListView.getChildAt(i-start);
                                downloadListView.getAdapter().getView(i,view,downloadListView);
                                break;
                            }
                        }
                        break;
                    case DOWNLOAD_PAUSED :
                        downloadManager.downloadThreadPool.remove(downloadTask.getDownloadRunnable());
                        Toast.makeText(context, "Download paused",Toast.LENGTH_SHORT).show();
                        break;
                    case DOWNLOAD_COMPLETED :
                        downloadManager.downloadThreadPool.remove(downloadTask.getDownloadRunnable());
                        Toast.makeText(context, "Download completed",Toast.LENGTH_SHORT).show();
                        break;
                    case DOWNLOAD_FAILED :
                        downloadManager.downloadThreadPool.remove(downloadTask.getDownloadRunnable());
                        Toast.makeText(context, "Download interrupted",Toast.LENGTH_SHORT).show();
                        break;
                    case DOWNLOAD_CANCELLED :
                        downloadManager.downloadThreadPool.remove(downloadTask.getDownloadRunnable());
                        break;
                }
            }
        };
    }

    public void updateProgress(DownloadTask dt){
        Message message = handler.obtainMessage(dt.getStatus(),dt);
        message.sendToTarget();
    }

    public void startDownload(DownloadTask downloadTask){
        downloadManager.downloadThreadPool.execute(downloadTask.getDownloadRunnable());
    }

    public void pauseDownload(DownloadTask downloadTask){
        if(downloadTask.getStatus() == DOWNLOAD_RUNNING){
            downloadTask.setStatus(DOWNLOAD_PAUSED);
            synchronized (downloadManager){
                Thread thread = downloadTask.getCurrentThread();
                if(thread != null){
                    thread.interrupt();
                }
            }
        }
    }

    public void resumeDownload(DownloadTask downloadTask){
        if(downloadTask.getStatus() == DOWNLOAD_PAUSED){
            downloadTask.setStatus(DOWNLOAD_NOT_STARTED);
            downloadManager.downloadThreadPool.execute(downloadTask.getDownloadRunnable());
        }
    }

    public void cancelDownload(DownloadTask downloadTask){
        if(downloadTask.getStatus() == DOWNLOAD_RUNNING || downloadTask.getStatus() == DOWNLOAD_NOT_STARTED ||
                downloadTask.getStatus() == DOWNLOAD_PAUSED || downloadTask.getStatus() == DOWNLOAD_FAILED){
            downloadTask.setStatus(DOWNLOAD_CANCELLED);
            synchronized (downloadManager){
                Thread thread = downloadTask.getCurrentThread();
                if(thread != null){
                    thread.interrupt();
                }
            }
        }
    }
}
