package com.lgvalle.beaufitulnews.ui;

import android.util.Log;
import com.lgvalle.beaufitulnews.data.ItemRepository;
import com.lgvalle.beaufitulnews.elpais.model.Item;
import com.lgvalle.beaufitulnews.elpais.model.Section;
import com.lgvalle.beaufitulnews.events.ItemChosenEvent;
import com.lgvalle.beaufitulnews.events.ItemsAvailableEvent;
import com.lgvalle.beaufitulnews.events.RequestingMoreItemsEvent;
import com.lgvalle.beaufitulnews.utils.BusHelper;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Created by lgvalle on 02/08/14.
 */
public class SectionsPagerPresenterImpl implements SectionsPagerPresenter {
	private static final String TAG = SectionsPagerPresenterImpl.class.getSimpleName();
	private final SectionsPagerScreen screen;
	private final ItemRepository repository;

	public SectionsPagerPresenterImpl(ItemRepository repository, SectionsPagerScreen screen) {

		this.repository = repository;
		this.screen = screen;
	}


	/**
	 * When a Section needs more elements
	 *
	 * @param event
	 */
	@Subscribe
	public void onSectionRequestingMoreEvent(RequestingMoreItemsEvent event) {
		if (event != null && event.getSection() != null) {
			final Section section = event.getSection();
			repository.getItemsBySection(section, new ItemRepository.Callback<List<Item>>() {
				@Override
				public void success(List<Item> items) {
					BusHelper.post(new ItemsAvailableEvent<>(items, section));
				}
			});
		}
	}

	/**
	 * When an Item is selected
	 */
	@Subscribe
	public void onItemChosen(final ItemChosenEvent event) {
		Log.d(TAG, "[BeautifulNewsPresenterImpl - onItemChosen] - (line 54): " + "item chosen");
		if (event != null && event.getItem() != null) {
			final Section section = event.getSection();
			repository.getItemsBySection(section, new ItemRepository.Callback<List<Item>>() {
				@Override
				public void success(List<Item> items) {
					Log.d(TAG, "[BeautifulNewsPresenterImpl - success] - (line 60): " + "");
					int index = items.indexOf(event.getItem());
					screen.openDetails(index, items, section);
				}
			});
		}
	}

	@Override
	public void dispose() {
		repository.storageItems();
	}
}
