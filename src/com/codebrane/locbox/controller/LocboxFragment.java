package com.codebrane.locbox.controller;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codebrane.locbox.LocationReceiver;
import com.codebrane.locbox.LocboxManager;
import com.codebrane.locbox.R;
import com.codebrane.locbox.model.Walk;

public class LocboxFragment extends Fragment {
  private BroadcastReceiver locationReceiver = new LocationReceiver() {
    @Override
    protected void onLocationReceived(Context context, Location location) {
      lastLocation = location;
      if (isVisible()) {
        updateUI();
      }
    }
    @Override
    protected void onProviderEnabledChanged(boolean enabled) {
      int toastText = enabled ? R.string.gps_enabled : R.string.gps_disabled;
      Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
    }
  };
  
  private LocboxManager locboxManager;
  private Walk walk;
  private Location lastLocation;
  private Button startButton, stopButton;
  private TextView startedTextView, latitudeTextView, longitudeTextView,
                    altitudeTextView, durationTextView;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    locboxManager = LocboxManager.get(getActivity());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_locbox, parent, false);
    
    startedTextView = (TextView)view.findViewById(R.id.startedTextView);
    latitudeTextView = (TextView)view.findViewById(R.id.latitudeTextView);
    longitudeTextView = (TextView)view.findViewById(R.id.longitudeTextView);
    altitudeTextView = (TextView)view.findViewById(R.id.altitudeTextView);
    durationTextView = (TextView)view.findViewById(R.id.durationTextView);
    
    startButton = (Button)view.findViewById(R.id.startButton);
    startButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        locboxManager.startLocationUpdates();
        walk = new Walk();
        updateUI();
      }
    });
    
    stopButton = (Button)view.findViewById(R.id.stopButton);
    stopButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        locboxManager.stopLocationUpdates();
        updateUI();
      }
    });
    
    return view;
  }
  
  @Override
  public void onStart() {
    super.onStart();
    getActivity().registerReceiver(locationReceiver, new IntentFilter(LocboxManager.ACTION_LOCATION));
  }
  
  @Override
  public void onStop() {
    getActivity().unregisterReceiver(locationReceiver);
    super.onStop();
  }
  
  private void updateUI() {
    boolean started = locboxManager.isTracking();
    
    if (walk != null) {
      startedTextView.setText(walk.getStartDate().toString());
    }
    
    int durationSeconds = 0;
    if ((walk != null) && (lastLocation != null)) {
      durationSeconds = walk.getDurationSeconds(lastLocation.getTime());
      latitudeTextView.setText(Double.toString(lastLocation.getLatitude()));
      longitudeTextView.setText(Double.toString(lastLocation.getLongitude()));
      altitudeTextView.setText(Double.toString(lastLocation.getAltitude()));
    }
    durationTextView.setText(Walk.formatDuration(durationSeconds));
    
    startButton.setEnabled(!started);
    stopButton.setEnabled(started);
  }

}
