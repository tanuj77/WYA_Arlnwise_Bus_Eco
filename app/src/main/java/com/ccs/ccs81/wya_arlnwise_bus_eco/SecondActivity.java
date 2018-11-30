package com.ccs.ccs81.wya_arlnwise_bus_eco;

import android.app.Activity;

/**
 * Created by Tanuj on 05/12/2017.
 */
import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Banke Bihari on 04/11/2016.
 */
public class
SecondActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    EditText editTextCompanyID, editTextBranch;
    TextView textViewStartDate, textViewEndDate;
    CheckBox checkBoxTotal, checkBoxBasicFare, checkBoxYQTax, checkBoxYRTax, checkBoxBusinessEconomy;
    RadioGroup radioGrouprg;
    RadioButton rbEco, rbBusiness, rbPrm, rbFirst, rbAll;
    Spinner spinnerType;
    Button buttonSubmit, buttonExcel;
    private ProgressBar pbCircular;
    String checkTotal = "", checkBF = "", checkYQ = "", checkYR = "", rbFlagBE = "", rgFlag = "";
    String cpCompanyID, branchName, startDate = "null", strStartDate = "null", cpStartDate, endDate, aResponse, cpCompanyUrl, cpPartyCode, mUserName, mPassword, check, res,
            makeExcelSheet = "false", state, cpMobileNo, dropDownAgentBranch = " ", cpMultipleBranch, cpBranch, customerBranch, agentBranch,cpFinancialYear,strFinancialYear;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Internet Connection Error");
            alertDialog.setMessage("Please connect to working Internet connection");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SecondActivity.this.finish();
                }
            });
            // Showing Alert Message
            alertDialog.show();

            return;
        }


        editTextCompanyID = (EditText) findViewById(R.id.et_companyid);
        editTextBranch = (EditText) findViewById(R.id.et_branch);
        spinnerType = (Spinner) findViewById(R.id.spinner);

        textViewStartDate = (TextView) findViewById(R.id.et_startdate);
        textViewEndDate = (TextView) findViewById(R.id.et_endtdate);
        checkBoxTotal = (CheckBox) findViewById(R.id.cb_total);
        checkBoxBasicFare = (CheckBox) findViewById(R.id.cb_basicfare);
        checkBoxYQTax = (CheckBox) findViewById(R.id.cb_yqtax);
        checkBoxYRTax = (CheckBox) findViewById(R.id.cb_yrtax);
        radioGrouprg = (RadioGroup) findViewById(R.id.rg);
        checkBoxBusinessEconomy = (CheckBox) findViewById(R.id.cb_businesseconomy);
        rbEco = (RadioButton) findViewById(R.id.rb_eco);
        rbBusiness = (RadioButton) findViewById(R.id.rb_business);
        rbPrm = (RadioButton) findViewById(R.id.rb_prm);
        rbFirst = (RadioButton) findViewById(R.id.rb_first);
        rbAll = (RadioButton) findViewById(R.id.rb_all);
        buttonExcel = (Button) findViewById(R.id.btn_downloadExcel);
        buttonSubmit = (Button) findViewById(R.id.btn_submit);
        pbCircular = (ProgressBar) findViewById(R.id.pbCirular);
        pbCircular.setVisibility(View.INVISIBLE);

        SharedPreferences sharedPreferences2 = getApplicationContext().getSharedPreferences("companyDetail", MODE_PRIVATE);
        cpCompanyID = sharedPreferences2.getString("companyID", null);
        cpStartDate = sharedPreferences2.getString("startDate", null);
        cpCompanyUrl = sharedPreferences2.getString("companyUrl", null);
        cpPartyCode = sharedPreferences2.getString("partyCode", null);
        mUserName = sharedPreferences2.getString("userName", null);
        mPassword = sharedPreferences2.getString("password", null);
        cpMobileNo = sharedPreferences2.getString("mobileNo", null);
        dropDownAgentBranch = sharedPreferences2.getString("dropDownAgentBranch", null);
        cpMultipleBranch = sharedPreferences2.getString("multipleBranch", null);
        cpBranch = sharedPreferences2.getString("branch", null);
        cpFinancialYear = sharedPreferences2.getString("financialYear", null);

        editTextCompanyID.setText(cpCompanyID);



        if (cpPartyCode == "Agent" || cpPartyCode.equalsIgnoreCase("Agent") || cpPartyCode.equals("Agent")) {
            editTextBranch.setText("");
            editTextBranch.setClickable(false);
            editTextBranch.setFocusableInTouchMode(false);
            editTextBranch.setFocusable(false);
            spinnerType.setClickable(true);
            spinnerType.setFocusableInTouchMode(true);
            spinnerType.setFocusable(true);
            spinnerType.setEnabled(true);

            if (dropDownAgentBranch.trim().equalsIgnoreCase("No Branch Found") || dropDownAgentBranch.trim().equals("No Branch Found") || dropDownAgentBranch == "No Branch Found"){
                spinnerType.setOnItemSelectedListener(this);
                List<String> categories = new ArrayList<String>();
                categories.add("No Branch");
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerType.setAdapter(dataAdapter);
            }else {
                dropDownAgentBranch = dropDownAgentBranch.replace("[", "");
                dropDownAgentBranch = dropDownAgentBranch.replace("]", "");
                String[] branch = dropDownAgentBranch.split(",");

                spinnerType.setOnItemSelectedListener(this);
                List<String> categories = new ArrayList<String>();
                categories.add("Select Agent Branch");
                for (int i = 0; i < branch.length; i++) {

                    categories.add(branch[i]);
                }
                categories.add("All");

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerType.setAdapter(dataAdapter);
            }

        } else {
            if (cpMultipleBranch == "yes" || cpMultipleBranch.equalsIgnoreCase("yes") || cpMultipleBranch.equals("yes")) {
                customerBranch = cpBranch;
                editTextBranch.setText(cpBranch);
                editTextBranch.setClickable(false);
                editTextBranch.setFocusableInTouchMode(false);
                editTextBranch.setFocusable(false);
                spinnerType.setClickable(false);
                spinnerType.setFocusableInTouchMode(false);
                spinnerType.setFocusable(false);
                spinnerType.setEnabled(false);

            } else {
                customerBranch = "";
                editTextBranch.setText("");
                editTextBranch.setClickable(false);
                editTextBranch.setFocusableInTouchMode(false);
                editTextBranch.setFocusable(false);
                spinnerType.setClickable(false);
                spinnerType.setFocusableInTouchMode(false);
                spinnerType.setFocusable(false);
                spinnerType.setEnabled(false);

            }

        }


        String dateFormat = cpStartDate;
        String[] dateAray = dateFormat.split("-");
        String year = dateAray[0];
        String month = dateAray[1];
        final String day = dateAray[2];

        textViewStartDate.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(year));
        startDate = String.valueOf(new StringBuilder().append(year).append("-").append(month).append("-").append(day));
        //////////START////////Show calendar and select date ////////
        final Calendar myStartDateCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datePickerListenerStartDAte = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myStartDateCalendar.set(Calendar.YEAR, year);
                myStartDateCalendar.set(Calendar.MONTH, month);
                myStartDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                textViewStartDate.setText(sdf.format(myStartDateCalendar.getTime()));
                startDate = String.valueOf(new StringBuilder().append(year).append("-").append(month).append("-").append(dayOfMonth)).trim();
            }
        };

        textViewStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SecondActivity.this, datePickerListenerStartDAte, myStartDateCalendar
                        .get(Calendar.YEAR), myStartDateCalendar.get(Calendar.MONTH),
                        myStartDateCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                textViewEndDate.setText(sdf.format(myCalendar.getTime()));
                endDate = String.valueOf(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(dayOfMonth)).trim();
            }
        };

        textViewEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SecondActivity.this, datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
