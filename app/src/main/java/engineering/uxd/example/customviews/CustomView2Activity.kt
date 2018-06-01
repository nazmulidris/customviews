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
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class CustomView2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view2)
    }
}

class TallyCounterView @JvmOverloads constructor(context: Context,
                                                 attrs: AttributeSet? = null,
                                                 defStyleAttr: Int = 0) :
        View(context, attrs, defStyleAttr), AnkoLogger, TallyCounter {

    // todo https://vimeo.com/242155617 (time: 16.46)

    // Constructor and properties

    private val helpers = Helpers()
    private val dimens = Dimens(resources, context)

    init {

        // Background paint helper
        with(helpers.backgroundPaint) {
            color = dimens.backgroundColor
        }

        // Line paint helper
        with(helpers.linePaint) {
            color = dimens.lineColor
            strokeWidth = dimens.strokeWidth
            info { "strokeWidth (px) = ${strokeWidth}" }
        }

        // Text paint helper
        with(helpers.textPaint) {
            color = dimens.textColor
            textSize = dimens.textSize
            info { "textSize (px) = ${textSize}" }
        }

    }

    data class Helpers(
            val backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG),
            val linePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG),
            val textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG),
            val backgroundRect: RectF = RectF(),
            var state: Int = 0
    )

    data class Dimens(private val resources: Resources,
                      private val context: Context,
                      val textSize: Float = resources.getDimension(
                              R.dimen.tally_counter_text_size),
                      val strokeWidth: Float = resources.getDimension(
                              R.dimen.tally_counter_stroke_width),
                      val cornerRadius: Float = resources.getDimension(
                              R.dimen.tally_counter_corner_radius),
                      val backgroundColor: Int = ContextCompat.getColor(context,
                              R.color.colorPrimary),
                      val lineColor: Int = ContextCompat.getColor(context,
                              R.color.colorAccent),
                      val textColor: Int = ContextCompat.getColor(context,
                              R.color.colorWindowBackground)
    )

    // Draw

    override fun onDraw(canvas: Canvas) {
        val width = canvas.width
        val height = canvas.height
        val centerX = width * 0.5f

        with(helpers) {
            backgroundRect.set(0f, 0f, width.toFloat(), height.toFloat())
            canvas.drawRoundRect(
                    backgroundRect, dimens.cornerRadius, dimens.cornerRadius, backgroundPaint)
        }

    }

    // Implement TallyCounter interface

    override fun reset() {
    }

    override fun increment() {
    }

    override fun getCount() {
    }

    override fun setCount(value: Int) {
        helpers.state = value
    }

}

// TallyCounter interface

interface TallyCounter {
    fun reset()
    fun increment()
    fun getCount()
    fun setCount(value: Int)
}