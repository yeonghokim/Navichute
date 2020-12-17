package swc.chuma.navichute;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import swc.chuma.navichute.MapUtil.AddressChanger;

public class NavigateActivity extends AppCompatActivity {

    private static final int defaultZoomLevel =7;

    private MapPoint getMiddlePoint(MapPoint mapPoint1, MapPoint mapPoint2){
        MapPoint.GeoCoordinate mapPoint1_geo = mapPoint1.getMapPointGeoCoord();
        MapPoint.GeoCoordinate mapPoint2_geo = mapPoint2.getMapPointGeoCoord();

        MapPoint mappoint = MapPoint.mapPointWithGeoCoord(mapPoint1_geo.latitude+mapPoint2_geo.latitude/2,
                mapPoint1_geo.longitude+mapPoint2_geo.longitude/2);

        return mappoint;
    }
    MapPoint departPoint;
    MapPoint arrivalPoint;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);

        AddressChanger addressChanger = new AddressChanger(this);

        departPoint = MapPoint.mapPointWithGeoCoord(addressChanger.getLatitude(),addressChanger.getLongitude());
        arrivalPoint = (MapPoint)getIntent().getExtras().get("mapPoint");

        MapView mapView = new MapView(this);
        ViewGroup mapViewContainer = findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        //mapView.setMapCenterPoint(getMiddlePoint(departPoint,arrivalPoint), true);

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

        mapView.fitMapViewAreaToShowAllPOIItems();
        //지도 화면에 추가된 모든 POI Item들이
        //화면에 나타나도록 지도 화면 중심과 확대/축소 레벨을 자동으로 조정한다

        TextView textView = (TextView)findViewById(R.id.navistart_address);
        textView.setText(addressChanger.getCurrentAddress());

        double metery = Math.abs((addressChanger.getLatitude()-arrivalPoint.getMapPointGeoCoord().latitude)/3600.0) * 30.8;
        double meterx = Math.abs((addressChanger.getLongitude()-arrivalPoint.getMapPointGeoCoord().longitude)/3600.0)*25;

        double distance = Math.sqrt(meterx*meterx+metery*metery);

        textView = (TextView)findViewById(R.id.navistart_distance);
        textView.setText(distance+"m");// 거리 넣기

        textView = (TextView)findViewById(R.id.navistart_latitude);
        textView.setText("위도 : "+arrivalPoint.getMapPointGeoCoord().latitude);// 거리 넣기

        textView = (TextView)findViewById(R.id.navistart_longitude);
        textView.setText("경도 : "+arrivalPoint.getMapPointGeoCoord().longitude);// 거리 넣기
    }
    public void GoNaviChute(View v){
        Intent intent= new Intent(NavigateActivity.this,NaviServiceActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("departPoint", (Parcelable) departPoint);
        intent.putExtra("arrivalPoint", (Parcelable) arrivalPoint);
        startActivity(intent);
    }
}