package com.podcast.event;

public class FeedUpdateRunningEvent {
    public final boolean isFeedUpdateRunning;

    public FeedUpdateRunningEvent(boolean isRunning) {
        this.isFeedUpdateRunning = isRunning;
    }
}
