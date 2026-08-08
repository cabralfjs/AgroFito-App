package pt.agrofito

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

abstract class DetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.btn_back).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        bind(view)
    }

    abstract fun bind(view: View)

    protected fun setHeader(view: View, titulo: String, estado: String) {
        view.findViewById<TextView>(R.id.detail_titulo).text = titulo
        view.findViewById<TextView>(R.id.detail_estado).text = "${estadoEmoji(estado)} $estado"
    }

    protected fun addField(container: LinearLayout, label: String, value: String?) {
        if (value.isNullOrEmpty()) return
        val v = LayoutInflater.from(requireContext()).inflate(R.layout.item_detail_field, container, false)
        v.findViewById<TextView>(R.id.field_label).text = label
        v.findViewById<TextView>(R.id.field_value).text = value
        container.addView(v)
    }

    protected fun addDivider(container: LinearLayout) {
        val d = View(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1
            ).also { it.setMargins(0, 12, 0, 12) }
            setBackgroundColor(0xFFD6D0C4.toInt())
        }
        container.addView(d)
    }
}
