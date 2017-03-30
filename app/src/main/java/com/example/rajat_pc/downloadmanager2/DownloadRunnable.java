package com.example.rajat_pc.downloadmanager2;

/**
 * Created by RAJAT-PC on 28-03-2017.
 */
public class DownloadRunnable implements Runnable{

    DownloadTask downloadTask;

    DownloadRunnable(DownloadTask dt){
        downloadTask = dt;
    }

    public void run() {

        //Verify URL, if it is invalid then set status to URL_INVALID and updateDownload(0);
        // else
        downloadTask.setStatus(downloadTask.getDownloadManager().DOWNLOAD_RUNNING);
        downloadTask.setCurrentThread(Thread.currentThread());
        int i = downloadTask.getDownloaded();
        try {
            while (i <= 100) {
                downloadTask.updateDownload(i);
                i++;
                Thread.sleep(500);
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
            }
            downloadTask.setStatus(downloadTask.getDownloadManager().DOWNLOAD_COMPLETED);
            downloadTask.updateDownload(i);
        }catch(InterruptedException e){
            if(downloadTask.getStatus() != 2 && downloadTask.getStatus() != 5){
                downloadTask.setStatus(downloadTask.getDownloadManager().DOWNLOAD_FAILED);
            }
            downloadTask.updateDownload(i);
        }
    }
}
