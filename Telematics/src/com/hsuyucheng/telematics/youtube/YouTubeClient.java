package com.hsuyucheng.telematics.youtube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.hsuyucheng.telematics.playing.Video;

/**
 * YouTubeClient, that connect Youtube Server and local device
 * 
 * @author hsuyucheng
 * @Version 0.9
 */
public class YouTubeClient {

	/**
	 * ask YouTube for a list of videos for a specified
	 * 
	 * @param username
	 *            - the username of who on YouTube you are browsing
	 */
	public List<Video> getYouTubeData(String username) {
		HttpClient client = new DefaultHttpClient();
		// perform a get request to YouTube for a JSON list of all the videos
		HttpUriRequest request = new HttpGet(
				"https://gdata.youtube.com/feeds/api/videos?author=" + username
						+ "&v=2&alt=jsonc");
		// get the response that YouTube sends back
		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// convert this response into a readable string
		String jsonString = null;
		try {
			jsonString = convertToString(response.getEntity().getContent());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// create a JSON object that we can use from the String
		JSONObject json = null;
		try {
			json = new JSONObject(jsonString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// get are search result items
		JSONArray jsonArray = null;
		// Create a list to store are videos in
		List<Video> videos = new ArrayList<Video>();
		// The title of the video
		String title;
		// url link back to YouTube
		String url;
		// A url to the thumbnail image of the video
		String thumbUrl;
		try {
			jsonArray = json.getJSONObject("data").getJSONArray("items");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				title = jsonObject.getString("title");

				try {
					url = jsonObject.getJSONObject("player")
							.getString("mobile");
				} catch (JSONException ignore) {
					url = jsonObject.getJSONObject("player").getString(
							"default");
				}
				thumbUrl = jsonObject.getJSONObject("thumbnail").getString(
						"sqDefault");
				String temp = jsonObject.toString();

				// Create the video object and add it to our list
				if (temp.contains("Telematics")) {
					videos.add(new Video(title, url, thumbUrl));
					Log.d("tt", title);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return videos;

	}

	/**
	 * A helper method to convert an InputStream into a String
	 * 
	 * @param inputStream
	 * @return the String or a blank string if the IS was null
	 * @throws IOException
	 */
	public static String convertToString(InputStream inputStream)
			throws IOException {
		if (inputStream != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(
						inputStream, "UTF-8"), 1024);
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				inputStream.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}
}