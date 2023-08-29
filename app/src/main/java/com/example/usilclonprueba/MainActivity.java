package com.example.usilclonprueba;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.widget.Toolbar;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.Random;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class MainActivity extends AppCompatActivity {
    private ImageView qrImageView;
    private TextView horaTextView;
    private TextView fechaTextView;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("FOTOCHECK");

        ImageView foto = (ImageView) findViewById(R.id.foto);
        foto.setImageDrawable(getResources().getDrawable(R.drawable.foto_ricardo));

        setSupportActionBar(toolbar);

        String[] nombre = {"ALEJANDRA", "ISABEL"};
        String[] apellido = {"ULLOA", "HUERTAS"};

        String nombres, apellidos;
        nombres = String.join(" ", nombre);
        apellidos = String.join(" ", apellido);
        String nombreCompleto = apellidos + ", " + nombres;

        qrImageView = findViewById(R.id.qr_image_view);

        int minuto, ano, dia, hora, mes;
        String mesString, horaString, diaString;

        minuto = LocalDateTime.now().getMinute();
        ano = LocalDateTime.now().getYear();
        dia = LocalDateTime.now().getDayOfMonth();
        hora = LocalDateTime.now().getHour();
        mes = LocalDateTime.now().getMonthValue();

        int anoUltimosDigitos = ano % 100;
        if (mes < 10){
            mesString = "0"+mes;
        }else{
            mesString = String.valueOf(mes);
        }
        if(hora < 10){
            horaString = "0"+hora;
        }else {
            horaString = String.valueOf(hora);
        }
        if(dia < 10){
            diaString = "0"+dia;
        }else{
            diaString = String.valueOf(dia);
        }



        String dataToEncode = "7726804"+minuto+anoUltimosDigitos+diaString+horaString+mesString;

        try {
            Bitmap qrBitmap = generateQRCode(dataToEncode);
            qrImageView.setImageBitmap(qrBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        //((ImageView)findViewById(R.id.foto)).setImageAlpha(10);
        Random random = new Random();
        int randomNumber = random.nextInt(90000) + 10000; // Genera un número aleatorio de 5 dígitos
        String formattedNumber = String.format("%05d", randomNumber);
        ((TextView)findViewById(R.id.nombreTextField)).setText(nombreCompleto);
        ((TextView)findViewById(R.id.matriculado)).setText("(01) MATRICULADO - ACT");
        ((TextView)findViewById(R.id.codigoTextField)).setText("22" + String.valueOf(formattedNumber));

        horaTextView = findViewById(R.id.hora);
        fechaTextView = findViewById(R.id.fecha);

        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                updateTime();
                updateDay();
                handler.postDelayed(this, 1000); // Actualizar cada segundo (1000 ms)
            }
        };
    }

    private Bitmap generateQRCode(String data) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 512, 512);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }

        return bitmap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 0); // Iniciar el bucle de actualización
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); // Detener el bucle de actualización
    }

    private void updateTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        horaTextView.setText(formattedTime);
    }
    private void updateDay(){
        LocalDateTime currentTime = LocalDateTime.now();
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        fechaTextView.setText(formattedTime);

    }
    private void cambiarImgaen(){

    }
}