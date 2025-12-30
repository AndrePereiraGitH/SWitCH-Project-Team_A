package TOPSECRET.dto;

import TOPSECRET.domain.Location;

public class LocationDTO {
    public final String street;
    public final String postalCode;

    public LocationDTO( Location location )
    {
        this.street = location.getStreet();
        this.postalCode = location.getPostalCode();
    }
}
