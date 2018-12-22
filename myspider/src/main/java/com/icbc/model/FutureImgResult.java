package com.icbc.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class FutureImgResult implements Serializable {

    private Integer id;
    private Boolean success;
    private String msg;
}
