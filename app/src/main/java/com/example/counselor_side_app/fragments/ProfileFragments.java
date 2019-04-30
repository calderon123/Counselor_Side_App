package com.example.counselor_side_app.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.counselor_side_app.R;
import com.example.counselor_side_app.models.UserMentor;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragments extends Fragment {

    private TextView fullname,expertise,count_counselors,email,fullname2,address;
    private CircleImageView profile_image;
    private ImageView card_background;
    private Button update_photo,save,cancel;
    private  RelativeLayout options,top;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private int countCounselors = 0;
    StorageReference storageReference;
    private  static final int  IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ScrollView scroll ,scroll_scroll;
    private StorageTask uploadTask;
    private View view1,view2,view3,view4;
    private FloatingActionButton edit;
    private EditText fullname1_edit,address_edit;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile_fragments, container, false);

        address = view.findViewById(R.id.address);
        fullname2 = view.findViewById(R.id.fullname2);
        fullname1_edit = view.findViewById(R.id.fullname1_edit);
        address_edit = view.findViewById(R.id.address_edit);


        scroll_scroll = view.findViewById(R.id.scroll_scroll);
        scroll = view.findViewById(R.id.scroll);
        top = view.findViewById(R.id.top);
        view1 = view.findViewById(R.id.view1);
        view3 = view.findViewById(R.id.view3);
        edit = view.findViewById(R.id.edit);


        profile_image = view.findViewById(R.id.profile_image);
        fullname = view.findViewById(R.id.fullname);
        expertise = view.findViewById(R.id.expertise);
        card_background = view.findViewById(R.id.card_background);
        count_counselors = view.findViewById(R.id.count_counselors);
        update_photo = view.findViewById(R.id.update_photo);
        email = view.findViewById(R.id.email);
        options = view.findViewById(R.id.options);
        save = view.findViewById(R.id.save);
        cancel = view.findViewById(R.id.cancel);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Add")
                .child(firebaseUser.getUid())
                .child("mentees");

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    countCounselors = (int) dataSnapshot.getChildrenCount();
                    if (countCounselors > 1){
                        count_counselors.setText("Mentees count: " +Integer.toString(countCounselors)+ " mentees");
                    }else if(countCounselors == 1) {
                        count_counselors.setText("Mentees count: " +Integer.toString(countCounselors) + " mentee");
                    }

                }else {
                    count_counselors.setText("No Mentees");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("UserMentor").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final UserMentor userMentor = dataSnapshot.getValue(UserMentor.class);

                fullname2.setText(userMentor.getFullname());
                address.setText(userMentor.getAddress());
                fullname.setText(userMentor.getFullname());
                expertise.setText(userMentor.getExpertise());
                Glide.with(getContext()).load(userMentor.getImageUrl()).into(card_background);
                Glide.with(getContext()).load(userMentor.getImageUrl()).into(profile_image);

                edit.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onClick(View v) {
                        top.setVisibility(View.GONE);
                        scroll.setVisibility(View.GONE);
                       scroll_scroll.setVisibility(View.VISIBLE);
                        edit.setVisibility(View.GONE);
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onClick(View v) {
                        top.setVisibility(View.VISIBLE);
                        scroll.setVisibility(View.VISIBLE);
                        scroll_scroll.setVisibility(View.GONE);
                        edit.setVisibility(View.VISIBLE);
                    }
                });
                address_edit.setText(userMentor.getAddress());
                fullname1_edit.setText(userMentor.getFullname());

                save.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onClick(View v) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserMentor")
                                .child(firebaseUser.getUid());
                        HashMap<String,Object> hashMap= new HashMap<>();

                        hashMap.put("address", address_edit.getText().toString());
                        hashMap.put("fullname", fullname1_edit.getText().toString());

                        reference.updateChildren(hashMap);
                        top.setVisibility(View.VISIBLE);
                        edit.setVisibility(View.VISIBLE);
                        scroll.setVisibility(View.VISIBLE);
                        scroll_scroll.setVisibility(View.GONE);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        storageReference = FirebaseStorage.getInstance().getReference("images");
        update_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
        return view;
    }

    private void openImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_REQUEST);
    }

    private String getFileExtension( Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getMimeTypeFromExtension(contentResolver.getType(uri));
    }

    private  void uploadImage(){
        final ProgressDialog pd  = new ProgressDialog(getContext());

        pd.setMessage("Uploading");
        pd.show();
        pd.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (imageUri != null){
            final StorageReference fileReference =  storageReference.child(System.currentTimeMillis() + "."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        databaseReference = FirebaseDatabase.getInstance().getReference("UserMentor").child(firebaseUser.getUid());

                        HashMap<String,Object> map = new HashMap<>();

                        map.put("imageURL",mUri);
                        databaseReference.updateChildren(map);
                        pd.dismiss();
                        pd.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    } else {
                        Toast.makeText(getContext(), "Failed",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        pd.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                    pd.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            });
        }else {
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();

            if (uploadTask !=null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "Upload in progress" ,Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }
}