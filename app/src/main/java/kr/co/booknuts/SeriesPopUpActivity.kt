package kr.co.booknuts

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import kr.co.booknuts.databinding.ActivitySeriesPopUpBinding

class SeriesPopUpActivity : AppCompatActivity() {

    val binding by lazy { ActivitySeriesPopUpBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.textCreateSeries.setOnClickListener {
            val intent = Intent(this@SeriesPopUpActivity, MakeSeriesFirstActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}