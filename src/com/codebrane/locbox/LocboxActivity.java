package com.codebrane.locbox;

import android.app.Fragment;

import com.codebrane.locbox.controller.LocboxFragment;

public class LocboxActivity extends SingleFragmentActivity {
  @Override
  protected Fragment createFragment() {
    return new LocboxFragment();
  }
}
