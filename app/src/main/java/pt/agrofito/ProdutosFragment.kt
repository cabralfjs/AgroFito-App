package pt.agrofito

import android.graphics.Color
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ProdutosFragment : BaseListFragment() {

    private val adapter = ProdutosAdapter { r ->
        try { openDetail(ProdutoDetailFragment.newInstance(r)) }
        catch (e: Exception) { Log.e("AgroFito", "open detail error", e) }
    }
    private var spEstado: Spinner? = null
    private var spFuncao: Spinner? = null
    private var allRecords: List<ProdutoRecord> = emptyList()

    override fun headerTitle() = "Produtos Fitofarmacêuticos"
    override fun headerColor() = Color.parseColor("#2d5a7a")
    override fun isDataLoaded() = DataRepository.produtosLoaded
    override fun provideAdapter() = adapter

    override fun setupFilters() {
        try {
            val estados = listOf("Todos estados", "Autorizada",
                "Cancelada — Venda Permitida",
                "Cancelada — Venda Interdita / Util. Permitida",
                "Cancelada — Venda e Util. Interditas")
            spEstado = makeSpinner(estados) { applyFilters() }
            spFuncao = makeSpinner(listOf("Todas funções")) { applyFilters() }
            filterRow?.addView(spEstado)
            filterRow?.addView(spFuncao)
        } catch (e: Exception) { Log.e("AgroFito", "setupFilters error", e) }
    }

    override fun loadData() {
        showLoading("A carregar produtos…")
        lifecycleScope.launch {
            try {
                DataRepository.loadProdutos(requireContext()) { msg -> showLoading(msg) }
                allRecords = DataRepository.produtos
                val funcoes = listOf("Todas funções") +
                    allRecords.map { it.funcaoTipo }.filter { it.isNotEmpty() }.distinct().sorted()
                activity?.runOnUiThread {
                    spFuncao?.adapter = ArrayAdapter(requireContext(),
                        android.R.layout.simple_spinner_item, funcoes)
                        .also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
                    showList()
                    applyFilters()
                }
            } catch (e: Exception) {
                Log.e("AgroFito", "loadData error", e)
                showLoading("Erro ao carregar. Verifique a ligação.")
            }
        }
    }

    override fun applyFilters() {
        if (!isAdded || !DataRepository.produtosLoaded) return
        try {
            if (allRecords.isEmpty()) allRecords = DataRepository.produtos
            val q      = searchInput?.text?.toString()?.trim()?.lowercase() ?: ""
            val estado = spEstado?.selectedItem?.toString() ?: "Todos estados"
            val funcao = spFuncao?.selectedItem?.toString() ?: "Todas funções"
            val result = allRecords.filter { r ->
                (estado == "Todos estados" || r.estado == estado) &&
                (funcao == "Todas funções" || r.funcaoTipo == funcao) &&
                (q.isEmpty() || "${r.designacao} ${r.substancia} ${r.titular} ${r.numero}".lowercase().contains(q))
            }
            adapter.submitList(result.take(300))
            countText?.text = "${result.size} registos"
        } catch (e: Exception) { Log.e("AgroFito", "applyFilters error", e) }
    }
}
