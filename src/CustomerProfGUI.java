import VehicleInfo.VehicleInfo;
import CustomerProf.CustomerProf;
import CustomerProfDB.CustomerProfDB;
import com.sun.jdi.event.ExceptionEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Year;
import java.util.Scanner;

/// This Code was quite difficult to write as this document at the time is around 1500 lines of code
/// For the person who is even looking at this code, I will give you a key to look at if you want to look at specific
/// pages for this code.
/// Key: 24-54 -> JFrame and Panel Setup, 55-136 -> Main Menu Setup, 137-397 -> Create Profile
/// 398-422 -> Delete Success, 423-470 -> Delete Profile, 471-852 -> Update Profile Landing Page
/// 853-1132 -> Update Profile, 1133-1230 -> Find/Display Profile Landing Page, 1231-1296 -> Find Profile
/// 1297 -1419 -> Display All Landing Page, 1420 - 1483 -> Display All Menu.
/// The rest is a combination of putting everything in the single panel and starting up the profile
///
/// Most of the large chunks of code are confusing, but to summarize what they are, they are basically copy and pastes
// of each other, meaning that alot of this code on the document is actually redunent and not really needing
/// to be read thouroughly, so I will comment on some things, but all of them are really not needed to.
/// so this might be annoying to scroll through, and we are sorry for our code being this way, but this is what
/// happens when cardlayout is used for every panel that should or could be a frame.
/// -A

public class CustomerProfGUI{

    /// Setting up everything we need for functionality
    static CustomerProfDB database;
    static CustomerProf tempcustprof;
    static Scanner Reader;
    static String file_name;

    int selection;
    int Create_Profile_Check_Use = 0; /// Unused varaibles
    int Create_Profile_Check_Status = 0;
    int Create_Profile_Check_Type = 0;
    int Create_Profile_Check_Method = 0;

    /// Setting up the frames and panels to be used
    JFrame frame;
    JPanel mainpanel = new JPanel();
    CardLayout cardlayout = new CardLayout();


    JPanel Main_Menu, Create_Profile, Delete_Profile, Update_Profile, Find_Profile, Display_All_Profiles;
    JPanel Success_Page, Delete_Success_Page, Update_Item_Page, Found_Profile_Page, Display_All_Profiles_Page;

