package com.megatorneos.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.megatorneos.viewmodels.FavoritosViewModel
import com.megatorneos.R

class FavoritosFragment : Fragment() {

    companion object {
        fun newInstance(): FavoritosFragment = FavoritosFragment()
    }

    private lateinit var viewModel: FavoritosViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        Log.e("TAG", "favoritos: onCreateView" )
        return inflater.inflate(R.layout.favoritos_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e("TAG", "favoritos: onActivityCreated" )
        viewModel = ViewModelProvider(this).get(FavoritosViewModel::class.java)
        // TODO: Use the ViewModel
    }

}