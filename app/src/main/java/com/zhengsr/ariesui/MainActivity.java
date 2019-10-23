package com.zhengsr.ariesui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhengsr.ariesuilib.select.colors.ColorsSelectView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




    }

    private String mCurrentColor;
    public void colorclick(View view) {
        CusDialog dialog = new CusDialog.Builder()
                .setContext(this)
                .setLayoutId(R.layout.colorselector)
                .setWidth(getResources().getDimensionPixelSize(R.dimen.dialog_width))
                .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOutSideDimiss(true)
                .builder();
        dialog.setDismissByid(R.id.cancel);
        final ColorsSelectView colorsSelectView = dialog.getViewbyId(R.id.colorselector);
        final String[] colorMsg = new String[1];
       /* colorsSelectView.circleColor(Color.WHITE)
                .type(ColorsSelectView.TRI)
                .typeColor(Color.WHITE)
                .triSize(10)
                .triOritation(ColorsSelectView.TOP)
                .go();*/
       colorsSelectView.circleColor(Color.WHITE)
               .go();
        dialog.setOnClickListener(R.id.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color = colorsSelectView.getColor();
                String msg = "#"+Integer.toHexString(color).toUpperCase();
                Toast.makeText(MainActivity.this, "选中颜色: "+msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
