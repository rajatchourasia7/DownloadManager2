package com.example.rajat_pc.downloadmanager2;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

/**
 * Created by RAJAT-PC on 29-03-2017.
 */
public class CustomAlertDialog {

    private DownloadTask downloadTask;
    private AlertDialog alertDialog;
    private Context context;
    public CustomAlertDialog(Context c){
        context = c;
        alertDialog = new AlertDialog.Builder(context).create();

        /*final LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.scan_dialog_layout,null);

        final TextView mDialog_ProductId = (TextView) dialogView.findViewById(R.id.Dproduct_id);
        final EditText mDialog_Quantity = (EditText) dialogView.findViewById(R.id.DQuantity);

        mDialog_ProductId.setText(intentResult);
        mDialog_Quantity.setText("1");
        mDialog_Quantity.setSelection(mDialog_Quantity.getText().length());
        Button mSaveButton = (Button) dialogView.findViewById(R.id.dialogSave);
        Button mCancelButton = (Button) dialogView.findViewById(R.id.dialogCancel);
        mSaveButton.setText("ADD");
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mDialog_Quantity.getText().toString().matches("")) {
                    Product product = new Product(mDialog_ProductId.getText().toString(), Integer.parseInt(mDialog_Quantity.getText().toString()));
                    mDbHandler.updateProduct(product);
                    alertDialog.dismiss();
                }
                else {
                    showToast("Please enter valid quantity!");
                }
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setView(dialogView);
        //To Prevent dismiss on backKey Press
        alertDialog.setCancelable(false);
        //To Prevent dismiss on outside Touch
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}*/


    }


}
