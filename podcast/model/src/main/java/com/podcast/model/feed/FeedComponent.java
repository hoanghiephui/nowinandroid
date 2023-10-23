/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.podcast.model.feed;

/**
 * Represents every possible component of a feed
 *
 * @author daniel
 */
public abstract class FeedComponent {

    long id;

    FeedComponent() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Update this FeedComponent's attributes with the attributes from another
     * FeedComponent. This method should only update attributes which where read from
     * the feed.
     */
    void updateFromOther(FeedComponent other) {
    }

    /**
     * Compare's this FeedComponent's attribute values with another FeedComponent's
     * attribute values. This method will only compare attributes which were
     * read from the feed.
     *
     * @return true if attribute values are different, false otherwise
     */
    boolean compareWithOther(FeedComponent other) {
        return false;
    }


    /**
     * Should return a non-null, human-readable String so that the item can be
     * identified by the user. Can be title, download-url, etc.
     */
    public abstract String getHumanReadableIdentifier();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FeedComponent)) return false;

        FeedComponent that = (FeedComponent) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
