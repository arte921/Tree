package arte921.tree

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

var maxx: Int = 0
var maxy: Int = 0

class TreeView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private val bgColor = ResourcesCompat.getColor(resources,R.color.bgColor,null)
    private val branchColor = ResourcesCompat.getColor(resources,R.color.branchColor,null)

    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    private var endxa: Double = 0.0
    private var endya: Double = 0.0
    private var endxb: Double = 0.0
    private var endyb: Double = 0.0
    private var rota: Double = 0.0
    private var rotb: Double = 0.0

    private val scalingFactor: Double = (1+sqrt(5.0))/2

    private val branchPaint = Paint().apply {
        color = branchColor
        isAntiAlias = true
        isDither = true
        style = Paint.Style.FILL
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 2f
    }

    override fun onSizeChanged(width:Int,height:Int,oldWidth:Int,oldHeight:Int) {
        super.onSizeChanged(width,height,oldWidth,oldHeight)

        if(::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(bgColor)
        maxx = width
        maxy = height
    }

    override fun onDraw(canvas: Canvas){
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap,0F,0F,null)

        newBranch(0.0,150.0,640.0,250.0,canvas)
    }

    fun newBranch(rotation: Double, posx: Double, posy: Double, len: Double, canvas: Canvas){
        if(len > 0.5){
            rota = (2 * PI + rotation + 0.3 * PI) % (2 * PI)
            endxa = cos(rota)*len+posx
            endya = sin(rota)*len+posy

            canvas.drawLine(posx.toFloat(),posy.toFloat(),endxa.toFloat(),endya.toFloat(),branchPaint)
            newBranch(rota,endxa,endya,len/scalingFactor,canvas)


            rotb = (2 * PI + rotation - 0.3 * PI) % (2 * PI)
            endxb = cos(rotb)*len+posx
            endyb = sin(rotb)*len+posy

            canvas.drawLine(posx.toFloat(),posy.toFloat(),endxb.toFloat(),endyb.toFloat(),branchPaint)
            newBranch(rotb,endxb,endyb,len/scalingFactor,canvas)
        }

    }
}
