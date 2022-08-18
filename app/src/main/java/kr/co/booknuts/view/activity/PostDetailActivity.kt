package kr.co.booknuts.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_post_detail.*
import kr.co.booknuts.R
import kr.co.booknuts.data.remote.*
import kr.co.booknuts.databinding.ActivityPostDetailBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import kr.co.booknuts.view.fragment.PostCommentFragment
import kr.co.booknuts.view.fragment.PostEditFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetailActivity : AppCompatActivity() {

    val binding by lazy { ActivityPostDetailBinding.inflate(layoutInflater) }
    var boardId: Long? = null
    var accessToken: String? = null
    var nickname: String? = null
    var writer: String? = null
    var postData: PostDetail? = null
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

        binding.swipe.setOnRefreshListener{
            getPostData()
            binding.swipe.isRefreshing = false
        }

        getPostData()

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onResume() {
        super.onResume()
        binding.imgArchiveBox.setOnClickListener {
            var intent = Intent(this@PostDetailActivity, ArchivePopUpActivity::class.java)
            intent.putExtra("id", postData?.boardId)
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
        if(nickname == writer) menuInflater.inflate(R.menu.menu_del_mod, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // 옵션 메뉴별 동작
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_mod -> {
                modifyPost()
                return super.onOptionsItemSelected(item)
            }
            R.id.menu_delete -> {
                deletePost()
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun getPostData() {
        RetrofitBuilder.boardApi.getPostDetail(accessToken, boardId).enqueue(object:
            Callback<PostDetail> {
            override fun onResponse(call: Call<PostDetail>, response: Response<PostDetail>) {
                postData = response.body()
                isHeartClicked = postData?.isHeart
                isNutsClicked = postData?.isNuts
                if(isHeartClicked == true){
                    binding.imgHeart.setImageResource(R.drawable.icon_heart_fill)
                }
                if(isHeartClicked == true){
                    binding.imgNuts.setImageResource(R.drawable.icon_heart_fill)
                }
                binding.textWriter.text = postData?.writer + " 님의 게시글"
                binding.textTitle.text = postData?.title
                binding.textNickname.text = postData?.writer
                binding.textContent.text = postData?.content
                binding.textDate.text = postData?.createdDate
                binding.textBookTitle.text = postData?.bookTitle
                binding.textAuthor.text = postData?.bookAuthor
                binding.textGenre.text = postData?.bookGenre
                binding.textNutsCnt.text = postData?.nutsCnt.toString()
                binding.textHeartCnt.text = postData?.heartCnt.toString()
                binding.textArchiveCnt.text = postData?.archiveCnt.toString()
                binding.textGenre.text = postData?.bookGenre
                val imgUser: ImageView = binding.imgUser
                var profileId = postData?.writer?.length?.rem(5)
                when (profileId) {
                    0 -> imgUser.setImageResource(R.drawable.img_user1)
                    1 -> imgUser.setImageResource(R.drawable.img_user2)
                    2 -> imgUser.setImageResource(R.drawable.img_user3)
                    3 -> imgUser.setImageResource(R.drawable.img_user4)
                    4 -> imgUser.setImageResource(R.drawable.img_user5)
                }
                Glide.with(this@PostDetailActivity)
                    .load(postData?.bookImgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .fitCenter()
                    .into(binding.imgBook)
            }
            override fun onFailure(call: Call<PostDetail>, t: Throwable) {
                Log.d("Approach Fail", "Wrong server approach in getPostDetail")
            }
        })
    }

    private fun deletePost() {
        RetrofitBuilder.boardApi.deletePost(accessToken, postData?.boardId).enqueue(object: Callback<DeleteResult> {
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

    private fun modifyPost() {
        val bundle = Bundle()
        bundle.putString("title", postData?.title)
        bundle.putString("content", postData?.content)
        bundle.putString("bookImgUrl", postData?.bookImgUrl)
        bundle.putLong("boardId", postData?.boardId!!)
        //bundle.putSerializable("postData", postData)
        var fragment = PostEditFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.linear_post_detail, fragment).commit()
    }

    private fun heartClickedHandling() {
        RetrofitBuilder.reactionApi.putHeart(accessToken, postData?.boardId).enqueue(object: Callback<HeartResult> {
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
        RetrofitBuilder.reactionApi.putNuts(accessToken, postData?.boardId)
            .enqueue(object : Callback<NutsResult> {
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
