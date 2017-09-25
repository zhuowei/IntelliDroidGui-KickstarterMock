package com.kickstarter.libs.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kickstarter.libs.KSCurrency;
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

  /**
   * Returns if a currency needs conversion based on the project's currency rate
   * and the user's currency preferences. Falls back to the config country for currency
   * display if no preference selected.
   */
  public static boolean needsConversion(final @NonNull Project project, final @NonNull String configCountry,
    final @NonNull KSCurrency.CurrencyOptions currencyOptions) {

    final String currentCurrency = project.currentCurrency();
    if (currentCurrency == null) {
      return needsConversionForConfigCountry(project.country(), configCountry);
    } else {
      return currentCurrency.equals(currencyOptions.currencyCode())
        && needsConversionForCurrentCurrency(project.country(), currentCurrency);
    }
  }

  private static boolean needsConversionForConfigCountry(final @NonNull String projectCountry, final @Nullable String configCountry) {
    return "US".equals(configCountry) && !isCountryUS(projectCountry);
  }

  public static boolean needsConversionForCurrentCurrency(final @NonNull String projectCountry, final @NonNull String currentCurrency) {
    return !projectCountry.equals(currentCurrency);
  }
}
