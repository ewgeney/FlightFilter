package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FlightFilter {

    public static List<Flight> filter (List<Flight> list, Predicate<Flight>... predicates) {

        return list.stream().filter(Arrays.stream(predicates)
                .reduce(flight -> true, Predicate::and)).collect(Collectors.toList());
    }

    public static final Predicate<Flight> departureUntilTheCurrentTime = new Predicate<Flight>() {
        @Override
        public boolean test(Flight flight) {
            return !flight.getSegments().get(0).getDepartureDate().isBefore(LocalDateTime.now());
        }
    };

    public static final Predicate<Flight> arrivalDateBeforeDepartureDate = new Predicate<Flight>() {
        @Override
        public boolean test(Flight flight) {
            return flight.getSegments().stream().anyMatch(new Predicate<Segment>() {
                @Override
                public boolean test(Segment segment) {
                    return !segment.getArrivalDate().isBefore(segment.getDepartureDate());
                }
            });
        }
    };

    public static final Predicate<Flight> moreThanTwoHoursOnEarth = new Predicate<Flight>() {
        @Override
        public boolean test(Flight flight) {

            //Получает продолжительность каждого сегмента в перелете и складывает вместе
            Duration segmentsCalculate = flight.getSegments().stream().map(new Function<Segment, Duration>() {
                                                                               @Override
                                                                               public Duration apply(Segment segment) {
                                                                                   return Duration.between(segment.getDepartureDate(), segment.getArrivalDate());
                                                                               }
                                                                           }).reduce(Duration::plus).get();

            //Получает продолжительность всего перелета включая пересадки
            Duration flightCalculate = Duration.between(flight.getSegments().get(0).getDepartureDate(),
                    flight.getSegments().get(flight.getSegments().size() - 1).getArrivalDate());

            return !(flightCalculate.minus(segmentsCalculate).compareTo(Duration.ofHours(2)) > 0);
        }
    };
}