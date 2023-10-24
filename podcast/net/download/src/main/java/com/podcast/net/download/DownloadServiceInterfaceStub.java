package com.podcast.net.download;

import android.content.Context;
import com.podcast.model.feed.FeedItem;

public class DownloadServiceInterfaceStub extends DownloadServiceInterface {

    @Override
    public void downloadNow(Context context, FeedItem item, boolean ignoreConstraints) {
    }

    @Override
    public void download(Context context, FeedItem item) {
    }

    @Override
    public void cancel(Context context, String url) {
    }

    @Override
    public void cancelAll(Context context) {
    }
}
