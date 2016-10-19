package ru.allmoyki.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ru.allmoyki.R;
import ru.allmoyki.adapters.ServicesAdapter;
import ru.allmoyki.bus.BusProvider;
import ru.allmoyki.bus.ServiceClick;
import ru.allmoyki.bus.ServiceList;
import ru.allmoyki.bus.ServiceListSelected;
import ru.allmoyki.pojo.CurrentCarwashPojo;
import ru.allmoyki.widget.state.StateTextView;

public class CarwashServicesActivity extends AppCompatActivity {

    String[] names;
    @InjectView(R.id.tvTitleToolBar)
    TextView tvTitleToolBar;
    @InjectView(R.id.btnGetServices)
    StateTextView btnGetServices;

    @OnClick(R.id.tvTitleToolBar)
    public void close() {
        finish();
    }

    @OnClick(R.id.btnGetServices)
    public void services() {
        ServiceListSelected serviceList = new ServiceListSelected();
        serviceList.setWashServiceList(selectedWashServiceList);
        finish();
        BusProvider.getInstance().post(serviceList);

    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    List<CurrentCarwashPojo.WashService> allWashServiceList;
    List<CurrentCarwashPojo.WashService> selectedWashServiceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carwash_services);
        ButterKnife.inject(this);

        tvTitleToolBar.setText(getIntent().getStringExtra("title"));

        mRecyclerView = (RecyclerView) findViewById(R.id.rvServices);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        selectedWashServiceList = new ArrayList<>();

    }

    @Subscribe
    public void setListServices(ServiceList serviceList) {
        allWashServiceList = serviceList.getWashServiceList();
        ArrayList<String> listServices = new ArrayList<>();
        for (CurrentCarwashPojo.WashService washService : ServiceList.getWashServiceList()) {
            listServices.add(washService.getService());
        }

        mAdapter = new ServicesAdapter(ServiceList.getWashServiceList());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Subscribe
    public void getService(ServiceClick serviceClick) {
        if (serviceClick.isState()) {
            selectedWashServiceList.add(allWashServiceList.get(serviceClick.getPosition()));
        } else {
            selectedWashServiceList.remove(allWashServiceList.get(serviceClick.getPosition()));
        }
        int price = 0;
        for (CurrentCarwashPojo.WashService washService : selectedWashServiceList) {
            price += Integer.parseInt(washService.getCost());
        }
        btnGetServices.setText(price + " руб");
    }

    @Override
    protected void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        BusProvider.getInstance().unregister(this);

    }


}
