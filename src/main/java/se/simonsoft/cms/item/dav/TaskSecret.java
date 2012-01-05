package se.simonsoft.cms.item.dav;

/**
 * Provides a secret for creating new task folders.
 *
 * Commonly expected to have enough randomness to make
 * it impossible to guess the URL without listing parent folder contents
 * or spying on requests.
 */
public interface TaskSecret {

	String getSecret();
	
}
