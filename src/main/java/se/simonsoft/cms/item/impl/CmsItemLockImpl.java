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

	public CmsItemLockImpl(CmsItemId itemId, String token, String owner, String comment, Date created, Date expires) {
		
		this.itemId = itemId;
		this.token = token;
		this.owner = owner;
		this.comment = comment;
		this.created = created;
		this.expires = expires;
				
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

}
