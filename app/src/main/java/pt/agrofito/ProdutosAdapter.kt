package pt.agrofito

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ProdutosAdapter(private val onClick: (ProdutoRecord) -> Unit)
    : ListAdapter<ProdutoRecord, ProdutosAdapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<ProdutoRecord>() {
            override fun areItemsTheSame(a: ProdutoRecord, b: ProdutoRecord) = a.numero == b.numero
            override fun areContentsTheSame(a: ProdutoRecord, b: ProdutoRecord) = a == b
        }
    }

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val estado     = v.findViewById<TextView>(R.id.tv_estado_prod)
        val designacao = v.findViewById<TextView>(R.id.tv_designacao)
        val mpb        = v.findViewById<TextView>(R.id.tv_mpb)
        val substancia = v.findViewById<TextView>(R.id.tv_substancia_prod)
        val titular    = v.findViewById<TextView>(R.id.tv_titular)
        val funcao     = v.findViewById<TextView>(R.id.tv_funcao_prod)
        val numero     = v.findViewById<TextView>(R.id.tv_numero_prod)

        fun bind(r: ProdutoRecord) {
            estado.text     = estadoEmoji(r.estado)
            designacao.text = r.designacao.ifEmpty { "—" }
            mpb.visibility  = if (r.mpb == "Sim") View.VISIBLE else View.GONE
            substancia.text = r.substancia.ifEmpty { "—" }
            titular.text    = r.titular.ifEmpty { "—" }
            funcao.text     = r.funcaoTipo.ifEmpty { r.funcaoCurta.take(20) }
            numero.text     = "Nº ${r.numero}"
            itemView.setOnClickListener { onClick(r) }
        }
    }

    override fun onCreateViewHolder(p: ViewGroup, t: Int) =
        VH(LayoutInflater.from(p.context).inflate(R.layout.item_produto, p, false))
    override fun onBindViewHolder(h: VH, pos: Int) = h.bind(getItem(pos))
}
