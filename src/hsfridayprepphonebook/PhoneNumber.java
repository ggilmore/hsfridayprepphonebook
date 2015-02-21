package hsfridayprepphonebook;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

public class PhoneNumber implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2738120421561730159L;

    private final long phoneNumber;

    private final static int LARGE_PRIME = 961748941;

    private final int lengthNumber;

    public PhoneNumber(Long phoneNumber) throws RuntimeException {
        this.lengthNumber = phoneNumber.toString().length();
        if (!Arrays.asList(7, 10).contains(this.lengthNumber)) {
            System.out.println(this.lengthNumber);
            throw new RuntimeException("bad number Length");
        }
        this.phoneNumber = phoneNumber;
    }

    public long getNumber() {
        return this.phoneNumber;
    }

    @Override
    public boolean equals(Object objectToTest) {
        boolean isPhoneNumber = objectToTest instanceof PhoneNumber;
        if (isPhoneNumber){
            PhoneNumber that = (PhoneNumber) objectToTest;
            boolean sameNumber = this.getNumber() == that.getNumber();
            return isPhoneNumber && sameNumber;
        }
        return isPhoneNumber;
        

    }
        

    @Override
    public int hashCode() {
        return (int) (this.phoneNumber % LARGE_PRIME);
    }

    @Override
    public String toString() {
        return "Phone#: " + this.phoneNumber;
    }

    public String prettyPrint() {
        String numbString = String.valueOf(this.phoneNumber);
        if (this.lengthNumber == 7) {
            return numbString.substring(0, 3) + " " + numbString.substring(3);
        } else {
            return numbString.substring(0, 3) + " "
                    + numbString.substring(3, 6) + " "
                    + numbString.substring(6);
        }

    }

}
