package com.lgvalle.beaufitulphotos;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.lgvalle.beaufitulphotos.elpais.model.Item;
import com.lgvalle.beaufitulphotos.elpais.model.Section;
import com.lgvalle.beaufitulphotos.gallery.BaseGalleryFragment;
import com.lgvalle.beaufitulphotos.gallery.NewsGalleryFragment;

/**
 * Created by lgvalle on 02/08/14.
 */
public class GalleriesPagerAdapter extends FragmentStatePagerAdapter {

	private final Section[] sections;

	public GalleriesPagerAdapter(FragmentManager fm, Section[] sections) {
		super(fm);
		this.sections = sections;
	}

	@Override
	public int getCount() {
		return sections.length;
	}

	@Override
	public Fragment getItem(int i) {
		BaseGalleryFragment<Item> news = NewsGalleryFragment.newInstance(sections[i]);;
		return news;
	}
}
