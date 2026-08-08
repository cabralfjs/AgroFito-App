package pt.agrofito

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class UsosAdapter(private val onClick: (UsoRecord) -> Unit)
    : ListAdapter<UsoRecord, UsosAdapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<UsoRecord>() {
            override fun areItemsTheSame(a: UsoRecord, b: UsoRecord) =
                a.numero == b.numero && a.cultura == b.cultura && a.inimigo == b.inimigo
            override fun areContentsTheSame(a: UsoRecord, b: UsoRecord) = a == b
        }
    }

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val estado       = v.findViewById<TextView>(R.id.tv_estado)
        val produto      = v.findViewById<TextView>(R.id.tv_produto)
        val numero       = v.findViewById<TextView>(R.id.tv_numero)
        val cultInimigo  = v.findViewById<TextView>(R.id.tv_cultura_inimigo)
        val substancia   = v.findViewById<TextView>(R.id.tv_substancia)

        fun bind(r: UsoRecord) {
            estado.text      = estadoEmoji(r.estado)
            produto.text     = r.produto.ifEmpty { "—" }
            numero.text      = "Nº ${r.numero}"
            cultInimigo.text = listOf(r.cultura, r.inimigo).filter { it.isNotEmpty() }.joinToString(" · ")
            substancia.text  = r.substancia.ifEmpty { "—" }
            itemView.setOnClickListener { onClick(r) }
        }
    }

    override fun onCreateViewHolder(p: ViewGroup, t: Int) =
        VH(LayoutInflater.from(p.context).inflate(R.layout.item_uso, p, false))
    override fun onBindViewHolder(h: VH, pos: Int) = h.bind(getItem(pos))
}
