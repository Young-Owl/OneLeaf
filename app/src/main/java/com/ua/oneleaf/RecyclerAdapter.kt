package com.ua.oneleaf

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ValueEventListener

private lateinit var mListener: RecyclerAdapter.onItemClickListener

class RecyclerAdapter(private val vaseList: ArrayList<Vase>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {



    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder{
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_select, parent, false)
        return ViewHolder(v,mListener)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int){
        val currentItem = vaseList[position]

        holder.itemTitle.text = currentItem.vasename
        holder.itemID.text = currentItem.vaseID
    }

    override fun getItemCount(): Int{
        return vaseList.size
    }

    inner class ViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        var itemTitle: TextView
        var itemID: TextView

        init{
            itemTitle = itemView.findViewById(R.id.IDVaseTxt)
            itemID = itemView.findViewById(R.id.VaseIDTxt)

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}