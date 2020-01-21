package com.hjm.mediastorevideosample;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    private static class MediaFile {
        public final String path;
        public final String mimeType;

        public MediaFile(String path, String mimeType) {
            this.path = path;
            this.mimeType = mimeType;
        }

        @Override
        public String toString() {
            return "path=" + path + ", " + "mimeType=" + mimeType;
        }
    }

    private static final int REQUEST_PERMISSION = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();

        final TextView textView = findViewById(R.id.text_view);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri target = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                List<MediaFile> files = getMediaStoreFiles(target);
                StringBuilder builder = new StringBuilder(1024);
                for (MediaFile file : files) {
                    builder.append(file).append("\n");
                }
                textView.setText(builder.toString());
            }
        });
    }

    private List<MediaFile> getMediaStoreFiles(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        List<MediaFile> ret = new ArrayList<>();
        try (Cursor cursor = contentResolver.query(uri,
                null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String path =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    String mimeType =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
                    ret.add(new MediaFile(path, mimeType));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return ret;
        }
        return ret;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }
    }
}
