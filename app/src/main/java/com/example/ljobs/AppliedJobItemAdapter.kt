package com.example.ljobs

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.example.ljobs.Job.JobEntity
import com.example.ljobs.Job.JobViewModel
import com.example.ljobs.JobApplication.JobApplicationViewModel
import com.example.ljobs.Session.LoginPref
import com.example.ljobs.User.UserViewModel
import com.example.ljobs.databinding.MyApplicationItemRowBinding


class AppliedJobItemAdapter(private val onClickListener: SelectJobOnClickListener, val context: Context, private val owner: ViewModelStoreOwner, val email: String): RecyclerView.Adapter<AppliedJobItemAdapter.ViewHolder>(){

    private var jobList = emptyList<JobEntity>()
    private lateinit var jobApplicationViewModel : JobApplicationViewModel
    lateinit var session : LoginPref

    class ViewHolder(val binding: MyApplicationItemRowBinding) : RecyclerView.ViewHolder(binding.root){
        val jobTitle = binding.jobTitle
        val location = binding.location
        val salary = binding.salary
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return AppliedJobItemAdapter.ViewHolder(
            MyApplicationItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userViewModel = ViewModelProvider(owner).get(UserViewModel::class.java)
        val jobApplicationViewModel = ViewModelProvider(owner).get(JobApplicationViewModel::class.java)

        val jobItem = jobList[position]
        holder.jobTitle.text = jobItem.title
        holder.location.text = jobItem.location
        holder.salary.text = jobItem.salary

        holder.binding.btnWithdraw.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Withdraw Application")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("Yes"){ dialogInterface, _ ->
                jobApplicationViewModel.deleteApplicationWithJobIdAndEmail(jobItem.id, email)
                Toast.makeText(context,"Withdraw Successfully", Toast.LENGTH_LONG).show()

                dialogInterface.dismiss()
            }
            builder.setNegativeButton("No"){dialogInterface,_->
                dialogInterface.dismiss()
            }
            val alertDialog : AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            builder.show()
        }

        holder.binding.itemLayout.setOnClickListener{
            val intent = Intent(holder.itemView.context, JobDescActivity::class.java)
            intent.putExtra("ID", jobItem.id.toInt())
            intent.putExtra("EMAIL", jobItem.email.toString())
            intent.putExtra("TITLE", jobItem.title.toString())
            intent.putExtra("EDUREQUIREMENT", jobItem.eduRequirement.toString())
            intent.putExtra("SALARY", jobItem.salary.toString())
            intent.putExtra("DESCRIPTION", jobItem.desc.toString())
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
        jobList = jobEntity
        notifyDataSetChanged()
    }

    class SelectJobOnClickListener (val clickListener: (jobItem: JobEntity) -> Unit) {
        fun onClick(jobItem: JobEntity) = clickListener(jobItem)
    }

}