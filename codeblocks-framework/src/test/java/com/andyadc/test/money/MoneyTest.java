package com.andyadc.test.money;

import com.andyadc.codeblocks.framework.money.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MoneyTest {

	@Test
	public void testMoney() {
		Money money = Money.of(Currency.getInstance("CNY"), new BigDecimal("9876.32"));
		System.out.println(money);

		Money money2 = Money.of(Currency.getInstance("JPY"), new BigDecimal("123567"));
		assertThrows(IllegalArgumentException.class, () -> money.add(money2));

		System.out.println(money.getAmount());
		System.out.println(money.getAmountMinorUnit());
		System.out.println(money2.getAmount());
		System.out.println(money2.getAmountMinorUnit());
	}

	/**
	 * |+++++++++++++++++++++++++++++|++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	 * |getCurrencyCode()    		 | Returns the currency's ISO 4217 code (e.g., CNY). 	|
	 * |getSymbol()					 | Returns the symbol of the currency (e.g., ￥).		|
	 * |getDefaultFractionDigits() 	 | Returns the default number of decimal digits.		|
	 * |getDisplayName() 			 | Returns the currency's display name.					|
	 * |+++++++++++++++++++++++++++++|++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	 */
	@Test
	public void testCurrency() {
		// Get Currency from ISO code
		Currency usd = Currency.getInstance("USD");
		System.out.println("Currency Code: " + usd.getCurrencyCode());  // Output: USD
		System.out.println("Symbol: " + usd.getSymbol());               // Output: USD

		// Get Currency from Locale
		Currency cny = Currency.getInstance(Locale.CHINA);
		System.out.println("Currency Code: " + cny.getCurrencyCode());  // Output: CNY
		System.out.println("Display Name: " + cny.getDisplayName());  // Output: 人民币
		System.out.println("Symbol: " + cny.getSymbol());  // Output: ￥
		System.out.println("Fraction Digits: " + cny.getDefaultFractionDigits()); // Output: 2

		Currency jpy = Currency.getInstance(Locale.JAPAN);
		System.out.println("Fraction Digits: " + jpy.getDefaultFractionDigits()); // Output: 0
		System.out.println("Currency Code: " + jpy.getCurrencyCode());  // Output: JPY

		// Format Currency
		double amount = 9876.59;
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.CHINA);
		System.out.println(currencyFormatter.format(amount));           // Output: ￥9,876.59
	}

	@Test
	public void testAvailableLocales() {
		for (Locale locale : Locale.getAvailableLocales()) {
			try {
				Currency currency = Currency.getInstance(locale);
				System.out.println(currency.getCurrencyCode() + " - " + currency.getDisplayName());
			} catch (Exception e) {
				// Skip unsupported locales
			}
		}
	}

}
