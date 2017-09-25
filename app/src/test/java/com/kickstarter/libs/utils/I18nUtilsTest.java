package com.kickstarter.libs.utils;

import com.kickstarter.factories.LocationFactory;
import com.kickstarter.factories.ProjectFactory;
import com.kickstarter.models.Project;

import junit.framework.TestCase;

import java.util.Locale;

public final class I18nUtilsTest extends TestCase {
  public void testLanguage() {
    assertEquals("en", Locale.US.getLanguage());
    assertEquals("de", Locale.GERMANY.getLanguage());
  }

  public void testIsCountryGermany() {
    assertFalse(I18nUtils.isCountryGermany(LocationFactory.unitedStates().country()));
    assertTrue(I18nUtils.isCountryGermany(LocationFactory.germany().country()));
  }

  public void testIsCountryUS() {
    assertTrue(I18nUtils.isCountryUS(LocationFactory.unitedStates().country()));
    assertFalse(I18nUtils.isCountryUS(LocationFactory.germany().country()));
  }

  public void testNeedsConversion() {
    final Project project = ProjectFactory.project()
      .toBuilder()
      .currentCurrency("MXN")
      .currentCurrencyRate(2.0f)
      .build();

    // todo: move out CurrencyOptions
//    assertTrue(I18nUtils.needsConversion(project, "USD", ));
  }
}
