package icm.ua.pt.icm_app.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import icm.ua.pt.icm_app.R;
import icm.ua.pt.icm_app.database.DBManager;
import icm.ua.pt.icm_app.database.DatabaseHelper;

public class ArtListFragment extends Fragment {
    private DBManager dbManager;

    private ArtDetailFragment fragment;
    private ListView listView;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private SimpleCursorAdapter adapter;
    private ArrayList<String> info = new ArrayList<>();
    final String[] from = new String[] { DatabaseHelper._ID,
            DatabaseHelper.TITLE, DatabaseHelper.DESC, DatabaseHelper.IMAGE};

    final int[] to = new int[] { R.id.id, R.id.title, R.id.desc, R.id.image_path };

    public ArtListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_art_list, container,         false);

        dbManager = new DBManager(getActivity());
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        listView = (ListView) rootView.findViewById(R.id.list_view);
        listView.setEmptyView(rootView.findViewById(R.id.empty));


        adapter = new SimpleCursorAdapter(rootView.getContext(), R.layout.activity_view_record, cursor, from, to, 0) ;
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        // OnCLickListiner For List Items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                TextView description = (TextView) view.findViewById(R.id.desc);
                TextView title = (TextView) view.findViewById(R.id.title);
                TextView image = (TextView) view.findViewById(R.id.image_path);





                //Firebase thing here
                mAuth = FirebaseAuth.getInstance();
                mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                String a = (String) title.getText();
                DatabaseReference ref = root.child("Exhibits").child(a);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            String infos = (String) singleSnapshot.getValue();
                            info.add(infos);
                        }

                        Iterator it = info.iterator();
                        String beaconID = info.get(0);
                        String desc = info.get(1);
                        String img = info.get(2);
                        String Title = info.get(3);

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction;
                        fragmentTransaction = fragmentManager.beginTransaction();

                        fragment = ArtDetailFragment.newInstance(desc,Title,img);
                        fragmentTransaction.replace(R.id.frame, fragment).addToBackStack("tag");
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }
        });
       /* Button addButton = (Button) rootView.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {    //nullpointerexception
            @Override
            public void onClick(View v) {
                Intent add_mem = new Intent(getActivity(), AddRecord.class);
                startActivity(add_mem);
            }
        });*/
        return rootView;
    }
}
