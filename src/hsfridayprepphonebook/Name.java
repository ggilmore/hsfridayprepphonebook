package hsfridayprepphonebook;

import java.io.Serializable;

public class Name implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1627237292368961610L;

    private final String firstName;

    private final String lastName;

    public Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    public String toString() {
        return "Name: " + this.getFirstName() + " " + this.getLastName();
    }

    @Override
    public int hashCode() {
        return this.getFirstName().length() * this.getLastName().length();
    }

    @Override
    public boolean equals(Object objectToTest) {
        boolean isName = objectToTest instanceof Name;
        if (isName) {
            Name that = (Name) objectToTest;
            boolean sameFullName = this.getFullName()
                    .equals(that.getFullName());
            return sameFullName;
        }
        return isName;
    }

}
