package com.finalproject.galiroth.myrecipes.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.EventListener;

/**
 * Created by Gali.Roth on 17/04/2016.
 * MessageBox encapsulates AlertDialog
 */
public class MessageBox {

    private Context _context;

    public MessageBox(Context context) {
        _context = context;
    }

    public void Show(String message, String title, int messageBoxButtons, final MessageBoxEvents event) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(_context);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (event != null)
                    event.onOKClick();
            }
        });
        if(messageBoxButtons == MessageBoxButtons.OK_CANCEL) {
            dlgAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (event != null)
                        event.onCancelClick();
                }
            });
        }
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public void Show(String message, String title, int messageBoxButtons) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(_context);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        if(messageBoxButtons == MessageBoxButtons.OK_CANCEL) {
            dlgAlert.setNegativeButton("Cancel", null);
            dlgAlert.setCancelable(true);
        }

        dlgAlert.create().show();
    }

    public interface MessageBoxEvents extends EventListener {
        void onOKClick();

        void onCancelClick();
    }

    public class MessageBoxButtons {
        public static final int OK_CANCEL = 0;
        public static final int OK = 1;

    }
}


