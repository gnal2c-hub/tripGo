package com.rutarentable.app;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.View;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.text.InputType;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;

public class OverlayService extends Service {
    WindowManager wm; View panel; WindowManager.LayoutParams params;
    float downX,downY; int startX,startY;
    @Override public IBinder onBind(Intent i){return null;}
    @Override public void onCreate(){super.onCreate();showPanel();}
    void showPanel(){
        if(!Settings.canDrawOverlays(this)) { stopSelf(); return; }
        wm=(WindowManager)getSystemService(WINDOW_SERVICE);
        LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(18,14,18,14);
        GradientDrawable bg=new GradientDrawable(); bg.setColor(Color.rgb(18,29,38)); bg.setCornerRadius(24); bg.setStroke(2,Color.rgb(255,209,47)); box.setBackground(bg);
        TextView title=new TextView(this); title.setText("RUTA RENTABLE  ⋮"); title.setTextColor(Color.rgb(255,209,47)); title.setTypeface(null,Typeface.BOLD); title.setTextSize(14); box.addView(title);
        TextView info=new TextView(this); info.setText("Oferta: ingresá pago y km"); info.setTextColor(Color.WHITE); info.setTextSize(13); box.addView(info);
        EditText fare=new EditText(this); fare.setSingleLine(true); fare.setHint("Pago Uber ($)"); fare.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL); fare.setTextColor(Color.WHITE); fare.setHintTextColor(Color.LTGRAY); box.addView(fare);
        EditText km=new EditText(this); km.setSingleLine(true); km.setHint("Km totales (incluye recogida)"); km.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL); km.setTextColor(Color.WHITE); km.setHintTextColor(Color.LTGRAY); box.addView(km);
        TextView result=new TextView(this); result.setText("Ganancia/km: —"); result.setTextColor(Color.rgb(66,220,136)); result.setTypeface(null,Typeface.BOLD); result.setTextSize(16); box.addView(result);
        TextView calc=new TextView(this); calc.setText("CALCULAR     OCULTAR"); calc.setTextColor(Color.rgb(255,209,47)); calc.setPadding(0,14,0,4); calc.setTypeface(null,Typeface.BOLD); box.addView(calc);
        calc.setOnClickListener(v->{
            if(result.getText().toString().contains("OCULTAR")){}
            try{
                double f=Double.parseDouble(fare.getText().toString().replace(",","."));
                double k=Double.parseDouble(km.getText().toString().replace(",","."));
                double fuel=getSharedPreferences("rr",MODE_PRIVATE).getFloat("fuel",2300);
                double range=getSharedPreferences("rr",MODE_PRIVATE).getFloat("range",28);
                double maint=getSharedPreferences("rr",MODE_PRIVATE).getFloat("maintenance",0);
                if(k<=0) throw new Exception();
                double net=f-k*(fuel/range+maint);
                result.setText(String.format(java.util.Locale.getDefault(),"Neto/km: $%.2f\nNeto viaje: $%.2f",net/k,net));
                result.setTextColor(net>=0?Color.rgb(66,220,136):Color.rgb(255,108,108));
            }catch(Exception e){result.setText("Ingresá pago y km válidos");}
        });
        title.setOnClickListener(v->stopSelf());
        params=new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT);
        params.gravity=Gravity.TOP|Gravity.END; params.x=16; params.y=180;
        title.setOnTouchListener((v,event)->{
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN: downX=event.getRawX();downY=event.getRawY();startX=params.x;startY=params.y;return true;
                case MotionEvent.ACTION_MOVE: params.x=startX+(int)(downX-event.getRawX());params.y=startY+(int)(event.getRawY()-downY);wm.updateViewLayout(box,params);return true;
            }return false;
        });
        panel=box; wm.addView(panel,params);
    }
    @Override public void onDestroy(){super.onDestroy();if(wm!=null&&panel!=null)try{wm.removeView(panel);}catch(Exception ignored){}}
}
