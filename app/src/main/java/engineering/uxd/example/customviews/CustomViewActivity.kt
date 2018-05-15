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
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View

class CustomViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view)
    }
}

class EmotionalFaceView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var faceColor = Color.YELLOW
    private var eyesColor = Color.BLACK
    private var mouthColor = Color.BLACK

    private var roundedRectRadius = 20f

    private var borderColor = Color.GRAY
    private var borderWidth = 16f

    private var boundsColor = Color.GREEN
    private var boundsWidth = 16f

    private var size = 320

    override fun onDraw(canvas: Canvas) {
        drawBounds(canvas)
        drawFaceBackground(canvas)
        drawEyes(canvas)
        drawMouth(canvas)
    }

    private fun drawBounds(canvas: Canvas) {
        // Draw bounds
        with(paint) {
            color = boundsColor
            style = Paint.Style.STROKE
            strokeWidth = boundsWidth
            alpha = 100
            canvas.drawRect(
                    0f, 0f,
                    width.toFloat(), height.toFloat(),
                    this)
            alpha = 255
        }
    }

    private fun drawFaceBackground(canvas: Canvas) {
        val radius = size / 2f
        val cx = size / 2f
        val cy = cx

        // Draw the face
        with(paint) {
            color = faceColor
            style = Paint.Style.FILL
            canvas.drawCircle(cx, cy, radius, this)
        }

        // Draw the border around the face
        with(paint) {
            color = borderColor
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
            canvas.drawCircle(cx, cy, radius - borderWidth / 2f, this)
        }
    }

    private fun drawMouth(canvas: Canvas) {
    }

    private fun drawEyes(canvas: Canvas) {
        with(paint) {
            color = eyesColor
            style = Paint.Style.FILL
            val leftEyeRect = RectF(
                    size * 0.32f, size * 0.23f,
                    size * 0.43f, size * 0.5f)
            val rightEyeRect = RectF(
                    size * 0.57f, size * 0.23f,
                    size * 0.68f, size * 0.5f)
            canvas.drawRoundRect(leftEyeRect, roundedRectRadius, roundedRectRadius, this)
            canvas.drawRoundRect(rightEyeRect, roundedRectRadius, roundedRectRadius, this)
        }
    }

}