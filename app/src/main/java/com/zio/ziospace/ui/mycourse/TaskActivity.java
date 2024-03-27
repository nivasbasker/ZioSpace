package com.zio.ziospace.ui.mycourse;

import static com.zio.ziospace.helpers.Constants.REQ_CHOOSE_FILE;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zio.ziospace.R;
import com.zio.ziospace.datamodels.ModelTask;
import com.zio.ziospace.common.LoginManager;

public class TaskActivity extends AppCompatActivity {


    private String CID, USERNAME, selected_file_name;
    ;
    private Uri selected_file_uri;
    LinearLayout result;
    TextView todo, xps, remarks, status;
    Button turn, choose;
    LinearProgressIndicator xpBar;


    private ModelTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_tasks);

        todo = findViewById(R.id.todo);
        status = findViewById(R.id.status_text);
        choose = findViewById(R.id.choose);
        turn = findViewById(R.id.turn_in);
        result = findViewById(R.id.result_lay);
        xps = findViewById(R.id.xps);
        remarks = findViewById(R.id.remarks);
        xpBar = findViewById(R.id.xp_reward);
        result.setVisibility(View.INVISIBLE);

        USERNAME = LoginManager.getUserName(this);

        Intent i = getIntent();
        CID = i.getStringExtra("cid");
        task = i.getParcelableExtra("task");

        todo.setText(task.getTodo());

        load_details();

    }

    private void load_details() {

        DatabaseReference submit_ref = FirebaseDatabase.getInstance().getReference("tasks")
                .child(CID).child(task.getTaskid()).child("submissions").child(USERNAME);
        submit_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    status.setText("Submitted file : " + snapshot.child("filename").getValue(String.class) + " is under review");
                    if (snapshot.child("remark").exists()) {
                        remarks.setText(snapshot.child("remark").getValue(String.class));
                        xps.setText(String.valueOf(snapshot.child("reward").getValue(Integer.class)));
                        result.setVisibility(View.VISIBLE);
                        status.setText("Task Finished !");
                    }
                } else {
                    choose.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TaskActivity.this, "Something went wrong try later", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void tunIn(View view) {
        if (selected_file_uri != null) {
            Toast.makeText(this, "uploading please wait...", Toast.LENGTH_SHORT).show();
            StorageReference task_storage = FirebaseStorage.getInstance().getReference("tasks").child(CID)
                    .child(selected_file_name);
            task_storage.putFile(selected_file_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    task_storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            save_details(uri);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TaskActivity.this, "Something has gone wrong", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    int percent = (int) (100 * (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount()));
                }
            });
        } else {
            Toast.makeText(this, "please choose a file", Toast.LENGTH_SHORT).show();
        }
    }

    private void save_details(Uri uri) {

        DatabaseReference tasks_ref = FirebaseDatabase.getInstance().getReference("tasks").child(CID)
                .child(task.getTaskid()).child("submissions").child(USERNAME);
        tasks_ref.child("fileuri").setValue(uri.toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        tasks_ref.child("filename").setValue(selected_file_name);
                        turn.setEnabled(false);
                        choose.setEnabled(false);
                        Toast.makeText(TaskActivity.this, " File submitted successfully ", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TaskActivity.this, "Something has gone wrong", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void choose(View view) {
        Intent intent = new Intent();
        intent.setType("*/*");
        String[] mimetypes;
        switch (task.getType()) {
            case 2:
                mimetypes = new String[]{"image/*"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                break;
            case 3:
                mimetypes = new String[]{"application/x-python-code", "text/*"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                break;
            case 1:
                mimetypes = new String[]{"application/pdf"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                break;
            case 5:
                mimetypes = new String[]{"video/*"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + task.getType());
        }

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQ_CHOOSE_FILE);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQ_CHOOSE_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                selected_file_uri = data.getData();

                Cursor cursor = null;
                try {
                    cursor = this.getContentResolver()
                            .query(selected_file_uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {

                        selected_file_name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        choose.setText("Replace File");
                        status.setText("Chosen file : " + selected_file_name);
                        turn.setEnabled(true);

                    }
                } finally {
                    cursor.close();
                }
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}