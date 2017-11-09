package com.kickstarter.libs

import com.kickstarter.libs.utils.NumberUtils
import com.kickstarter.models.Project
import java.math.RoundingMode

class KtKSCurrency(val currentConfig: CurrentConfigType) {

  /**
   * Returns a currency string appropriate to the user's locale and location relative to a project.
   *
   * @param initialValue        Value to display, local to the project's currency.
   * @param project             The project to use to look up currency information.
   * @param omitCurrencyCode If true, hide the currency code, even if that makes the returned value ambiguous.
   *                            This is used when space is constrained and the currency code can be determined elsewhere.
   * @param preferUSD           Attempt to convert a project from it's local currency to USD, if the user is located in
   *                            the US.
   */
  @JvmOverloads
  fun format(initialValue: Float, project: Project, omitCurrencyCode: Boolean = false, preferUSD: Boolean = false,
             roundingMode: RoundingMode = RoundingMode.DOWN): String {

    val currencyOptions: KSCurrency.CurrencyOptions = currencyOptions(initialValue, project, preferUSD)
    val showCurrencyCode = showCurrencyCode(currencyOptions, omitCurrencyCode)

    val numberOptions: NumberOptions = NumberOptions.builder()
      .currencyCode(if (showCurrencyCode) currencyOptions.currencyCode() else "")
      .currencySymbol(currencyOptions.currencySymbol())
      .roundingMode(roundingMode)
      .build()

    return NumberUtils.format(currencyOptions.value(), numberOptions)
  }

  @JvmOverloads
  fun currencyOptions(value: Float, project: Project, preferUSD: Boolean = false): KSCurrency.CurrencyOptions {
    val config: Config = this.currentConfig.config
    val currencyRate: Float? = project.currentCurrencyRate()
    val staticUsdRate: Float? = project.staticUsdRate()

    if (preferUSD && config.countryCode() == "US" && staticUsdRate != null ) {
      return KSCurrency.CurrencyOptions.builder()
        .country("US")
        .currencySymbol("$")
        .currencyCode("")
        .value(value * staticUsdRate)
        .build()
    } else if (currencyRate != null) {
      return KSCurrency.CurrencyOptions.builder()
        .country(project.country())
        .currencySymbol(currencySymbol(project))
        .currencyCode(project.currentCurrency())
        .value(value * currencyRate)
        .build()
    } else {
      return KSCurrency.CurrencyOptions.builder()
        .country(project.country())
        .currencyCode(project.currency())
        .currencySymbol(currencySymbol(project))
        .value(value)
        .build()
    }
  }

  /**
   * Returns the full currency symbol for a country. Special logic is added around prefixing currency symbols
   * with country/currency codes based on a variety of factors.
   *
   * @return The currency symbol that can be used for currency display.
   */
  fun currencySymbol(project: Project): String {
    val config = this.currentConfig.config

    if (!config.currencyNeedsCode(project.currencySymbol())) {
      // Currencies that don't have ambiguous currencies can just use their symbol.
      return project.currencySymbol()
    }

    // todo: use nbsp
    if (project.country() == "US" && config.countryCode() == "US") {
      // US people looking at US projects just get the currency symbol
      return project.currencySymbol()
    } else if (project.country() == "SG") {
      // Singapore projects get a special currency prefix "\(String.nbsp)S\(country.currencySymbol)\(String.nbsp)"
      return String.format(" S%s ", project.currencySymbol())
    } else if (project.currencySymbol() == "kr" || project.currencySymbol() == "Fr") {
      // Kroner projects use the currency code prefix "\(String.nbsp)\(country.currencyCode)\(String.nbsp)"
      return String.format(" %s ", project.currency())
    } else {
      // Everything else uses the country code prefix. "\(String.nbsp)\(country.countryCode)\(country.currencySymbol)\(String.nbsp)"
      return String.format(" %s%s ", project.country(), project.currencySymbol())
    }
  }

  /**
   * Determines whether the currency code should be shown. If the currency is ambiguous (e.g. CAD and USD both use `$`),
   * we show the currency code if the user is not in the US, or the project is not in the US.
   */
  private fun showCurrencyCode(currencyOptions: KSCurrency.CurrencyOptions, excludeCurrencyCode: Boolean): Boolean {
    if (excludeCurrencyCode) {
      return false
    }

    val config = this.currentConfig.config
    val currencyIsDupe = config.currencyNeedsCode(currencyOptions.currencySymbol())
    val userIsUS = config.countryCode() == "US"
    val projectIsUS = currencyOptions.country() == "US"

    return currencyIsDupe && !userIsUS || currencyIsDupe && !projectIsUS
  }
}
