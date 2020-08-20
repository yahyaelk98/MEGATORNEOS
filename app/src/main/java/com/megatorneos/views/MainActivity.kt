package com.megatorneos.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.megatorneos.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val principalFragment = PrincipalFragment.newInstance()
    private val favoritosFragment = FavoritosFragment.newInstance()
    private val mensajesFragment = MensajesFragment.newInstance()
    private var active: Fragment = principalFragment

    private var firstMensajes = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.main_container, favoritosFragment, "favoritos").hide(favoritosFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.main_container, mensajesFragment, "mensajes").hide(mensajesFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.main_container, principalFragment, "principal").commit()

        main_bnv.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.main_bnv_principal -> {
                    supportFragmentManager.beginTransaction().hide(active).show(principalFragment).commit()
                    active = principalFragment
                    true
                }
                R.id.main_bnv_favoritos -> {
                    supportFragmentManager.beginTransaction().hide(active).show(favoritosFragment).commit()
                    active = favoritosFragment
                    true
                }
                R.id.main_bnv_nuevo_torneo -> {
                    true
                }
                R.id.main_bnv_mensajes -> {
                    supportFragmentManager.beginTransaction().hide(active).show(mensajesFragment).commit()
                    active = mensajesFragment
                    if(firstMensajes) {
                        mensajesFragment.getMessages()
                        firstMensajes = false
                    }
                    true
                }
                R.id.main_bnv_perfil -> {
                    true
                }
                else -> false
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) openSplashActivity()
        else {//TODO
        }
    }

    private fun openSplashActivity() {
        val intent = Intent(this, SplashActivity::class.java).apply {
            flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }
}