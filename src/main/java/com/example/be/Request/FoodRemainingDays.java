package com.example.be.Request;

import lombok.Data;

@Data
public class FoodRemainingDays {
    private String id;      //음식 uuid
    private String refrigerator_id;
    private long remainingDays;
}
