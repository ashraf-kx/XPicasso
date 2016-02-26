package com.example.angel.xpicasso;

import android.support.v7.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageView XImageView;
    private ProgressBar XProgressBar;
    private Button bt;
    private Toast     XToast;
    private boolean   XToasted;

    private Canvas XCanvas;
    private Paint  XPaint;
    private Bitmap IMG_Bitmap;
    private Path   XPath;

    private Animation hyperspaceJumpAnimation;

    private int Startx,Starty,Endx,Endy,RotateValue,i,offSet;
    public  float[] pts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display currentDisplay  = getWindowManager().getDefaultDisplay();
        setContentView(R.layout.activity_main);

        XImageView  = (ImageView) findViewById(R.id.IV_Screen);
        XProgressBar= (ProgressBar) findViewById(R.id.XProgressBar);
        hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.first_animation);


        IMG_Bitmap  = Bitmap.createBitmap(currentDisplay.getWidth(),currentDisplay.getHeight(),Bitmap.Config.ARGB_8888);
        XCanvas     = new Canvas(IMG_Bitmap);
        XPaint      = new Paint();
        XPath       = new Path();

        XPaint.setColor(Color.MAGENTA);
        XPaint.setAntiAlias(true);
        XPaint.setStrokeWidth(7);

        XImageView.setImageBitmap(IMG_Bitmap);

        pts = new float[200000];  // 32-bit * 200000 = 6250 Kb ( 6Mb ).
        i=0;
        offSet =0;
        XToasted = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.XActionClear :
                XCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                XPath.reset();
                XImageView.invalidate();
                break;
            case R.id.BlackColor : XPaint.setColor(Color.BLACK); break;
            case R.id.RedColor :   XPaint.setColor(Color.RED);   break;
            case R.id.GreenColor : XPaint.setColor(Color.GREEN); break;
            case R.id.BlueColor :  XPaint.setColor(Color.BLUE);  break;

            case R.id.Stroke1: XPaint.setStrokeWidth(1); break;
            case R.id.Stroke2: XPaint.setStrokeWidth(2); break;
            case R.id.Stroke3: XPaint.setStrokeWidth(3); break;
            case R.id.Stroke4: XPaint.setStrokeWidth(4); break;

            case R.id.FILL:        XPaint.setStyle(Paint.Style.FILL);            break;
            case R.id.FILL_STROKE: XPaint.setStyle(Paint.Style.FILL_AND_STROKE); break;
            case R.id.STROKE:      XPaint.setStyle(Paint.Style.STROKE);          break;
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                XProgressBar.setVisibility(View.INVISIBLE);
                pts = null;
                pts = new float[200000];  // 32-bit * 200000 = 6250 Kb ( 6Mb ).
                pts[i] = event.getRawX();
                i++;
                pts[i] = event.getRawY();
                i++;
                break;
            case MotionEvent.ACTION_MOVE:
                pts[i] = event.getRawX();
                i++;
                pts[i] = event.getRawY();
                i++;
                break;
            case MotionEvent.ACTION_UP:
                offSet = i;
                XProgressBar.setMax(offSet);
                XProgressBar.setVisibility(View.VISIBLE);
                XProgressBar.startAnimation(hyperspaceJumpAnimation);

                PiccasoHand FirstHand = new PiccasoHand();
                FirstHand.start();

                //if(XToasted) XToast.cancel();
                //XToast = Toast.makeText(getBaseContext(), String.valueOf(Endx)+":"+String.valueOf(Endy),Toast.LENGTH_SHORT);
                //XToast.show();
                //XToasted = true;
                i = 0;
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /** original PicassoHand **/
    public class PiccasoHand extends Thread{

        float[] mPts = pts.clone();
        int i;

        public void run(){
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (i=0; i< offSet;){
                XCanvas.drawPoint(mPts[i], mPts[i + 1], XPaint);
                XProgressBar.setProgress(i);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        XImageView.setImageBitmap(IMG_Bitmap);
                        //_imageView.invalidate();
                    }
                });
                i+=2;
            }
            XProgressBar.setProgress(offSet);
        }
    }
    /** Test  **/
    public void  drawFirst(Canvas canvas,int x,int y){
        int val=80; int valLine=15;
        Paint paint = new Paint();
        int x_TopLEFT, y_TopLEFT;
        int x_TopRIGHT, y_TopRIGHT;
        int x_BottomLEFT, y_BottomLEFT;
        int x_BottomRIGHT, y_BottomRIGHT;
        /** Get Coordination For Each corner Edge **/
        x_TopLEFT    = x-val; y_TopLEFT    = y-val;
        x_TopRIGHT   = x+val; y_TopRIGHT   = y-val;
        x_BottomLEFT = x-val; y_BottomLEFT = y+val;
        x_BottomRIGHT= x+val; y_BottomRIGHT= y+val;

        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(3);

        /** Draw Center **/
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        canvas.drawPoint(x, y, paint);
        /** Draw 4 Lines Corners **/
        paint.setStrokeWidth(2);
        canvas.drawLine(x_TopLEFT, y_TopLEFT, x_TopLEFT + valLine, y_TopLEFT, paint);
        canvas.drawLine(x_TopLEFT, y_TopLEFT, x_TopLEFT, y_TopLEFT + valLine, paint);
        canvas.drawLine(x_TopRIGHT,y_TopLEFT,x_TopRIGHT-valLine,y_TopRIGHT,paint);
        canvas.drawLine(x_TopRIGHT,y_TopLEFT,x_TopRIGHT,y_TopRIGHT+valLine,paint);
        canvas.drawLine(x_BottomLEFT,y_BottomLEFT,x_BottomLEFT,y_BottomLEFT-valLine,paint);
        canvas.drawLine(x_BottomLEFT,y_BottomLEFT,x_BottomLEFT+valLine,y_BottomLEFT,paint);
        canvas.drawLine(x_BottomRIGHT, y_BottomRIGHT, x_BottomRIGHT - valLine, y_BottomRIGHT, paint);
        canvas.drawLine(x_BottomRIGHT, y_BottomRIGHT, x_BottomRIGHT, y_BottomRIGHT - valLine, paint);

    }

    public class FakeHand extends Thread{
        float[] mPts = pts.clone();
        int i;
        public void run(){
            for (i=0; i< 350;i++){
                XCanvas.drawPoint(i, i*3, XPaint);
                XCanvas.drawPoint(600-i, i*6, XPaint);
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //_imageView.setImageBitmap(IMG_Bitmap);
                        XImageView.invalidate();
                    }
                });
            }
        }
    }
}

// TODO : Create All Options Control Needed (setStyle, Color, Strike, etc) [ X ]
// TODO : Create Color Picker To Choose Pen Color.
// TODO : Visible/ Hide Menu ,Put Content behind the ActionBar.    [ X ]
// TODO : DrawPath ?? try It !                                    [ 100% ]
// TODO : Drawing on Canvas (Point by Point) ::multi-Threading::  [ 100% ]
                /** New Tasks To Learn Before Death **/
// TODO : Octa-Hand Drawing (Draw Point by Point a Set of forms Simultan.  => use Locks
// TODO : Rotate Buttons & GUIs (Animation Sections).
// TODO : Generate Skeltone ArrayPts from contour. => USE openCV4Android.
// TODO : Use FragmentsView In your App.