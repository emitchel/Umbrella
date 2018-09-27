package com.nerdery.umbrella.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.nerdery.umbrella.R;

public class UmbrellaDialog extends AppCompatDialogFragment {

  public static UmbrellaDialog newInstance() {
    return new UmbrellaDialog();
  }

  @Override public void onCreate(Bundle bundle) {
    Dialog dialog = getDialog();
    if (dialog != null) {
      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    super.onCreate(bundle);
    setCancelable(true);
    setRetainInstance(true);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.dialog_umbrella_loading, container);
    return view;
  }

  @Override
  public void onStart() {
    super.onStart();
    Window window = getDialog().getWindow();
    if (window != null) {
      View v = window.getDecorView();
      v.setBackgroundResource(android.R.color.transparent);
    }
    getDialog().getWindow().setWindowAnimations(R.style.ProgressDialogAnimation);
  }
}