package com.lgvalle.beaufitulnews.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.crashlytics.android.Crashlytics;
import com.lgvalle.beaufitulnews.R;
import com.lgvalle.beaufitulnews.data.InPreferencesItemStorage;
import com.lgvalle.beaufitulnews.data.ItemRepository;
import com.lgvalle.beaufitulnews.data.ItemStorage;
import com.lgvalle.beaufitulnews.data.OnlineItemRepository;
import com.lgvalle.beaufitulnews.elpais.ElPaisModule;
import com.lgvalle.beaufitulnews.elpais.model.Item;
import com.lgvalle.beaufitulnews.elpais.model.Section;
import com.lgvalle.beaufitulnews.utils.BusHelper;
import com.lgvalle.beaufitulnews.utils.PrefsManager;

import java.util.List;


public class SectionsPagerActivity extends BaseActivity implements SectionsPagerScreen {
	private static final String TAG = SectionsPagerActivity.class.getSimpleName();

	private SectionsPagerPresenter presenter;
	private Section sections[];
	@InjectView(R.id.pager)
	ViewPager pager;
	@InjectView(R.id.drawer_layout)
	DrawerLayout drawer;
	@InjectView(R.id.navdrawer_items)
	LinearLayout llDrawerItems;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
	}

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
	protected void onStop() {
		super.onStop();
		presenter.dispose();
	}

	@Override
	public void showError(int errorID) {
		// Sample error managing with a Toast
		Toast.makeText(this, getString(errorID), Toast.LENGTH_SHORT).show();
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_main;
	}

	@Override
	protected void initLayout() {
		ButterKnife.inject(this);
		createDrawer();
		SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager(), sections);
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

	private void createDrawer() {
		drawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
		createDrawerItems();
	}

	private void createDrawerItems() {
		llDrawerItems.removeAllViews();
		int i = 0;
		for (Section section : sections) {
			View v = createDrawerItem(section, i);
			llDrawerItems.addView(v);
			i++;
		}
	}

	private View createDrawerItem(final Section s, final int index) {
		View view = getLayoutInflater().inflate(R.layout.drawer_item, llDrawerItems, false);
		TextView title = (TextView) view.findViewById(R.id.drawer_item_title);
		title.setText(s.getTitle());

		View indicator = view.findViewById(R.id.drawer_item_indicator);
		GradientDrawable shapeDrawable = (GradientDrawable) indicator.getBackground();
		shapeDrawable.setColor(s.getColor());

		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onSectionClicked(index);
			}
		});
		return view;
	}

	/**
	 * Click on drawer item (section)
	 *
	 * @param index Element clicked
	 */
	private void onSectionClicked(int index) {
		pager.setCurrentItem(index);
		drawer.closeDrawer(Gravity.START);
	}

	/**
	 * Build sections array from strings
	 */
	private void buildSections() {
		String[] sections_array = getResources().getStringArray(R.array.sections_url);
		String[] sections_name_array = getResources().getStringArray(R.array.sections_name);
		int[] sections_color_array = getResources().getIntArray(R.array.sections_color);
		sections = new Section[sections_array.length];
		for (int i = 0; i < sections.length; i++) {
			sections[i] = new Section(sections_array[i], sections_name_array[i], sections_color_array[i]);
		}
	}

	@Override
	protected void initActionBar() {
		super.initActionBar();
		actionBarToTabs();
		buildSections();
		for (int i = 0; i < sections.length; i++) {
			createTab(i);
		}
	}

	private void createTab(int i) {
		final int finalI = i;
		ActionBar.Tab tab = getSupportActionBar().newTab();
		tab.setTabListener(new ActionBar.TabListener() {
			@Override
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
				if (pager != null) {
					pager.setCurrentItem(tab.getPosition());
				}
				getSupportActionBar().setSelectedNavigationItem(tab.getPosition());
				getSupportActionBar().show();
				getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(sections[finalI].getColor()));

			}

			@Override
			public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

			}

			@Override
			public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

			}
		});

		int tabViewResId = i == 0 ? R.layout.tab_home_custom : R.layout.tab_custom;
		View tabView = getLayoutInflater().inflate(tabViewResId, null);
		TextView tabText = (TextView) tabView.findViewById(R.id.tab_text);
		tabText.setText(sections[i].getTitle());
		tab.setCustomView(tabView);

		getSupportActionBar().addTab(tab);

	}

	@Override
	protected void initPresenter() {
		// Init activity presenter with all it's dependencies
		ItemStorage storage = InPreferencesItemStorage.getInstance(PrefsManager.getInstance(this));
		ItemRepository repository = OnlineItemRepository.getInstance(ElPaisModule.getService(), storage);

		this.presenter = new SectionsPagerPresenterImpl(repository, this);
	}

	@Override
	public void openDetails(int itemIndex, List<Item> items, Section section) {
		Log.d(TAG, "[BeautifulNewsActivity - openDetails] - (line 238): " + "open details");
		Intent i = new Intent(this, DetailsPagerActivity.class);
		i.putExtra(DetailsPagerActivity.INTENT_EXTRA_INDEX, itemIndex);
		i.putExtra(DetailsPagerActivity.INTENT_EXTRA_SECTION, section);
		//i.putParcelableArrayListExtra(DetailsPagerActivity.INTENT_EXTRA_ITEMS, (ArrayList<? extends Parcelable>) items);
		Log.d(TAG, "[BeautifulNewsActivity - openDetails] - (line 244): " + "intent built");
		startActivity(i);

	}

	private void actionBarToTabs() {
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().show();
	}
}
