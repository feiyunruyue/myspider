package com.icbc.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class ImgInfo implements Serializable {

    private Integer id;
    private Integer siteId;
    private String url;
    private String title;
    private Integer yn;
}
