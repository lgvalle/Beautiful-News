package com.lgvalle.beaufitulnews.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Xml;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;
import com.facebook.rebound.*;
import com.lgvalle.beaufitulnews.R;
import com.lgvalle.beaufitulnews.elpais.model.Item;
import com.lgvalle.beaufitulnews.elpais.model.Section;
import com.lgvalle.beaufitulnews.events.ItemChosenEvent;
import com.lgvalle.beaufitulnews.utils.BusHelper;
import com.lgvalle.beaufitulnews.utils.PicassoHelper;
import com.lgvalle.beaufitulnews.utils.TimeHelper;
import com.nirhart.parallaxscroll.views.ParallaxScrollView;

/**
 * Created by lgvalle on 22/07/14.
 * <p/>
 * Display a single photo in full screen.
 * <p/>
 * Initializes photoview with already cached thumbnail while fetching large image
 */
public class DetailsFragment extends BaseFragment {
	private static final String TAG = DetailsFragment.class.getSimpleName();
	/* Animations */
	private static final SpringConfig SPRING_CONFIG = SpringConfig.fromOrigamiTensionAndFriction(40, 10);
	public static final String INTENT_EXTRA_ITEM = "item";
	public static final String INTENT_EXTRA_SECTION = "section";
	private Spring mSpring;
	/* Views */
	@InjectView(R.id.parallax_scrollview)
	ParallaxScrollView scrollView;
	@InjectView(R.id.photo)
	ImageView ivPhoto;
	@InjectView(R.id.photo_enlarged)
	ImageView ivPhotoEnlarged;
	@InjectView(R.id.item_entradilla)
	TextView tvEntradilla;
	@InjectView(R.id.item_titular)
	TextView tvTitular;
	@InjectView(R.id.item_autor)
	TextView tvDate;
	@InjectView(R.id.item_cuerpo)
	WebView tvCuerpo;
	private Item item;
	private Section section;
	private double scrolled;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		item = getArguments().getParcelable(INTENT_EXTRA_ITEM);
		section = getArguments().getParcelable(INTENT_EXTRA_SECTION);
		// Setup the Spring by creating a SpringSystem adding a SimpleListener that renders the
		// animation whenever the spring is updated.
		mSpring = SpringSystem.create().createSpring().setSpringConfig(SPRING_CONFIG).addListener(new SimpleSpringListener() {
			@Override
			public void onSpringUpdate(Spring spring) {
				// Just tell the UI to update based on the springs current state.
				animate();
			}
		});

	}


	@Override
	public void onResume() {
		super.onResume();
		BusHelper.register(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		BusHelper.unregister(this);
	}

	public static DetailsFragment newInstance(Item item, Section section) {
		DetailsFragment f = new DetailsFragment();
		Bundle args = new Bundle();
		args.putParcelable(INTENT_EXTRA_ITEM, item);
		args.putParcelable(INTENT_EXTRA_SECTION, section);
		f.setArguments(args);
		return f;
	}


	@Override
	protected int getContentView() {
		return R.layout.fragment_item_details;
	}

	/**
	 * Layout is initialized empty. Photos are loaded by listening to bus events
	 */
	@Override
	protected void initLayout() {
		bindImages(item);
		bindTexts(item);
	}

	/**
	 * Load photo info from photoModel.
	 * First load thumbnail as background, and then load large photo in foreground
	 *
	 * @param item Object containing photo info
	 */
	private void bindImages(final Item item) {
		PicassoHelper.load(getActivity(), item.getImageURLLarge(), ivPhoto);
	}

	private void bindTexts(final Item item) {
		tvTitular.setText(item.getTitle());
		tvTitular.setBackgroundColor(section.getColor());
		tvEntradilla.setText(item.getDescription().get(0));
		tvDate.setText(DateUtils.formatDateTime(getActivity(), TimeHelper.getTimestamp(item.getPubDate()), DateUtils.FORMAT_SHOW_DATE));

		if (item.getDescription().size() > 1) {
			buildWebContent();
		}
	}

	private void buildWebContent() {
		// Build webview
		StringBuilder sb = new StringBuilder();
		sb.append(getActivity().getString(R.string.html_header));
		sb.append(item.getDescription().get(1));
		sb.append(getActivity().getString(R.string.html_footer));

		tvCuerpo.getSettings().setDefaultTextEncodingName(Xml.Encoding.UTF_8.toString());
		tvCuerpo.loadDataWithBaseURL("file:///android_asset/", sb.toString(), "text/html", Xml.Encoding.UTF_8.toString(), null);
		//tvCuerpo.loadData(sb.toString(), "text/html; charset=UTF-8", Xml.Encoding.UTF_8.toString());
		tvCuerpo.setBackgroundColor(0x00000000);
	}

	/**
	 * Execute spring animations
	 */
	private void animate() {
		// Map the spring to info bar position so that its hidden off screen and bounces in on ui restore.
		float position = (float) SpringUtil.mapValueFromRangeToRange(mSpring.getCurrentValue(), 0, 1, 0, -ivPhoto.getHeight());
		float alpha = (float) SpringUtil.mapValueFromRangeToRange(mSpring.getCurrentValue(), 0, 1, 0, 1);
		ivPhoto.setTranslationY(position);
	}

	/**
	 * When a new Gallery Item is selected, clear previous image views and load the new one
	 */
	public void onGalleryItemChosen(ItemChosenEvent event) {
		if (event != null && event.getItem() != null) {
			bindImages(event.getItem());
			bindTexts(event.getItem());
		}
	}

	@OnClick(R.id.action_share)
	public void onShare() {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_SUBJECT, item.getTitle());
		i.putExtra(Intent.EXTRA_TEXT, item.getLink());
		startActivity(Intent.createChooser(i, getString(R.string.share)));
	}

	@OnClick(R.id.action_up)
	public void onClickBack() {
		getActivity().finish();
	}

	/*
	*
	* TODO Not working perfect. Need improvement
	*

	@OnClick({R.id.photo, R.id.photo_enlarged})
	public void onClickPhoto() {
		if (ivPhotoEnlarged.getVisibility() == View.VISIBLE) {
			mSpring.setEndValue(0);

			//ivPhoto.setVisibility(View.VISIBLE);
			ivPhotoEnlarged.setVisibility(View.GONE);
		} else {
			//ivPhoto.setVisibility(View.GONE);
			mSpring.setEndValue(1);
			PicassoHelper.load(getActivity(), item.getImageURLLarge(), ivPhotoEnlarged);
			ivPhotoEnlarged.setVisibility(View.VISIBLE);
		}
	}
	*/
}