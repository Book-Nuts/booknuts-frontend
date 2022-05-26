package kr.co.booknuts

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import kr.co.booknuts.databinding.ActivityArchivePopUpBinding
import kr.co.booknuts.databinding.ActivitySeriesPopUpBinding

class ArchivePopUpActivity : AppCompatActivity() {

    val binding by lazy { ActivityArchivePopUpBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(binding.root)

        var boardId = intent.getIntExtra("id", -1)?.toInt()

        binding.textCreateArchive.setOnClickListener {
            val intent = Intent(this@ArchivePopUpActivity, MakeArchiveActivity::class.java)
            intent.putExtra("boardId", boardId)
            startActivity(intent)
            finish()
        }
    }
}