package com.ymr.common.ui.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ymr.common.R;
import com.ymr.common.databinding.CommonDialogBinding;


/**
 * Created by wuye on 15/10/17.
 */
public class CommonDialog implements View.OnClickListener{

    private Context mContext;
    private CommonDialogBinding mDataBinding;
    private Dialog mDialog;
    private OnCancelClickListener mCancelClickListener;
    private OnOKClickListener mOKClickListener;

    public CommonDialog(Context context){
        mContext=context;
        mDataBinding= DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.common_dialog,null,false);
        mDataBinding.dialogSure.setOnClickListener(this);
        mDataBinding.dialogSureOnly.setOnClickListener(this);
        mDataBinding.dialogCancel.setOnClickListener(this);
    }

    public interface OnCancelClickListener {
        void onClick(View view);
    }

    public interface OnOKClickListener {
        void onClick(View view);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dialog_sure) {
            mOKClickListener.onClick(v);

        } else if (i == R.id.dialog_sure_only) {
            mOKClickListener.onClick(v);

        } else if (i == R.id.dialog_cancel) {
            mCancelClickListener.onClick(v);

        }
    }

    public void showDialog(){
        Activity activity=(Activity)mContext;
        if(!activity.isFinishing()){
            mDialog = new Dialog(mContext);
            Window window = mDialog.getWindow();
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setContentView(mDataBinding.getRoot());
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawableResource(R.color.transparent);
            WindowManager.LayoutParams lp = mDialog.getWindow()
                    .getAttributes();
            mDialog.getWindow().setAttributes(lp);
            mDialog.show();
        }
    }

    public void setContent(String content){
        mDataBinding.dialogContent.setText(content);
    }

    public void isSureOnly(boolean isSureOnly){
        if (isSureOnly){
            mDataBinding.dialogSureOnly.setVisibility(View.VISIBLE);
            mDataBinding.dialogSureAndCancel.setVisibility(View.GONE);
        }else {
            mDataBinding.dialogSureOnly.setVisibility(View.GONE);
            mDataBinding.dialogSureAndCancel.setVisibility(View.VISIBLE);
        }
    }

    public void setOnCancelClickListener(OnCancelClickListener listener) {
        mCancelClickListener = listener;
    }

    public void setOnOKClickListener(OnOKClickListener listener) {
        mOKClickListener = listener;
    }

    public void dissmissDialog(){
        if(mDialog != null) {
            mDialog.dismiss();
        }
    }
    public void setOkText(String string){
        mDataBinding.dialogSure.setText(string);
        mDataBinding.dialogSureOnly.setText(string);
    }

    public void setCancelText(String string){
        mDataBinding.dialogCancel.setText(string);
    }
}
