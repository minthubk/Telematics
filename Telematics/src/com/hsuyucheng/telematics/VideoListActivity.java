package com.hsuyucheng.telematics;

import com.hsuyucheng.telematics.view.VideoListFragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * List video. Video Source: Local and Youtube
 * 
 * @author hsuyucheng
 * @version 0.5
 */
public class VideoListActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_vidoe_title_layout);
	}
	
	// Todo VideoList YouTubeList factory
	//private final static int ID_VIDEO = 0;
	//private final static int ID_YOUTUBE = 1;
	private final static int ID_BACK = 2;
	private static final String[] TITLES = {
		"Video", "Youtube", "Back"
	};
	
	/**
	 * This is a secondary activity, to show what the user has selected when the
	 * screen is not large enough to show it all in one activity.
	 */
	public static class DetailsActivity extends FragmentActivity {

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

			if (savedInstanceState == null) {
				// During initial setup, plug in the details fragment.
				VideoListFragment details = new VideoListFragment();
				details.setArguments(getIntent().getExtras());
				getSupportFragmentManager().beginTransaction()
						.add(android.R.id.content, details).commit();
			}
		}
	}

	/**
	 * This is the "top-level" fragment, showing a list of items that the user
	 * can pick. Upon picking an item, it takes care of displaying the data to
	 * the user as appropriate based on the currrent UI layout.
	 */
	public static class TitlesFragment extends ListFragment {
		boolean mDualPane;
		int mCurCheckPosition = 0;

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			// Populate list with our static array of titles.
			setListAdapter(new ArrayAdapter<String>(getActivity(),
					R.layout.simple_list_item_checkable_1, android.R.id.text1,
					TITLES));

			// Check to see if we have a frame in which to embed the details
			// fragment directly in the containing UI.
			View detailsFrame = getActivity().findViewById(R.id.details);
			mDualPane = detailsFrame != null
					&& detailsFrame.getVisibility() == View.VISIBLE;

			if (savedInstanceState != null) {
				// Restore last state for checked position.
				mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
			}

			if (mDualPane) {
				// In dual-pane mode, the list view highlights the selected
				// item.
				getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				// Make sure our UI is in the correct state.
				showDetails(mCurCheckPosition);
			}
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			outState.putInt("curChoice", mCurCheckPosition);
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			showDetails(position);
		}

		/**
		 * Helper function to show the details of a selected item, either by
		 * displaying a fragment in-place in the current UI, or starting a whole
		 * new activity in which it is displayed.
		 */
		void showDetails(int index) {
			if (index == ID_BACK) {
				this.getActivity().finish();
			}
			
			mCurCheckPosition = index;
			Log.d("change view", Integer.toString(index));

			if (mDualPane) {
				// display everything in-place with fragments
				getListView().setItemChecked(index, true);

				// Check what fragment is currently shown, replace if needed.
				VideoListFragment details = (VideoListFragment) getFragmentManager()
						.findFragmentById(R.id.details);
				if (details == null) {
					// Make new fragment to show this selection.
					details = new VideoListFragment();

					// Execute a transaction, replacing any existing fragment
					// with this one inside the frame.
					FragmentTransaction ft = getFragmentManager()
							.beginTransaction();
					ft.replace(R.id.details, details);
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					ft.commit();
				}

			} else {
				// Otherwise we need to launch a new activity to display
				Intent intent = new Intent();
				intent.setClass(getActivity(), DetailsActivity.class);
				intent.putExtra("index", index);
				startActivity(intent);
			}
		}
	}

}
