package com.dut.cinemaapp.services

import com.dut.cinemaapp.domain.UserLogged
import com.dut.cinemaapp.domain.UserLoginResponse
import com.dut.cinemaapp.domain.UserRegisterData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class UserService {
    fun login(login: String, password: String): Call<UserLoginResponse> {

        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiCaller::class.java)
            .login(mapOf(Pair("email", login), Pair("password", password)))
    }

    fun register(data: UserRegisterData): Call<UserLogged> {

        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiCaller::class.java)
            .register(data)
    }

    fun logout(id: Long, token: String): Call<Void> {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiCaller::class.java)
            .logout(mapOf(Pair("id", id.toString()), Pair("token", token)))
    }

    fun getUserData() : Call<UserLogged> {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiCaller::class.java)
            .getUser(
                AccountService.Singleton.getInstance()?.id!!,
                "Bearer_" + AccountService.Singleton.getInstance()?.token!!
            )

    }

    interface ApiCaller {
        @POST("auth/register")
        fun register(
            @Body data: UserRegisterData
        ): Call<UserLogged>

        @POST("auth/login")
        fun login(
            @Body loginPass: Map<String, String>
        ): Call<UserLoginResponse>

        @POST("auth/logout")
        fun logout(
            @Body idToken: Map<String, String>
        ): Call<Void>

        @GET("users/{id}")
        fun getUser(@Path("id") userId: Long, @Header("Authorization") token: String): Call<UserLogged>
    }
}