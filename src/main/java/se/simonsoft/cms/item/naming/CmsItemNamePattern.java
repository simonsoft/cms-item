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
package se.simonsoft.cms.item.naming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * Checks if the suggested name is valid with name, folder counter and file counter
 */
public class CmsItemNamePattern {

    private String prefix;
    private static Pattern PREFIX_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{1,}$");
    private static Pattern COUNTER_PATTERN = Pattern.compile("^[#]{2,}$");
    private String counter;
    private Logger logger = LoggerFactory.getLogger(CmsItemNamePattern.class);

    /**
     * @param name must contain at least one char(^[a-zA-Z0-9_]{1,}$) for the name,
     *             one # for the folder counter and
     *             three # for the file counter.
     *             e.g name|folderCounter|fileCounter
     *             funny_name#####: name will be funny_name max folder count is 99 max file count is 999 funny_name07666.jpeg
     */
    public CmsItemNamePattern(String name) {

        if (name == null || name == "") {
            throw new IllegalArgumentException("The name pattern can't be null or empty");
        }

        logger.info("Trying to parse name '{}' as a CmsItemNamePattern, pattern length {} ", name, name.length());

        setPrefixAndCounter(name);
    }

    private void setPrefixAndCounter(String namePattern) {

        int firstHash = namePattern.indexOf("#");
        if (firstHash == -1) {
            throw new IllegalArgumentException("Name pattern must have at least 2 '#' at the end of the pattern: " + namePattern);
        }

        setPrefix(namePattern.substring(0, firstHash));
        setCounter(namePattern.substring(firstHash));

    }

    /**
     *
     * @return returns the name without counters
     */
    public String getPrefix() {
        return prefix;
    }

    private void setPrefix(String prefix) {

        if (!PREFIX_PATTERN.matcher(prefix).matches()) {
            throw new IllegalArgumentException("The name prefix must be alphanumeric and at least one char long");
        }

        this.prefix = prefix;
    }

    private void setCounter(String counter) {

        if (!COUNTER_PATTERN.matcher(counter).matches()) {
            throw new IllegalArgumentException("Counter must match: " + COUNTER_PATTERN);
        }
        this.counter = counter;
    }

    /**
     * @return folder counter which always is the hashes
     */
    public String getCounter() {
        return this.counter;
    }

    /**
     * Converts hashes in counter.
     * @return the counter hashes converted to zeros as an String
     */
    public String getCounterAsZeros () {

        int length = counter.length();
        String asZeros = "";
        for (int i = 0; i < length; i++) {
            asZeros = asZeros + "0";
        }
        return asZeros;
    }

    /**
     * Concat's name, folder counter and file counter as zeros
     * @return String name with counters as zeros
     */
    public String getFullNameWithCountZero() {
        return getPrefix().concat(getCounterAsZeros());
    }

    public boolean isNameMatchingPattern(String name) {
        boolean matches = false;
        if (name.startsWith(prefix) && name.length() == getFullNameWithCountZero().length()) {
            String substring = name.substring(prefix.length());
            int len = substring.length();
            matches = substring.matches("[0-9]{"+len+"}");
        }

        return matches;
    }

}
