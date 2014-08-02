package com.lgvalle.beaufitulphotos.gallery;

import android.os.Bundle;
import android.util.Log;
import com.lgvalle.beaufitulphotos.elpais.model.Item;
import com.lgvalle.beaufitulphotos.elpais.model.Section;
import com.lgvalle.beaufitulphotos.events.GalleryItemsAvailableEvent;
import com.lgvalle.beaufitulphotos.events.GalleryRequestingMoreElementsEvent;
import com.lgvalle.beaufitulphotos.utils.BusHelper;
import com.lgvalle.beaufitulphotos.utils.Renderer;
import com.squareup.otto.Subscribe;

/**
 * Created by lgvalle on 02/08/14.
 */
public class NewsGalleryFragment extends BaseGalleryFragment<Item> {
	private static final String TAG = NewsGalleryFragment.class.getSimpleName();
	private Section section;

	@Override
	protected Renderer<Item> getRenderer() {
		return new NewsItemRenderer();
	}

	public static NewsGalleryFragment newInstance(Section section) {
		NewsGalleryFragment f = new NewsGalleryFragment();
		Bundle args = new Bundle();
		args.putSerializable("section", section);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.section = (Section) getArguments().getSerializable("section");


	}

	@Override
	public void onResume() {
		super.onResume();

		// Empty list? Ask for some items!
		BusHelper.post(new GalleryRequestingMoreElementsEvent(section));

	}

	@Override
	public void onPause() {
		super.onPause();

		Log.d(TAG, "[NewsGalleryFragment - onPause] - (line 55): " + "pausing "+section.getParam());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "[NewsGalleryFragment - onDestroy] - (line 63): " + "destroying "+section.getParam());
	}

	@Subscribe
	public void onItemsAvailableEvent(GalleryItemsAvailableEvent<Item, Section> event) {
		if (event.getSection() .equals(section)) {
			super.onItemsAvailableEvent(event);
		}

	}
}
