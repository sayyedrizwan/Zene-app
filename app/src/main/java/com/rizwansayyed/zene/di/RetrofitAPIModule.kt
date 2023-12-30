package com.rizwansayyed.zene.di


import com.rizwansayyed.zene.data.onlinesongs.downloader.SaveFromDownloaderService
import com.rizwansayyed.zene.data.onlinesongs.instagram.InstagramInfoService
import com.rizwansayyed.zene.data.onlinesongs.instagram.SaveFromInstagramService
import com.rizwansayyed.zene.data.onlinesongs.ip.AWSIpJsonService
import com.rizwansayyed.zene.data.onlinesongs.ip.IpJsonService
import com.rizwansayyed.zene.data.onlinesongs.lastfm.LastFMService
import com.rizwansayyed.zene.data.onlinesongs.news.GoogleNewsService
import com.rizwansayyed.zene.data.onlinesongs.pinterest.PinterestAPIService
import com.rizwansayyed.zene.data.onlinesongs.radio.OnlineRadioService
import com.rizwansayyed.zene.data.onlinesongs.soundcloud.SoundCloudApiService
import com.rizwansayyed.zene.data.onlinesongs.spotify.music.SpotifyAPIService
import com.rizwansayyed.zene.data.onlinesongs.spotify.users.SpotifyUsersAPIService
import com.rizwansayyed.zene.data.onlinesongs.youtube.YoutubeAPIService
import com.rizwansayyed.zene.data.onlinesongs.youtube.YoutubeMusicAPIService
import com.rizwansayyed.zene.data.utils.GoogleNewsAPI.GOOGLE_NEWS_BASE_URL
import com.rizwansayyed.zene.data.utils.InstagramAPI.INSTAGRAM_BASE_URL
import com.rizwansayyed.zene.data.utils.IpJsonAPI.IP_AWS_BASE_URL
import com.rizwansayyed.zene.data.utils.IpJsonAPI.IP_BASE_URL
import com.rizwansayyed.zene.data.utils.LastFM
import com.rizwansayyed.zene.data.utils.LastFM.LAST_FM_BASE_URL
import com.rizwansayyed.zene.data.utils.PinterestAPI.PINTEREST_BASE_URL
import com.rizwansayyed.zene.data.utils.SaveFromInstagram.SAVE_FROM_INSTAGRAM_BASE_URL
import com.rizwansayyed.zene.data.utils.SaveFromInstagram.SAVE_FROM_INSTAGRAM_ORIGIN
import com.rizwansayyed.zene.data.utils.SoundCloudAPI.SOUND_CLOUD_BASE_URL
import com.rizwansayyed.zene.data.utils.SpotifyAPI.SPOTIFY_API_BASE_URL
import com.rizwansayyed.zene.data.utils.USER_AGENT
import com.rizwansayyed.zene.data.utils.VideoDownloaderAPI.SAVE_FROM_BASE_URL
import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_BASE_URL
import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_MUSIC_BASE_URL
import com.rizwansayyed.zene.data.utils.gsonBuilder
import com.rizwansayyed.zene.data.utils.moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
object RetrofitAPIModule {

