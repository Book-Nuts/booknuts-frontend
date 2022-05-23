package kr.co.booknuts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kr.co.booknuts.databinding.ActivityMainBinding
import kr.co.booknuts.databinding.ActivitySignUpBinding
import kr.co.booknuts.fragment.*

class MainActivity : AppCompatActivity() {

    private val fragmentHome by lazy {HomeFragment()}
    private val fragmentSearch by lazy { SearchFragment() }
    private val fragmentWrite by lazy { WriteFragment() }
    private val fragmentDebate by lazy {DebateFragment()}
    private val fragmentMy by lazy { MyFragment() }

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initNavigationBar()

        val pref = getSharedPreferences("authToken", AppCompatActivity.MODE_PRIVATE)
        val editor = pref.edit()
    }

    private fun initNavigationBar() {
        binding.bnvMain.run {
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