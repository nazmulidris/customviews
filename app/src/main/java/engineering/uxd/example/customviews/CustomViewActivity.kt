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

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.activity_custom_view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class CustomViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view)
        setupViewModel()
        respondToClick()
    }

    // LiveData and user-selection-state retention

    private lateinit var stateViewModel: StateViewModel

    private fun setupViewModel() {
        stateViewModel = ViewModelProviders.of(this).get(StateViewModel::class.java)

        // Attach observers to changes in the LiveData
        stateViewModel.userSelectionState.observe(
            this,
            Observer {
                with(emotionalFaceView) {
                    emotion = it ?: 0
                    triggerClickAnimation()
                }
            })
    }

    class StateViewModel : ViewModel() {
        val userSelectionState = MutableLiveData<Int>()
    }

    // User interaction

    private fun respondToClick() {
        with(emotionalFaceView) {
            onClick {
                // Change the LiveData value
                stateViewModel.userSelectionState.value = if (emotion == 0) 1 else 0
            }
        }
    }

}

class EmotionalFaceView @JvmOverloads constructor(context: Context,
                                                  attrs: AttributeSet? = null,
                                                  defStyleAttr: Int = 0) :
        View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var faceColor = Color.YELLOW
    private var eyesColor = Color.BLACK
    private var mouthColor = Color.BLACK

    private var eyesRoundedRectRadius = 20f

    private var borderColor = Color.GRAY
    private var borderWidth = 16f
        set(value) {
            field = value
            invalidate()
        }

    private var boundsColor = Color.GREEN
    private var boundsWidth = 16f

    private var size: Int = 0

    var emotion = 0 // 0 is happy, 1 is sad
        set(value) {
            field = value
            invalidate()
        }

    // Get styling from XML attributes

    init {
        with(context.obtainStyledAttributes(
            attrs, R.styleable.EmotionalFaceViewStyles, 0, 0)
        ) {
            try {

                emotion = getInt(R.styleable.EmotionalFaceViewStyles_emotion, emotion)

                faceColor = getColor(R.styleable.EmotionalFaceViewStyles_faceColor, faceColor)
                mouthColor = getColor(R.styleable.EmotionalFaceViewStyles_mouthColor, mouthColor)
                eyesColor = getColor(R.styleable.EmotionalFaceViewStyles_eyesColor, eyesColor)

                borderColor = getColor(R.styleable.EmotionalFaceViewStyles_borderColor, borderColor)
                boundsColor = getColor(R.styleable.EmotionalFaceViewStyles_boundsColor, boundsColor)

                eyesRoundedRectRadius = getDimension(
                    R.styleable.EmotionalFaceViewStyles_eyesRoundedRectRadius,
                    eyesRoundedRectRadius)
                borderWidth = getDimension(
                    R.styleable.EmotionalFaceViewStyles_borderWidth,
                    borderWidth)
                boundsWidth = getDimension(
                    R.styleable.EmotionalFaceViewStyles_boundsWidth,
                    boundsWidth)


            } finally {
                recycle()
            }
        }
    }

    // Measure functions (set size)

    /**
     * Set the measured width and height to the max amount of space available
     * for this view (as provided by the layout manager), while preserving the
     * aspect ratio of the view.
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        size = Math.min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }

    // Drawing functions (based on size)

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
        val mouthPath = Path().apply {
            moveTo(size * 0.22f, size * 0.7f)

            when (emotion) {
            // Happy
                0 -> {
                    quadTo(size * 0.50f, size * 0.80f, size * 0.78f, size * 0.70f)
                    quadTo(size * 0.50f, size * 0.90f, size * 0.22f, size * 0.70f)
                }
            // Sad
                1 -> {
                    quadTo(size * 0.50f, size * 0.50f, size * 0.78f, size * 0.70f)
                    quadTo(size * 0.50f, size * 0.60f, size * 0.22f, size * 0.70f)
                }
            }
        }
        with(paint) {
            color = mouthColor
            style = Paint.Style.FILL
            canvas.drawPath(mouthPath, this)
        }
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
            canvas.drawRoundRect(leftEyeRect, eyesRoundedRectRadius, eyesRoundedRectRadius, this)
            canvas.drawRoundRect(rightEyeRect, eyesRoundedRectRadius, eyesRoundedRectRadius, this)
        }
    }

    // Animation

    val animators = AnimatorSet()

    fun triggerClickAnimation() {
        if (!animators.isRunning)
            with(animators) {
                playSequentially(
                    ObjectAnimator.ofFloat(this@EmotionalFaceView,
                                           "borderWidth", borderWidth, borderWidth * 4f),
                    ObjectAnimator.ofFloat(this@EmotionalFaceView,
                                           "borderWidth", borderWidth * 4f, borderWidth)
                )
                start()
            }
    }

}