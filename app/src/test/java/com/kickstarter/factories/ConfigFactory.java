package com.kickstarter.factories;

import android.support.annotation.NonNull;

import com.kickstarter.libs.Config;

import java.util.Arrays;
import java.util.Collections;

public final class ConfigFactory {
  private ConfigFactory() {}

  public static @NonNull Config config() {
    final Config.LaunchedCountry AU = Config.LaunchedCountry.builder()
      .name("AU")
      .currencyCode("AUD")
      .currencySymbol("$")
      .trailingCode(true)
      .build();

    final Config.LaunchedCountry AT = Config.LaunchedCountry.builder()
      .name("AT")
      .currencyCode("EUR")
      .currencySymbol("€")
      .trailingCode(false)
      .build();

    final Config.LaunchedCountry CA = Config.LaunchedCountry.builder()
      .name("CA")
      .currencyCode("CAD")
      .currencySymbol("$")
      .trailingCode(true)
      .build();

    final Config.LaunchedCountry DE = Config.LaunchedCountry.builder()
      .name("DE")
      .currencyCode("EUR")
      .currencySymbol("€")
      .trailingCode(false)
      .build();

    final Config.LaunchedCountry DK = Config.LaunchedCountry.builder()
      .name("DK")
      .currencyCode("DKK")
      .currencySymbol("kr")
      .trailingCode(true)
      .build();

    final Config.LaunchedCountry ES = Config.LaunchedCountry.builder()
      .name("ES")
      .currencyCode("EUR")
      .currencySymbol("€")
      .trailingCode(false)
      .build();

    final Config.LaunchedCountry FR = Config.LaunchedCountry.builder()
      .name("FR")
      .currencyCode("EUR")
      .currencySymbol("€")
      .trailingCode(false)
      .build();

    final Config.LaunchedCountry GB = Config.LaunchedCountry.builder()
      .name("GB")
      .currencyCode("GBP")
      .currencySymbol("£")
      .trailingCode(false)
      .build();

    final Config.LaunchedCountry JP = Config.LaunchedCountry.builder()
      .name("JP")
      .currencyCode("JPY")
      .currencySymbol("¥")
      .trailingCode(false)
      .build();

    final Config.LaunchedCountry MX = Config.LaunchedCountry.builder()
      .name("MX")
      .currencyCode("MXN")
      .currencySymbol("$")
      .trailingCode(true)
      .build();

    final Config.LaunchedCountry US = Config.LaunchedCountry.builder()
      .name("US")
      .currencyCode("USD")
      .currencySymbol("$")
      .trailingCode(true)
      .build();

    return Config.builder()
      .countryCode("US")
      .features(Collections.emptyMap())
      .launchedCountries(Arrays.asList(AU, AT, CA, DE, DK, ES, FR, GB, JP, MX, US))
      .build();
  }

  public static @NonNull Config configForMXUser() {
    return config().toBuilder()
      .countryCode("MX")
      .build();
  }

  public static @NonNull Config configForUSUser() {
    return config();
  }

  public static @NonNull Config configForCAUser() {
    return config().toBuilder()
      .countryCode("CA")
      .build();
  }
}
