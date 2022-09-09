package com.example.ljobs.fragments


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.ljobs.*
import com.example.ljobs.Session.LoginPref
import com.example.ljobs.databinding.DialogProfileUpdateBinding
import com.example.ljobs.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest

class ProfileFragment : Fragment() {

    lateinit var session : LoginPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        session = LoginPref(activity?.applicationContext!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : FragmentProfileBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false)

        val typeFace: Typeface = Typeface.createFromAsset(activity?.applicationContext?.assets,"goodTimesRg.otf")
        val userDao = (activity?.applicationContext as UserApp).db.userDao()

        var user:HashMap<String,String> = session.getUserDetails()
        val email: String = user.get(LoginPref.KEY_EMAIL).toString()
        val pass: String = user.get(LoginPref.KEY_PASS).toString()
        val id: Int = user.get(LoginPref.KEY_ID)?.toInt()!!

        binding.tvProfilePic.setOnClickListener {
           binding.tvProfileName.text = user.get(LoginPref.KEY_EMAIL)
        }
        binding.tvProfileName.typeface = typeFace

        lifecycleScope.launch{
            userDao.fetchUserByEmail(user.get(LoginPref.KEY_EMAIL).toString()).collect{
                if(it!=null){
                    var name : String = it.name?.subSequence(0,2).toString()
                    binding.tvProfilePic.text = name.uppercase()
                    binding.tvProfileName.text = it.name
                    binding.tvProfileEmail.text = it.email
                    binding.tvProfilePhone.text = "+60"+it.phoneNum
                }
            }
        }

        binding.resumeAdd.setOnClickListener {
            startFileChooser()
        }

        binding.ivEdit.setOnClickListener {
            updateProfileDialog(id,email,pass,userDao,container?.context!!)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun startFileChooser(){
        var intent = Intent()
        intent.setType("application/pdf")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivity(Intent.createChooser(intent,"Choose PDF"))
    }

    private fun updateProfileDialog(id:Int,email:String,currentPass:String,userDao: UserDao,context: Context){

        val updateDialog= Dialog(context, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)

        val binding = DialogProfileUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        lifecycleScope.launch{
            userDao.fetchUserByEmail(email).collect{
                if(it !=null){
                    binding.nameUp.setText(it.name.toString())
                    binding.phoneUp.setText(it.phoneNum.toString())
                }
            }
        }
        binding.editPassword.setOnClickListener {
            if(binding.editPassword.isChecked){
                binding.oldPassContainer.visibility = View.VISIBLE
                binding.newPasswordContainer.visibility = View.VISIBLE
                binding.confirmPassContainer.visibility = View.VISIBLE
            }else{
                binding.oldPassContainer.visibility = View.GONE
                binding.newPasswordContainer.visibility = View.GONE
                binding.confirmPassContainer.visibility = View.GONE
            }
        }

        binding.tvUpdate.setOnClickListener {
            val name = binding.nameUp.text.toString()
            val phone = binding.phoneUp.text.toString()
            val oldPassword = binding.oldPass.text.toString()
            val password = binding.newPassword.text.toString()
            val confirmPass = binding.confirmPass.text.toString()

            binding.nameContainerUp.helperText = nameValidation(name)
            binding.phoneContainerUp.helperText = phoneValidation(phone)
            binding.newPasswordContainer.helperText = passwordValidation(password,oldPassword)
            binding.confirmPassContainer.helperText = confirmPasswordValidation(confirmPass,password)

            if(binding.editPassword.isChecked){
                if( nameValidation(name)==""&&
                    phoneValidation(phone)==""&&
                    passwordValidation(password, oldPassword)==""&&
                    confirmPasswordValidation(confirmPass,password)==""){

                    if(currentPass != md5(oldPassword)){
                        binding.oldPassContainer.helperText = "Old password does not match"
                    }else{
                        lifecycleScope.launch{
                            userDao.update(UserEntity(id=id,password= md5(password),email=email,name= name, phoneNum = phone))
                            session.createLoginSession(id.toString(),email,md5(password))
                            Toast.makeText(activity?.applicationContext,"Record Updated.", Toast.LENGTH_LONG).show()
                            updateDialog.dismiss()
                        }
                    }
                }
            }else{
                if(nameValidation(name)==""&&
                    phoneValidation(phone)==""){
                    lifecycleScope.launch{
                        userDao.update(UserEntity(id=id,password= currentPass,email=email,name= name, phoneNum = phone))
                        Toast.makeText(activity?.applicationContext,"Record Updated.", Toast.LENGTH_LONG).show()
                        updateDialog.dismiss()
                    }
                }
            }


        }

        binding.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }

        updateDialog.show()

    }

    private fun confirmPasswordValidation(confirmPass:String,newPassword:String):String{
        if(confirmPass.isEmpty()){
            return "Confirm password is required"
        }

        if(newPassword != confirmPass){
            return "Confirm password does not match the password"
        }

        return ""
    }

    private fun passwordValidation(newPassword:String,currentPass: String):String{

        if(newPassword.isEmpty()){
            return "Password is required"
        }

        if(newPassword.length<6){
            return "Minimum 6 character password"
        }

        if(!newPassword.matches(".*[A-Z].*".toRegex())){
            return "Must contain 1 upper-case character"
        }

        if(!newPassword.matches(".*[a-z].*".toRegex())){
            return "Must contain 1 lower-case character"
        }

        if(!newPassword.matches(".*[@#\$%^&+=].*".toRegex())){
            return "Must contain 1 special character"

        }

        if(newPassword == currentPass){
            return "New Password cannot be the same as current password"
        }

        return ""
    }

    private fun phoneValidation(phone: String) : String{

        if(phone.isEmpty()){
//            binding.phoneContainerUp.helperText = "Phone number is required"
            return "Phone number is required"
        }

        if(phone.length < 9){
//            binding.phoneContainerUp.helperText = "Phone Number : 9-10 digits"
            return "Phone Number : 9-10 digits"
        }

        if(phone.length > 10){
//            binding.phoneContainerUp.helperText = "Phone Number : 9-10 digits"
            return "Phone Number : 9-10 digits"
        }

        return ""
    }

    private fun nameValidation(name: String) : String{
        if(name.isEmpty()){
            return "Name is required"
        }

        if(name.length<2) {
            return "Minimum 3 character"
        }

        return ""
    }

    private fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }


}