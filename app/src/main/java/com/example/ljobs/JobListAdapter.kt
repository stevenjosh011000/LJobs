package com.example.ljobs


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ljobs.Job.JobEntity
import com.example.ljobs.databinding.MyJobRowBinding

class JobListAdapter : RecyclerView.Adapter<JobListAdapter.MyViewHolder>() {

    private var jobList = ArrayList<JobEntity>()

    class MyViewHolder(binding: MyJobRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val numberJob = binding.tvNumber
        val title = binding.tvJobTitle
        val btnEdit=binding.btnEdit
        val status = binding.tvStatus
        val viewApplier = binding.btnViewApplicationer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return JobListAdapter.MyViewHolder(
            MyJobRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = jobList[position]
        holder.numberJob.text = currentItem.id.toString()
        holder.title.text = currentItem.title.toString()
        holder.status.text= currentItem.jobStatus.toString()
        holder.btnEdit.setOnClickListener {
            val intent = Intent(holder.itemView.context, JobUpdateActivity::class.java)

            intent.putExtra("ID", currentItem.id.toInt())
            intent.putExtra("TITLE", currentItem.title.toString())
            intent.putExtra("EDUREQUIREMENT", currentItem.eduRequirement.toString())
            intent.putExtra("SALARY", currentItem.salary.toString())
            intent.putExtra("DESCRIPTION", currentItem.descrip.toString())
            intent.putExtra("LOCATION", currentItem.location.toString())
            intent.putExtra("TYPE", currentItem.type.toString())
            intent.putExtra("COMPANYINFO", currentItem.companyInfo.toString())

            holder.itemView.context.startActivity(intent)
        }

        holder.viewApplier.setOnClickListener{
            val intent = Intent(holder.itemView.context, MyJobApplierList::class.java)
            intent.putExtra("JobID",currentItem.id.toInt())
            holder.itemView.context.startActivity(intent)

        }

    }
    override fun getItemCount(): Int {
        return jobList.size
    }

    fun setData(job: ArrayList<JobEntity>) {
        this.jobList = job
        notifyDataSetChanged()
    }


}