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
        paint.color = boundsColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = boundsWidth
        paint.alpha = 100
        canvas.drawRect(
                0f, 0f,
                width.toFloat(), height.toFloat(),
                paint)
    }

    private fun drawFaceBackground(canvas: Canvas) {
        // Draw the face
        paint.alpha = 255
        paint.color = faceColor
        paint.style = Paint.Style.FILL
        val radius = size / 2f
        val cx = size / 2f
        val cy = cx
        canvas.drawCircle(cx, cy, radius, paint)

        // Draw the border around the face
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth
        canvas.drawCircle(cx, cy, radius - borderWidth / 2f, paint)
    }

    private fun drawMouth(canvas: Canvas) {
    }

    private fun drawEyes(canvas: Canvas) {
    }

}