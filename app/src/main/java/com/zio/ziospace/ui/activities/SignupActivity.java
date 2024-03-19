package com.zio.ziospace.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zio.ziospace.R;

public class SignupActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 170;
    TextInputLayout _email, _password, _username;
    ProgressDialog progressDialog;

    DatabaseReference USERINFO;
    GoogleSignInClient googleSignInClient;

    String fullname = "", username, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_signup);
        //getWindow().setBackgroundDrawableResource(R.drawable.bg_login);

        //hooks
        _email = findViewById(R.id.su_email);
        _password = findViewById(R.id.su_password);
        _username = findViewById(R.id.su_username);

        g_signin_init();
        USERINFO = FirebaseDatabase.getInstance().getReference("USERINFO");

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("  Validating.... ");
    }

    private void g_signin_init() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void g_signin(View view) {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        //startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {         // @nullable in before Intent variable
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                _email.getEditText().setText(account.getEmail());
                String temp = account.getDisplayName().replace(" ", "_").toLowerCase();
                _username.getEditText().setText(temp.length() > 20 ? temp.substring(0, 20) : temp);
                fullname = account.getGivenName();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("All Set !!")
                        .setMessage("Fill in the remaining fields and click next")
                        .setCancelable(false)
                        .setPositiveButton(" okay ", (dialogInterface, i) -> {
                        });
                AlertDialog ad = builder.create();
                ad.show();

            } catch (ApiException e) {
                Toast.makeText(this, "something went wrong please try again later", Toast.LENGTH_SHORT).show();
                // Google Sign In failed, update UI appropriately

            }
        }
    }

    public void next(View view) {

        progressDialog.show();
        username = _username.getEditText().getText().toString();
        email = _email.getEditText().getText().toString();
        password = _password.getEditText().getText().toString();

        if (validateemail() && validatepassword() && validateusername()) {
            username_exists();
        }

    }

    private boolean validateusername() {

        if (username.isEmpty()) {
            _username.setError("enter your username");
            progressDialog.dismiss();
            return false;
        } else if (!username.matches("\\A\\w{1,30}\\z")) {
            _username.setError("no white spaces allowed");
            progressDialog.dismiss();
            return false;
        } else {
            _username.setError(null);
            _username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateemail() {

        String emailformat = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty()) {
            _email.setError("field can't be empty");
            progressDialog.dismiss();
            return false;
        } else if (!email.matches(emailformat)) {
            _email.setError("enter a valid email id");
            progressDialog.dismiss();
            return false;
        } else {
            _email.setError(null);
            _email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatepassword() {
        String val = _password.getEditText().getText().toString().trim();
        String passformat =
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z0-9])" +      //any letter and number
                        //"(?=.*[@#$%^&+=])" +    //at least 1 special characte
                        // "(?=S+$)" +        //no white spaces
                        ".{4,}" +               //at least 4 characters
                        "$";


        if (val.isEmpty()) {
            _password.setError("field can't be empty");
            progressDialog.dismiss();
            return false;
        } else if (!val.matches(passformat)) {
            _password.setError("minimum 4 characters");
            progressDialog.dismiss();
            return false;
        } else {
            _password.setError(null);
            _password.setErrorEnabled(false);
            return true;
        }

    }

    private void username_exists() {

        Query checkuser = USERINFO.orderByChild("username").equalTo(username);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    _username.setError("username already exists");
                    progressDialog.dismiss();
                } else {
                    _username.setError(null);
                    _username.setErrorEnabled(false);
                    proceed_to_otp();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(SignupActivity.this, "something went wrong please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void proceed_to_otp() {

        final String email, password, username;
        username = _username.getEditText().getText().toString().trim().toLowerCase();
        email = _email.getEditText().getText().toString().trim();
        password = _password.getEditText().getText().toString().trim();
        fullname = fullname.isEmpty() ? username : fullname;

        Intent i = new Intent(this, OTPScreen.class);
        i.putExtra("_username", username);
        i.putExtra("_email", email);
        i.putExtra("_password", password);
        i.putExtra("_fullname", fullname);
        progressDialog.dismiss();
        startActivity(i);
        finish();
    }

    public void login(View view) {
        google_logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void google_logout() {

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            GoogleSignInClient googleSignInClient;
            GoogleSignInOptions gso2 = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .build();
            googleSignInClient = GoogleSignIn.getClient(this, gso2);
            googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    _email.getEditText().setText("");
                    _username.getEditText().setText("");
                    _password.getEditText().setText("");
                    //Toast.makeText(SignupActivity.this, "signed out", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


}