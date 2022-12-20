package com.example.projectg104.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.AlertDialog;

import com.example.projectg104.DB.DBFirebase;
import com.example.projectg104.Entities.Product;
import com.example.projectg104.MainActivity;
import com.example.projectg104.MainActivity2;
import com.example.projectg104.MainActivity3;
import com.example.projectg104.ProductForm;
import com.example.projectg104.R;
import com.example.projectg104.Services.ProductService;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Product> arrayProducts;
    ProductService productService;

    public ProductAdapter(Context context, ArrayList<Product> arrayProducts) {
        this.context = context;
        this.arrayProducts = arrayProducts;
        this.productService = new ProductService();
    }

    @Override
    public int getCount() {
        return arrayProducts.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayProducts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        view = layoutInflater.inflate(R.layout.product_template, null);

        ImageView imgProduct = (ImageView) view.findViewById(R.id.imgProduct);
        TextView textNameProduct = (TextView) view.findViewById(R.id.textNameProduct);
        TextView textDescriptionProduct = (TextView) view.findViewById(R.id.textDescriptionProduct);
        TextView textPriceProduct = (TextView) view.findViewById(R.id.textPriceProduct);
        Button btnEditTemplate = (Button) view.findViewById(R.id.btnEditTemplate);
        Button btnDeleteTemplate = (Button) view.findViewById(R.id.btnDeleteTemplate);

        Product product = arrayProducts.get(i);

        //byte[] image = product.getImage();
        //Bitmap bitmap  = BitmapFactory.decodeByteArray(image, 0, image.length );
        //imgProduct.setImageBitmap(bitmap);

        textNameProduct.setText(product.getName());
        textDescriptionProduct.setText(product.getDescription());
        int Col = product.getPrice() * 5000;
        int Usd = product.getPrice();
        String prices = "USD: "+Usd;
        textPriceProduct.setText(prices);

        productService.insertUriToImageView(product.getImage(),imgProduct,context);

        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), MainActivity3.class);
                intent.putExtra("id", String.valueOf(product.getId()));
                intent.putExtra("name", String.valueOf(product.getName()));
                intent.putExtra("description", String.valueOf(product.getDescription()));
                intent.putExtra("price", String.valueOf(product.getPrice()));
                intent.putExtra("image", String.valueOf(product.getImage()));
                context.startActivity(intent);
            }
        });

        btnEditTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), ProductForm.class);
                intent.putExtra("edit", true);
                intent.putExtra("id", product.getId());
                intent.putExtra("name", product.getName());
                intent.putExtra("description", product.getDescription());
                intent.putExtra("price", product.getPrice());
                intent.putExtra("image", product.getImage());
                intent.putExtra("latitud", product.getLatitud());
                intent.putExtra("longitud", product.getLongitud());

                context.startActivity(intent);
            }
        });

        btnDeleteTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("¿Estas seguro que deseas eliminar el producto?")
                        .setTitle("Confirmación")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DBFirebase dbFirebase = new DBFirebase();
                                dbFirebase.deleteData(product.getId());
                                Intent intent = new Intent(context.getApplicationContext(), MainActivity2.class);
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        });

        return view;
    }
}
