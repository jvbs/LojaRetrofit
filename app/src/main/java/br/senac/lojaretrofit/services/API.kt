package br.senac.lojaretrofit.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object API {
    private const val baseUrl = "https://oficinacordova.azurewebsites.net"
    private const val timeOut = 30L

    private val retrofit: Retrofit
        get(){
            val okHttp = OkHttpClient
                .Builder()
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .build()

            return Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp)
                .build()
        }

    val produto: ProdutoService
        get() {
            return retrofit.create(ProdutoService::class.java)
        }
}