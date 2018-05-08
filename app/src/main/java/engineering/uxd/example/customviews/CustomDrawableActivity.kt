package engineering.uxd.example.customviews

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_custom_drawable.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.info

class CustomDrawableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_drawable)
        attachDrawable()
    }

    fun attachDrawable() {
        imageView.backgroundDrawable = SimpleTextDrawable()
    }
}

data class Config(val text: String = "Hello World!",
                  val color: Long = 0xFF311B92)

class SimpleTextDrawable : Drawable(), AnkoLogger {

    val config = Config()

    override fun draw(canvas: Canvas) {
        info { "draw something" }
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

}