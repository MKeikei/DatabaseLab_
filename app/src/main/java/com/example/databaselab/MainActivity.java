package com.example.databaselab;


import androidx.appcompat.app.AppCompatActivity;



import android.os.Bundle;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView idView;
    EditText productBox;
    EditText priceBox;

    ListView productlist;
    ArrayList<String> listItem;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idView = (TextView) findViewById(R.id.productID);
        productBox = (EditText) findViewById(R.id.productName);
        priceBox = (EditText) findViewById(R.id.productPrice);
        productlist = (ListView) findViewById(R.id.productListView);

        MyDBHandler dbHandler = new MyDBHandler(this);
        listItem = new ArrayList<>();
        viewData();

        productlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = productlist.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this, "" + text, Toast.LENGTH_SHORT).show();
            }
        });

        Button addButton = findViewById(R.id.Add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newProduct(view);
            }
        });
        Button findButton = findViewById(R.id.Find);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lookupProduct(view);
            }
        });
        Button removeButton = findViewById(R.id.Remove);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeProduct(view);
            }
        });



    }




    public void newProduct(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this);
        double price = Double.parseDouble(priceBox.getText().toString());
        Product product = dbHandler.findProduct(productBox.getText().toString());
        dbHandler.addProduct(product);
        productBox.setText(""); // Get ID of the text of
        priceBox.setText(""); // Get ID of the text of
        listItem.clear();
        viewData();
    }
    public void lookupProduct (View view){
        MyDBHandler dbHandler = new MyDBHandler(this);
        Product product = dbHandler.findProduct(productBox.getText().toString());
        if (product != null) {
            idView.setText(String.valueOf(product.getID()));
            priceBox.setText(String.valueOf(product.getPrice()));
        } else {
            idView.setText("No Match Found");
        }
    }

    public void removeProduct(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this);
        boolean result = dbHandler.deleteProduct(productBox.getText().toString());
        listItem.clear();
        viewData();
        if (result) {
            idView.setText("Record Deleted");
            productBox.setText("");
            priceBox.setText("");
        } else {
            idView.setText("No Match Found");
        }

    }
    private void viewData() {
        MyDBHandler dbHandler = new MyDBHandler(this);
        Cursor cursor = dbHandler.viewData();
        if(cursor.getCount() == 0) {
            Toast.makeText(this, "Not data to show", Toast.LENGTH_SHORT).show();
        }
        else {
            while(cursor.moveToNext()){
                listItem.add(cursor.getString(1));
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
            productlist.setAdapter(adapter);
        }
    }
}