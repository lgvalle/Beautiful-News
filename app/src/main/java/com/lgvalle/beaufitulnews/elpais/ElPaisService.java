package com.lgvalle.beaufitulnews.elpais;

import com.lgvalle.beaufitulnews.elpais.model.Rss;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by lgvalle on 31/07/14.
 */
public interface ElPaisService {


	@GET("/{section}")
	void getPortada(@Path("section") String section, Callback<Rss> callback);

	@GET("/tag/rss/{tag}")
	void getTag(@Path("section") String section, Callback<Rss> callback);
}