// ////////END////////Show calendar and select date ////////

        if (dropDownAgentBranch.trim().equalsIgnoreCase("No Branch Found") || dropDownAgentBranch.trim().equals("No Branch Found") || dropDownAgentBranch == "No Branch Found"){
            spinnerType.setOnItemSelectedListener(this);
            List<String> categories = new ArrayList<String>();
            categories.add("No Branch");
             ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerType.setAdapter(dataAdapter);
        }else {
            dropDownAgentBranch = dropDownAgentBranch.replace("[", "");
            dropDownAgentBranch = dropDownAgentBranch.replace("]", "");
            String[] branch = dropDownAgentBranch.split(",");

            spinnerType.setOnItemSelectedListener(this);
            List<String> categories = new ArrayList<String>();
            categories.add("Select Agent Branch");
            for (int i = 0; i < branch.length; i++) {

                categories.add(branch[i]);
            }
            categories.add("All");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerType.setAdapter(dataAdapter);
        }

        checkBoxBasicFare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    checkBF = "Y";
                    checkBoxTotal.setChecked(false);
                } else {
                    checkBF = "";
                }
            }
        });
        checkBoxYQTax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    checkYQ = "Y";
                    checkBoxTotal.setChecked(false);
                } else {
                    checkYQ = "";
                }
            }
        });
        checkBoxYRTax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    checkYR = "Y";
                    checkBoxTotal.setChecked(false);
                } else {
                    checkYR = "";
                }
            }
        });

        checkBoxTotal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkTotal = "Y";

                    checkBoxBasicFare.setChecked(false);
                    checkBoxYQTax.setChecked(false);
                    checkBoxYRTax.setChecked(false);
                    checkBF = "";
                    checkYQ = "";
                    checkYR = "";
                } else {
                    checkTotal = "";
                }
            }
        });


        checkBoxBusinessEconomy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbFlagBE = "Y";
                    radioGrouprg.setVisibility(View.INVISIBLE);
                    rgFlag = "";
                    rbEco.setChecked(false);
                    rbBusiness.setChecked(false);
                    rbPrm.setChecked(false);
                    rbFirst.setChecked(false);
                    rbAll.setChecked(false);
                } else {
                    rbFlagBE = "";
                    radioGrouprg.setVisibility(View.VISIBLE);
                }
            }
        });


        radioGrouprg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_eco) {
                    rgFlag = "E";
                } else if (checkedId == R.id.rb_business) {
                    rgFlag = "B";
                } else if (checkedId == R.id.rb_prm) {
                    rgFlag = "P";
                } else if (checkedId == R.id.rb_first) {
                    rgFlag = "F";
                } else if (checkedId == R.id.rb_all) {
                    rgFlag = "A";
                } else {
                    rgFlag = "";
                }
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeExcelSheet = "false";
                // branchName = editTextBranch.getText().toString();
                if (!cd.isConnectingToInternet()) {
                    Toast.makeText(SecondActivity.this, "Please connect to working Internet connection", Toast.LENGTH_LONG).show();
                    return;
                }

