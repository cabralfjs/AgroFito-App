package pt.agrofito

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout

class ProdutoDetailFragment : DetailFragment() {

    companion object {
        private var currentRecord: ProdutoRecord? = null
        fun newInstance(r: ProdutoRecord): ProdutoDetailFragment {
            currentRecord = r
            return ProdutoDetailFragment()
        }
    }

    override fun bind(view: View) {
        val r = currentRecord ?: return
        val titulo = listOfNotNull(
            r.designacao.takeIf { it.isNotEmpty() },
            r.numero.takeIf { it.isNotEmpty() }?.let { "Nº $it" },
            r.autorizacao.takeIf { it.isNotEmpty() }
        ).joinToString(" · ")
        setHeader(view, titulo, r.estado)
        val c = view.findViewById<LinearLayout>(R.id.detail_fields)
        addField(c, "Titular",                    r.titular)
        addField(c, "Tipo de Utilização",         r.tipoUtil)
        addField(c, "Substância(s) Ativa(s)",     r.substancia)
        addField(c, "Teor(es)",                   r.teor)
        addField(c, "Tipo de Formulação",         r.formulacao)
        addField(c, "Função",                     r.funcaoCurta)
        addField(c, "PF Baixo Risco",             r.baixoRisco)
        addField(c, "PF Cand. Substituição",      r.candSubs)
        addField(c, "PF MPB",                     r.mpb)
        addField(c, "Data de Autorização",        r.dataAutorizacao)
        if (r.classificacao.isNotEmpty()) {
            addDivider(c)
            addField(c, "Classificação / Pictogramas GHS", r.classificacao)
        }
    }
}
