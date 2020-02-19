package com.lovanto.keuangan.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.lovanto.keuangan.R;
import com.lovanto.keuangan.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Welcome extends AppCompatActivity {
    TextInputEditText namaText, emailText, uangText;
    TextInputLayout textInputLayoutNama, textInputLayoutEmail, textInputLayoutUang;
    CircleImageView circleImageView;

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public static final String TAG = Welcome.class.getSimpleName();
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        insertPermissions();
        init();

        User user = new User(Welcome.this);
        JSONObject dataUser = user.getUser();
        if (dataUser.length() != 0) {
            Intent intent = new Intent(Welcome.this, MainActivity.class);
            this.startActivity(intent);
            finish();
        } else {

        }

        this.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                } else {
                    showPictureDialog();
                }
            }
        });
        showImageList();
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                                break;
                            case 1:
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void init() {
        circleImageView = findViewById(R.id.profil);

        namaText = findViewById(R.id.editTextNama);
        emailText = findViewById(R.id.editTextEmail);
        uangText = findViewById(R.id.editTextUang);

        textInputLayoutNama = (TextInputLayout) findViewById(R.id.textInputLayoutNama);
        textInputLayoutUang = (TextInputLayout) findViewById(R.id.textInputLayoutUang);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            circleImageView.setImageBitmap(photo);
            saveImage(photo);
        }
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    circleImageView.setImageBitmap(bitmap);
                    saveImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Welcome.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void saveImage(Bitmap finalBitmap) {
        String iname, Image_path;
        int numberOfImages;

        String root = Environment.getExternalStorageDirectory().getPath() + "/Keuangan/";
        System.out.println(root +" Root value in saveImage Function");
        File myDir = new File(root + "/image/");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        iname = "image_" + sdf.format(new Date()) + ".jpg";
        File file = new File(myDir, iname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MediaScannerConnection.scanFile(this, new String[] { file.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });

        Image_path = Environment.getExternalStorageDirectory().getPath() + "/Keuangan/image/"+iname;
        File[] files = myDir.listFiles();
        numberOfImages=files.length;
        System.out.println("Total images in Folder "+numberOfImages);
    }

    private void showImageList() {
        String media = "mounted";
        String diskState = Environment.getExternalStorageState();
        try {
            if( diskState.equals(media) ) {
                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Keuangan/image/");
                if (file.exists()) {
                    File[] files = file.listFiles();
                    Arrays.sort(files);
                    for (int i = 0; i < files.length; i++) {
                        Log.i(TAG, "FOTO PROFILE [ FILE ] : " + files[i]);
                    }
                    Log.i(TAG, "FOTO PROFILE [ GET FILE ] : " + files[files.length - 1].toString());
                    Bitmap b = BitmapFactory.decodeFile(files[files.length - 1].toString());
                    circleImageView.setImageBitmap(b);
                }
            } else {
                Toast.makeText(this, "The external disk is not mounted", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertPermissions() {
        List<String> permissionsNeeded = new ArrayList<>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, "android.permission.INTERNET"))
            permissionsNeeded.add("Internet");
        if (!addPermission(permissionsList, "android.permission.CAMERA"))
            permissionsNeeded.add("Kamera");
        if (!addPermission(permissionsList, "android.permission.READ_EXTERNAL_STORAGE"))
            permissionsNeeded.add("Buka File");
        if (!addPermission(permissionsList, "android.permission.WRITE_EXTERNAL_STORAGE"))
            permissionsNeeded.add("Tulis File");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = "Aplikasi Keuanganku menginginkan Anda memberi izin untuk mengakses " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            }
                        });
                return;
            } if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", onClickListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return shouldShowRequestPermissionRationale(permission);
            }
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<>();
                perms.put("android.permission.INTERNET", PackageManager.PERMISSION_GRANTED);
                perms.put("android.permission.CAMERA", PackageManager.PERMISSION_GRANTED);
                perms.put("android.permission.READ_EXTERNAL_STORAGE", PackageManager.PERMISSION_GRANTED);
                perms.put("android.permission.WRITE_EXTERNAL_STORAGE", PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get("android.permission.INTERNET") == PackageManager.PERMISSION_GRANTED
                        && perms.get("android.permission.CAMERA") == PackageManager.PERMISSION_GRANTED
                        && perms.get("android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED
                        && perms.get("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Mohon untuk mengaktifkan semua perizinan", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void save(View v) {
        init();
        if (validate()) {
            String nama = namaText.getText().toString();
            String email = emailText.getText().toString();
            if (!uangText.getText().toString().isEmpty()) {
                Integer uangku = Integer.parseInt(uangText.getText().toString());

                User dataUser = new User(this);
                dataUser.nama = nama;
                dataUser.email = email;
                dataUser.uang = uangku;
                dataUser.uang = uangku;
                dataUser.pendapatan = 0;
                dataUser.pengeluaran = 0;

                Toast.makeText(this, "Data anda berhasil disimpan", Toast.LENGTH_LONG).show();
                dataUser.insertUser(dataUser);
                startActivity(new Intent(Welcome.this, MainActivity.class)); finish();
            } else {

            }
        } else {
            Toast.makeText(this, "Data anda gagal disimpan", Toast.LENGTH_LONG).show();
        }
    }

    public boolean validate() {
        boolean valid = false;
        init();
        String nama = namaText.getText().toString();
        String email = emailText.getText().toString();
        String uang = uangText.getText().toString();
        if (nama.isEmpty()) {
            valid = false;
            textInputLayoutNama.setError("Nama masih kosong");
        } else {
            if (nama.length() >= 3) {
                valid = true;
                textInputLayoutNama.setError(null);
            } else {
                valid = false;
                textInputLayoutNama.setError("Nama tidak sesuai ketentuan");
            }
        }

        if (uang.isEmpty()) {
            valid = false;
            textInputLayoutUang.setError("Uang masih kosong");
        } else {
            if (uang.length() >= 1) {
                valid = true;
                textInputLayoutUang.setError(null);
            } else {
                valid = false;
                textInputLayoutUang.setError("Uang anda tidak sesuai ketentuan");
            }
        }

        if (email.isEmpty()) {
            valid = false;
            textInputLayoutEmail.setError("Email masih kosong");
        } else {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                valid = true;
                textInputLayoutEmail.setError(null);

            } else {
                valid = false;
                textInputLayoutEmail.setError("Email tidak sesuai ketentuan");
            }
        }
        return valid;
    }
}
