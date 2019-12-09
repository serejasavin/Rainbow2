package com.example.rainbow2;

import android.content.ClipData;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;

import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    TextView circle1, circle2, circle3, circle4, circle5, circle6, target;

    private List<MyCircle> circles = new ArrayList<>();

    private int panelColor;

    private int currentCircleColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//full screen
        setContentView(R.layout.activity_main);

        circles.add(new MyCircle("purple", "#581845"));
        circles.add(new MyCircle("burgundy", "#900C3F"));
        circles.add(new MyCircle("red", "#C70039"));
        circles.add(new MyCircle("orange", "#FF5733"));
        circles.add(new MyCircle("yellow", "#FFC300"));
        circles.add(new MyCircle("green", "#C9F27F"));

        circle1 = (TextView) findViewById(R.id.circle1);
        circle2 = (TextView) findViewById(R.id.circle2);
        circle3 = (TextView) findViewById(R.id.circle3);
        circle4 = (TextView) findViewById(R.id.circle4);
        circle5 = (TextView) findViewById(R.id.circle5);
        circle6 = (TextView) findViewById(R.id.circle6);
        target = (TextView) findViewById(R.id.target);

        Log.i("mytag", "onCreate: circle1 " + circle1.getId());
        Log.i("mytag", "onCreate: circle2 " + circle2.getId());
        Log.i("mytag", "onCreate: circle3 " + circle3.getId());
        Log.i("mytag", "onCreate: circle4 " + circle4.getId());
        Log.i("mytag", "onCreate: circle5 " + circle5.getId());
        Log.i("mytag", "onCreate: circle6 " + circle6.getId());
        Log.i("mytag", "onCreate: target " + target.getId());

        circle1.setOnLongClickListener(longClickListener);
        circle2.setOnLongClickListener(longClickListener);
        circle3.setOnLongClickListener(longClickListener);
        circle4.setOnLongClickListener(longClickListener);
        circle5.setOnLongClickListener(longClickListener);
        circle6.setOnLongClickListener(longClickListener);

        Collections.shuffle(circles);

        panelColor = circles.get(circles.size() - 1).getColor();

        target.setBackgroundColor(panelColor);

        target.setOnDragListener(dragListener);

    }

    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean onLongClick(View v) {
            StateListDrawable viewBackground = (StateListDrawable) v.getBackground();
            currentCircleColor = ((GradientDrawable) viewBackground.getStateDrawable(0)).getColor().getDefaultColor();
            Log.i("mytag", "onLongClick: " + viewBackground);


            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
            v.startDragAndDrop(data, myShadowBuilder, v, 0);

            return true;
        }
    };
    final View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();
            final View view = (View) event.getLocalState();
            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:

                    if (currentCircleColor == ((ColorDrawable) target.getBackground()).getColor()) {

                        // Цвета равны
                        MediaPlayer myMedia = MediaPlayer.create(MainActivity.this, R.raw.squirt);
                        myMedia.start();
                        view.animate()
                                .x(target.getX())
                                .y(target.getY())
                                .setDuration(1000)
                                .start();
                        view.invalidate();

                        for (int i = 0; i < circles.size(); i++) {
                            if (circles.get(i).getColor() == panelColor) {
                                circles.get(i).setSelected(true);
                                break;
                            }
                        }

                        boolean isWin = true;
                        for (int i = 0; i < circles.size(); i++) {
                            if (!circles.get(i).isSelected()) {
                                panelColor = circles.get(i).getColor();
                                isWin = false;
                                break;
                            }
                        }

                        if (isWin) {
                            Toast.makeText(MainActivity.this, "Ты победил!", Toast.LENGTH_SHORT).show();
                        }

                        target.setBackgroundColor(panelColor);
                        break;
                    } else {
                        Toast.makeText(MainActivity.this, "Цвета не равны!", Toast.LENGTH_SHORT).show();
                        Log.i("mytag", "onDrag: " + panelColor + " " + currentCircleColor + " " + v.getId());

                        MediaPlayer myMedia = MediaPlayer.create(MainActivity.this, R.raw.leapout);
                        myMedia.start();
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    break;
            }
            return true;
        }
    };
}
