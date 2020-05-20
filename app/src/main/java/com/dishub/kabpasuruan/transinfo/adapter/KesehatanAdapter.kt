package com.dishub.kabpasuruan.transinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.model.kesehatan.Kesehatan

class KesehatanAdapter (
    private val listKesehatan : List<Kesehatan>,
    private val listener: (Kesehatan) -> Unit,
    private val listenerMaps: (Kesehatan) -> Unit
): RecyclerView.Adapter<KesehatanAdapter.KesehatanHolder>() {

    class KesehatanHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val namaInstansiTv = itemView.findViewById<TextView>(R.id.ks_tv_nama)
        private val alamatTv = itemView.findViewById<TextView>(R.id.ks_tv_alamat)
        private val jamTv = itemView.findViewById<TextView>(R.id.ks_tv_jam)
        private val cvCall =  itemView.findViewById<CardView>(R.id.ks_cv_call)
        private val cvLocation =  itemView.findViewById<CardView>(R.id.ks_cv_location)
        fun bind(kesehatan: Kesehatan,listener: (Kesehatan) -> Unit, listenerMaps: (Kesehatan) -> Unit) {
            namaInstansiTv.text = kesehatan.namaInstansi
            alamatTv.text = "Alamat :" + kesehatan.alamat
            val jamBuka = kesehatan.jam_buka.toString().jam24()
            val jamTutup = kesehatan.jam_tutup.toString().jam24()
            val jamRange = "Jam Kerja $jamBuka - $jamTutup"
            jamTv.text = jamRange
            cvCall.setOnClickListener { listener(kesehatan) }
            cvLocation.setOnClickListener { listenerMaps(kesehatan) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KesehatanHolder {
        return KesehatanHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_kesehatan, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listKesehatan.size
    }

    override fun onBindViewHolder(holder: KesehatanHolder, position: Int) {
        holder.bind(listKesehatan[position], listener, listenerMaps)
    }
}

private fun String.jam24(): String {
    val jam = this
    return if(jam.length == 3){
        "0" + jam.substring(0,1) + ":" + jam.substring(1)
    }else if(jam.length == 2){
        "00" + ":" + jam.substring(0)
    }else if(jam.length == 1){
        if(jam != "0"){
            "0" + jam.substring(0) + ":00"
        }else{
            "00:00"
        }
    }
    else{
        jam.substring(0,2) + ":" + jam.substring(2)
    }
}
