package com.mready.myapplication.api

import com.mready.myapplication.BuildConfig
import net.mready.apiclient.ApiClient
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FridgeBuddyApiClient @Inject constructor(
    httpClient: OkHttpClient
) : ApiClient(httpClient = httpClient, baseUrl = BuildConfig.API_HOST)