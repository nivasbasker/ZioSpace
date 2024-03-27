package com.zio.ziospace.ui.onboarding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import com.zio.ziospace.helpers.Constants;
import com.zio.ziospace.helpers.Utils;

public class SignupActivity extends AppCompatActivity {

    TextInputLayout emailView, passwordView, usernameView;
    ProgressDialog progressDialog;

    DatabaseReference userDatabaseReference;
    GoogleSignInClient googleSignInClient;

    private String fullname = "", username, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_signup);

        //getWindow().setBackgroundDrawableResource(R.drawable.bg_login);

        //hooks
        emailView = findViewById(R.id.su_email);
        passwordView = findViewById(R.id.su_password);
        usernameView = findViewById(R.id.su_username);

        g_signin_init();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("USERINFO");

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("  Validating.... ");
    }

    public void next(View view) {

        if (validateemail() && validatepassword() && validateusername()) {
            progressDialog.show();
            username_exists();
        }

    }

    private boolean validateusername() {
        username = usernameView.getEditText().getText().toString().trim().toLowerCase();
        if (username.isEmpty()) {
            usernameView.setError("enter your username");
            return false;
        } else if (!username.trim().matches("\\A\\w{1,30}\\z")) {
            usernameView.setError("no white spaces allowed");
            return false;
        } else {
            usernameView.setError(null);
            usernameView.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateemail() {

        email = emailView.getEditText().getText().toString().trim();
        String emailformat = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty()) {
            emailView.setError("field can't be empty");
            return false;
        } else if (!email.matches(emailformat)) {
            emailView.setError("enter a valid email id");
            return false;
        } else {
            emailView.setError(null);
            emailView.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatepassword() {
        password = passwordView.getEditText().getText().toString().trim();
        String passformat =
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z0-9])" +      //any letter and number
                        //"(?=.*[@#$%^&+=])" +    //at least 1 special characte
                        // "(?=S+$)" +        //no white spaces
                        ".{4,}" +               //at least 4 characters
                        "$";


        if (password.isEmpty()) {
            passwordView.setError("field can't be empty");
            return false;
        } else if (!password.matches(passformat)) {
            passwordView.setError("minimum 4 characters");
            return false;
        } else {
            passwordView.setError(null);
            passwordView.setErrorEnabled(false);
            return true;
        }

    }

    private void username_exists() {

        Query checkuser = userDatabaseReference.orderByChild("username").equalTo(username);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    usernameView.setError("username already exists");
                    progressDialog.dismiss();
                } else {
                    usernameView.setError(null);
                    usernameView.setErrorEnabled(false);
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


        fullname = fullname.isEmpty() ? username : fullname;

        Intent i = new Intent(this, OTPScreen.class);
        i.putExtra("username", username);
        i.putExtra("email", email);
        i.putExtra("password", password);
        i.putExtra("fullname", fullname);
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
                    emailView.getEditText().setText("");
                    usernameView.getEditText().setText("");
                    passwordView.getEditText().setText("");
                    //Toast.makeText(SignupActivity.this, "signed out", Toast.LENGTH_LONG).show();
                }
            });
        }
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
        //startActivityForResult(signInIntent, );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {         // @nullable in before Intent variable
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Constants.REQ_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                emailView.getEditText().setText(account.getEmail());
                String temp = account.getDisplayName().replace(" ", "_").toLowerCase();
                usernameView.getEditText().setText(temp.length() > 20 ? temp.substring(0, 20) : temp);
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

}