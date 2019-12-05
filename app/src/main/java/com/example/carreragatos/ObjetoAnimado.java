package com.example.carreragatos;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;

import java.io.InputStream;

public class ObjetoAnimado {

    public static final int NFotogramas= 8;
    public static final int Columnas=2;
    public static final int Filas=4;
    public static final int AnchoCol=500;
    public static final int AltoFil=260;
    public static final int frameTime=80;
    String fileName;
    private int posx,posy;
    Bitmap spriteSheet;
    int frameActual;

    public ObjetoAnimado(int x, int y, String file)
    {
        posx=x;
        posy=y;
        fileName=file;
        frameActual=0;

    }
    public void inicializar(AssetManager asm)
    {
        try {
            InputStream is = asm.open(fileName);
            spriteSheet=BitmapFactory.decodeStream(is);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void avanzar(int n)
    {
        posx+=n;

    }
    public void nextFrame()
    {
        frameActual=(frameActual<NFotogramas-1)?frameActual+1:0;
    }

    public void draw(Canvas c)
    {
        int x,y;
        y=AltoFil*(frameActual/Columnas);
        x=AnchoCol*(frameActual%Columnas);
        Rect origen=new Rect(x,y,x+AnchoCol,y+AltoFil);
        Rect destino=new Rect(posx,posy,posx+(int)(AnchoCol*0.40f),posy+(int)(AltoFil*0.40f));
        c.drawBitmap(spriteSheet,origen,destino,null);
    }

    public int getFrameTime()
    {
        return frameTime;
    }

    public boolean fin()
    {
        if(posx>=1600)
            return true;

        return false;
    }

}
