package com.rutarentable.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    EditText fuelPrice, range, maintenance;
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        fuelPrice=findViewById(R.id.fuelPrice);
        range=findViewById(R.id.range);
        maintenance=findViewById(R.id.maintenance);
        findViewById(R.id.overlayPermission).setOnClickListener(v -> {
            if (!Settings.canDrawOverlays(this)) {
                startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:"+getPackageName())));
            }
        });
        findViewById(R.id.accessibilitySettings).setOnClickListener(v ->
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        findViewById(R.id.startOverlay).setOnClickListener(v -> {
            getSharedPreferences("rr",MODE_PRIVATE).edit()
                .putFloat("fuel",(float)number(fuelPrice,2300))
                .putFloat("range",(float)Math.max(.1,number(range,28)))
                .putFloat("maintenance",(float)number(maintenance,0)).apply();
            if (Settings.canDrawOverlays(this)) {
                startService(new Intent(this, OverlayService.class));
            } else {
                startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:"+getPackageName())));
            }
        });
    }
    double number(EditText e,double fallback) {
        try { return Double.parseDouble(e.getText().toString().replace(",", ".")); }
        catch(Exception ex){ return fallback; }
    }
}
