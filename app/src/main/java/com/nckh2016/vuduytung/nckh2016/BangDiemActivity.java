package com.nckh2016.vuduytung.nckh2016;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

public class BangDiemActivity extends BaseActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_FROM_GALLERY = 2;
    public static final String PREFS_NAME = "current_user";
    //các biến được khôi phục lại nếu app resume
    public String current_user = null;
    String userMaMonHoc;
    Uri mCurrentPhoto;

    ObjectUserData userData;
    View view1, view2;
    Button btn_1, btn_2, btn_3;
    PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_bang_diem);
        view1 = findViewById(R.id.fullscreen_content);
        view2 = findViewById(R.id.fullscreen_image_content);
        btn_1 = (Button) findViewById(R.id.dummy_button_1);
        btn_2 = (Button) findViewById(R.id.dummy_button_2);
        btn_3 = (Button) findViewById(R.id.dummy_button_3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        for(int i=0; i < navigationView.getMenu().size(); i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
        SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        current_user = currentUserData.getString("user_mssv", null);
        userData = new ObjectUserData();
        userMaMonHoc = getIntent().getStringExtra("MaMonHoc");
        if (current_user != null) {
            SQLiteDataController data = SQLiteDataController.getInstance(getApplicationContext());
            try {
                data.isCreatedDatabase();
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            userData = data.getUserData(current_user, userMaMonHoc);
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
                new AlertDialog.Builder(BangDiemActivity.this)
                        .setTitle(R.string.txtChonAnh)
                        .setIcon(R.drawable.image_edit)
                        .setItems(new String[]{"Máy ảnh", "Thư viện"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                    {
                                        if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
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
                                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                                }
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Máy của bạn không hỗ trợ tính năng này", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    }

                                    case 1:
                                    {
                                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        intent.setType("image/*");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(intent, REQUEST_FROM_GALLERY);
                                        break;
                                    }
                                    default:
                                        break;
                                }
                            }
                        }).show();
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(BangDiemActivity.this)
                        .setTitle(R.string.txtChonAnh)
                        .setIcon(R.drawable.image_edit)
                        .setItems(new String[]{"Máy ảnh", "Thư viện"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                    {
                                        if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
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
                                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                                }
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Máy của bạn không hỗ trợ tính năng này", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    }
                                    case 1:
                                    {
                                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        intent.setType("image/*");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(intent, REQUEST_FROM_GALLERY);
                                        break;
                                    }
                                    default:
                                        break;
                                }
                            }
                        }).show();
            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(BangDiemActivity.this)
                        .setTitle(R.string.txtXoaAnh)
                        .setIcon(R.drawable.image_remove)
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
                }).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAttacher!=null){
            mAttacher.cleanup();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("picUri", mCurrentPhoto);
        outState.putString("maSinhVien", current_user);
        outState.putString("maMonHoc", userMaMonHoc);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentPhoto = savedInstanceState.getParcelable("picUri");
        current_user = savedInstanceState.getString("maSinhVien");
        userMaMonHoc = savedInstanceState.getString("maMonHoc");
    }

    //http://developer.android.com/intl/vi/training/camera/photobasics.html#TaskPhotoView
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhoto = Uri.fromFile(image);
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_IMAGE_CAPTURE:
                {
                    //Bundle extras = data.getExtras();
                    //Bitmap imageBitmap = (Bitmap) extras.get("data");
                    //Uri uri = Uri.parse(mCurrentPhotoPath);
                    try{
                        Bitmap imageBitmap = toGrayscale(MediaStore.Images.Media.getBitmap(this.getContentResolver(), mCurrentPhoto));
                        //Bitmap imageBitmap = toGrayscale(BitmapFactory.decodeFile(mCurrentPhotoPath));
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                        ImageView mImageView = (ImageView) view2;
                        mImageView.setImageBitmap(imageBitmap);
                        SQLiteDataController database = SQLiteDataController.getInstance(getApplicationContext());
                        try {
                            database.isCreatedDatabase();
                        } catch (IOException e) {
                            Log.e("tag", e.getMessage());
                        }
                        ContentValues value = new ContentValues();
                        value.put(MyContract.UserDataEntry.COLUMN_BANG_DIEM, stream.toByteArray());
                        database.updateUserData(current_user, userMaMonHoc, value);
                        recreate();
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    break;
                }
                case REQUEST_FROM_GALLERY:
                {
                    Uri selectedImage = data.getData();
                    String[] filePath = { MediaStore.Images.Media.DATA };
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    Bitmap imageBitmap = toGrayscale(BitmapFactory.decodeFile(picturePath));
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    ImageView mImageView = (ImageView) view2;
                    mImageView.setImageBitmap(imageBitmap);
                    SQLiteDataController database = SQLiteDataController.getInstance(getApplicationContext());
                    try {
                        database.isCreatedDatabase();
                    } catch (IOException e) {
                        Log.e("tag", e.getMessage());
                    }
                    ContentValues value = new ContentValues();
                    value.put(MyContract.UserDataEntry.COLUMN_BANG_DIEM, stream.toByteArray());
                    database.updateUserData(current_user, userMaMonHoc, value);
                    recreate();
                    break;
                }
                default:
                    break;
            }
        }
    }
}
