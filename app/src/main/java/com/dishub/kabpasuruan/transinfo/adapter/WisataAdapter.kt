package com.dishub.kabpasuruan.transinfo.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.model.tempatWisata.Wisata
import com.dishub.kabpasuruan.transinfo.utils.SupportUtil.Companion.siangsaPhoto
import com.squareup.picasso.Picasso

class WisataAdapter (
    private val listWisata : List<Wisata>, private val listener : (Wisata) -> Unit
): RecyclerView.Adapter<WisataAdapter.WisataHolder>() {

    class WisataHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nama = itemView.findViewById<TextView>(R.id.tw_nama)
        private val alamat = itemView.findViewById<TextView>(R.id.tw_alamat)
        private val thumnail = itemView.findViewById<ImageView>(R.id.tw_thumnail)
        fun bind(wisata: Wisata, listener : (Wisata) -> Unit) {
            nama.text = wisata.nama
            alamat.text = wisata.alamat
            Picasso.get()
                .load(siangsaPhoto + wisata.thumnail)
                .fit()
                .centerCrop()
                .error(R.drawable.wisata)
                .into(thumnail)
            itemView.setOnClickListener {
                listener(wisata)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WisataHolder {
        return WisataHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_wisata, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listWisata.size
    }

    override fun onBindViewHolder(holder: WisataHolder, position: Int) {
        holder.bind(listWisata[position], listener)
    }
}