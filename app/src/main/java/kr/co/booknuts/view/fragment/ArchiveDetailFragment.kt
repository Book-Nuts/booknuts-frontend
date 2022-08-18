package kr.co.booknuts.view.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.co.booknuts.view.activity.MainActivity
import kr.co.booknuts.view.activity.PostDetailActivity
import kr.co.booknuts.R
import kr.co.booknuts.data.remote.DeleteResult
import kr.co.booknuts.data.remote.MyArchive
import kr.co.booknuts.view.adapter.MyArchivePostListAdapter
import kr.co.booknuts.data.remote.PostDetail
import kr.co.booknuts.databinding.FragmentArchiveDetailBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import kr.co.booknuts.view.CommonMethod.closeFragment
import kr.co.booknuts.view.adapter.fragment.MyFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArchiveDetailFragment : Fragment() {

    private var mBinding: FragmentArchiveDetailBinding? = null
    private val binding get() = mBinding!!
    private val fragmentMy by lazy { MyFragment() }

    lateinit var recyclerView: RecyclerView
    private var accessToken: String? = null
    //private var data: Array<String?>? = null
    private var data: MyArchive? = null
    var postCnt: Int = 0

    private var archivePostDataArray: ArrayList<PostDetail>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentArchiveDetailBinding.inflate(inflater, container, false)

        // 아카이브 아이디 가져오기
        if(arguments != null) {
            data = arguments?.getSerializable("archiveDetailData") as MyArchive
        }

        binding.imgArchiveBg.setColorFilter(Color.parseColor("#aaaaaa"), PorterDuff.Mode.MULTIPLY);

        binding.myDetailImgBack.setOnClickListener{
            (activity as MainActivity).changeFragment(fragmentMy)
        }

        binding.toolbar.inflateMenu(R.menu.menu_del_mod)
        binding.toolbar.setOnMenuItemClickListener {
            optionItemClicked(it)
        }

        // 로컬에 저장된 토큰
        val pref = this.activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        accessToken = pref?.getString("accessToken", null).toString()

        RetrofitBuilder.myApi.getMyArchiveDetail(accessToken, data?.archiveId).enqueue(object:
            Callback<ArrayList<PostDetail>> {
            override fun onResponse(call: Call<ArrayList<PostDetail>>, response: Response<ArrayList<PostDetail>>) {
                archivePostDataArray = response.body()
                binding.textArchiveTitle.text = data?.title
                binding.textArchiveContent.text = data?.content
                Glide.with(binding.imgArchiveBg.context)
                    .load(data?.imgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .fitCenter()
                    .into(binding.imgArchiveBg)

                if(archivePostDataArray?.size != null) {
                    postCnt = archivePostDataArray?.size!!
                    recyclerView = binding.rvPost
                    recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
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
                //Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })
        return binding.root
    }

    // 옵션 메뉴 넣기
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_del_mod, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // 옵션 메뉴별 동작
    private fun optionItemClicked(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_mod -> {
                Log.d("Archive Modification", "Clicked")
                //modifyPost()
                super.onOptionsItemSelected(item)
            }
            R.id.menu_delete -> {
                Log.d("Archive Delete", "Clicked")
                deleteArchive()
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteArchive() {
        RetrofitBuilder.archiveApi.deleteArchive(accessToken, data?.archiveId).enqueue(object:
            Callback<DeleteResult> {
            override fun onResponse(call: Call<DeleteResult>, response: Response<DeleteResult>) {
                if(response.isSuccessful) {
                    Log.d("Archive Delete Success", "${response.body()?.result}")
                    (activity as MainActivity).changeFragment(fragmentMy);
                } else {
                    when(response.code()){
                        400 -> Log.d("Delete Archive Error", "No Permission")
                        404 -> Log.d("Delete Archive Error", "Archive Id is Not Exist")
                    }
                }
            }
            override fun onFailure(call: Call<DeleteResult>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                //Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }
}