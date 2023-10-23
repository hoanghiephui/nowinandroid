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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Provides sort orders to sort a list of episodes.
 */
public enum SortOrder {
    DATE_OLD_NEW(1, Scope.INTRA_FEED),
    DATE_NEW_OLD(2, Scope.INTRA_FEED),
    EPISODE_TITLE_A_Z(3, Scope.INTRA_FEED),
    EPISODE_TITLE_Z_A(4, Scope.INTRA_FEED),
    DURATION_SHORT_LONG(5, Scope.INTRA_FEED),
    DURATION_LONG_SHORT(6, Scope.INTRA_FEED),
    EPISODE_FILENAME_A_Z(7, Scope.INTRA_FEED),
    EPISODE_FILENAME_Z_A(8, Scope.INTRA_FEED),
    SIZE_SMALL_LARGE(9, Scope.INTRA_FEED),
    SIZE_LARGE_SMALL(10, Scope.INTRA_FEED),
    FEED_TITLE_A_Z(101, Scope.INTER_FEED),
    FEED_TITLE_Z_A(102, Scope.INTER_FEED),
    RANDOM(103, Scope.INTER_FEED),
    SMART_SHUFFLE_OLD_NEW(104, Scope.INTER_FEED),
    SMART_SHUFFLE_NEW_OLD(105, Scope.INTER_FEED);

    public enum Scope {
        INTRA_FEED, INTER_FEED
    }

    public final int code;

    @NonNull
    public final Scope scope;

    SortOrder(int code, @NonNull Scope scope) {
        this.code = code;
        this.scope = scope;
    }

    /**
     * Converts the string representation to its enum value. If the string value is unknown,
     * the given default value is returned.
     */
    public static SortOrder parseWithDefault(String value, SortOrder defaultValue) {
        try {
            return valueOf(value);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    @Nullable
    public static SortOrder fromCodeString(@Nullable String codeStr) {
        if (TextUtils.isEmpty(codeStr)) {
            return null;
        }
        int code = Integer.parseInt(codeStr);
        for (SortOrder sortOrder : values()) {
            if (sortOrder.code == code) {
                return sortOrder;
            }
        }
        throw new IllegalArgumentException("Unsupported code: " + code);
    }

    @Nullable
    public static String toCodeString(@Nullable SortOrder sortOrder) {
        return sortOrder != null ? Integer.toString(sortOrder.code) : null;
    }

    public static SortOrder[] valuesOf(String[] stringValues) {
        SortOrder[] values = new SortOrder[stringValues.length];
        for (int i = 0; i < stringValues.length; i++) {
            values[i] = SortOrder.valueOf(stringValues[i]);
        }
        return values;
    }
}
