package com.dishub.kabpasuruan.transinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.R.layout.item_cctv
import com.dishub.kabpasuruan.transinfo.model.cctv.CCTVLive

class CCTVListAdapter(
    private val listCCTV: ArrayList<CCTVLive>,
    private val listener: (CCTVLive) -> Unit,
    private val listenerMaps: (CCTVLive) -> Unit
) :
    RecyclerView.Adapter<CCTVListAdapter.CctvViewHolder>() {

    class CctvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cctvName = itemView.findViewById<TextView>(R.id.cctv_name)
        private val locationImg = itemView.findViewById<ImageView>(R.id.location_img)
        fun bind(cctv: CCTVLive, listener: (CCTVLive) -> Unit, listenerMaps: (CCTVLive) -> Unit) {
            cctvName.text = cctv.name
            cctvName.setOnClickListener {
                listener(cctv)
            }
            locationImg.setOnClickListener {
                listenerMaps(cctv)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CctvViewHolder(
        LayoutInflater.from(parent.context).inflate(item_cctv, parent, false)
    )

    override fun getItemCount(): Int {
        return listCCTV.size
    }

    override fun onBindViewHolder(holder: CctvViewHolder, position: Int) {
        holder.bind(listCCTV[position], listener, listenerMaps)
    }
}