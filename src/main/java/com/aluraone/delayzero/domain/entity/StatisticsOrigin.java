package com.aluraone.delayzero.domain.entity;

import com.aluraone.delayzero.dto.out.StatisticsByOrigin;
import lombok.Getter;

@Getter
public class StatisticsOrigin {

    private Double delayedPercentage;
    private String nameOrigin;

    public StatisticsOrigin(Double delayedPercentage, String nameOrigin) {

        this.delayedPercentage = delayedPercentage;
        this.nameOrigin = nameOrigin;
    }

    public StatisticsByOrigin createDTO() {
        return new StatisticsByOrigin(nameOrigin, delayedPercentage.floatValue());
    }

}
