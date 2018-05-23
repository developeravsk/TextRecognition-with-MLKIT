package developerabhi.silpatechinnovations.mlkittutorial;


import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    TextView tv;
    ImageView imv;
    Button snap, detect;
Bitmap bmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textview);
        imv = findViewById(R.id.imageview);
        snap = findViewById(R.id.snap);
        detect = findViewById(R.id.detect);
        snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takepicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takepicture.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takepicture, 12);
                }
            }
        });
        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
detectTxt();
            }
        });
    }

    private void detectTxt() {
        FirebaseVisionImage image=FirebaseVisionImage.fromBitmap(bmp);
        FirebaseVisionTextDetector detector= FirebaseVision.getInstance().getVisionTextDetector();
        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
             processText(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void processText(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.Block> blocks=firebaseVisionText.getBlocks();

        if(blocks.size()==0){
            Toast.makeText(this, "No text found", Toast.LENGTH_SHORT).show();
            return;
        }else{
            String txt="";
           for(FirebaseVisionText.Block myblock:firebaseVisionText.getBlocks()) {
               txt= txt+myblock.getText()+"\n";
           }
            tv.setTextSize(30);
            tv.setText(txt);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");
           bmp=image;
            imv.setImageBitmap(image);
        }
    }
}
