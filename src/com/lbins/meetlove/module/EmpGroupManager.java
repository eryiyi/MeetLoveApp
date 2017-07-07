package com.lbins.meetlove.module;

/**
 * Created by zhl on 2017/5/29.
 */
public class EmpGroupManager {
    private String emp_group_manager_id;
    private String empid;
    private String groupid;
    private String dateline;
    private String nickname;
    private String cover;
    private String title;
    private String mobile;//手机号

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getEmp_group_manager_id() {
        return emp_group_manager_id;
    }

    public void setEmp_group_manager_id(String emp_group_manager_id) {
        this.emp_group_manager_id = emp_group_manager_id;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }
}
