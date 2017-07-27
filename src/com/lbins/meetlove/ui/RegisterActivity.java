package com.lbins.meetlove.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.R;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.receiver.SMSBroadcastReceiver;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private EditText mobile;
    private EditText pwr;
    private EditText pwrsure;
    private Button btn_login;
    private Resources res;
    private TextView sex_man;
    private TextView sex_woman;

    private LinearLayout sex_liner;

    private String sex = "";//0女 1男

    private EditText card;
    private Button btn_card;

    //mob短信
    // 填写从短信SDK应用后台注册得到的APPKEY
    private static String APPKEY = InternetURL.APP_MOB_KEY;//"69d6705af33d";0d786a4efe92bfab3d5717b9bc30a10d
    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static String APPSECRET = InternetURL.APP_MOB_SCRECT;
    public String phString;//手机号码
    //短信读取
    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        res = getResources();
        //mob短信无GUI
        SMSSDK.initSDK(this, APPKEY, APPSECRET, false);
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
        initView();
        //生成广播处理
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();
        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(ACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);
        //注册广播
        this.registerReceiver(mSMSBroadcastReceiver, intentFilter);
        mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void onReceived(String message) {
                if (!StringUtil.isNullOrEmpty(message)) {
                    String codestr = StringUtil.valuteNumber(message);
                    if (!StringUtil.isNullOrEmpty(codestr)) {
                        card.setText(codestr);
                    }
                }
            }
        });
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("注册");
        this.findViewById(R.id.btn_fwtk).setOnClickListener(this);
        this.findViewById(R.id.btn_ysbh).setOnClickListener(this);
        mobile = (EditText) this.findViewById(R.id.mobile);
        pwr = (EditText) this.findViewById(R.id.pwr);
        pwrsure = (EditText) this.findViewById(R.id.pwrsure);
        btn_login = (Button) this.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        sex_man = (TextView) this.findViewById(R.id.sex_man);
        sex_woman = (TextView) this.findViewById(R.id.sex_woman);
        sex_man.setOnClickListener(this);
        sex_woman.setOnClickListener(this);
        sex_liner = (LinearLayout) this.findViewById(R.id.sex_liner);


        //设置监听  随时更改注册按钮的状态
        mobile.addTextChangedListener(watcher);
        pwr.addTextChangedListener(watcher);
        pwrsure.addTextChangedListener(watcher);

        btn_login.setBackgroundResource(R.drawable.btn_big_unactive);
        btn_login.setTextColor(res.getColor(R.color.textColortwo));
        card = (EditText) this.findViewById(R.id.card);
        btn_card = (Button) this.findViewById(R.id.btn_card);
        btn_card.setOnClickListener(this);
    }

    class MyTimer extends CountDownTimer {

        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btn_card.setText(res.getString(R.string.daojishi_three));
            btn_card.setClickable(true);//可点击
            btn_card.setBackgroundResource(R.drawable.btn_short_active);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_card.setText(res.getString(R.string.daojishi_one) + millisUntilFinished / 1000 + res.getString(R.string.daojishi_two));
        }
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                System.out.println("--------result" + event);
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    reg();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //已经验证
//                    Toast.makeText(getApplicationContext(), R.string.code_msg_one, Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(RegisterActivity.this, R.string.code_msg_two, Toast.LENGTH_SHORT).show();
                int status = 0;
                try {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;

                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    status = object.optInt("status");
                    if (!TextUtils.isEmpty(des)) {
                        Toast.makeText(RegisterActivity.this, des, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    SMSLog.getInstance().w(e);
                }
            }
            if(progressDialog != null){
                progressDialog.dismiss();
            }
        }

        ;
    };

    public void onDestroy() {
        super.onPause();
        SMSSDK.unregisterAllEventHandler();
        //注销短信监听广播
        this.unregisterReceiver(mSMSBroadcastReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_card:
            {
                //验证码
                if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
                    showMsg(RegisterActivity.this, "请输入手机号码！");
                    return;
                }
                SMSSDK.getVerificationCode("86", mobile.getText().toString());//发送请求验证码，手机10s之内会获得短信验证码
                phString = mobile.getText().toString();
                btn_card.setClickable(false);//不可点击
                btn_card.setBackgroundResource(R.drawable.btn_short_unactive);
                MyTimer myTimer = new MyTimer(60000, 1000);
                myTimer.start();

            }
            break;
            case R.id.back:
                finish();
                break;
            case R.id.btn_fwtk:
            {
                //服务条款
                Intent intent = new Intent(RegisterActivity.this, FutkActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_ysbh:
            {
                //隐私保护
                Intent intent = new Intent(RegisterActivity.this, YsbhActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btn_login:
            {
                //点击注册
                if(StringUtil.isNullOrEmpty(mobile.getText().toString().trim())){
                    showMsg(RegisterActivity.this, res.getString(R.string.error_login_one));
                    return;
                }
                if(mobile.getText().toString().length() != 11){
                    showMsg(RegisterActivity.this, "手机号码格式不正确！");
                    return;
                }
                if(StringUtil.isNullOrEmpty(card.getText().toString())){
                    showMsg(RegisterActivity.this, "请输入验证码！");
                    return;
                }
                if(StringUtil.isNullOrEmpty(pwr.getText().toString())){
                    showMsg(RegisterActivity.this, res.getString(R.string.error_login_two));
                    return;
                }

                if(pwr.getText().toString().length() > 18 || pwr.getText().toString().length()<6){
                    showMsg(RegisterActivity.this, res.getString(R.string.error_pwr_six_eighteen));
                    return;
                }
                if(StringUtil.isNullOrEmpty(pwrsure.getText().toString())){
                    showMsg(RegisterActivity.this, res.getString(R.string.error_pwr_again));
                    return;
                }
                if(!pwr.getText().toString().equals(pwrsure.getText().toString())){
                    showMsg(RegisterActivity.this, res.getString(R.string.error_pwr_two_no));
                    return;
                }
                if(StringUtil.isNullOrEmpty(sex)){
                    showMsg(RegisterActivity.this, "请选择性别");
                    return;
                }

                progressDialog = new CustomProgressDialog(RegisterActivity.this, "正在注册",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
//                reg();
                SMSSDK.submitVerificationCode("86", phString, card.getText().toString());
            }
                break;
            case R.id.sex_man:
            {
                //男
                sex_liner.setBackgroundResource(R.drawable.btn_sex_left);
                sex_man.setTextColor(res.getColor(R.color.white));
                sex_woman.setTextColor(res.getColor(R.color.main_color));
                sex = "1";
            }
                break;
            case R.id.sex_woman:
            {
                //女
                sex_liner.setBackgroundResource(R.drawable.btn_sex_right);
                sex_man.setTextColor(res.getColor(R.color.main_color));
                sex_woman.setTextColor(res.getColor(R.color.white));
                sex = "0";
            }
                break;
        }
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!StringUtil.isNullOrEmpty(mobile.getText().toString()) && !StringUtil.isNullOrEmpty(pwr.getText().toString()) && !StringUtil.isNullOrEmpty(pwrsure.getText().toString())){
                //都不是空的
                if(mobile.getText().toString().length() == 11 && pwr.getText().toString().equals(pwrsure.getText().toString()) && pwr.getText().toString().length() > 5 && pwr.getText().toString().length()<19){
                    //手机号是11位 两次输入密码一致 密码大于6位小于18位
                    btn_login.setBackgroundResource(R.drawable.btn_big_active);
                    btn_login.setTextColor(res.getColor(R.color.white));
                }else {
                    btn_login.setBackgroundResource(R.drawable.btn_big_unactive);
                    btn_login.setTextColor(res.getColor(R.color.textColortwo));
                }
            }
        }
    };

    private void reg(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appReg,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    String empid = jo.getString("data");
                                    save("empid", empid);
                                    save("password", pwr.getText().toString());
                                    save("mobile", mobile.getText().toString());
                                    save("sex", sex);
                                    save("is_use", "2");

                                    save("nickname", "");
                                    save("cover", "");
                                    save("sign", "");
                                    save("age", "");
                                    save("heightl", "");
                                    save("education", "");
                                    save("provinceid", "");
                                    save("cityid", "");
                                    save("areaid", "");
                                    save("marriage", "");
                                    save("company", "");
                                    save("likeids", "");
                                    save("state", "");
                                    save("cardpic", "");
                                    save("rzstate1", "");
                                    save("rzstate2", "");
                                    save("rzstate3", "");
                                    save("pname","");
                                    save("cityName", "");

                                    save("chooseid", "");
                                    save("agestart", "");
                                    save("ageend", "");
                                    save("heightlstart", "");
                                    save("heightlend", "");
                                    save("educationm", "");
                                    save("marriagem", "");
                                    save("is_push", "");

                                    Intent intent =  new Intent(RegisterActivity.this, RegUpdateActivity.class);
                                    intent.putExtra("empid", empid);
                                    startActivity(intent);
                                }  else {
                                    showMsg(RegisterActivity.this,  jo.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(RegisterActivity.this, "注册失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile.getText().toString().trim());
                params.put("password", pwr.getText().toString());
                params.put("sex", sex);
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
