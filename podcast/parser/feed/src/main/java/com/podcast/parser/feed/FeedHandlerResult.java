package com.podcast.parser.feed;


import com.podcast.model.feed.Feed;

import java.util.Map;

/**
 * Container for results returned by the Feed parser
 */
public class FeedHandlerResult {

    public final Feed feed;
    public final Map<String, String> alternateFeedUrls;
    public final String redirectUrl;

    public FeedHandlerResult(Feed feed, Map<String, String> alternateFeedUrls, String redirectUrl) {
        this.feed = feed;
        this.alternateFeedUrls = alternateFeedUrls;
        this.redirectUrl = redirectUrl;
    }
}
