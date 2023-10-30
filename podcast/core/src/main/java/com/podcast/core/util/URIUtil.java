package com.podcast.core.util;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility methods for dealing with URL encoding.
 */
public class URIUtil {
    private static final String TAG = "URIUtil";

    private URIUtil() {}

    public static URI getURIFromRequestUrl(String source) {
        // try without encoding the URI
        try {
            return new URI(source);
        } catch (URISyntaxException e) {
            Log.d(TAG, "Source is not encoded, encoding now");
        }
        try {
            URL url = new URL(source);
            return new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static final String REGEX_PATTERN_IP_ADDRESS = "([0-9]{1,3}[\\.]){3}[0-9]{1,3}";
    public static boolean wasDownloadBlocked(Throwable throwable) {
        String message = throwable.getMessage();
        if (message != null) {
            Pattern pattern = Pattern.compile(REGEX_PATTERN_IP_ADDRESS);
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                String ip = matcher.group();
                return ip.startsWith("127.") || ip.startsWith("0.");
            }
        }
        if (throwable.getCause() != null) {
            return wasDownloadBlocked(throwable.getCause());
        }
        return false;
    }
}
