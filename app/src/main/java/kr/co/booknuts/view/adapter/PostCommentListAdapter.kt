package kr.co.booknuts.view.adapter

import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kr.co.booknuts.data.remote.Comment
import kr.co.booknuts.databinding.ItemPostCommentBinding

class PostCommentListAdapter(private val dataList: Array<Comment>?) : RecyclerView.Adapter<PostCommentListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // 책 개수
    override fun getItemCount(): Int {
        if (dataList != null) {
            return dataList.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dataList?.get(position)?.let { holder?.bind(it) }
        holder.binding.textDelete.setOnClickListener{
            deleteClickListener.onClick(it, position)
        }
    }

    interface OnDeleteClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setDeleteClickListener(onDeleteClickListener: OnDeleteClickListener){
        this.deleteClickListener = onDeleteClickListener
    }

    private lateinit var deleteClickListener: OnDeleteClickListener

inner class ViewHolder(val binding: ItemPostCommentBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Comment) {
            if(item.writer == null) {
                binding.textCommentNickname.text = "(삭제)"
                binding.textCommentNickname.setTextColor(Color.parseColor("#FF707070"))
            } else binding.textCommentNickname.text = item.writer
            binding.textComment.text = item.content
            binding.textCommentDate.text = item.createdDate

            /*Glide.with(bookImg.context)
                .load(item.thumbnail)
                .placeholder(R.drawable.img_book_cover_default)
                .error(R.drawable.img_book_cover_default)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .fitCenter()
                .into(bookImg)*/
        }
    }
}