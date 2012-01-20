package se.simonsoft.cms.item.dav;

import java.util.Date;

/**
 * Provides a secret for creating new task folders.
 *
 * Commonly expected to have enough randomness to make
 * it impossible to guess the URL without listing parent folder contents
 * or spying on requests.
 */
public interface TaskSecret {

	/**
	 * @return A name prefix to group the folders, such as an activity name.
	 */
	String getPrefix();
	
	/**
	 * @return The random string that makes the URL secret.
	 */
	String getSecret();
	
	/**
	 * @return The timestamp at creation, normally currentTimeMillis
	 */
	Date getCreation();

	/**
	 * @return The timestamp after which the task folder should be deleted
	 */
	Date getExpiry();
	
}
