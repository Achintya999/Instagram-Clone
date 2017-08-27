package com.example.achintya.instagramnew;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton imgClicked;
    private Button btnClicked;

    private static  final int GALLERY_REQUEST = 2;

    private Uri uri = null;

    private EditText edtName, edtDesc;

    private StorageReference storageReference;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        edtName = (EditText)findViewById(R.id.edtName);
        edtDesc = (EditText)findViewById(R.id.edtDesc);

        imgClicked = (ImageButton)findViewById(R.id.imgClicked);

        btnClicked = (Button)findViewById(R.id.btnClicked);

        imgClicked.setOnClickListener(PostActivity.this);
        btnClicked.setOnClickListener(PostActivity.this);




        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference = database.getInstance().getReference().child("InstaApp");

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnClicked:

                final String titleValue = edtName.getText().toString().trim();
                final String descValue = edtDesc.getText().toString().trim();

                if(!TextUtils.isEmpty(titleValue) && !TextUtils.isEmpty(descValue)){

                    StorageReference filePath = storageReference.child("PostImage").child(uri.getLastPathSegment());
                    filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Uri downloadurl = taskSnapshot.getDownloadUrl();

                            Toast.makeText(PostActivity.this, "Upload Complete", Toast.LENGTH_LONG).show();

                            DatabaseReference newPost = databaseReference.push();
                            newPost.child("title").setValue(titleValue);
                            newPost.child("desc").setValue(descValue);
                            newPost.child("image").setValue(downloadurl.toString());


                        }
                    });

                }


                break;

            case R.id.imgClicked:

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("Image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            uri = data.getData();
            imgClicked.setImageURI(uri);

        }
    }
}
