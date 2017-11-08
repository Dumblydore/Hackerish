package me.mauricee.hackerish.common

import android.content.Context
import android.os.Build
import android.support.v7.widget.AppCompatTextView
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.AttributeSet

class HtmlTextView : AppCompatTextView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun setText(text: CharSequence?, type: BufferType?) {

    val htmlText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text.toString(), Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(text.toString())
        }
        movementMethod = LinkMovementMethod.getInstance()
        super.setText(htmlText, type)
    }


}