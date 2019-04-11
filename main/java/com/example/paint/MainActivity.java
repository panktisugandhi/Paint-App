package com.example.paint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.UUID;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton currpaint,drawbtn,newbtn,erase,save;
    private DrawingView drawView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = (DrawingView)findViewById(R.id.drawing);
        drawbtn = (ImageButton) findViewById(R.id.draw_btn);
        newbtn = (ImageButton) findViewById(R.id.new_btn);
        erase = (ImageButton) findViewById(R.id.erase_btn);
        save = (ImageButton) findViewById(R.id.save_btn);
        LinearLayout paintlayout = (LinearLayout) findViewById(R.id.paint_color);
        currpaint = (ImageButton)paintlayout.getChildAt(0);

        currpaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
        drawbtn.setOnClickListener(this);
        newbtn.setOnClickListener(this);
        erase.setOnClickListener(this);
        save.setOnClickListener(this);

    }

    public void paintClicked(View view){
        if (view!=currpaint) {
            ImageButton imgview = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            imgview.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currpaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currpaint = (ImageButton)view;
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.draw_btn){
            drawView.setupDrawing();
        }
        if (v.getId()==R.id.erase_btn){
            drawView.setErase(true);
           drawView.setBrushSize(25);
        }

        if (v.getId()==R.id.new_btn){
            AlertDialog.Builder newdialog = new AlertDialog.Builder(this);
            newdialog.setTitle("New Drawing");
            newdialog.setMessage("Start new Drawing");
            newdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            newdialog.show();
        }

        if (v.getId()==R.id.save_btn){
            AlertDialog.Builder savedialog = new AlertDialog.Builder(this);
            savedialog.setTitle("Save Drawing");
            savedialog.setMessage("Save drawing to Gallery ???");
            savedialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   drawView.setDrawingCacheEnabled(true);
                   String imgsave = MediaStore.Images.Media.insertImage(getContentResolver(),drawView.getDrawingCache(), UUID.randomUUID().toString()+".png","drawing");
                   if (imgsave!=null){
                       Toast savetoast = Toast.makeText(getApplicationContext(),"Drawing saved to Gallery!!!",Toast.LENGTH_SHORT);
                       savetoast.show();
                   }
                   else {
                       Toast.makeText(getApplicationContext(), "Image could not save!!!", Toast.LENGTH_SHORT).show();
                   }
                   drawView.destroyDrawingCache();
                }
            });
            savedialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   dialog.cancel();
                }
            });
            savedialog.show();
        }


    }
}
