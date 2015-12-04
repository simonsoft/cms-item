/**
 * Copyright (C) 2009-2013 Simonsoft Nordic AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.simonsoft.cms.item.impl;

import java.io.Serializable;
import java.util.Date;

import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemLock;

public class CmsItemLockImpl implements CmsItemLock, Serializable {

	private static final long serialVersionUID = 1L;
	
	private CmsItemId itemId;
	private String token;
	private String owner;
	private String comment;
	private Date created;
	private Date expires;
	
	/**
	 * This empty constructor only exist to please kryo de-serialization which needs an no arg constructor
	 * Do not initialize with this constructor
	 */
	@SuppressWarnings("unused")
	private CmsItemLockImpl() {}

	public CmsItemLockImpl(CmsItemId itemId, String token, String owner, String comment, Date created, Date expires) {
		
		this.itemId = itemId;
		this.token = token;
		this.owner = owner;
		this.comment = comment;
		this.created = created == null ? null : new Date(created.getTime()); // Ensure that the object is a standard Java Date instance.
		this.expires = expires == null ? null : new Date(expires.getTime());
				
	}

	@Override
	public CmsItemId getItemId() {
		return itemId;
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
	public String getToken() {

		return token;
	}

	@Override
	public String getOwner() {

		return owner;
	}
	
	// generated, trusting that CmsItemId hashCode is implemented
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((expires == null) ? 0 : expires.hashCode());
		result = prime * result + ((itemId == null) ? 0 : itemId.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	// generated, trusting that CmsItemId hashCode is implemented
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CmsItemLockImpl other = (CmsItemLockImpl) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (expires == null) {
			if (other.expires != null)
				return false;
		} else if (!expires.equals(other.expires))
			return false;
		if (itemId == null) {
			if (other.itemId != null)
				return false;
		} else if (!itemId.equals(other.itemId))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}

}
