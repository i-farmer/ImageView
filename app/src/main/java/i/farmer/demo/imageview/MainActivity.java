package i.farmer.demo.imageview;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import i.farmer.widget.imageview.ImageLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageLayout.ImageView mImageView1 = findViewById(R.id.mImageView1);
        mImageView1.setBorder(30, Color.parseColor("#ff0000"));
    }
}