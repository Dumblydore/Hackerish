package me.mauricee.hackerish.widget

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.text.method.ScrollingMovementMethod
import android.util.AttributeSet


class SlidingTextView : AppCompatTextView {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}



    init {
//        movementMethod = ScrollingMovementMethod()
    }


}