package com.icbc.model;

import lombok.Getter;

@Getter
public enum ImgStatusEnum {

    INIT(0, "初始化"),
    DOWN_SUCCESS(1, "下载成功"),
    DOWN_FAIL(2, "下载失败");

    private int value;
    private String desc;

    ImgStatusEnum(int value, String desc){
        this.value = value;
        this.desc = desc;
    }



}
