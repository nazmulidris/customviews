Android Custom Views
====================

* [Overview](#overview)
  * [Custom Drawable - SimpleTextDrawable](#custom-drawable---simpletextdrawable)
  * [Custom View - EmotionalFaceView](#custom-view---emotionalfaceview)
     * [Measure](#measure)
     * [Styling in XML with custom attributes](#styling-in-xml-with-custom-attributes)
  * [Custom View 2](#custom-view-2)
     * [Easier View constructor in Kotlin](#easier-view-constructor-in-kotlin)
     * [Retrieving scalable dimensions as pixels](#retrieving-scalable-dimensions-as-pixels)
     * [Themes and styles](#themes-and-styles)
  * [Custom ViewGroup](#custom-viewgroup)
  * [Custom LayoutManager](#custom-layoutmanager)
  * [Custom Scrolling](#custom-scrolling)
* [Reference info](#reference-info)
  * [Custom Views](#custom-views)
  * [Custom Views and Kotlin constructors using @JvmOverloads](#custom-views-and-kotlin-constructors-using-jvmoverloads)
  * [Android UI rendering](#android-ui-rendering)
  * [Styled attribute sets in XML](#styled-attribute-sets-in-xml)
  * [Theming](#theming)
  * [RelativeLayout](#relativelayout)
  * [Animation](#animation)
  * [ConstraintLayout](#constraintlayout)
      
# Overview

This project is about the exploration of custom views. It explores the following.

1. Custom Drawable
2. Custom View
3. Custom View 2
4. Custom ViewGroup
5. Custom LayoutManager (todo from video)
6. Custom Scrolling (todo from book)

## Custom Drawable - `SimpleTextDrawable`
The `ImageView` has its background set to `SimpleTextDrawable` and its height and width are
not explicitly set; the layout params of `wrap_content` is set for both the height and width.
```kotlin
imageView.backgroundDrawable = SimpleTextDrawable()
```

The `SimpleTextDrawable` sets its own intrinsic height and width based on the size of the
text it has to display. This information is passed to the `ImageView`, which then sets
its actual width and height to this intrinsic width and height when measure and layout is
performed.

## Custom View - `EmotionalFaceView`
The `EmotionalFaceView` component is responsive to the amount of space (width and height)
provided by its parent. In this case, the parent is a `FrameLayout` which both explicitly
sets the height and width of the `EmotionalFaceView` and also allows it to be responsive
by setting the width and height to `match_parent`. 

### Measure
```kotlin
override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    size = Math.min(measuredWidth, measuredHeight)
    setMeasuredDimension(size, size)
}
```

The `EmotionalFaceView` figures out how much space it has by asking its parent for its size, then
measures itself by picking a height and width that preserves its aspect ration (regardless of
how much space there is for it to actually fill).

### Styling in XML with custom attributes
For styling, this custom view actually uses attributes that are declared in `attrs.xml`
and then used in the `activity_custom_view.xml` layout file. This way colors and dimensions
don't have to be hardcoded in the Kotlin code. To learn more about declaring styleable
attributes read [this medium article](http://blog.danlew.net/2016/07/19/a-deep-dive-into-android-view-constructors/).

```kotlin
with(context.obtainStyledAttributes(attrs, R.styleable.EmotionalFaceViewStyles, 0, 0)) {
    try {
        emotion = getInt(R.styleable.EmotionalFaceViewStyles_emotion, emotion)
        faceColor = getColor(R.styleable.EmotionalFaceViewStyles_faceColor, faceColor)
        boundsWidth = getDimension(R.styleable.EmotionalFaceViewStyles_boundsWidth,
                boundsWidth)
    } finally {
        recycle()
    }
}
```

## Custom View 2

### Easier View constructor in Kotlin
Since `View` classes might have up to 3 constructors, one way to handle this situation
in Kotlin is to write code like this.

```kotlin
class TallyCounterView : View {
    // View constructors
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) :
            this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) :
            super(context, attributeSet, defStyleAttr)

    // View implementation
    
}
```

While this will work just fine, there's a much more terse way of expressing the same
thing as shown below using `@JvmOverloads` annotation.

```kotlin
class EmotionalFaceView @JvmOverloads constructor(context: Context,
                                                  attrs: AttributeSet? = null,
                                                  defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    
    // View implementation
    
}
```

### Retrieving scalable dimensions as pixels
You can define `sp`, `dp`, values in `dimens.xml` and in order to retrieve them, you can use
the `Resources.getDimension()` method, which will convert the `sp` or `dp` values into a `float`
that you can use. If you wanted to get a pixel value (`Int`) instead of a `float`, then you
can use `Resources.getDimensionPixelSize()` instead.
```kotlin
val size: Float = resources.getDimension(R.dimen.tally_counter_text_size)
val sizePx : Int = resources.getDimensionPixelSize(R.dimen.tally_counter_text_size)
```

Here's the code you would have to write if you don't use `getDimension()` or
`getDimensionPixelSize()`.
```kotlin
val altTextSize = Math.round(resources.displayMetrics.scaledDensity * 64f)
```

### Themes and styles
The `CustomView2Activity` doesn't use the default style `DefaultActivity`, and it also
a `RelativeLayout` in order to layout a `Toolbar`, `TextView`, and `TallyCounterView`.

```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <android.support.v7.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:title="@string/app_name"
        style="@style/ToolbarStyle" />

    <TextView
        android:id="@+id/textView"
        android:text="Custom View 2"
        android:layout_below="@id/toolbar"
        android:layout_centerHorizontal="true"
        android:layout_centerInParent="false"
        android:layout_marginTop="@dimen/default_margin"
        style="@style/EmptyActivityLabel" />

    <engineering.uxd.example.customviews.TallyCounterView
        android:layout_margin="@dimen/default_margin"
        android:layout_below="@id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />

</RelativeLayout>
```

The `Toolbar` uses the `ToolbarStyle`. This is what it looks like.

```xml
<style name="ToolbarStyle">
    <item name="titleTextColor">@color/colorAccent</item>
    <item name="titleTextAppearance">@style/ToolbarTextAppearance</item>
    <item name="android:background">@color/colorPrimary</item>
</style>

<style name="ToolbarTextAppearance" parent="TextAppearance.AppCompat.Medium">
    <item name="android:fontFamily">@font/noto_sans</item>
</style>
```

The `ToolbarStyle` style uses both `TextAppearance` and `style` elements.
The `Toolbar` widget allows itself to be customized thru the use of
`app:titleTextAppearance` and `app:titleTextColor`. These are both
used in the `ToolbarStyle`, however, the `app:` namespace has to be used if
this was applied to the `android.support.v7.widget.Toolbar` in the XML layout
directly. Here's an example of what that would look like.

```xml
<android.support.v7.widget.Toolbar
    android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:titleTextAppearance="..."          <- need namespace
    app:titleTextColor="..."               <- need namespace
    app:title="@string/app_name"
    style="@style/Toolbar" />
```

The `ToolbarTextAppearance` is used in the `styles.xml` to set a custom font, which
is then used by the `ToolbarStyle` style itself. This style is then used on the `Toolbar`
widget.

### Paint and text (baseline)
In order to display the current count vertically centered inside of the `Canvas` a lot of
work has to be done in order to make this happen. This is due to the fact that the `Paint`
object interprets the Y value passed to it, to mean the baseline of the text. Here's 
[more information](https://stackoverflow.com/a/42091739/2085356) on baseline, `FontMetrics`
ascent and descent. In order to vertically center the text so that the `Paint` can actually
`drawText()` at the vertical center of the `Canvas` the following code has to be used.

```kotlin
override fun onDraw(canvas: Canvas) {
        val width = canvas.width
        val height = canvas.height

        val fontMetrics = helpers.textPaint.getFontMetrics()
        val baselineY = height / 2f + (-fontMetrics.ascent / 2) - (fontMetrics.descent / 2)

        // Draw text
        with(helpers) {
            val content = "❤ ${String.format(Locale.getDefault(), "%03d", count)}"
            val centerX = width * 0.5f
            val textWidth = textPaint.measureText(content)
            val textX = centerX - textWidth * 0.5f
            canvas.drawText(content, textX, baselineY, textPaint)
        }

    }
```

## Custom ViewGroup
todo

## Custom LayoutManager
todo

## Custom Scrolling
todo

# Reference info
The following links are relevant to all the background material that is relevant to the
creation of this project.

## Custom Views
- [Video on Android Custom Views](https://vimeo.com/242155617)
- [Book on Android Custom Views, Drawables, LayoutManagers, Scrolling](https://play.google.com/books/reader?id=dnr_CgAAQBAJ&printsec=frontcover&output=reader&hl=en&pg=GBS.PT400)
- [Tutorial on creating custom views](https://www.raywenderlich.com/175645/android-custom-view-tutorial)

## Custom Views and Kotlin constructors using @JvmOverloads
- [Kotlin and Android custom view constructors](https://antonioleiva.com/custom-views-android-kotlin/)

## Android UI rendering
- [Video about Android UI rendering from Google IO 18](https://youtu.be/zdQRIYOST64)

## TextView and baseline
- [Android TextView height, FontMetrics, and baseline](https://stackoverflow.com/a/42091739/2085356)

## Styled attribute sets in XML
- [More information on Styled Attribute Sets in XML](http://blog.danlew.net/2016/07/19/a-deep-dive-into-android-view-constructors/)

## Theming
- [Theming Android Buttons](https://medium.com/android-bits/android-material-button-e7b92cb014e0)
- [Android custom themes](https://guides.codepath.com/android/developing-custom-themes)
- [TextAppearance vs Styles](http://blog.danlew.net/2014/11/19/styles-on-android/)
- [Toolbar TextAppearance and Style](https://stackoverflow.com/questions/26533510/android-toolbar-center-title-and-custom-font/40916343#40916343)
- [App namespace in styles](https://stackoverflow.com/questions/39336054/using-app-namespace-in-style/39336520)

## RelativeLayout
- [Toolbar and RelativeLayout](https://stackoverflow.com/questions/27103954/is-the-toolbar-able-to-work-in-relativelayout)

## Animation
- [View Property Animators tutorial](https://android-developers.googleblog.com/2011/05/introducing-viewpropertyanimator.html)

## ConstraintLayout
- [ConstraintLayout Guide](https://developer.android.com/reference/android/support/constraint/ConstraintLayout#CenteringPositioning)
- [ConstraintLayout Chains Tutorial](https://medium.com/@nomanr/constraintlayout-chains-4f3b58ea15bb)
