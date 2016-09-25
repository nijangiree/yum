package ca.yum.yum.businesses;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.yum.yum.YumApplication;
import ca.yum.yum.model.CategorizedBusinesses;
import ca.yum.yum.yelp.YelpController;
import ca.yum.yum.yelp.YelpOAuth;
import okhttp3.OkHttpClient;

/**
 * Created by nijan.
 */
public class BaseDataFragment extends Fragment {
	YelpController yelpController;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	public void initialize(Context context) {
		YumApplication app = YumApplication.fromContext(context);
		ObjectMapper objectMapper = app.getObjectMapper();
		OkHttpClient httpClient = app.getHttpClient();
		YelpOAuth yelpOAuth = new YelpOAuth(httpClient, objectMapper);
		yelpController = new YelpController(httpClient, objectMapper, yelpOAuth);
	}

	public YelpController getYelpController() {
		return yelpController;
	}

	public void setYelpController(YelpController yelpController) {
		this.yelpController = yelpController;
	}
}
