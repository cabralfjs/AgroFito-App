package pt.agrofito

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout

class UsoDetailFragment : DetailFragment() {

    companion object {
        private var currentRecord: UsoRecord? = null
        fun newInstance(r: UsoRecord): UsoDetailFragment {
            currentRecord = r
            return UsoDetailFragment()
        }
    }

    override fun bind(view: View) {
        val r = currentRecord ?: return
        val titulo = listOfNotNull(
            r.produto.takeIf { it.isNotEmpty() },
            r.numero.takeIf { it.isNotEmpty() }?.let { "Nº $it" },
            r.autorizacao.takeIf { it.isNotEmpty() }
        ).joinToString(" · ")
        setHeader(view, titulo, r.estado)
        val c = view.findViewById<LinearLayout>(R.id.detail_fields)
        addField(c, "Cultura",                         r.cultura)
        addField(c, "Inimigo / Efeito",                r.inimigo)
        addField(c, "Nome Científico",                 r.nomeCient)
        addField(c, "Função",                          r.funcao)
        addField(c, "Substância(s) Ativa(s)",          r.substancia)
        addField(c, "Técnica / Equipamento",           r.tecnica)
        addField(c, "Concentração (min–máx)",          r.concentracao)
        addField(c, "Dose (min–máx)",                  r.dose)
        addField(c, "Nº Máx. / Intervalo (dias)",      r.numMaxIntervalo)
        addField(c, "I. Segurança / Reentrada (dias)", r.intervaloSeg)
        addField(c, "Volume de Calda (min–máx)",       r.volCalda)
        addField(c, "Uso Menor (UM)",                  r.usoMenor)
        addField(c, "Limite de Comercialização",       r.limiteComrc)
        addField(c, "Limite de Utilização",            r.limiteUtil)
        if (listOf(r.sitParticular, r.epoca, r.restricoes).any { it.isNotEmpty() }) {
            addDivider(c)
            addField(c, "Situação Particular",             r.sitParticular)
            addField(c, "Época / Estado Fenológico",       r.epoca)
            addField(c, "Cond. e Restrições Específicas",  r.restricoes)
        }
    }
}
