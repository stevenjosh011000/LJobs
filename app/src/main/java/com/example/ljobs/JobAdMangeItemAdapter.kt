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
import com.example.ljobs.Edu.EduEntity
import com.example.ljobs.Job.JobEntity
import com.example.ljobs.Job.JobViewModel
import com.example.ljobs.databinding.JobAdItemRowBinding
import kotlinx.android.synthetic.main.activity_job_ad_manage.view.*
import kotlinx.android.synthetic.main.job_ad_item_row.view.*
import kotlinx.coroutines.launch

class JobAdMangeItemAdapter : RecyclerView.Adapter<JobAdMangeItemAdapter.ViewHolder>(){

    private var jobList = emptyList<JobEntity>()
    private var context: Context? = null

    class ViewHolder(binding: JobAdItemRowBinding) : RecyclerView.ViewHolder(binding.root){
        val title = binding.title
        val id = binding.idText
        val editJob = binding.editJob
        val manageJob = binding.manageJob
        val status = binding.status
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(JobAdItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = jobList[position]
        holder.title.text = currentItem.title
        holder.id.text = currentItem.id.toString()
        if(currentItem.jobStatus.toString() == "0"){
            holder.status.text = "Pending"
            holder.status.setTextColor(Color.BLUE)
        }else if(currentItem.jobStatus.toString() == "1"){
            holder.status.text = "Approved"
            holder.status.setTextColor(Color.GREEN)
        }else if(currentItem.jobStatus.toString() == "2"){
            holder.status.text = "Rejected"
            holder.status.setTextColor(Color.RED)
        }

        holder.editJob.setOnClickListener {
            val intent = Intent(context,JobAdEditActivity::class.java)
            intent.putExtra("id",currentItem.id.toString())
            context?.startActivity(intent)
        }

        holder.manageJob.setOnClickListener{
            val intent = Intent(context,JobAdManageActivity::class.java)
            intent.putExtra("id",currentItem.id.toString())
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    fun setData(job:List<JobEntity>){
        this.jobList = job
        notifyDataSetChanged()
    }


    }