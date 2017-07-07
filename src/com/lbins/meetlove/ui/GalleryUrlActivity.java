package com.lbins.meetlove.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.R;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.gallery.BasePagerAdapter;
import com.lbins.meetlove.gallery.GalleryViewPager;
import com.lbins.meetlove.gallery.UrlPagerAdapter;
import com.lbins.meetlove.util.Constants;
import com.lbins.meetlove.util.PicUtil;
import com.lbins.meetlove.util.StringUtil;
import org.json.JSONObject;

import java.util.*;

/**
 * author: liuzwei
 * Date: 2014/8/27
 * Time: 17:53
 * 类的功能、说明写在此处.
 */
public class GalleryUrlActivity extends BaseActivity implements View.OnClickListener {
    private GalleryViewPager mViewPager;
    private String[] imageUrls;
    private TextView picSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_url);
        picSum = (TextView) findViewById(R.id.gallery_pic_sum);
        Bundle bundle = getIntent().getExtras();
        imageUrls = bundle.getStringArray(Constants.IMAGE_URLS);
        int pagerPosition = bundle.getInt(Constants.IMAGE_POSITION, 0);
        List<String> items = new ArrayList<String>();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Collections.addAll(items, imageUrls);
        UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(GalleryUrlActivity.this, items, dm.widthPixels, dm.heightPixels);
        pagerAdapter.setOnItemChangeListener(new BasePagerAdapter.OnItemChangeListener() {
            @Override
            public void onItemChange(int currentPosition) {
                picSum.setText(currentPosition + 1 + "/" + imageUrls.length);
            }
        });

        mViewPager = (GalleryViewPager) findViewById(R.id.viewer);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(pagerPosition);
        mViewPager.setOnItemClickListener(new GalleryViewPager.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                finish();
            }
        });
    }

    public void download(View view) {
        int i = mViewPager.getCurrentItem();
        getLxThread().execute(new PicUtil(imageUrls[i]));
        String fileName = PicUtil.getImagePath(imageUrls[i]);
        Toast.makeText(this, "已保存至" + fileName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.btn_right:
//            {
//                //删除按钮
//                showMsg(GalleryUrlActivity.this, "删除" + mViewPager.getCurrentItem());
//                deleteByIds(imageUrls[mViewPager.getCurrentItem()]);
//                if((mViewPager.getCurrentItem()+1)  == imageUrls.length){
//                    //说明是最后一张
//                    finish();
//                }else{
//                    mViewPager.setCurrentItem( mViewPager.getCurrentItem()+1);
//                }
//            }
//                break;
//            case R.id.back:
//            {
//                finish();
//            }
//                break;
        }
    }






}
