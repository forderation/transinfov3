package com.dishub.kabpasuruan.transinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.model.callCenter.CallCenter

class CallCenterAdapter (
    private val listCC : List<CallCenter>,
    private val listener: (CallCenter) -> Unit
): RecyclerView.Adapter<CallCenterAdapter.CallCenterHolder>() {

    class CallCenterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ccCaption = itemView.findViewById<TextView>(R.id.cc_caption)
        private val ccAdress = itemView.findViewById<TextView>(R.id.cc_address)
        fun bind(callCenter: CallCenter,listener: (CallCenter) -> Unit) {
            ccCaption.text = callCenter.namaInstansi
            ccAdress.text = callCenter.alamat
            itemView.setOnClickListener {
                listener(callCenter)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallCenterHolder {
        return CallCenterHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_call_center, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listCC.size
    }

    override fun onBindViewHolder(holder: CallCenterHolder, position: Int) {
        holder.bind(listCC[position], listener)
    }
}