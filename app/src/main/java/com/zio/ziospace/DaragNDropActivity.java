package com.zio.ziospace;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DaragNDropActivity extends AppCompatActivity {

    public String[] cond, todos;
    public String input;
    LinearLayout codeviewlayout;
    ConstraintLayout keyboard;
    TextView t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18;
    TextView codeview, insts;
    int i = 0;
    private View.OnTouchListener myClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstmain);

        Intent intent = new Intent();
        intent.putExtra("tdf", cond);
        cond = getIntent().getStringArrayExtra("conds");
        todos = getIntent().getStringArrayExtra("todos");

        codeviewlayout = findViewById(R.id.targetact);
        keyboard = findViewById(R.id.startact);
        insts = findViewById(R.id.heds);
        codeview = findViewById(R.id.randomc);

        updateviewda();

        t0 = findViewById(R.id.q0);
        t1 = findViewById(R.id.q1);
        t2 = findViewById(R.id.q2);
        t3 = findViewById(R.id.q3);
        t4 = findViewById(R.id.q4);
        t5 = findViewById(R.id.q5);
        t6 = findViewById(R.id.q6);
        t7 = findViewById(R.id.q7);
        t8 = findViewById(R.id.q8);
        t9 = findViewById(R.id.q9);
        t10 = findViewById(R.id.q10);
        t11 = findViewById(R.id.q11);

        myClickListener = new MyClickListener();

        t0.setOnTouchListener(myClickListener);
        t1.setOnTouchListener(myClickListener);
        t2.setOnTouchListener(myClickListener);
        t3.setOnTouchListener(myClickListener);
        t4.setOnTouchListener(myClickListener);
        t5.setOnTouchListener(myClickListener);
        t6.setOnTouchListener(myClickListener);
        t7.setOnTouchListener(myClickListener);
        t8.setOnTouchListener(myClickListener);
        t9.setOnTouchListener(myClickListener);
        t10.setOnTouchListener(myClickListener);
        t11.setOnTouchListener(myClickListener);

        codeviewlayout.setOnDragListener(new MyDragListener());
        keyboard.setOnDragListener(new MyDragListener());

    }

    public void erfg(View view) {
        String x = codeview.getText().toString().replace(" ", "");
        String y = x.replace("\n", "");
        Pattern p = Pattern.compile(cond[i - 1]);
        Matcher m = p.matcher(y);
        if (m.find()) {
            Toast.makeText(getApplicationContext(), "good job", Toast.LENGTH_LONG).show();
            updateviewda();

        }
        Log.d("output", cond[0] + "-" + y);
    }

    private void addview(TextView view) {
        codeview.append((CharSequence) view.getTag());
    }

    public void resetit(View view) {
        codeview.setText("");
    }

    private void updateviewda() {
        insts.setText("todos");
        codeview.setText("");
        i++;
    }

    private final class MyClickListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());


            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

            view.startDrag(data, //data to be dragged
                    shadowBuilder, //drag shadow
                    view, //local data about the drag and drop operation
                    0   //no needed flags
            );
            return true;
        }

    }

    class MyDragListener implements View.OnDragListener {
        Drawable normalShape = getResources().getDrawable(R.drawable.shapes);
        Drawable targetShape = getResources().getDrawable(R.drawable.shape_drop_target);

        @Override
        public boolean onDrag(View v, DragEvent event) {

            // Handles each of the expected events
            switch (event.getAction()) {

                //signal for the start of a drag and drop operation.
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;

                case DragEvent.ACTION_DRAG_LOCATION:
                    break;

                //the drag point has entered the bounding box of the View
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackground(targetShape);   //change the shape of the view
                    break;

                //the user has moved the drag shadow outside the bounding box of the View
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackground(normalShape);   //change the shape of the view back to normal
                    break;

                //drag shadow has been released,the drag point is within the bounding box of the View
                case DragEvent.ACTION_DROP:
                    // if the view is the bottomlinear, we accept the drag item
                    if (v == findViewById(R.id.targetact)) {
                        TextView view = (TextView) event.getLocalState();
                        ViewGroup viewgroup = (ViewGroup) view.getParent();
                        //viewgroup.removeView(view);

                        LinearLayout containView = (LinearLayout) v;
                        //containView.addView(view);
                        view.setVisibility(View.VISIBLE);
                        String x = view.getTag().toString();
                        //updateviewda(x);
                        addview(view);
                        view.setOnTouchListener(new MyClickListener());
                    } else {
                        View view = (View) event.getLocalState();
                        view.setVisibility(View.VISIBLE);
                        Toast.makeText(DaragNDropActivity.this, "You can't drop the image here",
                                Toast.LENGTH_LONG).show();
                        break;
                    }
                    break;

                //the drag and drop operation has concluded.
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackground(normalShape);   //go back to normal shape

                default:
                    break;
            }
            return true;
        }
    }

}