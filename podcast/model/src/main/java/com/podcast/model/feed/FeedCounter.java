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

public enum FeedCounter {
    SHOW_NEW(1),
    SHOW_UNPLAYED(2),
    SHOW_NONE(3),
    SHOW_DOWNLOADED(4),
    SHOW_DOWNLOADED_UNPLAYED(5);

    public final int id;

    FeedCounter(int id) {
        this.id = id;
    }

    public static FeedCounter fromOrdinal(int id) {
        for (FeedCounter counter : values()) {
            if (counter.id == id) {
                return counter;
            }
        }
        return SHOW_NONE;
    }
}
