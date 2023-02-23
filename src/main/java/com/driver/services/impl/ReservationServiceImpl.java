package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        try {
            if (!userRepository3.findById(userId).isPresent() || !parkingLotRepository3.findById(parkingLotId).isPresent()) {
                throw new Exception("Cannot make reservation");
            }
            User user = userRepository3.findById(userId).get();
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();

            List<Spot> spotList = parkingLot.getSpotList();
            boolean LookForSpot = false;
            for (Spot findspot : spotList) {
                if (!findspot.getOccupied()) {
                    LookForSpot = true;
                    break;
                }

            }
            if (!LookForSpot) {
                throw new Exception("Cannot make reservation");
            }
            SpotType SetSpotType;

            if (numberOfWheels > 4) SetSpotType = SpotType.OTHERS;
            else if (numberOfWheels > 2) SetSpotType = SpotType.FOUR_WHEELER;
            else SetSpotType = SpotType.TWO_WHEELER;

            int minPrice = Integer.MAX_VALUE;
            LookForSpot = false;
            Spot bookSpot = null;

            if (SetSpotType.equals(SpotType.OTHERS) ) {
                for (Spot find : spotList) {
                    if (find.getSpotType().equals(SpotType.OTHERS)) {
                        int price = find.getPricePerHour() * timeInHours;
                        if (find.getOccupied() == false && price < minPrice) {
                            minPrice = price;
                            LookForSpot = true;
                            bookSpot = find;
                        }
                    }
                }
            } else if (SetSpotType.equals(SpotType.FOUR_WHEELER) ) {
                for (Spot find : spotList) {
                    if (find.getSpotType().equals(SpotType.FOUR_WHEELER) || find.getSpotType().equals(SpotType.OTHERS)) {
                        int price = find.getPricePerHour() * timeInHours;
                        if (price < minPrice && find.getOccupied() == false) {
                            price = minPrice;
                            LookForSpot = true;
                            bookSpot = find;
                        }
                    }
                }
            } else if (SetSpotType.equals(SpotType.TWO_WHEELER)) {
                for (Spot find : spotList) {

                    int price = find.getPricePerHour() * timeInHours;
                    if (!find.getOccupied() && price < minPrice) {
                        price = minPrice;
                        LookForSpot = true;
                        bookSpot = find;

                    }
                }
            }
            if (LookForSpot == false || bookSpot == null) {
                throw new Exception("Cannot make reservation");
            }
            bookSpot.setOccupied(true);

            Reservation reservation = new Reservation();
            reservation.setNumberOfHours(timeInHours);
            reservation.setSpot(bookSpot);
            reservation.setUser(user);

            bookSpot.getReservationList().add(reservation);
            user.getReservationList().add(reservation);

            spotRepository3.save(bookSpot);
            userRepository3.save(user);

            return reservation;

        }catch(Exception e){
            throw new Exception("Cannot make reservation");
        }
    }
}