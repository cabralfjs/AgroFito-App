package pt.agrofito
import org.json.JSONObject

data class UsoRecord(
    val estado: String, val cultura: String, val inimigo: String,
    val nomeCient: String, val produto: String, val numero: String,
    val autorizacao: String, val funcao: String, val substancia: String,
    val dose: String, val tecnica: String, val concentracao: String,
    val volCalda: String, val numMaxIntervalo: String, val intervaloSeg: String,
    val usoMenor: String, val limiteComrc: String, val limiteUtil: String,
    val sitParticular: String, val epoca: String, val restricoes: String
) {
    companion object {
        fun fromJson(j: JSONObject) = UsoRecord(
            estado=j.optString("estado"), cultura=j.optString("cultura"),
            inimigo=j.optString("inimigo"), nomeCient=j.optString("nome_cient"),
            produto=j.optString("produto"), numero=j.optString("numero"),
            autorizacao=j.optString("autorizacao"), funcao=j.optString("funcao"),
            substancia=j.optString("substancia"), dose=j.optString("dose"),
            tecnica=j.optString("tecnica"), concentracao=j.optString("concentracao"),
            volCalda=j.optString("vol_calda"), numMaxIntervalo=j.optString("num_max_intervalo"),
            intervaloSeg=j.optString("intervalo_seg"), usoMenor=j.optString("uso_menor"),
            limiteComrc=j.optString("limite_comerc"), limiteUtil=j.optString("limite_util"),
            sitParticular=j.optString("sit_particular"), epoca=j.optString("epoca"),
            restricoes=j.optString("restricoes")
        )
    }
}

data class ProdutoRecord(
    val estado: String, val designacao: String, val autorizacao: String,
    val numero: String, val titular: String, val tipoUtil: String,
    val substancia: String, val teor: String, val formulacao: String,
    val funcaoCurta: String, val funcaoTipo: String, val classificacao: String,
    val frases: String, val baixoRisco: String, val candSubs: String,
    val mpb: String, val dataAutorizacao: String
) {
    companion object {
        fun fromJson(j: JSONObject) = ProdutoRecord(
            estado=j.optString("estado"), designacao=j.optString("designacao"),
            autorizacao=j.optString("autorizacao"), numero=j.optString("numero"),
            titular=j.optString("titular"), tipoUtil=j.optString("tipo_util"),
            substancia=j.optString("substancia"), teor=j.optString("teor"),
            formulacao=j.optString("formulacao"), funcaoCurta=j.optString("funcao_curta"),
            funcaoTipo=j.optString("funcao_tipo"), classificacao=j.optString("classificacao"),
            frases=j.optString("frases"), baixoRisco=j.optString("baixo_risco"),
            candSubs=j.optString("cand_subs"), mpb=j.optString("mpb"),
            dataAutorizacao=j.optString("data_autorizacao")
        )
    }
}

fun estadoEmoji(estado: String) = when {
    estado.contains("Venda e Util")    -> "⚫"
    estado.contains("Venda Interdita") -> "🟠"
    estado.contains("Venda Permitida") -> "🟡"
    estado.contains("Cancelada")       -> "🔴"
    else                               -> "✅"
}
