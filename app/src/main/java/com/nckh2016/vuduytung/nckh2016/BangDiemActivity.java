package com.nckh2016.vuduytung.nckh2016;

import android.animation.LayoutTransition;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nckh2016.vuduytung.nckh2016.Data.MyContract;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectUserData;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.senab.photoview.PhotoViewAttacher;

public class BangDiemActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String PREFS_NAME = "current_user";
    public String current_user = null;

    String maMonHoc;
    ObjectUserData userData;
    String mCurrentPhotoPath;
    View view1, view2;
    Button btn_1, btn_2, btn_3;
    PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bang_diem);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        view1 = findViewById(R.id.fullscreen_content);
        view2 = findViewById(R.id.fullscreen_image_content);
        btn_1 = (Button) findViewById(R.id.dummy_button_1);
        btn_2 = (Button) findViewById(R.id.dummy_button_2);
        btn_3 = (Button) findViewById(R.id.dummy_button_3);
        SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        current_user = currentUserData.getString("user_mssv", null);
        maMonHoc = getIntent().getStringExtra("MaMonHoc");
        userData = new ObjectUserData();
        if (current_user != null) {
            SQLiteDataController data = SQLiteDataController.getInstance(this);
            try {
                data.isCreatedDatabase();
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            userData = data.getUserData(current_user, maMonHoc);
        }
        byte[] image = userData.getBangdiem();
        if (image == null) {
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.GONE);
            btn_1.setVisibility(View.VISIBLE);
            btn_2.setVisibility(View.GONE);
            btn_3.setVisibility(View.GONE);
        } else {
            ImageView mImageView = (ImageView) view2;
            mImageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
            mAttacher = new PhotoViewAttacher(mImageView);
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.VISIBLE);
            btn_1.setVisibility(View.GONE);
            btn_2.setVisibility(View.VISIBLE);
            btn_3.setVisibility(View.VISIBLE);
        }
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        ex.printStackTrace();
                    }
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        ex.printStackTrace();
                    }
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(BangDiemActivity.this)
                        .setTitle("Xóa bảng điểm")
                        .setMessage("Xóa bảng điểm này?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDataController database = SQLiteDataController.getInstance(getParent());
                                try {
                                    database.isCreatedDatabase();
                                } catch (IOException e) {
                                    Log.e("tag", e.getMessage());
                                }
                                ContentValues value = new ContentValues();
                                byte[] empty = null;
                                value.put(MyContract.UserDataEntry.COLUMN_BANG_DIEM, empty);
                                database.updateUserData(userData.getMasv(), userData.getMamonhoc(), value);
                                recreate();
                            }
                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                        .setIcon(R.drawable.error)
                        .show();
            }
        });
    }
    //http://developer.android.com/intl/vi/training/camera/photobasics.html#TaskPhotoView
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Transition
        LinearLayout searchBar = (LinearLayout) searchView.findViewById(R.id.search_bar);
        searchBar.setLayoutTransition(new LayoutTransition());
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.txtTimKiem));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getBaseContext(), SearchResultActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            Uri uri = Uri.parse(mCurrentPhotoPath);
            try{
                Bitmap imageBitmap = toGrayscale(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri));
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                ImageView mImageView = (ImageView) view2;
                mImageView.setImageBitmap(imageBitmap);
                SQLiteDataController database = SQLiteDataController.getInstance(this);
                try {
                    database.isCreatedDatabase();
                } catch (IOException e) {
                    Log.e("tag", e.getMessage());
                }
                ContentValues value = new ContentValues();
                value.put(MyContract.UserDataEntry.COLUMN_BANG_DIEM, stream.toByteArray());
                database.updateUserData(userData.getMasv(), userData.getMamonhoc(), value);
                recreate();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}