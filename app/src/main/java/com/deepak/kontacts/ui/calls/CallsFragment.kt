package com.deepak.kontacts.ui.calls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.deepak.kontacts.R

class CallsFragment : Fragment() {

    private lateinit var callsViewModel: CallsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        callsViewModel =
                ViewModelProviders.of(this).get(CallsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_calls, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        callsViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}