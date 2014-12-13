package com.deadlyduo.deadlyworkout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    Spinner spinner;
    Button button;
    EditText editText;
    public static final String PREF = "saved";
    public static final String PHONE_KEY = "phoneNumber";
    public static final String OPTION_KEY = "helpOption";
    private int itemSelected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.phone_number);
        spinner = (Spinner) findViewById(R.id.option_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        itemSelected = 0;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        button = (Button) findViewById(R.id.send_sms);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = editText.getText().toString();
                if(phoneNumber.equals("")){
                    Toast.makeText(getBaseContext(), "Provide a phone number",Toast.LENGTH_LONG).show();
                }else {
                    SharedPreferences sharedPreferences = getSharedPreferences(PREF,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(PHONE_KEY, phoneNumber);
                    editor.putInt(OPTION_KEY, itemSelected);
                    editor.commit();
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
