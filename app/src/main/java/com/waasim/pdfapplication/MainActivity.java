package com.waasim.pdfapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText name,FName,age,location;
    Button btnCreate;
    String targetPdf;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Do the file write
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        name = findViewById(R.id.edName);
         FName = findViewById(R.id.edFName);
        age = findViewById(R.id.edAge);
        location=findViewById(R.id.edLocation);
        btnCreate = findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPdf(name.getText().toString(),
                 FName.getText().toString(),
                 age.getText().toString(),
                location.getText().toString());



            }
        });
    }


    private void createPdf(String title,String fName,String age,String location) {
        Document doc = new Document();
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 30, Font.BOLD);
        if (!file.exists()) {
            file.mkdirs();
        }
        targetPdf = directory_path + title + ".pdf";
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(targetPdf));
            doc.open();

            // load image
            InputStream ims = getAssets().open("download.png");
            Bitmap bmp = BitmapFactory.decodeStream(ims);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.scalePercent(50);

            image.setAbsolutePosition(240,720);
            // image.setAlignment(Element.ALIGN_TOP);
            image.setSpacingAfter(50);
            doc.add(image);


            Paragraph paragraph = new Paragraph(title, smallBold);
            paragraph.setSpacingBefore(100);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.setSpacingAfter(100);

            doc.add(paragraph);

            //// create Table
            PdfPTable table = new PdfPTable(new float[]{2, 2, 2, 2});
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell("S.No");
            table.addCell("Name");
            table.addCell("Age");
            table.addCell("Location");

            table.setHeaderRows(1);
            PdfPCell[] cells = table.getRow(0).getCells();
            for (int j = 0; j <cells.length; j++) {
                cells[j].setBackgroundColor(BaseColor.GRAY);
            }
            for (int i = 1; i < 2; i++) {
                table.addCell(i +"");
                table.addCell(fName);
                table.addCell(age);
                table.addCell(location);

            }
            doc.add(table);


 //load image
            InputStream ims1 = getAssets().open("download.png");
            Bitmap bmp1 = BitmapFactory.decodeStream(ims1);
            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            bmp1.compress(Bitmap.CompressFormat.PNG, 100, stream1);
            Image image1 = Image.getInstance(stream1.toByteArray());
            image1.scalePercent(50);

            image1.setAbsolutePosition(230,50);
           // image.setAlignment(Element.ALIGN_MIDDLE);
            doc.add(image1);

            doc.close();
            Toast.makeText(this, "PDF Created..", Toast.LENGTH_LONG).show();
        } catch (Exception e) {

            Log.e("main", "error " + e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),
                    Toast.LENGTH_LONG).show();
        }

        openGeneratedPDF();
    }


    private void openGeneratedPDF() {
        File file = new File(targetPdf);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(MainActivity.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }

    }

}
