package kr.co.booknuts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.booknuts.fragment.*

class MainActivity : AppCompatActivity() {

    private val fragmentHome by lazy {HomeFragment()}
    private val fragmentSearch by lazy { SearchFragment() }
    private val fragmentWrite by lazy { WriteFragment() }
    private val fragmentDebate by lazy {DebateFragment()}
    private val fragmentMy by lazy { MyFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNavigationBar()
    }

    private fun initNavigationBar() {
        bnv_main.run {
            setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.home -> {
                        changeFragment(fragmentHome)
                    }
                    R.id.search -> {
                        changeFragment(fragmentSearch)
                    }
                    R.id.write -> {
                        changeFragment(fragmentWrite)
                    }
                    R.id.debate -> {
                        changeFragment(fragmentDebate)
                    }
                    R.id.my -> {
                        changeFragment(fragmentMy)
                    }
                }
                true
            }
            selectedItemId = R.id.home
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_container, fragment)
            .commit()
    }

}