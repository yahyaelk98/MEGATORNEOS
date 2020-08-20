package com.megatorneos.views

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.megatorneos.R
import com.megatorneos.model.Chat
import com.megatorneos.model.ItemMensaje
import com.megatorneos.utils.dateToStringMensajes
import com.megatorneos.utils.pxToDp
import com.megatorneos.viewmodels.ChatViewModel
import com.megatorneos.views.adapters.ChatAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    private lateinit var viewModel: ChatViewModel
    private lateinit var chatId: String

    private val chatAdapter = ChatAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        addObservers()

        chatId = intent.getStringExtra("chatId")

        chat_rv_chats.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }

        getChat()
    }

    /* OBTENCIÓN DE DATOS DEL CHAT */
    private fun getChat(){
        val user = FirebaseAuth.getInstance().currentUser

        if(user!!.email != null && chatId.isNotEmpty()){
            val db = FirebaseFirestore.getInstance()

            db.collection("chats").document(chatId).get()
                .addOnSuccessListener {
                    val tourney = it.getString("tourney")!!

                    db.collection("tourneys").document(tourney).get().addOnSuccessListener {
                        viewModel.titulo.value = it.getString("title")
                        viewModel.fecha.value = dateToStringMensajes(it.getDate("dateStart")!!)
                        Firebase.storage.reference.child(it.getString("image")!!).downloadUrl
                            .addOnSuccessListener {
                                viewModel.image.value = it

                                db.collection("messages").document(chatId).collection("messages").orderBy("messageDate", Query.Direction.DESCENDING).get().addOnSuccessListener {
                                    val numChats = it.size()
                                    for ((num, chat) in it.withIndex()) {
                                        val itemChat = Chat(chatId)
                                        itemChat.message = chat.getString("message")!!
                                        itemChat.messageDate = chat.getDate("messageDate")
                                        itemChat.sentBy = chat.getString("sentBy")!!

                                        if(itemChat.sentBy != user.email){
                                            db.collection("users").document(itemChat.sentBy).get().addOnSuccessListener {
                                                Firebase.storage.reference.child(it.getString("image")!!).downloadUrl
                                                    .addOnSuccessListener {
                                                        itemChat.avatar = it
                                                        viewModel.addChat(itemChat)
                                                        if (num == numChats - 1) showScreen()
                                                    }
                                            }
                                        }else{
                                            viewModel.addChat(itemChat)
                                            if (num == numChats - 1) showScreen()
                                        }
                                    }
                                }
                            }
                    }
                }.addOnFailureListener {
                    finish()
                }
        }else openSplashActivity()
    }

    private fun addObservers(){
        /*val tituloObserver = Observer<String>{
            chat_tv_titulo.text = it
        }*/
        viewModel.titulo.observe(this, Observer<String>{
            chat_tv_titulo.text = it
        })
        viewModel.fecha.observe(this, Observer<String>{
            chat_tv_fecha.text = it
        })
        viewModel.image.observe(this, Observer<Uri>{
            Picasso.get().load(it!!).into(chat_riv_image)
        })
        viewModel.listChats.observe(this, Observer<MutableList<Chat>>{
            chatAdapter.update(it)
        })
    }

    private fun showScreen(){
        chat_progressBar.visibility = View.GONE
        chat_layout.visibility = View.VISIBLE
    }

    fun chatBtnBack(view: View) = finish()

    private fun openSplashActivity() {
        val intent = Intent(applicationContext, SplashActivity::class.java).apply {
            flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }
}