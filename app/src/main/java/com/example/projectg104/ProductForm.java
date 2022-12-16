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
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectg104.DB.DBFirebase;
import com.example.projectg104.DB.DBHelper;
import com.example.projectg104.Entities.Product;
import com.example.projectg104.Services.ProductService;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ProductForm extends AppCompatActivity {
    private ProductService productService;
    private DBHelper dbHelper;
    private DBFirebase dbFirebase;
    private Button btnFormProduct,btnGet, btnDelete, btnUpdate;
    private EditText editNameFormProduct, editDescriptionFormProduct, editPriceFormProduct, editIdFormProduct;
    private ImageView imgFormProduct;
    private TextView textLatitudFormProduct, textLongitudFormProduct;
    private MapView map;
    private MapController mapController;
    ActivityResultLauncher<String> content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        btnFormProduct = (Button) findViewById(R.id.btnFormProduct);
        btnGet = (Button) findViewById(R.id.btnGet);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        editNameFormProduct = (EditText) findViewById(R.id.editNameFormProduct);
        editDescriptionFormProduct = (EditText) findViewById(R.id.editDescriptionFormProduct);
        editIdFormProduct = (EditText) findViewById(R.id.editIdFormProduct);
        editPriceFormProduct = (EditText) findViewById(R.id.editPriceFormProduct);
        imgFormProduct = (ImageView) findViewById(R.id.imgFormProduct);
        textLatitudFormProduct = (TextView) findViewById(R.id.textLatitudFormProduct);
        textLongitudFormProduct = (TextView) findViewById(R.id.textLongitudFormProduct);

        map = (MapView) findViewById(R.id.mapForm);
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setBuiltInZoomControls(true);
        mapController = (MapController) map.getController();

        GeoPoint colombia = new GeoPoint(4.570868, -74.297333);


        mapController.setCenter(colombia);
        mapController.setZoom(12);
        map.setMultiTouchControls(true);

        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                textLatitudFormProduct.setText(String.valueOf(p.getLatitude()));
                textLongitudFormProduct.setText(String.valueOf(p.getLongitude()));
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, mapEventsReceiver);
        map.getOverlays().add(mapEventsOverlay);


        byte[] img = "".getBytes(StandardCharsets.UTF_8);
        try {
            productService = new ProductService();
            dbHelper = new DBHelper(this);
            dbFirebase = new DBFirebase();
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
                    Product product = new Product(
                            editNameFormProduct.getText().toString(),
                            editDescriptionFormProduct.getText().toString(),
                            Integer.parseInt(editPriceFormProduct.getText().toString()),
                            "",
                            Double.parseDouble(textLatitudFormProduct.getText().toString().trim()),
                            Double.parseDouble(textLongitudFormProduct.getText().toString().trim())
                    );

                    //dbHelper.insertData(product);
                    dbFirebase.insertData(product);


                }catch (Exception e){
                    Log.e("DB Insert", e.toString());
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editIdFormProduct.getText().toString().trim();
                if(id.compareTo("") != 0){
                    Log.d("DB",id);
                    dbHelper.deleteDataById(id);
                    clean();

                }else{
                    Toast.makeText(getApplicationContext(),"Ingrese id a eliminar",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editIdFormProduct.getText().toString().trim();
                if(id.compareTo("") != 0){
                    dbHelper.updateDataById(
                            id,
                            editNameFormProduct.getText().toString(),
                            editDescriptionFormProduct.getText().toString(),
                            editPriceFormProduct.getText().toString(),
                            productService.imageViewToByte(imgFormProduct)
                    );
                    clean();
                }

            }
        });
    }

    public void clean(){
        editNameFormProduct.setText("");
        editDescriptionFormProduct.setText("");
        editPriceFormProduct.setText("");
        imgFormProduct.setImageResource(R.drawable.empty);
    }
}