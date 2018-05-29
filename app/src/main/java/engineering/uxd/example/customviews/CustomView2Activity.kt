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
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import org.jetbrains.anko.AnkoLogger


class CustomView2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view2)
    }
}

interface TallyCounter {
    fun reset()
    fun increment()
    fun getCount()
    fun setCount(value: Int)
}

// Verbose constructor code (not using @JvmOverloads)

/*
class TallyCounterView : View {

    // View constructors

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) :
            this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) :
            super(context, attributeSet, defStyleAttr)

    // View implementation

}*/

class TallyCounterView @JvmOverloads constructor(context: Context,
                                                 attrs: AttributeSet? = null,
                                                 defStyleAttr: Int = 0) :
        View(context, attrs, defStyleAttr), AnkoLogger {

    // todo https://vimeo.com/242155617 (time: 13.52)

    private var backgroundPaint: Paint
    private var linePaint: Paint
    private var numberPaint: Paint

    init {
        // Background
        backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        backgroundPaint.color = ContextCompat.getColor(context, R.color.colorPrimary)

        // Line
        linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        linePaint.color = ContextCompat.getColor(context, R.color.colorAccent)
        linePaint.strokeWidth = resources.getDimension(R.dimen.tally_counter_stroke_width)

        // Number
        numberPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        numberPaint.color = ContextCompat.getColor(context, R.color.colorWindowBackground)
    }

}