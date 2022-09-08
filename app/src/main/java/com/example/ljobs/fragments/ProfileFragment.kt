package com.example.ljobs.fragments


import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.ljobs.R
import com.example.ljobs.Session.LoginPref
import com.example.ljobs.UserApp
import com.example.ljobs.UserDao
import com.example.ljobs.databinding.DialogProfileUpdateBinding
import com.example.ljobs.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch

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
        val email: String = binding.tvProfileEmail.toString()
        var user:HashMap<String,String> = session.getUserDetails()



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

        binding.ivEdit.setOnClickListener {
            updateProfileDialog(email,userDao)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun updateProfileDialog(email:String,userDao: UserDao){
        val fragment : ProfileFragment = (activity?.supportFragmentManager?.findFragmentByTag("ProfileFragment") as ProfileFragment)
        val updateDialog= Dialog(fragment.requireContext(), R.style.Theme_Dialog)
        updateDialog.setCancelable(false)

        val binding = DialogProfileUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

//        lifecycleScope.launch{
//            employeeDao.fetchEmployeeById(id).collect{
//                if(it != null){
//                    binding.etUpdateName.setText(it.name)
//                    binding.etUpdateEmailId.setText(it.email)
//                }
//            }
//        }
//
//        binding.tvUpdate.setOnClickListener {
//            val name = binding.etUpdateName.text.toString()
//            val email = binding.etUpdateEmailId.text.toString()
//            if(name.isNotEmpty() && email.isNotEmpty()){
//                lifecycleScope.launch{
//                    employeeDao.update(EmployeeEntity(id,name,email))
//                    Toast.makeText(applicationContext,"Record Updated.", Toast.LENGTH_LONG).show()
//                    updateDialog.dismiss()
//                }
//            }else{
//                Toast.makeText(applicationContext,"Name or Email cannot be blank.", Toast.LENGTH_LONG).show()
//            }
//        }

        binding.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }

        updateDialog.show()

    }


}