package com.lgvalle.beaufitulnews.ui;

import com.lgvalle.beaufitulnews.elpais.model.Item;
import com.lgvalle.beaufitulnews.elpais.model.Section;

import java.util.List;


public interface SectionsPagerScreen {
	void showError(int errorId);
	void openDetails(int index, List<Item> items, Section section);

}
