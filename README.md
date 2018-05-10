# customviews

This project is about the exploration of custom views. It explores the following.

1. Custom Drawable
2. Custom View (todo from book and video)
3. Custom LayoutManager (todo from video)
4. Custom Scrolling (todo from book)

## Custom Drawable
The `ImageView` has it's background set to `SimpleTextDrawable` and it's height and width are
not explicitly set; the layout params of `wrap_content` is set for both the height and width.
```kotlin
imageView.backgroundDrawable = SimpleTextDrawable()
```

The `SimpleTextDrawable` sets its own intrinsic height and width based on the size of the
text it has to display. This information is passed to the `ImageView`, which then sets
its actual width and height to this intrinsic width and height when measure and layout is
performed.

## Custom View
todo

## Custom LayoutManager
todo

## Custom Scrolling
todo

# Reference info
- [ConstraintLayout Guide](https://developer.android.com/reference/android/support/constraint/ConstraintLayout#CenteringPositioning)
- [ConstraintLayout Chains Tutorial](https://medium.com/@nomanr/constraintlayout-chains-4f3b58ea15bb)
- [Video on Android Custom Views](http://oredev.org/2017/sessions/measure-layout-draw-repeat-custom-views-and-viewgroups)
- [Book on Android Custom Views, Drawables, LayoutManagers, Scrolling](https://play.google.com/books/reader?id=dnr_CgAAQBAJ&printsec=frontcover&output=reader&hl=en&pg=GBS.PT400)