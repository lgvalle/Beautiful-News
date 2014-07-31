package com.lgvalle.beaufitulphotos.elpais;

import com.lgvalle.beaufitulphotos.elpais.model.Rss;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by lgvalle on 31/07/14.
 */
public interface ElPaisService {


	@GET("/portada.xml")
	void getPortada(Callback<Rss> callback);
}
