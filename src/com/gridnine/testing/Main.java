package com.gridnine.testing;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Flight> list = FlightBuilder.createFlights();

        //Исключить из тестового набора перелёты где вылет до текущего момента времени.
        System.out.println(FlightFilter.filter(list,
                FlightFilter.departureUntilTheCurrentTime));

        //Исключить из тестового набора перелёты где имеются сегменты с датой прилёта раньше даты вылета
        System.out.println(FlightFilter.filter(list,
                FlightFilter.arrivalDateBeforeDepartureDate));

        //Исключить из тестового набора перелёты где общее время, проведённое на земле превышает два часа.
        System.out.println(FlightFilter.filter(list,
                FlightFilter.moreThanTwoHoursOnEarth));
    }
}
