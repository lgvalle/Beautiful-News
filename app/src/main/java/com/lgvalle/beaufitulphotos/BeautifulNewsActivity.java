package com.lgvalle.beaufitulphotos;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.lgvalle.beaufitulphotos.elpais.ElPaisModule;
import com.lgvalle.beaufitulphotos.elpais.model.Item;
import com.lgvalle.beaufitulphotos.elpais.model.Section;
import com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosScreen;
import com.lgvalle.beaufitulphotos.utils.BusHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;


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
public class BeautifulNewsActivity extends BaseActivity implements BeautifulPhotosScreen, SlidingUpPanelLayout.PanelSlideListener {
	static final String FRAGMENT_GALLERY_TAG = "fragment_gallery_tag";
	static final String FRAGMENT_DETAILS_TAG = "fragment_details_tag";
	/* Manage all business logic for this activity */

	/* Actionbar title */
	private String title;

	Section sections[];
	private BeautifulNewsPresenterImpl presenter;
	/* Views */
	@InjectView(R.id.sliding_layout)
	SlidingUpPanelLayout panel;

	@InjectView(R.id.pager)
	ViewPager pager;


	@Override
	protected void onResume() {
		super.onResume();
		// Register on bus to let activity and presenter listen to events
		BusHelper.register(this);
		BusHelper.register(presenter);

	}

	@Override
	protected void onPause() {
		super.onPause();
		// Unregister every time activity is paused
		BusHelper.unregister(this);
		BusHelper.unregister(presenter);
	}

	@Override
	public void onBackPressed() {
		// If panel is expanded -> collapse
		// If panel is not expanded forward call to super class (making activity close)
		if (panel.collapsePanel()) {
			actionBarToTabs();
		} else {
			super.onBackPressed();
		}
	}


	@Override
	public void onPanelAnchored(View view) {
	}

	@Override
	public void onPanelHidden(View view) {
	}

	@Override
	public void onPanelSlide(View view, float v) {
	}

	@Override
	public void onPanelCollapsed(View view) {
		// When panel collapsed restore actionbar title and UI elements
		actionBarToTabs();
		toggleUI(false);
	}

	@Override
	public void onPanelExpanded(View view) {
		// When panel expands (photo selected) always display actionbar with back button
		toggleUI(true);
	}

	@Override
	public void showError(int errorID) {
		// Sample error managing with a Toast
		Toast.makeText(this, getString(errorID), Toast.LENGTH_SHORT).show();
	}

	/**
	 * Screen api method to update this UI element title
	 *
	 * @param titleRes Title resource
	 */
	@Override
	public void updateTitle(int titleRes) {
		title = getString(titleRes);
		getSupportActionBar().setTitle(title);
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_main;
	}

	@Override
	protected void initLayout() {
		ButterKnife.inject(this);



		GalleriesPagerAdapter adapter = new GalleriesPagerAdapter(getSupportFragmentManager(), sections);

		// Listen to details panel to act in actionbar
		panel.setPanelSlideListener(this);
		// Add Gallery Fragment to main_content frame. If this is a tablet there will be another frame to add content
		pager.setAdapter(adapter);
		// Bind pager to tabs
		pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			                              @Override
			                              public void onPageSelected(int position) {
				                              // When swiping between pages, select the
				                              // corresponding tab.
				                              getSupportActionBar().setSelectedNavigationItem(position);
			                              }
		                              }
		);
	}

	private void buildSections() {
		String[] sections_array = getResources().getStringArray(R.array.sections_url);
		sections = new Section[sections_array.length];
		for (int i=0;i<sections.length;i++) {
			sections[i] = new Section(sections_array[i], R.string.section_portada);
		}
	}

	@Override
	protected void initActionBar() {
		super.initActionBar();
		actionBarToTabs();
		buildSections();
		// Add 3 tabs, specifying the tab's text and TabListener
		for (int i = 0; i < sections.length; i++) {
			ActionBar.Tab tab = getSupportActionBar().newTab().setText(sections[i].getTitle()).setTabListener(new ActionBar.TabListener() {
				@Override
				public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
					if (pager != null) {
						pager.setCurrentItem(tab.getPosition());
					}
					getSupportActionBar().show();
				}

				@Override
				public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

				}

				@Override
				public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

				}
			});
			getSupportActionBar().addTab(tab);

		}
	}

	@Override
	protected void initPresenter() {
		// Init activity presenter with all it's dependencies
		this.presenter = new BeautifulNewsPresenterImpl(ElPaisModule.getService(), this);

	}

	/**
	 * Show/Hide elements of UI depending if panel is expanded or not
	 *
	 * @param panelExpanded True if panel is expanded, false otherwise
	 */
	private void toggleUI(boolean panelExpanded) {
		// Only toggle this elements if not a tablet

	}


	@Override
	public void openDetails(int itemIndex, List<Item> items) {
		Intent i = new Intent(this, DetailsPagerActivity.class);
		i.putExtra(DetailsPagerActivity.INTENT_EXTRAS_INDEX, itemIndex);
		i.putParcelableArrayListExtra(DetailsPagerActivity.INTENT_EXTRAS_ITEMS, (ArrayList<? extends Parcelable>) items);
		startActivity(i);
	}

	private void actionBarToTabs() {
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().show();
	}
}
