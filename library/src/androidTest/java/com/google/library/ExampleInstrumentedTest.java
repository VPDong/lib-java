package com.google.library;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
	@Test
	public void doTest() {
		Context context = InstrumentationRegistry.getInstrumentation().getContext();
		assertEquals("com.google.library", context.getPackageName());
	}
}
