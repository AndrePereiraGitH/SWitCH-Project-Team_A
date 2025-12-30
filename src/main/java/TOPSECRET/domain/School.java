package TOPSECRET.domain;


public class School
{
    private Location _location = null;
    private String _name;


    public School( String name) {
        this._name = name;
    }

    public Location defineLocation( String street, String postalCode) throws InstantiationException
    {
        this._location = new Location( street, postalCode);

        return new Location( street, postalCode);
    }

    public String getName() {
        return this._name;
    }

}
