package kr.co.booknuts.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.booknuts.data.DebateSearchInfo
import kr.co.booknuts.databinding.ListitemDebateRoomBinding
import android.widget.LinearLayout




class DebateRoomAdapter: RecyclerView.Adapter<DebateRoomHolder>() {
    var listData = mutableListOf<DebateSearchInfo>()

    // 한 화면의 레이아웃 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebateRoomHolder {
        val binding = ListitemDebateRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            Log.d("ROOMLIST_ADAPTER", "dataListSize " + listData.size)
            return listData.size
        }
        Log.d("ROOMLIST_ADAPTER", "dataListSize zero")
        return 0
    }

}

class DebateRoomHolder(val binding: ListitemDebateRoomBinding): RecyclerView.ViewHolder(binding.root) {
    fun setDebate(debate: DebateSearchInfo) {
        Log.d("ROOMLIST_PROCEED", "setDebate 실행")
        // ★★★ 토론장 사진 바꿀수 있게 설정하기
        binding.textTitle.text = "${debate.topic}"
        binding.textBookTitle.text = "${debate.bookTitle}"
        binding.textParticipant.text = "${debate.owner}님 외 ${debate.participants - 1}명"
        binding.textTime.text = "${debate.time}전"
        (binding.lineCons.layoutParams as LinearLayout.LayoutParams).weight = debate.cons.toFloat()
        (binding.linePros.layoutParams as LinearLayout.LayoutParams).weight = debate.pros.toFloat()
        Log.d("ROOMLIST_PROCEED", debate.toString())
    }
}