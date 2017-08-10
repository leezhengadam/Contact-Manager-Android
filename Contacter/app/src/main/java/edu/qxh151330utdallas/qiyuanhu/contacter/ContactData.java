
/**
 *Copyright ©  2017
 *Title: Asg4-xxw151430
 *Description: Android Contact program
 *Auther: Qiyuan Hu, Xuwei Wang
 *NetID: qxh151330, xxw151430
 *Date: 03/25/2017
 *Version: 2.1.1
 */

package edu.qxh151330utdallas.qiyuanhu.contacter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Pattern;

public class ContactData extends AppCompatActivity {


    private Button Add_Button;
    private Button Save_Button;
    private Button Delete_Button;


    private String first_name="";
    private String last_name="";
    private String phone="";
    private String mail="";
    private String birth_date="";

    String getStr;

	//written by Qiyuan Hu
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Receive intent
        Bundle bundle = getIntent().getExtras();

        // When user click on ListView, get all resources and display in layout.
        if(!(bundle.getString("click").equals("")))
        {
            String[] source = bundle.getString("click").split("\t");
            first_name = source[0];
            last_name = source[1];
            phone = source[2];
            mail = source[3];
            birth_date = source[4];

            //Set layout with Save/Delete buttons and functions.
            setContentView(R.layout.contact_edit);
        }
        else
        {
            setContentView(R.layout.contact_add);
        }

		
		//written by Qiyuan Hu
        //Find EditText in layout by ID
        final EditText firstEdit = (EditText)findViewById(R.id.firsttext);
        final EditText lastEdit = (EditText)findViewById(R.id.lasttext);
        final EditText phoneEdit = (EditText)findViewById(R.id.phonetext);
        final EditText emailEdit = (EditText)findViewById(R.id.emailtext);
        final EditText dateEdit = (EditText)findViewById(R.id.datetext);

		
        //Set detail for EditText, default is blank
        firstEdit.setText(first_name);
        lastEdit.setText(last_name);
        phoneEdit.setText(phone);
        emailEdit.setText(mail);
        dateEdit.setText(getDate());

        //Get message from MainActivity
        getStr = bundle.getString("click");

        //AlertDialog for checking First Name
        final AlertDialog.Builder check = new AlertDialog.Builder(this);

