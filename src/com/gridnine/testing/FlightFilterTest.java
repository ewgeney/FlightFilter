package com.gridnine.testing;

import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class FlightFilterTest {

    @Test
    public void filter_all_filters() {

        List<Flight> list = FlightBuilder.createFlights();
        List<Flight> expected = FlightFilter.filter(list,
                FlightFilter.departureUntilTheCurrentTime,
                FlightFilter.arrivalDateBeforeDepartureDate,
                FlightFilter.moreThanTwoHoursOnEarth);
        List<Flight> actual = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            actual.add(list.get(i));
        }

        Assert.assertEquals(expected, actual);
    }
    @Test
    public void filter_list_not_null() {
        List<Flight> expected = FlightBuilder.createFlights();
        Assert.assertNotNull(expected);
    }

    @Test
    public void departure_until_the_current_time() {
        List<Flight> list = FlightBuilder.createFlights();
        final Predicate<Flight> departureUntilTheCurrentTime = flight -> flight.getSegments().get(0).getDepartureDate().isBefore(LocalDateTime.now());
        List<Flight> expected = FlightFilter.filter(list, departureUntilTheCurrentTime);
        List<Flight> actual = Arrays.asList(list.get(2));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void arrival_date_before_departure_date() {
        List<Flight> list = FlightBuilder.createFlights();
        final Predicate<Flight> arrivalDateBeforeDepartureDate = flight -> flight.getSegments().stream().anyMatch(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate()));
        List<Flight> expected = FlightFilter.filter(list, arrivalDateBeforeDepartureDate);
        List<Flight> actual = Arrays.asList(list.get(3));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void more_than_two_hours_on_earth() {
        List<Flight> list = FlightBuilder.createFlights();
        final Predicate<Flight> moreThanTwoHoursOnEarth = flight -> {
            Duration segmentsCalculate = flight.getSegments().stream().map(segment ->
                    Duration.between(segment.getDepartureDate(), segment.getArrivalDate())).reduce(Duration::plus).get();
            Duration flightCalculate = Duration.between(flight.getSegments().get(0).getDepartureDate(),
                    flight.getSegments().get(flight.getSegments().size() - 1).getArrivalDate());
            return flightCalculate.minus(segmentsCalculate).compareTo(Duration.ofHours(2)) > 0;
        };
        List<Flight> expected = FlightFilter.filter(list, moreThanTwoHoursOnEarth);
        List<Flight> actual = Arrays.asList(list.get(4),list.get(5));
        Assert.assertEquals(expected, actual);
    }
}