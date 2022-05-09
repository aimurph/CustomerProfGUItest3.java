package VehicleInfo;
public class VehicleInfo{
    String model;
    String year;
    String type;
    String method;

    public VehicleInfo(String model, String year, String type, String method){
        this.model = model;
        this.year = year;
        this.type = type;
        this.method = method;
    }
    /// This and CustomerProf were made with the generate button given to us by IntelliJ. They were quite useful,
    /// but the way they generate makes me question the syntax. Just not used to seeing them this way.

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }

    public String getType() {
        return type;
    }

    public String getMethod() {
        return method;
    }

    public String updateModel(String model){
        this.model = model;
        return model;
    }

    public String updateYear(String year) {
        this.year = year;
        return year;
    }

    public String updateType(String type){
        // This if statement below makes it so that it checks if the update to the Vehicle Info is the correct version
        if(type.equals("Sedan") || type.equals("Hatchback") || type.equals("Luxury") || type.equals("Sport") || type.equals("Other")){
            this.type = type;
            return this.type;
        }
        else{
            this.type = "Other";
            return this.type;
        }

    }
    //sedan, hatchback, luxury, sport, other

    public String updateMethod(String method){
        if(method.equals("New") || method.equals("Certified-Pre-Owned") || method.equals("Used") || method.equals("Other")){
            this.method = method;
            return this.method;
        }
        else{
            this.method = "Other"; // This, like above prevents people from entering a false Method or Type, and if they do
            return this.method; // It makes it "other" and returns it so that it causes less trouble with coding
        }
    }

}



