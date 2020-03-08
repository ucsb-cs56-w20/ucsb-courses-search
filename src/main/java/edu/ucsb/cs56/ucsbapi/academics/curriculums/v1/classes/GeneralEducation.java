package edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes;

import java.util.Objects;

public class GeneralEducation {

    private String geCode;
    private String geCollege;

    public GeneralEducation() {}

    public String getGeCode() {
        return this.geCode;
    }

    public void setGeCode(String geCode) {
        this.geCode = geCode;
    }

    public String getGeCollege() {
        return this.geCollege;
    }

    public void setGeCollege(String geCollege) {
        this.geCollege = geCollege;
    }

    public GeneralEducation(String geCode, String geCollege) {
        this.geCode = geCode;
        this.geCollege = geCollege;
    }

    public GeneralEducation geCode(String geCode) {
        this.geCode = geCode;
        return this;
    }

    public GeneralEducation geCollege(String geCollege) {
        this.geCollege = geCollege;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof GeneralEducation)) {
            return false;
        }
        GeneralEducation generalEducation = (GeneralEducation) o;
        return Objects.equals(geCode, generalEducation.geCode) && Objects.equals(geCollege, generalEducation.geCollege);
    }

    @Override
    public int hashCode() {
        return Objects.hash(geCode, geCollege);
    }

    @Override
    public String toString() {
        return "{" +
            " geCode='" + getGeCode() + "'" +
            ", geCollege='" + getGeCollege() + "'" +
            "}";
    }


}