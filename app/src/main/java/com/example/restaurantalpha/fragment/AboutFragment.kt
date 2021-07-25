package com.example.restaurantalpha.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import com.example.restaurantalpha.R
import com.example.restaurantalpha.activity.ContactUsActivity
import com.example.restaurantalpha.activity.FAQActivity
import com.example.restaurantalpha.activity.TOSActivity


class AboutFragment : Fragment() {
    lateinit var scrollView: ScrollView
    lateinit var btnContactUs: Button
    lateinit var btnFAQs: Button
    lateinit var btntos: Button
    lateinit var txtQuote: TextView
    lateinit var txtCopyRight: TextView
    lateinit var imgMeatonline: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_about, container, false)
        scrollView = view.findViewById(R.id.scrollView)
        btnContactUs = view.findViewById(R.id.btnContactUs)
        btnFAQs = view.findViewById(R.id.btnFAQs)
        btntos = view.findViewById(R.id.btntos)
        txtQuote = view.findViewById(R.id.txtQuote)
        txtCopyRight = view.findViewById(R.id.txtCopyRight)
        imgMeatonline = view.findViewById(R.id.imgMeatonline)

        btnContactUs.setOnClickListener {
            startActivity(Intent(activity as Context, ContactUsActivity::class.java))
        }
        btnFAQs.setOnClickListener {
            startActivity(Intent(activity as Context, FAQActivity::class.java))
        }
        btntos.setOnClickListener {
            startActivity(Intent(activity as Context, TOSActivity::class.java))
        }




        return view
    }


}