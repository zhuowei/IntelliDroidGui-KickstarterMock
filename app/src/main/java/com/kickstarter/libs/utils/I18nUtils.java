package com.kickstarter.libs.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kickstarter.libs.Config;
import com.kickstarter.models.Project;

import java.util.Locale;

public final class I18nUtils {
  private I18nUtils() {}

  public static boolean isCountryGermany(final @NonNull String country) {
    return Locale.GERMANY.getCountry().equals(country);
  }

  public static boolean isCountryUS(final @NonNull String country) {
    return Locale.US.getCountry().equals(country);
  }

  /**
   * Gets the language set on the device, or if none is found, just return "en" for english.
   *
   * This value can be changed while an app is running, so the value shouldn't be cached.
   */
  public static @NonNull String language() {
    final String language = Locale.getDefault().getLanguage();
    return language.isEmpty() ? "en" : language;
  }

  private static boolean needsConversion(final @NonNull Project project, final @NonNull String configCountry) {
    final String currentCurrency = project.currentCurrency();

    if (currentCurrency == null) {
      // should not be recursive
      return needsConversion(project, configCountry);
    } else {
      return currentCurrency.equals(configCountry) && needsConversion(project.country(), currentCurrency);
    }
  }

  // Todo: disambiguate this from above
  private static boolean needsConversion(final @NonNull Config.LaunchedCountry launchedCountry, final @Nullable String userCountry) {
    return !userCountry.equals("US") && !launchedCountry.name().equals("US");
  }

  public static boolean needsConversion(final @NonNull String projectCountry, final @NonNull String currentCurrency) {
    return !projectCountry.equals(currentCurrency);
  }
}
