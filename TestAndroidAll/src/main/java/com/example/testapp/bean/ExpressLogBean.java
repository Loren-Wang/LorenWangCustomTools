package com.example.testapp.bean;

/**
 * Created by lizhifeng on 2018/11/13.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 功能作用：订单快递物流信息实体
 * 初始注释时间： 2020/4/2 2:01 下午
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class ExpressLogBean {
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("expressManMobile")
    @Expose
    private String expressManMobile;
    @SerializedName("createTime")
    @Expose
    private Long createTime;
    @SerializedName("context")
    @Expose
    private String context;
    @SerializedName("size")
    @Expose
    private Integer size;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getExpressManMobile() {
        return expressManMobile;
    }

    public void setExpressManMobile(String expressManMobile) {
        this.expressManMobile = expressManMobile;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
