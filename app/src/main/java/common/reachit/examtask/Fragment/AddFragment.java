package common.reachit.examtask.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import common.reachit.examtask.Model.AddData;
import common.reachit.examtask.R;
import common.reachit.examtask.UserProfile;

public class AddFragment extends Fragment {

    EditText addData;
    Button btnSubmit;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference myRef;


    public AddFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
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
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        addData = (EditText)view.findViewById(R.id.etData);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("Data").child(firebaseAuth.getCurrentUser().getUid());


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!addData.getText().toString().equals("")){
                    AddData add = new AddData(addData.getText().toString());
                    myRef.push().setValue(add);

                    Toast.makeText(getContext(),"Submit Successfully",Toast.LENGTH_SHORT).show();
                    addData.setText("");
                }
                else{
                    Toast.makeText(getContext(),"Please enter data",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }
}