package com.project.agroworldapp.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.agroworldapp.BuildConfig;
import com.project.agroworldapp.R;
import com.project.agroworldapp.databinding.FragmentShoppingBinding;
import com.project.agroworldapp.db.PreferenceHelper;
import com.project.agroworldapp.manufacture.adapter.ProductAdapter;
import com.project.agroworldapp.payment.activities.PaymentHistoryActivity;
import com.project.agroworldapp.shopping.activity.AddToCartActivity;
import com.project.agroworldapp.shopping.activity.ProductDetailActivity;
import com.project.agroworldapp.shopping.listener.OnProductListener;
import com.project.agroworldapp.shopping.model.ProductModel;
import com.project.agroworldapp.ui.repository.AgroWorldRepositoryImpl;
import com.project.agroworldapp.utils.Constants;
import com.project.agroworldapp.utils.Permissions;
import com.project.agroworldapp.utils.Resource;
import com.project.agroworldapp.viewmodel.AgroViewModel;
import com.project.agroworldapp.viewmodel.AgroWorldViewModelFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ShoppingFragment extends Fragment {
    private FragmentShoppingBinding binding;
    private WebView webView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shopping, container, false);

        webView = view.findViewById(R.id.marketwebviw);

        webView.setWebViewClient(new WebViewClient()); // Keeps navigation in WebView
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true); // Enable JavaScript if needed
        webSettings.setDomStorageEnabled(true); // For HTML5 storage APIs
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        WebView.setWebContentsDebuggingEnabled(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setSupportZoom(true);               // Allow zooming
        webSettings.setBuiltInZoomControls(true);       // Enable built-in gestures
        webSettings.setDisplayZoomControls(false);
        webView.loadUrl("https://www.agmarknet.gov.in/PriceAndArrivals/DatewiseCommodityReport.aspx");

        return view;

    }

    @Override
    public void onDestroyView() {
        if (webView != null) {
            webView.loadUrl("https://www.agmarknet.gov.in/PriceAndArrivals/DatewiseCommodityReport.aspx");
            webView.stopLoading();
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.clearHistory();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        super.onDestroyView();
    }





}