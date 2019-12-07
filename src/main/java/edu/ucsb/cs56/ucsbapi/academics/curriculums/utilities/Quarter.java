package edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities;

/**
 * Represents a UCSB quarter. Allows easy conversion between QYY alphanumeric
 * format (F19, W20, S20, M20) and YYYYQ numerical format (20194, 20201, 20202,
 * 20203) as well as incrementing and decrementing.
 *
 */

public class Quarter {

    private int yyyyq; // YYYYQ where Q = 1, 2, 3 or 4

    public Quarter(int yyyyq) {
        setValue(yyyyq);
    }

    public int getValue() {
        return this.yyyyq;
    }

    public void setValue(int yyyyq) {
        if (invalidQtr(yyyyq))
            throw new IllegalArgumentException("Quarter constructor requires a integer ending in 1,2,3 or 4");
        this.yyyyq = yyyyq;
    }

    public Quarter(String qyy) {
        if (qyy.length() != 3) {
            throw new IllegalArgumentException("Quarter requires string of length 3");
        }
        char q = qyy.charAt(0);
        String yy = qyy.substring(1, 3);
        String legalQuarters = "WSMF";
        int qInt = legalQuarters.indexOf(q) + 1;
        if (invalidQtr(qInt)) {
            throw new IllegalArgumentException("First char should be one of " + legalQuarters);
        }
        int yyInt = Integer.parseInt(yy);
        int century = (yyInt > 50) ? 1900 : 2000;
        this.yyyyq = (century + yyInt) * 10 + qInt;
    }

    public String getYY() {
        return String.format("%02d", (yyyyq / 10) % 100);
    }

    public String getYYYY() {
        return String.format("%04d", (yyyyq / 10));
    }

    public String toString() {
        return String.format("%s%s", getQ(), getYY());
    }

    public String getQ() {
        String[] quarters = new String[] { "W", "S", "M", "F" };
        int index = yyyyq % 10;
        if (index < 1 || index > 4)
            throw new IllegalStateException("Invalid value for quarter " + index);
        return quarters[index - 1];
    }

    private static boolean invalidQtr(int value) {
        int index = value % 10;
        return (index < 1) || (index > 4);
    }

    /**
     * Advance to the next quater, and return the value of that quarter as an int.
     * @return
     */
    public int increment() {
        int q = this.yyyyq % 10;
        int yyyy = this.yyyyq / 10;

        setValue( (q==4) ? (((yyyy + 1) * 10) + 1 ) : (this.yyyyq + 1) );
        return this.yyyyq;
    }

}