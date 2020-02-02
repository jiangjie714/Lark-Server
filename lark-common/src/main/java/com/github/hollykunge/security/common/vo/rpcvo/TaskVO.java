package com.github.hollykunge.security.common.vo.rpcvo;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
@NoArgsConstructor
public class TaskVO implements Serializable {
    private String id;

    private String type;

    private String name;

    private String date;

    private String color;

    private String time;
}
