package com.dishub.kabpasuruan.transinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.model.parking.Parking

class ParkingAdapter (
    private val listParking : List<Parking>,
    private val listener: (Parking) -> Unit
): RecyclerView.Adapter<ParkingAdapter.ParkingHolder>() {

    class ParkingHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNama = itemView.findViewById<TextView>(R.id.tv_nama_jalan)
        private val tvLokasi = itemView.findViewById<TextView>(R.id.tv_lk)
        private val tvKec = itemView.findViewById<TextView>(R.id.tv_kc)
        private val tvDes = itemView.findViewById<TextView>(R.id.tv_desa)
        private val tvJukir = itemView.findViewById<TextView>(R.id.tv_jukir)
        private val tvPengawas = itemView.findViewById<TextView>(R.id.tv_pengawas)
        private val tvKap = itemView.findViewById<TextView>(R.id.tv_kapasitas)
        private val imgLocation = itemView.findViewById<ImageView>(R.id.img_location)
        fun bind(parking: Parking, listener: (Parking) -> Unit) {
            tvNama.text = "Jalan: ${parking.namaJalan}"
            tvLokasi.text = "Lokasi: ${parking.lokasiParkir}"
            tvKec.text = "Kecamatan: ${parking.kecataman}"
            tvDes.text = "Desa: ${parking.desa}"
            tvJukir.text = "Jukir: ${parking.namaJukir}"
            tvPengawas.text = "Pengawas: ${parking.namaPengawas}"
            tvKap.text = "Kapasitas:\n ${parking.kapasitas}"
            imgLocation.setOnClickListener{
                listener(parking)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingHolder {
        return ParkingHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_parking, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listParking.size
    }

    override fun onBindViewHolder(holder: ParkingHolder, position: Int) {
        holder.bind(listParking[position], listener)
    }
}