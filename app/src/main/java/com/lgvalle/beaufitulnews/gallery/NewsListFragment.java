package com.lgvalle.beaufitulnews.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.OnItemClick;
import com.lgvalle.beaufitulnews.R;
import com.lgvalle.beaufitulnews.elpais.model.Item;
import com.lgvalle.beaufitulnews.elpais.model.Section;
import com.lgvalle.beaufitulnews.events.GalleryItemsAvailableEvent;
import com.lgvalle.beaufitulnews.events.GalleryRequestingMoreElementsEvent;
import com.lgvalle.beaufitulnews.events.NewsItemChosen;
import com.lgvalle.beaufitulnews.utils.BusHelper;
import com.lgvalle.beaufitulnews.utils.Renderer;
import com.squareup.otto.Subscribe;

/**
 * Created by lgvalle on 02/08/14.
 */
public class NewsListFragment extends BaseElementListFragment<Item> {
	private static final String TAG = NewsListFragment.class.getSimpleName();
	private static final String INTENT_EXTRA_SECTION = "section";
	private Section section;

	@Override
	protected Renderer<Item> getRenderer() {
		return new NewsItemRenderer();
	}

	public static NewsListFragment newInstance(Section section) {
		NewsListFragment f = new NewsListFragment();
		Bundle args = new Bundle();
		args.putParcelable(INTENT_EXTRA_SECTION, section);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.section = getArguments().getParcelable(INTENT_EXTRA_SECTION);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		// Empty list? Ask for some items!
		if (adapter.isEmpty()) {
			BusHelper.post(new GalleryRequestingMoreElementsEvent(section));
		}
	}

	@Override
	protected void initLayout() {
		super.initLayout();
		list.setBackgroundColor(section.getColor());
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
