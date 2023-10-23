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

public enum VolumeAdaptionSetting {
    OFF(0, 1.0f),
    LIGHT_REDUCTION(1, 0.5f),
    HEAVY_REDUCTION(2, 0.2f),
    LIGHT_BOOST(3, 1.5f),
    MEDIUM_BOOST(4, 2f),
    HEAVY_BOOST(5, 2.5f);

    private final int value;
    private float adaptionFactor;

    VolumeAdaptionSetting(int value, float adaptionFactor) {
        this.value = value;
        this.adaptionFactor = adaptionFactor;
    }

    public static VolumeAdaptionSetting fromInteger(int value) {
        for (VolumeAdaptionSetting setting : values()) {
            if (setting.value == value) {
                return setting;
            }
        }
        throw new IllegalArgumentException("Cannot map value to VolumeAdaptionSetting: " + value);
    }

    public int toInteger() {
        return value;
    }

    public float getAdaptionFactor() {
        return adaptionFactor;
    }
}
