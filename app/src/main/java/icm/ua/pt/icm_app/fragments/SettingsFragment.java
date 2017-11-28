package icm.ua.pt.icm_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import icm.ua.pt.icm_app.R;
import icm.ua.pt.icm_app.database.DBManager;
import icm.ua.pt.icm_app.service.BackgroundScanService;


public class SettingsFragment extends Fragment implements View.OnClickListener{
    private Intent serviceIntent;

    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container,         false);
        serviceIntent = new Intent(getActivity().getApplicationContext(), BackgroundScanService.class);
        Button startScanButton = (Button) rootView.findViewById(R.id.start_button);
        Button stopScanButton = (Button) rootView.findViewById(R.id.stop_button);
        startScanButton.setOnClickListener(this);
        stopScanButton.setOnClickListener(this);
        Button fillDB = (Button) rootView.findViewById(R.id.fill_db_button);
        fillDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBManager manager = new DBManager(getActivity());
                manager.open();
                manager.insert("Mona Lisa", "Mona Lisa, oil painting on a poplar wood panel by the Italian painter, draftsman, sculptor, architect, and engineer Leonardo da Vinci, probably the world’s most-famous painting. It was painted sometime between 1503 and 1519, when Leonardo was living in Florence, and it now hangs in the Louvre, in Paris, where it remains an object of pilgrimage in the 21st century. The poplar panel shows evidence of warping and was stabilized in 1951 with the addition of an oak frame and in 1970 with four vertical braces. Dovetails also were added, to prevent the widening of a small crack visible near the centre of the upper edge of the painting. The sitter’s mysterious smile and her unproven identity have made the painting a source of ongoing investigation and fascination.","","FnMX","mona");
                manager.insert("Starry Night","Vincent van Gogh painted Starry Night in 1889 during his stay at the asylum of Saint-Paul-de-Mausole near Saint-Rémy-de-Provence. Van Gogh lived well in the hospital; he was allowed more freedoms than any of the other patients. If attended, he could leave the hospital grounds; he was allowed to paint, read, and withdraw into his own room. He was even given a studio. While he suffered from the occasional relapse into paranoia and fits - officially he had been diagnosed with epileptic fits - it seemed his mental health was recovering.\n" +
                        "\n" +
                        "Unfortunately, he relapsed. He began to suffer hallucination and have thoughts of suicide as he plunged into depression. Accordingly, there was a tonal shift in his work. He returned to incorporating the darker colors from the beginning of his career and Starry Night is a wonderful example of that shift. Blue dominates the painting, blending hills into the sky. The little village lays at the base in the painting in browns, greys, and blues. Even though each building is clearly outlined in black, the yellow and white of the stars and the moon stand out against the sky, drawing the eyes to the sky. They are the big attention grabber of the painting.","","1","starry");
                manager.insert("The Last Supper","The Last Supper of Leonardo da Vinci (Cenacolo Vinciano) is one of the most famous paintings in the world. This artwork was painted between 1494 and 1498 under the government of Ludovico il Moro and represents the last \"dinner\" between Jesus and his disciples.\n" +
                        "\n" +
                        "In order to create this unique work, Leonardo carried out an exhaustive research creating an infinity of preparatory sketches. Leonardo abandons the traditional method of fresco painting, painting the scene \"dry\" on the wall of the refectory. Traces of gold and silver foils have been found which testify to the artist's willingness to make the figures in a much more realistic manner, including precious details. After completion, his technique and environmental factor had contributed to the eventual deterioration of the fresco, which had undergone numerous restorations.\n" +
                        "\n" +
                        "The most recent restoration was completed in 1999 where several scientific methods were used to restore the original colors as close as possible, and to eliminate traces of paint applied in previous attempts to restore the fresco.","","FnMX","last_supper");

            }
        });
        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.start_button:
                getActivity().startService(serviceIntent);
                break;
            case R.id.stop_button:
                getActivity().stopService(serviceIntent);
                break;
        }

    }
}
