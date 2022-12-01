package com.example.projectg104;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.projectg104.DB.DBHelper;
import com.example.projectg104.Services.ProductService;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ProductForm extends AppCompatActivity {
    private ProductService productService;
    private DBHelper dbHelper;
    private Button btnFormProduct;
    private EditText editNameFormProduct, editDescriptionFormProduct, editPriceFormProduct;
    private ImageView imgFormProduct;
    ActivityResultLauncher<String> content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);

        btnFormProduct = (Button) findViewById(R.id.btnFormProduct);
        editNameFormProduct = (EditText) findViewById(R.id.editNameFormProduct);
        editDescriptionFormProduct = (EditText) findViewById(R.id.editDescriptionFormProduct);
        editPriceFormProduct = (EditText) findViewById(R.id.editPriceFormProduct);
        imgFormProduct = (ImageView) findViewById(R.id.imgFormProduct);

        byte[] img = "".getBytes(StandardCharsets.UTF_8);
        try {
            productService = new ProductService();
            dbHelper = new DBHelper(this);
        }catch (Exception e){
            Log.e("DB", e.toString());
        }

        content = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(result);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            imgFormProduct.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        imgFormProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content.launch("image/*");
            }
        });

        btnFormProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    dbHelper.insertData(
                            editNameFormProduct.getText().toString(),
                            editDescriptionFormProduct.getText().toString(),
                            editPriceFormProduct.getText().toString(),
                            productService.imageViewToByte(imgFormProduct)
                    );
                }catch (Exception e){
                    Log.e("DB Insert", e.toString());
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
            }
        });

    }
}