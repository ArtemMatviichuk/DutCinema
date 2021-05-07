package com.dut.cinemaapp.services

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.dut.cinemaapp.domain.UserLoginResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class AccountService {

    private constructor(context: Context){
        this.context = context

        val prefs: SharedPreferences? = context.getSharedPreferences("userData", MODE_PRIVATE)
        val userJson = prefs?.getString("user", "")

        if (!userJson.equals(""))
            this.userLoginResponse =  Gson().fromJson(userJson, UserLoginResponse::class.java)
        else
            this.userLoginResponse = null
    }

    private var context: Context? = null

    object Singleton {
        @SuppressLint("StaticFieldLeak")
        private var instance: AccountService? = null

        fun getAndInitInstance(context: Context) : AccountService? {
            if (instance?.context == null)
                instance = AccountService(context)

            return instance
        }

        fun getInstance() : AccountService? {
            return instance
        }
    }

    var userLoginResponse: UserLoginResponse? = null
        private set

    val isLoggedIn: Boolean
        get() = userLoginResponse != null

    val token: String?
        get() = userLoginResponse?.token

    val id: Long?
        get() = userLoginResponse?.id

    lateinit var json: String

    fun login(username: String, password: String, onSuccess: () -> Unit) {

        UserService().login(username, password).enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                if (response.isSuccessful) {
                    setLoggedInUser(response.body() as UserLoginResponse)
                    onSuccess.invoke()
                }
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {

            }

        })
    }

    private fun setLoggedInUser(userLoginResponse: UserLoginResponse) {
        this.userLoginResponse = userLoginResponse
        val editor: SharedPreferences.Editor? =
            context?.getSharedPreferences("userData", MODE_PRIVATE)?.edit()
        editor?.putString("user", Gson().toJson(userLoginResponse) )
        editor?.apply()
    }

    fun logout(onSuccess: () -> Unit) {
        UserService().logout(this.id!!, this.token!!).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    userLoginResponse = null
                    val editor: SharedPreferences.Editor? =
                        context?.getSharedPreferences("userData", MODE_PRIVATE)?.edit()
                    editor?.putString("user", null)
                    editor?.apply()
                    onSuccess.invoke()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {

            }
        })
    }
}