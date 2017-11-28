package icm.ua.pt.icm_app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import icm.ua.pt.icm_app.fragments.ArtDetailFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends Fragment {

    private ListView listView;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    //private RecyclerView recyclerView;
    //private RecyclerView.Adapter rvAdapter;
    //private RecyclerView.LayoutManager rvLayoutManager;
    private ArrayList<String> entries;

    private ArrayList<String> Favs = new ArrayList<>();
    private String _Title;
    public FavouritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_favourites, container,         false);

        //ListView lv = (ListView) rootView.findViewById(R.id.list_vw);
        //Get all the data

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = root.child("Users").child(mFirebaseUser.getUid()).child("Favs");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    String infos = (String) singleSnapshot.getValue();
                    Favs.add(infos);
                }

                Iterator it = Favs.iterator();

                while (it.hasNext()){
                    String title = (String) it.next();

                }
                /*
                while (it.hasNext()){
                }
                _Title = Favs.get(0); */
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

}
