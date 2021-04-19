package com.andyadc.codeblocks.test.calc;

/**
 * 复利率(compound rate)
 * F（未来的资金量）=P（本金）*（1+i（利率））^n（投资年限，当然也可能不以年作为时间单位，视具体情况而定）
 * <p>
 * 比如，银行有一款理财产品，年化收益率为3%，计息期为1年，你买了一万，
 * 并持有了两年那么第一年的利息收入为：10000*3%=300第二年的利息收入为：（10000+300）*3%=309元
 * 第二年多出来的9块钱就是你第一年的利息在第二年所创造的利息收入。
 * 第二年的本金和利息总数为：10000*（1+3%）*（1+3%）=10609元
 * 【计息期：简单来说就是多久算一次利息】在上述条件不变的情况下，计息期改为半年一次，
 * 那么第一年的实际利率就为：（1+3%/2）*（1+3%/2）-1=3.0225%那么第一年你的利息收入就是：10000*3.0225%=302.25元
 * 第二年你的利息收入就是：（10000+302.25）*3.0225%=311.38元复利模式，
 * 就是把你的利息也归入本金中来计算利息，计息期决定了多久期限归入一次
 * <p>
 * 作者：巴果财经
 * 链接：https://www.zhihu.com/question/318719474/answer/1677218944
 * 来源：知乎
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 * </p>
 */
public class InterestCalc {

	// P (1 + R/n) (nt) - P
	public static void main(String[] args) {

		System.out.println(Math.pow(2d, 3d));
		double rate = Math.pow((1d + 0.05d / 1d), 10d);
		System.out.println(1000000d * rate);


		// https://geek-docs.com/java/java-examples/calculate-compound-interest.html
//		double rate1 = Math.pow((1d + 0.08d / 12d), 60);
//		System.out.println(rate1);
//		System.out.println(rate1 * 2000d);
//		System.out.println(rate1 * 5d);
//		System.out.println(rate1 * 5d * 2000d);

		System.out.println("------------");
		calculate(1000000, 10, 0.035d, 1);
		// 12-418344.82228688314
		// 1-410598.7606211209

		System.out.println(calcCompoundRate(1000000d, 0.035d, 10, 1d));
		System.out.println(calcCompoundRate(1000000d, 0.035d, 10, 10.43d));
	}

	/**
	 * 复利计算
	 *
	 * @param principal 本金
	 * @param rate      年利率
	 * @param year      时间(年)
	 * @param times     每年复利次数
	 */
	private static double calcCompoundRate(double principal, double rate, int year, double times) {
		return principal * Math.pow(1d + (rate / times), year * times);
	}

	public static void calculate(int p, int t, double r, int n) {
		double amount = p * Math.pow(1d + (r / n), n * t);
		double cinterest = amount - p;
		System.out.println("Compound Interest after " + t + " years: " + cinterest);
		System.out.println("Amount after " + t + " years: " + amount);
	}
}
