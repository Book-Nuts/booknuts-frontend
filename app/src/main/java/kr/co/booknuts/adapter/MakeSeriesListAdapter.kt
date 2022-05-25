package kr.co.booknuts.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.co.booknuts.R
import kr.co.booknuts.data.MySeries
import kr.co.booknuts.data.Post
import kr.co.booknuts.data.UserCheckStatus
import kr.co.booknuts.databinding.*

class MakeSeriesListAdapter(private val dataList: ArrayList<Post>?) : RecyclerView.Adapter<MakeSeriesListAdapter.ViewHolder>() {

    var binding: SeriesMakeItemBinding? = null
    //private val userCheckBoxStatus = arrayListOf<UserCheckStatus>()
    //private var mSelectedItems = SparseBooleanArray(0);

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = SeriesMakeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding!!)
    }

    // 시리즈 게시글 개수
    override fun getItemCount(): Int {
        if (dataList != null) {
            Log.d("Adapter", "dataListSize " + dataList.size)
            return dataList.size
        }
        Log.d("Adapter", "dataListSize zero")
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dataList?.get(position)?.let { holder?.bind(it) }
        holder.binding.linearItem.setOnClickListener{
            itemClickListener.onClick(it, position)
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener: OnItemClickListener

    inner class ViewHolder(val binding: SeriesMakeItemBinding): RecyclerView.ViewHolder(binding.root) {
        private val title: TextView = binding.textTitle
        private val date: TextView = binding.textDate
        private val bookImg: ImageView = binding.imgBook
        fun bind(item: Post) {
            title.text = item.title
            date.text = item.bookTitle
            Glide.with(bookImg.context)
                .load(item.bookImgUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.img_user3)
                .fitCenter()
                .into(bookImg)
        }
    }
}
