package com.lbins.meetlove.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.hyphenate.util.EMLog;
import com.lbins.meetlove.MeetLoveApplication;
import com.lbins.meetlove.R;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.data.EmpGroupManagerData;
import com.lbins.meetlove.data.HappyHandGroupDataSingle;
import com.lbins.meetlove.module.EmpGroupManager;
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
public class GroupSetSetActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "GroupSetSetActivity";

    private TextView title;
    private String groupId;
    private EMGroup group;

    private EaseSwitchButton switchButton;
    private LinearLayout linerr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups_set_activity);
        groupId = getIntent().getExtras().getString("groupId");
        group = EMClient.getInstance().groupManager().getGroup(groupId);
        if(group == null){
            finish();
            return;
        }

        initView();

        progressDialog = new CustomProgressDialog(GroupSetSetActivity.this, "请稍后",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getGroupsManager();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("详细设置");

        this.findViewById(R.id.liner5).setOnClickListener(this);

        RelativeLayout rl_switch_block_groupmsg = (RelativeLayout) findViewById(R.id.rl_switch_block_groupmsg);
        switchButton = (EaseSwitchButton) findViewById(R.id.switch_btn);
        rl_switch_block_groupmsg.setOnClickListener(this);

        EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
        if (group != null){
            if (group.isMsgBlocked()) {
                switchButton.openSwitch();
            }
        }
        linerr = (LinearLayout) this.findViewById(R.id.linerr);
        linerr.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.liner5:
            {
                //投诉
                Intent intent = new Intent(GroupSetSetActivity.this, AddReportActivity.class);
                intent.putExtra("name" , (group.getGroupName()==null?"":group.getGroupName()));
                intent.putExtra("empid" ,"");
                intent.putExtra("empmobileStr" ,"");
                startActivity(intent);
            }
            break;
            case R.id.rl_switch_block_groupmsg:
                // 屏蔽或取消屏蔽群组
                toggleBlockGroup();
                break;
            case R.id.linerr:
            {
                Intent intent = new Intent(GroupSetSetActivity.this, GroupsDelEmpActivity.class);
                intent.putExtra("groupid", groupId );
                startActivity(intent);
            }
                break;
        }
    }

    private ProgressDialog progressDialog;

    private void toggleBlockGroup() {
        if(switchButton.isSwitchOpen()){
            EMLog.d(TAG, "change to unblock group msg");
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(GroupSetSetActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
            }
            progressDialog.setMessage(getString(R.string.Is_unblock));
            progressDialog.show();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().unblockGroupMessage(groupId);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Intent intent1 = new Intent("update_group_set_msg");
                                sendBroadcast(intent1);
                                switchButton.closeSwitch();
                                progressDialog.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), R.string.remove_group_of, Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }
            }).start();

        } else {
            String st8 = getResources().getString(R.string.group_is_blocked);
            final String st9 = getResources().getString(R.string.group_of_shielding);
            EMLog.d(TAG, "change to block group msg");
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(GroupSetSetActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
            }
            progressDialog.setMessage(st8);
            progressDialog.show();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().blockGroupMessage(groupId);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Intent intent1 = new Intent("update_group_set_msg");
                                sendBroadcast(intent1);
                                switchButton.openSwitch();
                                progressDialog.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), st9, Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }
            }).start();
        }
    }

    private List<EmpGroupManager> list = new ArrayList<>();
    private void getGroupsManager() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appGroupsManager,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    EmpGroupManagerData data = getGson().fromJson(s, EmpGroupManagerData.class);
                                    if(data != null){
                                        list.clear();
                                        list.addAll(data.getData());
                                        if(list.size()>0){
                                            EmpGroupManager empGroupManager = list.get(0);
                                            if(empGroupManager != null && getGson().fromJson(getSp().getString("empid", ""), String.class).equals(empGroupManager.getEmpid())){
                                                linerr.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }
                                }else {
                                    Toast.makeText(GroupSetSetActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

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
                params.put("groupid", groupId);
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
