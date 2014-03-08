package com.codebrane.locbox;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public abstract class SingleFragmentActivity extends FragmentActivity {
  
  protected abstract Fragment createFragment();
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fragment);

    if (savedInstanceState == null) {
      getFragmentManager().beginTransaction()
          .add(R.id.fragmentContainer, createFragment()).commit();
    }
  }

}
