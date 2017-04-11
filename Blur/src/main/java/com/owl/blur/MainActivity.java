package com.owl.blur;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.id_main_render)
    public void OnClickRender() {
        Intent intent = new Intent(this, RenderScriptTest.class);
        startActivity(intent);
    }

    @OnClick(R.id.id_main_fast)
    public void OnClickFast() {
        Intent intent = new Intent(this, FastBlurTest.class);
        startActivity(intent);
    }

    @OnClick(R.id.id_main_real)
    public void OnClickReal() {
        Intent intent = new Intent(this, RealBlurTest.class);
        startActivity(intent);
    }
}
