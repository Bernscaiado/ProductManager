package com.example.lab3databases;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView productId;
    EditText productName, productPrice;
    Button addBtn, findBtn, deleteBtn;
    ListView productListView;

    ArrayList<String> productList;
    ArrayAdapter adapter;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productList = new ArrayList<>();

        // info layout
        productId = findViewById(R.id.productId);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);

        //buttons
        addBtn = findViewById(R.id.addBtn);
        findBtn = findViewById(R.id.findBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        // listview
        productListView = findViewById(R.id.productListView);

        // db handler
        dbHandler = new MyDBHandler(this);

        // button listeners
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = productName.getText().toString();
                double price = Double.parseDouble(productPrice.getText().toString());
                Product product = new Product(name, price);
                dbHandler.addProduct(product);

                productName.setText("");
                productPrice.setText("");

//                Toast.makeText(MainActivity.this, "Add product", Toast.LENGTH_SHORT).show();
                viewProducts();
            }
        });

        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = productName.getText().toString();
                productName.setText("");
                productPrice.setText("");
                findByProductsName(name);
                //Toast.makeText(MainActivity.this, "Find product", Toast.LENGTH_SHORT).show();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = productName.getText().toString();
                productName.setText("");
                productPrice.setText("");
                deleteByName(name);
                //Toast.makeText(MainActivity.this, "Delete product", Toast.LENGTH_SHORT).show();
            }
        });


        viewProducts();
    }

    private void viewProducts() {
        productList.clear();
        Cursor cursor = dbHandler.getData();
        if (cursor.getCount() == 0) {
            Toast.makeText(MainActivity.this, "Nothing to show", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                productList.add(cursor.getString(1) + " (" +cursor.getString(2)+")");
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        productListView.setAdapter(adapter);
    }

    private void findByProductsName(String name){
        productList.clear();
        Cursor cursor = dbHandler.findByName(name);
        if (cursor.moveToFirst()) {

            Product product = new Product();
            product.setId(cursor.getInt(0));
            product.setProductName(cursor.getString(1));
            product.setProductPrice(Double.parseDouble(cursor.getString(2)));
            productList.add(product.toString());
        }

        else{
            productList.add("NO SUCH PRODUCT EXISTS");
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        productListView.setAdapter(adapter);
    }



    private void deleteByName(String name){
        productList.clear();
        dbHandler.deleteByName(name);

        Cursor cursor = dbHandler.getData();
        if (cursor.moveToFirst()) {
            productList.add("DELETION WAS SUCCESSFUL");
            do {
                Product product = new Product();
                product.setId(cursor.getInt(0));
                product.setProductName(cursor.getString(1));
                product.setProductPrice(Double.parseDouble(cursor.getString(2)));
                productList.add(product.toString());

            } while (cursor.moveToNext());
        }
        else{
            productList.add("DELETION WAS UNSUCCESSFUL BECAUSE NO SUCH PRODUCT EXISTS");
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        productListView.setAdapter(adapter);
    }
}