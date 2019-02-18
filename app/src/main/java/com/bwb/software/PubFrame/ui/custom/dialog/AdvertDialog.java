package com.bwb.software.PubFrame.ui.custom.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bwb.software.PubFrame.R;
import com.bwb.software.PubFrame.utils.GetToast;

/**
 * Created by baiweibo on 2018/11/20.
 */

public class AdvertDialog extends BaseDialogFragment{

    private Button btn_ok;
    private ImageView image;

    String imageUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dialog, container, false);
        //初始化
        btn_ok = (Button) v.findViewById(R.id.btn_ok);
        image=v.findViewById(R.id.adv_image);
        //参数
        Bundle bundle=getArguments();
        if(bundle!=null){
            imageUrl=bundle.getString("photourl");
        }

        //属性设置
        setShowBottom(false);
        //setSize(0,310);
        setMargin(80);
        setAnimStyle(R.style.BaseDialog);
        setDimAmout(1.0f);

        Glide.with(v).load(imageUrl).into(image);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetToast.useString(getActivity(),"你点啥呢...");
                dismiss();
            }
        });

        return v;
    }



    @Override
    public int setUpLayoutId() {
        return R.layout.fragment_dialog;
    }

    @Override
    public void convertView(DialogViewHolder holder, BaseDialogFragment dialog) {

    }
}
