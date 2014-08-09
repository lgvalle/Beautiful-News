package com.lgvalle.beaufitulphotos;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.lgvalle.beaufitulphotos.elpais.model.Item;
import com.lgvalle.beaufitulphotos.elpais.model.Section;
import com.lgvalle.beaufitulphotos.gallery.DetailsFragment;

import java.util.List;

/**
 * Created by lgvalle on 02/08/14.
 */
public class DetailsPagerAdapter extends FragmentStatePagerAdapter {

	private final List<Item> items;
	private final Section section;

	public DetailsPagerAdapter(FragmentManager fm, List<Item> items, Section section) {
		super(fm);
		this.items = items;
		this.section = section;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Fragment getItem(int i) {
		DetailsFragment f = DetailsFragment.newInstance(items.get(i), section);
		return f;
	}

	public void addItems(List<Item> items) {
		items.addAll(items);
		notifyDataSetChanged();
	}
}
