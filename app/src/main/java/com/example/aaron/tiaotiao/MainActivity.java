package com.example.aaron.tiaotiao;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aaron.tiaotiao.Activities.AccountActivity;
import com.example.aaron.tiaotiao.Fragments.GuideFragment;
import com.example.aaron.tiaotiao.Fragments.PartnerFragment;
import com.example.aaron.tiaotiao.Fragments.RecommendFragment;
import com.nineoldandroids.view.ViewHelper;


public class MainActivity extends FragmentActivity {

    private DrawerLayout mDrawerLayout;// 控件对象
    private Button button_1, button_2, button_3;
    private Fragment fragment_1, fragment_2, fragment_3, current_Fragment;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvents();

        if (savedInstanceState == null) {
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment_1)
                    .commit();
            current_Fragment = fragment_1;
            ((TextView) findViewById(R.id.fragment_title)).setText("换宿");
        }
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout);
        button_1 = (Button) findViewById(R.id.button_1);
        button_2 = (Button) findViewById(R.id.button_2);
        button_3 = (Button) findViewById(R.id.button_3);

        button_1.setOnClickListener(new myButtonClickListener());
        button_2.setOnClickListener(new myButtonClickListener());
        button_3.setOnClickListener(new myButtonClickListener());

        fragment_1 = new RecommendFragment();
        fragment_2 = new PartnerFragment();
        fragment_3 = new GuideFragment();
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.content_frame, fragment_3, "Tag_3")
                .add(R.id.content_frame, fragment_2, "Tag_2")
                .add(R.id.content_frame, fragment_1, "Tag_1")
                .commit();
        //最后add的显示在最上层
        //save fragment instance to use when fragment changes

        (findViewById(R.id.titlebar_button_left)).setOnClickListener(new myButtonClickListener());
        (findViewById(R.id.userPic)).setOnClickListener(new myButtonClickListener());
    }

    private void initEvents() {
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = mDrawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;

                if (drawerView.getTag().equals(
                        getResources().getString(R.string.left_tag))) {// 展开左侧菜单
                    float leftScale = 1 - 0.3f * scale;

                    // 设置左侧菜单缩放效果
                    ViewHelper.setScaleX(mMenu, leftScale);
                    ViewHelper.setScaleY(mMenu, leftScale);
                    ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));

                    // 设置中间View缩放效果
                    ViewHelper.setTranslationX(mContent,
                            mMenu.getMeasuredWidth() * (1 - scale));
                    ViewHelper.setPivotX(mContent, 0);
                    ViewHelper.setPivotY(mContent,
                            mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);
                } else {// 展开右侧菜单
                    // 设置中间View缩放效果
                    ViewHelper.setTranslationX(mContent,
                            -mMenu.getMeasuredWidth() * slideOffset);
                    ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
                    ViewHelper.setPivotY(mContent,
                            mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerLayout.closeDrawers();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
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

    class myButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Fragment fragment = null;

            switch (v.getId()) {
                case R.id.button_1:
                    fragment = fragmentManager.findFragmentByTag("Tag_1");
                    if (fragment == null) {
                        fragment = new RecommendFragment();
                        fragmentManager.beginTransaction()
                                .add(R.id.content_frame, fragment, "Tag_1")
                                .commit();
                    }
                    fragmentManager.beginTransaction()
                            .hide(current_Fragment)
                            .show(fragment)
                            .commit();
                    current_Fragment = fragment;
                    ((TextView) findViewById(R.id.fragment_title)).setText("换宿");
                    mDrawerLayout.closeDrawers();
                    break;

                case R.id.button_2:
                    fragment = fragmentManager.findFragmentByTag("Tag_2");
                    if (fragment == null) {
                        fragment = new PartnerFragment();
                        fragmentManager.beginTransaction()
                                .add(R.id.content_frame, fragment, "Tag_2")
                                .commit();
                    }
                    fragmentManager.beginTransaction()
                            .hide(current_Fragment)
                            .show(fragment)
                            .commit();
                    current_Fragment = fragment;
                    ((TextView) findViewById(R.id.fragment_title)).setText("小伙伴");
                    mDrawerLayout.closeDrawers();
                    break;

                case R.id.button_3:
                    fragment = fragmentManager.findFragmentByTag("Tag_3");
                    if (fragment == null) {
                        fragment = new GuideFragment();
                        fragmentManager.beginTransaction()
                                .add(R.id.content_frame, fragment, "Tag_3")
                                .commit();
                    }
                    fragmentManager.beginTransaction()
                            .hide(current_Fragment)
                            .show(fragment)
                            .commit();
                    current_Fragment = fragment;
                    ((TextView) findViewById(R.id.fragment_title)).setText("攻略");
                    mDrawerLayout.closeDrawers();
                    break;

                case R.id.userPic:
                    startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                    mDrawerLayout.closeDrawers();
                    break;

                case R.id.titlebar_button_left:
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                    break;
            }

        }
    }

}
