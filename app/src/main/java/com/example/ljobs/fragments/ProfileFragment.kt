package com.example.ljobs.fragments


import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.ljobs.R
import com.example.ljobs.Session.LoginPref
import com.example.ljobs.UserApp
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
        // Inflate the layout for this fragment
        return binding.root
    }


}