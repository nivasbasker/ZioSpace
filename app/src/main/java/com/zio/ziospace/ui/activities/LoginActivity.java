package com.zio.ziospace.ui.activities;

import static com.zio.ziospace.helpers.Constants.LOG_TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zio.ziospace.databinding.EntryLoginBinding;
import com.zio.ziospace.ui.dashboard.DashboardActivity;
import com.zio.ziospace.helpers.LoginManager;
import com.zio.ziospace.helpers.Utils;
import com.zio.ziospace.data.ModelUser;

public class LoginActivity extends AppCompatActivity {


    private EntryLoginBinding binding;
    private TextInputLayout username, password;
    private ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = EntryLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        username = binding.lgusername;
        password = binding.lgpassword;
        pbar = binding.pbar;

        pbar.setVisibility(View.INVISIBLE);

    }


    public void userlogin(View view) {
        final String _username = username.getEditText().getText().toString().trim().toLowerCase();
        final String _password = password.getEditText().getText().toString().trim();

        if (!Utils.checknetwork(this)) {
            Toast.makeText(this, "Please Check you internet connectivity", Toast.LENGTH_LONG).show();
        } else if (validatepassword() & validateusername()) {

            pbar.setVisibility(View.VISIBLE);

            //database query
            Query checkuser = FirebaseDatabase.getInstance().getReference("USERINFO").child(_username);
            checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        username.setError(null);
                        username.setErrorEnabled(false);
                        //querying
                        String storedpassword = dataSnapshot.child("password").getValue(String.class);
                        if (storedpassword.equals(_password)) {

                            password.setError(null);
                            password.setErrorEnabled(false);
                            //fetch user data

                            store_credentials(dataSnapshot, _username);

                        } else {

                            pbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "wrong password", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        pbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "username does not exists\nNew User ? Try Signup", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Toast.makeText(this, Constants.FB_ERROR, Toast.LENGTH_SHORT).show();
                    pbar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    private void store_credentials(DataSnapshot dataSnapshot, String _username) {
        ModelUser user = dataSnapshot.getValue(ModelUser.class);
        //create a session
        LoginManager session = new LoginManager(LoginActivity.this);
        if (session.loginUser(user)) {
            Intent intent = new Intent(getBaseContext(), DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        //signinwithempass();

    }

    private boolean validatepassword() {
        String val = password.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            password.setError("field can't be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateusername() {
        String val = username.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            username.setError("enter your username");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    public void signup(View view) {
        startActivity(new Intent(this, SignupActivity.class));
    }

    public void forgotpass(View view) {
        Toast.makeText(LoginActivity.this, "please contact the administrator \n to change password", Toast.LENGTH_LONG).show();
    }

    public void goback(View view) {
        finish();
    }

}