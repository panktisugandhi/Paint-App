package com.example.paint;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.content.Context;

public class DrawingView extends View{

    private Path drawpath;
    private boolean erase=false;
    private Paint drawPaint,canvasPaint;
    private Canvas drawCanvas;
    private int paintcolor=0xFF660000;
    private Bitmap canvasBitmap;
    private float brushSize,lastBrushSize;

    public DrawingView(Context context,AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    public void startNew(){
        drawCanvas.drawColor(0,PorterDuff.Mode.CLEAR);
        invalidate();
    }
    public void setErase(boolean isErase){
        erase = isErase;
        if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);
    }
    public void setBrushSize(float newsize){
        float pixelamount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,newsize,getResources().getDisplayMetrics());
        brushSize = pixelamount;
        drawPaint.setStrokeWidth(brushSize);
    }
    public void setLastBrushSize(float lastSize){
        lastBrushSize = lastSize;
    }
    public float getBrushSize(){
        return  lastBrushSize;
    }

    @Override
    protected void onSizeChanged(int w,int h,int oldw,int oldh){
        super.onSizeChanged(w,h,oldw,oldh);
        canvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawBitmap(canvasBitmap,0,0,canvasPaint);
        canvas.drawPath(drawpath,drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float touchx = event.getX();
        float touchy = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                drawpath.moveTo(touchx,touchy);
                break;
            case MotionEvent.ACTION_MOVE:
                drawpath.lineTo(touchx,touchy);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawpath,drawPaint);
                drawpath.reset();
                break;
            default:
                    return false;
        }
        invalidate();
        return true;
    }

    public void setColor(String newcolor){
        invalidate();
        paintcolor = Color.parseColor(newcolor);
        drawPaint.setColor(paintcolor);

    }
    public void setupDrawing(){
        drawpath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintcolor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
        brushSize = 20;
        lastBrushSize = brushSize;
        drawPaint.setStrokeWidth(brushSize);
    }

}
