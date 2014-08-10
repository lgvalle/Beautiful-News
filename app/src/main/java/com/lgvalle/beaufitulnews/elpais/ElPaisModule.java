package com.lgvalle.beaufitulnews.elpais;


import retrofit.RestAdapter;
import retrofit.converter.SimpleXMLConverter;

/**
 * Created by lgvalle on 31/07/14.
 */
public class ElPaisModule {
	private static final String END_POINT = "http://elpais.com/rss/";
	private static final ElPaisService service;

	static {
		// Interceptor to append consumer key on every request
		/*
		RequestInterceptor requestInterceptor = new RequestInterceptor() {
			@Override
			public void intercept(RequestFacade request) {
				request.addQueryParam(CONSUMER_KEY_PARAM, CONSUMER_KEY_VALUE);
			}
		};

		*/

		// Configure an adapter for this client
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(END_POINT)

				.setLogLevel(RestAdapter.LogLevel.BASIC)
				.setConverter(new SimpleXMLConverter())
				.build();

		// Create rest client
		service = restAdapter.create(ElPaisService.class);
	}

	/**
	 * Hide constructor
	 */
	private ElPaisModule() {}

	/**
	 * Expose rest client
	 */
	public static ElPaisService getService() {
		return service;
	}

}