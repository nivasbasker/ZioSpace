package com.zio.ziospace.adapter;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import static com.zio.ziospace.helpers.Constants.LOG_TAG;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.zio.ziospace.R;
import com.zio.ziospace.ui.dashboard.DashboardActivity;
import com.zio.ziospace.helpers.Utils;

import java.io.File;
import java.util.List;

public class AdapterResources extends RecyclerView.Adapter<AdapterResources.ViewHolder> {

    private final Context context;
    private final List<String> resourcesList;
    private final File DirectoryRef;
    private final String CourseID;
    private final String[] local_files_list;

    public AdapterResources(Context context, List<String> resourcesList, String CourseID, File DirectoryRef) {
        this.context = context;
        this.resourcesList = resourcesList;
        this.CourseID = CourseID;
        this.DirectoryRef = DirectoryRef;
        local_files_list = DirectoryRef.list();


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_resources, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final String filename = resourcesList.get(position);
        holder.title.setText(filename);

        final boolean type;
        if (local_files_list != null && Utils.stringExistsInArray(local_files_list, filename + ".pdf")) {
            type = true;
            holder.download.setBackgroundResource(R.drawable.icon_checks);
        } else type = false;

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (type)
                    openFile(filename);
                else {
                    download(filename);
                }
            }
        });
        holder.mat_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type)
                    openFile(filename);
                else {
                    Toast.makeText(context, "Please download the file", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void openFile(String title) {
        File pdffile = new File(DirectoryRef, title + ".pdf");
        Uri path = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", pdffile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf")
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        ;
        //.setPackage("com.zio.ziospace");
        context.startActivity(pdfIntent);

    }

    private void download(String filename) {
        StorageReference fileRef = FirebaseStorage.getInstance().getReference()
                .child("course_res/" + CourseID + "/" + filename + ".pdf");

        Toast.makeText(context, "Download in progress in background ...", Toast.LENGTH_SHORT).show();

        File localFile = new File(DirectoryRef, filename + ".pdf");

        fileRef.getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(context, "Successfully Downloaded", Toast.LENGTH_SHORT).show();
                    openFile(filename);
                })
                .addOnFailureListener(exception -> {
                    Log.d(LOG_TAG, exception.getMessage());
                    Toast.makeText(context, "Unknown error, try later", Toast.LENGTH_SHORT).show();
                }).addOnProgressListener(snapshot -> {
                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    Log.d(LOG_TAG, "Download is " + progress + "% done");

                });
    }

    private void notifier(String filename, NotificationCompat.Builder builder, NotificationManagerCompat notifManager) {


        File pdffile = new File(DirectoryRef, filename + ".pdf");
        Uri path = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", pdffile);

        Intent notifyIntent = new Intent(context, DashboardActivity.class);
        notifyIntent.setAction(Intent.ACTION_VIEW)
                .setDataAndType(path, "application/pdf")
                .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | FLAG_IMMUTABLE);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notifManager = NotificationManagerCompat.from(context);
        builder = new NotificationCompat.Builder(context, "1");
        builder.setSmallIcon(R.drawable.icon_znotif)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setProgress(0, 0, false)
                .setContentTitle("Download Completed  (Tap to open) ")
                .setContentIntent(notifyPendingIntent);
        notifManager.notify(200, builder.build());

    }

    @Override
    public int getItemCount() {
        return resourcesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public Button download;
        public RelativeLayout mat_open;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.mtitle);
            download = itemView.findViewById(R.id.mdown);
            mat_open = itemView.findViewById(R.id.material);
        }
    }

}
