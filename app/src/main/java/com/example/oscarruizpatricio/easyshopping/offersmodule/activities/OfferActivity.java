package com.example.oscarruizpatricio.easyshopping.offersmodule.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.oscarruizpatricio.easyshopping.R;

public class OfferActivity extends AppCompatActivity {

    /**
     * Link
     */
    private String link;

    /**
     * Webview
     */
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        setViews();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            link = bundle.getString("link");
            webView.loadUrl(link);
        }
    }

    private void setViews() {
        webView = (WebView)findViewById(R.id.webview);
    }
}
