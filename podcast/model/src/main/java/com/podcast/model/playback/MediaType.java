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

package com.podcast.model.playback;

import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum MediaType {
    AUDIO, VIDEO, UNKNOWN;

    private static final Set<String> AUDIO_APPLICATION_MIME_STRINGS = new HashSet<>(Arrays.asList(
            "application/ogg",
            "application/opus",
            "application/x-flac"
    ));

    public static MediaType fromMimeType(String mimeType) {
        if (TextUtils.isEmpty(mimeType)) {
            return MediaType.UNKNOWN;
        } else if (mimeType.startsWith("audio")) {
            return MediaType.AUDIO;
        } else if (mimeType.startsWith("video")) {
            return MediaType.VIDEO;
        } else if (AUDIO_APPLICATION_MIME_STRINGS.contains(mimeType)) {
            return MediaType.AUDIO;
        }
        return MediaType.UNKNOWN;
    }
}
