package kr.co.booknuts.view.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_post_detail.*
import kr.co.booknuts.R
import kr.co.booknuts.data.remote.*
import kr.co.booknuts.databinding.ActivityPostDetailBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import kr.co.booknuts.view.adapter.BoardListAdapter
import kr.co.booknuts.view.fragment.PostCommentFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetailActivity : AppCompatActivity() {

    val binding by lazy { ActivityPostDetailBinding.inflate(layoutInflater) }
    var boardId: Long? = null
    var accessToken: String? = null
    var nickname: String? = null
    var writer: String? = null
    var data: PostDetail? = null
    var isHeartClicked: Boolean? = false
    var isNutsClicked: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 로컬에 저장된 토큰
        val pref = getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        accessToken = pref?.getString("accessToken", null)
        nickname = pref?.getString("nickname", null)

        boardId = intent.getIntExtra("id", -1).toLong()
        writer = intent.getStringExtra("writer").toString()

        binding.imgClose.setOnClickListener{
            finish()
        }

        RetrofitBuilder.boardApi.getPostDetail(accessToken, boardId).enqueue(object:
            Callback<PostDetail> {
            override fun onResponse(call: Call<PostDetail>, response: Response<PostDetail>) {
                data = response.body()
                isHeartClicked = data?.isHeart
                isNutsClicked = data?.isNuts
                if(isHeartClicked == true){
                    binding.imgHeart.setImageResource(R.drawable.icon_heart_fill)
                }
                if(isHeartClicked == true){
                    binding.imgNuts.setImageResource(R.drawable.icon_heart_fill)
                }
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
            }
            override fun onFailure(call: Call<PostDetail>, t: Throwable) {
                Log.d("Approach Fail", "Wrong server approach in getPostDetail")
            }
        })

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onResume() {
        super.onResume()
        binding.imgArchiveBox.setOnClickListener {
            var intent = Intent(this@PostDetailActivity, ArchivePopUpActivity::class.java)
            intent.putExtra("id", data?.boardId)
            startActivity(intent)
        }
        // 하트 클릭 시
        binding.imgHeart.setOnClickListener{
            isHeartClicked = !isHeartClicked!!
            heartClickedHandling()
            val heartCnt = binding.textHeartCnt.text.toString().toInt()
            if(isHeartClicked == true){
                binding.imgHeart.setImageResource(R.drawable.icon_heart_fill)
                binding.textHeartCnt.text = (heartCnt.plus(1)).toString()
            } else {
                binding.imgHeart.setImageResource(R.drawable.icon_heart_white)
                binding.textHeartCnt.text = (heartCnt.minus(1)).toString()
            }
        }

        // 넛츠 클릭 시
        binding.imgNuts.setOnClickListener{
            isNutsClicked = !isNutsClicked!!
            nutsClickedHandling()
            val nutsCnt = binding.textNutsCnt.text.toString().toInt()
            if(isNutsClicked == true){
                // 넛츠 활성화 이미지 없음 (하트로 대체)
                binding.imgNuts.setImageResource(R.drawable.icon_heart_fill)
                binding.textNutsCnt.text = (nutsCnt.plus(1)).toString()
            } else {
                binding.imgNuts.setImageResource(R.drawable.icon_nuts_b)
                binding.textNutsCnt.text = (nutsCnt.minus(1)).toString()
            }
        }

        // 댓글 클릭 시
        binding.textCommentCnt.setOnClickListener {
            val bundle = Bundle()
            bundle.putLong("boardId", boardId!!)
            var fragment = PostCommentFragment()
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.linear_post_detail, fragment).commit()
        }
    }

    // 옵션 메뉴 넣기
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("Nickname", "" + nickname)
        Log.d("Writer", "" + writer)
        if(nickname == writer) menuInflater.inflate(R.menu.menu_board_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }
    
    // 옵션 메뉴별 동작
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_delete -> {
                deletePost()
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun deletePost() {
        RetrofitBuilder.boardApi.deletePost(accessToken, data?.boardId?.toLong()).enqueue(object: Callback<DeleteResult> {
            override fun onResponse(call: Call<DeleteResult>, response: Response<DeleteResult>) {
                Log.d("Post Delete has an Response", "" + response.body()?.result)
                if (response.isSuccessful) {
                    Log.d("Post Delete Success", "" + response.body()?.result)

                    finish()
                } else if (response.errorBody() != null) {
                    when(response.code()) {
                        400 -> {
                            Log.d("Delete Permission Fail", "No Permission")
                        }
                    }
                }
            }
            override fun onFailure(call: Call<DeleteResult>, t: Throwable) {
                Log.d("Approach Fail", "Wrong server approach in deletePost")
            }
        })
    }

    private fun heartClickedHandling() {
        RetrofitBuilder.reactionApi.putHeart(accessToken, data?.boardId).enqueue(object: Callback<HeartResult> {
            override fun onResponse(call: Call<HeartResult>, response: Response<HeartResult>) {
                if (response.isSuccessful) {
                    Log.d("Heart clicked result", "" + response.body()?.result)
                }
            }
            override fun onFailure(call: Call<HeartResult>, t: Throwable) {
                Log.d("Approach Fail", "Wrong server approach in putHeart")
            }
        })
    }

    private fun nutsClickedHandling() {
        RetrofitBuilder.reactionApi.putNuts(accessToken, data?.boardId).enqueue(object: Callback<NutsResult> {
            override fun onResponse(call: Call<NutsResult>, response: Response<NutsResult>) {
                if (response.isSuccessful) {
                    Log.d("Nuts clicked result", "" + response.body()?.result)
                }
            }
            override fun onFailure(call: Call<NutsResult>, t: Throwable) {
                Log.d("Approach Fail", "Wrong server approach in putHeart")
            }
        })
    }
}