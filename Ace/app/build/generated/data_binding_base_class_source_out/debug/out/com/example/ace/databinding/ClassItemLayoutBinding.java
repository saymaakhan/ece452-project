// Generated by view binder compiler. Do not edit!
package com.example.ace.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.ace.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ClassItemLayoutBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageView letterGradeImageView;

  @NonNull
  public final TextView tvClassName;

  @NonNull
  public final TextView tvWeight;

  private ClassItemLayoutBinding(@NonNull LinearLayout rootView,
      @NonNull ImageView letterGradeImageView, @NonNull TextView tvClassName,
      @NonNull TextView tvWeight) {
    this.rootView = rootView;
    this.letterGradeImageView = letterGradeImageView;
    this.tvClassName = tvClassName;
    this.tvWeight = tvWeight;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ClassItemLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ClassItemLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.class_item_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ClassItemLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.letterGradeImageView;
      ImageView letterGradeImageView = ViewBindings.findChildViewById(rootView, id);
      if (letterGradeImageView == null) {
        break missingId;
      }

      id = R.id.tvClassName;
      TextView tvClassName = ViewBindings.findChildViewById(rootView, id);
      if (tvClassName == null) {
        break missingId;
      }

      id = R.id.tvWeight;
      TextView tvWeight = ViewBindings.findChildViewById(rootView, id);
      if (tvWeight == null) {
        break missingId;
      }

      return new ClassItemLayoutBinding((LinearLayout) rootView, letterGradeImageView, tvClassName,
          tvWeight);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
