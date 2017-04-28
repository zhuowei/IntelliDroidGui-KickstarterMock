package com.kickstarter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.kickstarter.libs.AndroidPayCapability;
import com.kickstarter.libs.BuildCheck;
import com.kickstarter.libs.Config;
import com.kickstarter.libs.CurrentConfig;
import com.kickstarter.libs.CurrentConfigType;
import com.kickstarter.libs.Environment;
import com.kickstarter.libs.KSCurrency;
import com.kickstarter.libs.KSString;
import com.kickstarter.libs.Koala;
import com.kickstarter.libs.MockCurrentConfig;
import com.kickstarter.libs.MockCurrentUser;
import com.kickstarter.libs.MockSharedPreferences;
import com.kickstarter.libs.MockTrackingClient;
import com.kickstarter.libs.preferences.BooleanPreference;
import com.kickstarter.libs.preferences.MockBooleanPreference;
import com.kickstarter.libs.preferences.MockIntPreference;
import com.kickstarter.libs.utils.ListUtils;
import com.kickstarter.libs.utils.MapUtils;
import com.kickstarter.libs.utils.PlayServicesCapability;
import com.kickstarter.services.MockApiClient;
import com.kickstarter.services.MockWebClient;
import com.kickstarter.services.WebClientType;
import com.kickstarter.viewmodels.DiscoveryViewModel;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;


import org.robolectric.shadows.multidex.ShadowMultiDex;

import java.net.CookieManager;
import java.util.Arrays;
import java.util.List;

import rx.observers.TestSubscriber;
import rx.schedulers.TestScheduler;

@RunWith(KSRobolectricGradleTestRunner.class)
@org.robolectric.annotation.Config(constants = BuildConfig.class, shadows = ShadowMultiDex.class, sdk = KSRobolectricGradleTestRunner.DEFAULT_SDK)
public abstract class KSRobolectricTestCase extends TestCase {
  private TestKSApplication application;
  public TestSubscriber<String> koalaTest;
  private Environment environment;
  final private CurrentConfigType mockCurrentConfig = new MockCurrentConfig();

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    final MockTrackingClient testTrackingClient = new MockTrackingClient();
    koalaTest = new TestSubscriber<>();
    testTrackingClient.eventNames.subscribe(koalaTest);

    final List<Config.LaunchedCountry> launchedCountries = Arrays.asList(
      Config.LaunchedCountry
        .builder()
        .currencyCode("USD")
        .currencySymbol("$")
        .name("US")
        .trailingCode(false)
        .build(),

      Config.LaunchedCountry
        .builder()
        .currencyCode("CAD")
        .currencySymbol("$")
        .name("CA")
        .trailingCode(true)
        .build(),

      Config.LaunchedCountry
        .builder()
        .currencyCode("GBP")
        .currencySymbol("£")
        .name("GB")
        .trailingCode(false)
        .build()
    );

    final Config config = Config
      .builder()
      .countryCode("US")
      .features(MapUtils.empty())
      .launchedCountries(launchedCountries)
      .build();
    this.mockCurrentConfig.config(config);

      environment = application().component().environment().toBuilder()
      .activitySamplePreference(new MockIntPreference())
      .androidPayCapability(new AndroidPayCapability(true))
      .apiClient(new MockApiClient())
      .buildCheck(BuildCheck.DEFAULT)
      .cookieManager(new CookieManager())
      .currentConfig(this.mockCurrentConfig)
      .currentUser(new MockCurrentUser())
      .gson(new Gson())
      .hasSeenAppRatingPreference(new MockBooleanPreference())
      .hasSeenGamesNewsletterPreference(new MockBooleanPreference())
      .koala(new Koala(testTrackingClient))
      .ksCurrency(new KSCurrency(new MockCurrentConfig()))
      .playServicesCapability(new PlayServicesCapability(false))
      .scheduler(new TestScheduler())
      .sharedPreferences(new MockSharedPreferences())
      .webClient(new MockWebClient())
      .build();
  }

  protected @NonNull TestKSApplication application() {
    if (application != null) {
      return application;
    }

    application = (TestKSApplication) RuntimeEnvironment.application;
    return application;
  }

  protected @NonNull Context context() {
    return application().getApplicationContext();
  }

  protected @NonNull Environment environment() {
    return environment;
  }

  protected @NonNull KSString ksString() {
    return new KSString(application().getPackageName(), application().getResources());
  }
}
