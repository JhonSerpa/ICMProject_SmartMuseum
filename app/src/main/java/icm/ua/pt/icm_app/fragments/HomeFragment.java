package icm.ua.pt.icm_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import icm.ua.pt.icm_app.R;



public class HomeFragment extends Fragment {
    private Intent serviceIntent;
    private final static String TAG = "HomeFragment";
    private List<String> pastExibs;
    private List<String> information;

    private ImageView img1;  //For the first PastExib
    private ImageView img2;  //For the 2 PastExib
    private ImageView img3;  //For the 3 PastExib
    private ImageView img4;  //For the 4 PastExib

    private Button btn1;    //For the first button
    private Button btn2;    //For the first button
    private Button btn3;    //For the first button
    private Button btn4;    //For the first button

    private String _Title = "";
    private String _Desc = "";
    private String _Image = "";

    FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    private FragmentManager fragmentManager;
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        // args.putString(ARG_PARAM1, param1);
        //  args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //    if (getArguments() != null) {
        //       mParam1 = getArguments().getString(ARG_PARAM1);
        //       mParam2 = getArguments().getString(ARG_PARAM2);
        //   }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.content_main, container, false);
        Button currentExhibition = (Button) rootView.findViewById(R.id.current_exhibition_button);
        currentExhibition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction;
                fragmentTransaction = fragmentManager.beginTransaction();
                ArtListFragment fragment = new ArtListFragment();
                fragmentTransaction.replace(R.id.frame, fragment).addToBackStack("tag");
                fragmentTransaction.commit();
            }
        });

        FirebaseUser mFirebaseUser;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        //Initialize the 4 past Exibs
        img1 = (ImageView) rootView.findViewById(R.id.pastExib1);
        img2 = (ImageView) rootView.findViewById(R.id.pastExib2);
        img3 = (ImageView) rootView.findViewById(R.id.pastExib3);
        img4 = (ImageView) rootView.findViewById(R.id.pastExib4);


        //sss
        DatabaseReference ref = root.child("Users").child(mFirebaseUser.getUid()).child("PastExib");
        pastExibs = new ArrayList<>();

        Query pastQuery = ref.orderByValue();
        pastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    String past = (String) singleSnapshot.getValue();
                    pastExibs.add(past);

                }
                Iterator iter = pastExibs.iterator();
                while (iter.hasNext()) {
                    final String c = (String) iter.next();
                    if (c.trim().equalsIgnoreCase("Mona Lisa")) {
                        img1.setImageResource(R.drawable.mona);
                        img1.setVisibility(rootView.VISIBLE);
                        btn1 = (Button) rootView.findViewById(R.id.pastExib1Btn);
                        btn1.setText("Mona Lisa");
                        btn1.setVisibility(View.VISIBLE);
                        btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                fragmentManager = getActivity().getSupportFragmentManager();

                                fragmentTransaction = fragmentManager.beginTransaction();
                                /**
                                 * This part cant be in a function because it doesnt go sequential
                                 * AKA Skips to the next line of code
                                 *
                                 */
                                final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference ref = root.child("Exhibits").child("Mona Lisa");
                                information = new ArrayList<>();

                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                            String infos = (String) singleSnapshot.getValue();
                                            information.add(infos);

                                        }
                                        _Desc = information.get(3);
                                        _Image = information.get(2);
                                        _Title = information.get(1);

                                        ArtDetailFragment fragment = ArtDetailFragment.newInstance(_Title,_Desc,_Image);

                                        fragmentTransaction.replace(R.id.frame, fragment).addToBackStack("tag");
                                        fragmentTransaction.commit();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                                /**
                                 *
                                 */

                            }
                        });
                    }
                    //For the Starry Night
                    if (c.trim().equalsIgnoreCase("Starry Night")) {
                        img2.setImageResource(R.drawable.starry);
                        img2.setVisibility(rootView.VISIBLE);
                        btn2 = (Button) rootView.findViewById(R.id.pastExib2Btn);
                        btn2.setText("Starry Night");
                        btn2.setVisibility(View.VISIBLE);
                        btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                fragmentManager = getActivity().getSupportFragmentManager();

                                fragmentTransaction = fragmentManager.beginTransaction();
                                /**
                                 * This part cant be in a function because it doesnt go sequential
                                 * AKA Skips to the next line of code
                                 *
                                 */
                                final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference ref = root.child("Exhibits").child("Starry Night");
                                information = new ArrayList<>();

                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                            String infos = (String) singleSnapshot.getValue();
                                            information.add(infos);

                                        }
                                        _Desc = information.get(3);
                                        _Image = information.get(2);
                                        _Title = information.get(1);
                                        ArtDetailFragment fragment = ArtDetailFragment.newInstance(_Title,_Desc,_Image);

                                        fragmentTransaction.replace(R.id.frame, fragment).addToBackStack("tag");
                                        fragmentTransaction.commit();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                                /**
                                 *
                                 */

                            }
                        });
                    }

                    //The Last Supper
                    //For the Starry Night
                    if (c.trim().equalsIgnoreCase("The Last Supper")) {
                        img3.setImageResource(R.drawable.last_supper);
                        img3.setVisibility(rootView.VISIBLE);
                        btn3 = (Button) rootView.findViewById(R.id.pastExib3Btn);
                        btn3.setText("The Last Supper");
                        btn3.setVisibility(View.VISIBLE);
                        btn3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                fragmentManager = getActivity().getSupportFragmentManager();

                                fragmentTransaction = fragmentManager.beginTransaction();
                                /**
                                 * This part cant be in a function because it doesnt go sequential
                                 * AKA Skips to the next line of code
                                 *
                                 */
                                final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference ref = root.child("Exhibits").child("The Last Supper");
                                information = new ArrayList<>();

                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                            String infos = (String) singleSnapshot.getValue();
                                            information.add(infos);

                                        }
                                        _Desc = information.get(3);
                                        _Image = information.get(2);
                                        _Title = information.get(1);
                                        ArtDetailFragment fragment = ArtDetailFragment.newInstance(_Title,_Desc,_Image);

                                        fragmentTransaction.replace(R.id.frame, fragment).addToBackStack("tag");
                                        fragmentTransaction.commit();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                                /**
                                 *
                                 */

                            }
                        });
                    }











                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });


        return rootView;
    }





}
