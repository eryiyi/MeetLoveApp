package com.lbins.meetlove.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.TuijianGroupdapter;
import com.lbins.meetlove.adapter.TuijianPeopledapter;
import com.lbins.meetlove.base.BaseFragment;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.dao.Emp;
import com.lbins.meetlove.dao.HappyHandGroup;
import com.lbins.meetlove.data.EmpsData;
import com.lbins.meetlove.data.HappyHandGroupData;
import com.lbins.meetlove.ui.*;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import com.lbins.meetlove.widget.PictureGridview;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/7/1.
 * 推荐
 */
public class OneFragment extends BaseFragment implements View.OnClickListener  {

    private View view;
    private Resources res;

    private PictureGridview gridView1;
    private PictureGridview gridView2;
    private TuijianPeopledapter adapter1;
    private TuijianGroupdapter adapter2;
    private List<Emp> list1 = new ArrayList<Emp>();
    private List<HappyHandGroup> list2 = new ArrayList<HappyHandGroup>();

    private RelativeLayout liner_1;
    private TextView no_data;

    private ImageView btn_right;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.one_fragment, null);
        res = getActivity().getResources();
        registerBoradcastReceiver();
        initView();
        progressDialog = new CustomProgressDialog(getActivity(), "请稍后...",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getTuijianren1();
        getTuijianGroups();
        return view;
    }

    void initView(){
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText("推荐");
        view.findViewById(R.id.back).setVisibility(View.GONE);

        liner_1 = (RelativeLayout) view.findViewById(R.id.liner_1);
        no_data = (TextView) view.findViewById(R.id.no_data);

        gridView1 = (PictureGridview) view.findViewById(R.id.gridView1);
        gridView2 = (PictureGridview) view.findViewById(R.id.gridView2);

        adapter1 = new TuijianPeopledapter(list1, getActivity());
        adapter2 = new TuijianGroupdapter(list2, getActivity());
        gridView1.setAdapter(adapter1);
        gridView2.setAdapter(adapter2);
        gridView1.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView2.setSelector(new ColorDrawable(Color.TRANSPARENT));

        view.findViewById(R.id.btn_one).setOnClickListener(this);
        view.findViewById(R.id.btn_two).setOnClickListener(this);

        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(list1.size()>position){
                    Emp emp = list1.get(position);
                    if(emp != null){
                        Intent intent =  new Intent(getActivity(), ProfileEmpActivity.class);
                        intent.putExtra("empid", emp.getEmpid());
                        startActivity(intent);
                    }
                }
            }
        });
        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(list2.size()>position){
                    HappyHandGroup group = list2.get(position);
                    if(group != null){
                        Intent intent =  new Intent(getActivity(), GroupDetailActivity.class);
                        intent.putExtra("groupid", group.getGroupid());
                        startActivity(intent);
                    }
                }
            }
        });

        btn_right = (ImageView) view.findViewById(R.id.btn_right);
        btn_right.setImageDrawable(res.getDrawable(R.drawable.icon_navbar_search));
        btn_right.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_one:
            {
                //更多推荐人
                Intent intent = new Intent(getActivity(), TuijianPeopleActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_two:
            {
                //更多推荐群
                Intent intent = new Intent(getActivity(), TuijianGroupActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_right:
            {
                //搜索
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
            break;
        }
    }


    private void getTuijianren1() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appTuijianPeoples,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    EmpsData data = getGson().fromJson(s, EmpsData.class);
                                    if(data != null){
                                        list1.clear();
                                        list1.addAll(data.getData());
                                    }
                                    adapter1.notifyDataSetChanged();
                                    if(list1.size() > 0){
                                        liner_1.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                    }else {
                                        liner_1.setVisibility(View.GONE);
                                        no_data.setVisibility(View.VISIBLE);
                                    }
                                }else {
                                    Toast.makeText(getActivity(), jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                        }
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empid", getGson().fromJson(getSp().getString("empid", ""), String.class));
                params.put("sex", getGson().fromJson(getSp().getString("sex", ""), String.class));
                params.put("size", "1");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

    private void getTuijianGroups() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appTuijianGroups,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    HappyHandGroupData data = getGson().fromJson(s, HappyHandGroupData.class);
                                    if(data != null){
                                        List<HappyHandGroup> listsGroups = new ArrayList<>();
                                        listsGroups.addAll(data.getData());
                                        for(int i=0;i<(listsGroups.size()<2?listsGroups.size():2);i++){
                                            list2.add(listsGroups.get(i));
                                        }
                                    }
                                    adapter2.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(getActivity(), jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                        }
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empid", getGson().fromJson(getSp().getString("empid", ""), String.class));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("update_mine_profile_success")) {
                getTuijianren1();
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("update_mine_profile_success");
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }




}
