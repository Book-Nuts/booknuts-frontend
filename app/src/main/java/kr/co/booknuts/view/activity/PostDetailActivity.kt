package kr.co.booknuts.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.co.booknuts.R
import kr.co.booknuts.data.remote.PostDetail
import kr.co.booknuts.databinding.ActivityPostDetailBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetailActivity : AppCompatActivity() {

    val binding by lazy { ActivityPostDetailBinding.inflate(layoutInflater) }
    var data: PostDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 로컬에 저장된 토큰
        val pref = getSharedPreferences("authToken", AppCompatActivity.MODE_PRIVATE)
        val savedToken = pref?.getString("Token", null)

        var boardId = intent.getIntExtra("id", -1)?.toLong()

        Log.d("Board ID", "" + boardId)

        binding.imgClose.setOnClickListener{
            finish()
        }

        RetrofitBuilder.api.getPostDetail(savedToken, boardId).enqueue(object:
            Callback<PostDetail> {
            override fun onResponse(call: Call<PostDetail>, response: Response<PostDetail>) {
                data = response.body()
                Log.d("Board Detail Get Test", "" + data.toString())
                //Toast.makeText(this@PostDetailActivity, "통신 성공", Toast.LENGTH_SHORT).show()

                binding.textWriter.text = data?.writer + " 님의 게시글"
                binding.textTitle.text = data?.title
                binding.textNickname.text = data?.writer
                binding.textContent.text = data?.content
                binding.textDate.text = data?.createdDate
                binding.textBookTitle.text = data?.bookTitle
                binding.textAuthor.text = data?.bookAuthor
                binding.textGenre.text = data?.bookGenre
                binding.textNutsCnt.text = data?.nutsCnt.toString()
                binding.textHeartCnt.text = data?.heartCnt.toString()
                binding.textArchiveCnt.text = data?.archiveCnt.toString()
                binding.textGenre.text = data?.bookGenre
                val imgUser: ImageView = binding.imgUser
                var profileId = data?.writer?.length?.rem(5)
                when (profileId) {
                    0 -> imgUser.setImageResource(R.drawable.img_user1)
                    1 -> imgUser.setImageResource(R.drawable.img_user2)
                    2 -> imgUser.setImageResource(R.drawable.img_user3)
                    3 -> imgUser.setImageResource(R.drawable.img_user4)
                    4 -> imgUser.setImageResource(R.drawable.img_user5)
                }
                Glide.with(this@PostDetailActivity)
                    .load(data?.bookImgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .fitCenter()
                    .into(binding.imgBook)

                binding.imgArchiveBox.setOnClickListener {
                    var intent = Intent(this@PostDetailActivity, ArchivePopUpActivity::class.java)
                    intent.putExtra("id", data?.boardId)
                    Log.d("Board ID", "" + data?.boardId)
                    startActivity(intent)
                }
            }
            override fun onFailure(call: Call<PostDetail>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                //Toast.makeText(this@PostDetailActivity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }
}