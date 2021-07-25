package com.example.restaurantalpha.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantalpha.R
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil

class ContactUsActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var scrollView: ScrollView
    lateinit var txt1: TextView
    lateinit var txt2: TextView
    lateinit var txt3: TextView
    lateinit var txt4: TextView
    lateinit var txt5: TextView
    lateinit var txt6: TextView
    lateinit var txt7: TextView
    lateinit var txt8: TextView
    lateinit var txt9: TextView
    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etMessage: EditText
    lateinit var btnSend: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        txt1 = findViewById(R.id.txt1)
        txt2 = findViewById(R.id.txt2)
        txt3 = findViewById(R.id.txt3)
        txt4 = findViewById(R.id.txt4)
        txt5 = findViewById(R.id.txt5)
        txt6 = findViewById(R.id.txt6)
        txt7 = findViewById(R.id.txt7)
        txt8 = findViewById(R.id.txt8)
        txt9 = findViewById(R.id.txt9)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)
        scrollView = findViewById(R.id.scrollView)
        setUpToolbar()
        btnSend.setOnClickListener {
            sendEmail(etMessage.text.toString())
            UIUtil.hideKeyboard(this)
        }


    }

    private fun sendEmail(message: String) {
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("info@meat2home.online"))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, "A message from")
        mIntent.putExtra(Intent.EXTRA_TEXT, message)
        try {
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        } catch (e: Exception) {

            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Contact Us"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}