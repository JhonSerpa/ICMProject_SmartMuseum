package icm.ua.pt.icm_app.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kontakt.sdk.android.ble.configuration.ScanMode;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;

import java.util.List;
import java.util.concurrent.TimeUnit;

import icm.ua.pt.icm_app.R;
import icm.ua.pt.icm_app.database.DBManager;
import icm.ua.pt.icm_app.database.DatabaseHelper;


public class ProximityFragment extends Fragment implements View.OnClickListener {
    private DBManager dbManager;

    private ListView listView;
    private ProximityManager proximityManager;
    public static final String TAG = "ProximityManager";

    private SimpleCursorAdapter adapter;

    private String beacon = "";

    final String[] from = new String[] { DatabaseHelper._ID,
            DatabaseHelper.TITLE, DatabaseHelper.DESC, DatabaseHelper.IMAGE};

    final int[] to = new int[] { R.id.id, R.id.title, R.id.desc, R.id.image_path};

    public ProximityFragment() {
        // Required empty public constructor
    }
    public static ProximityFragment newInstance(String beacon) {
        ProximityFragment fragment = new ProximityFragment();
        Bundle args = new Bundle();
        args.putString("BEACON", beacon);
        //  args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            beacon = getArguments().getString("BEACON");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_proximity, container,         false);

        //Setup buttons
        Button startScanButton = (Button) rootView.findViewById(R.id.start_button_proximity);
        Button stopScanButton = (Button) rootView.findViewById(R.id.stop_button_proximity);
        startScanButton.setOnClickListener(this);
        stopScanButton.setOnClickListener(this);

        //Initialize and configure proximity manager
        setupProximityManager();


        dbManager = new DBManager(getActivity());
        dbManager.open();

        Cursor cursor = dbManager.fetchByBeacon(this.beacon);


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


                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction;
                fragmentTransaction = fragmentManager.beginTransaction();


                ArtDetailFragment fragment = ArtDetailFragment.newInstance(description.getText().toString(),title.getText().toString(),image.getText().toString());
                fragment.setDescription(description.getText().toString());

                fragmentTransaction.replace(R.id.frame, fragment).addToBackStack("tag");
                fragmentTransaction.commit();
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

    private void setupProximityManager() {
        proximityManager = ProximityManagerFactory.create(getActivity());

        //Configure proximity manager basic options
        proximityManager.configuration()
                //Using ranging for continuous scanning or MONITORING for scanning with intervals
                .scanPeriod(ScanPeriod.RANGING)
                //Using BALANCED for best performance/battery ratio
                .scanMode(ScanMode.BALANCED)
                //OnDeviceUpdate callback will be received with 5 seconds interval
                .deviceUpdateCallbackInterval(TimeUnit.SECONDS.toMillis(5));

        //Setting up iBeacon and Eddystone listeners
        proximityManager.setIBeaconListener(createIBeaconListener());
       // proximityManager.setEddystoneListener(createEddystoneListener());
    }
    private void startScanning() {
        //Connect to scanning service and start scanning when ready
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                //Check if proximity manager is already scanning
                if (proximityManager.isScanning()) {
                    Toast.makeText(getActivity(), "Already scanning", Toast.LENGTH_SHORT).show();
                    return;
                }
                proximityManager.startScanning();
                //progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Scanning started", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void stopScanning() {
        //Stop scanning if scanning is in progress
        if (proximityManager.isScanning()) {
            proximityManager.stopScanning();
           // progressBar.setVisibility(View.GONE);
           // Toast.makeText(this, "Scanning stopped", Toast.LENGTH_SHORT).show();
        }
    }

    private IBeaconListener createIBeaconListener() {
        return new IBeaconListener() {
            @Override
            public void onIBeaconDiscovered(IBeaconDevice iBeacon, IBeaconRegion region) {
                Log.i(TAG, "onIBeaconDiscovered: " + iBeacon.toString());
                testList(iBeacon.getUniqueId());
            }

            @Override
            public void onIBeaconsUpdated(List<IBeaconDevice> iBeacons, IBeaconRegion region) {
                Log.i(TAG, "onIBeaconsUpdated: " + iBeacons.size());
            }

            @Override
            public void onIBeaconLost(IBeaconDevice iBeacon, IBeaconRegion region) {
                Log.e(TAG, "onIBeaconLost: " + iBeacon.toString());
                testList("");
            }
        };
    }


    public void testList(String beacon) {

        dbManager = new DBManager(getActivity());
        dbManager.open();

        Cursor cursor = dbManager.fetchByBeacon(beacon);


        listView = (ListView) getActivity().findViewById(R.id.list_view);
        listView.setEmptyView(getActivity().findViewById(R.id.empty));


        adapter = new SimpleCursorAdapter(getActivity(), R.layout.activity_view_record, cursor, from, to, 0) ;
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        // OnCLickListiner For List Items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                TextView description = (TextView) view.findViewById(R.id.desc);
                TextView title = (TextView) view.findViewById(R.id.title);
                TextView image = (TextView) view.findViewById(R.id.image_path);


                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction;
                fragmentTransaction = fragmentManager.beginTransaction();


                ArtDetailFragment fragment = ArtDetailFragment.newInstance(description.getText().toString(),title.getText().toString(),image.getText().toString());
                fragment.setDescription(description.getText().toString());

                fragmentTransaction.replace(R.id.frame, fragment).addToBackStack("tag");
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_button_proximity:
                startScanning();
                break;
            case R.id.stop_button_proximity:
                stopScanning();
                break;
        }
    }
}