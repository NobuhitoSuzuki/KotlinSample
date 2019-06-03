package jp.co.sgnet.mediaPicker.ui.widget

import android.app.Service
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.v4.content.res.ResourcesCompat
import android.text.TextPaint
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import jp.co.sgnet.mediaPicker.R

private const val STROKE_WIDTH: Float = 3.0f
private const val SHADOW_WIDTH = 6.0f
private const val SIZE = 40
private const val STROKE_RADIUS = 11.5f
private const val BG_RADIUS = 11.0f
private const val CONTENT_SIZE = 16

class CheckView: View {
    companion object {
        const val UNCHECKED: Int = Integer.MIN_VALUE
    }

    private var countable: Boolean = false
    private var checked: Boolean = false
    private var checkedNum = 0

    private val strokePaint: Paint by lazy { Paint() }
    private val shadowPaint: Paint by lazy { Paint() }
    private val backgroundPaint: Paint by lazy { Paint() }
    private val textPaint: Paint by lazy { Paint() }

    private var checkDrawable: Drawable? = null
    private var density: Float = 0f
    private var checkRect: Rect? = null
    private var enable = true

    constructor(context: Context?) : this(context, null, 0)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup(context)
    }

    private fun setup(context: Context?) {
        context?.let {
            val displayMatrix = DisplayMetrics()
            val windowManager = it.getSystemService(Service.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(displayMatrix)
            density = displayMatrix.density
            strokePaint.isAntiAlias = true
            strokePaint.style = Paint.Style.STROKE
            strokePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
            strokePaint.strokeWidth = STROKE_WIDTH * density
            val ta: TypedArray = context.theme?.obtainStyledAttributes(intArrayOf(android.R.color.white))!!
            val defaultColor = ResourcesCompat.getColor(context.resources, android.R.color.holo_blue_bright, context.theme)
            val color = ta.getColor(0, defaultColor)
            ta.recycle()
            strokePaint.color = color
            checkDrawable = ResourcesCompat.getDrawable(context.resources, android.R.drawable.checkbox_on_background, context.theme)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val sizeSpec = MeasureSpec.makeMeasureSpec((density * SIZE).toInt(), MeasureSpec.EXACTLY)
        super.onMeasure(sizeSpec, sizeSpec)
    }

    fun setEnable(enable: Boolean) {
        if (this.enable != enable) {
            this.enable = enable
            invalidate()
        }
    }

    fun setCountable(boolean: Boolean) {
        if (countable != boolean) {
            countable = boolean
            invalidate()
        }
    }

    fun setChecked(boolean: Boolean) {
        if (countable) {
            throw IllegalStateException("CheckView is countable, call setCheckedNum() instead.")
        }

        checked = boolean
        invalidate()
    }

    fun setCheckedNum(num: Int) {
        if (!countable) {
            throw IllegalStateException("CheckView is not countable, call setChecked() instead.")
        }
        if (num != UNCHECKED && num < 0) {
            throw  IllegalStateException("the num can't be negative")
        }
        checkedNum = num
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // draw outer and inner shadow
        setupShadowPaint()
        canvas?.drawCircle(density * SIZE / 2, density * SIZE / 2, density.times(STROKE_RADIUS + STROKE_WIDTH / 2 + SHADOW_WIDTH), shadowPaint)

        // draw white stroke
        canvas?.drawCircle(density * SIZE / 2, density * SIZE / 2, density.times(STROKE_RADIUS), strokePaint)

        // draw content
        if (countable) {
            if (checkedNum != UNCHECKED) {
                setupBackgroundPaint()
                canvas?.drawCircle(density * SIZE / 2, density * SIZE / 2,
                    density.times(BG_RADIUS), backgroundPaint)
                setupTextPaint()
                val text = checkedNum.toString()
                val baseX = (width - textPaint.measureText(text)) / 2
                val baseY = (height - textPaint.descent() - textPaint.ascent()) / 2
                canvas?.drawText(text, baseX, baseY, textPaint)
            }
        } else {
            if (checked) {
                setupBackgroundPaint()
                canvas!!.drawCircle(density * SIZE / 2, density * SIZE / 2,
                    BG_RADIUS * density, backgroundPaint)
                checkDrawable!!.bounds = getCheckRect()!!
                checkDrawable!!.draw(canvas)
            }
        }
        alpha = if (enable) 1.0f else 0.5f
    }

    private fun getCheckRect(): Rect? {
        if (checkRect == null) {
            val rectPadding = (density * SIZE / 2 - CONTENT_SIZE * density / 2).toInt()
            checkRect = Rect(rectPadding, rectPadding, (SIZE * density - rectPadding).toInt(), (SIZE * density - rectPadding).toInt())
        }
        return checkRect
    }

    private fun setupTextPaint() {
        textPaint.isAntiAlias = true
        textPaint.color = Color.WHITE
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textPaint.textSize = 12.0f * density
    }

    private fun setupBackgroundPaint() {
        backgroundPaint.isAntiAlias = true
        backgroundPaint.style = Paint.Style.FILL
        val ta: TypedArray = context!!.theme!!.obtainStyledAttributes(intArrayOf(android.R.color.holo_red_dark))
        val defaultColor = ResourcesCompat.getColor(context!!.resources, R.color.primary_dark_material_dark, context!!.theme)
        val color = ta.getColor(0, defaultColor)
        ta.recycle()
        backgroundPaint.color = color
    }

    private fun setupShadowPaint() {
        shadowPaint.isAntiAlias = true
        val outerRadius = STROKE_RADIUS + STROKE_WIDTH / 2
        val innerRadius = outerRadius - STROKE_WIDTH
        val gradientRadius = outerRadius + SHADOW_WIDTH
        val stop0 = (innerRadius - SHADOW_WIDTH) / gradientRadius
        val stop1 = innerRadius / gradientRadius
        val stop2 = outerRadius / gradientRadius
        val stop3 = 1.0f
        shadowPaint.shader = RadialGradient(
            SIZE.toFloat() * density / 2,
            SIZE.toFloat() * density / 2,
            gradientRadius * density,
            intArrayOf(
                Color.parseColor("#00000000"),
                Color.parseColor("#0D000000"),
                Color.parseColor("#0D000000"),
                Color.parseColor("#00000000")
            ),
            floatArrayOf(stop0, stop1, stop2, stop3),
            Shader.TileMode.CLAMP
        )
    }
}