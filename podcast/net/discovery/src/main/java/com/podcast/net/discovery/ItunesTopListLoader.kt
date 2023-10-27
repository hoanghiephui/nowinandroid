package com.podcast.net.discovery

import android.content.Context
import android.util.Log
import com.podcast.core.R.string
import com.podcast.core.service.download.AntennapodHttpClient
import com.podcast.model.feed.Feed
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request.Builder
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.Locale
import java.util.concurrent.TimeUnit.DAYS

class ItunesTopListLoader(private val context: Context) {
    @Throws(JSONException::class, IOException::class)
    fun loadToplist(
        country: String,
        limit: Int,
        subscribed: List<Feed>,
    ): List<PodcastSearchResult> {
        val client = AntennapodHttpClient.getHttpClient()
        val feedString: String
        var loadCountry = country
        if (COUNTRY_CODE_UNSET == country) {
            loadCountry = Locale.getDefault().country
        }
        feedString = try {
            getTopListFeed(client, loadCountry)
        } catch (e: IOException) {
            if (COUNTRY_CODE_UNSET == country) {
                getTopListFeed(client, "US")
            } else {
                throw e
            }
        } ?: return emptyList()
        return removeSubscribed(parseFeed(feedString), subscribed, limit)
    }

    @Throws(IOException::class)
    private fun getTopListFeed(client: OkHttpClient, country: String): String? {
        val url = ("https://itunes.apple.com/%s/rss/toppodcasts/limit=" + NUM_LOADED
            + "/explicit=true/json")
        Log.d(TAG, "Feed URL " + String.format(url, country))
        val httpReq: Builder = Builder()
            .cacheControl(CacheControl.Builder().maxStale(1, DAYS).build())
            .url(String.format(url, country))
        client.newCall(httpReq.build()).execute().use { response ->
            if (response.isSuccessful) {
                return response.body?.string()
            }
            if (response.code == 400) {
                throw IOException("iTunes does not have data for the selected country.")
            }
            val prefix = context.getString(string.error_msg_prefix)
            throw IOException(prefix + response)
        }
    }

    @Throws(JSONException::class)
    private fun parseFeed(jsonString: String): List<PodcastSearchResult> {
        val result = JSONObject(jsonString)
        val feed: JSONObject
        val entries: JSONArray
        try {
            feed = result.getJSONObject("feed")
            entries = feed.getJSONArray("entry")
        } catch (e: JSONException) {
            return ArrayList()
        }
        val results: MutableList<PodcastSearchResult> = ArrayList()
        for (i in 0 until entries.length()) {
            val json = entries.getJSONObject(i)
            results.add(PodcastSearchResult.fromItunesToplist(json))
        }
        return results
    }

    companion object {
        private const val TAG = "ITunesTopListLoader"
        const val PREF_KEY_COUNTRY_CODE = "country_code"
        const val PREF_KEY_HIDDEN_DISCOVERY_COUNTRY = "hidden_discovery_country"
        const val PREF_KEY_NEEDS_CONFIRM = "needs_confirm"
        const val PREFS = "CountryRegionPrefs"
        const val COUNTRY_CODE_UNSET = "99"
        private const val NUM_LOADED = 25
        private fun removeSubscribed(
            suggestedPodcasts: List<PodcastSearchResult>, subscribedFeeds: List<Feed>, limit: Int,
        ): List<PodcastSearchResult> {
            val subscribedPodcastsSet: MutableSet<String> = HashSet()
            for (subscribedFeed in subscribedFeeds) {
                if (subscribedFeed.title != null && subscribedFeed.author != null) {
                    subscribedPodcastsSet.add(
                        subscribedFeed.title.trim { it <= ' ' } + " - " + subscribedFeed.author
                            .trim { it <= ' ' },
                    )
                }
            }
            val suggestedNotSubscribed: MutableList<PodcastSearchResult> = ArrayList()
            for (suggested in suggestedPodcasts) {
                if (!subscribedPodcastsSet.contains(suggested.title.trim { it <= ' ' })) {
                    suggestedNotSubscribed.add(suggested)
                }
                if (suggestedNotSubscribed.size == limit) {
                    return suggestedNotSubscribed
                }
            }
            return suggestedNotSubscribed
        }
    }
}
