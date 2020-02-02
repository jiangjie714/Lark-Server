package com.github.hollykunge.security.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class MeetingVO {
    private String id;

    private String type;

    private String name;

    private String date;

    private String time;
}
