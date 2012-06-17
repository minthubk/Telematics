package com.hsuyucheng.telematics.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SearchViewCompat;
import android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.hsuyucheng.telematics.PlayActivity;
import com.hsuyucheng.telematics.R;
import com.hsuyucheng.telematics.playing.Video;
import com.hsuyucheng.telematics.util.Storage;

/** UI for Video list
 * @author hsuyucheng
 * @version 0.3
 */
public class VideoListFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<List<Video>> {
	// This is the Adapter being used to display the list's data.
	VideoListAdapter mAdapter;
	List<Video> mVideos;
	// If non-null, this is the current filter the user has provided.
	String mCurFilter;

	OnQueryTextListenerCompat mOnQueryTextListenerCompat;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Give some text to display if there is no data. In a real
		// application this would come from a resource.
		setEmptyText("No Video");

		// We have a menu item to show in action bar.
		setHasOptionsMenu(true);

		// Create an empty adapter we will use to display the loaded data.
		mAdapter = new VideoListAdapter(getActivity());
		setListAdapter(mAdapter);

		// Start out with a progress indicator.
		setListShown(false);

		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Place an action bar item for searching.
		MenuItem item = menu.add("Search");
		item.setIcon(android.R.drawable.ic_menu_search);
		MenuItemCompat.setShowAsAction(item,
				MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
		View searchView = SearchViewCompat.newSearchView(getActivity());
		if (searchView != null) {
			SearchViewCompat.setOnQueryTextListener(searchView,
					new OnQueryTextListenerCompat() {
						@Override
						public boolean onQueryTextChange(String newText) {
							// Called when the action bar search text has
							// changed. Since this
							// is a simple array adapter, we can just have it do
							// the filtering.
							mCurFilter = !TextUtils.isEmpty(newText) ? newText
									: null;
							mAdapter.getFilter().filter(mCurFilter);
							return true;
						}
					});
			MenuItemCompat.setActionView(item, searchView);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Video video = mVideos.get(position);
		// call other activity
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("video", video.getPath());
		intent.putExtras(bundle);
		intent.setClass(getActivity(), PlayActivity.class);
		startActivity(intent);

	}

	@Override
	public Loader<List<Video>> onCreateLoader(int id, Bundle args) {
		// This is called when a new Loader needs to be created. This
		// sample only has one Loader with no arguments, so it is simple.
		return new VideoListLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<Video>> loader, List<Video> data) {
		// Set the new data in the adapter.
		mVideos = data;
		mAdapter.setData(mVideos);

		// The list should now be shown.
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}

	}

	@Override
	public void onLoaderReset(Loader<List<Video>> loader) {
		// Clear the data in the adapter.
		mAdapter.setData(null);
	}

	public static class VideoListAdapter extends ArrayAdapter<Video> {
		private final LayoutInflater mInflater;

		public VideoListAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_2);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void setData(List<Video> data) {
			clear();
			if (data != null) {
				for (Video appEntry : data) {
					add(appEntry);
				}
			}
		}

		/**
		 * Populate new items in the list.
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;

			if (convertView == null) {
				view = mInflater.inflate(R.layout.list_video_icon_text, parent,
						false);
			} else {
				view = convertView;
			}

			Video item = getItem(position);
			// get icon
			ImageView image = (ImageView) view
					.findViewById(R.id.video_list_icon);
			image.setImageDrawable(item.getIcon());
			((TextView) view.findViewById(R.id.video_list_text)).setText(item
					.getName());

			return view;
		}
	}

	/**
	 * A custom Loader that loads all of the installed applications.
	 */
	public static class VideoListLoader extends AsyncTaskLoader<List<Video>> {
		List<Video> mVideos;

		public VideoListLoader(Context context) {
			super(context);
		}

		/**
		 * This is where the bulk of our work is done. This function is called
		 * in a background thread and should generate a new set of data to be
		 * published by the loader.
		 */
		@Override
		public List<Video> loadInBackground() {
			// assign resource icon to video
			// final Context context = getContext();
			File f = new File(Storage.getVideoLocation());
			Log.d("folder", f.getPath());

			File[] files = f.listFiles();
			List<Video> entries = new ArrayList<Video>();
			for (int count = 0; count < files.length; count++) {
				String temp = files[count].getPath();
				if (temp.contains("mp4")) {
					Video entry = new Video(temp);
					entries.add(entry);
				}

			}

			// Sort the list.
			Collections.sort(entries, REV_ALPHA_COMPARATOR);

			// Done!
			return entries;
		}

		/**
		 * Called when there is new data to deliver to the client. The super
		 * class will take care of delivering it; the implementation here just
		 * adds a little more logic.
		 */
		@Override
		public void deliverResult(List<Video> videos) {
			if (isReset()) {
				// An async query came in while the loader is stopped. We
				// don't need the result.
				if (videos != null) {
					onReleaseResources(videos);
				}
			}
			List<Video> oldVideos = videos;
			mVideos = videos;

			if (isStarted()) {
				// If the Loader is currently started, we can immediately
				// deliver its results.
				super.deliverResult(videos);
			}

			// At this point we can release the resources associated with
			// 'oldApps' if needed; now that the new result is delivered we
			// know that it is no longer in use.
			if (oldVideos != null) {
				onReleaseResources(oldVideos);
			}
		}

		/**
		 * Handles a request to start the Loader.
		 */
		@Override
		protected void onStartLoading() {
			if (mVideos != null) {
				// If we currently have a result available, deliver it
				// immediately.
				deliverResult(mVideos);
			}

			// Has something interesting in the configuration changed since we
			// last built the app list?

			if (takeContentChanged() || mVideos == null) {
				forceLoad();
			}
		}

		/**
		 * Handles a request to stop the Loader.
		 */
		@Override
		protected void onStopLoading() {
			// Attempt to cancel the current load task if possible.
			cancelLoad();
		}

		/**
		 * Handles a request to cancel a load.
		 */
		@Override
		public void onCanceled(List<Video> videos) {
			super.onCanceled(videos);

			// At this point we can release the resources associated with 'apps'
			// if needed.
			onReleaseResources(videos);
		}

		/**
		 * Handles a request to completely reset the Loader.
		 */
		@Override
		protected void onReset() {
			super.onReset();

			// Ensure the loader is stopped
			onStopLoading();

			// At this point we can release the resources associated with 'apps'
			// if needed.
			if (mVideos != null) {
				onReleaseResources(mVideos);
				mVideos = null;
			}
		}

		/**
		 * Helper function to take care of releasing resources associated with
		 * an actively loaded data set.
		 */
		protected void onReleaseResources(List<Video> videos) {

		}
	}

	/**
	 * Perform reverse alphabetical comparison of application entry objects.
	 */
	public static final Comparator<Video> REV_ALPHA_COMPARATOR = new Comparator<Video>() {
		private final Collator sCollator = Collator.getInstance();

		@Override
		public int compare(Video object1, Video object2) {
			int ret = sCollator.compare(object1.getName(), object2.getName());
			if (ret > 0) {
				return -1;
			} else if (ret < 0) {
				return 1;
			} else {
				return ret;
			}

		}
	};

}
