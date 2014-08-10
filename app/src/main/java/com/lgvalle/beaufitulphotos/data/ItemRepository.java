package com.lgvalle.beaufitulphotos.data;

import com.lgvalle.beaufitulphotos.elpais.model.Item;
import com.lgvalle.beaufitulphotos.elpais.model.Section;

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
