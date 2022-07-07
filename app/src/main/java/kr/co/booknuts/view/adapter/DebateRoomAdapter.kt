package kr.co.booknuts.view.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.booknuts.data.remote.DebateSearchInfo
import kr.co.booknuts.databinding.ItemDebateRoomBinding
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import kr.co.booknuts.view.activity.ChatDetailActivity
import kr.co.booknuts.R

class DebateRoomAdapter() : RecyclerView.Adapter<DebateRoomAdapter.DebateRoomHolder>() {
    var listData = mutableListOf<DebateSearchInfo>()

    // 한 화면의 레이아웃 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebateRoomHolder {
        val binding = ItemDebateRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.d("ROOMLIST_ADAPTER", "onCreateViewHolder 실행")
        return DebateRoomHolder(binding)
    }

    // 아이템 레이아웃에 값 입력 후 목록에 출력
    override fun onBindViewHolder(holder: DebateRoomHolder, position: Int) {
        val debate = listData.get(position)
        Log.d("ROOMLIST_ADAPTER", "onBindViewHolder 실행")
        holder.setDebate(debate)
    }

    // 목록에 보여줄 아이템의 개수
    override fun getItemCount(): Int {
        if (listData != null) {
            Log.d("ROOMLIST_ADAPTER", "dataListSize " + listData.size + " : " + listData.toString())
            return listData.size
        }
        Log.d("ROOMLIST_ADAPTER", "dataListSize zero")
        return 0
    }

    inner class DebateRoomHolder(val binding: ItemDebateRoomBinding): RecyclerView.ViewHolder(binding.root) {
        fun setDebate(debate: DebateSearchInfo) {
            Log.d("ROOMLIST_PROCEED", "setDebate 실행")
            // ★★★ 토론장 사진 바꿀수 있게 설정하기
            val idx = debate.id.rem(2)
            when (idx) {
                0 -> binding.imgDebate.setImageResource(R.drawable.img_debate1)
                1 -> binding.imgDebate.setImageResource(R.drawable.img_debate2)
            }
            binding.imgDebate.clipToOutline = true
            binding.textTitle.text = "${debate.topic}"
            binding.textBookTitle.text = "${debate.bookTitle}"
            binding.textParticipant.text = "${debate.owner}님 외 ${debate.cons + debate.pros - 1}명"
            binding.textTime.text = "${debate.time}"
            (binding.lineCons.layoutParams as LinearLayout.LayoutParams).weight = debate.pros.toFloat()
            (binding.linePros.layoutParams as LinearLayout.LayoutParams).weight = debate.cons.toFloat()
            Log.d("DEBATEROOM_WEIGHT_CONS", (binding.lineCons.layoutParams as LinearLayout.LayoutParams).weight.toString())
            Log.d("DEBATEROOM_WEIGHT_PROS", (binding.linePros.layoutParams as LinearLayout.LayoutParams).weight.toString())

            Log.d("ROOMLIST_PROCEED", debate.toString())
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, ChatDetailActivity::class.java)
                intent.putExtra("roomId", debate.id)
                intent.putExtra("topic", debate.topic)
                intent.putExtra("title", debate.bookTitle)
                intent.putExtra("author", debate.author)
                startActivity(binding.root.context, intent, null)
            }
        }
    }
}