package com.example.vivlio;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.Surface;
import android.view.SurfaceView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static junit.framework.TestCase.assertTrue;


public class BarcodeScannerTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<BarcodeScannerActivity> rule =
                            new ActivityTestRule<>(BarcodeScannerActivity.class,
                            true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkPreview() {
        solo.assertCurrentActivity("Wrong Activity", BarcodeScannerActivity.class);
        SurfaceView view = (SurfaceView) solo.getView(R.id.surface_view);

        // checks to see if a valid surface for SurfaceView has been created
        assertTrue(view.getHolder().getSurface().isValid());
    }
}
