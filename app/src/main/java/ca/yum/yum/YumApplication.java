package ca.yum.yum;

import android.app.Application;
import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Locale;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by nijan.
 */

public class YumApplication extends Application {
	ObjectMapper objectMapper;
	OkHttpClient httpClient;

	@Override
	public void onCreate() {
		super.onCreate();
		objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH));
		Cache cache = new Cache(getCacheDir(), 20 * 1024 * 1024);
		httpClient = new OkHttpClient.Builder().cache(cache).build();
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public OkHttpClient getHttpClient() {
		return httpClient;
	}

	public static YumApplication fromContext(Context context) {
		return (YumApplication) context.getApplicationContext();
	}
}
