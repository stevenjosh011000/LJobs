package com.example.ljobs

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ljobs.User.UserEntity
import com.example.ljobs.User.UserViewModel
import com.example.ljobs.databinding.ActivityEditUserBinding
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class EditUserActivity : AppCompatActivity() {

    var pickedPhoto : Uri? = null
    var pickedBitMap : Bitmap? = null
    var imageLatest : ByteArray? =null
    private val URL: String ="http://10.0.2.2/Ljobs/deleteLan.php"
    var roleLatest : String? = "1"
    lateinit var binding: ActivityEditUserBinding
    private lateinit var mUserViewModel : UserViewModel
    var account : UserEntity? = null
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_user)
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        setUpActionBar()

        val intent = intent
        val id = intent.getStringExtra("id")
        account = mUserViewModel.fetchById(id.toString().toInt())

        if(account!=null){

            if(account!!.image!=null){
                val bmp =  getImage(account?.image!!)
                binding.tvProfilePic.visibility = View.GONE
                binding.image.visibility = View.VISIBLE
                binding.image.setImageBitmap(bmp)
            }
            if(account?.role.toString()=="3"){
                binding.buttonDelete.visibility = View.GONE
                binding.buttonEdit.visibility = View.GONE
                binding.changeRole.visibility = View.GONE
                binding.editRoleTitle.setText("Current Role : Manager")
                binding.emailRg.setOnKeyListener(null)
                binding.phoneRg.setOnKeyListener(null)
                binding.tvProfilePic.isEnabled = false
                binding.image.isEnabled = false
                binding.nameRg.isEnabled = false
                binding.emailRg.isEnabled = false
                binding.phoneRg.isEnabled = false
            }
            var name: String = account?.name?.subSequence(0, 2).toString()
            binding.tvProfilePic.text = name.uppercase()
            roleLatest = account?.role.toString()

            binding.emailRg.setText(account?.email.toString())
            binding.nameRg.setText(account?.name.toString())
            binding.phoneRg.setText(account?.phoneNum.toString())
            if(account?.role.toString()=="1"){
                binding.editRoleTitle.setText(binding.editRoleTitle.text.toString()+"User")
            }else if(account?.role.toString()=="2"){
                binding.editRoleTitle.setText(binding.editRoleTitle.text.toString()+"Admin")
            }else{
                binding.editRoleTitle.setText(binding.editRoleTitle.text.toString())
            }
        }

        binding.changeRole.setOnClickListener {
            if(binding.changeRole.isChecked){
                binding.editRoleTitle.visibility = View.GONE
                binding.roleTitle.visibility = View.VISIBLE
                binding.roleSpinner.visibility = View.VISIBLE
            }else{
                binding.editRoleTitle.visibility = View.VISIBLE
                binding.roleTitle.visibility = View.GONE
                binding.roleSpinner.visibility = View.GONE
            }
        }

        ArrayAdapter.createFromResource(this,R.array.role,android.R.layout.simple_spinner_item).also{
                adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.roleSpinner.adapter = adapter
        }

        binding.tvProfilePic.setOnClickListener {
            pickPhoto()
        }

        binding.image.setOnClickListener {
            pickPhoto()
        }

        binding.buttonEdit.setOnClickListener {
            if(binding.changeRole.isChecked){
                if(binding.roleSpinner.selectedItemPosition==0){
                    roleLatest = "1"
                }else{
                    roleLatest = "2"
                }
            }
            editUser(id.toString(),account?.email.toString())
        }

        binding.buttonDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setCancelable(false)
            builder.setTitle("Confirm delete this Account?")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("Yes"){
                    dialogInterface, _ ->

                val stringRequest: StringRequest = object : StringRequest(
                    Request.Method.POST, URL,
                    Response.Listener { response ->
                        Log.e("Register",response)
                        if (response == "success") {
                            mUserViewModel.deleteUser(id.toString().toInt())
                            Toast.makeText(this,"Account deleted successfully", Toast.LENGTH_SHORT).show()
                            dialogInterface.dismiss()
                            this.finish()
                        } else if (response == "failure") {
                            Toast.makeText(
                                this@EditUserActivity,
                                "Something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    Response.ErrorListener { error ->
                        Log.e("error", error.toString().trim { it <= ' ' })
                        Toast.makeText(
                            applicationContext,
                            error.toString().trim { it <= ' ' },
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                    @Throws(AuthFailureError::class)
                    override fun getParams(): Map<String, String>? {
                        val data: MutableMap<String, String> = HashMap()
                        data["email"] = account?.email.toString()
                        return data
                    }
                }
                val requestQueue = Volley.newRequestQueue(applicationContext)
                requestQueue.add(stringRequest)
            }
            builder.setNegativeButton("No"){dialogInterface,_->
                dialogInterface.dismiss()
            }
            val alertDialog : AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            builder.show()
        }
    }

    private fun editUser(id:String,currentEmail:String){

        val email = binding.emailRg.text.toString()
        val name = binding.nameRg.text.toString()
        val phoneNo = binding.phoneRg.text.toString()

        if(email.isEmpty()){
            binding.emailContainerRg.helperText = "Email is required"
            return
        }
        if(!email.matches("^\\S+(?: \\S+)*\$".toRegex())){
            binding.emailContainerRg.helperText = "Cannot start, end with white-space and consist of few white-spaces in a row"
            return
        }
        if(!isValidEmail(email)){
            binding.emailContainerRg.helperText = "Invalid email"
            return

        }else{
            binding.emailContainerRg.helperText = ""
        }

        if(name.isEmpty()){
            binding.nameContainerRg.helperText = "Name is required"
            return
        }

        if(name.length<2) {
            binding.nameContainerRg.helperText = "Minimum 3 character"
            return
        }
        if(!name.matches("^\\S+(?: \\S+)*\$".toRegex())){
            binding.nameContainerRg.helperText = "Cannot start, end with white-space and consist of few white-spaces in a row"
            return
        }
        else
        {
            binding.nameContainerRg.helperText = ""
        }

        if(phoneNo.isEmpty()){
            binding.phoneContainerRg.helperText = "Phone number is required"
            return
        }

        if(phoneNo.length < 9){
            binding.phoneContainerRg.helperText = "Phone Number : 9-10 digits"
            return
        }

        if(phoneNo.length > 10){
            binding.phoneContainerRg.helperText = "Phone Number : 9-10 digits"
            return
        }
        else{
            binding.phoneContainerRg.helperText = ""
        }

        val emailExist = mUserViewModel.emailExist(email)

        if(currentEmail == email){
            mUserViewModel.updateUserProfile(imageLatest,binding.emailRg.text.toString(),binding.nameRg.text.toString(),binding.phoneRg.text.toString(),roleLatest.toString(),id.toInt())
            Toast.makeText(this,"Updated Successfully",Toast.LENGTH_SHORT).show()
        }else {
            if (emailExist) {
                Toast.makeText(
                    this@EditUserActivity,
                    "Email has been used",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                mUserViewModel.updateUserProfile(
                    imageLatest,
                    binding.emailRg.text.toString(),
                    binding.nameRg.text.toString(),
                    binding.phoneRg.text.toString(),
                    roleLatest.toString(),
                    id.toInt()
                )
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun pickPhoto(){
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            val galeriIntext = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntext,2)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val galeriIntext = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntext,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            pickedPhoto = data.data
            if (pickedPhoto != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(contentResolver,pickedPhoto!!)
                    pickedBitMap = ImageDecoder.decodeBitmap(source)
                    val bos = ByteArrayOutputStream()
                    pickedBitMap!!.compress(Bitmap.CompressFormat.PNG, 100, bos)
                    val bArray: ByteArray = bos.toByteArray()

                    imageLatest = compressImage(bArray)
                    val bmp = getImage(compressImage(bArray))
                    binding.image.visibility = View.VISIBLE
                    binding.tvProfilePic.visibility = View.GONE
                    binding.image.setImageBitmap(bmp)
                }
                else {
                    pickedBitMap = MediaStore.Images.Media.getBitmap(contentResolver,pickedPhoto)
                    val bos = ByteArrayOutputStream()
                    pickedBitMap!!.compress(Bitmap.CompressFormat.PNG, 100, bos)
                    val bArray: ByteArray = bos.toByteArray()

                    imageLatest = compressImage(bArray)
                    val bmp = getImage(compressImage(bArray))
                    binding.image.visibility = View.VISIBLE
                    binding.tvProfilePic.visibility = View.GONE
                    binding.image.setImageBitmap(bmp)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun getImage(image: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }

    fun compressImage(imageToCompress:ByteArray):ByteArray{
        var compressImage = imageToCompress
        while (compressImage.size>500000){
            val bitmap = BitmapFactory.decodeByteArray(compressImage,0,compressImage.size)
            val resized = Bitmap.createScaledBitmap(bitmap,(bitmap.width*0.8).toInt(),(bitmap.height*0.8).toInt(),true)
            val stream = ByteArrayOutputStream()
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream)
            compressImage = stream.toByteArray()
        }
        return compressImage
    }

    private fun setUpActionBar(){
        setSupportActionBar(binding.toolbarUserEditActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.arrow_back_24)
        }

        binding.toolbarUserEditActivity.setNavigationOnClickListener{onBackPressed()}
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}