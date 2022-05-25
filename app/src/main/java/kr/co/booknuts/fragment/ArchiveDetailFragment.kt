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
import kr.co.booknuts.MainActivity
import kr.co.booknuts.PostDetailActivity
import kr.co.booknuts.R
import kr.co.booknuts.adapter.MyArchiveListAdapter
import kr.co.booknuts.adapter.MyArchivePostListAdapter
import kr.co.booknuts.adapter.MySeriesPostListAdapter
import kr.co.booknuts.data.MyArchive
import kr.co.booknuts.data.PostDetail
import kr.co.booknuts.databinding.FragmentArchiveDetailBinding
import kr.co.booknuts.databinding.FragmentMySeriesDetailBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArchiveDetailFragment : Fragment() {

    private var mBinding: FragmentArchiveDetailBinding? = null
    private val binding get() = mBinding!!
    private val fragmentMy by lazy {MyFragment()}

    lateinit var recyclerView: RecyclerView
    private var savedToken: String? = null
    private var id: Int? = null
    var postCnt: Int = 0

    private var archivePostDataArray: ArrayList<PostDetail>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentArchiveDetailBinding.inflate(inflater, container, false)

        // 아카이브 아이디 가져오기
        if(arguments != null) {
            id = arguments!!.getInt("id")
            Log.d("Arguments id", ""+id)
        }

        binding.imgArchiveBg.setColorFilter(Color.parseColor("#aaaaaa"), PorterDuff.Mode.MULTIPLY);

        binding.myDetailImgBack.setOnClickListener{
            (activity as MainActivity).changeFragment(fragmentMy);
        }

        // 로컬에 저장된 토큰
        val pref = this.activity?.getSharedPreferences("authToken", AppCompatActivity.MODE_PRIVATE)
        savedToken = pref?.getString("Token", null).toString()

        RetrofitBuilder.api.getMyArchiveDetail(savedToken, id).enqueue(object:
            Callback<ArrayList<PostDetail>> {
            override fun onResponse(call: Call<ArrayList<PostDetail>>, response: Response<ArrayList<PostDetail>>) {
                archivePostDataArray = response.body()
                Log.d("Archive Post List", "data : " + archivePostDataArray?.get(0)?.boardId)
                Toast.makeText(activity, "통신 성공", Toast.LENGTH_SHORT).show()

                if(archivePostDataArray?.size != null) {
                    postCnt = archivePostDataArray?.size!!
                    recyclerView = binding.rvPost
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    val adapter: MyArchivePostListAdapter = MyArchivePostListAdapter(archivePostDataArray)
                    if (postCnt != 0)
                        Log.d("DataArray size is not 0", "" + postCnt)
                    recyclerView.adapter = adapter
                    adapter.setItemClickListener(object: MyArchivePostListAdapter.OnItemClickListener{
                        override fun onClick(v: View, position: Int) {
                            var intent = Intent(activity, PostDetailActivity::class.java)
                            intent.putExtra("id", archivePostDataArray?.get(position)?.boardId)
                            Log.d("Board ID", "" + archivePostDataArray?.get(position)?.boardId)
                            startActivity(intent)
                        }
                    })
                }

                binding.textPostCount.text = postCnt.toString() + "개의 포스트"
            }
            override fun onFailure(call: Call<ArrayList<PostDetail>>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })
        return binding.root
    }
}