    public CustomerProfGUI() throws IOException {
        /// Database setup so you can edit that specific database
        file_name = "new_test.txt";
        database = new CustomerProfDB(file_name);
        database.initializeDataBaseNoPrint(file_name);
        database.InputIntoNewDatabase(file_name);


        /// Frame setup
        frame = new JFrame("Integrated Customer System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(216, 700);

        /// Card layout in order to seamlessly change panels

////////////////// Main Menu Panel Setup /////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Main_Menu = new JPanel();
        Main_Menu.setBackground(new Color(42, 222, 106));
        //Main_Menu.setLayout(new GridLayout(0, 1));

        /// Setup of the Radio and Select button
        JLabel ICS_Title1 = new JLabel("Integrated");
        ICS_Title1.setFont(new Font("Serif", Font.PLAIN, 36));
        JLabel ICS_Title2 = new JLabel("Customer System");
        ICS_Title2.setFont(new Font("Serif", Font.PLAIN, 26));

        JRadioButton Create_Profile_Button = new JRadioButton("Create Profile");
        JRadioButton Delete_Profile_Button = new JRadioButton("Delete Profile");
        JRadioButton Update_Profile_Button = new JRadioButton("Update Profile");
        JRadioButton Find_Profile_Button = new JRadioButton("Find/Display Profile");
        JRadioButton Display_All_Profile_Button = new JRadioButton("Display All Profiles");

        JButton Select = new JButton("Select");
        JLabel MenuError = new JLabel("Please Select an Option");
        MenuError.setVisible(false);

        /// Makes it so you can only choose one option
        ButtonGroup Menu_Button_Group = new ButtonGroup();
        Menu_Button_Group.add(Create_Profile_Button);
        Menu_Button_Group.add(Delete_Profile_Button);
        Menu_Button_Group.add(Update_Profile_Button);
        Menu_Button_Group.add(Find_Profile_Button);
        Menu_Button_Group.add(Display_All_Profile_Button);

        /// Adding The actions to the JRadioButtons and Select button

        Create_Profile_Button.addActionListener(e ->{
            selection = 1;
        });

        Delete_Profile_Button.addActionListener(e ->{
            selection = 2;
        });

        Update_Profile_Button.addActionListener(e ->{
            selection = 3;
        });

        Find_Profile_Button.addActionListener(e ->{
            selection = 4;
        });

        Display_All_Profile_Button.addActionListener(e ->{
            selection = 5;
        });

        /// Sends you to specific panels
        Select.addActionListener(e ->{
            if(selection == 0){
                MenuError.setVisible(true);
            }
            if(selection == 1){
                cardlayout.show(mainpanel, "Create_Profile");
            }
            if(selection == 2){
                cardlayout.show(mainpanel, "Delete_Profile");
            }
            if(selection == 3){
                cardlayout.show(mainpanel, "Update_Profile");
            }
            if(selection == 4){
                cardlayout.show(mainpanel, "Find_Profile");
            }
            if(selection == 5){
                cardlayout.show(mainpanel, "Display_All_Profiles");
            }
        });

        Main_Menu.add(ICS_Title1);
        Main_Menu.add(ICS_Title2);
        Main_Menu.add(Create_Profile_Button);
        Main_Menu.add(Delete_Profile_Button);
        Main_Menu.add(Update_Profile_Button);
        Main_Menu.add(Find_Profile_Button);
        Main_Menu.add(Display_All_Profile_Button);
        Main_Menu.add(Select);
        Main_Menu.add(MenuError);

////////////////// Create Profile Menu Setup /////////////////////////////////////////////////////////////////////////////////////////////////////////

        Create_Profile = new JPanel();
        Create_Profile.setBackground(new Color(42, 222, 106));
        //Create_Profile.setLayout(new GridLayout(0, 1));

        //frame.setSize(500, 800);

        /// Error Set-up
        JLabel surprise = new JLabel("There was an Error");
        JLabel phone_length_incorrect = new JLabel("Phone length too short");
        JLabel CombosWereSelected = new JLabel("Select an Option");
        surprise.setVisible(false);
        phone_length_incorrect.setVisible(false);
        CombosWereSelected.setVisible(false);

        JLabel Create_Profile_Title = new JLabel("Create Profile");
        JLabel Create_Profile_Spacer = new JLabel();
        Create_Profile_Title.setFont(new Font("Serif", Font.PLAIN, 26));
        ///Setup of Text Boxes to the Question Fields

        JLabel AdminID_Label = new JLabel("AdminID: ");
        JLabel First_Name_Label = new JLabel("First Name: ");
        JLabel Last_Name_Label = new JLabel("Last Name: ");
        JLabel Address_Label = new JLabel("Address: ");
        JLabel Phone_Label = new JLabel("Phone: ");
        JLabel Income_Label = new JLabel("Income: ");
        JLabel Use_Menu_Label = new JLabel("Use: ");
        JLabel Status_Menu_Label = new JLabel("Status: ");
        JLabel Model_Label = new JLabel("Model: ");
        JLabel Year_Label = new JLabel("Year: ");
        JLabel Type_Menu_Label = new JLabel("Type: ");
        JLabel Method_Menu_Label = new JLabel("Method: ");

        /// Setup of Question Field
        JTextField AdminID_Field = new JTextField(10);
        JTextField First_Name_Field = new JTextField(10);
        JTextField Last_Name_Field = new JTextField(10);
        JTextField Address_Field = new JTextField(10);
        JTextField Phone_Field = new JTextField(10);
        JTextField Income_Field = new JTextField(10);
        JComboBox Use_Menu_Field = new JComboBox();
        JComboBox Status_Menu_Field = new JComboBox();
        JTextField Model_Field = new JTextField(10);
        JTextField Year_Field = new JTextField(10);
        JComboBox Type_Menu_Field = new JComboBox();
        JComboBox Method_Menu_Field = new JComboBox();

        JButton Submit = new JButton("Submit");

        /// Adding Materials to the ComboBoxes
        Use_Menu_Field.addItem("Please Select an Option");
        Use_Menu_Field.addItem("Business");
        Use_Menu_Field.addItem("Personal");
        Use_Menu_Field.addItem("Both");

        Status_Menu_Field.addItem("Please Select an Option");
        Status_Menu_Field.addItem("Active");
        Status_Menu_Field.addItem("Inactive");

        Type_Menu_Field.addItem("Please Select an Option");
        Type_Menu_Field.addItem("Sedan");
        Type_Menu_Field.addItem("Hatchback");
        Type_Menu_Field.addItem("Luxury");
        Type_Menu_Field.addItem("Sport");
        Type_Menu_Field.addItem("Other");

        Method_Menu_Field.addItem("Please Select an Option");
        Method_Menu_Field.addItem("New");
        Method_Menu_Field.addItem("Certified Pre-Owned");
        Method_Menu_Field.addItem("Used");
        Method_Menu_Field.addItem("Other");

        /// These variables are used for the action listeners below, which are used for
        /// Option selection with the boxes, and contain some unneeded variables
        final String[] use_variable = new String[1];
        final String[] status_variable = new String[1];
        final String[] type_variable = new String[1];
        final String[] method_variable = new String[1];

        Use_Menu_Field.addActionListener(e->{
            if(e.getSource() == Use_Menu_Field){
                JComboBox cb = (JComboBox)e.getSource();
                String msg = (String)cb.getSelectedItem();
                if(msg.equals("Business")){
                    use_variable[0] = msg;
                    Create_Profile_Check_Use = 1;
                }
                if(msg.equals("Personal")){
                    use_variable[0] = msg;
                    Create_Profile_Check_Use = 1;
                }
                if(msg.equals("Both")){
                    use_variable[0] = msg;
                    Create_Profile_Check_Use = 1;
                }
                else{
                    Create_Profile_Check_Use = 0;
                }
            }
        });

        Status_Menu_Field.addActionListener(e->{
            if(e.getSource() == Status_Menu_Field){
                JComboBox cb = (JComboBox)e.getSource();
                String msg = (String)cb.getSelectedItem();
                if(msg.equals("Active")){
                    surprise.setVisible(false);
                    status_variable[0] = msg;
                    Create_Profile_Check_Status = 0;

                }
                if(msg.equals("Inactive")){
                    surprise.setVisible(false);
                    status_variable[0] = msg;
                    Create_Profile_Check_Status = 0;

                }
                else{
                    Create_Profile_Check_Status = 0;
                }
            }
        });

        Type_Menu_Field.addActionListener(e->{
            if(e.getSource() == Type_Menu_Field){
                JComboBox cb = (JComboBox)e.getSource();
                String msg = (String)cb.getSelectedItem();
                if(msg.equals("Sedan")){
                    surprise.setVisible(false);
                    type_variable[0] = msg;
                    Create_Profile_Check_Type = 1;
                }
                if(msg.equals("Hatchback")){
                    surprise.setVisible(false);
                    type_variable[0] = msg;
                    Create_Profile_Check_Type = 1;
                }
                if(msg.equals("Sport")){
                    surprise.setVisible(false);
                    type_variable[0] = msg;
                    Create_Profile_Check_Type = 1;
                }
                if(msg.equals("Luxury")){
                    surprise.setVisible(false);
                    type_variable[0] = msg;
                    Create_Profile_Check_Type = 1;
                }
                if(msg.equals("Other")){
                    surprise.setVisible(false);
                    type_variable[0] = msg;
                    Create_Profile_Check_Type = 1;
                }
                else{
                    Create_Profile_Check_Type = 0;
                }
            }
        });

        Method_Menu_Field.addActionListener(e->{
            if(e.getSource() == Method_Menu_Field){
                JComboBox cb = (JComboBox)e.getSource();
                String msg = (String)cb.getSelectedItem();
                if(msg.equals("New")){
                    surprise.setVisible(false);
                    method_variable[0] = msg;
                    Create_Profile_Check_Method = 1;
                }
                if(msg.equals("Certified Pre-Owned")){
                    surprise.setVisible(false);
                    method_variable[0] = msg;
                    Create_Profile_Check_Method = 1;
                }
                if(msg.equals("Used")){
                    surprise.setVisible(false);
                    method_variable[0] = msg;
                    Create_Profile_Check_Method = 1;
                }
                if(msg.equals("Other")){
                    surprise.setVisible(false);
                    method_variable[0] = msg;
                    Create_Profile_Check_Method = 1;
                }
                else{
                    Create_Profile_Check_Method = 0;
                }
            }
        });

        Submit.addActionListener(e->{
            CombosWereSelected.setVisible(false);
            surprise.setVisible(false);
            try{
                String adminID = AdminID_Field.getText();
                String first_name = First_Name_Field.getText();
                String last_name = Last_Name_Field.getText();
                String address = Address_Field.getText();
                String phone = Phone_Field.getText();
                if (phone.length() == 10) {
                    phone_length_incorrect.setVisible(false);
                    float income = Float.parseFloat(Income_Field.getText());
                    String use = use_variable[0];
                    String status = status_variable[0];
                    String model = Model_Field.getText();
                    String year = Year_Field.getText();
                    String type = type_variable[0];
                    String method = method_variable[0];

                    VehicleInfo temp_vehicle_info = new VehicleInfo(model, year, type, method);
                    CustomerProf temp_cust_profile = new CustomerProf(adminID, first_name, last_name, address, phone, income, use,
                            status, temp_vehicle_info);
                    database.insertNewProfile(temp_cust_profile);
                    database.writeAllCustomerProf();

                    cardlayout.show(mainpanel, "Success_Page");
                }else{
                    System.out.println("Reaches");
                    JOptionPane.showMessageDialog(null, "Please input 10 numbers");
                }
            }catch (Exception error){
                surprise.setVisible(true);
            }

        });

        /// Return to menu buttons, you will be seeing these often and I am sorry
        JButton Return_To_Menu1 = new JButton("Return to Menu");
        Return_To_Menu1.addActionListener(e->{
            cardlayout.show(mainpanel, "Main_Menu");
        });

        /// Adding all to the panels
        Create_Profile.add(Create_Profile_Title);
        Create_Profile.add(Create_Profile_Spacer);
        Create_Profile.add(AdminID_Label);
        Create_Profile.add(AdminID_Field);
        Create_Profile.add(First_Name_Label);
        Create_Profile.add(First_Name_Field);
        Create_Profile.add(Last_Name_Label);
        Create_Profile.add(Last_Name_Field);
        Create_Profile.add(Address_Label);
        Create_Profile.add(Address_Field);
        Create_Profile.add(Phone_Label);
        Create_Profile.add(Phone_Field);
        Create_Profile.add(Income_Label);
        Create_Profile.add(Income_Field);
        Create_Profile.add(Use_Menu_Label);
        Create_Profile.add(Use_Menu_Field);
        Create_Profile.add(Status_Menu_Label);
        Create_Profile.add(Status_Menu_Field);
        Create_Profile.add(Model_Label);
        Create_Profile.add(Model_Field);
        Create_Profile.add(Year_Label);
        Create_Profile.add(Year_Field);
        Create_Profile.add(Type_Menu_Label);
        Create_Profile.add(Type_Menu_Field);
        Create_Profile.add(Method_Menu_Label);
        Create_Profile.add(Method_Menu_Field);

        Create_Profile.add(Submit);
        Create_Profile.add(surprise);
        Create_Profile.add(phone_length_incorrect);
        Create_Profile.add(CombosWereSelected);
        Create_Profile.add(Return_To_Menu1);

//////////////// Delete Success Page Setup /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Delete_Success_Page = new JPanel();
        Delete_Success_Page.setBackground(new Color(42, 222, 106));

        /// Setup of the Panel in order for it to look nice
        JLabel Delete_Profile_Success_Title = new JLabel("Delete Profile");
        Delete_Profile_Success_Title.setFont(new Font("Serif", Font.PLAIN, 26));

        JLabel Delete_Success_Label = new JLabel("Profile Deleted!");

        /// Adding buttons and stuff for functonality
        JButton Delete_Success_Page_Button = new JButton("OK");
        Delete_Success_Page_Button.addActionListener(e->{
            cardlayout.show(mainpanel,"Delete_Profile");
        });

        JButton Return_To_Menu3 = new JButton("Return to Menu");
        Return_To_Menu3.addActionListener(e->{
            cardlayout.show(mainpanel, "Main_Menu");
        });

        Delete_Success_Page.add(Delete_Profile_Success_Title);
        Delete_Success_Page.add(Delete_Success_Label);
        Delete_Success_Page.add(Delete_Success_Page_Button);
        Delete_Success_Page.add(Return_To_Menu3);

///////////////// Delete Profile Menu Setup //////////////////////////////////////////////////////////////////////////////////////////////////////////

        Delete_Profile = new JPanel();
        Delete_Profile.setBackground(new Color(42, 222, 106));

        JLabel Delete_Profile_Title = new JLabel("Delete Profile");
        Delete_Profile_Title.setFont(new Font("Serif", Font.PLAIN, 26));

        JTextField Delete_Profile_AdminID_Field = new JTextField(10);
        JTextField Delete_Profile_Last_Field = new JTextField(10);

        JLabel AdminID_Label1 = new JLabel("AdminID: ");
        JLabel Last_Name_Label1 = new JLabel("Last Name: ");


        /// Checks if the profile exists at the action event listener, and sends you to the page
        /// Where it says whether or not you deleted the profile
        JButton Delete_Profile_Delete_Button = new JButton("Delete");
        Delete_Profile_Delete_Button.addActionListener(e->{
            String Admin_ID = Delete_Profile_AdminID_Field.getText();
            String Last_Name = Delete_Profile_Last_Field.getText();
            if(database.deleteProfile(Admin_ID, Last_Name)){
                try {
                    database.writeAllCustomerProf();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Delete_Success_Label.setText("Profile Deleted!");
                cardlayout.show(mainpanel,"Delete_Success_Page");
            }
            else{
                Delete_Success_Label.setText("Profile was not Deleted!  ");
                cardlayout.show(mainpanel,"Delete_Success_Page");
            }
        });

        JButton Return_To_Menu2 = new JButton("Return to Menu");
        Return_To_Menu2.addActionListener(e->{
            cardlayout.show(mainpanel, "Main_Menu");
        });

        Delete_Profile.add(Delete_Profile_Title);
        Delete_Profile.add(AdminID_Label1);
        Delete_Profile.add(Delete_Profile_AdminID_Field);
        Delete_Profile.add(Last_Name_Label1);
        Delete_Profile.add(Delete_Profile_Last_Field);
        Delete_Profile.add(Delete_Profile_Delete_Button);
        Delete_Profile.add(Return_To_Menu2);


/////////////// Update Item Page Setup //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Update_Item_Page = new JPanel();
        Update_Item_Page.setBackground(new Color(42, 222, 106));

        JLabel Update_Item_Title = new JLabel("     Update     ");
        Update_Item_Title.setFont(new Font("Serif", Font.PLAIN, 28));

        JLabel Update_Item_Admin_Label = new JLabel();
        JLabel Update_Item_Last_Label = new JLabel();

        /// These look really weird in text, but on the GUI they look fine
        /// We really didn't know how to format well, but this was the best at it
        /// as those grid layouts and stuff looked really ugly, so thank your eyes
        /// That they weren't implemented, at least I hope...
        JLabel Update_Item_Admin_ID_Label = new JLabel(" Admin ID -   ");
        JLabel Update_Item_Last_Name_Label = new JLabel("     Last Name -       ");
        JLabel Updated_Field_Choice_Label = new JLabel();

        /// Setup, honestly it's just like really long so to save you time, I will tell you what we did
        /// Created a bunch of fields to be changed, just like in create profile,
        /// but they were hidden to the naked eye, so you can't see them unless you see this.
        /// We next then created listeners to each event in the next section so we can hide everything that we
        /// don't need. Then finally, each of them gets the submission we put after we click the submit button,
        /// and updates it.
        ///
        JTextField First_Name_Update_Item_Field = new JTextField(10);
        JTextField Last_Name_Update_Item_Field = new JTextField(10);
        JTextField Address_Update_Item_Field = new JTextField(10);
        JTextField Phone_Update_Item_Field = new JTextField(10);
        JTextField Income_Update_Item_Field = new JTextField(10);
        JComboBox Use_Update_Item_Field = new JComboBox();
        JComboBox Status_Update_Item_Field = new JComboBox();
        JTextField Model_Update_Item_Field = new JTextField(10);
        JTextField Year_Update_Item_Field = new JTextField(10);
        JComboBox Type_Update_Item_Field = new JComboBox();
        JComboBox Method_Update_Item_Field = new JComboBox();

        Use_Update_Item_Field.addItem("Please Select an Option");
        Use_Update_Item_Field.addItem("Business");
        Use_Update_Item_Field.addItem("Personal");
        Use_Update_Item_Field.addItem("Both");

        Status_Update_Item_Field.addItem("Please Select an Option");
        Status_Update_Item_Field.addItem("Active");
        Status_Update_Item_Field.addItem("Inactive");

        Type_Update_Item_Field.addItem("Please Select an Option");
        Type_Update_Item_Field.addItem("Sedan");
        Type_Update_Item_Field.addItem("Hatchback");
        Type_Update_Item_Field.addItem("Luxury");
        Type_Update_Item_Field.addItem("Sport");
        Type_Update_Item_Field.addItem("Other");

        Method_Update_Item_Field.addItem("Please Select an Option");
        Method_Update_Item_Field.addItem("New");
        Method_Update_Item_Field.addItem("Certified Pre-Owned");
        Method_Update_Item_Field.addItem("Used");
        Method_Update_Item_Field.addItem("Other");

        final String[] use_update_variable = new String[1];
        final String[] status_update_variable = new String[1];
        final String[] type_update_variable = new String[1];
        final String[] method_update_variable = new String[1];

        Use_Update_Item_Field.addActionListener(e->{
            if(e.getSource() == Use_Update_Item_Field){
                JComboBox cb = (JComboBox)e.getSource();
                String msg = (String)cb.getSelectedItem();
                if(msg.equals("Business")){
                    use_update_variable[0] = msg;
                }
                if(msg.equals("Personal")){
                    use_update_variable[0] = msg;
                }
                if(msg.equals("Both")){
                    use_update_variable[0] = msg;
                }
                else{

                }
            }
        });

        Status_Update_Item_Field.addActionListener(e->{
            if(e.getSource() == Status_Update_Item_Field){
                JComboBox cb = (JComboBox)e.getSource();
                String msg = (String)cb.getSelectedItem();
                if(msg.equals("Active")){
                    status_update_variable[0] = msg;
                }
                if(msg.equals("Inactive")){
                    status_update_variable[0] = msg;
                }
            }
        });

        Type_Update_Item_Field.addActionListener(e->{
            if(e.getSource() == Type_Update_Item_Field){
                JComboBox cb = (JComboBox)e.getSource();
                String msg = (String)cb.getSelectedItem();
                if(msg.equals("Sedan")){
                    type_update_variable[0] = msg;
                }
                if(msg.equals("Hatchback")){
                    type_update_variable[0] = msg;
                }
                if(msg.equals("Sport")){
                    type_update_variable[0] = msg;
                }
                if(msg.equals("Luxury")){
                    type_update_variable[0] = msg;
                }
                if(msg.equals("Other")){
                    type_update_variable[0] = msg;
                }
                else{

                }
            }
        });

        Method_Update_Item_Field.addActionListener(e->{
            if(e.getSource() == Method_Update_Item_Field){
                JComboBox cb = (JComboBox)e.getSource();
                String msg = (String)cb.getSelectedItem();
                if(msg.equals("New")){
                    method_update_variable[0] = msg;
                }
                if(msg.equals("Certified Pre-Owned")){
                    method_update_variable[0] = msg;
                }
                if(msg.equals("Used")){
                    method_update_variable[0] = msg;
                }
                if(msg.equals("Other")){
                    method_update_variable[0] = msg;
                }
                else{

                }
            }
        });

        First_Name_Update_Item_Field.setVisible(false);
        Last_Name_Update_Item_Field.setVisible(false);
        Address_Update_Item_Field.setVisible(false);
        Phone_Update_Item_Field.setVisible(false);
        Income_Update_Item_Field.setVisible(false);
        Use_Update_Item_Field.setVisible(false);
        Status_Update_Item_Field.setVisible(false);
        Model_Update_Item_Field.setVisible(false);
        Year_Update_Item_Field.setVisible(false);
        Type_Update_Item_Field.setVisible(false);
        Method_Update_Item_Field.setVisible(false);

        if(Updated_Field_Choice_Label.getText().equals("First Name")){
            First_Name_Update_Item_Field.setVisible(true);
            Last_Name_Update_Item_Field.setVisible(false);
            Address_Update_Item_Field.setVisible(false);
            Phone_Update_Item_Field.setVisible(false);
            Income_Update_Item_Field.setVisible(false);
            Use_Update_Item_Field.setVisible(false);
            Status_Update_Item_Field.setVisible(false);
            Model_Update_Item_Field.setVisible(false);
            Year_Update_Item_Field.setVisible(false);
            Type_Update_Item_Field.setVisible(false);
            Method_Update_Item_Field.setVisible(false);
        }
        if(Updated_Field_Choice_Label.getText().equals("Last Name")){
            First_Name_Update_Item_Field.setVisible(false);
            Last_Name_Update_Item_Field.setVisible(true);
            Address_Update_Item_Field.setVisible(false);
            Phone_Update_Item_Field.setVisible(false);
            Income_Update_Item_Field.setVisible(false);
            Use_Update_Item_Field.setVisible(false);
            Status_Update_Item_Field.setVisible(false);
            Model_Update_Item_Field.setVisible(false);
            Year_Update_Item_Field.setVisible(false);
            Type_Update_Item_Field.setVisible(false);
            Method_Update_Item_Field.setVisible(false);
        }
        if(Updated_Field_Choice_Label.getText().equals("Address")){
            First_Name_Update_Item_Field.setVisible(false);
            Last_Name_Update_Item_Field.setVisible(false);
            Address_Update_Item_Field.setVisible(true);
            Phone_Update_Item_Field.setVisible(false);
            Income_Update_Item_Field.setVisible(false);
            Use_Update_Item_Field.setVisible(false);
            Status_Update_Item_Field.setVisible(false);
            Model_Update_Item_Field.setVisible(false);
            Year_Update_Item_Field.setVisible(false);
            Type_Update_Item_Field.setVisible(false);
            Method_Update_Item_Field.setVisible(false);
        }
        if(Updated_Field_Choice_Label.getText().equals("Phone")){
            First_Name_Update_Item_Field.setVisible(false);
            Last_Name_Update_Item_Field.setVisible(false);
            Address_Update_Item_Field.setVisible(false);
            Phone_Update_Item_Field.setVisible(true);
            Income_Update_Item_Field.setVisible(false);
            Use_Update_Item_Field.setVisible(false);
            Status_Update_Item_Field.setVisible(false);
            Model_Update_Item_Field.setVisible(false);
            Year_Update_Item_Field.setVisible(false);
            Type_Update_Item_Field.setVisible(false);
            Method_Update_Item_Field.setVisible(false);
        }
        if(Updated_Field_Choice_Label.getText().equals("Income")){
            First_Name_Update_Item_Field.setVisible(false);
            Last_Name_Update_Item_Field.setVisible(false);
            Address_Update_Item_Field.setVisible(false);
            Phone_Update_Item_Field.setVisible(false);
            Income_Update_Item_Field.setVisible(true);
            Use_Update_Item_Field.setVisible(false);
            Status_Update_Item_Field.setVisible(false);
            Model_Update_Item_Field.setVisible(false);
            Year_Update_Item_Field.setVisible(false);
            Type_Update_Item_Field.setVisible(false);
            Method_Update_Item_Field.setVisible(false);
        }
        if(Updated_Field_Choice_Label.getText().equals("Use")){
            First_Name_Update_Item_Field.setVisible(false);
            Last_Name_Update_Item_Field.setVisible(false);
            Address_Update_Item_Field.setVisible(false);
            Phone_Update_Item_Field.setVisible(false);
            Income_Update_Item_Field.setVisible(false);
            Use_Update_Item_Field.setVisible(true);
            Status_Update_Item_Field.setVisible(false);
            Model_Update_Item_Field.setVisible(false);
            Year_Update_Item_Field.setVisible(false);
            Type_Update_Item_Field.setVisible(false);
            Method_Update_Item_Field.setVisible(false);
        }
        if(Updated_Field_Choice_Label.getText().equals("Status")){
            First_Name_Update_Item_Field.setVisible(false);
            Last_Name_Update_Item_Field.setVisible(false);
            Address_Update_Item_Field.setVisible(false);
            Phone_Update_Item_Field.setVisible(false);
            Income_Update_Item_Field.setVisible(false);
            Use_Update_Item_Field.setVisible(false);
            Status_Update_Item_Field.setVisible(true);
            Model_Update_Item_Field.setVisible(false);
            Year_Update_Item_Field.setVisible(false);
            Type_Update_Item_Field.setVisible(false);
            Method_Update_Item_Field.setVisible(false);
        }
        if(Updated_Field_Choice_Label.getText().equals("Model")){
            First_Name_Update_Item_Field.setVisible(false);
            Last_Name_Update_Item_Field.setVisible(false);
            Address_Update_Item_Field.setVisible(false);
            Phone_Update_Item_Field.setVisible(false);
            Income_Update_Item_Field.setVisible(false);
            Use_Update_Item_Field.setVisible(false);
            Status_Update_Item_Field.setVisible(false);
            Model_Update_Item_Field.setVisible(true);
            Year_Update_Item_Field.setVisible(false);
            Type_Update_Item_Field.setVisible(false);
            Method_Update_Item_Field.setVisible(false);
        }
        if(Updated_Field_Choice_Label.getText().equals("Year")){
            First_Name_Update_Item_Field.setVisible(false);
            Last_Name_Update_Item_Field.setVisible(false);
            Address_Update_Item_Field.setVisible(false);
            Phone_Update_Item_Field.setVisible(false);
            Income_Update_Item_Field.setVisible(false);
            Use_Update_Item_Field.setVisible(false);
            Status_Update_Item_Field.setVisible(false);
            Model_Update_Item_Field.setVisible(false);
            Year_Update_Item_Field.setVisible(true);
            Type_Update_Item_Field.setVisible(false);
            Method_Update_Item_Field.setVisible(false);
        }
        if(Updated_Field_Choice_Label.getText().equals("Type")){
            First_Name_Update_Item_Field.setVisible(false);
            Last_Name_Update_Item_Field.setVisible(false);
            Address_Update_Item_Field.setVisible(false);
            Phone_Update_Item_Field.setVisible(false);
            Income_Update_Item_Field.setVisible(false);
            Use_Update_Item_Field.setVisible(false);
            Status_Update_Item_Field.setVisible(false);
            Model_Update_Item_Field.setVisible(false);
            Year_Update_Item_Field.setVisible(false);
            Type_Update_Item_Field.setVisible(true);
            Method_Update_Item_Field.setVisible(false);
        }
        if(Updated_Field_Choice_Label.getText().equals("Method")){
            First_Name_Update_Item_Field.setVisible(false);
            Last_Name_Update_Item_Field.setVisible(false);
            Address_Update_Item_Field.setVisible(false);
            Phone_Update_Item_Field.setVisible(false);
            Income_Update_Item_Field.setVisible(false);
            Use_Update_Item_Field.setVisible(false);
            Status_Update_Item_Field.setVisible(false);
            Model_Update_Item_Field.setVisible(false);
            Year_Update_Item_Field.setVisible(false);
            Type_Update_Item_Field.setVisible(false);
            Method_Update_Item_Field.setVisible(true);
        }

        JButton Submit_Update_Item = new JButton("Submit");
        Submit_Update_Item.addActionListener(e->{
            String adminID = Update_Item_Admin_Label.getText();
            String lastname = Update_Item_Last_Label.getText();
            tempcustprof = database.findProfile(adminID, lastname);

            if(Updated_Field_Choice_Label.getText().equals("First Name")){
                String temp_first_name = First_Name_Update_Item_Field.getText();
                tempcustprof.updateFirstName(temp_first_name);
            }
            if(Updated_Field_Choice_Label.getText().equals("Last Name")){
                String temp_last_name = Last_Name_Update_Item_Field.getText();
                tempcustprof.updateLastName(temp_last_name);
            }
            if(Updated_Field_Choice_Label.getText().equals("Address")){
                String temp_address = Address_Update_Item_Field.getText();
                tempcustprof.updateAddress(temp_address);
            }
            if(Updated_Field_Choice_Label.getText().equals("Phone")){
                String temp_phone = Phone_Update_Item_Field.getText();
                tempcustprof.updatePhone(temp_phone);
            }
            if(Updated_Field_Choice_Label.getText().equals("Income")){
                float temp_income = Float.parseFloat(Income_Update_Item_Field.getText());
                tempcustprof.updateIncome(temp_income);
            }
            if(Updated_Field_Choice_Label.getText().equals("Use")){
                String temp_use = use_update_variable[0];
                /// Realized while making all of this that it was told that we did not need to edit this use
            }
            if(Updated_Field_Choice_Label.getText().equals("Status")){
                String temp_status = status_update_variable[0];
                //System.out.print(temp_status);
                tempcustprof.updateStatus(temp_status);
            }
            if(Updated_Field_Choice_Label.getText().equals("Vehicle: Model")){
                String temp_model = Model_Update_Item_Field.getText();
                tempcustprof.getVehicleInfo().updateModel(temp_model);
            }
            if(Updated_Field_Choice_Label.getText().equals("Vehicle: Year")){
                String temp_year = Year_Update_Item_Field.getText();
                tempcustprof.getVehicleInfo().updateYear(temp_year);
            }
            if(Updated_Field_Choice_Label.getText().equals("Vehicle: Type")){
                String temp_type = type_update_variable[0];
                tempcustprof.getVehicleInfo().updateType(temp_type);
            }
            if(Updated_Field_Choice_Label.getText().equals("Vehicle: Method")){
                String temp_method = method_update_variable[0];
                tempcustprof.getVehicleInfo().updateMethod(temp_method);
            }

            try {
                database.writeAllCustomerProf();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            cardlayout.show(mainpanel, "Update_Profile");

        });




        JButton Return_To_Menu5 = new JButton("Return to Menu");
        Return_To_Menu5.addActionListener(e->{
            cardlayout.show(mainpanel, "Main_Menu");
        });

        Updated_Field_Choice_Label.setFont(new Font("Serif", Font.PLAIN, 24));
        /// Finally everything gets added into the page
        Update_Item_Page.add(Update_Item_Title);
        Update_Item_Page.add(Update_Item_Admin_ID_Label);
        Update_Item_Page.add(Update_Item_Admin_Label);
        Update_Item_Page.add(Update_Item_Last_Name_Label);
        Update_Item_Page.add(Update_Item_Last_Label);
        Update_Item_Page.add(Updated_Field_Choice_Label);
        Update_Item_Page.add(First_Name_Update_Item_Field);
        Update_Item_Page.add(Last_Name_Update_Item_Field);
        Update_Item_Page.add(Address_Update_Item_Field);
        Update_Item_Page.add(Phone_Update_Item_Field);
        Update_Item_Page.add(Income_Update_Item_Field);
        //Update_Item_Page.add(Use_Update_Item_Field);
        Update_Item_Page.add(Status_Update_Item_Field);
        Update_Item_Page.add(Model_Update_Item_Field);
        Update_Item_Page.add(Year_Update_Item_Field);
        Update_Item_Page.add(Type_Update_Item_Field);
        Update_Item_Page.add(Method_Update_Item_Field);
        Update_Item_Page.add(Submit_Update_Item);
        Update_Item_Page.add(Return_To_Menu5);



///////////////// Update Profile Menu Setup //////////////////////////////////////////////////////////////////////////////////////////////////////////

        Update_Profile = new JPanel();
        Update_Profile.setBackground(new Color(42, 222, 106));

        /// This string is to see what we choose in the menu
        final String[] Updated_Selection = new String[1];

        /// Setup for the panel
        JLabel Update_Profile_Title = new JLabel("Update Profile");
        Update_Profile_Title.setFont(new Font("Serif", Font.PLAIN, 26));

        JTextField Update_Profile_AdminID_Field = new JTextField(10);
        JTextField Update_Profile_Last_Field = new JTextField(10);

        JLabel AdminID_Label2 = new JLabel("AdminID: ");
        JLabel Last_Name_Label2 = new JLabel("Last Name: ");
        JLabel Update_Combo_Field_Label = new JLabel("Update Field: ");

        JComboBox Update_Profile_Combo_Field = new JComboBox();
        Update_Profile_Combo_Field.addItem("Please Choose an Option");
        Update_Profile_Combo_Field.addItem("First Name");
        Update_Profile_Combo_Field.addItem("Last Name");
        Update_Profile_Combo_Field.addItem("Address");
        Update_Profile_Combo_Field.addItem("Phone");
        Update_Profile_Combo_Field.addItem("Income");
        //Update_Profile_Combo_Field.addItem("Use");
        Update_Profile_Combo_Field.addItem("Status");
        Update_Profile_Combo_Field.addItem("Vehicle: Model");
        Update_Profile_Combo_Field.addItem("Vehicle: Year");
        Update_Profile_Combo_Field.addItem("Vehicle: Type");
        Update_Profile_Combo_Field.addItem("Vehicle: Method");

        JLabel Update_Failure = new JLabel("Could Not Find Profile Specified");
        Update_Failure.setVisible(false);
        /// Listens to the combo box you choose from, and then reads what you need, and sends it over to the
        /// other update page in order for you to update it
        JButton Update_Profile_Find_Button = new JButton("Find");
        Update_Profile_Find_Button.addActionListener(e->{
            String Admin_ID = Update_Profile_AdminID_Field.getText();
            String Last_Name = Update_Profile_Last_Field.getText();
            String Update_Selection = (String)Update_Profile_Combo_Field.getSelectedItem();
            Updated_Selection[0] = Update_Selection;
            if(database.findProfile(Admin_ID, Last_Name) != null) {
                Updated_Field_Choice_Label.setText(Update_Selection);
                if (Update_Selection.equals("First Name")){

                    First_Name_Update_Item_Field.setVisible(true);
                    Last_Name_Update_Item_Field.setVisible(false);
                    Address_Update_Item_Field.setVisible(false);
                    Phone_Update_Item_Field.setVisible(false);
                    Income_Update_Item_Field.setVisible(false);
                    Use_Update_Item_Field.setVisible(false);
                    Status_Update_Item_Field.setVisible(false);
                    Model_Update_Item_Field.setVisible(false);
                    Year_Update_Item_Field.setVisible(false);
                    Type_Update_Item_Field.setVisible(false);
                    Method_Update_Item_Field.setVisible(false);

                    Update_Item_Admin_Label.setText(Admin_ID);
                    Update_Item_Last_Label.setText(Last_Name);
                    cardlayout.show(mainpanel, "Update_Item_Page");

                }
                if (Update_Selection.equals("Last Name")){

                    First_Name_Update_Item_Field.setVisible(false);
                    Last_Name_Update_Item_Field.setVisible(true);
                    Address_Update_Item_Field.setVisible(false);
                    Phone_Update_Item_Field.setVisible(false);
                    Income_Update_Item_Field.setVisible(false);
                    Use_Update_Item_Field.setVisible(false);
                    Status_Update_Item_Field.setVisible(false);
                    Model_Update_Item_Field.setVisible(false);
                    Year_Update_Item_Field.setVisible(false);
                    Type_Update_Item_Field.setVisible(false);
                    Method_Update_Item_Field.setVisible(false);

                    Update_Item_Admin_Label.setText(Admin_ID);
                    Update_Item_Last_Label.setText(Last_Name);
                    cardlayout.show(mainpanel, "Update_Item_Page");

                }
                if (Update_Selection.equals("Address")) {

                    First_Name_Update_Item_Field.setVisible(false);
                    Last_Name_Update_Item_Field.setVisible(false);
                    Address_Update_Item_Field.setVisible(true);
                    Phone_Update_Item_Field.setVisible(false);
                    Income_Update_Item_Field.setVisible(false);
                    Use_Update_Item_Field.setVisible(false);
                    Status_Update_Item_Field.setVisible(false);
                    Model_Update_Item_Field.setVisible(false);
                    Year_Update_Item_Field.setVisible(false);
                    Type_Update_Item_Field.setVisible(false);
                    Method_Update_Item_Field.setVisible(false);

                    Update_Item_Admin_Label.setText(Admin_ID);
                    Update_Item_Last_Label.setText(Last_Name);
                    cardlayout.show(mainpanel, "Update_Item_Page");

                }
                if (Update_Selection.equals("Phone")) {

                    First_Name_Update_Item_Field.setVisible(false);
                    Last_Name_Update_Item_Field.setVisible(false);
                    Address_Update_Item_Field.setVisible(false);
                    Phone_Update_Item_Field.setVisible(true);
                    Income_Update_Item_Field.setVisible(false);
                    Use_Update_Item_Field.setVisible(false);
                    Status_Update_Item_Field.setVisible(false);
                    Model_Update_Item_Field.setVisible(false);
                    Year_Update_Item_Field.setVisible(false);
                    Type_Update_Item_Field.setVisible(false);
                    Method_Update_Item_Field.setVisible(false);

                    Update_Item_Admin_Label.setText(Admin_ID);
                    Update_Item_Last_Label.setText(Last_Name);
                    cardlayout.show(mainpanel, "Update_Item_Page");

                }
                if (Update_Selection.equals("Income")){

                    First_Name_Update_Item_Field.setVisible(false);
                    Last_Name_Update_Item_Field.setVisible(false);
                    Address_Update_Item_Field.setVisible(false);
                    Phone_Update_Item_Field.setVisible(false);
                    Income_Update_Item_Field.setVisible(true);
                    Use_Update_Item_Field.setVisible(false);
                    Status_Update_Item_Field.setVisible(false);
                    Model_Update_Item_Field.setVisible(false);
                    Year_Update_Item_Field.setVisible(false);
                    Type_Update_Item_Field.setVisible(false);
                    Method_Update_Item_Field.setVisible(false);

                    Update_Item_Admin_Label.setText(Admin_ID);
                    Update_Item_Last_Label.setText(Last_Name);
                    cardlayout.show(mainpanel, "Update_Item_Page");

                }
                if (Update_Selection.equals("Use")){

                    First_Name_Update_Item_Field.setVisible(false);
                    Last_Name_Update_Item_Field.setVisible(false);
                    Address_Update_Item_Field.setVisible(false);
                    Phone_Update_Item_Field.setVisible(false);
                    Income_Update_Item_Field.setVisible(false);
                    Use_Update_Item_Field.setVisible(true);
                    Status_Update_Item_Field.setVisible(false);
                    Model_Update_Item_Field.setVisible(false);
                    Year_Update_Item_Field.setVisible(false);
                    Type_Update_Item_Field.setVisible(false);
                    Method_Update_Item_Field.setVisible(false);

                    Update_Item_Admin_Label.setText(Admin_ID);
                    Update_Item_Last_Label.setText(Last_Name);
                    cardlayout.show(mainpanel, "Update_Item_Page");

                }
                if (Update_Selection.equals("Status")){

                    First_Name_Update_Item_Field.setVisible(false);
                    Last_Name_Update_Item_Field.setVisible(false);
                    Address_Update_Item_Field.setVisible(false);
                    Phone_Update_Item_Field.setVisible(false);
                    Income_Update_Item_Field.setVisible(false);
                    Use_Update_Item_Field.setVisible(false);
                    Status_Update_Item_Field.setVisible(true);
                    Model_Update_Item_Field.setVisible(false);
                    Year_Update_Item_Field.setVisible(false);
                    Type_Update_Item_Field.setVisible(false);
                    Method_Update_Item_Field.setVisible(false);

                    Update_Item_Admin_Label.setText(Admin_ID);
                    Update_Item_Last_Label.setText(Last_Name);
                    cardlayout.show(mainpanel, "Update_Item_Page");

                }
                if (Update_Selection.equals("Vehicle: Model")) {

                    First_Name_Update_Item_Field.setVisible(false);
                    Last_Name_Update_Item_Field.setVisible(false);
                    Address_Update_Item_Field.setVisible(false);
                    Phone_Update_Item_Field.setVisible(false);
                    Income_Update_Item_Field.setVisible(false);
                    Use_Update_Item_Field.setVisible(false);
                    Status_Update_Item_Field.setVisible(false);
                    Model_Update_Item_Field.setVisible(true);
                    Year_Update_Item_Field.setVisible(false);
                    Type_Update_Item_Field.setVisible(false);
                    Method_Update_Item_Field.setVisible(false);

                    Update_Item_Admin_Label.setText(Admin_ID);
                    Update_Item_Last_Label.setText(Last_Name);
                    cardlayout.show(mainpanel, "Update_Item_Page");

                }
                if (Update_Selection.equals("Vehicle: Year")) {

                    First_Name_Update_Item_Field.setVisible(false);
                    Last_Name_Update_Item_Field.setVisible(false);
                    Address_Update_Item_Field.setVisible(false);
                    Phone_Update_Item_Field.setVisible(false);
                    Income_Update_Item_Field.setVisible(false);
                    Use_Update_Item_Field.setVisible(false);
                    Status_Update_Item_Field.setVisible(false);
                    Model_Update_Item_Field.setVisible(false);
                    Year_Update_Item_Field.setVisible(true);
                    Type_Update_Item_Field.setVisible(false);
                    Method_Update_Item_Field.setVisible(false);

                    Update_Item_Admin_Label.setText(Admin_ID);
                    Update_Item_Last_Label.setText(Last_Name);
                    cardlayout.show(mainpanel, "Update_Item_Page");

                }
                if (Update_Selection.equals("Vehicle: Type")) {

                    First_Name_Update_Item_Field.setVisible(false);
                    Last_Name_Update_Item_Field.setVisible(false);
                    Address_Update_Item_Field.setVisible(false);
                    Phone_Update_Item_Field.setVisible(false);
                    Income_Update_Item_Field.setVisible(false);
                    Use_Update_Item_Field.setVisible(false);
                    Status_Update_Item_Field.setVisible(false);
                    Model_Update_Item_Field.setVisible(false);
                    Year_Update_Item_Field.setVisible(false);
                    Type_Update_Item_Field.setVisible(true);
                    Method_Update_Item_Field.setVisible(false);

                    Update_Item_Admin_Label.setText(Admin_ID);
                    Update_Item_Last_Label.setText(Last_Name);
                    cardlayout.show(mainpanel, "Update_Item_Page");

                }
                if (Update_Selection.equals("Vehicle: Method")) {

                    First_Name_Update_Item_Field.setVisible(false);
                    Last_Name_Update_Item_Field.setVisible(false);
                    Address_Update_Item_Field.setVisible(false);
                    Phone_Update_Item_Field.setVisible(false);
                    Income_Update_Item_Field.setVisible(false);
                    Use_Update_Item_Field.setVisible(false);
                    Status_Update_Item_Field.setVisible(false);
                    Model_Update_Item_Field.setVisible(false);
                    Year_Update_Item_Field.setVisible(false);
                    Type_Update_Item_Field.setVisible(false);
                    Method_Update_Item_Field.setVisible(true);

                    Update_Item_Admin_Label.setText(Admin_ID);
                    Update_Item_Last_Label.setText(Last_Name);
                    cardlayout.show(mainpanel, "Update_Item_Page");

                }

                else{
                    Update_Failure.setVisible(true);
                }

            }
            else{
                /// if anything goes bad, this will show ^^ and below
                Update_Item_Admin_Label.setText(Admin_ID);
                Update_Item_Last_Label.setText(Last_Name);
                Update_Failure.setVisible(true);
            }
        });

        JButton Return_To_Menu4 = new JButton("Return to Menu");
        Return_To_Menu4.addActionListener(e->{
            cardlayout.show(mainpanel, "Main_Menu");
        });

        Update_Profile.add(Update_Profile_Title);
        Update_Profile.add(AdminID_Label2);
        Update_Profile.add(Update_Profile_AdminID_Field);
        Update_Profile.add(Last_Name_Label2);
        Update_Profile.add(Update_Profile_Last_Field);
        Update_Profile.add(Update_Combo_Field_Label);
        Update_Profile.add(Update_Profile_Combo_Field);
        Update_Profile.add(Update_Profile_Find_Button);
        Update_Profile.add(Update_Failure);
        Update_Profile.add(Return_To_Menu4);

/////////////// Found Profile Page Setup //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Found_Profile_Page = new JPanel();
        Found_Profile_Page.setBackground(new Color(42, 222, 106));
        //Found_Profile_Page.setLayout(new GridLayout(0, 1));


        JLabel Found_Customer_Profile_Title = new JLabel("  Customer Profile  ");
        Found_Customer_Profile_Title.setFont(new Font("Serif", Font.PLAIN, 26));


        /// Sets up the entire panel, and resizes it
        JLabel AdminID_Label4 = new JLabel("AdminID: ");
        AdminID_Label4.setFont(new Font("Serif", Font.PLAIN, 22));
        JLabel First_Name_Label4 = new JLabel("First Name: ");
        First_Name_Label4.setFont(new Font("Serif", Font.PLAIN, 22));
        JLabel Last_Name_Label4 = new JLabel("Last Name: ");
        Last_Name_Label4.setFont(new Font("Serif", Font.PLAIN, 22));
        JLabel Address_Label4 = new JLabel("Address: ");
        Address_Label4.setFont(new Font("Serif", Font.PLAIN, 18));
        JLabel Phone_Label4 = new JLabel("Phone: ");
        Phone_Label4.setFont(new Font("Serif", Font.PLAIN, 20));
        JLabel Income_Label4 = new JLabel("Income: ");
        Income_Label4.setFont(new Font("Serif", Font.PLAIN, 22));
        JLabel Use_Menu_Label4 = new JLabel("        Use: ");
        Use_Menu_Label4.setFont(new Font("Serif", Font.PLAIN, 22));
        JLabel Status_Menu_Label4 = new JLabel("   Status:  ");
        Status_Menu_Label4.setFont(new Font("Serif", Font.PLAIN, 22));
        JLabel Model_Label4 = new JLabel("  Model: ");
        Model_Label4.setFont(new Font("Serif", Font.PLAIN, 22));
        JLabel Year_Label4 = new JLabel("    Year:   ");
        Year_Label4.setFont(new Font("Serif", Font.PLAIN, 22));
        JLabel Type_Menu_Label4 = new JLabel("     Type:   ");
        Type_Menu_Label4.setFont(new Font("Serif", Font.PLAIN, 22));
        JLabel Method_Menu_Label4 = new JLabel("  Method:  ");
        Method_Menu_Label4.setFont(new Font("Serif", Font.PLAIN, 22));

        /// setup
        JLabel Found_AdminID = new JLabel();
        JLabel Found_First_Name = new JLabel();
        JLabel Found_Last_Name = new JLabel();
        JLabel Found_Address = new JLabel();
        JLabel Found_Phone = new JLabel();
        JLabel Found_Income = new JLabel();
        JLabel Found_Use = new JLabel();
        JLabel Found_Status = new JLabel();
        JLabel Found_Model = new JLabel();
        JLabel Found_Year = new JLabel();
        JLabel Found_Type = new JLabel();
        JLabel Found_Method = new JLabel();

        JButton Found_Return_To_Find = new JButton("OK");
        Found_Return_To_Find.addActionListener(e->{
            cardlayout.show(mainpanel, "Find_Profile");
        });

        JButton Return_To_Menu7 = new JButton("Return to Menu");
        Return_To_Menu7.addActionListener(e->{
            cardlayout.show(mainpanel, "Main_Menu");
        });

        Found_Profile_Page.add(Found_Customer_Profile_Title);

        Found_Profile_Page.add(AdminID_Label4);
        Found_Profile_Page.add(Found_AdminID);

        Found_Profile_Page.add(First_Name_Label4);
        Found_Profile_Page.add(Found_First_Name);

        Found_Profile_Page.add(Last_Name_Label4);
        Found_Profile_Page.add(Found_Last_Name);

        Found_Profile_Page.add(Address_Label4);
        Found_Profile_Page.add(Found_Address);

        Found_Profile_Page.add(Phone_Label4);
        Found_Profile_Page.add(Found_Phone);

        Found_Profile_Page.add(Income_Label4);
        Found_Profile_Page.add(Found_Income);

        Found_Profile_Page.add(Use_Menu_Label4);
        Found_Profile_Page.add(Found_Use);

        Found_Profile_Page.add(Status_Menu_Label4);
        Found_Profile_Page.add(Found_Status);

        Found_Profile_Page.add(Model_Label4);
        Found_Profile_Page.add(Found_Model);

        Found_Profile_Page.add(Year_Label4);
        Found_Profile_Page.add(Found_Year);

        Found_Profile_Page.add(Type_Menu_Label4);
        Found_Profile_Page.add(Found_Type);

        Found_Profile_Page.add(Method_Menu_Label4);
        Found_Profile_Page.add(Found_Method);

        Found_Profile_Page.add(Found_Return_To_Find);
        Found_Profile_Page.add(Return_To_Menu7);

///////////////// Find / Display Menu Setup ///////////////////////////////////////////////////////////////////////////////////////////////////////////

        Find_Profile = new JPanel();
        Find_Profile.setBackground(new Color(42, 222, 106));

        JLabel Find_Profile_Title = new JLabel("Find Profile    ");
        Find_Profile_Title.setFont(new Font("Serif", Font.PLAIN, 26));

        JLabel Find_Profile_Spacer = new JLabel();
        JTextField Find_Profile_AdminID_Field = new JTextField(10);
        JTextField Find_Profile_Last_Field = new JTextField(10);

        JButton Display_Profile_Button = new JButton("Find Profile");

        JLabel AdminID_Label3 = new JLabel("AdminID: ");
        JLabel Last_Name_Label3 = new JLabel("Last Name: ");

        JLabel Display_Profile_Failure = new JLabel("Could Not Find Profile"); /// Failure check label
        Display_Profile_Failure.setVisible(false);

        JButton Return_To_Menu6 = new JButton("Return to Menu");
        Return_To_Menu6.addActionListener(e->{
            cardlayout.show(mainpanel, "Main_Menu");
        });

        Display_Profile_Button.addActionListener(e->{
            String Admin_ID = Find_Profile_AdminID_Field.getText();
            String Last_Name = Find_Profile_Last_Field.getText();
            Display_Profile_Failure.setVisible(false);
            try{
                if(!((database.findProfile(Admin_ID, Last_Name)) == null)) { /// checks if it can even find a profile
                    tempcustprof = database.findProfile(Admin_ID, Last_Name);
                    Found_AdminID.setText(tempcustprof.getAdminID()); /// sends over all data to other slide in order for
                    Found_First_Name.setText(tempcustprof.getFirstName()); /// it to have the profile
                    Found_Last_Name.setText(tempcustprof.getLastName());
                    Found_Address.setText(tempcustprof.getAddress());
                    Found_Phone.setText(tempcustprof.getPhone());
                    Found_Income.setText(Float.toString(tempcustprof.getIncome()));
                    Found_Use.setText(tempcustprof.getUse());
                    Found_Status.setText(tempcustprof.getStatus());
                    Found_Model.setText(tempcustprof.getVehicleInfo().getModel());
                    Found_Year.setText(tempcustprof.getVehicleInfo().getYear());
                    Found_Type.setText(tempcustprof.getVehicleInfo().getType());
                    Found_Method.setText(tempcustprof.getVehicleInfo().getMethod());

                    cardlayout.show(mainpanel, "Found_Profile_Page");
                }else{
                    Display_Profile_Failure.setVisible(true); /// can't find file
                }
            }
            catch(Error error){
                Display_Profile_Failure.setVisible(true);
            }

        });

        Find_Profile.add(Find_Profile_Title);
        Find_Profile.add(Find_Profile_Spacer);
        Find_Profile.add(AdminID_Label3);
        Find_Profile.add(Find_Profile_AdminID_Field);
        Find_Profile.add(Last_Name_Label3);
        Find_Profile.add(Find_Profile_Last_Field);
        Find_Profile.add(Display_Profile_Button);
        Find_Profile.add(Display_Profile_Failure);
        Find_Profile.add(Return_To_Menu6);

//////////////// Displayed Profiles Page Setup ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Display_All_Profiles_Page = new JPanel();
        Display_All_Profiles_Page.setBackground(new Color(42, 222, 106));

        JLabel Display_All_Profiles_Page_Title = new JLabel("Customer Profile");
        Display_All_Profiles_Page_Title.setFont(new Font("Serif", Font.PLAIN, 26));

        /// setup, this stuff has really gotten long and boring hasn't it? I think it has
        JLabel AdminID_Label5 = new JLabel("AdminID: ");
        AdminID_Label5.setFont(new Font("Serif", Font.PLAIN, 22));
        JLabel First_Name_Label5 = new JLabel("First Name: ");
        First_Name_Label5.setFont(new Font("Serif", Font.PLAIN, 22));
        JLabel Last_Name_Label5 = new JLabel("Last Name: ");
        Last_Name_Label5.setFont(new Font("Serif", Font.PLAIN, 22));
        JLabel Address_Label5 = new JLabel("Address: ");
        Address_Label5.setFont(new Font("Serif", Font.PLAIN, 18));
        JLabel Phone_Label5 = new JLabel("Phone: ");
        Phone_Label5.setFont(new Font("Serif", Font.PLAIN, 20));
        JLabel Income_Label5 = new JLabel("Income: ");
        Income_Label5.setFont(new Font("Serif", Font.PLAIN, 22));
        JLabel Use_Menu_Label5 = new JLabel("     Use:   ");
        Use_Menu_Label5.setFont(new Font("Serif", Font.PLAIN, 22));
        JLabel Status_Menu_Label5 = new JLabel("   Status:   ");
        Status_Menu_Label5.setFont(new Font("Serif", Font.PLAIN, 22));
        JLabel Model_Label5 = new JLabel("   Model:  ");
        Model_Label5.setFont(new Font("Serif", Font.PLAIN, 22));
        JLabel Year_Label5 = new JLabel("    Year:   ");
        Year_Label5.setFont(new Font("Serif", Font.PLAIN, 22));
        JLabel Type_Menu_Label5 = new JLabel("    Type:    ");
        Type_Menu_Label5.setFont(new Font("Serif", Font.PLAIN, 22));
        JLabel Method_Menu_Label5 = new JLabel(" Method: ");
        Method_Menu_Label5.setFont(new Font("Serif", Font.PLAIN, 22));

        JLabel Found_AdminID_Display = new JLabel();
        JLabel Found_First_Name_Display = new JLabel();
        JLabel Found_Last_Name_Display = new JLabel();
        JLabel Found_Address_Display = new JLabel();
        JLabel Found_Phone_Display = new JLabel();
        JLabel Found_Income_Display = new JLabel();
        JLabel Found_Use_Display = new JLabel();
        JLabel Found_Status_Display = new JLabel();
        JLabel Found_Model_Display = new JLabel();
        JLabel Found_Year_Display = new JLabel();
        JLabel Found_Type_Display = new JLabel();
        JLabel Found_Method_Display = new JLabel();

        final String[] admin_ID_transfer = new String[1];

        JButton swap = new JButton("Next Profile");
        swap.addActionListener(e->{
            String adminID_check = admin_ID_transfer[0];
            String last_name_check;
            /// Okay so this part I thought was cool, so between this and the Display All Menu Page, this
            /// was the most challenging part. So this reads the database file, and takes each line and
            /// inputs it with your ADMINID, to see if they match. Then if they do it sends it over.
            /// This part actually isn't the start, rather then back half because it needs to keep reading from
            /// that file in order to get all profiles under that Admin_ID. This is also because we didn't
            /// properly create it before and I think it's kinda cool.
            while(Reader.hasNextLine()){
                last_name_check = Reader.nextLine();
                System.out.println(last_name_check);
                if(!((database.findProfile(adminID_check, last_name_check)) == null)){
                    tempcustprof = database.findProfile(adminID_check, last_name_check);
                    Found_AdminID_Display.setText(adminID_check);
                    Found_First_Name_Display.setText(tempcustprof.getFirstName());
                    Found_Last_Name_Display.setText(tempcustprof.getLastName());
                    Found_Address_Display.setText(tempcustprof.getAddress());
                    Found_Phone_Display.setText(tempcustprof.getPhone());
                    Found_Income_Display.setText(Float.toString(tempcustprof.getIncome()));
                    Found_Use_Display.setText(tempcustprof.getUse());
                    Found_Status_Display.setText(tempcustprof.getStatus());
                    Found_Model_Display.setText(tempcustprof.getVehicleInfo().getModel());
                    Found_Year_Display.setText(tempcustprof.getVehicleInfo().getYear());
                    Found_Type_Display.setText(tempcustprof.getVehicleInfo().getType());
                    Found_Method_Display.setText(tempcustprof.getVehicleInfo().getMethod());
                    break;
                }

            }
            cardlayout.show(mainpanel, "Display_All_Profiles_Page");
        });

        JButton Return_To_Menu9 = new JButton("Return to Menu");
        Return_To_Menu9.addActionListener(e->{
            cardlayout.show(mainpanel, "Main_Menu");
        });

        Display_All_Profiles_Page.add(Display_All_Profiles_Page_Title);

        Display_All_Profiles_Page.add(AdminID_Label5);
        Display_All_Profiles_Page.add(Found_AdminID_Display);

        Display_All_Profiles_Page.add(First_Name_Label5);
        Display_All_Profiles_Page.add(Found_First_Name_Display);

        Display_All_Profiles_Page.add(Last_Name_Label5);
        Display_All_Profiles_Page.add(Found_Last_Name_Display);

        Display_All_Profiles_Page.add(Address_Label5);
        Display_All_Profiles_Page.add(Found_Address_Display);

        Display_All_Profiles_Page.add(Phone_Label5);
        Display_All_Profiles_Page.add(Found_Phone_Display);

        Display_All_Profiles_Page.add(Income_Label5);
        Display_All_Profiles_Page.add(Found_Income_Display);

        Display_All_Profiles_Page.add(Use_Menu_Label5);
        Display_All_Profiles_Page.add(Found_Use_Display);

        Display_All_Profiles_Page.add(Status_Menu_Label5);
        Display_All_Profiles_Page.add(Found_Status_Display);

        Display_All_Profiles_Page.add(Model_Label5);
        Display_All_Profiles_Page.add(Found_Model_Display);

        Display_All_Profiles_Page.add(Year_Label5);
        Display_All_Profiles_Page.add(Found_Year_Display);

        Display_All_Profiles_Page.add(Type_Menu_Label5);
        Display_All_Profiles_Page.add(Found_Type_Display);

        Display_All_Profiles_Page.add(Method_Menu_Label5);
        Display_All_Profiles_Page.add(Found_Method_Display);

        Display_All_Profiles_Page.add(swap);
        Display_All_Profiles_Page.add(Return_To_Menu9);

///////////////// Display All Menu Setup /////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Display_All_Profiles = new JPanel();
        Display_All_Profiles.setBackground(new Color(42, 222, 106));

        JLabel Display_All_Profiles_Title = new JLabel("Display All Profiles");
        Display_All_Profiles_Title.setFont(new Font("Serif", Font.PLAIN, 26));

        JLabel Display_All_Profiles_Spacer = new JLabel();
        JTextField Display_All_Profiles_AdminID_Field = new JTextField(10);

        JButton Display_All_Profiles_Button = new JButton("Find Profiles");

        JLabel Display_All_AdminID_Label = new JLabel("AdminID: ");

        JButton Return_To_Menu8 = new JButton("Return to Menu");
        Return_To_Menu8.addActionListener(e->{
            cardlayout.show(mainpanel, "Main_Menu");
        });
        Display_All_Profiles_Button.addActionListener(e->{
            String Admin_ID = Display_All_Profiles_AdminID_Field.getText();
            admin_ID_transfer[0] = Admin_ID;
            if(database.AdminIDcheck(Admin_ID)){
                File new_File = new File(file_name);
                try {
                    Reader = new Scanner(new_File); /// SEE! this is where it starts, and that it re-reads over our
                    /// database file we are editing now! Sick work!
                    String adminID_check = Admin_ID;
                    String last_name_check;
                    while(Reader.hasNextLine()){
                        last_name_check = Reader.nextLine();
                        System.out.println(last_name_check);
                        if(!((database.findProfile(adminID_check, last_name_check)) == null)){ /// combines them to make sure they work
                            tempcustprof = database.findProfile(adminID_check, last_name_check);
                            Found_AdminID_Display.setText(Admin_ID);
                            Found_First_Name_Display.setText(tempcustprof.getFirstName());
                            Found_Last_Name_Display.setText(tempcustprof.getLastName());
                            Found_Address_Display.setText(tempcustprof.getAddress());
                            Found_Phone_Display.setText(tempcustprof.getPhone());
                            Found_Income_Display.setText(Float.toString(tempcustprof.getIncome()));
                            Found_Use_Display.setText(tempcustprof.getUse());
                            Found_Status_Display.setText(tempcustprof.getStatus());
                            Found_Model_Display.setText(tempcustprof.getVehicleInfo().getModel());
                            Found_Year_Display.setText(tempcustprof.getVehicleInfo().getYear());
                            Found_Type_Display.setText(tempcustprof.getVehicleInfo().getType());
                            Found_Method_Display.setText(tempcustprof.getVehicleInfo().getMethod());
                            break;
                        }
                    }
                    cardlayout.show(mainpanel, "Display_All_Profiles_Page");
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
            cardlayout.show(mainpanel, "Display_All_Profiles_Page");
        });

        Display_All_Profiles.add(Display_All_Profiles_Title);
        Display_All_Profiles.add(Display_All_Profiles_Spacer);
        Display_All_Profiles.add(Display_All_AdminID_Label);
        Display_All_Profiles.add(Display_All_Profiles_AdminID_Field);
        Display_All_Profiles.add(Display_All_Profiles_Button);

        Display_All_Profiles.add(Return_To_Menu8);


//////////////// Success Page Setup //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /// Eh this page is just the Create profile success page, where it sends you to after you successfully create
        /// a new profile
        Success_Page = new JPanel();
        Success_Page.setBackground(new Color(42, 222, 106));
        //Success_Page.setLayout(new GridLayout(0, 1));

        JLabel Success_Title = new JLabel("Create Profile");
        JLabel Success_Label = new JLabel("Success!");

        Success_Title.setFont(new Font("Serif", Font.PLAIN, 28));
        Success_Label.setFont(new Font("Serif", Font.PLAIN, 26));


        JButton Return_To_Create = new JButton("Make a New Profile");
        Return_To_Create.addActionListener(e->{
            cardlayout.show(mainpanel, "Create_Profile");
        });

        JButton Return_To_Menu_Success = new JButton("Return to Menu");
        Return_To_Menu_Success.addActionListener(e->{
            cardlayout.show(mainpanel, "Main_Menu");
        });

        Success_Page.add(Success_Title);
        Success_Page.add(Success_Label);
        Success_Page.add(Return_To_Create);
        Success_Page.add(Return_To_Menu_Success);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




        mainpanel.setLayout(cardlayout); /// so this sets the layout of the big panel that is being used during
        /// this whole thing. The mainpanel basically serves as the "background" and allows you to switch seemlessly
        /// between all panels that you see when you go through the GUI.
        mainpanel.add(Main_Menu, "Main_Menu");
        mainpanel.add(Create_Profile, "Create_Profile");
        mainpanel.add(Delete_Profile, "Delete_Profile");
        mainpanel.add(Update_Profile, "Update_Profile");
        mainpanel.add(Find_Profile, "Find_Profile");
        mainpanel.add(Display_All_Profiles, "Display_All_Profiles");
        mainpanel.add(Success_Page, "Success_Page");
        mainpanel.add(Delete_Success_Page,"Delete_Success_Page");
        mainpanel.add(Update_Item_Page, "Update_Item_Page");
        mainpanel.add(Found_Profile_Page,"Found_Profile_Page");
        mainpanel.add(Display_All_Profiles_Page,"Display_All_Profiles_Page");

        /// Adds the main panel to the frame
        frame.add(mainpanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        CustomerProfGUI test = new CustomerProfGUI();
    }
}


