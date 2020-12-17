package swc.chuma.navichute.NavigateUtil;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import swc.chuma.navichute.MapUtil.AddressChanger;
import swc.chuma.navichute.NaviServiceActivity;
import swc.chuma.navichute.R;
import swc.chuma.navichute.ReadUtil.TextReader;

public class NavigateService extends Service {

    public NavigateService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //목적지 설정된것 가져오기

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationManager manager  = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("navichute","NavigateService", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(Color.WHITE);
            channel.enableVibration(true);
            manager.createNotificationChannel(channel);
            
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"navichute");
            //builder.setSmallIcon(R.drawable.logo_lock);
            builder.setContentTitle("navichute");
            builder.setContentText("네비게이션이 진행되고 있습니다");
            builder.setAutoCancel(true);

            Notification notification =builder.build();
            startForeground(10,notification);
        }

        Context context = this;
        Thread thread = new Thread(){
            @Override
            public void run() {
                //설정된 목적지
                double endLa=37;
                double endLo=126;

                int i=0;
                while(true) {
                    //몇초마다 목적지 설정된것과의 거리 측정
                    AddressChanger addressChanger = new AddressChanger(context);
                    double startLa = addressChanger.getLatitude();//위도
                    double startLo = addressChanger.getLongitude();//경도

                    double metery = Math.abs((startLa-endLa)/3600.0) * 30.8;
                    double meterx = Math.abs((startLo-endLo)/3600.0)*25;

                    double distance = Math.sqrt(meterx*meterx+metery*metery);

                    //글씨 읽어주기
                    TextReader.getInstance(context);
                    TextReader.readText("목적지까지 "+distance+"m 남았습니다.");

                    Intent intent = new Intent(NaviServiceActivity.ReceiverID);
                    intent.putExtra("distance", distance);
                    intent.putExtra("direction","방향");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                    if(i++>=10) break;
                }
            }
        };
        thread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {


        throw new UnsupportedOperationException("Not yet implemented");
    }
}