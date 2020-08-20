package com.megatorneos.views

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.megatorneos.viewmodels.PrincipalViewModel
import com.megatorneos.R

class PrincipalFragment : Fragment() {

    companion object {
        fun newInstance(): PrincipalFragment =
            PrincipalFragment()
    }

    private lateinit var viewModel: PrincipalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.principal_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PrincipalViewModel::class.java)
        // TODO: Use the ViewModel
    }

}