//                if (agentBranch.trim().equalsIgnoreCase("Select Agent Branch") || agentBranch.trim().equals("Select Agent Branch") || agentBranch.trim() == "Select Agent Branch") {
//
//                } else {
//
//                }


                strStartDate = textViewStartDate.getText().toString();
                String dateFormat = strStartDate;
                String[] dateAray = dateFormat.split("-");
                String day = dateAray[0];
                String month = dateAray[1];
                final String year = dateAray[2];
                strStartDate = String.valueOf(new StringBuilder().append(year).append("-").append(month).append("-").append(day));

                if (textViewEndDate.getText().length() < 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                    builder.setTitle("Alert");
                    builder.setMessage("Select End Date first");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else if (!dateValidation(strStartDate, endDate, "yyyy-MM-dd")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                    builder.setTitle("Alert");
                    builder.setMessage(" End Date must be grater than start date");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }else if (cpPartyCode == "Agent" || cpPartyCode.equalsIgnoreCase("Agent") || cpPartyCode.equals("Agent")) {

                    if (agentBranch.trim().equalsIgnoreCase("Select Agent Branch") || agentBranch.trim().equals("Select Agent Branch") || agentBranch == "Select Agent Branch") {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                        builder.setTitle("Alert");
                        builder.setMessage("Select Any agent branch first");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    } else {
                        branchName = agentBranch;
                        pbCircular.setVisibility(View.VISIBLE);
                        new agentAsync().execute();
                    }

                } else {

                    branchName = customerBranch;
                    pbCircular.setVisibility(View.VISIBLE);
                    new customerAsync().execute();
                }

            }
        });
        buttonExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////START/ Run time Permission to write file///////////
                if (ActivityCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(SecondActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //Show Information about why you need the permission
                        AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                        builder.setTitle("Need Storage Permission");
                        builder.setMessage("This app needs storage permission.");
                        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                ActivityCompat.requestPermissions(SecondActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
                        //Previously Permission Request was cancelled with 'Dont Ask Again',
                        // Redirect to Settings after showing Information about why you need the permission
                        AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                        builder.setTitle("Need Storage Permission");
                        builder.setMessage("This app needs storage permission.");
                        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                sentToSettings = true;
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                                Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    } else {
                        //just request the permission
                        ActivityCompat.requestPermissions(SecondActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }

                    SharedPreferences.Editor editor = permissionStatus.edit();
                    editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
                    editor.commit();
                } else {
///END/ Run time Permission to write file////in above else block further code vl write ///////

                    makeExcelSheet = "True";
                    //  branchName = editTextBranch.getText().toString();
                    // new infoAsync().execute();
                    strStartDate = textViewStartDate.getText().toString();
                    String dateFormat = strStartDate;
                    String[] dateAray = dateFormat.split("-");
                    String day = dateAray[0];
                    String month = dateAray[1];
                    final String year = dateAray[2];
                    strStartDate = String.valueOf(new StringBuilder().append(year).append("-").append(month).append("-").append(day));

                    if (textViewEndDate.getText().length() < 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                        builder.setTitle("Alert");
                        builder.setMessage("Select End Date first");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }else if (!dateValidation(strStartDate, endDate, "yyyy-MM-dd")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                        builder.setTitle("Alert");
                        builder.setMessage(" End Date must be grater than start date");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    } else if (cpPartyCode == "Agent" || cpPartyCode.equalsIgnoreCase("Agent") || cpPartyCode.equals("Agent")) {
                        if (agentBranch.trim().equalsIgnoreCase("Select Agent Branch") || agentBranch.trim().equals("Select Agent Branch") || agentBranch == "Select Agent Branch") {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                            builder.setTitle("Alert");
                            builder.setMessage("Select Any agent branch first");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        } else {
                            branchName = agentBranch;
                            pbCircular.setVisibility(View.VISIBLE);
                            new agentAsync().execute();
                        }

                    } else {
                        branchName = customerBranch;
                        new customerAsync().execute();
                    }
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.spinner) {
            if (dropDownAgentBranch.trim().equalsIgnoreCase("No Branch Found") || dropDownAgentBranch.trim().equals("No Branch Found") || dropDownAgentBranch == "No Branch Found") {
                agentBranch = "NoBranch";
            } else {
                agentBranch = parent.getItemAtPosition(position).toString();
                agentBranch = String.valueOf(agentBranch);
            }
        }

        if (spinner.getId() == R.id.spinnerfinancialyear){
            strFinancialYear = parent.getItemAtPosition(position).toString();
            strFinancialYear = String.valueOf(strFinancialYear);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class agentAsync extends AsyncTask<Void, Void, Void> {
        String aResponse;

        @Override
        protected Void doInBackground(Void... voids) {
            //Create Webservice class object
            AgentWebservice agentWebservice = new AgentWebservice();

            //Call Webservice class method and pass values and get response
            aResponse = agentWebservice.getConvertedWeight(cpCompanyUrl, "GetValidUser", cpCompanyID, mUserName, mPassword);
            res = aResponse;
            Log.i("BBRRaResponse", aResponse);
            if (res == "Invalid User" || res.equalsIgnoreCase("Invalid User") || res.equals("Invalid User")) {
                check = "";
                startDate = "";
            } else {

                String[] strArray = res.split("%");
                check = strArray[0];
                startDate = strArray[1];
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            pbCircular.setVisibility(View.INVISIBLE);
            if (res == "Invalid User" || res.equalsIgnoreCase("Invalid User") || res.equals("Invalid User")) {
                Toast.makeText(SecondActivity.this, "Do Registration Again", Toast.LENGTH_LONG).show();
            } else if (check.equals("Valid User")) {
                new infoAsync().execute();
            }
        }
    }

    class customerAsync extends AsyncTask<Void, Void, Void> {
        String bResponse;

        @Override
        protected Void doInBackground(Void... voids) {
            //Create Webservice class object
            CustomerWebservice com = new CustomerWebservice();

            //Call Webservice class method and pass values and get response
            bResponse = com.checkValidUsernamePassword(cpCompanyUrl, "wyCheckPartyLogin", cpCompanyID, cpPartyCode, mUserName, mPassword);

            Log.i("AndroidExampleOutput", "----" + bResponse);

            return null;
        }

        protected void onPostExecute(Void result) {
            //Toast.makeText(getApplicationContext(), bResponse, Toast.LENGTH_LONG).show();

            if (bResponse.equals("Successfully Login")) {
                new infoAsync().execute();
            } else {
                Toast.makeText(SecondActivity.this, "Do Registration Again", Toast.LENGTH_LONG).show();
            }
        }
    }


    class infoAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            SecondWebservice secondWebservice = new SecondWebservice();
            aResponse = secondWebservice.getConvertedWeight(cpCompanyUrl, "GetArlnBusinessEconomy_JSON1", cpCompanyID, branchName, strStartDate, endDate, checkBF, checkYQ, checkYR, checkTotal, rbFlagBE, rgFlag, cpMobileNo);
            Log.i("response22223", aResponse);
            return null;
        }

        protected void onPostExecute(Void result) {
            pbCircular.setVisibility(View.INVISIBLE);
            if (aResponse.trim().equalsIgnoreCase("No Data Found") || aResponse.trim().equals("No Data Found") || aResponse.trim() == "No Data Found") {
                Toast.makeText(SecondActivity.this, aResponse, Toast.LENGTH_LONG).show();
            } else if (aResponse.trim().equalsIgnoreCase("Party doesn't belongs to this branch") || aResponse.trim().equals("Party doesn't belongs to this branch") || aResponse.trim() == "Party doesn't belongs to this branch") {
                Toast.makeText(SecondActivity.this, aResponse, Toast.LENGTH_LONG).show();
            } else if (aResponse.trim().equalsIgnoreCase("Something went wrong at server side or wrong input") || aResponse.trim().equals("Something went wrong at server side or wrong input") || aResponse.trim() == "Something went wrong at server side or wrong input") {
                Toast.makeText(SecondActivity.this, aResponse, Toast.LENGTH_LONG).show();
            } else {
                if (makeExcelSheet.equals("True") || makeExcelSheet == "True") {
                    String input = "";// = "Harey,Krishnagg"+"\n"+"Harey,Ramgg";
                    String string = aResponse;
                    String[] parts = string.split("%");
//input = parts[0].replace(",","\n")+","+parts[1].replace(",","\n");
                    String[] response0 = parts[0].split(",");
                    String[] response01 = parts[1].split(",");
                    String[] response02 = parts[2].split(",");
                    String[] response03 = parts[3].split(",");
                    String[] response04 = parts[4].split(",");
                    String[] response05 = parts[5].split(",");
                    String[] response06 = parts[6].split(",");
                    String[] response07 = parts[7].split(",");
                    String[] response08 = parts[8].split(",");
                    String[] response09 = parts[9].split(",");
                    String[] response10 = parts[10].split(",");

                    for (int i = 0; i < response0.length; i++) {
                        String Airline = response0[i];
                        String BranchId = response01[i];
                        String Business = response02[i];
                        String Business_Amount = response03[i];
                        String Eco = response04[i];
                        String Eco_Amount = response05[i];
                        String First = response06[i];
                        String First_Amount = response07[i];
                        String Premium = response08[i];
                        String Premium_Amount = response09[i];
                        String AirlineName = response10[i];
                        input = input + "" + Airline + "," + BranchId + "," + Business + "," + Business_Amount + "," + Eco + "," + Eco_Amount + "," + First + "," + First_Amount + "," + Premium + "," + Premium_Amount + "," + AirlineName + "\n";
                        input = input.replace("[", "");
                        input = input.replace("]", "");

                    }

                    state = Environment.getExternalStorageState();

                    if (Environment.MEDIA_MOUNTED.equals(state)) {//checking sd card or extenal device is available or not
                        File Root = Environment.getExternalStorageDirectory();
                        File Dir = new File(Root.getAbsolutePath() + "/Winyatra");//new folder created
                        if (!Dir.exists()) { //folder exist or not
                            Dir.mkdir();    //folder created
                        }

                        String fileName = "";
                        if (rbFlagBE.trim().equalsIgnoreCase("Y") || rbFlagBE.trim().equals("Y") || rbFlagBE.trim() == "Y") {
                            fileName = "A.B.E Business Economy Report.csv";
                        } else if (rgFlag.trim().equalsIgnoreCase("E") || rgFlag.trim().equals("E") || rgFlag.trim() == "E") {
                            fileName = "A.B.E Economy Report.csv";
                        } else if (rgFlag.trim().equalsIgnoreCase("B") || rgFlag.trim().equals("B") || rgFlag.trim() == "B") {
                            fileName = "A.B.E Business Report.csv";
                        } else if (rgFlag.trim().equalsIgnoreCase("P") || rgFlag.trim().equals("P") || rgFlag.trim() == "P") {
                            fileName = "A.B.E Premium Report.csv";
                        } else if (rgFlag.trim().equalsIgnoreCase("F") || rgFlag.trim().equals("F") || rgFlag.trim() == "F") {
                            fileName = "A.B.E First Report.csv";
                        } else if (rgFlag.trim().equalsIgnoreCase("A") || rgFlag.trim().equals("A") || rgFlag.trim() == "A") {
                            fileName = "A.B.E All Report.csv";
                        }

                        File file = null;
                        file = new File(Dir, fileName);  //text file created

                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(file); //write in text file
                            fileOutputStream.write(input.getBytes());
                            fileOutputStream.close();
                            Toast.makeText(getApplicationContext(), "Excel saved ", Toast.LENGTH_LONG).show();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "sd card not found", Toast.LENGTH_LONG).show();
                    }

                } else if (makeExcelSheet.equals("false") || makeExcelSheet == "false") {

                    Intent intent = new Intent(SecondActivity.this, RecyclerViewActivity.class);
                    Bundle extra = new Bundle();
                    extra.putString("webserviceresponse", aResponse);
                    intent.putExtras(extra);
                    startActivity(intent);

                }
            }
        }
    }

    public static boolean dateValidation(String startDate, String endDate, String dateFormat) {

        try {
            SimpleDateFormat df = new SimpleDateFormat(dateFormat);
            Date stDate = df.parse(startDate);
            Date enDate = df.parse(endDate);

            if (enDate.equals(stDate)) {
                return true;
            } else if (enDate.after(stDate)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }
}



