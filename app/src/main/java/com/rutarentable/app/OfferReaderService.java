package com.rutarentable.app;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Prototipo de lector: inspecciona solo texto accesible expuesto por Uber Driver.
 * Uber puede dibujar partes de la oferta sin exponerlas como nodos; en ese caso
 * se necesitaría entrada manual o una solución OCR consentida y claramente indicada.
 * Este servicio NO toca botones ni acepta/rechaza viajes.
 */
public class OfferReaderService extends AccessibilityService {
    private long lastLog=0;
    @Override public void onAccessibilityEvent(AccessibilityEvent event) {
        if(event==null || event.getPackageName()==null ||
            !"com.ubercab.driver".contentEquals(event.getPackageName())) return;
        AccessibilityNodeInfo root=getRootInActiveWindow();
        if(root==null)return;
        List<String> texts=new ArrayList<>();
        collect(root,texts,0);
        // Extracción preliminar para pruebas; deliberadamente no automatiza acciones.
        String joined=String.join(" | ",texts);
        long now=System.currentTimeMillis();
        if(now-lastLog>1500 && (joined.contains("$") || joined.toLowerCase().contains("km"))) {
            lastLog=now;
            android.util.Log.d("RutaRentableOfferReader","Texto visible candidato: "+joined);
        }
        root.recycle();
    }
    private void collect(AccessibilityNodeInfo n,List<String> out,int depth){
        if(n==null||depth>20)return;
        CharSequence t=n.getText();
        if(t!=null&&!t.toString().trim().isEmpty())out.add(t.toString().trim());
        for(int i=0;i<n.getChildCount();i++){
            AccessibilityNodeInfo c=n.getChild(i); if(c!=null)collect(c,out,depth+1);
        }
    }
    @Override public void onInterrupt(){}
}
