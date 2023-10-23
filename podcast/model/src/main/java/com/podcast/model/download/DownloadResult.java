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


import androidx.annotation.NonNull;
import com.podcast.model.feed.FeedFile;

import java.util.Date;

/**
 * Contains status attributes for one download
 */
public class DownloadResult {
    /**
     * Downloaders should use this constant for the size attribute if necessary
     * so that the listadapters etc. can react properly.
     */
    public static final int SIZE_UNKNOWN = -1;

    /**
     * A human-readable string which is shown to the user so that he can
     * identify the download. Should be the title of the item/feed/media or the
     * URL if the download has no other title.
     */
    private final String title;
    private final long feedfileId;
    /**
     * Is used to determine the type of the feedfile even if the feedfile does
     * not exist anymore. The value should be FEEDFILETYPE_FEED,
     * FEEDFILETYPE_FEEDIMAGE or FEEDFILETYPE_FEEDMEDIA
     */
    private final int feedfileType;
    /**
     * Unique id for storing the object in database.
     */
    private long id;
    private DownloadError reason;
    /**
     * A message which can be presented to the user to give more information.
     * Should be null if Download was successful.
     */
    private String reasonDetailed;
    private boolean successful;
    private final Date completionDate;

    /**
     * Constructor for creating new completed downloads.
     */
    public DownloadResult(@NonNull FeedFile feedfile, String title, DownloadError reason, boolean successful,
                          String reasonDetailed) {
        this(0, title, feedfile.getId(), feedfile.getTypeAsInt(), successful, reason, new Date(),
                reasonDetailed);
    }

    public DownloadResult(long id, String title, long feedfileId, int feedfileType, boolean successful,
                          DownloadError reason, Date completionDate,
                          String reasonDetailed) {
        this.id = id;
        this.title = title;
        this.feedfileId = feedfileId;
        this.reason = reason;
        this.successful = successful;
        this.completionDate = (Date) completionDate.clone();
        this.reasonDetailed = reasonDetailed;
        this.feedfileType = feedfileType;
    }

    @Override
    @NonNull
    public String toString() {
        return "DownloadStatus [id=" + id + ", title=" + title + ", reason="
                + reason + ", reasonDetailed=" + reasonDetailed
                + ", successful=" + successful + ", completionDate="
                + completionDate + ", feedfileId=" + feedfileId
                + ", feedfileType=" + feedfileType + "]";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public DownloadError getReason() {
        return reason;
    }

    public String getReasonDetailed() {
        return reasonDetailed;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public Date getCompletionDate() {
        return (Date) completionDate.clone();
    }

    public long getFeedfileId() {
        return feedfileId;
    }

    public int getFeedfileType() {
        return feedfileType;
    }

    public void setSuccessful() {
        this.successful = true;
        this.reason = DownloadError.SUCCESS;
    }

    public void setFailed(DownloadError reason, String reasonDetailed) {
        this.successful = false;
        this.reason = reason;
        this.reasonDetailed = reasonDetailed;
    }

    public void setCancelled() {
        this.successful = false;
        this.reason = DownloadError.ERROR_DOWNLOAD_CANCELLED;
    }
}
