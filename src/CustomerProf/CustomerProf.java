package CustomerProf;
import VehicleInfo.VehicleInfo;

public class CustomerProf {

    String adminID;
    String firstName;
    String lastName;
    String address;
    String phone;
    float income;
    String use;
    String status;
    VehicleInfo VehicleInfo; /// Addition from the package VehicleInfo.java

    public CustomerProf(String adminID, String firstName, String lastName, String address, String phone, float income, String use, String status, VehicleInfo VehicleInfo){
        this.adminID = adminID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.income = income;
        this.use = use;
        this.status = status;
        this.VehicleInfo = VehicleInfo;
    }

    public String getAdminID() {
        return adminID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public float getIncome() {
        return income;
    }

    public String getUse(){ /// I was confused on why there is no update Use, but okay.
        return use;
    }

    public String getStatus() {
        return status;
    }

    public VehicleInfo getVehicleInfo(){
        return VehicleInfo;
    }

    public String updateFirstName(String firstName){
        this.firstName = firstName;
        return firstName;
    }

    public String updateLastName(String lastName){
        this.lastName = lastName;
        return lastName;
    }

    public String updateAddress(String address){
        this.address = address;
        return address;
    }

    public String updatePhone(String phone){
        if(phone.length() == 10){}
        else{
            phone = "1234567890";
        }
        this.phone = phone;
        return phone;
    }

    public float updateIncome(float income){
        this.income = income;
        return income;
    }

    public String updateStatus(String status){
        if(status.equals("Active") || status.equals("Inactive")){
            this.status = status;
            return this.status;
        }
        else{
            this.status = "Inactive"; /// This, just like in VehicleInfo, is to make sure no errors occur when updating
            return this.status;
        }
    }

    public VehicleInfo updateVehicleInfo(VehicleInfo VehicleInfo){
        this.VehicleInfo = VehicleInfo;
        return VehicleInfo;
    }
    /// Thought the function above wasn't necessary, as you can easily call for the editing of the class
    /// with the .getVehicleInfo().updatewhatever function. So if you wanted to edit just one part of it you could.
    /// Personally I didn't like some functions given to us as their abilities to help aren't helpful at all, rather
    /// it's easier to just implement them into the other function.
}
