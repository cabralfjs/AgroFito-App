package pt.agrofito

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class UsosFragment : BaseListFragment() {

    private val adapter = UsosAdapter { r ->
        try { openDetail(UsoDetailFragment.newInstance(r)) }
        catch (e: Exception) { Log.e("AgroFito", "open detail error", e) }
    }
    private var spEstado: Spinner? = null
    private var btnLoadAll: Button? = null
    private var allRecords: List<UsoRecord> = emptyList()

    override fun headerTitle() = "Condições de Utilização"
    override fun headerColor() = Color.parseColor("#0f3d22")
    override fun isDataLoaded() = DataRepository.usosLoaded
    override fun provideAdapter() = adapter

    override fun setupFilters() {
        try {
            val estados = listOf("Todos estados", "Autorizada",
                "Cancelada — Venda Permitida",
                "Cancelada — Venda Interdita / Util. Permitida",
                "Cancelada — Venda e Util. Interditas")
            spEstado = makeSpinner(estados) { applyFilters() }
            filterRow?.addView(spEstado)

            // Button to load cancelled records
            btnLoadAll = Button(requireContext()).apply {
                text = "＋ Carregar canceladas"
                textSize = 11f
                setTextColor(Color.WHITE)
                setBackgroundColor(Color.parseColor("#1a5c35"))
                setPadding(16, 8, 16, 8)
                visibility = View.VISIBLE
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).also { it.marginStart = 6 }
                setOnClickListener { loadAllData() }
            }
            filterRow?.addView(btnLoadAll)
        } catch (e: Exception) { Log.e("AgroFito", "setupFilters error", e) }
    }

    override fun loadData() {
        showLoading("A carregar autorizadas…")
        lifecycleScope.launch {
            try {
                DataRepository.loadUsos(requireContext()) { msg -> showLoading(msg) }
                allRecords = DataRepository.usos
                showList()
                applyFilters()
            } catch (e: Exception) {
                Log.e("AgroFito", "loadData error", e)
                showLoading("Erro ao carregar. Verifique a ligação.")
            }
        }
    }

    private fun loadAllData() {
        btnLoadAll?.visibility = View.GONE
        showLoading("A carregar canceladas…")
        lifecycleScope.launch {
            try {
                DataRepository.loadUsosAll(requireContext()) { msg -> showLoading(msg) }
                allRecords = DataRepository.usos
                showList()
                applyFilters()
            } catch (e: Exception) {
                Log.e("AgroFito", "loadAllData error", e)
                showList()
            }
        }
    }

    override fun applyFilters() {
        if (!isAdded || !DataRepository.usosLoaded) return
        try {
            if (allRecords.isEmpty()) allRecords = DataRepository.usos
            val q      = searchInput?.text?.toString()?.trim()?.lowercase() ?: ""
            val estado = spEstado?.selectedItem?.toString() ?: "Todos estados"
            val result = allRecords.filter { r ->
                (estado == "Todos estados" || r.estado == estado) &&
                (q.isEmpty() || "${r.cultura} ${r.inimigo} ${r.produto} ${r.substancia} ${r.numero}".lowercase().contains(q))
            }
            adapter.submitList(result.take(300))
            countText?.text = "${result.size} registos${if (!DataRepository.usosFullyLoaded) " (só autorizadas)" else ""}"
        } catch (e: Exception) { Log.e("AgroFito", "applyFilters error", e) }
    }
}
