package com.example.carreragatos;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class VistaDibujo extends SurfaceView implements Runnable{
    MediaPlayer mp;
    public int NGatos=4;
    ArrayList<ObjetoAnimado> coleccion=new ArrayList<ObjetoAnimado>();
    Thread hiloDibujo;
    volatile boolean running;
    SurfaceHolder holder;
    int x;
    Fondo pista;
    ObjetoAnimado gato;
    public VistaDibujo(Context context) {

        super(context);

        holder=getHolder();
        pista=new Fondo("pista.jpg");
        pista.inicializar(context.getAssets());
        int d=(int)(ObjetoAnimado.AltoFil*0.50f)+25;
        for (int i=0;i<NGatos;i++) {

            gato = new ObjetoAnimado(0, 25+(i*d), "gato.png");
            gato.inicializar(context.getAssets());
            coleccion.add(gato);
        }

    }


    @Override
    public void run() {
        x=0;
        long actual=System.currentTimeMillis();
        ArrayList<String> ganadores=new ArrayList<String>();
        mp=MediaPlayer.create(getContext(),R.raw.musica);
        mp.setLooping(true);
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
            }
        });

        while(running)
        {   x++;
            x=x%256;
            if(System.currentTimeMillis()-actual>=80)
            {         actual=System.currentTimeMillis();
                for (ObjetoAnimado gato:coleccion)
                        gato.nextFrame();
            }
            if (holder.getSurface().isValid())
            {
                Canvas c=holder.lockCanvas();
                Random x=new Random(System.currentTimeMillis());
                for(ObjetoAnimado gato:coleccion)
                {
                    gato.avanzar(x.nextInt(10));
                }

                drawSurface(c);
                holder.unlockCanvasAndPost(c);

            }

            for (ObjetoAnimado gato:coleccion)
            {
                if(gato.fin())
                {
                    ganadores.add(String.valueOf(coleccion.indexOf(gato)+1));
                }
            }
            if (ganadores.size()!=0)
            {
                running=false;
            }

        }
        mp.stop();
        String mensaje=getNombres(ganadores);
        while(!holder.getSurface().isValid())
        {

        }
        Canvas c=holder.lockCanvas();
        Paint p=new Paint();
        p.setTextSize(100);
        p.setColor(Color.BLUE);
        c.drawText(mensaje,500,500,p);
        holder.unlockCanvasAndPost(c);
    }


    private String getNombres(ArrayList<String> dorsales)
    {
     String mensaje= "Ganadores: ";
        int i;

     for (i=0; i<dorsales.size()-1;i++)
         mensaje+=dorsales.get(i)+", ";

     mensaje+=dorsales.get(i);
     return mensaje;
    }


    private void drawSurface(Canvas c) {

        pista.draw(c);
        for (ObjetoAnimado gato: coleccion)
                { gato.draw(c);}
    }

    public  void pausar()
    {
        running=false;
        while(true)
        {
            try {
                hiloDibujo.join();
                break;
            }
            catch(Exception ex)
            {

            }
        }
        hiloDibujo=null;
    }

    public void reanudar()
    {
        hiloDibujo=new Thread(this);
        running=true;
        hiloDibujo.start();
    }
}
