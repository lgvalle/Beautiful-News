package com.lgvalle.beaufitulphotos;

import com.lgvalle.beaufitulphotos.data.ItemRepository;
import com.lgvalle.beaufitulphotos.elpais.model.Item;
import com.lgvalle.beaufitulphotos.elpais.model.Section;
import com.lgvalle.beaufitulphotos.events.GalleryItemsAvailableEvent;
import com.lgvalle.beaufitulphotos.events.GalleryRequestingMoreElementsEvent;
import com.lgvalle.beaufitulphotos.events.NewsItemChosen;
import com.lgvalle.beaufitulphotos.interfaces.BeautifulNewsPresenter;
import com.lgvalle.beaufitulphotos.interfaces.BeautifulNewsScreen;
import com.lgvalle.beaufitulphotos.utils.BusHelper;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Created by lgvalle on 02/08/14.
 */
public class BeautifulNewsPresenterImpl implements BeautifulNewsPresenter {
	private static final String TAG = BeautifulNewsPresenterImpl.class.getSimpleName();
	private final BeautifulNewsScreen screen;
	private final ItemRepository repository;

	public BeautifulNewsPresenterImpl(ItemRepository repository, BeautifulNewsScreen screen) {
		this.repository = repository;
		this.screen = screen;
	}


	/**
	 * When a Section needs more elements
	 *
	 * @param event
	 */
	@Subscribe
	public void onSectionRequestingMoreEvent(GalleryRequestingMoreElementsEvent event) {
		if (event != null && event.getSection() != null) {
			final Section section = event.getSection();
			repository.getItemsBySection(section, new ItemRepository.Callback<List<Item>>() {
				@Override
				public void success(List<Item> items) {
					BusHelper.post(new GalleryItemsAvailableEvent<>(items, section));
				}
			});
		}
	}

	/**
	 * When an Item is selected
	 */
	@Subscribe
	public void onItemChosen(final NewsItemChosen event) {
		if (event != null && event.getItem() != null) {
			final Section section = event.getSection();
			repository.getItemsBySection(section, new ItemRepository.Callback<List<Item>>() {
				@Override
				public void success(List<Item> items) {
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
