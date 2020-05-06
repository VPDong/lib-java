package com.vpdong.lib.java;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
	@Test
	public void doTest() {
		Context context = InstrumentationRegistry.getInstrumentation().getContext();
		assertEquals("com.vpdong.lib.java.test", context.getPackageName());
	}
}
