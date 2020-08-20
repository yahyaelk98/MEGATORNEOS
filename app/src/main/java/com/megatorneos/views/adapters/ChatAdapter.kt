package com.megatorneos.views.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.megatorneos.R
import com.megatorneos.model.Chat
import com.megatorneos.utils.checkMismoDia
import com.megatorneos.utils.dateToStringMensajes
import com.megatorneos.utils.dateToTimeString
import com.mikhaellopez.circularimageview.CircularImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chat_recycler_view_me.view.*
import kotlinx.android.synthetic.main.chat_recycler_view_other.view.*

class ChatAdapter(var listChats: MutableList<Chat> = ArrayList()) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ITEM_ME = 0
    private val ITEM_OTHER = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == ITEM_ME) {
            val v = inflater.inflate(R.layout.chat_recycler_view_me, parent, false)
            MeViewHolder(v)
        } else {
            val v = inflater.inflate(R.layout.chat_recycler_view_other, parent, false)
            OtherViewHolder(v)
        }
    }

    override fun getItemCount(): Int = listChats.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = listChats[position]
        when (holder) {
            is MeViewHolder -> {
                holder.message.text = chat.message
                //TODO holder.fecha.text = chat.messageDate
                //TODO holder.header.text = chat.messageDate
            }
            is OtherViewHolder -> {
                holder.message.text = chat.message
                holder.fecha.text = dateToTimeString(chat.messageDate!!)
                holder.header.text = dateToStringMensajes(chat.messageDate!!)
                Picasso.get().load(chat.avatar).into(holder.avatar)
                if(position > 0 && !checkMismoDia(chat.messageDate!!, listChats[position - 1].messageDate!!)){
                    holder.header.visibility = View.VISIBLE
                }else{
                    holder.header.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (listChats[position].sentBy) {
            FirebaseAuth.getInstance().currentUser!!.email -> ITEM_ME
            else -> ITEM_OTHER
        }
    }

    inner class MeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var message: TextView = itemView.chat_rv_tv_message_me
        var fecha: TextView = itemView.chat_rv_tv_fecha_me
        var header: TextView = itemView.chat_rv_tv_header_me
    }

    inner class OtherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var message: TextView = itemView.chat_rv_tv_message_other
        var fecha: TextView = itemView.chat_rv_tv_fecha_other
        var header: TextView = itemView.chat_rv_tv_header_other
        var avatar: CircularImageView = itemView.chat_rv_civ_avatar
    }

    fun update(chats: MutableList<Chat>){
        listChats = chats
        notifyDataSetChanged()
    }

}