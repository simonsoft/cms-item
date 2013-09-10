package se.simonsoft.cms.item.impl;

import java.io.Serializable;
import java.util.Date;

import se.simonsoft.cms.item.CmsItemLock;

public class CmsItemLockImpl implements CmsItemLock, Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String owner;
	private String comment;
	private Date created;
	private Date expires;

	public CmsItemLockImpl(String id, String owner, String comment, Date created, Date expires) {
		
		this.id = id;
		this.owner = owner;
		this.comment = comment;
		this.created = created;
		this.expires = expires;
				
	}
	
	@Override
	public String getComment() {

		return comment;
	}

	@Override
	public Date getDateCreation() {

		return created;
	}

	@Override
	public Date getDateExpiration() {

		return expires;
	}

	@Override
	public String getID() {

		return id;
	}

	@Override
	public String getOwner() {

		return owner;
	}

}
