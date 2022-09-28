package com.example.ljobs

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.ljobs.Job.JobEntity
import com.example.ljobs.User.UserEntity
import com.example.ljobs.databinding.UserItemRowBinding
import kotlinx.android.synthetic.main.activity_job_ad_manage.view.*
import kotlinx.android.synthetic.main.job_ad_item_row.view.*
import kotlinx.android.synthetic.main.job_ad_item_row.view.editJob
import kotlinx.android.synthetic.main.job_ad_item_row.view.idText
import kotlinx.android.synthetic.main.user_item_row.view.*
import kotlinx.coroutines.launch

class userMangeItemAdapter : RecyclerView.Adapter<userMangeItemAdapter.ViewHolder>(){

    private var userList = emptyList<UserEntity>()
    private var context: Context? = null

    class ViewHolder(binding: UserItemRowBinding) : RecyclerView.ViewHolder(binding.root){
        val id = binding.idText
        val name = binding.userName
        val phoneNo = binding.userPhoneNo
        val email = binding.userEmail
        val role = binding.userType
        val editUser = binding.editUser
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(UserItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.id.text = currentItem.id.toString()
        holder.name.text = currentItem.name.toString()
        holder.phoneNo.text = "(+60)"+currentItem.phoneNum.toString()
        holder.email.text = currentItem.email.toString()

        if(currentItem.role.toString() == "1"){
            holder.role.text = "Normal User"
            holder.role.setTextColor(Color.GREEN)
        }else if(currentItem.role.toString() == "2"){
            holder.role.text = "Admin"
            holder.role.setTextColor(Color.BLUE)
        }else if(currentItem.role.toString() == "3"){
            holder.role.text = "Manager"
            holder.role.setTextColor(Color.BLUE)
        }

        holder.editUser.setOnClickListener {
            val intent = Intent(context,EditUserActivity::class.java)
            intent.putExtra("id",currentItem.id.toString())
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(job:List<UserEntity>){
        this.userList = job
        notifyDataSetChanged()
    }


    }