package com.lgvalle.beaufitulphotos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import com.lgvalle.beaufitulphotos.elpais.ElPaisService;
import com.lgvalle.beaufitulphotos.elpais.model.Rss;
import com.lgvalle.beaufitulphotos.events.GalleryReloadEvent;
import com.lgvalle.beaufitulphotos.events.GalleryRequestingMoreElementsEvent;
import com.lgvalle.beaufitulphotos.events.NewsAvailableEvent;
import com.lgvalle.beaufitulphotos.events.PhotoDetailsAvailableEvent;
import com.lgvalle.beaufitulphotos.fivehundredpxs.Api500pxService;
import com.lgvalle.beaufitulphotos.fivehundredpxs.model.Feature;
import com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosPresenter;
import com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosScreen;
import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;
import com.lgvalle.beaufitulphotos.utils.BusHelper;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import retrofit.RetrofitError;

import java.util.ArrayList;

/**
 * Created by lgvalle on 21/07/14.
 * <p/>
 * Responsible for all business layer of it's UI {@link com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosScreen}
 * <p/>
 * This class main functionality is to query photo service and post results into app bus.
 * It also produce photo events in the bus when a someone new subscribes.
 * <p/>
 * It is subscribed to two bus events:
 * <li>{@link com.lgvalle.beaufitulphotos.events.GalleryReloadEvent} produced when the gallery needs to completely refresh it's content</li>
 * <li>{@link com.lgvalle.beaufitulphotos.events.GalleryRequestingMoreElementsEvent} produce when gallery needs more items</li>
 * <p/>
 * In both cases queries photo service and dispatch results into app bus.
 */
public class BeautifulPhotosPresenterImpl implements BeautifulPhotosPresenter {
	private static final String TAG = BeautifulPhotosPresenterImpl.class.getSimpleName();
	/* UI layer interface */
	private final BeautifulPhotosScreen screen;
	/* Network service interface */
	private final ElPaisService service;
	private Feature currentFeature;
	/* Memory cached photo-model list */
	private ArrayList<PhotoModel> photos;
	/* Service currentPage. Increments after successful operation */
	private int currentPage;
	/* Service total pages */
	private int totalPages;
	/* Index of current displayed item */
	private int itemIndex;

	/**
	 * Create presenter and set its dependencies
	 *
	 * @param screen  UI Layer interface
	 * @param service Network service interface
	 */
	public BeautifulPhotosPresenterImpl(BeautifulPhotosScreen screen, ElPaisService service, Feature feature) {
		this.screen = screen;
		this.service = service;
		this.currentFeature = feature;
		this.photos = new ArrayList<PhotoModel>();
		resetPage();
	}

	/**
	 * Request more info about a photo (in this example, the count of favorites)
	 * The information is only requested if it's not already present
	 *
	 * @param photoModel Photo object which we need more details of
	 */
	@Override
	public void needPhotoDetails(PhotoModel photoModel) {
		itemIndex = photos.indexOf(photoModel);
		if (itemIndex == -1) {
			// Item is not in photos array, something wrong has happen
			Log.e(TAG, "[BeautifulPhotosPresenterImpl - needPhotoDetails] - (line 87): " + "Asking for details of a not saved item. Should never happen");
			return;
		}

		final PhotoModel photo = photos.get(itemIndex);
		// First time asking for details (favorites in this example) we get them from server and save them into the photo item to avoid future calls
		if (photo.getFavorites() == null) {
			// TODO llamar a detalles

		} else {
			// Already got details, just post photo item on bus
			BusHelper.post(new PhotoDetailsAvailableEvent(photo));
		}
	}

	/**
	 * Request photos to service.
	 * Save results and post on bus immediately after fetching.
	 * Increments currentPage number after successful fetch.
	 * If failure, calls ui layer to display an error message.
	 */
	@Override
	public void needPhotos() {
		if (currentPage > totalPages) {
			// Already at the end. No more pages
			Log.i(TAG, "[BeautifulPhotosPresenterImpl - needPhotos] - (line 112): " + "No more pages");
			return;
		}

		service.getPortada(new retrofit.Callback<Rss>() {
			@Override
			public void success(Rss rss, retrofit.client.Response response) {
				if (rss == null) {
					Log.d(TAG, "[BeautifulPhotosPresenterImpl - success] - (line 119): " + "rss null");
				} else if (rss.getChannel() == null) {
					Log.d(TAG, "[BeautifulPhotosPresenterImpl - success] - (line 121): " + "channel null");
				}

				BusHelper.post(new NewsAvailableEvent(rss.getChannel().getItem()));
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				Log.e(TAG, "[BeautifulPhotosPresenterImpl - failure] - (line 124): " + "", retrofitError);
			}
		});
		incrementPage();
		// Update UI with current feature info
		screen.updateTitle(currentFeature.getTitle());

	}

	/**
	 * Request next currentPage of items
	 *
	 * @param event Event object is empty for this event
	 */
	@Subscribe
	public void onGalleryRequestingMoreEvent(GalleryRequestingMoreElementsEvent event) {
		needPhotos();
	}

	/**
	 * Switch to a new feature means reloading everything
	 *
	 * @param feature Feature to switch to
	 */
	@Override
	public void switchFeature(Feature feature) {
		currentFeature = feature;
		// Post this event on bus (Gallery UI should be listening and do whatever is needed)
		BusHelper.post(new GalleryReloadEvent());
		// Clear memory cached items
		photos.clear();
		// Reset page count
		resetPage();
		// Finally, request photos for new feature
		needPhotos();
	}

	/**
	 * Launch intent to share current photo
	 */
	@Override
	public void share(final Context ctx) {
		final PhotoModel photo = photos.get(itemIndex);

		// Picasso already has cached this image, so extract cached bitmap from its cache
		Picasso.with(ctx).load(photo.getLargeUrl()).into(new Target() {
			@Override
			public void onBitmapFailed(Drawable errorDrawable) {
			}

			@Override
			public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
				// Get bitmap uri from filesystem and create intent with it.
				shareBitmap(ctx, bitmap, photo.getTitle());
			}

			@Override
			public void onPrepareLoad(Drawable placeHolderDrawable) {
				 /* nothing to do */
			}
		});

	}

	/**
	 * Get bitmap uri from filesystem and create intent with it.
	 *
	 * @param bitmap Image bitmap
	 * @param title  Image title
	 */
	private void shareBitmap(Context ctx, Bitmap bitmap, String title) {
		// TODO: do this in a new separate thread if needed
		String path = MediaStore.Images.Media.insertImage(ctx.getContentResolver(), bitmap, title, null);
		if (path == null) {
			screen.showError(R.string.share_error);
		} else {
			Uri uri = Uri.parse(path);
			final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra(Intent.EXTRA_STREAM, uri);
			intent.setType("image/png");
			ctx.startActivity(intent);
		}
	}

	/**
	 * Decrement page until reach first one
	 */
	private void decrementPage() {
		if (currentPage > 1) {
			currentPage--;
		}
	}

	/**
	 * Increment currentPage number until reach max pages
	 */
	private void incrementPage() {
		if (currentPage < totalPages) {
			currentPage++;
		}
	}

	/**
	 * Reset currentPage number to first one in current service
	 */
	private void resetPage() {
		currentPage = Api500pxService.FIRST_PAGE;
		totalPages = Integer.MAX_VALUE;
	}
}
