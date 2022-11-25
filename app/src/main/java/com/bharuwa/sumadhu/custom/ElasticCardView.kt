package com.bharuwa.sumadhu.custom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View.OnClickListener
import androidx.annotation.Px
import androidx.cardview.widget.CardView
import com.bharuwa.sumadhu.R

@Suppress("unused", "MemberVisibilityCanBePrivate")
class ElasticCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : CardView(context, attrs, defStyle) {

    var scale = 0.96f
    var duration = 250
    @Px
    var cornerRadius = 0f
    private var onClickListener: OnClickListener? = null
    private var onFinishListener: ElasticFinishListener? = null

    init {
        onCreate()
        when {
            attrs != null && defStyle != 0 -> getAttrs(attrs, defStyle)
            attrs != null -> getAttrs(attrs)
        }
    }

    private fun onCreate() {
        this.isClickable = true
        this.isFocusable = true
        super.setOnClickListener {
            elasticAnimation(this) {
                setDuration(this@ElasticCardView.duration)
                setScaleX(this@ElasticCardView.scale)
                setScaleY(this@ElasticCardView.scale)
                setOnFinishListener { invokeListeners() }
            }.doAction()
        }
    }

    private fun getAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ElasticLayout)
        try {
            setTypeArray(typedArray)
        } finally {
            typedArray.recycle()
        }
    }

    private fun getAttrs(attrs: AttributeSet, defStyle: Int) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.ElasticLayout,
            defStyle,
            0
        )
        try {
            setTypeArray(typedArray)
        } finally {
            typedArray.recycle()
        }
    }

    private fun setTypeArray(typedArray: TypedArray) {
        this.scale = typedArray.getFloat(R.styleable.ElasticLayout_layout_scale, this.scale)
        this.duration = typedArray.getInt(R.styleable.ElasticLayout_layout_duration, this.duration)
        this.cornerRadius =
            typedArray.getDimension(
                R.styleable.ElasticLayout_layout_cornerRadius,
                this.cornerRadius
            )
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initializeBackground()
    }

    private fun initializeBackground() {
        if (this.background is ColorDrawable) {
            this.background = GradientDrawable().apply {
                cornerRadius = this@ElasticCardView.cornerRadius
                setColor((background as ColorDrawable).color)
            }.mutate()
        }
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }

    fun setOnFinishListener(listener: ElasticFinishListener) {
        this.onFinishListener = listener
    }

    private fun invokeListeners() {
        this.onClickListener?.onClick(this)
        this.onFinishListener?.onFinished()
    }

    /** Builder class for creating [ElasticCardView]. */
    class Builder(context: Context) {
        private val elasticLayout = ElasticCardView(context)

        fun setScale(value: Float) = apply { this.elasticLayout.scale = value }
        fun setDuration(value: Int) = apply { this.elasticLayout.duration = value }
        fun setCornerRadius(@Px value: Float) = apply { this.elasticLayout.cornerRadius = value }

        @JvmSynthetic
        fun setOnClickListener(block: () -> Unit) = apply {
            setOnClickListener(OnClickListener { block() })
        }

        fun setOnClickListener(value: OnClickListener) = apply {
            this.elasticLayout.setOnClickListener(value)
        }

        @JvmSynthetic
        fun setOnFinishListener(block: () -> Unit) = apply {
            setOnFinishListener(ElasticFinishListener { block() })
        }

        fun setOnFinishListener(value: ElasticFinishListener) = apply {
            this.elasticLayout.setOnFinishListener(value)
        }

        fun build() = this.elasticLayout
    }
}