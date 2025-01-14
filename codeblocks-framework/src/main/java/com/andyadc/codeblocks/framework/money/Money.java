package com.andyadc.codeblocks.framework.money;


import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public class Money implements Comparable<Money>, Serializable {

	private static final long serialVersionUID = 3961895777343526440L;

	private final BigDecimal amount;
	private final Currency currency;

	/**
	 * @param amount   金额，单位为元
	 * @param currency 币种，非空
	 */
	private Money(BigDecimal amount, Currency currency) {
		if (amount == null) {
			throw new IllegalArgumentException("Amount cannot be null.");
		}
		if (currency == null) {
			throw new IllegalArgumentException("Currency cannot be null.");
		}
		this.amount = amount;
		this.currency = currency;
	}

	/**
	 * 通过传入Currency和BigDecimal金额创建Money实例。
	 * 默认使用RoundingMode.HALF_UP进行四舍五入。
	 *
	 * @param currency 币种
	 * @param amount   金额，单位为元
	 * @return 新的Money实例
	 */
	public static Money of(Currency currency, BigDecimal amount) {
		return of(currency, amount, RoundingMode.HALF_UP);
	}

	/**
	 * 通过传入Currency和BigDecimal金额创建Money实例。
	 * 允许指定RoundingMode进行四舍五入。
	 *
	 * @param currency     币种
	 * @param amount       金额，单位为元
	 * @param roundingMode 四舍五入模式
	 * @return 新的Money实例
	 */
	public static Money of(Currency currency, BigDecimal amount, RoundingMode roundingMode) {
		Objects.requireNonNull(currency, "Currency cannot be null.");
		Objects.requireNonNull(amount, "Amount cannot be null.");
		Objects.requireNonNull(roundingMode, "RoundingMode cannot be null.");

		BigDecimal scaledAmount = amount.setScale(
			currency.getDefaultFractionDigits(),
			roundingMode
		);

		return new Money(scaledAmount, currency);
	}

	/**
	 * 获取金额，单位为元。
	 *
	 * @return 金额
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * 获取最小单位金额，通过Currency.getDefaultFractionDigits()和amount计算。
	 * 例如，人民币1元 = 100分，日元1元 = 1元。
	 *
	 * @return 最小单位金额
	 */
	public BigDecimal getAmountMinorUnit() {
		int fractionDigits = currency.getDefaultFractionDigits();
		return amount.movePointRight(fractionDigits);
	}

	/**
	 * 加法操作，返回新的Money实例。
	 * 仅允许相同币种的加法操作。
	 *
	 * @param other 加数
	 * @return 相加后的Money实例
	 * @throws IllegalArgumentException 如果币种不一致
	 */
	public Money add(Money other) {
		validateSameCurrency(other);
		BigDecimal resultAmount = this.amount.add(other.amount);
		return new Money(resultAmount, this.currency);
	}

	/**
	 * 减法操作，返回新的Money实例。
	 * 仅允许相同币种的减法操作。
	 *
	 * @param other 减数
	 * @return 相减后的Money实例
	 * @throws IllegalArgumentException 如果币种不一致
	 */
	public Money subtract(Money other) {
		validateSameCurrency(other);
		BigDecimal resultAmount = this.amount.subtract(other.amount);
		return new Money(resultAmount, this.currency);
	}

	/**
	 * 乘法操作，使用默认舍入模式（RoundingMode.HALF_UP），返回新的Money实例。
	 *
	 * @param multiplier 乘数
	 * @return 乘法后的Money实例
	 * @throws ArithmeticException      如果需要进行舍入但无法进行
	 * @throws IllegalArgumentException 如果multiplier为null
	 */
	public Money multiply(BigDecimal multiplier) {
		return multiply(multiplier, RoundingMode.HALF_UP);
	}

	/**
	 * 乘法操作，返回新的Money实例。
	 *
	 * @param multiplier   乘数
	 * @param roundingMode 四舍五入模式
	 * @return 乘法后的Money实例
	 * @throws ArithmeticException      如果需要进行舍入但没有指定舍入模式
	 * @throws IllegalArgumentException 如果multiplier或roundingMode为null
	 */
	public Money multiply(BigDecimal multiplier, RoundingMode roundingMode) {
		Objects.requireNonNull(multiplier, "Multiplier cannot be null.");
		Objects.requireNonNull(roundingMode, "RoundingMode cannot be null.");

		BigDecimal resultAmount = this.amount.multiply(multiplier)
			.setScale(currency.getDefaultFractionDigits(), roundingMode);
		return new Money(resultAmount, this.currency);
	}

	/**
	 * 除法操作，返回新的Money实例。
	 *
	 * @param divisor      除数
	 * @param scale        保留的小数位数
	 * @param roundingMode 四舍五入模式
	 * @return 除法后的Money实例
	 * @throws ArithmeticException      如果除数为零或无法精确表示
	 * @throws IllegalArgumentException 如果divisor或roundingMode为null
	 */
	public Money divide(BigDecimal divisor, int scale, RoundingMode roundingMode) {
		Objects.requireNonNull(divisor, "Divisor cannot be null.");
		Objects.requireNonNull(roundingMode, "RoundingMode cannot be null.");
		if (divisor.compareTo(BigDecimal.ZERO) == 0) {
			throw new ArithmeticException("Division by zero.");
		}

		BigDecimal resultAmount = this.amount.divide(divisor, scale, roundingMode)
			.setScale(currency.getDefaultFractionDigits(), roundingMode);
		return new Money(resultAmount, this.currency);
	}

	/**
	 * 校验两个Money对象的币种是否相同。
	 *
	 * @param other 另一个Money对象
	 * @throws IllegalArgumentException 如果币种不一致
	 */
	private void validateSameCurrency(Money other) {
		if (!this.currency.equals(other.currency)) {
			throw new IllegalArgumentException("Currencies do not match.");
		}
	}

	@Override
	public int compareTo(Money money) {
		Objects.requireNonNull(money);
		validateSameCurrency(money);
		return this.amount.compareTo(money.amount);
	}

	/**
	 * 重写equals方法，基于金额和币种判断相等。
	 *
	 * @param o 其他对象
	 * @return 是否相等
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Money money = (Money) o;
		return amount.equals(money.amount) &&
			currency.equals(money.currency);
	}

	/**
	 * 重写hashCode方法，基于金额和币种生成哈希码。
	 *
	 * @return 哈希码
	 */
	@Override
	public int hashCode() {
		return Objects.hash(amount, currency);
	}

	/**
	 * 重写toString方法，格式化输出币种和金额。
	 *
	 * @return 格式化后的字符串
	 */
	@Override
	public String toString() {
		return String.format("%s %s", currency.getCurrencyCode(), amount);
	}

}
