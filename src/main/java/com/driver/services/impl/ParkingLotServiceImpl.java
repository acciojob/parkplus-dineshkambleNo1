package com.driver.services.impl;

import com.driver.Enums.SpotType;
import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.getName();
        parkingLot.getAddress();
        parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {

        Spot spot = new Spot();

        if(numberOfWheels <=  2 ) numberOfWheels = 2;

        else if (numberOfWheels >2 && numberOfWheels<= 4)  { numberOfWheels = 4; }

        if(numberOfWheels == 2 ) spot.setSpotType(SpotType.TWO_WHEELER);
        else if (numberOfWheels == 4 ) spot.setSpotType(SpotType.FOUR_WHEELER);
        else spot.setSpotType(SpotType.OTHERS);

        spot.setPricePerHour(pricePerHour);

        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        parkingLot.getSpotList().add(spot);
        spot.setPricePerHour(pricePerHour);

        return spot;


    }

    @Override
    public void deleteSpot(int spotId) { spotRepository1.deleteById(spotId); }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Spot spot = spotRepository1.findById(spotId).get();

        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        spot.setParkingLot(parkingLot);
        spot.setPricePerHour(pricePerHour);
        spotRepository1.save(spot);

        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {

        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        parkingLotRepository1.delete(parkingLot);
    }
}