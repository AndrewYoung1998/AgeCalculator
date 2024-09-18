package edu.psu.sweng888.agecalculator;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mDateOfBirth;
    private Button mCalculateAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_age_calculator);
        mFirstName = findViewById(R.id.firstName);
        mLastName = findViewById(R.id.lastName);
        mDateOfBirth = findViewById(R.id.date_of_birth);
        mCalculateAge = findViewById(R.id.calculate_button);
        // Disable keyboard on focus
        mDateOfBirth.setOnClickListener(v -> {
            // Get current date for the DatePicker
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create and show DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        // Set selected date on the EditText
                        mDateOfBirth.setText( (monthOfYear + 1)  + "/" + dayOfMonth + "/" + year1);
                    }, year, month, day);
            datePickerDialog.show();
        });
        mCalculateAge.setOnClickListener(v -> {
            String firstName = mFirstName.getText().toString();
            String lastName = mLastName.getText().toString();
            String dateOfBirth = mDateOfBirth.getText().toString();
            //Validate input
            String validationMessage = validation(firstName, lastName, dateOfBirth);
            if (validationMessage != null) {
                Toast.makeText(this, validationMessage, Toast.LENGTH_LONG).show();
                return;
            }
            //Calculate age
            int age = calculateAge(dateOfBirth);
            String message = String.format("Hello %s %s, you are %d years old", firstName, lastName, age);
            //Display the age
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
    }
    // Calculate age based on date of birth
    private int calculateAge(String dateOfBirth) {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        sdf.setLenient(false); // Ensure strict parsing
        try {
            // Parse the date of birth
            Date dob = sdf.parse(dateOfBirth);
            // Calculate age
            Calendar dobCalendar = Calendar.getInstance();
            // Set the date of birth
            dobCalendar.setTime(dob);
            // Get the current date
            Calendar today = Calendar.getInstance();
            // Calculate the age
            int age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);
            // Check if the birthday has occurred this year
            if (today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }

            return age;
        } catch (ParseException e) {
            // Handle the exception
            e.printStackTrace();
            return 0;
        }
    }
    // Validate the input fields
    private String validation(String firstName, String lastName, String dateOfBirth){
        // Check if any of the fields are empty
        if(firstName.isEmpty() || lastName.isEmpty() || dateOfBirth.isEmpty()){
            return "Please fill in all fields";
        }
        return null;
    }
}