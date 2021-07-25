package com.example.restaurantalpha.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.restaurantalpha.R
import com.example.restaurantalpha.model.Customer
import com.example.restaurantalpha.model.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil

class ProfileActivity : AppCompatActivity() {
    lateinit var lldisplayprofile: LinearLayout
    lateinit var lleditprofile: LinearLayout
    lateinit var toolbar: Toolbar
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var frame: FrameLayout
    lateinit var drawerLayout: DrawerLayout
    lateinit var cicProfileLo: CircleImageView
    lateinit var btnProfile: ImageButton
    lateinit var btnUpdateProfile: Button
    lateinit var txtName: TextView
    lateinit var txtemail: TextView
    lateinit var txtPhone: TextView
    lateinit var btnEditProfile: Button
    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var txtTotalOrders: TextView
    lateinit var txtTotalMoney: TextView

    val ordList = arrayListOf<Order>()
    var totalMoney = 0
    var selectedPhoto: Uri? = null
    var uid = FirebaseAuth.getInstance().uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        txtTotalMoney = findViewById(R.id.txtTotalMoney)
        txtTotalOrders = findViewById(R.id.txtTotalOrders)
        etEmail = findViewById(R.id.etEmail)
        etName = findViewById(R.id.etName)
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile)
        lleditprofile = findViewById(R.id.lleditprofile)
        lldisplayprofile = findViewById(R.id.lldisplayprofile)
        txtPhone = findViewById(R.id.txtPhone)
        cicProfileLo = findViewById(R.id.cicProfileLo)
        btnProfile = findViewById(R.id.btnProfile)
        btnEditProfile = findViewById(R.id.btnEditProfile)
        txtName = findViewById(R.id.txtName)
        txtemail = findViewById(R.id.txtEmail)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frame = findViewById(R.id.frame)
        drawerLayout = findViewById(R.id.drawerLayout)

        lleditprofile.visibility = View.GONE
        setUpToolbar()
        setUpYourProfile()
        getOrdersAndMoney()

        btnEditProfile.setOnClickListener {
            lldisplayprofile.visibility = View.GONE
            lleditprofile.visibility = View.VISIBLE
            UIUtil.showKeyboard(this, etName)
            val ref =
                FirebaseDatabase.getInstance().getReference("/users/$uid")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val p = p0.getValue(Customer::class.java)

                    etName.setText(p!!.cname)
                    etEmail.setText(p.cemail)

                }

            })
        }
        btnUpdateProfile.setOnClickListener {
            updateProfile()
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
        btnProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)

        }
    }

    private fun logoutnow() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Confirmation")
        dialog.setMessage("Are you sure you wanna Logout?")
        dialog.setPositiveButton("Logout") { _, _ ->
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "You have been Signed Out", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
        dialog.setNegativeButton("Cancel") { _, _ ->
            dialog.show().cancel()
        }
        dialog.create()
        dialog.show()
    }


    private fun setUpYourProfile() {
        if (uid == null && FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, PhoneVerificationActivity::class.java))
            finish()
        } else {
            val ref =
                FirebaseDatabase.getInstance().getReference("/users/$uid")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val p = p0.getValue(Customer::class.java)
                    if (p!!.cimage == "") {
                        Picasso.get().load(R.drawable.ic_person).into(cicProfileLo)
                    } else
                        Picasso.get().load(p.cimage).error(R.drawable.ic_person).into(cicProfileLo)
                    txtName.text = p.cname
                    txtemail.text = p.cemail
                    txtPhone.text = p.cphone

                }

            })
        }
    }

    private fun updateProfile() {

        val name = etName.text.toString()
        val email = etEmail.text.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.child("cname").setValue(name)
        ref.child("cemail").setValue(email)
            .addOnSuccessListener {
                Toast.makeText(this, "Update Successful", Toast.LENGTH_LONG).show()
                lldisplayprofile.visibility = View.VISIBLE
                lleditprofile.visibility = View.GONE
            }
    }

    private fun getOrdersAndMoney() {
        val ref = FirebaseDatabase.getInstance().getReference("Orders/$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val order = it.getValue(Order::class.java)
                    ordList.add(order!!)
                }
                txtTotalOrders.text = "Total Orders: ${ordList.size}"
                for (i in 0 until ordList.size) {
                    totalMoney += ordList[i].totalCost
                }
                txtTotalMoney.text = "Total Money Spent:  ₹$totalMoney"
            }


        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhoto = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhoto)
            cicProfileLo.setImageBitmap(bitmap)
            btnProfile.alpha = 0f
            updatePhoto()
        }
    }

    private fun updatePhoto() {

        val ref123 = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val ref = FirebaseStorage.getInstance().getReference("/Profiles/${uid}")
        ref.delete()
        if (selectedPhoto == null) return
        ref.putFile(selectedPhoto!!).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                ref123.child("cimage").setValue(it.toString())
                    .addOnSuccessListener {
                        Toast.makeText(this, "Update Successful", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Your Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.lo -> {
                logoutnow()
            }
            else ->
                super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}