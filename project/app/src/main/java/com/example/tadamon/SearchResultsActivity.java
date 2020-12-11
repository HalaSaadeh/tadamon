package com.example.tadamon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class SearchResultsActivity extends AppCompatActivity {

    private TextView criteriaLabel;
    private String searchCriteria;
    private LinearLayout searchResultList;
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // get Instance of the Cloud Firestore database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Intent intent = getIntent();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.search);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.search:
                    return true;
                case R.id.discover:
                    startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                case R.id.profile:
                    startActivity(new Intent(getApplicationContext(), ProfileScreenActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
            }
            return false;
        });

        criteriaLabel = findViewById(R.id.criteria);
        searchCriteria = intent.getStringExtra("criteria");
        criteriaLabel.setText(criteriaLabel.getText() + searchCriteria);

        searchResultList = findViewById(R.id.searchResultList);

        boolean keywords = getIntent().getBooleanExtra("keywords", true);
        if (!keywords)
            getResultsByCategory(getIntent().getStringExtra("criteria"));

        searchResultList.addView(createEventCard("nocnwie", "Event", "R.drawable.bg_img"));
        searchResultList.addView(createEventCard("nocnwie", "Event", "R.drawable.bg_img"));
        searchResultList.addView(createEventCard("nocnwie", "Event", "R.drawable.bg_img"));

    }

    public FlexboxLayout createEventCard(String id, String title, String imgSrc) {
        int margin = dpToPx(8);
        int padding = dpToPx(16);

        FlexboxLayout flexboxLayout = new FlexboxLayout(this);
        flexboxLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        flexboxLayout.setJustifyContent(JustifyContent.CENTER);
        flexboxLayout.setAlignItems(AlignItems.CENTER);

        MaterialCardView card = new MaterialCardView(this);
        //card.setId(id);
        card.setTag(id);

        card.setLayoutParams(new LinearLayout.LayoutParams(
                (int) getResources().getDimension(R.dimen.largeButtonWidth),
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) card.getLayoutParams();
        marginLayoutParams.setMargins(margin, margin, margin, margin);

        card.setElevation(8);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dpToPx(200)
        ));

        //imageView.setImageDrawable(getResources().getDrawable(imgSrc, getApplicationContext().getTheme()));
        setPic(imgSrc, imageView);
        setPic(imgSrc, imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout innerLinearLayout = new LinearLayout(this);
        innerLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(60)
        ));
        innerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        innerLinearLayout.setPadding(padding, padding, padding, padding);


        TextView text = new TextView(this);
        text.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        text.setText(title);
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.largeTextSize));
        text.setTextColor(getResources().getColor(R.color.black, getApplicationContext().getTheme()));
        Typeface typeface = ResourcesCompat.getFont(this, R.font.poppins_light);
        text.setTypeface(typeface);

        innerLinearLayout.addView(text);
        linearLayout.addView(imageView);
        linearLayout.addView(innerLinearLayout);
        card.addView(linearLayout);

        flexboxLayout.addView(card);

        // TODO -> NAVIGATE TO APPROPRIATE SCREEN WITH THE RIGHT INFO PULLED FROM THE DB
        card.setOnClickListener(e -> {
            Intent intent = new Intent(this, CrisisActivity.class);
            intent.putExtra("id", card.getTag().toString());
            startActivity(intent);
        });

        return flexboxLayout;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private static void setPic(String urlImage, ImageView imageView){
        new AsyncTask<String, Integer, Drawable>(){
            @Override
            protected Drawable doInBackground(String... strings) {
                Bitmap pic = null;
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(urlImage).openConnection();
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    pic = BitmapFactory.decodeStream(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new BitmapDrawable(Resources.getSystem(), pic);
            }
            protected void onPostExecute(Drawable result) {

                //Add image to ImageView
                imageView.setImageDrawable(result);
            }
        }.execute();
    }
    private void getResultsByCategory(String cat){
        db.collection("crisis_events").whereEqualTo("category", cat)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String,Object> data =  document.getData();
                                searchResultList.addView(createEventCard(document.getId(), (String )data.get("eventname"), (String) data.get("photo_url")));
                            }
                        }
                    }
                });
    }
}