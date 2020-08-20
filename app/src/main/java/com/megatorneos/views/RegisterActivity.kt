package com.megatorneos.views

import android.app.ProgressDialog
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.ProgressBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.megatorneos.R
import com.megatorneos.utils.showSnackBarClose
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        addWriteListeners()

        auth = FirebaseAuth.getInstance()
    }

    private fun addWriteListeners() {
        register_nombre_apellidos.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) ib_register_nombre_clear.visibility = View.INVISIBLE
                else ib_register_nombre_clear.visibility = View.VISIBLE
            }
        })
        register_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) ib_register_email_clear.visibility = View.INVISIBLE
                else ib_register_email_clear.visibility = View.VISIBLE
            }
        })
        register_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) ib_register_password_clear.visibility = View.GONE
                else ib_register_password_clear.visibility = View.VISIBLE
            }
        })
        register_tv_mostrar.setOnClickListener {
            if (register_password.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                register_password.inputType = InputType.TYPE_CLASS_TEXT
                register_tv_mostrar.text = getString(R.string.register_ocultar)
            } else {
                register_password.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                register_tv_mostrar.text = getString(R.string.register_mostrar)
            }
        }
    }

    /** ONCLICK CLEARS */
    fun registerClearNombre(view: View) = register_nombre_apellidos.setText("")
    fun registerClearCorreo(view: View) = register_email.setText("")
    fun registerClearPassword(view: View) = register_password.setText("")

    /** ONCLICK BUTTONS */
    fun registerOpenLogin(view: View) = startActivity(Intent(this, LoginActivity::class.java))
    fun registerBtnRegister(view: View) {
        showLoading()
        val currentUser = auth.currentUser

        if (currentUser == null) {
            val nombre = register_nombre_apellidos.text.toString().trim()
            val email = register_email.text.toString().trim()
            val password = register_password.text.toString().trim()

            if(checkForm(nombre, email, password)){
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        val user = auth.currentUser
                        user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(nombre).build())
                            ?.addOnCompleteListener {
                                openMainActivity()
                            }
                    }.addOnFailureListener {
                        hideLoading()
                    }
            }else hideLoading()

        } else {
            openMainActivity()
        }
    }


    private fun checkForm(nombre: String, email: String, password: String): Boolean {
        var formCorrecto: Boolean = true

        if (nombre.isEmpty()) {
            register_nombre_apellidos.error = "Campo obligatorio"
            formCorrecto = false
        } else if (nombre.length < 3) {
            register_nombre_apellidos.error = "El nombre debe tener al menos 3 carácteres"
            formCorrecto = false
        }
        if (email.isEmpty()) {
            register_email.error = "Campo obligatorio"
            formCorrecto = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            register_email.error = "El correo electrónico no tiene un formato válido"
            formCorrecto = false
        }
        if (password.isEmpty()) {
            register_password.error = "Campo obligatorio"
            formCorrecto = false
        } else if (password.length < 6) {
            register_password.error = "La contraseña debe tener al menos 6 dígitos"
            formCorrecto = false
        }

        return formCorrecto
    }

    private fun showLoading(){
        register_nombre_apellidos.isEnabled = false
        ib_register_nombre_clear.isEnabled = false
        register_email.isEnabled = false
        ib_register_email_clear.isEnabled = false
        register_password.isEnabled = false
        register_tv_mostrar.isEnabled = false
        ib_register_password_clear.isEnabled = false
        register_btn_register.isEnabled = false
        register_btn_login_google.isEnabled = false
        register_tv_login.isEnabled = false
        register_loading.visibility = View.VISIBLE
    }
    private fun hideLoading(){
        register_nombre_apellidos.isEnabled = true
        ib_register_nombre_clear.isEnabled = true
        register_email.isEnabled = true
        ib_register_email_clear.isEnabled = true
        register_password.isEnabled = true
        register_tv_mostrar.isEnabled = true
        ib_register_password_clear.isEnabled = true
        register_btn_register.isEnabled = true
        register_btn_login_google.isEnabled = true
        register_tv_login.isEnabled = true
        register_loading.visibility = View.GONE
    }

    private fun openMainActivity(){
        val intent = Intent(this, MainActivity::class.java).apply {
            flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }
}