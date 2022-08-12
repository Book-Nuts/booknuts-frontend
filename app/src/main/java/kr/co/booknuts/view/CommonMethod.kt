package kr.co.booknuts.view

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object CommonMethod{
    // 키보드 내리기
    fun hideKeyboard(editText: EditText, context: Context) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    // 2개 키보드 숨길 때
    fun hideKeyboards(editText1: EditText, editText2: EditText, context: Context) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText1.windowToken, 0)
        imm.hideSoftInputFromWindow(editText2.windowToken, 0)
    }
}