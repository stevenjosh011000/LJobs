package com.example.ljobs


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ljobs.JobApplication.JobApplicationEntity
import com.example.ljobs.databinding.MyJobApplierRowBinding

class JobApplierAdapter : RecyclerView.Adapter<JobApplierAdapter.MyViewHolder>() {

    private var applierList = ArrayList<JobApplicationEntity>()

    class MyViewHolder(binding: MyJobApplierRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val number = binding.tvNumber
        val gmail = binding.tvApplierEmail
        val detail = binding.btnViewApplierDetail
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return JobApplierAdapter.MyViewHolder(
            MyJobApplierRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = applierList[position]
        holder.number.text = currentItem.id.toString()
        holder.gmail.text = currentItem.email.toString()

        holder.detail.setOnClickListener{


            val intent = Intent(holder.itemView.context, JobApplierDetailActivity::class.java)
            intent.putExtra("EMAIL", currentItem.email.toString())
            holder.itemView.context.startActivity(intent)
        }


    }
    override fun getItemCount(): Int {
        return applierList.size
    }

    fun setData(jobApplier: ArrayList<JobApplicationEntity>) {
        this.applierList = jobApplier
        notifyDataSetChanged()
    }


}