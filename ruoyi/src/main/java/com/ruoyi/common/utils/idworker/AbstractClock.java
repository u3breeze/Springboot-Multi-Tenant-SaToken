package com.ruoyi.common.utils.idworker;

/**
 * 时钟抽象类.
 * @author will
 */
public abstract class AbstractClock {

	/**
	 * 创建系统时钟.
	 * 
	 * @return 系统时钟
	 */
	public static AbstractClock systemClock() {
		return new SystemClock();
	}

	/**
	 * 返回从纪元开始的毫秒数.
	 * 
	 * @return 从纪元开始的毫秒数
	 */
	public abstract long millis();

	private static final class SystemClock extends AbstractClock {

		@Override
		public long millis() {
			return System.currentTimeMillis();
		}
	}
}
