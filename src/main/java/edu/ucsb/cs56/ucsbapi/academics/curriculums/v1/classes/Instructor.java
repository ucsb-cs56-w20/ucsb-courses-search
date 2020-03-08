package edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes;
import lombok.Data;

@Data
public class Instructor {
    private String instructor;
    private String functionCode;

    public String getInstructor() {
        return this.instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getFunctionCode() {
        return this.functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }
    public Instructor() {}
}