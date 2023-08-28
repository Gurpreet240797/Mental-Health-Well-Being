package com.soen6841.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AvailableTime {
    String id;

    String time;

    public AvailableTime(String id, String time){
        this.id = id;
        this.time = time;
    }
}
