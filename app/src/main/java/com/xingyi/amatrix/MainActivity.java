package com.xingyi.amatrix;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView martrix;
    private MartrixImageView bitmapMartrix;
    private LinearLayout allItemContainer;
    private List<TextViewEntity> data;
    private TextView calendarDesc;
    private WithdrawClearEditText clearEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        createData();

        martrix = (TextView) findViewById(R.id.tv_hello);
        bitmapMartrix = (MartrixImageView) findViewById(R.id.iv_bitmap);
        allItemContainer = (LinearLayout) findViewById(R.id.ll_all_item);

        setTextViewContent(martrix, data.get(0));
        addItem2View(data);
        allItemContainer.setVisibility(View.GONE);
        martrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTVMartrx();
            }
        });

        bitmapMartrix.setOpenListener(new MartrixImageView.OpenListener() {
            @Override
            public void openOrClose(boolean open) {
                hideOrShowContainer(open);
            }
        });

        calendarDesc = (TextView) findViewById(R.id.tv_calendar);
        clearEditText = (WithdrawClearEditText) findViewById(R.id.cet_date);

        calendarDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                boolean temp = showExpiredNotice(clearEditText.getText().toString());
//                calendarDesc.setText("temp--->" + temp);
                try {

                    String all = clearEditText.getText().toString();
                    String current = all.substring(0, all.length() - 3);
                    calendarDesc.setText(showExpiredNoticeDesc(current));
                    Toast.makeText(MainActivity.this, showExpiredNoticeDesc(clearEditText.getText().toString()), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e("---", e.toString());
                }

            }
        });
    }


    private boolean showExpiredNotice(String expiredDesc) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date expiredData;
        Calendar expiredC = Calendar.getInstance();
        Calendar currentC = Calendar.getInstance();

        boolean showExpired;
        try {
            expiredData = sdf.parse(expiredDesc);
            expiredC.setTime(expiredData);
            Log.e("---信用卡过期日期---", sdf.format(expiredC.getTime()));
            currentC.setTime(new Date());
            Log.e("---当前日期---", sdf.format(currentC.getTime()));
            currentC.add(Calendar.MONTH, 6);
            Log.e("---当前日期加6个月后的日期---", sdf.format(currentC.getTime()));
            showExpired = currentC.compareTo(expiredC) >= 0;
        } catch (ParseException e) {
            showExpired = false;
            Log.e("---日期转换异常", e.toString());
        }
        return showExpired;
    }


    private String showExpiredNoticeDesc(String expiredDesc) {
        String notice;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date expiredData;
        Calendar expiredC = Calendar.getInstance();
        Calendar currentC = Calendar.getInstance();

        try {
            expiredData = sdf.parse(expiredDesc);
            expiredC.setTime(expiredData);
            Log.e("---信用卡过期日期---", sdf.format(expiredC.getTime()));
            currentC.setTime(new Date());
            Log.e("---当前日期---", sdf.format(currentC.getTime()));

            String todayDesc = sdf.format(new Date());
            if (expiredDesc.equals(todayDesc)) {
                // 说明这个月过期
                notice = "您的信用卡有效期将在6个月内过期，请及时申领新卡或修改有效期";
                return notice;
            } else {
                int tempCom = currentC.compareTo(expiredC);
                if (tempCom > 0) {
                    // 先和当天日期做对比,判断是否已经过期
                    notice = "您上次使用的信用卡已过期，请及时申领新卡或修改有效期";
                    return notice;
                } else {
                    // 如果没有过期,在和六个月内的日期做对比
                    currentC.add(Calendar.MONTH, 6);
                    Log.e("---当前日期加6个月后的日期---", sdf.format(currentC.getTime()));
                    if (currentC.compareTo(expiredC) >= 0) {
                        notice = "您的信用卡有效期将在6个月内过期，请及时申领新卡或修改有效期";
                    } else {
                        notice = "";
                    }
                    return notice;
                }
            }
        } catch (ParseException e) {
            Log.e("---日期转换异常", e.toString());
            notice = "";
        }
        return notice;
    }


    /**
     * 构建模拟数据
     */
    private void createData() {
        data = new ArrayList<>();
        TextViewEntity entity = new TextViewEntity();
        entity.setBgColor(ContextCompat.getColor(this, android.R.color.holo_green_light));
        entity.setDrawableRes(R.mipmap.icon_8);
        entity.setTextDesc("Android");
        data.add(entity);

        entity = new TextViewEntity();
        entity.setBgColor(ContextCompat.getColor(this, android.R.color.holo_orange_light));
        entity.setDrawableRes(R.mipmap.icon_7);
        entity.setTextDesc("iOS");
        data.add(entity);

        entity = new TextViewEntity();
        entity.setBgColor(ContextCompat.getColor(this, android.R.color.holo_purple));
        entity.setDrawableRes(R.mipmap.icon_6);
        entity.setTextDesc("Html5");
        data.add(entity);

        entity = new TextViewEntity();
        entity.setBgColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
        entity.setDrawableRes(R.mipmap.icon_2);
        entity.setTextDesc("ReactNative");
        data.add(entity);


    }

    private void changeTVMartrx() {
        Bitmap bitmapFromView = getBitmapFromView(martrix);
        bitmapMartrix.setBitmap(bitmapFromView);
        bitmapMartrix.setMatrixDegree(45);
        martrix.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取view的bitmap
     *
     * @param v
     * @return
     */
    public static Bitmap getBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        // Draw background
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(c);
        else
            c.drawColor(Color.WHITE);
        // Draw view to canvas
        v.draw(c);
        return b;
    }

    public static class TextViewEntity {
        private int bgColor;
        private String textDesc;
        private int drawableRes;

        public int getBgColor() {
            return bgColor;
        }

        public void setBgColor(int bgColor) {
            this.bgColor = bgColor;
        }

        public String getTextDesc() {
            return textDesc;
        }

        public void setTextDesc(String textDesc) {
            this.textDesc = textDesc;
        }

        public int getDrawableRes() {
            return drawableRes;
        }

        public void setDrawableRes(int drawableRes) {
            this.drawableRes = drawableRes;
        }
    }

    private TextView creatTextView(TextViewEntity entity) {
        TextView tv = new TextView(this);
        tv.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setBackgroundColor(entity.getBgColor());
        tv.setText(entity.getTextDesc());
        Drawable drawable = ContextCompat.getDrawable(this, entity.getDrawableRes());
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv.setCompoundDrawables(null, null, drawable, null);
        int dp2px = dp2px(this, 5);
        tv.setPadding(dp2px, dp2px, dp2px, dp2px);
        return tv;
    }

    private static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    private void addItem2View(List<TextViewEntity> data) {
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                final TextViewEntity temp = data.get(i);
                TextView textView = creatTextView(temp);
                allItemContainer.addView(textView);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        martrix.setVisibility(View.VISIBLE);
                        setTextViewContent(martrix, temp);
                        changeTVMartrx();
                    }
                });
            }
        }
    }

    private void setTextViewContent(TextView textView, TextViewEntity entity) {
        textView.setText(entity.getTextDesc());
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setBackgroundColor(entity.getBgColor());
        Drawable drawable = ContextCompat.getDrawable(MainActivity.this, entity.getDrawableRes());
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
        int dp2px = dp2px(MainActivity.this, 5);
        textView.setPadding(dp2px, dp2px, dp2px, dp2px);
    }

    /**
     * 显示或者隐藏item布局
     *
     * @param openState
     */
    private void hideOrShowContainer(boolean openState) {
        allItemContainer.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator;
        if (openState) {
            valueAnimator = ValueAnimator.ofFloat(0.F, 1.F);
        } else {
            valueAnimator = ValueAnimator.ofFloat(1.F, 0.F);
        }
        valueAnimator.setDuration(350);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = Float.valueOf(animation.getAnimatedValue().toString());
                // 动态的计算当前容器的高度
                int currentHeight = (int) (bitmapMartrix.getHeight() * data.size() * percent);
                if (percent >= 1) {
                    currentHeight += data.size() * 5;
                }
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, currentHeight);
                params.addRule(RelativeLayout.BELOW, R.id.fl_select_contaier);
                allItemContainer.setLayoutParams(params);
            }
        });
        valueAnimator.start();
    }
}
