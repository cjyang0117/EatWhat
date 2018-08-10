package tw.com.flag.eatwhat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import static android.content.Context.LOCATION_SERVICE;

public class Gps implements LocationListener {
    private LocationManager mLocationManager;
    double longitude, latitude;
    private String best = LocationManager.GPS_PROVIDER;
    private boolean getGPSService, getGPSinfo = true;
    private static final int LOCATION_UPDATE_MIN_DISTANCE = 6000;
    private static final int LOCATION_UPDATE_MIN_TIME = 50;
    private LocationManager status;
    Context mContext;

    public Gps(Context mContext) {
        this.mContext = mContext;
        status = (LocationManager) (this.mContext.getSystemService(LOCATION_SERVICE));
        if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(best)) {
            //如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
            locationServiceInitial();
            getGPSService = true;
        } else {
            //OpenGps();
            Toast.makeText(this.mContext, "請開啟定位服務", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void locationServiceInitial() {
        Criteria criteria = new Criteria();  //資訊提供者選取標準
        mLocationManager = (LocationManager) (this.mContext.getSystemService(LOCATION_SERVICE));
        best = mLocationManager.getBestProvider(criteria, true);

        //@SuppressLint("MissingPermission") Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);//獲取最後的定位信息
        @SuppressLint("MissingPermission") Location location = mLocationManager.getLastKnownLocation(best);//獲取最後的定位信息
        if(location == null) {
            if(best.equals("gps")) {
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }else if(best.equals("network")) {
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        getLocation(location);
    }

    protected void getLocation(Location location) { //將定位資訊顯示在畫面中
        if (location != null) {
            longitude = location.getLongitude();   //取得經度
            latitude = location.getLatitude();     //取得緯度
        } else {
            longitude = 120.812320;
            latitude = 24.545804;
            Toast.makeText(this.mContext, "暫無準確定位座標", Toast.LENGTH_LONG).show();
        }
    }
    protected void delete(){
        if (getGPSService) {
            mLocationManager.removeUpdates(this);
            getGPSService=false;
        }
    }
    protected void update() {
        if(getGPSService) {
            if (ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLocationManager.requestLocationUpdates(best, LOCATION_UPDATE_MIN_DISTANCE, LOCATION_UPDATE_MIN_TIME, this);
        }
    }
    protected double getGPSLongitude(){
        return longitude;
    }
    protected double getGPSLatitude(){
        return latitude;
    }
    protected boolean isGetGPSService(){
        return getGPSService;
    }
    protected boolean getGPSinfo(){
        return getGPSinfo;
    }

    @Override
    public void onLocationChanged(Location location) {
        getLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    /*void OpenGps() {
        if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(best)) {
        } else {
            ((Activity)(mContext)).finish();
        }
    }*/
    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}