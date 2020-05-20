package com.dishub.kabpasuruan.transinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.model.linkWeb.Web

class LinkWebAdapter (
    private val listWeb : List<Web>,
    private val listener: (Web) -> Unit
): RecyclerView.Adapter<LinkWebAdapter.CallCenterHolder>() {

    class CallCenterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val webName = itemView.findViewById<TextView>(R.id.web_title)
        private val webDesc = itemView.findViewById<TextView>(R.id.web_desc)
        fun bind(web: Web,listener: (Web) -> Unit) {
            webName.text = web.namaWeb
            webDesc.text = web.descWeb
            itemView.setOnClickListener {
                listener(web)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallCenterHolder {
        return CallCenterHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_web, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listWeb.size
    }

    override fun onBindViewHolder(holder: CallCenterHolder, position: Int) {
        holder.bind(listWeb[position], listener)
    }
}