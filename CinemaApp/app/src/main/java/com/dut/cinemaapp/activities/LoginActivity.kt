package com.dut.cinemaapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dut.cinemaapp.R
import com.dut.cinemaapp.services.AccountService
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.loading_comp.*

class LoginActivity : AppCompatActivity() {

    private lateinit var repository: AccountService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        setLoading(true)

        repository = AccountService.Singleton.getAndInitInstance(this)!!

        if (repository.isLoggedIn)
            moveToMainActivity()
        else{
            setLoading(false)

            register.setOnClickListener {
                this.startActivity(Intent(this, RegisterActivity::class.java))
            }

            login.setOnClickListener {
                repository.login(
                    username.text.toString(),
                    password.text.toString()
                ) { moveToMainActivity() }
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading){
            loading.visibility = View.VISIBLE
            username.visibility = View.GONE
            password.visibility = View.GONE
            login.visibility = View.GONE
            register.visibility = View.GONE
        }
        else {
            loading.visibility = View.GONE
            username.visibility = View.VISIBLE
            password.visibility = View.VISIBLE
            login.visibility = View.VISIBLE
            register.visibility = View.VISIBLE
        }

    }

    private fun moveToMainActivity(){
        this.startActivity(Intent(this, MainActivity::class.java))
        setLoading(false)
    }
}