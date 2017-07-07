package com.lbins.meetlove.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.ItemGroupMemberAdapter;
import com.lbins.meetlove.adapter.ItemJwdxAdapter;
import com.lbins.meetlove.adapter.OnClickContentItemListener;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.dao.Friends;
import com.lbins.meetlove.data.EmpGroupsData;
import com.lbins.meetlove.data.FriendsData;
import com.lbins.meetlove.module.EmpGroups;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupsDelEmpActivity extends BaseActivity implements View.OnClickListener,OnClickContentItemListener ,AdapterView.OnItemClickListener{
    private TextView title;
    private TextView btn_right;

    private ListView lstv;
    private ItemGroupMemberAdapter adapter;
    private List<EmpGroups> lists = new ArrayList<EmpGroups>();

    private List<String> listEmpSelect = new ArrayList<String>();//存放选中的会员

    public GroupsDelEmpActivity() {
    }

    private String groupid;

    private EditText keywords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_del_emp_activity);
        groupid = getIntent().getExtras().getString("groupid");
        initView();
        adapter.isCheckMap.clear();
        progressDialog = new CustomProgressDialog(GroupsDelEmpActivity.this, "请稍后...",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();
        changeRightBtn();
    }

    void changeRightBtn(){
        if(listEmpSelect.size()>0){
            btn_right.setBackgroundColor(getResources().getColor(R.color.main_color));
        }else{
            btn_right.setBackgroundColor(getResources().getColor(R.color.gray_color));
        }
    }
    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        btn_right = (TextView) this.findViewById(R.id.btn_right);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("群成员列表");
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(this);
        btn_right.setText("删除");

        adapter = new ItemGroupMemberAdapter(GroupsDelEmpActivity.this, lists, getGson().fromJson(getSp().getString("empid", ""), String.class));
        adapter.setOnClickContentItemListener(this);
        lstv = (ListView) this.findViewById(R.id.lstv);
        lstv.setAdapter(adapter);
        lstv.setVisibility(View.VISIBLE);
        lstv.setOnItemClickListener(this);
        keywords = (EditText) this.findViewById(R.id.keywords);
//        keywords.addTextChangedListener(watcher);
        keywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“GO”键*/
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }
                    getData();
                    return true;
                }
                return false;
            }
        });

        mapDone();
    }

//    private TextWatcher watcher = new TextWatcher() {
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count,
//                                      int after) {
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//                getData();
//        }
//    };

    void mapDone(){
        listEmpSelect.clear();//先清空

        for (Map.Entry<Integer, Boolean> entry : adapter.isCheckMap.entrySet()) {
            Integer key = entry.getKey();
            Boolean value = entry.getValue();
            System.out.println("key=" + key + " value=" + value);
            if(value){
                //如果选中了
                if(lists.size()>key){
                    listEmpSelect.add(lists.get(key).getEmpid());
                }
            }
        }
        changeRightBtn();
    }

    /**
     * 当ListView 子项点击的时候
     */
    @Override
    public void onItemClick(AdapterView<?> listView, View itemLayout,
                            int position, long id) {
        if (itemLayout.getTag() instanceof ItemJwdxAdapter.ViewHolder) {
            ItemJwdxAdapter.ViewHolder holder = (ItemJwdxAdapter.ViewHolder) itemLayout.getTag();
            // 会自动出发CheckBox的checked事件
            holder.cbCheck.toggle();
            mapDone();
        }
    }

    //查询
    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appEmpByGroupId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    EmpGroupsData data = getGson().fromJson(s, EmpGroupsData.class);
                                    if(data != null){
                                        lists.clear();
                                        lists.addAll(data.getData());
                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    Toast.makeText(GroupsDelEmpActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(GroupsDelEmpActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(GroupsDelEmpActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("groupid", groupid);
                if(!StringUtil.isNullOrEmpty(keywords.getText().toString())){
                    params.put("keywords", keywords.getText().toString());
                }else{
                    params.put("keywords", "");
                }

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_right:
            {
                if(listEmpSelect.size() == 0){
                    showMsg(GroupsDelEmpActivity.this, "请选择要删除的对象！");
                    return;
                }
                String empids = "";
                for(String str:listEmpSelect){
                    empids +=str+",";
                }

                progressDialog = new CustomProgressDialog(GroupsDelEmpActivity.this, "请稍后...",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                saveApply(empids);
            }
            break;
        }
    }

    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        mapDone();
    }

    private void saveApply(final String empids) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appDeleteMembers,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    showMsg(GroupsDelEmpActivity.this, "删除成功！");
                                    Intent intent1 = new Intent("delete_group_member");
                                    sendBroadcast(intent1);
                                    finish();
                                } else {
                                    Toast.makeText(GroupsDelEmpActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(GroupsDelEmpActivity.this, "操作失败，请稍后重试！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(GroupsDelEmpActivity.this, "操作失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("groupid", groupid);
                params.put("empids", empids);
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



}
