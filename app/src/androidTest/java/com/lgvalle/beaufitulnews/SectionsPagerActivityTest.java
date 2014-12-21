package com.lgvalle.beaufitulnews;

import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;

import com.lgvalle.beaufitulnews.ui.SectionsPagerActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

/**
 * Created by lgvalle on 21/12/14.
 */
public class SectionsPagerActivityTest extends ActivityInstrumentationTestCase2<SectionsPagerActivity> {
    public SectionsPagerActivityTest() {
        super(SectionsPagerActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testLayoutSetup() {
        onView(ViewMatchers.withId(R.id.pager))
                .check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.drawer_layout))
                .check(matches(isDisplayed()));
    }
}
