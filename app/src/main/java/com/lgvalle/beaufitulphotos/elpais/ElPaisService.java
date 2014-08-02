package com.lgvalle.beaufitulphotos.elpais;

import com.lgvalle.beaufitulphotos.elpais.model.Rss;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by lgvalle on 31/07/14.
 */
public interface ElPaisService {


	@GET("/{section}/portada.xml")
	void getPortada(@Path("section") String section, Callback<Rss> callback);
}
