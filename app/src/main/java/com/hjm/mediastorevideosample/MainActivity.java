package com.hjm.mediastorevideosample;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

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
                List<String> paths = getMediaStoreFiles(target);
                StringBuilder builder = new StringBuilder(1024);
                for (String path : paths) {
                    builder.append(path).append("\n");
                }
                textView.setText(builder.toString());
            }
        });
    }

    private List<String> getMediaStoreFiles(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        List<String> ret = new ArrayList<>();
        try (Cursor cursor = contentResolver.query(uri,
                null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String path =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    ret.add(path);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            ret.clear();
            ret.add("error : " + e.getLocalizedMessage());
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
