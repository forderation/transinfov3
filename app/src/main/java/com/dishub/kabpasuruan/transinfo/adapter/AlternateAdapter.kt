package com.dishub.kabpasuruan.transinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.model.jalanAlternate.Alternate

class AlternateAdapter (
    private val listAlternatif : List<Alternate>,
    private val listener: (Alternate) -> Unit
): RecyclerView.Adapter<AlternateAdapter.AlternateHolder>() {

    class AlternateHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nama = itemView.findViewById<TextView>(R.id.rw_nama)
        private val status = itemView.findViewById<TextView>(R.id.rw_status)
        private val info = itemView.findViewById<TextView>(R.id.rw_informasi)
        fun bind(alternate: Alternate, listener: (Alternate) -> Unit) {
            itemView.setOnClickListener {
                listener(alternate)
            }
            nama.text = alternate.nama
            status.text = alternate.status
            val informasi = "Informasi Tambahan: ${alternate.informasi}"
            info.text = informasi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlternateHolder {
        return AlternateHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_alternate, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listAlternatif.size
    }

    override fun onBindViewHolder(holder: AlternateHolder, position: Int) {
        holder.bind(listAlternatif[position], listener)
    }
}