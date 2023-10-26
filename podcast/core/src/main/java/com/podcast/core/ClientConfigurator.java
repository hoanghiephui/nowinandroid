package com.podcast.core;

import android.content.Context;
import com.podcast.core.service.download.AntennapodHttpClient;
import com.podcast.net.download.DownloadServiceInterface;
import com.podcast.net.ssl.SslProviderInstaller;
import com.podcast.storage.database.PodDBAdapter;

import java.io.File;

public class ClientConfigurator {
    private static boolean initialized = false;

    public static synchronized void initialize(Context context) {
        if (initialized) {
            return;
        }
        PodDBAdapter.init(context);
        //UserPreferences.init(context);
        //UsageStatistics.init(context);
        //PlaybackPreferences.init(context);
        SslProviderInstaller.install(context);
        //NetworkUtils.init(context);
        //NetworkConnectionChangeHandler.init(context);
        //DownloadServiceInterface.setImpl(new DownloadServiceInterfaceImpl());
        //SynchronizationQueueSink.setServiceStarterImpl(() -> SyncService.sync(context));
        AntennapodHttpClient.setCacheDirectory(new File(context.getCacheDir(), "okhttp"));
        //AntennapodHttpClient.setProxyConfig(UserPreferences.getProxyConfig());
        //SleepTimerPreferences.init(context);
        //NotificationUtils.createChannels(context);
        initialized = true;
    }
}
