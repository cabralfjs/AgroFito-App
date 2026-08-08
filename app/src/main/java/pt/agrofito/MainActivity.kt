package pt.agrofito

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val nav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        if (savedInstanceState == null) loadFragment(UsosFragment())
        nav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_usos     -> { loadFragment(UsosFragment()); true }
                R.id.nav_produtos -> { loadFragment(ProdutosFragment()); true }
                else -> false
            }
        }
    }
    fun loadFragment(f: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, f).commit()
    }
}
