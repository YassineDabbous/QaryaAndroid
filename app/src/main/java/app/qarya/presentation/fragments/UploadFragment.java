package app.qarya.presentation.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import android.widget.ImageView;

import app.qarya.R;
import app.qarya.model.shared.YDUserManager;
import app.qarya.model.models.User;
import app.qarya.presentation.activities.MainActivity;
import app.qarya.presentation.base.MyActivity;

import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import app.qarya.presentation.base.MyFragment;
import app.qarya.presentation.vms.VMUser;
import app.qarya.utils.ImageHelper;
import tn.core.util.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends MyFragment<VMUser> implements View.OnClickListener  {

    Bitmap bmp;
    Button button;
    LinearLayout imageSelector;


    boolean noNeed = false;

    public static UploadFragment newInstance() {

        Bundle args = new Bundle();

        UploadFragment fragment = new UploadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public UploadFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_upload, container, false);
        initDefaultViews(v);
        imageSelector = v.findViewById(R.id.picture);
        button = v.findViewById(R.id.upload);
        imageSelector.setOnClickListener(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.upload(new File(Utilities.getPath(image, getActivity())));//ImageHelper.toBase64(bmp)
            }
        });
        return v;
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VMUser.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.getUpload().observe(this, this::onDataReceived);
    }

    public void onDataReceived(User data){ //TODO update local user data, drawer header ...
        imageSelector = null;
        noNeed = true;
        YDUserManager.saveFromUser(data);
        Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
        ((MainActivity) getActivity()).resetHeader();

    }


    public void onClick(View v) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle("") //Delete entry
                .setMessage(R.string.please_choose)//Are you sure you want to delete this entry?
                .setPositiveButton(R.string.capture_photo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue to camera
                        if(!isCameraPermissionGranted()){
                        }
                        else{
                            launchCamera();
                        }
                    }
                })
                .setNegativeButton(R.string.choose_photo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(!checkPermissionREAD_EXTERNAL_STORAGE(getActivity())){
                        }
                        else{
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto , 1);
                        }
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    //with revoked permission android.permission.CAMERA

    String filePath;
    void launchCamera(){
        final String dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/Folder/";
        File newdir = new File(dir);
        newdir.mkdirs();
        filePath = dir+ DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString()+".jpg";
        File newfile = new File(filePath);
        try {
            newfile.createNewFile();
        } catch (IOException e) {}
        Uri outputFileUri = Uri.fromFile(newfile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }


    public  boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getContext().checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("CAMERA","Permission is granted");
                return true;
            } else {

                Log.v("CAMERA","Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("CAMERA","Permission is granted");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case  1:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v("CAMERA","Permission: "+permissions[0]+ "was "+grantResults[0]);
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else{
                    new AlertDialog.Builder(getContext())
                            .setMessage("التطبيق يحتاج صلاحيات إستعمال الكاميرا")
                            .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    isCameraPermissionGranted();
                                }
                            }).create()
                            .show();
                }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(getContext(), "Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    Uri image;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        image = data.getData();
                        setImageSelector(image);
                    }
                    if (image == null && filePath != null) {
                        image = Uri.fromFile(new File(filePath));
                        setImageSelector(image);
                    }
                    File file = new File(filePath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // User cancelled the image capture
                } else {
                    // Image capture failed, advise user
                }

                break;
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    setImageSelector(data.getData());
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // User cancelled the image capture
                } else {
                    // Image capture failed, advise user
                }
                break;
        }
    }

    void setImageSelector(Uri uri){
        image = uri;
        String path = Utilities.getPath(uri, getContext());
        bmp = BitmapFactory.decodeFile(ImageHelper.resizeAndCompressImageBeforeSend(getContext(), path, "cache_.jpg"));
        if (bmp==null){
            MyActivity.log("☻ Can't make bitmap from => "+uri);
        }
        setPicture(path);
    }
    void setPicture(String path){
        //int id = getResources().getIdentifier("gameover", "drawable", getPackageName());
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(vp);
        //imageSelector.setImageResource(id);
        imageSelector.removeAllViews();
        imageSelector.addView(imageView);
        Picasso.get()
                .load(new File(path))
                //.resize(300, 250)
                //.centerCrop()
                .into(imageView);
    }



    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }
    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }


}
