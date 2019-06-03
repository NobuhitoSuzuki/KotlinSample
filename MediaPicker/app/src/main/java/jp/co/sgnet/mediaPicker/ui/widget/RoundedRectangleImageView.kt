package jp.co.sgnet.mediaPicker.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

class RoundedRectangleImageView: AppCompatImageView {

    private var radius: Float = 0.0F
    private var roundedRectPath: Path = Path()
    private var rectF: RectF = RectF()

    constructor(context: Context?) : super(context) {
        setup(context)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        setup(context)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup(context)
    }

    private fun setup(context: Context?) {
        context?.let {
            val density = context.resources.displayMetrics.density
            radius = 2.0F * density
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        rectF.set(0.0f, 0.0f, measuredWidth.toFloat(), measuredHeight.toFloat())
        roundedRectPath.addRoundRect(rectF, radius, radius, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.clipPath(roundedRectPath)
        super.onDraw(canvas)
    }
}