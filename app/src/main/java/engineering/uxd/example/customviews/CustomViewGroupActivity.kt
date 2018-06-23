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
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_custom_view_group.view.*
import org.jetbrains.anko.AnkoLogger

// todo https://vimeo.com/242155617 (time: 31.48)

class CustomViewGroupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view_group)
    }

}

class SimpleListItem @JvmOverloads constructor(context: Context,
                                               attrs: AttributeSet? = null,
                                               defStyleAttr: Int = 0) :
        ViewGroup(context, attrs, defStyleAttr), AnkoLogger {

    // Make sure to use MarginLayoutParams (to preserve margin values)

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p is MarginLayoutParams
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return generateDefaultLayoutParams()
    }

    // Measure

    override fun onMeasure(widthMS: Int, heightMS: Int) {
        // Actually do the measurement
        performMeasurementOfChildren(widthMS, heightMS)

        // Find out how much space the icon used
        val iconWidth = icon.widthUsed()
        val iconHeight = icon.heightUsed()

        // Find out how much space the title used
        val titleWidth = title.widthUsed()
        val titleHeight = title.heightUsed()

        // Find out how much space the subtitle used
        val subtitleWidth = subtitle.widthUsed()
        val subtitleHeight = subtitle.heightUsed()

        // Find the width take by the children and padding since this ViewGroup
        // is behaving as a View in this case and has to deal w/ it's own padding.
        val width = paddingLeft + paddingRight + iconWidth + Math.max(titleWidth, subtitleWidth)
        val height = paddingTop + paddingBottom + iconHeight + titleHeight + subtitleHeight

        // Reconcile the measured dimensions w/ this view's constraints and set the
        // final measured width and height for the composite ViewGroup
        setMeasuredDimension(
                resolveSize(width, widthMS),
                resolveSize(height, heightMS)
        )

    }

    private fun performMeasurementOfChildren(widthMS: Int, heightMS: Int) {
        // Variables to store width and height consumed thus far
        var widthUsed = 0
        var heightUsed = 0

        // Measure icon
        measureChildWithMargins(icon, widthMS, widthUsed, heightMS, heightUsed)

        // Figure out how much width and height the icon used
        widthUsed = icon.widthUsed()

        // Measure title
        measureChildWithMargins(title, widthMS, widthUsed, heightMS, heightUsed)
        heightUsed = title.measuredHeight

        // Measure subtitle
        measureChildWithMargins(subtitle, widthMS, widthUsed, heightMS, heightUsed)
    }

    private fun View.widthUsed(): Int = measuredWidth + with(mlp()) { leftMargin + rightMargin }

    private fun View.heightUsed(): Int = measuredHeight + with(mlp()) { topMargin + bottomMargin }

    private fun View.mlp(): MarginLayoutParams = layoutParams as MarginLayoutParams

    // Layout

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var x = 0
        var y = 0

        // Icon

        // Calculate the the x and y coordinates of the icon
        x = paddingLeft + icon.mlp().leftMargin
        y = paddingTop + icon.mlp().topMargin

        // Layout the icon
        icon.layout(x, y, x + icon.measuredWidth, y + icon.measuredHeight)

        // Title

        // Calculate the x, y coordinates of the title:
        // icon's right coordinate + icon's right margin
        x += measuredWidth + icon.mlp().rightMargin

        // Add the title's left margin
        x += title.mlp().leftMargin

        // Subtitle

    }

}