    private var okHttpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    fun retrofitOnlineRadioService(): OnlineRadioService = Retrofit.Builder()
        .baseUrl("https://demo.com").client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder!!))
        .build().create(OnlineRadioService::class.java)


    @Provides
    fun retrofitIpJsonService(): IpJsonService = Retrofit.Builder()
        .baseUrl(IP_BASE_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder!!))
        .build().create(IpJsonService::class.java)


    @Provides
    fun retrofitAwsIpJsonService(): AWSIpJsonService = Retrofit.Builder()
        .baseUrl(IP_AWS_BASE_URL).client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build().create(AWSIpJsonService::class.java)


    @Provides
    fun retrofitSpotifyApiService(): SpotifyAPIService = Retrofit.Builder()
        .baseUrl(SPOTIFY_API_BASE_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder!!))
        .build().create(SpotifyAPIService::class.java)



    @Provides
    fun retrofitSpotifyUsersApiService(): SpotifyUsersAPIService = Retrofit.Builder()
        .baseUrl(SPOTIFY_API_BASE_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder!!))
        .build().create(SpotifyUsersAPIService::class.java)


    @Provides
    fun retrofitInstagramService(): InstagramInfoService {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val chains = chain.request().newBuilder()
                .addHeader("authority", "www.instagram.com")
                .addHeader("user-agent", USER_AGENT)
            chain.proceed(chains.build())
        })
        return Retrofit.Builder()
            .baseUrl(INSTAGRAM_BASE_URL).client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(builder.build())
            .build().create(InstagramInfoService::class.java)
    }


    @Provides
    fun retrofitYoutubeApiService(): YoutubeAPIService {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val chains = chain.request().newBuilder()
                .addHeader("authority", "www.youtube.com")
                .addHeader("cookie", "GPS=1;")
                .addHeader("origin", "https://www.youtube.com")
                .addHeader("x-origin", "https://www.youtube.com")
                .addHeader("user-agent", USER_AGENT)
            chain.proceed(chains.build())
        })

        return Retrofit.Builder()
            .baseUrl(YT_BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder!!))
            .build().create(YoutubeAPIService::class.java)
    }


    @Provides
    fun retrofitSoundCloudService(): SoundCloudApiService {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val chains = chain.request().newBuilder()
                .addHeader("Origin", "https://soundcloud.com")
                .addHeader("Referer", "https://soundcloud.com")
                .addHeader("User-Agent", USER_AGENT)
            chain.proceed(chains.build())
        })

        return Retrofit.Builder()
            .baseUrl(SOUND_CLOUD_BASE_URL).client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build().create(SoundCloudApiService::class.java)
    }


    @Provides
    fun retrofitYoutubeMusicApiService(): YoutubeMusicAPIService {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val chains = chain.request().newBuilder()
                .addHeader("authority", "www.music.youtube.com")
                .addHeader("content-type", "application/json")
                .addHeader("cookie", "GPS=1;")
                .addHeader("origin", "https://music.youtube.com")
                .addHeader("x-origin", "https://music.youtube.com")
                .addHeader("user-agent", USER_AGENT)
            chain.proceed(chains.build())
        })

        return Retrofit.Builder()
            .baseUrl(YT_MUSIC_BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder!!))
            .build().create(YoutubeMusicAPIService::class.java)
    }


    @Provides
    fun retrofitLastFMApiService(): LastFMService {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val chains = chain.request().newBuilder()
                .addHeader("referer", LAST_FM_BASE_URL)
                .addHeader("origin", LAST_FM_BASE_URL)
                .addHeader("user-agent", USER_AGENT)
            chain.proceed(chains.build())
        })

        return Retrofit.Builder()
            .baseUrl(LastFM.LFM_BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder!!))
            .build().create(LastFMService::class.java)
    }


    @Provides
    fun retrofitSaveFromDownloaderService(): SaveFromDownloaderService {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val chains = chain.request().newBuilder()
                .addHeader("referer", SAVE_FROM_BASE_URL)
                .addHeader("origin", SAVE_FROM_BASE_URL)
                .addHeader("user-agent", USER_AGENT)
            chain.proceed(chains.build())
        })

        return Retrofit.Builder()
            .baseUrl(SAVE_FROM_BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder!!))
            .build().create(SaveFromDownloaderService::class.java)
    }


    @Provides
    fun retrofitPinterestService(): PinterestAPIService {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val chains = chain.request().newBuilder()
                .addHeader("origin", PINTEREST_BASE_URL)
                .addHeader("user-agent", USER_AGENT)
            chain.proceed(chains.build())
        })

        return Retrofit.Builder()
            .baseUrl(PINTEREST_BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder!!))
            .build().create(PinterestAPIService::class.java)
    }


    @Provides
    fun retrofitGoogleNewsService(): GoogleNewsService = Retrofit.Builder()
        .baseUrl(GOOGLE_NEWS_BASE_URL).client(okHttpClient)
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .build().create(GoogleNewsService::class.java)

    @Provides
    fun retrofitSaveFromInstagramService(): SaveFromInstagramService {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val chains = chain.request().newBuilder()
                .addHeader("origin", SAVE_FROM_INSTAGRAM_ORIGIN)
                .addHeader("referer", SAVE_FROM_INSTAGRAM_ORIGIN)
                .addHeader("user-agent", USER_AGENT)
            chain.proceed(chains.build())
        })

        return Retrofit.Builder()
            .baseUrl(SAVE_FROM_INSTAGRAM_BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder!!))
            .build().create(SaveFromInstagramService::class.java)
    }

}