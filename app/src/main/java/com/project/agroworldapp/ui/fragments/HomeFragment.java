package com.project.agroworldapp.ui.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.project.agroworldapp.BuildConfig;
import com.project.agroworldapp.R;
import com.project.agroworldapp.articles.activity.CropsActivity;
import com.project.agroworldapp.articles.activity.DiseasesActivity;
import com.project.agroworldapp.articles.activity.FlowersActivity;
import com.project.agroworldapp.articles.activity.FruitsActivity;
import com.project.agroworldapp.articles.activity.HowToExpandActivity;
import com.project.agroworldapp.databinding.FragmentHomeBinding;
import com.project.agroworldapp.manufacture.adapter.ProductAdapter;
import com.project.agroworldapp.shopping.activity.ProductDetailActivity;
import com.project.agroworldapp.shopping.listener.OnProductListener;
import com.project.agroworldapp.shopping.model.ProductModel;
import com.project.agroworldapp.transport.adapter.OnVehicleCallClick;
import com.project.agroworldapp.transport.adapter.VehicleAdapter;
import com.project.agroworldapp.transport.model.VehicleModel;
import com.project.agroworldapp.ui.repository.AgroWorldRepositoryImpl;
import com.project.agroworldapp.utils.Constants;
import com.project.agroworldapp.utils.Permissions;
import com.project.agroworldapp.utils.Resource;
import com.project.agroworldapp.viewmodel.AgroViewModel;
import com.project.agroworldapp.viewmodel.AgroWorldViewModelFactory;
import com.project.agroworldapp.weather.activity.WeatherActivity;
import com.project.agroworldapp.weather.model.weather_data.WeatherResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private final List<ProductModel> productModelArrayList = new ArrayList<>(5);
    private final ArrayList<VehicleModel> vehicleItemList = new ArrayList<>(5);
    double latitude, longitude;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private FragmentHomeBinding binding;
    private AgroViewModel agroViewModel;
    private String locality;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        initializeAgroWorldViewModel();
        if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            askPermission();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        checkPermissionCallApi();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();

    }



        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            WebView webView = view.findViewById(R.id.weather_webview);
            WebSettings settings = webView.getSettings();
            settings.setBuiltInZoomControls(true);
            settings.setJavaScriptEnabled(true);
            settings.setSupportMultipleWindows(true);
            settings.setLoadWithOverviewMode(true);
            // settings.setAppCacheEnabled(false);
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            settings.setAllowFileAccess(true);
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
            settings.setDomStorageEnabled(true);
            settings.setUserAgentString("Android");
            settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
            settings.setUseWideViewPort(true);
            // settings.setAppCacheEnabled(true);
            webView.clearCache(true);
            webView.setVerticalScrollbarOverlay(true);

            webView.loadUrl("https://www.accuweather.com/");

            webView.setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_UP && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
                return false;
            });


    }

  /*  private void setupwebview(){
        binding.weatherWebview.setWebViewClient(new WebViewClient()); // Navigation stays inside WebView
        WebSettings webSettings = binding.weatherWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        WebView.setWebContentsDebuggingEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setLoadWithOverviewMode(true);
        // settings.setAppCacheEnabled(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUserAgentString("Android");
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        webSettings.setUseWideViewPort(true);
        // settings.setAppCacheEnabled(true);
        binding.weatherWebview.clearCache(true);


        binding.weatherWebview.loadUrl("https://www.accuweather.com/");
    }*/
   /* private void setupWebView() {
        WebView wv = binding.weatherWebview;

        WebSettings ws = wv.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setUseWideViewPort(true);
        ws.setLoadWithOverviewMode(true);
        wv.setVerticalScrollBarEnabled(true);
        wv.loadUrl("https://www.accuweather.com/");
    }
*/

    private void getProductListFromFirebase() {
        LiveData<Resource<List<ProductModel>>> observeProductFirebaseLivedata;
        boolean selectedAppLanguage = Constants.selectedLanguage(getContext());
        if (selectedAppLanguage) {
            agroViewModel.getLocalizedProductDataList();
            observeProductFirebaseLivedata = agroViewModel.observeLocalizedProductLivedata;
        } else {
            agroViewModel.getProductModelLivedata();
            observeProductFirebaseLivedata = agroViewModel.observeProductLivedata;
        }
        observeProductFirebaseLivedata.observe(getViewLifecycleOwner(), productModelResource -> {
            switch (productModelResource.status) {
                case ERROR:
                    break;
                case LOADING:
                    break;
                case SUCCESS:
                    if (productModelResource.data != null) {
                        productModelArrayList.clear();
                        productModelArrayList.addAll(productModelResource.data);
                    } else {

                    }
                    break;
            }
        });
    }



    private void getVehicleListFromFirebase() {
        agroViewModel.getVehicleModelLivedata();
        agroViewModel.observeTransportResourceLiveData.observe(getViewLifecycleOwner(), vehicleModelResource -> {
            switch (vehicleModelResource.status) {
                case ERROR:
                    break;
                case LOADING:
                    break;
                case SUCCESS:
                    if (vehicleModelResource.data != null) {
                        vehicleItemList.clear();
                        vehicleItemList.addAll(vehicleModelResource.data);
                    } else {

                    }
                    break;
            }
        });
    }

    public void initializeAgroWorldViewModel() {
        AgroWorldRepositoryImpl agroWorldRepository = new AgroWorldRepositoryImpl();
        agroViewModel = ViewModelProviders.of(this, new AgroWorldViewModelFactory(agroWorldRepository, getContext())).get(AgroViewModel.class);
    }


    private void askPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.GPS_REQUEST_CODE);

    }

    private void checkPermissionCallApi() {
        if (Permissions.checkConnection(getContext()) && Permissions.isGpsEnable(getContext())) {
            getProductListFromFirebase();
            getVehicleListFromFirebase();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.GPS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Constants.showToast(requireContext(), getString(R.string.provide_permission));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE || requestCode == Constants.GPS_REQUEST_CODE) {
            checkPermissionCallApi();
        }
    }

}