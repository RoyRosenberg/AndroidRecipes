package com.example.royrosenberg.loginappfb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.royrosenberg.loginappfb.DM.User;
import com.example.royrosenberg.loginappfb.Services.UserManager;
import com.example.royrosenberg.loginappfb.Utils.MessageBox;


/**
 * Created by Roy.Rosenberg on 17/04/2016.
 */
public class TabProfile extends Fragment {

    private UserManager _userMngr;
    private User _user;
    private View _fragmentView;
    /**/
    private EditText _etFullName;
    private EditText _etEmail;
    private EditText _etDescription;
    private EditText _etPsw, _etPsw2;
    private Button _btnSave;
    private ProgressBar _progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        _userMngr = new UserManager(getContext());

        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        _user = (User) bundle.get("UserObj");

        // Inflate the layout for this fragment
        _fragmentView = inflater.inflate(R.layout.tab_profile, container, false);

        loadControls();

        return _fragmentView;
    }

    private void loadControls() {
        _btnSave = (Button)_fragmentView.findViewById(R.id.btnSingIn);
        _btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile_click(v);
            }
        });
        _progressBar = (ProgressBar)_fragmentView.findViewById(R.id.progressBar);
        _etFullName = (EditText) _fragmentView.findViewById(R.id.etFullName);
        _etEmail = (EditText) _fragmentView.findViewById(R.id.etEmail);
        _etDescription = (EditText) _fragmentView.findViewById(R.id.etDescription);
        _etPsw = (EditText) _fragmentView.findViewById(R.id.etPass);
        _etPsw2 = (EditText) _fragmentView.findViewById(R.id.etPass2);

        if (_user.FullName != null && !_user.FullName.isEmpty())
            _etFullName.setText(_user.FullName);
        if (!_user.Email.isEmpty())
            _etEmail.setText(_user.Email);
        if (_user.Description != null && !_user.Description.isEmpty())
            _etDescription.setText(_user.Description);
    }

    public void saveProfile_click(View view) {
        //check control data
        if (checkControlsBeforeSave() == false) {
            //show message
            MessageBox msgBx = new MessageBox(getContext());
            msgBx.Show("Some information is incorrect\nPlease fix and try again", "Incorrect Data", MessageBox.MessageBoxButtons.OK);
        } else {
            //show progress bar
            _progressBar.setVisibility(View.VISIBLE);

            //save profile
            _userMngr.SaveProfile(_user, new UserManager.SaveProfileEvents() {
                @Override
                public void onSaveFinished(User u) {
                    _progressBar.setVisibility(View.INVISIBLE);
                }
            });

        }
    }

    private boolean checkControlsBeforeSave() {
        String fullName = _etFullName.getText().toString();
        String email = _etEmail.getText().toString();
        String desc = _etDescription.getText().toString();
        String p1 = _etPsw.getText().toString();
        String p2 = _etPsw2.getText().toString();

        if (fullName.isEmpty())
            return false;
        if (email.isEmpty())
            return false;
        if (p1.compareTo(p2) != 0)
            return false;

        _user.Email = email;
        _user.FullName = fullName;
        _user.Description = desc;

        if (!p1.isEmpty())
            _user.Password = p1;

        return true;
    }
}
