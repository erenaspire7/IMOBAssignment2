package com.example.imob301assignment;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.example.imob301assignment.placeholder.PlaceholderContent;
import com.example.imob301assignment.databinding.FragmentActionDetailBinding;

import java.lang.reflect.Method;

/**
 * A fragment representing a single Action detail screen.
 * This fragment is either contained in a {@link ActionListFragment}
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
public class ActionDetailFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The placeholder content this fragment is presenting.
     */
    private PlaceholderContent.PlaceholderItem mItem;
    private CollapsingToolbarLayout mToolbarLayout;
    private TextView mTextView;

    private final View.OnDragListener dragListener = (v, event) -> {
        if (event.getAction() == DragEvent.ACTION_DROP) {
            ClipData.Item clipDataItem = event.getClipData().getItemAt(0);
            mItem = PlaceholderContent.ITEM_MAP.get(clipDataItem.getText().toString());
            updateContent();
        }
        return true;
    };
    private FragmentActionDetailBinding binding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ActionDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the placeholder content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = PlaceholderContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentActionDetailBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

//        mToolbarLayout = rootView.findViewById(R.id.toolbar_layout);
        mTextView = binding.actionDetail;

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.getSupportActionBar().setTitle(mItem.actionName);
        }

        // Show the placeholder content as text in a TextView & in the toolbar if available.
        updateContent();
        rootView.setOnDragListener(dragListener);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateContent() {
        if (mItem != null) {
            Context context = getContext();

            if (mItem.actionLayout != 0) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View child = inflater.inflate(mItem.actionLayout, null);

                binding.baseContainer.addView(child);
            }

            ActivityFunctionality activityFunctionality = new ActivityFunctionality(
                context, binding.baseContainer
            );

            String methodName = mItem.actionName.replaceAll(" ", "");

            try {
                Method method = activityFunctionality.getClass().getMethod(methodName);
                method.invoke(activityFunctionality);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}