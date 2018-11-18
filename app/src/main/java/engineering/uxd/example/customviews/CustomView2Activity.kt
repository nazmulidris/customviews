/*
 * Copyright 2018 Nazmul Idris. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package engineering.uxd.example.customviews

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.activity_custom_view2.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*


class CustomView2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view2)
        button_increment_counter.onClick {
            view_tally_counter.increment()
        }
    }
}

class TallyCounterView @JvmOverloads constructor(context: Context,
                                                 attrs: AttributeSet? = null,
                                                 defStyleAttr: Int = 0) :
        View(context, attrs, defStyleAttr),
        AnkoLogger,
        TallyCounter {

    // Properties, paint helpers, and loaded dimensions

    private val dimens = Dimens(resources, context)
    private val helpers = Helpers(dimens)
    private var count = 0

    data class Helpers(
            val dimens: Dimens,
            val backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG),
            val linePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG),
            val textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG),
            val backgroundRect: RectF = RectF()
    ) : AnkoLogger {

        init {

            // Background paint helper
            with(backgroundPaint) {
                color = dimens.backgroundColor
            }

            // Line paint helper
            with(linePaint) {
                color = dimens.lineColor
                strokeWidth = dimens.strokeWidth
                info { "strokeWidth (px) = ${strokeWidth}" }
            }

            // Text paint helper
            with(textPaint) {
                color = dimens.textColor
                textSize = dimens.textSize
                info { "textSize (px) = ${textSize}" }
            }

        }

    }

    data class Dimens(private val resources: Resources,
                      private val context: Context,
                      val textSize: Float = resources.getDimension(
                          R.dimen.tally_counter_text_size),
                      val strokeWidth: Float = resources.getDimension(
                          R.dimen.tally_counter_stroke_width),
                      val cornerRadius: Float = resources.getDimension(
                          R.dimen.tally_counter_corner_radius),
                      val backgroundColor: Int = ContextCompat.getColor(context,
                                                                        R.color.colorPrimaryDark),
                      val lineColor: Int = ContextCompat.getColor(context,
                                                                  R.color.colorPrimary),
                      val textColor: Int = ContextCompat.getColor(context,
                                                                  R.color.colorAccent)
    )

    // Measure

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Max text height and width
        val maxTextWidth = helpers.textPaint.measureText(generateTextContent())
        val maxTextHeight = with(helpers.textPaint.fontMetrics) { Math.abs(top) + Math.abs(bottom) }

        // Max content height and width (including padding), views don't deal /w margin
        val desiredWidth = maxTextWidth + paddingLeft + paddingRight
        val desiredHeight = maxTextHeight + paddingTop + paddingBottom

        // Note in the real world use View.resolveSize() instead of reconcileSize()
        val measuredWidth = reconcileSize(desiredWidth.toInt(), widthMeasureSpec)
        val measuredHeight = reconcileSize(desiredHeight.toInt(), heightMeasureSpec)

        // Set the final measured dimensions
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    /**
     * Implementation copied from [View.resolveSizeAndState]. In the real world, please don't call
     * this method, instead call [View.resolveSize].
     */
    private fun reconcileSize(size: Int, measureSpec: Int): Int {
        val measureSpecMode = View.MeasureSpec.getMode(measureSpec)
        val measureSpecSize = View.MeasureSpec.getSize(measureSpec)
        return when (measureSpecMode) {
            View.MeasureSpec.EXACTLY -> measureSpecSize
            View.MeasureSpec.UNSPECIFIED -> size
            View.MeasureSpec.AT_MOST -> if (measureSpecSize < size) measureSpecSize else size
            else -> size
        }
    }

    // Draw

    override fun onDraw(canvas: Canvas) {
        val width = canvas.width
        val height = canvas.height

        val fontMetrics = helpers.textPaint.fontMetrics
        //val fontHeight = fontMetrics.top - fontMetrics.bottom
        val baselineY = height / 2f + (-fontMetrics.top / 2) - (fontMetrics.bottom / 2)

        // Draw filled rectangle (background)
        with(helpers) {
            backgroundRect.set(0f, 0f, width.toFloat(), height.toFloat())
            canvas.drawRoundRect(
                backgroundRect, dimens.cornerRadius, dimens.cornerRadius, backgroundPaint)
        }

        // Draw text baseline & vertical center of the Canvas
        with(helpers) {

            // Unbroken vertical center line
            // canvas.drawLine(0F, (height / 2).toFloat(), width.toFloat(), (height / 2).toFloat(),
            // linePaint)

            // Dashed vertical center line
            for (x in 1..width step (dimens.strokeWidth * 5).toInt()) {
                canvas.drawLine(
                    x.toFloat(), (height / 2).toFloat(),
                    x + dimens.strokeWidth, (height / 2).toFloat(),
                    linePaint)
            }

            // Unbroken text-baseline line
            // canvas.drawLine(0F, baselineY, width.toFloat(), baselineY, linePaint)

            // Dashed text-baseline line
            for (x in 1..width step (dimens.strokeWidth * 3).toInt()) {
                canvas.drawLine(
                    x.toFloat(), baselineY,
                    x + dimens.strokeWidth, baselineY,
                    linePaint)
            }

        }

        // Draw text
        with(helpers) {
            val centerX = width * 0.5f
            val textWidth = textPaint.measureText(generateTextContent())
            val textX = centerX - textWidth * 0.5f
            canvas.drawText(generateTextContent(), textX, baselineY, textPaint)
        }

    }

    // Implement TallyCounter interface

    override fun reset() {
        setCount(0)
    }

    override fun increment() {
        setCount(count + 1)
    }

    override fun getCount(): Int {
        return count
    }

    override fun setCount(value: Int) {
        count = value
        invalidate()
    }

    private fun generateTextContent(): String {
        return "❤ ${String.format(Locale.getDefault(), "%03d", count)}"
    }

}

// TallyCounter interface

interface TallyCounter {
    fun reset()
    fun increment()
    fun getCount(): Int
    fun setCount(value: Int)
}