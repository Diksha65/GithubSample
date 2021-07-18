package com.project.githubsample.di

import com.google.gson.Gson
import com.project.githubsample.data.*
import com.project.githubsample.utils.fastLazy
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Module {

    private const val BASE_URL = "https://api.github.com/"

    private val okHttpLoggingInterceptor: HttpLoggingInterceptor by fastLazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient: OkHttpClient by fastLazy {
        OkHttpClient
            .Builder()
            .addNetworkInterceptor(okHttpLoggingInterceptor)
            .build()
    }

    val moshi: Moshi by fastLazy {
        Moshi.Builder().build()
    }

    private val moshiConverterFactory: MoshiConverterFactory by fastLazy {
        MoshiConverterFactory.create(moshi)
    }

    private val api: Api by fastLazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(moshiConverterFactory)
            .client(okHttpClient)
            .build()
            .create(Api::class.java)
    }

    private val dataSource: DataSource by fastLazy {
        DataSourceImpl(
            api = api
        )
    }

    val repository: Repository by fastLazy {
        RepositoryImpl(
            dataSource = dataSource
        )
    }
}