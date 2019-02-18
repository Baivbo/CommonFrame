package com.bwb.software.PubFrame.ui.custom.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.bwb.software.PubFrame.utils.GetToast;

/**
 * Created by baiweibo on 2018/11/21.
 */

public class TextDialog extends BaseDialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
        dialog.setTitle("tips");
        dialog.setMessage("This is Dialog");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GetToast.useString(getActivity(),"点确定干哈");
                dismiss();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GetToast.useString(getActivity(),"点取消就没你事了？");
                dismiss();
            }
        });
        return dialog.create();
    }

    @Override
    public int setUpLayoutId() {
        return 0;
    }

    @Override
    public void convertView(DialogViewHolder holder, BaseDialogFragment dialog) {

    }
}
