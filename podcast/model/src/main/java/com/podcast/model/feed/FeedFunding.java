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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class FeedFunding {
    public static final String FUNDING_ENTRIES_SEPARATOR = "\u001e";
    public static final String FUNDING_TITLE_SEPARATOR = "\u001f";

    public String url;
    public String content;

    public FeedFunding(String url, String content) {
        this.url = url;
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(this.getClass())) {
            return false;
        }

        FeedFunding funding = (FeedFunding) obj;
        if (url == null && funding.url == null && content == null && funding.content == null) {
            return true;
        }
        if (url != null && url.equals(funding.url) && content != null && content.equals(funding.content)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (url + FUNDING_TITLE_SEPARATOR + content).hashCode();
    }

    public static ArrayList<FeedFunding> extractPaymentLinks(String payLinks) {
        if (StringUtils.isBlank(payLinks)) {
            return null;
        }
        // old format before we started with PodcastIndex funding tag
        ArrayList<FeedFunding> funding = new ArrayList<FeedFunding>();
        if (!payLinks.contains(FeedFunding.FUNDING_ENTRIES_SEPARATOR)
                && !payLinks.contains(FeedFunding.FUNDING_TITLE_SEPARATOR)) {
            funding.add(new FeedFunding(payLinks, ""));
            return funding;
        }
        String [] list = payLinks.split(FeedFunding.FUNDING_ENTRIES_SEPARATOR);
        if (list.length == 0) {
            return null;
        }

        for (String str : list) {
            String [] linkContent = str.split(FeedFunding.FUNDING_TITLE_SEPARATOR);
            if (StringUtils.isBlank(linkContent[0])) {
                continue;
            }
            String url = linkContent[0];
            String title = "";
            if (linkContent.length > 1 && ! StringUtils.isBlank(linkContent[1])) {
                title = linkContent[1];
            }
            funding.add(new FeedFunding(url, title));
        }
        return funding;
    }

    public static String getPaymentLinksAsString(ArrayList<FeedFunding> fundingList) {
        StringBuilder result = new StringBuilder();
        if (fundingList == null) {
            return null;
        }
        for (FeedFunding fund : fundingList) {
            result.append(fund.url).append(FeedFunding.FUNDING_TITLE_SEPARATOR).append(fund.content);
            result.append(FeedFunding.FUNDING_ENTRIES_SEPARATOR);
        }
        return StringUtils.removeEnd(result.toString(), FeedFunding.FUNDING_ENTRIES_SEPARATOR);
    }

}
