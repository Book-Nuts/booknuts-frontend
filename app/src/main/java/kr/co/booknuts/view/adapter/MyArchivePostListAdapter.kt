package kr.co.booknuts.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.co.booknuts.R
import kr.co.booknuts.data.remote.PostDetail
import kr.co.booknuts.databinding.MyArchiveDetailItemBinding

class MyArchivePostListAdapter(private val dataList: ArrayList<PostDetail>?) : RecyclerView.Adapter<MyArchivePostListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MyArchiveDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // 게시글 개수
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
        holder.binding.linearPost.setOnClickListener{
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

inner class ViewHolder(val binding: MyArchiveDetailItemBinding): RecyclerView.ViewHolder(binding.root) {
        private val bookImg: ImageView = binding.imgBook
        fun bind(item: PostDetail) {
            Glide.with(bookImg.context)
                .load(item.bookImgUrl)
                .placeholder(R.drawable.img_book_cover_default)
                .error(R.drawable.img_book_cover_default)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .fitCenter()
                .into(bookImg)
        }
    }
}
