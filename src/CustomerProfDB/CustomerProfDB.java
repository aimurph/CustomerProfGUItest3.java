package CustomerProfDB;
import java.io.IOException;
import CustomerProf.CustomerProf;
import VehicleInfo.VehicleInfo;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class CustomerProfDB{
    int numCustomer = 0;
    int currentCustomerIndex;
    String fileName;
    ArrayList<CustomerProf> customerList = new ArrayList<>(); /// The Array list made it easier for us
    /// to change and store data as we pleased.

    public CustomerProfDB(String file_name){
        this.fileName = file_name;
    }

    public CustomerProf insertNewProfile(CustomerProf CustomerProf){ /// Adds new customer to array and adds them to the total
        this.customerList.add(CustomerProf);
        this.numCustomer++;
        return CustomerProf;
    }

    public boolean deleteProfile(String adminID, String lastName) {
        int flag = 0;
        for (int i = 0; i < this.numCustomer; i++){
            if (this.customerList.get(i).getAdminID().equals(adminID) && this.customerList.get(i).getLastName().equals(lastName)){
                this.customerList.remove(i);
                /// The if statement above checks for both the adminID and last name to make sure they are in the
                /// array, and if they are they will remove them from the array and remove 1 off the total people
                this.numCustomer = this.numCustomer - 1;
                ++flag;
                break;
            }
        }
        return flag == 1; /// This checks if the for loop above was successful, sending out a true or false to the Interface
    }

    /// The findProfile uses the same code as deleteProfile, but obviously doesn't delete, but sends a return statement
    /// back either a customer profile or a null profile, stating that it doesn't exist
    public CustomerProf findProfile(String adminID, String lastName){
        for (int i = 0; i < this.numCustomer; i++){
            if(this.customerList.get(i).getAdminID().equals(adminID) && this.customerList.get(i).getLastName().equals(lastName)){
                return this.customerList.get(i);
            }
        }
        return null;
    }

    public CustomerProf findNextProfile(){
        if (this.currentCustomerIndex == this.numCustomer){
            this.currentCustomerIndex = 0;
        }
        if (this.currentCustomerIndex != this.numCustomer){
            this.currentCustomerIndex = this.currentCustomerIndex + 1;
        }
        return this.customerList.get(this.currentCustomerIndex);
    }

    public CustomerProf findFirstProfile(){
            return this.customerList.get(0);
    }
    /// These functions above just aren't useful, as we are using an array list, which already does these functions for us.
    /// They are still coded in, but it was much easier to call an array list and use that.


    public void writeAllCustomerProf() throws IOException {
        FileWriter writer = new FileWriter(this.fileName);
        int i = 1;
        for (CustomerProf customerProff: this.customerList){
            if(i != numCustomer) { /// This creates the entry and a newline to make sure we know which profiles are which
                writer.write(customerProff.getAdminID() + "\n"
                        + customerProff.getFirstName() + "\n" + customerProff.getLastName() + "\n"
                        + customerProff.getAddress() + "\n" + customerProff.getPhone() + "\n"
                        + customerProff.getIncome() + "\n" + customerProff.getStatus() + "\n"
                        + customerProff.getUse() + "\n" + customerProff.getVehicleInfo().getModel()
                        + "\n" + customerProff.getVehicleInfo().getYear() + "\n"
                        + customerProff.getVehicleInfo().getType() + "\n"
                        + customerProff.getVehicleInfo().getMethod() + System.lineSeparator());
                i++;
            }
            /// This part is the end of the text file, as if we use the InputIntoNewDatabase on this with only the code above
            /// It would break the scanner class below and not be able to read the file. This is because of the
            /// Reader.hasNextLine() if statement. It reads the System.lineSeparator() as a next line, and because of the
            /// scanner it tries to get the next strings, but it doesn't exist.
            else{
                writer.write(customerProff.getAdminID() + "\n"
                        + customerProff.getFirstName() + "\n" + customerProff.getLastName() + "\n"
                        + customerProff.getAddress() + "\n" + customerProff.getPhone() + "\n"
                        + customerProff.getIncome() + "\n" + customerProff.getStatus() + "\n"
                        + customerProff.getUse() + "\n" + customerProff.getVehicleInfo().getModel()
                        + "\n" + customerProff.getVehicleInfo().getYear() + "\n"
                        + customerProff.getVehicleInfo().getType() + "\n"
                        + customerProff.getVehicleInfo().getMethod());
            }
        }
        writer.close();
    }

    public void initializeDataBase(String new_filename) throws IOException{
        File new_File = new File(new_filename);
        try{
            if(new_File.createNewFile()){
                System.out.println("Database cannot be reached! Creating new file under the name: " + new_filename);
            }
            else{
                Scanner Reader = new Scanner(new_File);
                int count = 0;
                while(Reader.hasNextLine()){
                    String adminID;
                    String first_name;
                    String last_name;
                    String address;
                    String phone;
                    float income;
                    String use;
                    String status;
                    String model;
                    String year;
                    String type;
                    String method;
                    while(count < 1){
                        adminID = Reader.nextLine(); /// This was a test to see how you can read from the file
                        first_name = Reader.nextLine();
                        last_name = Reader.nextLine();
                        address = Reader.nextLine();
                        phone = Reader.nextLine();
                        income = Float.parseFloat(Reader.nextLine());
                        use = Reader.nextLine();
                        status = Reader.nextLine();
                        model = Reader.nextLine();
                        year = Reader.nextLine();
                        type = Reader.nextLine();
                        method = Reader.nextLine();
                        System.out.println(adminID + " " + first_name + " " + last_name + " " + address + " " + phone + " " + income
                                + " " + use + " " + status + " " + model + " " + year + " " + type + " " + method);
                        /// This was the first idea behind printing out the database file so that way the person could see
                        /// all the adminID's on the profile when in the process of testing
                        if(!(Reader.hasNextLine())){
                            break;
                        }
                    }
                }
                Reader.close();
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
    /// The functions below are the creations of making all the processes easier for us and the users who use the files
    /// we created. This first function takes everything from the text file and actually puts it in the database. This is
    /// because the program actually doesn't allow you to save it on the Interface file, so this was our work around for it.
    public boolean InputIntoNewDatabase(String filename) throws IOException {
        File new_File = new File(filename);
        CustomerProf tempprof;
        VehicleInfo tempvehicle;
        Scanner remake = new Scanner(new_File);
        if(!remake.hasNext()){ /// Checks if the file even has anything in it.
            return false;
        }
        else{
            while(remake.hasNextLine()){
                String adminID = remake.nextLine();
                String firstName = remake.nextLine();
                String lastName = remake.nextLine();
                String address = remake.nextLine();
                String phone = remake.nextLine();
                float income = Float.parseFloat(remake.nextLine());
                String use = remake.nextLine();
                String status = remake.nextLine();
                String model = remake.nextLine();
                String year = remake.nextLine();
                String type = remake.nextLine();
                String method = remake.nextLine();
                tempvehicle = new VehicleInfo(model, year, type, method);
                tempprof = new CustomerProf(adminID, firstName, lastName, address, phone, income, use, status, tempvehicle);
                insertNewProfile(tempprof);
                /// Takes in all the items on each line in the text file and makes them into their own customer profiles.
                /// Then it implements them into a temporary java database before it saves back into another/ the same text
                /// file.
            }
            remake.close();
            return true; /// Tells if the transfer of all elements from the database was successful.
        }
    }
    /// This function checks for if there is an actual entry in the first part of the database. If there is not,
    /// the database will create a new profile that you can use to open and close the file with. Basically the first
    /// admin profile.
    public boolean FirstProfileCheck() throws IndexOutOfBoundsException{
        if(numCustomer == 0){
            return false;
        }
        else{
            return true;
        }
    }
    /// This function basically does what the find profile does, but is a simpler version of such. It doesn't call for
    /// the last name, as it's only here to check if the adminID is actually in there, and not to find the profile.
    public boolean AdminIDcheck(String adminID){
        int flag = 0;
        for (int i = 0; i < this.numCustomer; i++){
            if(this.customerList.get(i).getAdminID().equals(adminID)){
                flag = flag + 1;
            }
        }
        if(flag == 0){
            return false;
        }
        else{
            return true;
        }
    }
    /// This function is the same thing as initializeDataBase but doesn't print anything. We used this so that way we can
    /// know if the file is actually created or not without the user knowing.
    public void initializeDataBaseNoPrint(String new_filename) throws IOException{
        File new_File = new File(new_filename);
        try{
            if(new_File.createNewFile()){
                System.out.println("Database cannot be reached! Creating new file under the name: " + new_filename);
            }
            else{
                Scanner Reader = new Scanner(new_File);
                int count = 0;
                while(Reader.hasNextLine()){
                    String adminID;
                    String first_name;
                    String last_name;
                    String address;
                    String phone;
                    float income;
                    String use;
                    String status;
                    String model;
                    String year;
                    String type;
                    String method;
                    while(count < 1){
                        adminID = Reader.nextLine(); /// This was a test to see how you can read from the file
                        first_name = Reader.nextLine();
                        last_name = Reader.nextLine();
                        address = Reader.nextLine();
                        phone = Reader.nextLine();
                        income = Float.parseFloat(Reader.nextLine());
                        use = Reader.nextLine();
                        status = Reader.nextLine();
                        model = Reader.nextLine();
                        year = Reader.nextLine();
                        type = Reader.nextLine();
                        method = Reader.nextLine();
                        /// This was the first idea behind printing out the database file so that way the person could see
                        /// all the adminID's on the profile when in the process of testing
                        if(!(Reader.hasNextLine())){
                            count = 1;
                        }
                    }
                    //System.out.println(adminID + data);
                }
                Reader.close();
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

}
