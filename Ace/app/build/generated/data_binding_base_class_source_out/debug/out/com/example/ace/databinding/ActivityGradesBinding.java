// Generated by view binder compiler. Do not edit!
package com.example.ace.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.ace.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityGradesBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final LinearLayout containerClasses;

  @NonNull
  public final FloatingActionButton fabAdd;

  @NonNull
  public final Toolbar toolbar;

  private ActivityGradesBinding(@NonNull RelativeLayout rootView,
      @NonNull LinearLayout containerClasses, @NonNull FloatingActionButton fabAdd,
      @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.containerClasses = containerClasses;
    this.fabAdd = fabAdd;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityGradesBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityGradesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_grades, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityGradesBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.containerClasses;
      LinearLayout containerClasses = ViewBindings.findChildViewById(rootView, id);
      if (containerClasses == null) {
        break missingId;
      }

      id = R.id.fabAdd;
      FloatingActionButton fabAdd = ViewBindings.findChildViewById(rootView, id);
      if (fabAdd == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      return new ActivityGradesBinding((RelativeLayout) rootView, containerClasses, fabAdd,
          toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
