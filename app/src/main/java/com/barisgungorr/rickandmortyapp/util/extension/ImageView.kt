package com.barisgungorr.rickandmortyapp.util.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.load(
    imageUrl: String,
    size: Size? = null
) {
    var requestOptions = RequestOptions()
    if (size != null) {
        requestOptions = requestOptions.override(width, height)
    }
    Glide.with(this.context)
        .load(imageUrl)
        .apply(requestOptions)
        .into(this)
}
data class Size(val width: Int, val height: Int)

fun hideKeyboard(context: Context, view : View){
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)

}