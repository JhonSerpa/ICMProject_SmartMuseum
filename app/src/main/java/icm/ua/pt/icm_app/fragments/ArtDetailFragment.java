package icm.ua.pt.icm_app.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import icm.ua.pt.icm_app.R;

public class ArtDetailFragment extends Fragment {

    private String description;
    private String id;
    private String imagePath;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private String title;

    public ArtDetailFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            description = getArguments().getString("DESCRIPTION");
            id = getArguments().getString("TITLE");
            imagePath = getArguments().getString("IMAGE");
        }

    }

    public static ArtDetailFragment newInstance(String description, String title,String image) {
        ArtDetailFragment fragment = new ArtDetailFragment();
        Bundle args = new Bundle();
        args.putString("DESCRIPTION",description);
        args.putString("TITLE",title);
        args.putString("IMAGE",image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_art_detail, container, false);

        TextView desc = rootView.findViewById(R.id.art_description_text);
        TextView id = rootView.findViewById(R.id.art_title_text);
        ImageView imageView = rootView.findViewById(R.id.image_detail);
        desc.setText(this.description);
        int resID = getResources().getIdentifier(this.imagePath, "drawable", getActivity().getPackageName());
        imageView.setImageResource(resID);
        id.setText(this.id);
        title = id.getText().toString();
        final Button btnFavs = (Button) rootView.findViewById(R.id.btnAddToFavs);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = root.child("Users").child(mFirebaseUser.getUid()).child("Favs");

        //Se ja existe no USER nao vai aparecer
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("Title" + title)) {
                    btnFavs.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Add to favourits MAYBE REMOVE BUTTON AFTERWORDS?
        btnFavs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                mFirebaseUser = mAuth.getCurrentUser();
                final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                DatabaseReference ref = root.child("Users").child(mFirebaseUser.getUid()).child("Favs");
                ref.child("Title"+title).setValue(title);


            }
        });

        ImageView sound = (ImageView) rootView.findViewById(R.id.play_button);
        final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.sound);
        sound.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                if(mp.isPlaying()){
                    mp.pause();
                } else {
                    mp.start();
                }
            }
        });


        return rootView;
    }




    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }
}
