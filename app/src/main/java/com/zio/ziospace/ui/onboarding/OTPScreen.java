package com.zio.ziospace.ui.onboarding;

import static com.zio.ziospace.helpers.Constants.LOG_TAG;
import static com.zio.ziospace.helpers.Constants.REQ_SAVE_PASSWORD;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zio.ziospace.R;
import com.zio.ziospace.datamodels.ModelUser;
import com.zio.ziospace.ui.dashboard.DashboardActivity;
import com.zio.ziospace.common.LoginManager;
import com.zio.ziospace.helpers.Utils;

import java.util.concurrent.TimeUnit;

public class OTPScreen extends AppCompatActivity {

    private TextInputLayout phoneNumView;
    private TextView messageView;
    private ProgressDialog progressDialog;
    private EditText otpView;
    private Button sendOtpButton, verifyButton;
    private Spinner countryCodeSelector;

    private String country_code = "+91";
    private String email, phoneno, password, username, fullname, verificationId;

    private FirebaseAuth mauth;
    private CredentialsClient mCredentialsClient;
    private DatabaseReference userDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_otp);
        //getWindow().setBackgroundDrawableResource(R.drawable.bg_login);

        //hooks
        messageView = findViewById(R.id.message_view);
        otpView = findViewById(R.id.input_otp);
        phoneNumView = findViewById(R.id.input_phone_num);
        sendOtpButton = findViewById(R.id.btn_send_otp);
        verifyButton = findViewById(R.id.btn_verify);
        countryCodeSelector = findViewById(R.id.country_code);


        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        fullname = getIntent().getStringExtra("fullname");

        mauth = FirebaseAuth.getInstance();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("USERINFO");

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("  Signing you up.... ");

        initCountryCodeSelector();

    }

    private void initCountryCodeSelector() {
        String[] countryCodesNums = {"+91", "+1", "+971", "+44", "+880", "+1", "+86", "+49", "+62", "+60", "+92", "+63", "+7", "+94"};
        String[] countryCodesName = {"IND", "USA", "ARE", "GBR", "BGD", "CAN", "CHN", "DEU", "IDN", "MYS", "PAK", "PHL", "RUS", "LKA"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countryCodesName);
        countryCodeSelector.setAdapter(adapter);

        countryCodeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < countryCodesNums.length) {
                    country_code = countryCodesNums[position];
                    phoneNumView.setPrefixText(country_code);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void sendotp(View view) {

        phoneno = phoneNumView.getEditText().getText().toString().trim();

        if (!Utils.checknetwork(this))
            Toast.makeText(this, "Please ensure proper internet connectivity", Toast.LENGTH_SHORT).show();
        else if (validatephno())
            call_for_otp();

    }

    private boolean validatephno() {

        phoneNumView.setError(null);
        String val = phoneNumView.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            phoneNumView.setError("enter your phone number");
            return false;
        } else if ((val.length() < 7) || (val.length() > 13)) {
            phoneNumView.setError("Enter a valid phone number");
            return false;
        } else {
            phoneNumView.setError(null);
            return true;
        }
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            OTPScreen.this.verificationId = verificationId;
            messageView.setText("Please enter the OTP sent to " + phoneno + " to verify ");
            verifyButton.setEnabled(true);

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Log.d(LOG_TAG, "onVerificationCompleted:" + phoneAuthCredential);
            signInWithPhoneAuthCredential(phoneAuthCredential);

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                otpView.setText(code);
                progressDialog.show();
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            sendOtpButton.setEnabled(true);
            messageView.setText("");
            Log.d(LOG_TAG, e.getMessage());
            Toast.makeText(OTPScreen.this, "Something went wrong, please try again later ", Toast.LENGTH_SHORT).show();
        }
    };

    private void call_for_otp() {
        sendOtpButton.setEnabled(false);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mauth)
                        .setPhoneNumber(country_code + phoneno)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        messageView.setText("Please wait...\nyou might be redirected for verification");
    }

    public void signupfor(View view) {
        String code = otpView.getText().toString();
        progressDialog.show();
        verifyotp(code);

    }

    private void verifyotp(String code) {
        PhoneAuthCredential auth = PhoneAuthProvider.getCredential(verificationId, code);
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
                                Toast.makeText(OTPScreen.this, "Invalid OTP !!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    }
                });
    }

    private void createuser() {
        ModelUser user = new ModelUser(username, fullname, email, phoneno, password, "NA");

        userDatabaseReference.child(username).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(LOG_TAG, "setting user in fb went okay");
                LoginManager session = new LoginManager(OTPScreen.this);
                if (session.loginUser(user)) {
                    savein_smartlock();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Log.d(LOG_TAG, "setting user in fb went wrong");
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
                                rae.startResolutionForResult(OTPScreen.this, REQ_SAVE_PASSWORD);
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

        if (requestCode == REQ_SAVE_PASSWORD) {
            open_Dashboard();
        }
    }

}