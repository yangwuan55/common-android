package com.ymr.common.ui.view;

import android.content.Context;
import android.view.View;

/**
 * Created by ymr on 15/11/16.
 */
public class SureDialog {

    private final CommonDialog mCommonDialog;
    private SureDialogListener mSureDialogListener;

    public interface SureDialogListener{
        void onOk();
        void onCancel();
    }

    public SureDialog setSureDialogListener(SureDialogListener sureDialogListener) {
        this.mSureDialogListener = sureDialogListener;
        return this;
    }

    public SureDialog(Context context, final Runnable okRun) {
        mCommonDialog = new CommonDialog(context);
        mCommonDialog.setOnCancelClickListener(new CommonDialog.OnCancelClickListener() {
            @Override
            public void onClick(View view) {
                mCommonDialog.dissmissDialog();
                if (mSureDialogListener != null) {
                    mSureDialogListener.onCancel();
                }
            }
        });
        mCommonDialog.setOnOKClickListener(new CommonDialog.OnOKClickListener() {
            @Override
            public void onClick(View view) {
                mCommonDialog.dissmissDialog();
                okRun.run();
                if (mSureDialogListener != null) {
                    mSureDialogListener.onOk();
                }
            }
        });
    }

    public SureDialog setContent(String string) {
        mCommonDialog.setContent(string);
        return this;
    }

    public void show() {
        mCommonDialog.showDialog();
    }

    public void dismiss() {
        mCommonDialog.dissmissDialog();
    }
}
