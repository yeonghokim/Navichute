package swc.chuma.navichute;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import swc.chuma.navichute.MapUtil.AddressChanger;

public class MainActivity extends AppCompatActivity implements MapView.MapViewEventListener,MapView.POIItemEventListener{

    private static final int defaultZoomLevel =7;

    private static final int selectMarkerTag=1000;

    private boolean IsMakeMaker=false;
    private MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AddressChanger addressChanger = new AddressChanger(this);

        MapPoint departPoint = MapPoint.mapPointWithGeoCoord(addressChanger.getLatitude(),addressChanger.getLongitude());

        mapView = new MapView(this);
        ViewGroup mapViewContainer = findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.setMapCenterPoint(departPoint, true);

        // 줌 레벨 변경
        mapView.setZoomLevel(defaultZoomLevel, true);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("현재 지역");
        marker.setTag(999);
        marker.setMapPoint(departPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setDraggable(false);

        mapView.addPOIItem(marker);

        mapView.setMapViewEventListener(this); // this에 MapView.MapViewEventListener 구현.
        mapView.setPOIItemEventListener(this);
    }
    public void mapZoomIn(View v){
        mapView.zoomIn(true);
    }

    public void mapZoomOut(View v){
        mapView.zoomOut(true);
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
        //더블 터치
        //마커 생성
        if(!IsMakeMaker) {
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName("선택한 지역");
            marker.setTag(selectMarkerTag);
            marker.setMapPoint(mapPoint);
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            marker.setDraggable(true);

            mapView.addPOIItem(marker);
            mapView.setZoomLevel(defaultZoomLevel, true);
            IsMakeMaker=true;
        }
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }
/////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        //
        if(mapPOIItem.getTag()==selectMarkerTag){



        }
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        //이동
        if(calloutBalloonButtonType==MapPOIItem.CalloutBalloonButtonType.MainButton){
            //네비게이션 화면으로 이동
            Toast.makeText(this, "Main", Toast.LENGTH_SHORT).show();
            /*
            Intent intent= new Intent(MainActivity.this,NavigateActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.putExtra("mapPoint", (Parcelable) mapPOIItem.getMapPoint());
            startActivity(intent);
            */
        }else if(calloutBalloonButtonType==MapPOIItem.CalloutBalloonButtonType.LeftSideButton){
            Toast.makeText(this, "Left", Toast.LENGTH_SHORT).show();
            //Balloon 지우기
        }else if(calloutBalloonButtonType==MapPOIItem.CalloutBalloonButtonType.RightSideButton){
            Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show();
            //Item 지우기
        }
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}