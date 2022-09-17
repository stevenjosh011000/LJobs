package com.example.ljobs

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ljobs.Edu.EduEntity
import com.example.ljobs.databinding.EduItemRowBinding

class EduItemAdapter (private val items:ArrayList<EduEntity>,
                      private val updateListener:(id:Int)->Unit,
                      private val deleteListener:(id:Int)->Unit)
    : RecyclerView.Adapter<EduItemAdapter.ViewHolder>(){

    class ViewHolder(binding: EduItemRowBinding) : RecyclerView.ViewHolder(binding.root){
        val eduLvlName = binding.eduLvlName
        val certName = binding.certName
        val schoolName = binding.school
        val endYear = binding.endYear
        val ivDelete = binding.ivDelete
        val ivEdit = binding.ivEdit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(EduItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.eduLvlName.text = item.eduLvl
        holder.certName.text = item.eduCert
        holder.schoolName.text = item.eduSchool
        holder.endYear.text = item.eduEndYear

        holder.ivEdit.setOnClickListener {
            updateListener.invoke(item.id)
        }

        holder.ivDelete.setOnClickListener {
            deleteListener.invoke(item.id)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


    }