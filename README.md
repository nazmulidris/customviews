# Android Custom Views

This project is about the exploration of custom views. It explores the following.

1. Custom Drawable
2. Custom View
3. Custom LayoutManager (todo from video)
4. Custom Scrolling (todo from book)

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

The `EmotionalFaceView` figures out
how much space it has by asking its parent for its size, then measures itself by picking
a height and width that preserves its aspect ration (regardless of how much space there
is for it to actually fill).

### Styling in XML
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

## Custom LayoutManager
todo

## Custom Scrolling
todo

# Reference info
- [ConstraintLayout Guide](https://developer.android.com/reference/android/support/constraint/ConstraintLayout#CenteringPositioning)
- [ConstraintLayout Chains Tutorial](https://medium.com/@nomanr/constraintlayout-chains-4f3b58ea15bb)
- [Video on Android Custom Views](http://oredev.org/2017/sessions/measure-layout-draw-repeat-custom-views-and-viewgroups)
- [Book on Android Custom Views, Drawables, LayoutManagers, Scrolling](https://play.google.com/books/reader?id=dnr_CgAAQBAJ&printsec=frontcover&output=reader&hl=en&pg=GBS.PT400)
- [Video about Android UI rendering from Google IO 18](https://youtu.be/zdQRIYOST64)
- [Tutorial on creating custom views](https://www.raywenderlich.com/175645/android-custom-view-tutorial)
- [More information on Styled Attribute Sets in XML](http://blog.danlew.net/2016/07/19/a-deep-dive-into-android-view-constructors/)
- [View Property Animators tutorial](https://android-developers.googleblog.com/2011/05/introducing-viewpropertyanimator.html)