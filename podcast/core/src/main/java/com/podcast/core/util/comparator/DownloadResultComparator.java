package com.podcast.core.util.comparator;


import com.podcast.model.download.DownloadResult;

import java.util.Comparator;

/** Compares the completion date of two DownloadResult objects. */
public class DownloadResultComparator implements Comparator<DownloadResult> {

    @Override
    public int compare(DownloadResult lhs, DownloadResult rhs) {
        return rhs.getCompletionDate().compareTo(lhs.getCompletionDate());
    }
}
