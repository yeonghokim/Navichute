package swc.chuma.navichute;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import swc.chuma.navichute.NavigateUtil.NavigateService;

public class NaviServiceActivity extends AppCompatActivity {

    public static String ReceiverID ="navi_service_reciever";

    MapPOIItem changeMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_service);

        MapPoint departPoint = (MapPoint)getIntent().getExtras().get("departPoint");
        MapPoint arrivalPoint = (MapPoint)getIntent().getExtras().get("arrivalPoint");

        MapView mapView = new MapView(this);
        ViewGroup mapViewContainer = findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.setMapCenterPoint(departPoint, true);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("출발 지역");
        marker.setTag(1001);
        marker.setMapPoint(departPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setDraggable(false);
        marker.setShowCalloutBalloonOnTouch(false);

        mapView.addPOIItem(marker);

        marker = new MapPOIItem();
        marker.setItemName("도착 지역");
        marker.setTag(1002);
        marker.setMapPoint(arrivalPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setDraggable(false);
        marker.setShowCalloutBalloonOnTouch(false);

        mapView.addPOIItem(marker);

        changeMarker = new MapPOIItem();
        changeMarker.setItemName("현위치");
        changeMarker.setTag(1003);
        changeMarker.setMapPoint(departPoint);
        changeMarker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        changeMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        changeMarker.setDraggable(false);
        changeMarker.setShowCalloutBalloonOnTouch(false);

        mapView.addPOIItem(changeMarker);

        Intent intent = new Intent(this,NavigateService.class);
        startService(intent);

    }

    private final Handler handler = new Handler();

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // Get extra data included in the Intent
            String distance = intent.getStringExtra("distance");
            String direction = intent.getStringExtra("direction");

            //바뀔때마다 해야할 행동
            Runnable updater = new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(),distance+" , "+direction,Toast.LENGTH_LONG).show();
                }
            };
            handler.post(updater);
        }
    };

    @Override
    protected void onResume() {
        // action 이름이 "custom-event-name"으로 정의된 intent를 수신하게 된다.
        // observer의 이름은 mMessageReceiver이다.
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(ReceiverID));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this,NavigateService.class);
        stopService(intent);
    }
}