		//written by Qiyuan Hu
        if((bundle.getString("click").equals(""))) {

            Add_Button = (Button) findViewById(R.id.add);
            Add_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String firstinput = firstEdit.getText().toString();
                    String dateinput = dateEdit.getText().toString();

                    //Check first name
                    if (firstinput.equals("")) {
                        check.setTitle("Save");
                        check.setMessage("Please Enter First Name");
                        check.setPositiveButton("OK", null);
                        check.show();
                        return;
                    }

                    //Check Date validation
                    if (!DateValid(dateinput)) {
                        check.setTitle("Save");
                        check.setMessage("Please Enter Date Correctly");
                        check.setPositiveButton("OK", null);
                        check.show();
                        return;
                    }

                    //Get new data from EditText
                    String lastinpute = lastEdit.getText().toString();
                    String phoneinput = phoneEdit.getText().toString();
                    String emailinput = emailEdit.getText().toString();

                    String alldata = firstinput + "\t" + lastinpute + "\t" + phoneinput + "\t" + emailinput + "\t" + dateinput;

                    String loadData;
                    File contactfile = new File("record.txt");
                    if (!contactfile.exists()) {
                        try {
                            contactfile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    //Use ArrayList to store all the contacts data
                    ArrayList<String> contactdata = new ArrayList();
                    try {
                        FileInputStream fileinput = openFileInput("record.txt");
                        InputStreamReader fReader = new InputStreamReader(fileinput);
                        BufferedReader bReader = new BufferedReader(fReader);

                        while ((loadData = bReader.readLine()) != null) {
                            contactdata.add(loadData);
                        }

                        fileinput.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Add new contact
                    contactdata.add(alldata);

                    //Sort ArrayList Alphabetically
                    Collections.sort(contactdata);

                    //Write back in record.txt
                    try {
                        FileOutputStream fileoutput = openFileOutput("record.txt", Context.MODE_PRIVATE);
                        int length = contactdata.size();
                        for (int i = 0; i < length; i++) {
                            fileoutput.write(contactdata.get(i).getBytes());
                            fileoutput.write("\n".getBytes());
                        }
                        fileoutput.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Start MainActivity Activity to show all the data
                    startActivity(new Intent(ContactData.this, MainActivity.class));
                }
            });
        }

        if(!(bundle.getString("click").equals(""))) {

            Save_Button = (Button) findViewById(R.id.save);
            Save_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Get the target contact need to modify
                    String olddata = first_name + "\t" + last_name + "\t" + phone + "\t" + mail + "\t" + birth_date;

                    String firstinput = firstEdit.getText().toString();
                    String dateinput = dateEdit.getText().toString();

                    //Check First Name is not blank
                    if (firstinput.equals("")) {
                        check.setTitle("Save");
                        check.setMessage("Please Enter First Name");
                        check.setPositiveButton("OK", null);
                        check.show();
                        return;
                    }

                    //Check Date Validation
                    if (!DateValid(dateinput)) {
                        check.setTitle("Save");
                        check.setMessage("Please Enter Date Correctly");
                        check.setPositiveButton("OK", null);
                        check.show();
                        return;
                    }

                    String lastinpute = lastEdit.getText().toString();
                    String phoneinput = phoneEdit.getText().toString();
                    String emailinput = emailEdit.getText().toString();

                    // Get all new contact details
                    String alldata = firstinput + "\t" + lastinpute + "\t" + phoneinput + "\t" + emailinput + "\t" + dateinput;

                    String loadData;
                    File contactfile = new File("record.txt");
                    if (!contactfile.exists()) {
                        try {
                            contactfile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    ArrayList<String> contactdata = new ArrayList();
                    try {
                        FileInputStream fileinput = openFileInput("record.txt");
                        InputStreamReader fReader = new InputStreamReader(fileinput);
                        BufferedReader bReader = new BufferedReader(fReader);

                        while ((loadData = bReader.readLine()) != null) {

                            if (loadData.equals(olddata))
                                contactdata.add(alldata);
                            else
                                contactdata.add(loadData);

                        }

                        fileinput.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Sort ArrayList Alphabetically
                    Collections.sort(contactdata);

                    //Write back in record.txt
                    try {
                        FileOutputStream fileoutput = openFileOutput("record.txt", Context.MODE_PRIVATE);
                        int length = contactdata.size();
                        for (int i = 0; i < length; i++) {
                            fileoutput.write(contactdata.get(i).getBytes());
                            fileoutput.write("\n".getBytes());
                        }
                        fileoutput.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    startActivity(new Intent(ContactData.this, MainActivity.class));
                }
            });

            Delete_Button = (Button) findViewById(R.id.delete);
            Delete_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String firstinput = firstEdit.getText().toString();
                    String dateinput = dateEdit.getText().toString();

                    //Show dialog to make sure user want to delete
                    check.setTitle("Delete");
                    check.setMessage("Are You Sure?");

                    //User click on Yes.
                    check.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String loadData;

                            File contactfile = new File("record.txt");

                            if (!contactfile.exists()) {
                                try {
                                    contactfile.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            ArrayList<String> contactdata = new ArrayList();
                            try {
                                FileInputStream fileinput = openFileInput("record.txt");
                                InputStreamReader fReader = new InputStreamReader(fileinput);
                                BufferedReader bReader = new BufferedReader(fReader);
                                String olddata = first_name + "\t" + last_name + "\t" + phone + "\t" + mail + "\t" + birth_date;
                                boolean del = true;
                                while ((loadData = bReader.readLine()) != null) {

                                    if (loadData.equals(olddata) && del) {
                                        del = false;
                                    }
                                    else {
                                        contactdata.add(loadData);
                                    }
                                }

                                fileinput.close();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //Sort ArrayList Alphabetically
                            Collections.sort(contactdata);

                            //Write back in record.txt
                            try {
                                FileOutputStream fileoutput = openFileOutput("record.txt", Context.MODE_PRIVATE);
                                int length = contactdata.size();
                                for (int i = 0; i < length; i++) {
                                    fileoutput.write(contactdata.get(i).getBytes());
                                    fileoutput.write("\n".getBytes());
                                }
                                fileoutput.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            startActivity(new Intent(ContactData.this, MainActivity.class));
                        }
                    });

                    //User click on No.
                    check.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            return;
                        }
                    });

                    //Display dialog once Delete button is clicked
                    check.show();

                }
            });
        }
    }

	//written by Qiyuan Hu
    public String getDate()
    {
        String dateStr;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        dateStr = format.format(date);
        return dateStr;
    }

	//written by Xuwei Wang
    public boolean DateValid(String date )
    {
        boolean valid = true;

        while (valid == true)
        {
            //check length
            if(date.length() == 10)
            {
                //check all number
                Pattern pattern = Pattern.compile("[0-9]*");
                if( (pattern.matcher(date.substring(0, 2)).matches())
                        &&  (pattern.matcher(date.substring(3, 5)).matches())
                        && (pattern.matcher(date.substring(6, 10)).matches())
                        && (date.substring(2, 3).equals("/"))
                        && (date.substring(5, 6).equals("/")) )
                {
                    //check year
                    Calendar today = Calendar.getInstance();
                    int yearNow = today.get(Calendar.YEAR);
                    int monthNow = today.get(Calendar.MONTH) + 1 ;
                    int dateNow = today.get(Calendar.DATE);
                    int year= Integer.parseInt( date.substring(6, 10));
                    int month= Integer.parseInt( date.substring(0, 2));
                    int dat= Integer.parseInt( date.substring(3, 5));

				    /*
				     * In the future
				     */
                    if ( year > yearNow )
                    {
                        valid = false;
                    }

                    if ( year == yearNow && month > monthNow)
                    {
                        valid = false;
                    }

                    if ( year == yearNow && month == monthNow && dat > dateNow)
                    {
                        valid = false;
                    }

                    //check month
                    if ( month > 12 || month == 0)
                    {
                        valid = false;
                    }

                    //check date
                    if (  dat > 31 || dat == 0)
                    {
                        valid = false;
                    }

                    //check 4,6,9,11 month and date
                    if ( ( month == 4 || month == 6 || month == 9 || month == 11)&& dat > 30 )
                    {
                        valid = false;
                    }

                    //check Feb
                    if ( ( month == 2)&& dat > 29 )
                    {
                        valid = false;
                    }

                    //check this year has feb 29
                    if ( ( month == 2)&& (year%4)!=0 && dat > 28 )
                    {
                        valid = false;
                    }
                }
                else
                {
                    valid = false;
                }

            }
            else
            {
                valid = false;
            }
            break;
        }

        return valid;
    }

}
