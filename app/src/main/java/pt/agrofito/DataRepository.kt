package pt.agrofito

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.net.URL

object DataRepository {
    private const val BASE = "https://cabralfjs.github.io/AgroFito"
    private const val CACHE_MS = 7L * 24 * 3600 * 1000

    // Load only Autorizadas first to avoid OOM
    private val USOS_PRIMARY = listOf(
        "data_autorizadas.json" to "Autorizada"
    )
    private val USOS_EXTRA = listOf(
        "data_canceladas_venda_permitida.json" to "Cancelada — Venda Permitida",
        "data_canceladas_venda_interdita_util_permitida.json" to "Cancelada — Venda Interdita / Util. Permitida",
        "data_canceladas_venda_util_interditas.json" to "Cancelada — Venda e Util. Interditas"
    )
    private val PROD_FILES = listOf(
        "prod_autorizadas.json" to "Autorizada",
        "prod_canceladas_venda_permitida.json" to "Cancelada — Venda Permitida",
        "prod_canceladas_venda_interdita_util_permitida.json" to "Cancelada — Venda Interdita / Util. Permitida",
        "prod_canceladas_venda_util_interditas.json" to "Cancelada — Venda e Util. Interditas"
    )

    var usos: List<UsoRecord> = emptyList(); private set
    var produtos: List<ProdutoRecord> = emptyList(); private set
    var usosLoaded = false; private set
    var usosFullyLoaded = false; private set
    var produtosLoaded = false; private set

    suspend fun loadUsos(ctx: Context, onProgress: (String) -> Unit = {}) = withContext(Dispatchers.IO) {
        val all = mutableListOf<UsoRecord>()
        for ((file, estado) in USOS_PRIMARY) {
            onProgress("A carregar $estado…")
            try {
                val j = fetchOrCache(ctx, file)
                val arr = j.getJSONArray("records")
                for (i in 0 until arr.length()) {
                    try { all.add(UsoRecord.fromJson(arr.getJSONObject(i))) }
                    catch (e: Exception) { }
                }
            } catch (e: Exception) { Log.w("AgroFito", "Failed $file: ${e.message}") }
        }
        usos = all; usosLoaded = true
        Log.d("AgroFito", "Loaded ${all.size} usos (primary)")
    }

    suspend fun loadUsosAll(ctx: Context, onProgress: (String) -> Unit = {}) = withContext(Dispatchers.IO) {
        val all = usos.toMutableList()
        for ((file, estado) in USOS_EXTRA) {
            onProgress("A carregar $estado…")
            try {
                val j = fetchOrCache(ctx, file)
                val arr = j.getJSONArray("records")
                for (i in 0 until arr.length()) {
                    try { all.add(UsoRecord.fromJson(arr.getJSONObject(i))) }
                    catch (e: Exception) { }
                }
            } catch (e: Exception) { Log.w("AgroFito", "Failed $file: ${e.message}") }
        }
        usos = all; usosFullyLoaded = true
        Log.d("AgroFito", "Loaded ${all.size} usos (all)")
    }

    suspend fun loadProdutos(ctx: Context, onProgress: (String) -> Unit = {}) = withContext(Dispatchers.IO) {
        val all = mutableListOf<ProdutoRecord>()
        for ((file, estado) in PROD_FILES) {
            onProgress("A carregar $estado…")
            try {
                val j = fetchOrCache(ctx, file)
                val arr = j.getJSONArray("records")
                for (i in 0 until arr.length()) {
                    try { all.add(ProdutoRecord.fromJson(arr.getJSONObject(i))) }
                    catch (e: Exception) { }
                }
            } catch (e: Exception) { Log.w("AgroFito", "Failed $file: ${e.message}") }
        }
        produtos = all; produtosLoaded = true
        Log.d("AgroFito", "Loaded ${all.size} produtos")
    }

    private fun fetchOrCache(ctx: Context, filename: String): JSONObject {
        val f = File(ctx.cacheDir, filename)
        if (f.exists() && System.currentTimeMillis() - f.lastModified() < CACHE_MS) {
            return JSONObject(f.readText())
        }
        val conn = URL("$BASE/$filename").openConnection()
        conn.connectTimeout = 30_000
        conn.readTimeout    = 60_000
        val text = conn.getInputStream().bufferedReader().readText()
        f.writeText(text)
        return JSONObject(text)
    }
}
