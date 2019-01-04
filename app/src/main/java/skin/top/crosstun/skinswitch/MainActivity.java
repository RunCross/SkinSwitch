package skin.top.crosstun.skinswitch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import crossrun.top.skin.SkinFactory;
import crossrun.top.skin.SkinManager;

public class MainActivity extends AppCompatActivity {

    protected SkinFactory skinFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        skinFactory = new SkinFactory();
        LayoutInflater.from(this).setFactory(skinFactory);
        super.onCreate(savedInstanceState);
        SkinManager.getInstance().init(this);
        setContentView(R.layout.activity_main);
    }

    public void onApply(View view) {
        SkinManager.getInstance().loadSkin("/storage/emulated/0/1");
        skinFactory.apply();
    }

    public void onApplyDefault(View view) {
        SkinManager.getInstance().loadSkin("");
        skinFactory.apply();
    }

}
