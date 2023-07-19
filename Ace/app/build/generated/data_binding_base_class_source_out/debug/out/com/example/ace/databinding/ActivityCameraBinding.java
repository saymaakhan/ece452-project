// Generated by view binder compiler. Do not edit!
package com.example.ace.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.ace.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityCameraBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView clear;

  @NonNull
  public final ConstraintLayout constraintLayout;

  @NonNull
  public final ImageView copy;

  @NonNull
  public final ImageView getImage;

  @NonNull
  public final EditText recgText;

  @NonNull
  public final TextView textView2;

  @NonNull
  public final TextView textView3;

  private ActivityCameraBinding(@NonNull ConstraintLayout rootView, @NonNull ImageView clear,
      @NonNull ConstraintLayout constraintLayout, @NonNull ImageView copy,
      @NonNull ImageView getImage, @NonNull EditText recgText, @NonNull TextView textView2,
      @NonNull TextView textView3) {
    this.rootView = rootView;
    this.clear = clear;
    this.constraintLayout = constraintLayout;
    this.copy = copy;
    this.getImage = getImage;
    this.recgText = recgText;
    this.textView2 = textView2;
    this.textView3 = textView3;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityCameraBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityCameraBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_camera, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityCameraBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.clear;
      ImageView clear = ViewBindings.findChildViewById(rootView, id);
      if (clear == null) {
        break missingId;
      }

      id = R.id.constraintLayout;
      ConstraintLayout constraintLayout = ViewBindings.findChildViewById(rootView, id);
      if (constraintLayout == null) {
        break missingId;
      }

      id = R.id.copy;
      ImageView copy = ViewBindings.findChildViewById(rootView, id);
      if (copy == null) {
        break missingId;
      }

      id = R.id.getImage;
      ImageView getImage = ViewBindings.findChildViewById(rootView, id);
      if (getImage == null) {
        break missingId;
      }

      id = R.id.recgText;
      EditText recgText = ViewBindings.findChildViewById(rootView, id);
      if (recgText == null) {
        break missingId;
      }

      id = R.id.textView2;
      TextView textView2 = ViewBindings.findChildViewById(rootView, id);
      if (textView2 == null) {
        break missingId;
      }

      id = R.id.textView3;
      TextView textView3 = ViewBindings.findChildViewById(rootView, id);
      if (textView3 == null) {
        break missingId;
      }

      return new ActivityCameraBinding((ConstraintLayout) rootView, clear, constraintLayout, copy,
          getImage, recgText, textView2, textView3);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}