package com.lgvalle.beaufitulphotos;

import android.support.v4.view.ViewPager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.lgvalle.beaufitulphotos.elpais.model.Item;
import com.lgvalle.beaufitulphotos.utils.BusHelper;
import com.xgc1986.parallaxPagerTransformer.ParallaxPagerTransformer;

import java.util.ArrayList;


/**
 * Main activity
 * <p/>
 * This class is on UI layer, so it's only responsible for UI interactions.
 * <p/>
 * It loads a Presenter to manage all business logic: data fetching and caching.
 * <p/>
 * The UI consist in two fragments: one with a list of photos and one for photo details.
 * <p/>
 * Finally, the activity (screen) creates a presenter and ask for photos. Results communication will happen through the event bus
 */
public class DetailsPagerActivity extends BaseActivity {
	static final String FRAGMENT_GALLERY_TAG = "fragment_gallery_tag";
	static final String FRAGMENT_DETAILS_TAG = "fragment_details_tag";
	public static final String INTENT_EXTRAS_ITEMS = "intent_extras_items";
	public static final String INTENT_EXTRAS_INDEX = "intent_extras_index";
	/* Manage all business logic for this activity */

	/* Actionbar title */
	private String title;

	private BeautifulNewsPresenterImpl presenter;

	@InjectView(R.id.pager)
	ViewPager pager;
	private ArrayList<Item> item;
	private int index;


	@Override
	protected void getExtras() {
		super.getExtras();
		item = getIntent().getExtras().getParcelableArrayList(INTENT_EXTRAS_ITEMS);
		index = getIntent().getExtras().getInt(INTENT_EXTRAS_INDEX);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Register on bus to let activity and presenter listen to events
		BusHelper.register(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		// Unregister every time activity is paused
		BusHelper.unregister(this);

	}


	@Override
	protected int getContentView() {
		return R.layout.activity_main;
	}

	@Override
	protected void initLayout() {
		ButterKnife.inject(this);

		DetailsPagerAdapter adapter = new DetailsPagerAdapter(getSupportFragmentManager(), item);

		// Add Gallery Fragment to main_content frame. If this is a tablet there will be another frame to add content
		pager.setAdapter(adapter);
		pager.setPageTransformer(false, new ParallaxPagerTransformer(R.id.photo));

		// Scroll to selected item
		pager.setCurrentItem(index);

	}

	@Override
	protected void initActionBar() {
		super.initActionBar();
		getSupportActionBar().hide();
	}

	@Override
	protected void initPresenter() {
		// Init activity presenter with all it's dependencies
		//this.presenter = new BeautifulNewsPresenterImpl(ElPaisModule.getService());

	}

}
