package com.podcast.net.discovery

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

object PodcastSearcherRegistry {
    @get:Synchronized var searchProviders: MutableList<SearcherInfo> = mutableListOf()
        get() {
            if (field.isEmpty()) {
                field.add(SearcherInfo(CombinedSearcher(), 1.0f))
                //searchProviders.add(new SearcherInfo(new GpodnetPodcastSearcher(), 0.0f));
                //searchProviders.add(new SearcherInfo(new FyydPodcastSearcher(), 1.0f));
                field.add(SearcherInfo(ItunesPodcastSearcher(), 1.0f));
                //searchProviders.add(new SearcherInfo(new PodcastIndexPodcastSearcher(), 1.0f));
            }
            return field
        }
        private set

    fun lookupUrl(url: String): Flow<String> = flow {
        val searchResults = searchProviders.map { searchProviderInfo ->
            if (searchProviderInfo.searcher::class != CombinedSearcher::class
                && searchProviderInfo.searcher.urlNeedsLookup(url)
            ) {
                emitAll(searchProviderInfo.searcher.lookupUrl(url))
            }
        }
        if (searchResults.isEmpty()) {
            emit(url)
        }
    }

    fun urlNeedsLookup(url: String): Boolean {
        for (searchProviderInfo in searchProviders) {
            if (searchProviderInfo.searcher.javaClass != CombinedSearcher::class.java
                && searchProviderInfo.searcher.urlNeedsLookup(url)
            ) {
                return true
            }
        }
        return false
    }

    class SearcherInfo(val searcher: PodcastSearcher, val weight: Float)
}
