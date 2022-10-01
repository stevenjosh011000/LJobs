package com.example.ljobs

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.ljobs.Job.JobEntity
import com.example.ljobs.JobApplication.JobApplicationEntity
import com.example.ljobs.User.UserViewModel
import com.example.ljobs.databinding.JobItemRowBinding
import java.security.acl.Owner

class JobItemAdapter(private val onClickListener: SelectJobOnClickListener, val owner: ViewModelStoreOwner): RecyclerView.Adapter<JobItemAdapter.ViewHolder>(){

    private var jobList = emptyList<JobEntity>()

    class ViewHolder(val binding: JobItemRowBinding) : RecyclerView.ViewHolder(binding.root){
        val jobTitle = binding.jobTitle
        val companyName = binding.companyName
        val location = binding.location
        val salary = binding.salary
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return JobItemAdapter.ViewHolder(
            JobItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userViewModel = ViewModelProvider(owner).get(UserViewModel::class.java)

        val jobItem = jobList[position]
        holder.jobTitle.text = jobItem.title
        holder.companyName.text = userViewModel.fetchByEmail(jobItem.email!!).name
        holder.location.text = jobItem.location
        holder.salary.text = jobItem.salary

        holder.binding.itemLayout.setOnClickListener{
            val intent = Intent(holder.itemView.context, JobDescActivity::class.java)
            intent.putExtra("ID", jobItem.id.toInt())
            intent.putExtra("EMAIL", jobItem.email.toString())
            intent.putExtra("TITLE", jobItem.title.toString())
            intent.putExtra("EDUREQUIREMENT", jobItem.eduRequirement.toString())
            intent.putExtra("SALARY", jobItem.salary.toString())
            intent.putExtra("DESCRIPTION", jobItem.descrip.toString())
            intent.putExtra("LOCATION", jobItem.location.toString())
            intent.putExtra("TYPE", jobItem.type.toString())
            intent.putExtra("COMPANYINFO", jobItem.companyInfo.toString())

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    internal fun setJobList(jobEntity: List<JobEntity>) {
        this.jobList = jobEntity
        notifyDataSetChanged()
    }

    class SelectJobOnClickListener (val clickListener: (jobItem: JobEntity) -> Unit) {
        fun onClick(jobItem: JobEntity) = clickListener(jobItem)
    }

}