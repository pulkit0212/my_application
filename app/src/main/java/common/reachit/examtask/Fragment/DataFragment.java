package common.reachit.examtask.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import common.reachit.examtask.Interface.OfferItemClickListener;
import common.reachit.examtask.Model.AddData;
import common.reachit.examtask.R;
import common.reachit.examtask.ViewHolder.DataViewHolder;

public class DataFragment extends Fragment {

    FirebaseAuth firebaseAuth;

    private Context mContext;

    RecyclerView recycler;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<AddData, DataViewHolder> adapter;
    // FirebaseRecyclerAdapter<CameraSale, CameraSaleViewHolder> cameraAdapter;

    FirebaseDatabase database;
    DatabaseReference databaseReference;


    public DataFragment() {
    }

    public static DataFragment newInstance(String param1, String param2) {
        DataFragment fragment = new DataFragment();
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
        View view = inflater.inflate(R.layout.fragment_data, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        databaseReference=database.getReference("Data").child(firebaseAuth.getCurrentUser().getUid());

        recycler=(RecyclerView) view.findViewById(R.id.recycler_home);
        recycler.setHasFixedSize(false);
        layoutManager=new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(layoutManager);

        loadData();

        return view;
    }

    private void loadData() {
        adapter =new FirebaseRecyclerAdapter<AddData, DataViewHolder>(AddData.class,
                R.layout.data_item,
                DataViewHolder.class,
                databaseReference) {
            @Override
            protected void populateViewHolder(DataViewHolder viewHolder, AddData model, int position) {

                viewHolder.name.setText(model.getName());


                final AddData clickItem=model;
                viewHolder.setOfferItemClickListener(new OfferItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                    }
                });

            }
        };
        recycler.setAdapter(adapter);
    }
}