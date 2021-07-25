package com.example.restaurantalpha.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.restaurantalpha.R
import com.example.restaurantalpha.model.Customer
import com.example.restaurantalpha.util.ConnectionManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class PhoneVerificationActivity : AppCompatActivity() {
    lateinit var imgMeat2home: ImageView
    lateinit var llotp: RelativeLayout
    lateinit var llphoneverify: RelativeLayout
    lateinit var etPhone: EditText
    lateinit var etotp: EditText
    lateinit var btnverify: Button
    lateinit var btnsignIn: Button
    lateinit var rlsigninbtn:RelativeLayout
    lateinit var progressBar: ProgressBar
    var codeSent = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_verification)

        rlsigninbtn = findViewById(R.id.rlsigninbtn)
        progressBar = findViewById(R.id.progressBar)
        imgMeat2home = findViewById(R.id.imgMeat2home)
        llotp = findViewById(R.id.llotp)
        llphoneverify = findViewById(R.id.llphoneverify)
        etPhone = findViewById(R.id.etPhone)
        btnsignIn = findViewById(R.id.btnsignIn)
        etotp = findViewById(R.id.etotp)
        btnverify = findViewById(R.id.btnverify)
        llotp.visibility = View.GONE
        rlsigninbtn.visibility=View.GONE
        btnsignIn.setOnClickListener {
            rlsigninbtn.visibility=View.VISIBLE
            sendVerificationCode()
        }
        btnverify.setOnClickListener {
            verifySignInCode()
        }
    }

    private fun verifySignInCode() {
        val code = etotp.text.toString()
        if (code.isEmpty()) {
            etPhone.error = "OTP is required!"
            etPhone.requestFocus()
            return
        }
        val credential = PhoneAuthProvider.getCredential(codeSent, code)
        signInWithCredential(credential)

    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(
            this
        ) { task ->
            if (task.isSuccessful) {
                val uId = FirebaseAuth.getInstance().uid ?:""
                val ref = FirebaseDatabase.getInstance().getReference("/users/$uId")

                ref.addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                        //
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                       if(snapshot.exists()){
                           Toast.makeText(this@PhoneVerificationActivity, "Sign In Successful", Toast.LENGTH_SHORT).show()
                           startActivity(Intent(this@PhoneVerificationActivity, MainActivity::class.java))
                           finishAffinity()
                       }
                        else{
                           val user = Customer(
                               uId,
                               "user$uId",
                               "",
                               etPhone.text.toString(),
                               ""
                           )
                           ref.setValue(user).addOnSuccessListener {
                               Toast.makeText(this@PhoneVerificationActivity, "Sign In Successful", Toast.LENGTH_SHORT).show()
                               startActivity(Intent(this@PhoneVerificationActivity, MainActivity::class.java))
                               finishAffinity()
                           }
                       }
                    }

                })


            } else {
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(this, "Sign In not Successful", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun sendVerificationCode() {
        val phone = "+91${etPhone.text}"
        if (phone.isEmpty()) {
            etPhone.error = "Phone number is required!"
            etPhone.requestFocus()
            return
        }
        if (phone.length < 10) {
            etPhone.error = "Please enter a valid number"
            etPhone.requestFocus()
            return
        }
        if (ConnectionManager().checkConnection(this)){
            rlsigninbtn.visibility=View.GONE
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            etPhone.text.toString(), 60, TimeUnit.SECONDS, this,

            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    etotp.setText(codeSent)
                    signInWithCredential(p0)
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(this@PhoneVerificationActivity, p0.message, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    codeSent = p0
                    llphoneverify.visibility = View.GONE
                    llotp.visibility = View.VISIBLE
                }
            })

    }
    }
}