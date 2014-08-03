package com.lgvalle.beaufitulphotos;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.lgvalle.beaufitulphotos.elpais.model.Item;
import com.lgvalle.beaufitulphotos.gallery.DetailsFragment;

import java.util.List;

/**
 * Created by lgvalle on 02/08/14.
 */
public class DetailsPagerAdapter extends FragmentStatePagerAdapter {

	private final List<Item> items;

	public DetailsPagerAdapter(FragmentManager fm, List<Item> items) {
		super(fm);
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Fragment getItem(int i) {
		DetailsFragment f = DetailsFragment.newInstance(items.get(i));
		return f;
	}

	public void addItems(List<Item> items) {
		items.addAll(items);
		notifyDataSetChanged();
	}
}
