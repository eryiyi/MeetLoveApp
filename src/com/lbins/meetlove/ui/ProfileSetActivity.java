package com.lbins.meetlove.ui;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lbins.meetlove.R;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.dao.Emp;
import com.lbins.meetlove.dao.Friends;
import com.lbins.meetlove.dao.HappyHandJw;
import com.lbins.meetlove.data.EmpData;
import com.lbins.meetlove.data.FriendsData;
import com.lbins.meetlove.data.HappyHandJwDatas;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class ProfileSetActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private Emp emp ;
    private String empid ;

    private String is_friends = "";//1是好友
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_set_activity);
        empid = getIntent().getExtras().getString("empid");
        initView();
        progressDialog = new CustomProgressDialog(ProfileSetActivity.this, "请稍后...",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getEmpById();
        //判断我和他之间的关系
        getFriends();
    }

    private void getEmpById() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appEmpByEmpId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    EmpData data = getGson().fromJson(s, EmpData.class);
                                    if(data != null){
                                        emp = data.getData();
                                    }
                                }else {
                                    Toast.makeText(ProfileSetActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("empid", empid);
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

    List<Friends> listsIS = new ArrayList<>();

    private void getFriends() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appFriends,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    FriendsData data = getGson().fromJson(s, FriendsData.class);
                                    if(data != null){
                                        listsIS.clear();
                                        listsIS.addAll(data.getData());
                                        if(listsIS != null && listsIS.size()>0){
                                            //是好友
                                            is_friends = "1";
                                        }else{
                                            is_friends = "0";
                                        }
                                    }
                                }else {
                                    Toast.makeText(ProfileSetActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("is_check", "1");
                params.put("empid1", getGson().fromJson(getSp().getString("empid", ""), String.class));
                params.put("empid2", empid);
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

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("详细设置");

        this.findViewById(R.id.liner1).setOnClickListener(this);
        this.findViewById(R.id.liner3).setOnClickListener(this);
        this.findViewById(R.id.liner4).setOnClickListener(this);
        this.findViewById(R.id.liner5).setOnClickListener(this);
        this.findViewById(R.id.liner9).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.liner1:
            {
                //查看交往请求
//                getApply1();
                Intent intent = new Intent(ProfileSetActivity.this, JwdxApplyActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.liner3:
            {
                //黑名单
                    //删除好友
                    progressDialog = new CustomProgressDialog(ProfileSetActivity.this, "正在处理",R.anim.custom_dialog_frame);
                    progressDialog.setCancelable(true);
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();
                    deleteFriendsBlack();
            }
                break;
            case R.id.liner4:
            {
                //删除好友
                if("1".equals(is_friends)){
                    //删除好友
                    showVersionDialog();
                }else{
                    showMsg(ProfileSetActivity.this ,"对方不是您的好友，无法删除！");
                }
            }
            break;
            case R.id.liner5:
            {
                //投诉
                Intent intent = new Intent(ProfileSetActivity.this, AddReportActivity.class);
                intent.putExtra("name" , emp.getNickname());
                intent.putExtra("empid" , emp.getEmpid());
                intent.putExtra("empmobileStr" , emp.getMobile());
                startActivity(intent);
            }
            break;
            case R.id.liner9:
            {
                Intent intent = new Intent(ProfileSetActivity.this, MineBlackListActivity.class);
                startActivity(intent);
            }
                break;
        }
    }


    private void getApply1() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appJiaowangs,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    HappyHandJwDatas data = getGson().fromJson(s, HappyHandJwDatas.class);
                                    if(data != null){
                                        List<HappyHandJw> datas = data.getData();
                                        if(datas != null){
                                            if(datas.size() > 0){
                                                //说明有交往申请 我是被动接受
                                                showJwqq1(datas.get(0));
                                            }else {
                                                getApply2();
                                            }
                                        }else {
                                            getApply2();
                                        }
                                    }else {
                                        getApply2();
                                    }
                                }else {
                                    Toast.makeText(ProfileSetActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("empid2", getGson().fromJson(getSp().getString("empid", ""), String.class));
                params.put("empid1", empid);
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

    private void showJwqq1(final HappyHandJw happyHandJw) {
        final Dialog picAddDialog = new Dialog(ProfileSetActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.msg_jwqq1_dialog, null);
        TextView msg = (TextView) picAddInflate.findViewById(R.id.msg);
        msg.setText(emp.getNickname()+"想与您交往");
        Button btn_sure = (Button) picAddInflate.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new CustomProgressDialog(ProfileSetActivity.this, "请稍后...",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                happyHandJw.setIs_check("2");
                saveAcctept(happyHandJw);
                picAddDialog.dismiss();
            }
        });

        //取消
        Button btn_cancel = (Button) picAddInflate.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new CustomProgressDialog(ProfileSetActivity.this, "请稍后...",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                happyHandJw.setIs_check("1");
                saveAcctept(happyHandJw);
                picAddDialog.dismiss();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }

    private void saveAcctept(final HappyHandJw friends) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appAcceptJiaowang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    showMsg(ProfileSetActivity.this, "操作成功!");
                                    //调用广播，刷新主页
                                    if("1".equals(friends.getIs_check())){
                                        //同意了
                                        save("state", "2");
                                        Intent intent1 = new Intent("update_jwdx_success");
                                        sendBroadcast(intent1);
                                    }
                                    if("2".equals(friends.getIs_check())){
                                        //拒绝
                                        Intent intent1 = new Intent("update_jwdx_refuse");
                                        sendBroadcast(intent1);
                                    }
                                }else {
                                    Toast.makeText(ProfileSetActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("jwid", friends.getJwid());
                params.put("is_check", friends.getIs_check());
                params.put("empid1", friends.getEmpid1());
                params.put("empid2", friends.getEmpid2());
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


    private void getApply2() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appJiaowangs,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    HappyHandJwDatas data = getGson().fromJson(s, HappyHandJwDatas.class);
                                    if(data != null){
                                        List<HappyHandJw> datas = data.getData();
                                        if(datas != null){
                                            if(datas.size() > 0){
                                                //说明有交往申请 我是请求方
                                                HappyHandJw happyHandJw = datas.get(0);
                                                if(happyHandJw != null){
                                                    if("0".equals(happyHandJw.getIs_check())){
//                                                        showMsg(ProfileSetActivity.this, "您的交往请求，对方尚未接受，请等待！");
                                                        //对方尚未确认，请耐心等待
                                                        showMsgDialog(empid);
                                                    }else if("1".equals(happyHandJw.getIs_check())){
                                                        showMsg(ProfileSetActivity.this, "对方已经接受您的交往请求！");
                                                    }else if("2".equals(happyHandJw.getIs_check())){
                                                        showMsg(ProfileSetActivity.this, "对方已经拒绝您的交往请求！");
                                                    }
                                                }
                                            }else {
                                                showMsg(ProfileSetActivity.this, "目前没有交往请求");
                                            }
                                        }else {
                                            showMsg(ProfileSetActivity.this, "目前没有交往请求");
                                        }
                                    }else {
                                        showMsg(ProfileSetActivity.this, "目前没有交往请求");
                                    }
                                }else {
                                    Toast.makeText(ProfileSetActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("empid1", getGson().fromJson(getSp().getString("empid", ""), String.class));
                params.put("empid2", empid);
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


    private void showMsgDialog(final String empid2) {
        final Dialog picAddDialog = new Dialog(ProfileSetActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.jwdx_msg_dialog, null);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消邀请
                deleteYq(empid2);
                picAddDialog.dismiss();
            }
        });

        //取消
        TextView btn_cancel = (TextView) picAddInflate.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picAddDialog.dismiss();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }

    private void deleteYq(final String empid2) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appDeleteJiaowang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    showMsg(ProfileSetActivity.this, "取消邀请成功！");
                                    finish();
                                } else {
                                    Toast.makeText(ProfileSetActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(ProfileSetActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ProfileSetActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empid2", empid2);
                params.put("empid1", getGson().fromJson(getSp().getString("empid", ""), String.class));
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


    private void showVersionDialog() {
        final Dialog picAddDialog = new Dialog(ProfileSetActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.msg_delete_friends_dialog, null);
        Button btn_sure = (Button) picAddInflate.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new CustomProgressDialog(ProfileSetActivity.this, "正在处理",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                deleteFriends();
                picAddDialog.dismiss();
            }
        });

        //取消
        Button btn_cancel = (Button) picAddInflate.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picAddDialog.dismiss();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }

    private void deleteFriends() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appDeleteFriends,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    showMsg(ProfileSetActivity.this, "删除好友关系成功！");
                                    Intent intent1 = new Intent("delete_friends_success");
                                    sendBroadcast(intent1);
                                }else {
                                    Toast.makeText(ProfileSetActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("is_check", "1");
                params.put("empid1", getGson().fromJson(getSp().getString("empid", ""), String.class));
                params.put("empid2", empid);
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



    private void deleteFriendsBlack() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appDeleteFriends,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    showMsg(ProfileSetActivity.this, "对方已经加入黑名单！");
                                    EMClient.getInstance().contactManager().addUserToBlackList(empid,true);
                                    Intent intent1 = new Intent("delete_friends_success");
                                    sendBroadcast(intent1);
                                }else{
                                    showMsg(ProfileSetActivity.this, jo.getString("message"));
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
                params.put("is_check", "1");
                params.put("empid1", getGson().fromJson(getSp().getString("empid", ""), String.class));
                params.put("empid2", empid);
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
