package swc.chuma.navichute;

import android.os.Bundle;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;

import net.daum.mf.map.api.MapView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapView mapView = new MapView(this);
        ViewGroup mapViewContainer = findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        //mapView.setMapViewEventListener(this); // this에 MapView.MapViewEventListener 구현.
        //mapView.setPOIItemEventListener(this);
    }
}