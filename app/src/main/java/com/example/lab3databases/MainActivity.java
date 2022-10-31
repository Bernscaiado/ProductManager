package com.example.lab3databases;

import static com.google.android.material.internal.ContextUtils.getActivity;

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
import java.util.List;

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
                String price = productPrice.getText().toString();
                productName.setText("");
                productPrice.setText("");

                if(name.length() > 0 && price.length() >0)
                    findProduct(name, price);
                else if (name.length() > 0)
                    findByProductsName(name);
                else if(price.length() > 0)
                    findByProductsPrice(price);
                else {
                    emptyFieldException();
                }

                //Toast.makeText(MainActivity.this, "Find product", Toast.LENGTH_SHORT).show();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = productName.getText().toString();
                String price = productPrice.getText().toString();
                System.out.println("Name: "+name.length()+"\tPrice: "+price.length());
                productName.setText("");
                productPrice.setText("");
                if(name.length() > 0 && price.length() > 0)
                    deleteProduct(name, price);
                else if(name.length() > 0)
                    deleteByName(name);
                else if(price.length() > 0)
                    deleteByPrice(price);
                else {
                    emptyFieldException();
                }
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
            do{
                Product product = new Product();
                product.setId(cursor.getInt(0));
                product.setProductName(cursor.getString(1));
                product.setProductPrice(Double.parseDouble(cursor.getString(2)));
                productList.add(product.toString());
            }while(cursor.moveToNext());
        }

        else{
            productList.add("NO SUCH PRODUCT EXISTS");
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        productListView.setAdapter(adapter);
    }

    private void deleteByName(String name){
        productList.clear();
        boolean flag = dbHandler.deleteByName(name);
        Cursor cursor = dbHandler.getData();

        if (flag)
            productList.add("DELETION WAS SUCCESSFUL");
        else
            productList.add("DELETION WAS UNSUCCESSFUL, BECAUSE NO SUCH PRODUCT EXISTS.");
        if (cursor.moveToFirst()) {

            do {
                Product product = new Product();
                product.setId(cursor.getInt(0));
                product.setProductName(cursor.getString(1));
                product.setProductPrice(Double.parseDouble(cursor.getString(2)));
                productList.add(product.toString());

            } while (cursor.moveToNext());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        productListView.setAdapter(adapter);
    }


    private void findByProductsPrice(String price){
        productList.clear();
        Cursor cursor = dbHandler.findByPrice(price);
        if (cursor.moveToFirst()) {
            do{
                Product product = new Product();
                product.setId(cursor.getInt(0));
                product.setProductName(cursor.getString(1));
                product.setProductPrice(Double.parseDouble(cursor.getString(2)));
                productList.add(product.toString());
            }while(cursor.moveToNext());
        }

        else{
            productList.add("NO SUCH PRODUCT EXISTS");
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        productListView.setAdapter(adapter);
    }


    private void deleteByPrice(String price){
        productList.clear();
        boolean flag = dbHandler.deleteByPrice(price);
        Cursor cursor = dbHandler.getData();

        if (flag)
            productList.add("DELETION WAS SUCCESSFUL");
        else
            productList.add("DELETION WAS UNSUCCESSFUL BECAUSE NO SUCH PRODUCT EXISTS");

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(0));
                product.setProductName(cursor.getString(1));
                product.setProductPrice(Double.parseDouble(cursor.getString(2)));
                productList.add(product.toString());

            } while (cursor.moveToNext());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        productListView.setAdapter(adapter);
    }


    private void findProduct(String name, String price){
        productList.clear();
        Cursor cursor = dbHandler.findProduct(name, price);
        if (cursor.moveToFirst()) {
            do{
                Product product = new Product();
                product.setId(cursor.getInt(0));
                product.setProductName(cursor.getString(1));
                product.setProductPrice(Double.parseDouble(cursor.getString(2)));
                productList.add(product.toString());
            }while(cursor.moveToNext());
        }

        else{
            productList.add("NO SUCH PRODUCT EXISTS");
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        productListView.setAdapter(adapter);
    }

    private void deleteProduct(String name, String price){
        productList.clear();
        boolean flag = dbHandler.deleteProduct(name, price);
        Cursor cursor = dbHandler.getData();
        if (flag)
            productList.add("DELETION WAS SUCCESSFUL");
        else
            productList.add("DELETION WAS UNSUCCESSFUL BECAUSE NO SUCH PRODUCT EXISTS");

        if (cursor.moveToFirst()) {
            do{
                Product product = new Product();
                product.setId(cursor.getInt(0));
                product.setProductName(cursor.getString(1));
                product.setProductPrice(Double.parseDouble(cursor.getString(2)));
                productList.add(product.toString());
            }while(cursor.moveToNext());
        }


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        productListView.setAdapter(adapter);
    }

    private void emptyFieldException(){
        productList.clear();
        productList.add("This Operation cannot be performed. Please complete at least one field.");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        productListView.setAdapter(adapter);
    }
}