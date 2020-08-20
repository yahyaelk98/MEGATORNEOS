package com.megatorneos.views.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.megatorneos.R
import com.megatorneos.model.ItemMensaje
import com.megatorneos.views.ChatActivity
import com.megatorneos.views.LoginActivity
import com.megatorneos.views.SplashActivity
import com.mikhaellopez.circularimageview.CircularImageView
import com.mikhaellopez.hfrecyclerviewkotlin.HFRecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.mensajes_recycler_view.view.*

class MensajesAdapter() : HFRecyclerView<ItemMensaje>(true, false) {

    override fun getFooterView(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder? = null

    override fun getHeaderView(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder? =
        ViewHolder.HeaderViewHolder(inflater.inflate(R.layout.mensajes_recycler_view_header, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder.ItemViewHolder) holder.bind(getItem(position))
    }

    override fun getItemView(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder.ItemViewHolder(inflater.inflate(R.layout.mensajes_recycler_view, parent, false))

    sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        class ItemViewHolder(view: View) : ViewHolder(view){
            private var avatar: CircularImageView? = null
            private var titulo: TextView? = null
            private var ultimo: TextView? = null
            private var ultimoFecha: TextView? = null

            init {
                avatar = itemView.mensajes_rv_civ
                titulo = itemView.mensajes_rv_tv_titulo
                ultimo = itemView.mensajes_rv_tv_ultimo
                ultimoFecha = itemView.mensajes_rv_tv_ultimo_fecha
            }

            fun bind(itemMensaje: ItemMensaje){
                Picasso.get().load(itemMensaje.image).into(avatar)
                titulo?.text = itemMensaje.titulo
                ultimo?.text = itemMensaje.ultimoMensaje
                ultimoFecha?.text = itemMensaje.fechaUltimoMensaje
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, ChatActivity::class.java)
                    intent.putExtra("chatId", itemMensaje.id)
                    itemView.context.startActivity(intent)
                }
            }
        }

        class HeaderViewHolder(view: View) : ViewHolder(view)
    }
}
