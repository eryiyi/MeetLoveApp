package com.lbins.meetlove.dao;

import com.lbins.meetlove.dao.DaoSession;
import de.greenrobot.dao.DaoException;

import java.io.Serializable;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table EMP.
 */
public class Emp{

    /** Not-null value. */
    private String empid;
    private String mobile;
    private String password;
    private String nickname;
    private String cover;
    private String sign;
    private String age;
    private String sex;
    private String heightl;
    private String education;
    private String provinceid;
    private String cityid;
    private String areaid;
    private String marriage;
    private String company;
    private String likeids;
    private String state;
    private String cardpic;
    private String rzstate1;
    private String rzstate2;//0未认证 1已认证 2临时认证 3认证过期
    private String rzstate3;
    private String is_use;
    private String dateline;
    private String userId;
    private String channelId;
    private String deviceType;
    private String chooseid;
    private String agestart;
    private String ageend;
    private String heightlstart;
    private String heightlend;
    private String educationm;
    private String marriagem;
    private String pname;
    private String cityName;
    private String is_push;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient EmpDao myDao;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Emp() {
    }

    public Emp(String empid) {
        this.empid = empid;
    }

    public Emp(String empid, String mobile, String password, String nickname, String cover, String sign, String age, String sex, String heightl, String education, String provinceid, String cityid, String areaid, String marriage, String company, String likeids, String state, String cardpic, String rzstate1, String rzstate2, String rzstate3, String is_use, String dateline, String userId, String channelId, String deviceType, String chooseid, String agestart, String ageend, String heightlstart, String heightlend, String educationm, String marriagem, String pname, String cityName, String is_push) {
        this.empid = empid;
        this.mobile = mobile;
        this.password = password;
        this.nickname = nickname;
        this.cover = cover;
        this.sign = sign;
        this.age = age;
        this.sex = sex;
        this.heightl = heightl;
        this.education = education;
        this.provinceid = provinceid;
        this.cityid = cityid;
        this.areaid = areaid;
        this.marriage = marriage;
        this.company = company;
        this.likeids = likeids;
        this.state = state;
        this.cardpic = cardpic;
        this.rzstate1 = rzstate1;
        this.rzstate2 = rzstate2;
        this.rzstate3 = rzstate3;
        this.is_use = is_use;
        this.dateline = dateline;
        this.userId = userId;
        this.channelId = channelId;
        this.deviceType = deviceType;
        this.chooseid = chooseid;
        this.agestart = agestart;
        this.ageend = ageend;
        this.heightlstart = heightlstart;
        this.heightlend = heightlend;
        this.educationm = educationm;
        this.marriagem = marriagem;
        this.pname = pname;
        this.cityName = cityName;
        this.is_push = is_push;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getEmpDao() : null;
    }

    /** Not-null value. */
    public String getEmpid() {
        return empid;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeightl() {
        return heightl;
    }

    public void setHeightl(String heightl) {
        this.heightl = heightl;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLikeids() {
        return likeids;
    }

    public void setLikeids(String likeids) {
        this.likeids = likeids;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCardpic() {
        return cardpic;
    }

    public void setCardpic(String cardpic) {
        this.cardpic = cardpic;
    }

    public String getRzstate1() {
        return rzstate1;
    }

    public void setRzstate1(String rzstate1) {
        this.rzstate1 = rzstate1;
    }

    public String getRzstate2() {
        return rzstate2;
    }

    public void setRzstate2(String rzstate2) {
        this.rzstate2 = rzstate2;
    }

    public String getRzstate3() {
        return rzstate3;
    }

    public void setRzstate3(String rzstate3) {
        this.rzstate3 = rzstate3;
    }

    public String getIs_use() {
        return is_use;
    }

    public void setIs_use(String is_use) {
        this.is_use = is_use;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getChooseid() {
        return chooseid;
    }

    public void setChooseid(String chooseid) {
        this.chooseid = chooseid;
    }

    public String getAgestart() {
        return agestart;
    }

    public void setAgestart(String agestart) {
        this.agestart = agestart;
    }

    public String getAgeend() {
        return ageend;
    }

    public void setAgeend(String ageend) {
        this.ageend = ageend;
    }

    public String getHeightlstart() {
        return heightlstart;
    }

    public void setHeightlstart(String heightlstart) {
        this.heightlstart = heightlstart;
    }

    public String getHeightlend() {
        return heightlend;
    }

    public void setHeightlend(String heightlend) {
        this.heightlend = heightlend;
    }

    public String getEducationm() {
        return educationm;
    }

    public void setEducationm(String educationm) {
        this.educationm = educationm;
    }

    public String getMarriagem() {
        return marriagem;
    }

    public void setMarriagem(String marriagem) {
        this.marriagem = marriagem;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getIs_push() {
        return is_push;
    }

    public void setIs_push(String is_push) {
        this.is_push = is_push;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
