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
import java.util.List;
import java.util.Set;

public abstract class CmsCurrentUserBase implements CmsCurrentUser {
    @Override
    public boolean hasRole(Set<String> expectedRoles) {
        Set<String> roles = (this.getUserRoles() == null) ? new HashSet<>() : new HashSet<>(List.of(this.getUserRoles().split(",")));
        if (expectedRoles == null) return false;
        if (expectedRoles.size() == 1 && expectedRoles.contains("*")) return true;
        for (String role : expectedRoles) {
            if (roles.contains(role)) return true;
        }
        return false;
    }
}