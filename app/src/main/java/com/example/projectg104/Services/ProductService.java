package com.example.projectg104.Services;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.example.projectg104.Entities.Product;
import com.example.projectg104.R;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProductService {
    public ArrayList<Product> cursorToArray(Cursor cursor){
        ArrayList<Product> list = new ArrayList<>();
        if(cursor.getCount() == 0){
            return list;
        }else{
            while (cursor.moveToNext()){
                Product product = new Product(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        Integer.parseInt(cursor.getString(3)),
                        cursor.getString(4),
                        Boolean.valueOf(cursor.getString(5)),
                        stringToDate(cursor.getString(6)),
                        stringToDate(cursor.getString(7)),
                        Double.parseDouble(cursor.getString(8)),
                        Double.parseDouble(cursor.getString(9))
                );
                list.add(product);
            }
        }
        return list;
    }

    public byte[] imageViewToByte(ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public Bitmap byteToBitmap(byte[] image){
        Bitmap bitmap  = BitmapFactory.decodeByteArray(image, 0, image.length );
        return bitmap;
    }

    public Date stringToDate (String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String dateToString (Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.d("ErrorDate", dateFormat.format(date));
        return dateFormat.format(date);
    }
}
