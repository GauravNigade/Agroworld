package com.project.agroworld.articles;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.project.agroworld.R;
import com.project.agroworld.articles.adapter.FruitsAdapter;
import com.project.agroworld.articles.adapter.HowToExpandAdapter;
import com.project.agroworld.articles.listener.ExpandClickListener;
import com.project.agroworld.articles.model.FruitsResponse;
import com.project.agroworld.articles.model.HowToExpandResponse;
import com.project.agroworld.databinding.ActivityHowToExpandBinding;
import com.project.agroworld.ui.viewmodel.AgroViewModel;
import com.project.agroworld.utils.CustomMultiColorProgressBar;

import java.util.ArrayList;

public class HowToExpandActivity extends AppCompatActivity implements ExpandClickListener {

    private final ArrayList<HowToExpandResponse> howToExpandDataList = new ArrayList<>();
    private HowToExpandAdapter expandAdapter;
    private AgroViewModel viewModel;
    private ActivityHowToExpandBinding binding;
    private CustomMultiColorProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_how_to_expand);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.fruits));
        viewModel = ViewModelProviders.of(this).get(AgroViewModel.class);
        progressBar = new CustomMultiColorProgressBar(this, getString(R.string.no_data_found));
        viewModel.init();
        getExpandListFromApi();
    }

    private void getExpandListFromApi() {
        progressBar.showProgressBar();
        viewModel.getHowToExpandResponseLivedata().observe(this, resource -> {
            switch (resource.status) {
                case ERROR:
                    progressBar.hideProgressBar();
                    binding.rvHowToExpand.setVisibility(View.GONE);
                    binding.tvNoExpandDataFound.setVisibility(View.VISIBLE);
                    binding.tvNoExpandDataFound.setText(resource.message);
                    break;
                case LOADING:
                    progressBar.showProgressBar();
                    break;
                case SUCCESS:
                    if (resource.data != null) {
                        howToExpandDataList.clear();
                        howToExpandDataList.addAll(resource.data);
                        progressBar.hideProgressBar();
                        binding.rvHowToExpand.setVisibility(View.VISIBLE);
                        setRecyclerView();
                    } else {
                        binding.tvNoExpandDataFound.setVisibility(View.VISIBLE);
                        binding.tvNoExpandDataFound.setText(getString(R.string.no_data_found));
                    }
                    break;
            }
        });
    }

    private void setRecyclerView() {
        expandAdapter = new HowToExpandAdapter(howToExpandDataList, this);
        binding.rvHowToExpand.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvHowToExpand.setAdapter(expandAdapter);
    }

    @Override
    public void onExpandItemClick(HowToExpandResponse response) {
        Intent intent = new Intent(HowToExpandActivity.this, ArticleDetailsActivity.class);
        intent.putExtra("expandItemResponse", response);
        intent.putExtra("isExpandResponse", true);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}