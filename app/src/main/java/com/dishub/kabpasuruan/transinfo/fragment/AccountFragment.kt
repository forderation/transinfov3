package com.dishub.kabpasuruan.transinfo.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dishub.kabpasuruan.transinfo.BuildConfig
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.activity.RegisterActivity
import com.dishub.kabpasuruan.transinfo.activity.SplashActivity
import com.dishub.kabpasuruan.transinfo.api.ApiClient
import com.dishub.kabpasuruan.transinfo.api.ApiService
import com.dishub.kabpasuruan.transinfo.model.LoginResponse
import com.dishub.kabpasuruan.transinfo.model.UserDetails
import com.dishub.kabpasuruan.transinfo.utils.SupportUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_login.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AccountFragment : Fragment(), View.OnClickListener {
    private var loggedIn: Boolean? = null
    private var preferences: SharedPreferences? = null
    private lateinit var api: ApiService
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        preferences = this.activity?.getSharedPreferences(
            getString(R.string.db_preferences),
            Context.MODE_PRIVATE
        )
        loggedIn = preferences!!.getBoolean(SupportUtil.spIsLoggedIn, false)
        api = ApiClient().getApiClient(BuildConfig.BASE_API)
        return if (loggedIn!!) {
            inflater.inflate(R.layout.fragment_account, container, false)
        } else {
            inflater.inflate(R.layout.fragment_login, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (loggedIn!!) {
            btn_logout.setOnClickListener(this)
            getAccountDetails()
        } else {
            btn_daftar.setOnClickListener(this)
            btn_login.setOnClickListener(this)
        }
    }

    private fun getAccountDetails() {
        val api = ApiClient().getApiClient(BuildConfig.BASE_API)
        val token = preferences?.getString(SupportUtil.spToken, "")
        val request = api.userDetails("Bearer $token")
        request.enqueue(object : Callback<UserDetails> {
            override fun onFailure(call: Call<UserDetails>, t: Throwable) {
                showToast("Koneksi error")
            }

            override fun onResponse(call: Call<UserDetails>, response: Response<UserDetails>) {
                if (response.code() == 200) {
                    val userDetails = response.body() as UserDetails
                    val activated =
                        if (userDetails.activated.toInt() == 1) "Terverifikasi" else "Belum Terverfikasi"
                    ac_edt_address.setText(userDetails.address)
                    ac_edt_contact.setText(userDetails.nomorKontak)
                    ac_edt_email.setText(userDetails.email)
                    ac_edt_status.setText("Akun ${activated}")
                    ac_edt_username.setText(userDetails.name)
                    Picasso.get()
                        .load(BuildConfig.BASE_API + "/public/uploads/" + userDetails.photo)
                        .fit()
                        .centerCrop()
                        .error(R.drawable.login)
                        .into(img_foto_ktp)
                } else {
                    showToast("Error kesalahan : ${response.code()}")
                }
            }
        })
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btn_daftar -> {
                startActivity(Intent(activity, RegisterActivity::class.java))
            }
            btn_login -> {
                postLogin()
            }
            btn_logout -> {
                val editor = preferences!!.edit()
                editor.putString(SupportUtil.spToken, "")
                editor.putBoolean(SupportUtil.spIsLoggedIn, false)
                editor.putBoolean(SupportUtil.spIsVerified, false)
                editor.putInt(SupportUtil.spUserID, -1)
                editor.apply()
                val mStartActivity = Intent(context, SplashActivity::class.java)
                mStartActivity.putExtra(SplashActivity.USER_MESSAGE, "Your account has been logout")
                startActivity(mStartActivity)
                activity?.finish()
            }
        }
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun postLogin() = if (edt_lg_email.text.isEmpty()) {
        edt_lg_email.error = "Email tidak boleh kosong"
    } else if (!edt_lg_email.text.isValidEmail()) {
        edt_lg_email.error = "Email tidak valid"
    } else if (edt_lg_password.text.isEmpty()) {
        edt_lg_password.error = "Kata sandi tidak boleh kosong"
    } else {
        edt_lg_password.hideKeyboard()
        val request: Call<LoginResponse> = api.login(
            edt_lg_email.text.requestBody(),
            edt_lg_password.text.requestBody()
        )
        request.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showToast("Koneksi error")
            }

            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.code() == 200) {
                    val loginResponse = response.body() as LoginResponse
                    val token = loginResponse.message
                    if (token != null) {
                        forwardLogin(token)
                    }
                } else if (response.code() == 401) {
                    showToast("Error code : ${response.code()} , Email atau Kata Sandi salah")
                } else {
                    showToast("Error code: ${response.code()}")
                }
            }
        })
    }

    private fun forwardLogin(token: String) {
        val api = ApiClient().getApiClient(BuildConfig.BASE_API)
        val request = api.userDetails("Bearer $token")
        request.enqueue(object : Callback<UserDetails> {
            override fun onFailure(call: Call<UserDetails>, t: Throwable) {
            }

            override fun onResponse(call: Call<UserDetails>, response: Response<UserDetails>) {
                if (response.code() == 200) {
                    val userDetails = response.body() as UserDetails
                    val activated = userDetails.activated.toInt() == 1
                    val editor = preferences!!.edit()
                    editor.putString(SupportUtil.spToken, token)
                    editor.putBoolean(SupportUtil.spIsLoggedIn, true)
                    editor.putBoolean(SupportUtil.spIsVerified, activated)
                    editor.putInt(SupportUtil.spUserID, userDetails.id)
                    editor.apply()
                    val mStartActivity = Intent(context, SplashActivity::class.java)
                    mStartActivity.putExtra(SplashActivity.USER_MESSAGE, userDetails.name)
                    startActivity(mStartActivity)
                    activity?.finish()
                }
            }
        })
    }

    private fun showToast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }

    private fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this!!).matches()
}

fun CharSequence?.requestBody(): RequestBody =
    RequestBody.create(MediaType.parse("text/plain"), this.toString())
