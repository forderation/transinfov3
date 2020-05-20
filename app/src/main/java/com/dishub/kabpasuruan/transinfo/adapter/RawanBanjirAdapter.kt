package com.dishub.kabpasuruan.transinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.model.rawanBanjir.Banjir

class RawanBanjirAdapter (
    private val listRawan : List<Banjir>,
    private val listener: (Banjir) -> Unit
): RecyclerView.Adapter<RawanBanjirAdapter.RawanHolder>() {

    class RawanHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nama = itemView.findViewById<TextView>(R.id.rw_nama)
        private val kec = itemView.findViewById<TextView>(R.id.rw_kec)
        private val desa = itemView.findViewById<TextView>(R.id.rw_desa)
        private val status = itemView.findViewById<TextView>(R.id.rw_status)
        fun bind(rawan: Banjir,listener: (Banjir) -> Unit) {
            itemView.setOnClickListener {
                listener(rawan)
            }
            nama.text = rawan.nama
            kec.text = rawan.kec
            desa.text = rawan.desa
            status.text = rawan.status
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RawanHolder {
        return RawanHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_banjir, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listRawan.size
    }

    override fun onBindViewHolder(holder: RawanHolder, position: Int) {
        holder.bind(listRawan[position], listener)
    }
}