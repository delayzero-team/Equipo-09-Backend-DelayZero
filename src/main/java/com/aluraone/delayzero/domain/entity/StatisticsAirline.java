package com.aluraone.delayzero.domain.entity;

import com.aluraone.delayzero.dto.out.StatisticsByAirline;
import lombok.Getter;

@Getter
public class StatisticsAirline {

    private Double delayedPercentage;
    private String nameAirline;

    public StatisticsAirline(Double delayedPercentage, String nameAirline) {
        this.delayedPercentage = delayedPercentage;
        this.nameAirline = nameAirline;
    }

    public StatisticsByAirline createDTO() {
        return new StatisticsByAirline(nameAirline, delayedPercentage.floatValue());
    }
}
