package kr.co.booknuts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.booknuts.databinding.ActivityMakeSeriesFirstBinding

class MakeSeriesFirstActivity : AppCompatActivity() {

    val binding by lazy {ActivityMakeSeriesFirstBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.textGoNext.setOnClickListener {
            val intent = Intent(this@MakeSeriesFirstActivity, MakeSeriesSecondActivity::class.java)
            startActivity(intent)
        }
    }
}