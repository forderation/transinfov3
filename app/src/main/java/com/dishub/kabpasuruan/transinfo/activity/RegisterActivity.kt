package com.dishub.kabpasuruan.transinfo.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dishub.kabpasuruan.transinfo.BuildConfig
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.api.ApiClient
import com.dishub.kabpasuruan.transinfo.model.UploadResponse
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RegisterActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks,
    View.OnClickListener {

    companion object {
        const val REQUEST_GALLERY_CODE = 200
        const val READ_REQUEST_CODE = 300
    }

    private var uri: Uri? = null
    private var fileToUpload: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.title = getString(R.string.create_new)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btn_ktp.setOnClickListener(this)
        btn_daftar.setOnClickListener(this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(this, "Permission has been denied", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK) {
            uri = data?.data
            if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                val filePath = getRealPathFromURIPath(uri, this@RegisterActivity)
                if (filePath != null) {
                    val file = File(filePath)
                    val mFile = RequestBody.create(MediaType.parse("image/*"), file)
                    fileToUpload = MultipartBody.Part.createFormData("foto", file.name, mFile)
                    btn_ktp.text = file.name
                } else {
                    btn_ktp.text = getString(R.string.pilih_foto_ktp)
                }
            }
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.read_file),
                READ_REQUEST_CODE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            RegisterActivity::class.java
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (uri != null) {
            val filePath = getRealPathFromURIPath(uri, this@RegisterActivity)
            if (filePath != null) {
                val file = File(filePath)
                val mFile = RequestBody.create(MediaType.parse("image/*"), file)
                fileToUpload = MultipartBody.Part.createFormData("foto", file.name, mFile)
                btn_ktp.text = file.name
            }
            btn_ktp.text = getString(R.string.pilih_foto_ktp)
        }
    }

    private fun getRealPathFromURIPath(contentURI: Uri?, activity: Activity): String? {
        val cursor = activity.contentResolver.query(
            contentURI!!, null, null, null, null
        )
        val path = if (cursor == null) {
            contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(idx)
        }
        cursor?.close()
        return path
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btn_ktp -> {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_GALLERY_CODE)
            }
            btn_daftar -> {
                var isValid = false
                if (edt_username.text.isEmpty()) {
                    edt_username.error = "Nama lengkap tidak boleh kosong"
                } else if (!edt_email.text.isValidEmail()) {
                    edt_email.error = "Email tidak valid"
                } else if (edt_contact.text.isEmpty()) {
                    edt_contact.error = "Kontak tidak boleh kosong"
                } else if (edt_address.text.isEmpty()) {
                    edt_address.error = "Alamat tidak boleh kosong"
                } else if (edt_password.text.isEmpty()) {
                    edt_password.error = "Kata sandi tidak boleh kosong"
                } else if (edt_password.text.toString().length < 7) {
                    edt_password.error = "Kata sandi harus lebih dari 6 karakter"
                } else if (edt_password.text.toString() != edt_password_confirm.text.toString()) {
                    edt_password_confirm.error = "Konfirmasi kata sandi tidak sama"
                } else if (fileToUpload == null) {
                    Toast.makeText(this, "Foto KTP belum di pilih", Toast.LENGTH_SHORT).show()
                } else {
                    isValid = true
                }
                if (isValid) {
                    val api = ApiClient().getApiClient(BuildConfig.BASE_API)
                    val request: Call<UploadResponse> = api.registerAccount(
                        fileToUpload!!,
                        edt_username.text.requestBody(),
                        edt_password.text.requestBody(),
                        edt_email.text.requestBody(),
                        edt_address.text.requestBody(),
                        edt_contact.text.requestBody()
                    )
                    request.enqueue(object : Callback<UploadResponse> {
                        override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Koneksi gagal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<UploadResponse>,
                            response: Response<UploadResponse>
                        ) {
                            if (response.code() == 403) {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "email already registered or photo max size must be < 2MB",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else if (response.code() == 200) {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    response.body()?.message!!,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Log.d("registrasi", response.body().toString())
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Request gagal : ${response.code()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    private fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this!!).matches()

    private fun CharSequence?.requestBody() =
        RequestBody.create(MediaType.parse("text/plain"), this.toString())

}
