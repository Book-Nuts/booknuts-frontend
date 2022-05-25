package kr.co.booknuts.adapter

import android.content.Context
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
import kr.co.booknuts.data.Post
import kr.co.booknuts.databinding.HomeRecyclerviewItemBinding

class BoardListAdapter(private val dataList: ArrayList<Post>?) : RecyclerView.Adapter<BoardListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        holder.binding.homeLinearPostItem.setOnClickListener{
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

    inner class ViewHolder(val binding: HomeRecyclerviewItemBinding): RecyclerView.ViewHolder(binding.root) {
        private val title: TextView = binding.boardTextTitle
        private val writer: TextView = binding.boardTextWriter
        private val bookImg: ImageView = binding.boardImgBookImg
        private val bookTitle: TextView = binding.boardTextBookTitle
        private val content: TextView = binding.boardTextContent
        fun bind(item: Post) {
            title.text = item.title
            bookTitle.text = item.bookTitle
            writer.text = item.writer
            content.text = item.content
            Glide.with(bookImg.context)
                .load(item.bookImgUrl)
                .placeholder(R.drawable.icon_search)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.img_user3)
                .fallback(R.drawable.img_user2)
                .fitCenter()
                .into(bookImg)
        }
    }
}
