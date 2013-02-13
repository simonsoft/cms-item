package se.simonsoft.cms.item.info;

import se.simonsoft.cms.item.CmsItemId;

public class CmsLockQuery {

	private CmsItemId parent;
	private boolean userSame;
	private boolean recursive;
	private String messageGlob;
	
	public CmsItemId getParent() {
		return parent;
	}
	public CmsLockQuery setParent(CmsItemId parent) {
		this.parent = parent;
		return this;
	}
	
	public boolean isUserSame() {
		return userSame;
	}
	public CmsLockQuery setUserSame(boolean userSame) {
		this.userSame = userSame;
		return this;
	}
	
	public boolean isRecursive() {
		return recursive;
	}
	public CmsLockQuery setRecursive(boolean recursive) {
		this.recursive = recursive;
		return this;
	}
	
	public String getMessageGlob() {
		return messageGlob;
	}
	/**
	 * currently only wildcard (asterisk) meaning 0+ arbitrary chars at beginnning OR end of string, wildcard can not be escaped so meaningful strings should not contain wildcard
	 * @param messageGlob 
	 */
	public CmsLockQuery setMessageGlob(String messageGlob) {
		this.messageGlob = messageGlob;
		return this;
	}
	
}
