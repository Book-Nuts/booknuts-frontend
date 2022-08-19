package kr.co.booknuts.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.co.booknuts.R
import kr.co.booknuts.data.remote.Post
import kr.co.booknuts.databinding.*
import kr.co.booknuts.view.activity.MakeSeriesFirstActivity
import kr.co.booknuts.view.activity.MakeSeriesFirstActivity.Companion.parentBinding
import kr.co.booknuts.view.activity.MakeSeriesFirstActivity.Companion.postClickedList
import kr.co.booknuts.view.activity.MakeSeriesFirstActivity.Companion.postDataArray
import kr.co.booknuts.view.activity.MakeSeriesFirstActivity.Companion.seriesCount

class MakeSeriesListAdapter(private val dataList: ArrayList<Post>?) : RecyclerView.Adapter<MakeSeriesListAdapter.ViewHolder>() {

    var binding: ItemSeriesMakeBinding? = null
    //private val userCheckBoxStatus = arrayListOf<UserCheckStatus>()
    //private var mSelectedItems = SparseBooleanArray(0);

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemSeriesMakeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding!!)
    }

    // 시리즈 게시글 개수
    override fun getItemCount(): Int {
        if (dataList != null) {
            //Log.d("Adapter", "dataListSize " + dataList.size)
            return dataList.size
        }
        //Log.d("Adapter", "dataListSize zero")
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dataList?.get(position)?.let { holder?.bind(it) }
//        holder.binding.linearItem.setOnClickListener{
//            itemClickListener.onClick(it, position)
//        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    inner class ViewHolder(val binding: ItemSeriesMakeBinding): RecyclerView.ViewHolder(binding.root) {
        private val title: TextView = binding.textTitle
        private val date: TextView = binding.textDate
        private val bookImg: ImageView = binding.imgBook
        fun bind(item: Post) {
            title.text = item.title
            date.text = item.bookTitle
            Glide.with(bookImg.context)
                .load(item.bookImgUrl)
                .placeholder(R.drawable.img_book_cover_default)
                .error(R.drawable.img_book_cover_default)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .fitCenter()
                .into(bookImg)

            // 체크박스 선택
            binding.checkSeries.setOnCheckedChangeListener { button, isChecked ->
                // 선택
                if (isChecked) {
                    seriesCount++
                    // 1개면 보이게
                    if (seriesCount == 1) parentBinding?.cardView?.visibility = View.VISIBLE
                    MakeSeriesFirstActivity.postClickedList.add(postDataArray.get(adapterPosition).boardId)
                }
                // 해제
                else {
                    seriesCount--
                    // 0개면 안 보이게
                    if (seriesCount == 0) parentBinding?.cardView?.visibility = View.INVISIBLE
                    MakeSeriesFirstActivity.postClickedList.remove(postDataArray.get(adapterPosition).boardId)
                }
                Log.d("MAKE_SERIES", postClickedList.toString())
                parentBinding?.textSelectedPost?.text = "총 ${seriesCount}개 선택"
            }

            // 아무데나 선택
            binding.root.setOnClickListener {
                binding.checkSeries.isChecked = !binding.checkSeries.isChecked
            }
        }
    }
}
