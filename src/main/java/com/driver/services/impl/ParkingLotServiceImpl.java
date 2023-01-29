package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.driver.model.SpotType.*;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {

        ParkingLot parkingLot = new ParkingLot(name,address);
        parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        Spot spot = new Spot();

        //set number of wheels

        if(numberOfWheels <= 2) numberOfWheels = 2;
        else if (numberOfWheels > 2 && numberOfWheels <= 4)  numberOfWheels = 4;

        // set spot
        if(numberOfWheels == 2 ) spot.setSpotType(TWO_WHEELER);
        else if(numberOfWheels == 4) spot.setSpotType(FOUR_WHEELER);
        else if(numberOfWheels > 4) spot.setSpotType(OTHERS);



        spot.setPricePerHour(pricePerHour);

        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();
        parkingLot.getSpotList().add(spot);
        spot.setParkingLot(parkingLot);

        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        Spot spot = spotRepository1.findById(spotId).orElse(null);
        if (!Objects.isNull(spot)) {
            ParkingLot parkingLot = spot.getParkingLot();
            List<Spot> spotList= parkingLot.getSpotList();
            for (Spot s : spotList) {
                if(s == spot)
                    spotRepository1.delete(s);

            }
            spotRepository1.deleteById(spotId);
        }
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).orElse(null);
        if (!Objects.isNull(parkingLot)) {
            Spot requiredSpot = null;
            for(Spot spot : parkingLot.getSpotList()){
                if(spot.getId() == spotId){
                    spot.setPricePerHour(pricePerHour);
                    requiredSpot = spot;
                }
            }

            spotRepository1.save(requiredSpot);
            parkingLotRepository1.save(parkingLot);
            return requiredSpot;
        }
        return null;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();
        parkingLotRepository1.delete(parkingLot);
    }
}
