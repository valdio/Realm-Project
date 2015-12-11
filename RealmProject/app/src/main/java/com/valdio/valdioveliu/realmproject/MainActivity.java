package com.valdio.valdioveliu.realmproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get a Realm instance for this thread
        realm = Realm.getInstance(getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Writing to Realm with Transaction blocks
        realm.beginTransaction();
        Contact contact = realm.createObject(Contact.class);
        contact.setName("Contact's Name");
        contact.setEmail("Contact@hostname.com");
        contact.setAddress("Contact's Address");
        contact.setAge(20);
        realm.commitTransaction();

        //Writing to Realm by using executeTransaction()
        //Operation handled on a background thread
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Contact contact = realm.createObject(Contact.class);
                contact.setName("Contact's Name");
                contact.setEmail("Contact@hostname.com");
                contact.setAddress("Contact's Address");
                contact.setAge(20);
            }
        }, new Realm.Transaction.Callback() {
            @Override
            public void onSuccess() {
                //Contact saved
            }

            @Override
            public void onError(Exception e) {
                //Transaction is rolled-back
            }
        });


        //Query to retrieve all Contacts
        RealmQuery<Contact> query = realm.where(Contact.class);
        // Add query conditions: age over 18
        query.greaterThan("age", 18);
        // Execute the query:
        RealmResults<Contact> result = query.findAll();
        //Contacts stored in result

        Log.d("Realm", String.valueOf(result.size()));


        //Deleting data from Realm
        //Delete the first object of the result array
        realm.beginTransaction();
        result.remove(0);
        realm.commitTransaction();


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
