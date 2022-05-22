package xyz.asmallmuseum.android.domain.Messages;

import android.app.Activity;
import android.app.AlertDialog;

public class Message {
    public static void fatalError(Activity mActivity, String message){
        AlertDialog dialog = new AlertDialog.Builder(mActivity).create();

    }
}
