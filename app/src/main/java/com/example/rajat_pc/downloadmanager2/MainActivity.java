package com.example.rajat_pc.downloadmanager2;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ListView listView;
    private EditText editText;
    private Button button;
    private ArrayList<DownloadTask> downloads;
    private CustomAdapter customAdapter;
    private static DownloadManager downloadManager;
    private String[] statuses = {"DOWNLOAD_NOT_STARTED","DOWNLOAD_RUNNING","DOWNLOAD_PAUSED","DOWNLOAD_COMPLETED"
                                    ,"DOWNLOAD_FAILED","DOWNLOAD_CANCELLED"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.downloadlist);
        editText = (EditText) findViewById(R.id.urltext);
        button = (Button) findViewById(R.id.downloadbtn);
        downloads = new ArrayList<DownloadTask>();
        customAdapter = new CustomAdapter(downloads,this);
        listView.setAdapter(customAdapter);
        downloadManager = DownloadManager.getInstance();
        downloadManager.setDownloadListView(listView,this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showAlertDialog((DownloadTask) listView.getItemAtPosition(i));
            }
        });
    }

    public void downloadFile(View view){
        String downloadURL = editText.getText().toString();
        editText.setText("");
        new VerifyURLTask().execute(downloadURL);
    }

    public void showAlertDialog(final DownloadTask clickeditem){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup,null);

        TextView urltv = (TextView) dialogView.findViewById(R.id.popurl);
        TextView filenametv = (TextView) dialogView.findViewById(R.id.popfilename);
        TextView sizetv = (TextView) dialogView.findViewById(R.id.popsize);
        TextView statustv = (TextView) dialogView.findViewById(R.id.popstatus);
        TextView downloadedtv = (TextView) dialogView.findViewById(R.id.popdownloaded);

        urltv.setText(clickeditem.getURL());
        filenametv.setText(clickeditem.getFileName());
        sizetv.setText(clickeditem.getSize().toString());
        statustv.setText(statuses[clickeditem.getStatus()]);
        downloadedtv.setText(clickeditem.getDownloaded().toString());

        Button pausebtn = (Button) dialogView.findViewById(R.id.poppause);
        Button resumebtn = (Button) dialogView.findViewById(R.id.popresume);
        Button restartbtn = (Button) dialogView.findViewById(R.id.poprestart);
        Button cancelbtn = (Button) dialogView.findViewById(R.id.popcancel);
        Button deletebtn = (Button) dialogView.findViewById(R.id.popdelete);
        Button backbtn = (Button) dialogView.findViewById(R.id.popback);

        pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadManager.pauseDownload(clickeditem);
            }
        });
        resumebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadManager.resumeDownload(clickeditem);
            }
        });
        restartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadManager.cancelDownload(clickeditem);
                downloadManager.startDownload(clickeditem);
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadManager.cancelDownload(clickeditem);
            }
        });
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadManager.cancelDownload(clickeditem);
                downloads.remove(clickeditem);
                customAdapter.notifyDataSetChanged();
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        switch(clickeditem.getStatus()){
            case 0:
                cancelbtn.setClickable(true);
                deletebtn.setClickable(true);
                break;
            case 1:
                pausebtn.setClickable(true);
                cancelbtn.setClickable(true);
                deletebtn.setClickable(true);
                restartbtn.setClickable(true);
                break;
            case 2:
                resumebtn.setClickable(true);
                cancelbtn.setClickable(true);
                deletebtn.setClickable(true);
                restartbtn.setClickable(true);
                break;
            case 3:
                deletebtn.setClickable(true);
                break;
            case 4:
                restartbtn.setClickable(true);
                cancelbtn.setClickable(true);
                deletebtn.setClickable(true);
                break;
            case 5:
                restartbtn.setClickable(true);
                deletebtn.setClickable(true);
                break;
            default :
                break;
        }

        alertDialog.setView(dialogView);
        //To Prevent dismiss on backKey Press
        alertDialog.setCancelable(false);
        //To Prevent dismiss on outside Touch
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private class VerifyURLTask extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String[] strings) {
            // do URL verification and return result
            if(strings[0].length()>0) {
                return strings[0];
            }
            else{
                return null;
            }
        }

        protected void onPostExecute(String result){
            if(result != null){
                DownloadTask downloadTask = new DownloadTask(result,MainActivity.this);
                downloads.add(downloadTask);
                customAdapter.notifyDataSetChanged();
                downloadManager.startDownload(downloadTask);
            }
            else{
                Toast.makeText(MainActivity.this,"Enter valid URL",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
