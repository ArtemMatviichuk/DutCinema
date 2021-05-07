package com.dut.cinemaapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dut.cinemaapp.R
import com.dut.cinemaapp.domain.UserLogged
import com.dut.cinemaapp.domain.UserRegisterData
import com.dut.cinemaapp.services.AccountService
import com.dut.cinemaapp.services.UserService
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private var repository: AccountService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        register_login_btn.setOnClickListener {
            this.startActivity(Intent(this, LoginActivity::class.java))
        }

        repository = AccountService.Singleton.getAndInitInstance(this)

        register_btn.setOnClickListener {
            var error = ""

            if (register_name.text.length < 3)
                error += "First name is too short\n"

            if (register_surname.text.length < 3)
                error += "Last name is too short\n"

            if (register_email.text.length < 3)
                error += "Email is too short\n"

            if (register_password.text.length < 4)
                error += "Password is too short\n"

            if (!register_password.text.toString().equals(register_password_repeat.text.toString()))
                error += "Passwords are not equal\n"

            if (error != "") {
                Toast.makeText(this, error.substring(0, error.length - 1), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else
                UserService().register(
                    UserRegisterData(
                        register_name.text.toString(),
                        register_surname.text.toString(),
                        register_password.text.toString(),
                        register_email.text.toString()
                    )
                ).enqueue(object : Callback<UserLogged> {
                    override fun onResponse(
                        call: Call<UserLogged>,
                        response: Response<UserLogged>
                    ) {
                        if (response.isSuccessful) {
                            var user = response.body()
                            repository?.login(
                                user?.email!!,
                                register_password.text.toString()
                            ) {
                                this@RegisterActivity.startActivity(
                                    Intent(
                                        this@RegisterActivity,
                                        MainActivity::class.java
                                    )
                                )
                            }
                        } else
                            Toast.makeText(
                                this@RegisterActivity,
                                "Error: " + response.code().toString(),
                                Toast.LENGTH_LONG
                            ).show()

                    }

                    override fun onFailure(call: Call<UserLogged>, t: Throwable) {
                        Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_LONG).show()
                    }

                })
        }
    }
}