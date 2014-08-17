package com.lgvalle.beaufitulnews.ui;

import com.lgvalle.beaufitulnews.data.ItemRepository;
import com.lgvalle.beaufitulnews.elpais.model.Item;
import com.lgvalle.beaufitulnews.elpais.model.Section;

import java.util.List;

/**
 * Created by luis.gonzalez on 11/08/14.
 */
public class DetailsPagerPresenterImpl implements DetailsPagerPresenter{

	private final DetailsPagerScreen screen;
	private final ItemRepository repository;

	public DetailsPagerPresenterImpl(ItemRepository repository, DetailsPagerScreen screen) {
		this.repository = repository;
		this.screen = screen;
	}


	@Override
	public void needItems(Section section) {
		repository.getItemsBySection(section, new ItemRepository.Callback<List<Item>>() {
			@Override
			public void success(List<Item> items) {
				screen.setItems(items);
			}
		});

	}
}
