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
import kr.co.booknuts.databinding.ItemHomeRecyclerviewBinding

class BoardListAdapter(private val dataList: ArrayList<Post>?) : RecyclerView.Adapter<BoardListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        holder.binding.imgArchiveBox.setOnClickListener{
            itemClickListenerArchive.onClick(it, position)
        }
        holder.binding.imgNuts.setOnClickListener{
            itemClickListenerNuts.onClick(it, position)
        }
        holder.binding.imgHeart.setOnClickListener{
            itemClickListenerHeart.onClick(it, position)
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }

    interface OnItemClickListenerArchive {
        fun onClick(v: View, position: Int)
    }
    fun setItemClickListenerArchive(onItemClickListenerArchive: OnItemClickListenerArchive){
        this.itemClickListenerArchive = onItemClickListenerArchive
    }

    interface OnItemClickListenerNuts {
        fun onClick(v: View, position: Int)
    }
    fun setItemClickListenerNuts(onItemClickListenerNuts: OnItemClickListenerNuts){
        this.itemClickListenerNuts = onItemClickListenerNuts
    }

    interface OnItemClickListenerHeart {
        fun onClick(v: View, position: Int)
    }
    fun setItemClickListenerHeart(onItemClickListenerHeart: OnItemClickListenerHeart){
        this.itemClickListenerHeart = onItemClickListenerHeart
    }

    private lateinit var itemClickListener: OnItemClickListener
    private lateinit var itemClickListenerArchive: OnItemClickListenerArchive
    private lateinit var itemClickListenerNuts: OnItemClickListenerNuts
    private lateinit var itemClickListenerHeart: OnItemClickListenerHeart

    inner class ViewHolder(val binding: ItemHomeRecyclerviewBinding): RecyclerView.ViewHolder(binding.root) {

        private val title: TextView = binding.boardTextTitle
        private val writer: TextView = binding.boardTextWriter
        private val bookImg: ImageView = binding.boardImgBookImg
        private val bookTitle: TextView = binding.boardTextBookTitle
        private val content: TextView = binding.boardTextContent
        private val imgUser: ImageView = binding.imgUser
        private val imgHeart: ImageView = binding.imgHeart
        private val imgNuts: ImageView = binding.imgNuts
        private val archiveCnt: TextView = binding.textArchiveCnt
        private val nutsCnt: TextView = binding.textNutsCnt
        private val heartCnt: TextView = binding.textHeartCnt
        fun bind(item: Post) {
            title.text = item.title
            bookTitle.text = item.bookTitle
            writer.text = item.writer
            var profileId = writer.text.length.rem(5)
            content.text = item.content
            archiveCnt.text = item.archiveCnt.toString()
            nutsCnt.text = item.nutsCnt.toString()
            heartCnt.text = item.heartCnt.toString()
            if(item.isHeart == true){
                imgHeart.setImageResource(R.drawable.icon_heart_fill)
            }
            if(item.isNuts == true){
                // 넛츠 활성화 이미지 없음 (하트로 대체)
                imgNuts.setImageResource(R.drawable.icon_heart_fill)
            }
            when (profileId) {
                0 -> imgUser.setImageResource(R.drawable.img_user1)
                1 -> imgUser.setImageResource(R.drawable.img_user2)
                2 -> imgUser.setImageResource(R.drawable.img_user3)
                3 -> imgUser.setImageResource(R.drawable.img_user4)
                4 -> imgUser.setImageResource(R.drawable.img_user5)
            }
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
