package kr.co.booknuts.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.co.booknuts.MainActivity
import kr.co.booknuts.PostDetailActivity
import kr.co.booknuts.R
import kr.co.booknuts.adapter.BoardListAdapter
import kr.co.booknuts.adapter.MyPostListAdapter
import kr.co.booknuts.adapter.MySeriesPostListAdapter
import kr.co.booknuts.data.Post
import kr.co.booknuts.data.PostDetail
import kr.co.booknuts.databinding.FragmentMyBinding
import kr.co.booknuts.databinding.FragmentMySeriesDetailBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MySeriesDetailFragment : Fragment() {

    private var mBinding: FragmentMySeriesDetailBinding? = null
    private val binding get() = mBinding!!
    private val fragmentMy by lazy {MyFragment()}

    //private var id: Int? = null
    private var data: Array<String?>? = null
    var postCnt: Int = 0

    private var savedToken: String? = null
    private var seriesPostDataArray: ArrayList<PostDetail>? = null

    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentMySeriesDetailBinding.inflate(inflater, container, false)

        // 시리지 아이디 가져오기
        /*if(arguments != null) {
            id = arguments!!.getInt("id")
            Log.d("Arguments id", ""+id)
        }*/

        if(arguments != null) {
            data = arguments?.getStringArray("data") as Array<String?>
            Log.d("Arguments", data.toString())
        }

        val pref = this.activity?.getSharedPreferences("authToken", AppCompatActivity.MODE_PRIVATE)
        savedToken = pref?.getString("Token", null).toString()

        binding.myImgBg.setColorFilter(Color.parseColor("#aaaaaa"), PorterDuff.Mode.MULTIPLY);
        //binding.myImgSeriesDetail.bringToFront()

        binding.myDetailImgBack.setOnClickListener{
            (activity as MainActivity).changeFragment(fragmentMy);
        }

        // 서버에서 시리즈 세부 데이터 받아오기
        RetrofitBuilder.api.getMySeriesDetailPost(savedToken, data?.get(0)?.toInt()).enqueue(object:
            Callback<ArrayList<PostDetail>> {
            override fun onResponse(call: Call<ArrayList<PostDetail>>, response: Response<ArrayList<PostDetail>>) {
                seriesPostDataArray = response.body()
                //Log.d("Series Detail Post List", "data : " + seriesPostDataArray?.get(0)?.boardId)
                Toast.makeText(activity, "통신 성공", Toast.LENGTH_SHORT).show()
                binding.textSeriesTitle.text = data?.get(1)
                binding.textSeriesContent.text = data?.get(2)
                Glide.with(binding.myImgSeriesDetail.context)
                    .load(data?.get(3))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.img_user3)
                    .fitCenter()
                    .into(binding.myImgSeriesDetail)

                if(seriesPostDataArray?.size != null) {
                    postCnt = seriesPostDataArray?.size!!
                    recyclerView = binding.mySeriesDetailRvPost
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    val adapter: MySeriesPostListAdapter = MySeriesPostListAdapter(seriesPostDataArray)
                    if (postCnt != 0)
                        Log.d("DataArray size is not 0", "" + postCnt)
                    recyclerView.adapter = adapter
                    adapter.setItemClickListener(object: MySeriesPostListAdapter.OnItemClickListener{
                        override fun onClick(v: View, position: Int) {
                            var intent = Intent(activity, PostDetailActivity::class.java)
                            intent.putExtra("id", seriesPostDataArray?.get(position)?.boardId)
                            Log.d("Board ID", "" + seriesPostDataArray?.get(position)?.boardId)
                            startActivity(intent)
                        }
                    })
                }

                binding.mySeriesDetailTextPostCount.text = postCnt.toString() + "개의 포스트"
            }
            override fun onFailure(call: Call<ArrayList<PostDetail>>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }
}