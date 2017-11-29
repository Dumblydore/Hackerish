package me.mauricee.hackerish.widget

import android.content.Context
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import com.amulyakhare.textdrawable.TextDrawable
import com.jakewharton.rxbinding2.view.RxView
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import kotlinx.android.synthetic.main.view_story_card.view.*
import me.mauricee.hackerish.R
import me.mauricee.hackerish.model.Story

class StoryCardView : CardView {

    companion object {
        private val hnPattern = Regex("(^(\\w+ )(HN:))")
        private val builder = TextDrawable.builder()
        private val circleTransform = CircleTransform()
    }

    var story: Story? = null
        set(value) {
            if (value == null)
                return

            title.text = value.title.replace(hnPattern, "")
            subtitle.text = "by ${value.user}"

            if (value.title.contains(hnPattern)) {
                host?.text = hnPattern.find(value.title)?.value?.replace(":", "") ?: ""
            } else {
                host?.text = value.url.host?.replace("www.", "") ?: ""
            }
            if (value.url == Uri.EMPTY) {
                icon?.visibility = View.GONE
            } else if (icon != null) {
                icon?.visibility = View.VISIBLE
            }
        }

    var faviconUri: Uri = Uri.EMPTY
        set(value) {

            Picasso.with(context).load(value)
                    .fit()
                    .error(builder.buildRound(value.host?.substring(0, 1)?.toUpperCase() ?: "?",
                            ContextCompat.getColor(context, R.color.colorPrimary)))
                    .transform(circleTransform)
                    .into(favicon)

        }

    var iconUri: Uri = Uri.EMPTY
        set(value) {
            Picasso.with(context).load(value)
                    .error(R.drawable.ic_no_image)
                    .fit()
                    .centerCrop()
                    .into(icon)
        }

    val clicks: Observable<Boolean>
        get() {
            var a = RxView.clicks(this).map { false }
            if (icon != null) {
                a = a.mergeWith(RxView.clicks(icon!!).map { true })
            }
            return a
        }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.view_story_card, this)
    }

    fun clear() {
        favicon.setImageResource(0)
        icon.setImageResource(0)
    }
}