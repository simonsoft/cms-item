package se.simonsoft.cms.item.logging;

import org.slf4j.Logger;

/**
 * Supports capturing of services' logging, for recording or test assertions of logging requirements.
 * <p>
 * Unlike a custom logging handler in slf4j this can be used per method call.
 * Supports only one logger at a time so can not be shared by services or threads.
 * <p>
 * Patterns:
 *  - Pass as argument to methods, captures only the logging from that invocation.
 *  - Pass as dependency to service impl, can be used as extra field for example "loggerUser"
 *    so service can select per log entry if significant or not significant to user.
 * <p>
 * Note that in both patterns the receiver of the capture must immediately set its actual logger.
 * Logging statements shouldn't need to be duplicated, and system logging should be preserved.
 * <p>
 * Note also that location information in log entries will be lost.
 * <p>
 * TODO still just an idea, wanted for pretranslate log
 */
public abstract class LoggerCapture implements Logger {

	private Logger logger;

	public void setLogger(Logger logger) {
		if (hasLogger()) {
			throw new IllegalStateException("Already initialized with logger");
		}
		this.logger = logger;
	}
	
	public boolean hasLogger() {
		return this.logger != null;
	}

}
