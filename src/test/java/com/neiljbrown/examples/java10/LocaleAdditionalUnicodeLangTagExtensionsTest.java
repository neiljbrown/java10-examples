/*
 *  Copyright 2014-present the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.neiljbrown.examples.java10;

import org.junit.jupiter.api.Test;

import java.util.Currency;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A JUnit test case providing examples of how {@link java.util.Locale} and 'locale-sensitive' Java APIs (that
 * support i18n by using a supplied Locale object to alter their behaviour e.g. when formatting numbers and date for
 * display) have been enhanced in Java 10  (J10) to support some extra 'language-tag extensions' (locale extensions
 * and overrides) specified by the Unicode standard.
 *
 * <h2>Background (standards soup...)</h2>
 * In Java 7, the {@link java.util.Locale} class was enhanced to support / use the IETF BCP 47 standard for
 * defining / identifying locales using so-called 'language tag' strings. As such, from Java 7 onwards, a Locale object
 * can be built from the components of a BCP 47 language tag which include a language code (e.g. 'en'), country code
 * (aka region e.g. 'UK') and other optional components including a 'variant' and 'extensions'.
 * <p>
 * The 'extension' component of a BCP 47 language tag provides a means for orgs or apps to extend a BCP 47 language tag
 * to create their own (private and public) sub-tags for the purposes of overriding or extending the default behaviour
 * of a locale (for a given language and country/reguion). Each extension is identified by single unique character
 * within the BCP 47 language tag (locale) string e.g. a locale of language 'en', country 'UK' and extension identified
 * by 'u', the language tag local string would be "en-UK-u-...", where the format and meaning of "..." is extension
 * sub-tag specific. For more details see the  {@link java.util.Locale} class Javadoc.
 * <p>
 * The Unicode standard has registered its own BCP 47 language tag extension (identified by the  "u" extension
 * prefix) to support defining its own locale extensions for different types of data, including e.g. calendars,
 * numbers, currencies, etc. As part of adding support for BCP 47, Java 7 also added supported for a subset (only) of
 * the Unicode language tag extensions, including the use of Unicode distributed locale data. For more details
 * see the {@link java.util.Locale} class Javadoc, section 'Unicode locale/language extension'.
 *
 * <h2>Java 10 Support for Additional Unicode locale extensions / data</h2>
 * As of Java 9 (with support having been added in Java 7), Java only provided support for a subset of the Unicode's
 * locale extensions, limited to those for locale-specific calendars and numbers (as identified by the Unicode Locale
 * Data Markup Language (LDML) keywords 'ca' and 'nu' respectively).
 * <p>
 * Java 10 now adds support for the following additional Unicode locale extensions (locale-specific types of data) -
 * <ul>
 * <li>'currency type' (as defined by LDML keyword 'cu') - For example, the {@link java.util.Currency#getInstance(Locale)}
 * method now supports returning a locale-specific instance of Currency if the locale string used to create the
 * supplied Locale instance includes a Unicode language tag extension of 'u-cu-...'</li>
 * <li>'first day of week' date/calendar string (as defined by  LDML keyword 'fw') - For example,
 * {@link java.time.temporal.WeekFields#of(Locale)} now supports returning a locale-specific instance of WeekFields if
 * the locale string used to create the supplied Locale instance includes a Unicode language tag extension of 'u-fw...'.</li>
 * <li>'region override' (as defined by LDML keyword 'rg') (a BCP 47 region is aka as a country) - For example, the
 * {@link java.text.NumberFormat#getInstance(Locale)} method now supports returning a locale-specific instance of
 * NumberFormat if the locale string used to create the supplied Locale instance includes a Unicode language tag
 * extension of 'u-rg-...'</li>
 * <li>'time zone' (as defined by LDML keyword 'tz') - For example, the
 * {@link java.time.format.DateTimeFormatter#localizedBy(Locale)} method now supports returning a locale-specific
 * instance of DateTimeFormatter if the locale string used to create the supplied Locale instance includes a Unicode
 * language tag extension on 'u-tz-...'.</li>
 * </ul>
 *
 * <h2>Further Reading</h2>
 * <a href="http://openjdk.java.net/jeps/314">JEP 314: Additional Unicode Language-Tag Extensions</a> - The original
 * proposal for this enhancement, including goals, non-goals, further description.
 * <p>
 * <a href="https://bugs.openjdk.java.net/browse/JDK-8177568">JEP 314 dev ticket/issue</a> - The comments of this dev
 * ticket contain a little more explanation of the purpose of the enhancement.
 * <p>
 * <a href="https://www.baeldung.com/java-8-localization">Internationalization and Localization in Java 8, Baeldung</a>
 * - A useful primer / reminder of how {@link java.util.Locale} can be used to support i18n in Java apps.
 */
public class LocaleAdditionalUnicodeLangTagExtensionsTest {

  /**
   * Provides an example of Java 10's support for using the Unicode standard's locale-specific currency data, as
   * identified by the Unicode language-tag extension string 'u-cu-{currency-code}'.
   */
  @Test
  public void overrideDefaultCurrencyUsingUnicodeLocaleExtension() {
    // Get the default currency symbol for a given locale (e.g. the German language in Switzerland)
    String currencySymbol = Currency.getInstance(Locale.forLanguageTag("de-CH")).getSymbol();
    assertThat(currencySymbol).isEqualTo("CHF");

    // Use the same locale, but override the default currency symbol with the Unicode standard's definition of the
    // currency symbol for US dollars for a Swiss user/audience
    currencySymbol = Currency.getInstance(Locale.forLanguageTag("de-CH-u-cu-usd")).getSymbol();
    assertThat(currencySymbol).isEqualTo("US$");
  }
}