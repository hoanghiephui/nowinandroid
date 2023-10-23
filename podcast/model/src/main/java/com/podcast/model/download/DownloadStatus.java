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

package com.podcast.model.download;

public class DownloadStatus {
    public static final int STATE_QUEUED = 0;
    public static final int STATE_COMPLETED = 1; // Both successful and not successful
    public static final int STATE_RUNNING = 2;

    private final int state;
    private final int progress;

    public DownloadStatus(int state, int progress) {
        this.state = state;
        this.progress = progress;
    }

    public int getState() {
        return state;
    }

    public int getProgress() {
        return progress;
    }
}
