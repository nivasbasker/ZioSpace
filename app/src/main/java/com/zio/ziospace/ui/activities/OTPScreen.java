package com.zio.ziospace.ui.activities;

import static com.zio.ziospace.helpers.Constants.LOG_TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.auth.api.credentials.CredentialsOptions;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zio.ziospace.R;
import com.zio.ziospace.data.ModelUser;
import com.zio.ziospace.ui.dashboard.DashboardActivity;
import com.zio.ziospace.helpers.LoginManager;
import com.zio.ziospace.helpers.Utils;

import java.util.concurrent.TimeUnit;

public class OTPScreen extends AppCompatActivity {

    TextInputLayout _phno;
    TextView textView;
    ProgressDialog progressDialog;
    private final static int RC_SAVE = 124;

    EditText pinfromuser;
    FirebaseAuth mauth;
    CredentialsClient mCredentialsClient;
    Button send, verify;
    DatabaseReference USERINFO;
    Spinner country;
    String ccode = "+91";
    String email, phoneno, password, username, fullname, codebysys;
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            codebysys = s;
            textView.setText("Please enter the OTP sent to " + phoneno + " to verify ");
            verify.setEnabled(true);

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                pinfromuser.setText(code);
                progressDialog.show();
                verifyotp(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            send.setEnabled(true);
            textView.setText("");
            Log.d(LOG_TAG, e.getMessage());
            Toast.makeText(OTPScreen.this, "Something went wrong, please try again later ", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_otp);
        //getWindow().setBackgroundDrawableResource(R.drawable.bg_login);

        //hooks
        textView = findViewById(R.id.entrotptxt);
        pinfromuser = findViewById(R.id.pin);
        _phno = findViewById(R.id.su_phno);
        send = findViewById(R.id.button3);
        verify = findViewById(R.id.signupbtn);
        country = findViewById(R.id.country);

        String[] items = new String[]{"IND", "USA", "ARE", "GBR", "BGD", "CAN", "CHN", "DEU", "IDN", "MYS", "PAK", "PHL", "RUS", "LKA"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        country.setAdapter(adapter);

        username = getIntent().getStringExtra("_username");
        email = getIntent().getStringExtra("_email");
        password = getIntent().getStringExtra("_password");
        fullname = getIntent().getStringExtra("_fullname");

        mauth = FirebaseAuth.getInstance();
        USERINFO = FirebaseDatabase.getInstance().getReference("USERINFO");

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("  Signing you up.... ");

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        ccode = "+91";
                        break;
                    case 1:
                        ccode = "+1";
                        break;
                    case 2:
                        ccode = "+971";
                        break;
                    case 3:
                        ccode = "+44";
                        break;
                    case 4:
                        ccode = "+880";
                        break;
                    case 5:
                        ccode = "+1";
                        break;
                    case 6:
                        ccode = "+86";
                        break;
                    case 7:
                        ccode = "+49";
                        break;
                    case 8:
                        ccode = "+62";
                        break;
                    case 9:
                        ccode = "+60";
                        break;
                    case 10:
                        ccode = "+92";
                        break;
                    case 11:
                        ccode = "+63";
                        break;
                    case 12:
                        ccode = "+7";
                        break;
                    case 13:
                        ccode = "+94";
                        break;

                }
                _phno.setPrefixText(ccode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void sendotp(View view) {

        phoneno = _phno.getEditText().getText().toString().trim();

        if (!Utils.checknetwork(this))
            Toast.makeText(this, "Please ensure proper internet connectivity", Toast.LENGTH_SHORT).show();
        else if (validatephno())
            call_for_otp();

    }

    private boolean validatephno() {

        String val = _phno.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            _phno.setError("enter your phone number");
            return false;
        } else if ((val.length() < 7) || (val.length() > 13)) {
            _phno.setError("Enter a valid phone number");
            return false;
        } else {
            _phno.setError(null);
            _phno.setErrorEnabled(false);
            return true;
        }
    }

    private void call_for_otp() {
        send.setEnabled(false);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mauth)
                        .setPhoneNumber(ccode + phoneno)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        textView.setText("Please wait...\nyou might be redirected for verification");
    }

    public void signupfor(View view) {
        String code = pinfromuser.getText().toString();
        progressDialog.show();
        verifyotp(code);

    }

    private void verifyotp(String code) {
        PhoneAuthCredential auth = PhoneAuthProvider.getCredential(codebysys, code);
        signInWithPhoneAuthCredential(auth);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success,
                            createuser();

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(OTPScreen.this, "Invalid code !!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    }
                });
    }

    private void createuser() {
        ModelUser user = new ModelUser(username, fullname, email, phoneno, password, "NA");

        USERINFO.child(username).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                LoginManager session = new LoginManager(OTPScreen.this);
                if (session.loginUser(user)) {
                    savein_smartlock();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(OTPScreen.this, "Something went wrong please Try again later ", Toast.LENGTH_SHORT).show();
            }
        });


    }


    public void savein_smartlock() {
        Credential credential = new Credential.Builder(username)
                .setPassword(password)
                //.setAccountType("https://com.codewithzio.zio")
                .build();

        CredentialsOptions options = new CredentialsOptions.Builder()
                .forceEnableSaveDialog()
                .build();

        mCredentialsClient = Credentials.getClient(this, options);

        mCredentialsClient.save(credential).addOnCompleteListener(
                new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            open_Dashboard();
                            return;
                        }

                        Exception e = task.getException();
                        if (e instanceof ResolvableApiException) {
                            // Try to resolve the save request. This will prompt the user if
                            // the credential is new.
                            ResolvableApiException rae = (ResolvableApiException) e;
                            try {
                                rae.startResolutionForResult(OTPScreen.this, RC_SAVE);
                            } catch (IntentSender.SendIntentException exception) {
                                // Could not resolve the request
                                open_Dashboard();
                            }
                        } else {
                            // Request has no resolution , app blocked in smart lock
                            open_Dashboard();
                        }
                    }
                });

    }

    private void open_Dashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SAVE) {
            if (resultCode == RESULT_OK) {
                //saved
            } else {
                //cancelled by user
            }
            open_Dashboard();
        }
    }

}