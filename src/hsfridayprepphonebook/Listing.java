package hsfridayprepphonebook;

import java.io.Serializable;

public class Listing implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -3303716291553097512L;

    private final Name name;

    private final PhoneNumber number;

    public Listing(Name name, PhoneNumber number) {
        this.name = name;
        this.number = number;
    }

    public Name getName() {
        return this.name;
    }

    public PhoneNumber getNumber() {
        return this.number;
    }

    private String prettyPrint() {
        return this.name.getFullName() + " " + this.number.prettyPrint();
    }

    @Override
    public String toString() {
//        return "Listing: " + this.name.toString() + " "
//                + this.number.toString();
        return prettyPrint();
    }

    @Override
    public boolean equals(Object objectToTest) {
        boolean isListing = objectToTest instanceof Listing;
        if (isListing) {
            Listing that = (Listing) objectToTest;
            boolean sameName = this.getName().equals(that.getName());
            boolean samePhoneNumber = this.getNumber().equals(that.getNumber());
            return sameName && samePhoneNumber;
        }
        return isListing;
    }
    
    
}
