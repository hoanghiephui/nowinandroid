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

import android.text.TextUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class FeedItemFilter implements Serializable {

    private final String[] properties;

    public final boolean showPlayed;
    public final boolean showUnplayed;
    public final boolean showPaused;
    public final boolean showNotPaused;
    public final boolean showNew;
    public final boolean showQueued;
    public final boolean showNotQueued;
    public final boolean showDownloaded;
    public final boolean showNotDownloaded;
    public final boolean showHasMedia;
    public final boolean showNoMedia;
    public final boolean showIsFavorite;
    public final boolean showNotFavorite;

    public static final String PLAYED = "played";
    public static final String UNPLAYED = "unplayed";
    public static final String NEW = "new";
    public static final String PAUSED = "paused";
    public static final String NOT_PAUSED = "not_paused";
    public static final String IS_FAVORITE = "is_favorite";
    public static final String NOT_FAVORITE = "not_favorite";
    public static final String HAS_MEDIA = "has_media";
    public static final String NO_MEDIA = "no_media";
    public static final String QUEUED = "queued";
    public static final String NOT_QUEUED = "not_queued";
    public static final String DOWNLOADED = "downloaded";
    public static final String NOT_DOWNLOADED = "not_downloaded";

    public static FeedItemFilter unfiltered() {
        return new FeedItemFilter("");
    }

    public FeedItemFilter(String properties) {
        this(TextUtils.split(properties, ","));
    }

    public FeedItemFilter(String... properties) {
        this.properties = properties;

        // see R.arrays.feed_filter_values
        showUnplayed = hasProperty(UNPLAYED);
        showPaused = hasProperty(PAUSED);
        showNotPaused = hasProperty(NOT_PAUSED);
        showPlayed = hasProperty(PLAYED);
        showQueued = hasProperty(QUEUED);
        showNotQueued = hasProperty(NOT_QUEUED);
        showDownloaded = hasProperty(DOWNLOADED);
        showNotDownloaded = hasProperty(NOT_DOWNLOADED);
        showHasMedia = hasProperty(HAS_MEDIA);
        showNoMedia = hasProperty(NO_MEDIA);
        showIsFavorite = hasProperty(IS_FAVORITE);
        showNotFavorite = hasProperty(NOT_FAVORITE);
        showNew = hasProperty(NEW);
    }

    private boolean hasProperty(String property) {
        return Arrays.asList(properties).contains(property);
    }

    public String[] getValues() {
        return properties.clone();
    }

    public List<String> getValuesList() {
        return Arrays.asList(properties);
    }

    public boolean matches(FeedItem item) {
        if (showNew && !item.isNew()) {
            return false;
        } else if (showPlayed && !item.isPlayed()) {
            return false;
        } else if (showUnplayed && item.isPlayed()) {
            return false;
        } else if (showPaused && !item.isInProgress()) {
            return false;
        } else if (showNotPaused && item.isInProgress()) {
            return false;
        } else if (showNew && !item.isNew()) {
            return false;
        } else if (showQueued && !item.isTagged(FeedItem.TAG_QUEUE)) {
            return false;
        } else if (showNotQueued && item.isTagged(FeedItem.TAG_QUEUE)) {
            return false;
        } else if (showDownloaded && !item.isDownloaded()) {
            return false;
        } else if (showNotDownloaded && item.isDownloaded()) {
            return false;
        } else if (showHasMedia && !item.hasMedia()) {
            return false;
        } else if (showNoMedia && item.hasMedia()) {
            return false;
        } else if (showIsFavorite && !item.isTagged(FeedItem.TAG_FAVORITE)) {
            return false;
        } else if (showNotFavorite && item.isTagged(FeedItem.TAG_FAVORITE)) {
            return false;
        }
        return true;
    }
}
