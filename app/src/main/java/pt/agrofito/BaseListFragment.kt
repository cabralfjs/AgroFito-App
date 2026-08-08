package pt.agrofito

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

abstract class BaseListFragment : Fragment() {

    protected var recycler: RecyclerView? = null
    protected var searchInput: EditText? = null
    protected var filterRow: LinearLayout? = null
    protected var countText: TextView? = null
    protected var loadingLayout: LinearLayout? = null
    protected var loadingText: TextView? = null

    abstract fun headerTitle(): String
    abstract fun headerColor(): Int
    abstract fun setupFilters()
    abstract fun applyFilters()
    abstract fun provideAdapter(): RecyclerView.Adapter<*>
    abstract fun isDataLoaded(): Boolean
    abstract fun loadData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            view.findViewById<TextView>(R.id.header_title)?.text = headerTitle()
            view.findViewById<LinearLayout>(R.id.list_header)?.setBackgroundColor(headerColor())

            searchInput   = view.findViewById(R.id.search_input)
            filterRow     = view.findViewById(R.id.filter_row)
            countText     = view.findViewById(R.id.count_text)
            loadingLayout = view.findViewById(R.id.loading_layout)
            loadingText   = view.findViewById(R.id.loading_text)
            recycler      = view.findViewById(R.id.recycler)

            recycler?.layoutManager = LinearLayoutManager(requireContext())
            recycler?.adapter = provideAdapter()

            searchInput?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) { applyFilters() }
                override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
                override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            })

            setupFilters()

            if (isDataLoaded()) applyFilters() else loadData()

        } catch (e: Exception) {
            Log.e("AgroFito", "onViewCreated error: ${e.message}", e)
        }
    }

    protected fun showLoading(msg: String) {
        if (!isAdded) return
        activity?.runOnUiThread {
            loadingLayout?.visibility = View.VISIBLE
            loadingText?.text = msg
            recycler?.visibility = View.GONE
        }
    }

    protected fun showList() {
        if (!isAdded) return
        activity?.runOnUiThread {
            loadingLayout?.visibility = View.GONE
            recycler?.visibility = View.VISIBLE
        }
    }

    protected fun openDetail(fragment: Fragment) {
        if (!isAdded) return
        try {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null).commit()
        } catch (e: Exception) {
            Log.e("AgroFito", "openDetail error: ${e.message}", e)
        }
    }

    protected fun makeSpinner(options: List<String>, onChange: () -> Unit): Spinner {
        val sp = Spinner(requireContext())
        sp.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            .also { it.marginEnd = 6 }
        sp.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, options)
            .also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                if (isAdded) onChange()
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }
        return sp
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycler = null; searchInput = null; filterRow = null
        countText = null; loadingLayout = null; loadingText = null
    }
}
