package com.lgvalle.beaufitulphotos.gallery;

import android.os.Bundle;
import butterknife.OnItemClick;
import com.lgvalle.beaufitulphotos.R;
import com.lgvalle.beaufitulphotos.elpais.model.Item;
import com.lgvalle.beaufitulphotos.elpais.model.Section;
import com.lgvalle.beaufitulphotos.events.GalleryItemsAvailableEvent;
import com.lgvalle.beaufitulphotos.events.GalleryRequestingMoreElementsEvent;
import com.lgvalle.beaufitulphotos.events.NewsItemChosen;
import com.lgvalle.beaufitulphotos.utils.BusHelper;
import com.lgvalle.beaufitulphotos.utils.Renderer;
import com.squareup.otto.Subscribe;

/**
 * Created by lgvalle on 02/08/14.
 */
public class NewsListFragment extends BaseElementListFragment<Item> {
	private static final String TAG = NewsListFragment.class.getSimpleName();
	private Section section;

	@Override
	protected Renderer<Item> getRenderer() {
		return new NewsItemRenderer();
	}

	public static NewsListFragment newInstance(Section section) {
		NewsListFragment f = new NewsListFragment();
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
		if (adapter.isEmpty()) {
			BusHelper.post(new GalleryRequestingMoreElementsEvent(section));
		}

	}

	@Subscribe
	public void onItemsAvailableEvent(GalleryItemsAvailableEvent<Item, Section> event) {
		if (event.getSection().equals(section)) {
			super.onItemsAvailableEvent(event);
		}
	}

	/**
	 * Click on a gallery item
	 *
	 * @param position Position of clicked item
	 */
	@OnItemClick(R.id.photo_list)
	public void onItemClick(int position) {
		BusHelper.post(new NewsItemChosen(section, adapter.getItem(position)));
	}
}
