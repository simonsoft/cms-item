/**
 * Copyright (C) 2009-2017 Simonsoft Nordic AB
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
package se.simonsoft.cms.item.info;

import java.util.HashSet;
import java.util.Set;

public abstract class CmsCurrentUserBase implements CmsCurrentUser {
    Set<String> roles;

    public CmsCurrentUserBase(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public String getUserRoles() {
        return String.join(",", this.roles);
    }

    @Override
    public boolean hasRole(Set<String> expectedRoles) {
        if (expectedRoles.size() == 1 && expectedRoles.contains("*")) return true;
        for (String role : expectedRoles) {
            if (this.roles.contains(role)) return true;
        }
        return false;
    }
}
