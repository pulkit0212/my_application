package common.reachit.examtask.Fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import common.reachit.examtask.Login;
import common.reachit.examtask.R;
import common.reachit.examtask.SignUp;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    TextView txtName,txtMail,txtPhone;
    String name,phone,email,image;
    CircleImageView profileImage;

    TextView logout;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        txtName = (TextView)view.findViewById(R.id.tvprofileName);
        txtMail = (TextView)view.findViewById(R.id.tvuserMail);
        txtPhone = (TextView)view.findViewById(R.id.tvuserPhone);
        profileImage = (CircleImageView) view.findViewById(R.id.ivImage);

        logout = (TextView)view.findViewById(R.id.logout);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("users").child(firebaseAuth.getCurrentUser().getUid());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    name=(String) snapshot.child("userName").getValue();
                    phone=(String)snapshot.child("userPhone").getValue();
                    email=(String)snapshot.child("userEmail").getValue();
                    image=(String)snapshot.child("userImage").getValue();

                    txtName.setText(name);
                    txtPhone.setText(phone);
                    txtMail.setText(email);
                    Picasso.get().load(image).into(profileImage);
                }
                else {
                    Intent i = new Intent(getActivity(), SignUp.class);
                    startActivity(i);
                    ((Activity) getActivity()).overridePendingTransition(0, 0);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Logout")
                        .setMessage("Are you sure you want to Logout..?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseAuth.signOut();
                                Intent i = new Intent(getActivity(), Login.class);
                                startActivity(i);
                                ((Activity) getActivity()).overridePendingTransition(0, 0);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("no", null).setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return view;
    }
}