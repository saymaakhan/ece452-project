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
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.ace.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemDiscussionForumPeersBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final CardView cardForumMessagePeer;

  @NonNull
  public final LinearLayout layoutForumContainerPeer;

  @NonNull
  public final ImageView profilePicture;

  @NonNull
  public final TextView textForumMessagePeer;

  @NonNull
  public final TextView textForumTimestampPeer;

  @NonNull
  public final TextView textForumUserPeer;

  private ItemDiscussionForumPeersBinding(@NonNull ConstraintLayout rootView,
      @NonNull CardView cardForumMessagePeer, @NonNull LinearLayout layoutForumContainerPeer,
      @NonNull ImageView profilePicture, @NonNull TextView textForumMessagePeer,
      @NonNull TextView textForumTimestampPeer, @NonNull TextView textForumUserPeer) {
    this.rootView = rootView;
    this.cardForumMessagePeer = cardForumMessagePeer;
    this.layoutForumContainerPeer = layoutForumContainerPeer;
    this.profilePicture = profilePicture;
    this.textForumMessagePeer = textForumMessagePeer;
    this.textForumTimestampPeer = textForumTimestampPeer;
    this.textForumUserPeer = textForumUserPeer;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemDiscussionForumPeersBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemDiscussionForumPeersBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_discussion_forum_peers, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemDiscussionForumPeersBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.card_forum_message_peer;
      CardView cardForumMessagePeer = ViewBindings.findChildViewById(rootView, id);
      if (cardForumMessagePeer == null) {
        break missingId;
      }

      id = R.id.layout_forum_container_peer;
      LinearLayout layoutForumContainerPeer = ViewBindings.findChildViewById(rootView, id);
      if (layoutForumContainerPeer == null) {
        break missingId;
      }

      id = R.id.profile_picture;
      ImageView profilePicture = ViewBindings.findChildViewById(rootView, id);
      if (profilePicture == null) {
        break missingId;
      }

      id = R.id.text_forum_message_peer;
      TextView textForumMessagePeer = ViewBindings.findChildViewById(rootView, id);
      if (textForumMessagePeer == null) {
        break missingId;
      }

      id = R.id.text_forum_timestamp_peer;
      TextView textForumTimestampPeer = ViewBindings.findChildViewById(rootView, id);
      if (textForumTimestampPeer == null) {
        break missingId;
      }

      id = R.id.text_forum_user_peer;
      TextView textForumUserPeer = ViewBindings.findChildViewById(rootView, id);
      if (textForumUserPeer == null) {
        break missingId;
      }

      return new ItemDiscussionForumPeersBinding((ConstraintLayout) rootView, cardForumMessagePeer,
          layoutForumContainerPeer, profilePicture, textForumMessagePeer, textForumTimestampPeer,
          textForumUserPeer);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
