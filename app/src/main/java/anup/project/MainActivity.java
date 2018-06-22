package anup.project;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;

import anup.project.response.FlickrResponse;
import anup.project.response.ResponsePhotos;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,IView, AbsListView.OnScrollListener {

    private MainPresenter mPresenter;
    private EditText editTextView;
    private ProgressBar progressBar;
    private GridView gridView;
    private View mEditTextLL;
    private int page=1;
    private boolean isLoading;
    private boolean hasMorePages;
    private boolean isRefreshing;
    private GridAdapter mAdapter;
    private ResponsePhotos mResponse;
    private ProgressBar progressBarLoadMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter=new MainPresenter(this,new ImageFetcher());
        initViews();
    }

    private void initViews() {
        gridView = (GridView) findViewById(R.id.gridview);
        mEditTextLL=findViewById(R.id.search_ll);
        findViewById(R.id.search_button).setOnClickListener(this);
        editTextView=(EditText)findViewById(R.id.etQuery);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBarLoadMore = (ProgressBar) findViewById(R.id.progressBarLoadMore);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_button:
                editTextView.clearFocus();
                page=1;
                mPresenter.searchImages(editTextView.getText().toString(),page);
                break;
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        mEditTextLL.setVisibility(View.INVISIBLE);
        gridView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        mEditTextLL.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(AppConstants.ErrorType api) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        String message=getString(R.string.something_went_wrong);
        if(api == AppConstants.ErrorType.API){
            message = getString(R.string.response_could_notfetch);
        }else if (api == AppConstants.ErrorType.VALIDATION){
            message=getString(R.string.valid_query);
        }
        builder.setTitle(getString(R.string.alert))
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void hideKeyBoard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onResult(FlickrResponse json) {
        if(page==1){
            mResponse=json.getPhotos();
            mAdapter=new GridAdapter(this,json);
            gridView.setAdapter(mAdapter);
        }else{
            progressBarLoadMore.setVisibility(View.GONE);
            mResponse=json.getPhotos();
            mAdapter.addMore(json);
            mAdapter.notifyDataSetChanged();
        }
        isRefreshing=false;
        gridView.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //Do Nothing
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (gridView.getLastVisiblePosition() + 1 == totalItemCount && !isLoading) {
            isLoading = true;
            if (mResponse!=null && mResponse.getPage()<mResponse.getPages()&&!isRefreshing) {
                isRefreshing=true;
                onRefresh(page+1);
            }
        } else {
            isLoading = false;
        }
    }

    private void onRefresh(int pageSize) {
        Log.d("loading","page "+pageSize);
        page=pageSize;
        progressBarLoadMore.setVisibility(View.VISIBLE);
        mPresenter.loadMore(editTextView.getText().toString(),page);
    }
}
