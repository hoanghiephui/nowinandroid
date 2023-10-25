package com.podcast.event;


import com.podcast.model.download.DownloadStatus;

import java.util.Map;
import java.util.Set;

public class EpisodeDownloadEvent {
    private final Map<String, DownloadStatus> map;

    public EpisodeDownloadEvent(Map<String, DownloadStatus> map) {
        this.map = map;
    }

    public Set<String> getUrls() {
        return map.keySet();
    }
}
