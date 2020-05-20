package com.dishub.kabpasuruan.transinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.model.perizinan.Izin

class IzinAdapter(
    private val listIzin: List<Izin>
) :
    RecyclerView.Adapter<IzinAdapter.IzinHolder>() {

    class IzinHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val descIzin = itemView.findViewById<TextView>(R.id.desc_izin)
        private val tglIzin = itemView.findViewById<TextView>(R.id.tgl_izin)
        fun bind(izin: Izin) {
            descIzin.text = izin.desc
            val tanggal = "Tanggal dibuat: ${izin.createdAt}"
            tglIzin.text = tanggal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = IzinHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_izin, parent, false)
    )

    override fun getItemCount(): Int {
        return listIzin.size
    }

    override fun onBindViewHolder(holder: IzinHolder, position: Int) {
        holder.bind(listIzin[position])
    }
}