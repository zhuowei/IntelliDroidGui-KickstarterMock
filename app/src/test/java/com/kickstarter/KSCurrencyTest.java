package com.kickstarter;

import com.kickstarter.factories.ConfigFactory;
import com.kickstarter.factories.ProjectFactory;
import com.kickstarter.libs.Config;
import com.kickstarter.libs.CurrentConfigType;
import com.kickstarter.libs.KtKSCurrency;
import com.kickstarter.libs.MockCurrentConfig;
import com.kickstarter.models.Project;

import junit.framework.TestCase;

import java.math.RoundingMode;

public class KSCurrencyTest extends TestCase {
  public void testFormatCurrency_withUserInUS() {
    final KtKSCurrency currency = createKSCurrency("US");
    assertEquals("$1,000", currency.format(1000.0f, ProjectFactory.project()));
    assertEquals("CA$ 1,000", currency.format(1000.0f, ProjectFactory.caProject()));
    assertEquals("€1,000", currency.format(1000.0f, ProjectFactory.deProject()));
    assertEquals("DKK 1,000", currency.format(1000.0f, ProjectFactory.dkProject()));
    assertEquals("£1,000", currency.format(1000.0f, ProjectFactory.gbProject()));
    assertEquals("¥1,000", currency.format(1000.0f, ProjectFactory.jpProject()));

    // Currency code should still be shown to provide context for ambiguous currency symbols.
    assertEquals("CA$ 1,000", currency.format(1000.0f, ProjectFactory.caProject(), true));
    assertEquals("CA$ 1,000", currency.format(1000.0f, ProjectFactory.caProject(), false));
  }

  public void testFormatCurrency_withUserInUS_CADPreference() {
    final KtKSCurrency currency = createKSCurrency("US");
    final Project usProject = ProjectFactory.project()
      .toBuilder()
      .currentCurrency("CAD")
      .currentCurrencyRate(1.3f)
      .build();

    final Project caProject = ProjectFactory.caProject().toBuilder()
      .currentCurrency("CAD")
      .currentCurrencyRate(1.3f)
      .build();

    assertEquals("CA$ 1,300", currency.format(1000.0f, usProject));
    assertEquals("CA$ 1,300", currency.format(1000.0f, caProject));
  }

  public void testFormatCurrency_withUserInCA() {
    final KtKSCurrency currency = createKSCurrency("CA");
    assertEquals("US$ 100", currency.format(100.0f, ProjectFactory.project()));
    assertEquals("CA$ 100", currency.format(100.0f, ProjectFactory.caProject()));
    assertEquals("£100", currency.format(100.0f, ProjectFactory.gbProject()));
  }

  public void testFormatCurrency_withUserInES() {
    final KtKSCurrency currency = createKSCurrency("ES");
    assertEquals("1.000 $", currency.format(1000.0f, ProjectFactory.project()));
    assertEquals("1.000 CA$", currency.format(1000.0f, ProjectFactory.caProject()));
    assertEquals("1.000 £", currency.format(1000.0f, ProjectFactory.gbProject()));
    assertEquals("1.000 €", currency.format(1000.0f, ProjectFactory.deProject()));
    assertEquals("1.000 DKK", currency.format(1000.0f, ProjectFactory.dkProject()));
    assertEquals("1.000 ¥", currency.format(1000.0f, ProjectFactory.jpProject()));
  }

  public void testFormatCurrency_withUserInFR() {
    final KtKSCurrency currency = createKSCurrency("FR");
    assertEquals("1 000 $", currency.format(1000.0f, ProjectFactory.project()));
    assertEquals("1 000 CA$", currency.format(1000.0f, ProjectFactory.caProject()));
    assertEquals("1 000 £", currency.format(1000.0f, ProjectFactory.gbProject()));
    assertEquals("1 000 €", currency.format(1000.0f, ProjectFactory.deProject()));
    assertEquals("1 000 DKK", currency.format(1000.0f, ProjectFactory.dkProject()));
    assertEquals("1 000 ¥", currency.format(1000.0f, ProjectFactory.jpProject()));
  }

  public void testFormatCurrency_withUserInJP() {
    final KtKSCurrency currency = createKSCurrency("JP");
    assertEquals("US$ 1000", currency.format(1000.0f, ProjectFactory.project()));
    assertEquals("CA$ 1000", currency.format(1000.0f, ProjectFactory.caProject()));
    assertEquals("£ 1000", currency.format(1000.0f, ProjectFactory.gbProject()));
    assertEquals("€ 1000", currency.format(1000.0f, ProjectFactory.deProject()));
    assertEquals("DKK 1000", currency.format(1000.0f, ProjectFactory.dkProject()));
    assertEquals("¥ 1000", currency.format(1000.0f, ProjectFactory.jpProject()));
  }

  public void testFormatCurrency_withUserInUK() {
    final KtKSCurrency currency = createKSCurrency("UK");
    assertEquals("US$ 100", currency.format(100.0f, ProjectFactory.project()));
    assertEquals("CA$ 100", currency.format(100.0f, ProjectFactory.caProject()));
    assertEquals("£100", currency.format(100.0f, ProjectFactory.gbProject()));
  }

  public void testFormatCurrency_withUserInUnlaunchedCountry() {
    final KtKSCurrency currency = createKSCurrency("XX");
    assertEquals("US$ 100", currency.format(100.0f, ProjectFactory.project()));
    assertEquals("CA$ 100", currency.format(100.0f, ProjectFactory.caProject()));
    assertEquals("£100", currency.format(100.0f, ProjectFactory.gbProject()));
  }

  public void testFormatCurrency_withCurrencyCodeExcluded() {
    final KtKSCurrency currency = createKSCurrency("CA");
    assertEquals("$100", currency.format(100.0f, ProjectFactory.project(), true));
  }

  public void testFormatCurrency_withUserInUSAndUSDPreferred() {
    final KtKSCurrency currency = createKSCurrency("US");
    assertEquals("$150", currency.format(100.0f, ProjectFactory.gbProject(), false, true, RoundingMode.DOWN));
  }

  public void testFormatCurrency_withUserInUKAndUSDPreferred() {
    final KtKSCurrency currency = createKSCurrency("UK");
    assertEquals("£100", currency.format(100.0f, ProjectFactory.gbProject(), false, true, RoundingMode.DOWN));
  }

  public void testFormatCurrency_roundsDown() {
    final KtKSCurrency currency = createKSCurrency("US");
    final Project project = ProjectFactory.project();
    assertEquals("$100", currency.format(100.4f, project));
    assertEquals("$100", currency.format(100.5f, project));
    assertEquals("$101", currency.format(101.5f, project));
    assertEquals("$100", currency.format(100.9f, project));
  }

  private static KtKSCurrency createKSCurrency(final String countryCode) {
    final Config config = ConfigFactory.config().toBuilder()
      .countryCode(countryCode)
      .build();

    final CurrentConfigType currentConfig = new MockCurrentConfig();
    currentConfig.config(config);

    return new KtKSCurrency(currentConfig);
  }
}
