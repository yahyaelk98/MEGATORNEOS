package com.megatorneos.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.megatorneos.R
import com.megatorneos.model.ItemMensaje
import com.megatorneos.utils.dateToStringMensajes
import com.megatorneos.utils.showSnackBar
import com.megatorneos.viewmodels.MensajesViewModel
import com.megatorneos.views.adapters.MensajesAdapter
import kotlinx.android.synthetic.main.mensajes_fragment.*

class MensajesFragment : Fragment() {

    companion object {
        fun newInstance(): MensajesFragment = MensajesFragment()
    }

    private lateinit var viewModel: MensajesViewModel
    private val mensajesAdapter = MensajesAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.mensajes_fragment, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mensajes_rv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mensajesAdapter
        }/*.addItemDecoration(HorizontalDividerItemDecoration.Builder(context)
            .margin(dpToPx(72).toInt(),dpToPx(16).toInt())
            .positionInsideItem(true)
            .build())*/

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MensajesViewModel::class.java)

        val listObserver = Observer<MutableList<ItemMensaje>>{
            mensajesAdapter.data = it
        }
        viewModel.listMensajes.observe(viewLifecycleOwner, listObserver)

        mensajes_srl.setOnRefreshListener {
            viewModel.setListMensajes(ArrayList())
            getMessages()
        }
    }

    fun getMessages() {
        val user = FirebaseAuth.getInstance().currentUser
        if(user != null && user.email != null){
            val db = FirebaseFirestore.getInstance()

            db.collection("chats").whereArrayContains("members", user.email!!).get()
                .addOnSuccessListener {
                    if(it.isEmpty){
                        showRecyclerView()
                    }else {
                        val numChats = it.size()
                        for ((num, chat) in it.withIndex()) {
                            val itemMensaje = ItemMensaje(chat.id)
                            val lastMessageSent = chat.getString("lastMessageSent")!!
                            val tourney = chat.getString("tourney")!!

                            db.collection("messages").document(itemMensaje.id)
                                .collection("messages")
                                .document(lastMessageSent).get().addOnSuccessListener {
                                    itemMensaje.ultimoMensaje = it.getString("message")!!
                                    itemMensaje.fechaUltimoMensaje = dateToStringMensajes(it.getDate("messageDate")!!)

                                    db.collection("tourneys").document(tourney).get()
                                        .addOnSuccessListener {
                                            itemMensaje.titulo = it.getString("title")!!

                                            Firebase.storage.reference.child(it.getString("image")!!).downloadUrl
                                                .addOnSuccessListener {
                                                    itemMensaje.image = it
                                                    viewModel.addMensaje(itemMensaje)

                                                    if (num == numChats - 1) {
                                                        showRecyclerView()
                                                    }
                                                }
                                        }
                                }

                        }
                    }
                }.addOnFailureListener {
                    showSnackBar(mensajes_progressBar,"Error al obtener los mensajes")
                    showRecyclerView()
                }
        }
        else openSplashActivity()
    }

    private fun showRecyclerView(){
        mensajes_progressBar.visibility = View.GONE
        mensajes_srl.visibility = View.VISIBLE
        mensajes_srl.isRefreshing = false
    }

    private fun openSplashActivity() {
        val intent = Intent(context, SplashActivity::class.java).apply {
            flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        activity?.finish()
    }
}