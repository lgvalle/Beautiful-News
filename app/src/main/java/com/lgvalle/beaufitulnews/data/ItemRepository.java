package com.lgvalle.beaufitulnews.data;

import com.lgvalle.beaufitulnews.elpais.model.Item;
import com.lgvalle.beaufitulnews.elpais.model.Section;

import java.util.List;

/**
 * Created by lgvalle on 10/08/14.
 */
public interface ItemRepository {
	public void getItemsBySection(Section section, OnlineItemRepository.Callback<List<Item>> callback);

	public void storageItems();

	public interface Callback<List> {
		public void success(List items);
	}
}
