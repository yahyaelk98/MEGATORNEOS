package com.megatorneos.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.megatorneos.R
import com.megatorneos.utils.showSnackBarClose
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        addWriteListeners()

        auth = FirebaseAuth.getInstance()
    }

    private fun addWriteListeners() {
        login_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) ib_login_email_clear.visibility = View.INVISIBLE
                else ib_login_email_clear.visibility = View.VISIBLE
            }
        })
        login_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) ib_login_password_clear.visibility = View.GONE
                else ib_login_password_clear.visibility = View.VISIBLE
            }
        })
        login_tv_mostrar.setOnClickListener {
            if (login_password.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                login_password.inputType = InputType.TYPE_CLASS_TEXT
                login_tv_mostrar.text = getString(R.string.register_ocultar)
            }else {
                login_password.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                login_tv_mostrar.text = getString(R.string.register_mostrar)
            }
        }
    }

    /** ONCLICK CLEARS */
    fun loginClearCorreo(view: View) = login_email.setText("")
    fun loginClearPassword(view: View) = login_password.setText("")

    /** ONCLICK BUTTONS */
    fun loginOpenRegister(view: View) = startActivity(Intent(this, RegisterActivity::class.java))
    fun loginBtnLogin(view: View){
        showLoading()

        val currentUser = auth.currentUser

        if(currentUser == null){
            val email = login_email.text.toString().trim()
            val password = login_password.text.toString().trim()

            if(checkForm(email, password)){
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        val user = auth.currentUser
                        openMainActivity()
                    }.addOnFailureListener {
                        hideLoading()
                        showSnackBarClose(view, "Datos de inicio de sesión incorrectos. Inténtalo de nuevo.")
                    }
            }else hideLoading()

        }else{
            openMainActivity()
        }
    }

    private fun checkForm(email: String, password: String): Boolean {
        var formCorrecto: Boolean = true

        if (email.isEmpty()) {
            login_email.error = "Campo obligatorio"
            formCorrecto = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            login_email.error = "El correo electrónico no tiene un formato válido"
            formCorrecto = false
        }
        if (password.isEmpty()) {
            login_password.error = "Campo obligatorio"
            formCorrecto = false
        }

        return formCorrecto
    }

    private fun showLoading(){
        login_email.isEnabled = false
        ib_login_email_clear.isEnabled = false
        login_password.isEnabled = false
        login_tv_mostrar.isEnabled = false
        ib_login_password_clear.isEnabled = false
        login_btn_login.isEnabled = false
        login_btn_login_google.isEnabled = false
        login_tv_register.isEnabled = false
        login_loading.visibility = View.VISIBLE
    }
    private fun hideLoading(){
        login_email.isEnabled = true
        ib_login_email_clear.isEnabled = true
        login_password.isEnabled = true
        login_tv_mostrar.isEnabled = true
        ib_login_password_clear.isEnabled = true
        login_btn_login.isEnabled = true
        login_btn_login_google.isEnabled = true
        login_tv_register.isEnabled = true
        login_loading.visibility = View.GONE
    }

    private fun openMainActivity(){
        val intent = Intent(this, MainActivity::class.java).apply {
            flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }
}