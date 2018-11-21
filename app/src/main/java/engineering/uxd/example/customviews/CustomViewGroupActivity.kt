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

class CustomViewGroupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view_group)
    }

}

class SimpleListItem @JvmOverloads constructor(context: Context,
                                               attrs: AttributeSet? = null,
                                               defStyleAttr: Int = 0) :
        ViewGroup(context, attrs, defStyleAttr),
        AnkoLogger {

    // Make sure to use MarginLayoutParams (to ensure that any child views that are added
    // to this ViewGroup can have margins).

    override fun checkLayoutParams(p: LayoutParams) = p is MarginLayoutParams

    override fun generateDefaultLayoutParams() =
            ViewGroup.MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(attrs: AttributeSet) =
            ViewGroup.MarginLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams) = ViewGroup.MarginLayoutParams(p)

    // Measure.

    override fun onMeasure(widthMS: Int, heightMS: Int) {
        // Actually do the measurement.
        performMeasurementOfChildren(widthMS, heightMS)

        // Find out how much space the icon used (including margins).
        val iconWidth = icon.widthUsed()
        val iconHeight = icon.heightUsed()

        // Find out how much space the title used (including margins).
        val titleWidth = title.widthUsed()
        val titleHeight = title.heightUsed()

        // Find out how much space the subtitle used (including margins).
        val subtitleWidth = subtitle.widthUsed()
        val subtitleHeight = subtitle.heightUsed()

        // Find the width taken up by the children and own padding. The ViewGroup
        // is behaving as a View in this case and has to deal w/ it's own padding.
        val width = paddingLeft + paddingRight +
                iconWidth +
                Math.max(titleWidth, subtitleWidth)
        val height = paddingTop + paddingBottom +
                Math.max(iconHeight, titleHeight + subtitleHeight)

        // Reconcile the measured dimensions w/ this view's constraints and set the
        // final measured width and height for the composite ViewGroup.
        setMeasuredDimension(
                resolveSize(width, widthMS),
                resolveSize(height, heightMS)
        )

    }

    private fun performMeasurementOfChildren(widthMS: Int, heightMS: Int) {
        // Measure icon.
        measureChildWithMargins(icon,
                                widthMS, 0,
                                heightMS, 0)

        // Measure title (by making sure to the icon's margins into account).
        measureChildWithMargins(title,
                                widthMS, icon.widthUsed(),
                                heightMS, 0)

        // Measure subtitle (by making sure to take the icon's and title's margins into account).
        measureChildWithMargins(subtitle,
                                widthMS, icon.widthUsed(),
                                heightMS, title.heightUsed())
    }

    /** Includes margin and padding. [getMeasuredWidth] only includes padding for the [View] */
    private fun View.widthUsed(): Int = measuredWidth + leftMargin() + rightMargin()

    /** Includes margin and padding. [getMeasuredHeight] only includes padding for the [View] */
    private fun View.heightUsed(): Int = measuredHeight + topMargin() + bottomMargin()

    /** The children of this [ViewGroup] can have margins, since [checkLayoutParams],
     * [generateDefaultLayoutParams], [generateLayoutParams], were overridden in order to
     * support children having margins.*/
    private fun View.marginLayoutParams(): MarginLayoutParams = layoutParams as MarginLayoutParams

    private fun View.leftMargin(): Int = marginLayoutParams().leftMargin
    private fun View.rightMargin(): Int = marginLayoutParams().rightMargin
    private fun View.topMargin(): Int = marginLayoutParams().topMargin
    private fun View.bottomMargin(): Int = marginLayoutParams().bottomMargin

    // Layout.

    val coord = XYCoord()

    data class XYCoord(var x: Int = 0, var y: Int = 0) {
        fun reset() {
            x = 0
            y = 0
        }
    }

    /**
     * Note that [View.getMeasuredWidth] does not include margins (but does include padding).
     * This is why the [leftMargin], [rightMargin], [topMargin], and [bottomMargin] extension
     * functions are used extensively in this method.
     */
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        // Icon
        with(coord) {
            reset()
            icon.let {
                // Calculate the the x and y coordinates of the icon.
                x = paddingLeft + it.leftMargin()
                y = paddingTop + it.topMargin()
                // Layout the icon.
                it.layout(x, y,
                          x + it.measuredWidth, y + it.measuredHeight)
            }
        }

        // Title
        with(coord) {
            reset()
            // Calculate the x coordinate of the title:
            // icon's right coordinate + icon's right margin + title's left margin.
            x = paddingLeft +
                    with(icon) { leftMargin() + measuredWidth + rightMargin() } +
                    title.leftMargin()
            // Calculate the y coordinate of the title:
            // this ViewGroup's top padding + tile's top margin.
            y = paddingTop + title.topMargin()
            // Layout the title.
            title.layout(x, y,
                         x + title.measuredWidth, y + title.measuredHeight)
        }

        // Subtitle.
        with(coord) {
            // The subtitle has the same x coordinate as the title, so don't reset
            // Calculate the y coordinate of the title:
            // title's bottom coordinate + title's bottom margin + subtitle's top margin.
            y += title.measuredHeight + title.bottomMargin() + subtitle.topMargin()
            // Layout the subtitle.
            subtitle.layout(
                    x, y,
                    x + subtitle.measuredWidth, y + subtitle.measuredHeight)
        }

    }

}