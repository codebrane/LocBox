package com.codebrane.locbox;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

public class LocboxManager {
  public static final String ACTION_LOCATION = "com.codebrane.locbox.ACTION_LOCATION";
  
  private static final String TAG = "LocboxManager";
  
  private static LocboxManager locboxManager;
  private Context appContext;
  private LocationManager locationManager;
  
  private LocboxManager(Context appContext) {
    this.appContext = appContext;
    locationManager = (LocationManager)appContext.getSystemService(Context.LOCATION_SERVICE);
  }
  
  public static LocboxManager get(Context context) {
    if (locboxManager == null) {
      locboxManager = new LocboxManager(context.getApplicationContext());
    }
    return locboxManager;
  }
  
  public void startLocationUpdates() {
    String provider = LocationManager.GPS_PROVIDER;
    
    // Broadcast the last known location
    Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
    if (lastKnownLocation != null) {
      lastKnownLocation.setTime(System.currentTimeMillis());
      broadcastLocation(lastKnownLocation);
    }
    
    PendingIntent pendingIntent = getLocationPendingIntent(true);
    locationManager.requestLocationUpdates(provider, 0, 0, pendingIntent);
  }
  
  public void stopLocationUpdates() {
    PendingIntent pendingIntent = getLocationPendingIntent(false);
    if (pendingIntent != null) {
      locationManager.removeUpdates(pendingIntent);
      pendingIntent.cancel();
    }
  }
  
  public boolean isTracking() {
    return getLocationPendingIntent(false) != null;
  }
  
  private void broadcastLocation(Location location) {
    Intent broadcast = new Intent(ACTION_LOCATION);
    broadcast.putExtra(LocationManager.KEY_LOCATION_CHANGED, location);
    appContext.sendBroadcast(broadcast);
  }
  
  // LOCATION METHODS
  
  private PendingIntent getLocationPendingIntent(boolean shouldCreate) {
    Intent broadcast = new Intent(ACTION_LOCATION);
    int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
    return PendingIntent.getBroadcast(appContext, 0, broadcast, flags);
  }
}
