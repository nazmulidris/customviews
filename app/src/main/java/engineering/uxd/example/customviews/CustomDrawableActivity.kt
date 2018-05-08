package engineering.uxd.example.customviews

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
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

    private fun attachDrawable() {
        imageView.backgroundDrawable = SimpleTextDrawable()
    }

}

data class Config(val text_content: String = "Hello World!",
                  val text_color: Int = 0xFF311B92.toInt(),
                  val text_paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG),
                  val text_size: Float = 100f,
                  val bg_paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG),
                  val bg_color: Int = 0xABABAB92.toInt())

class SimpleTextDrawable : Drawable(), AnkoLogger {

    val config = Config()

    init {
        with(config) {
            text_paint.color = text_color
            text_paint.textSize = text_size
            bg_paint.color = bg_color
        }
    }

    override fun getIntrinsicHeight(): Int {
        val height = config.text_paint.textSize.toInt()
        info { "intrinsic height= $height" }
        return height
    }

    override fun getIntrinsicWidth(): Int {
        with(config) {
            val width = text_paint.measureText(text_content).toInt()
            info { "intrinsic width= $width" }
            return width
        }
    }

    override fun draw(canvas: Canvas) {
        info {
            "draw something in bounds: " +
                    "width=${bounds.right - bounds.left}, " +
                    "height=${bounds.bottom - bounds.top}"
        }
        with(config) {
            canvas.drawRect(bounds, bg_paint)
            canvas.drawText(text_content, 0F, text_size, text_paint)
        }
    }

    override fun setAlpha(alpha: Int) {
        config.text_paint.alpha = alpha
        config.bg_paint.alpha = alpha
        invalidateSelf()
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

}