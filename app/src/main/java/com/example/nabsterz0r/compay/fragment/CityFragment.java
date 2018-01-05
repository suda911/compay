package com.example.nabsterz0r.compay.fragment;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.nabsterz0r.compay.ApiService;
import com.example.nabsterz0r.compay.BR;
import com.example.nabsterz0r.compay.MyApp;
import com.example.nabsterz0r.compay.R;
import com.example.nabsterz0r.compay.Util;
import com.example.nabsterz0r.compay.adapter.RVAdapterSimple;
import com.example.nabsterz0r.compay.model.City;
import com.example.nabsterz0r.compay.model.WeatherResponse;
import com.example.nabsterz0r.compay.model.orm.Weather;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.objectbox.Box;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class CityFragment extends Fragment {

    List<City> cities = new ArrayList<>();

    RVAdapterSimple adapter;

    @BindView(R.id.rec_view_city)
    RecyclerView recViewCity;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    Unbinder unbinder;

    @Inject
    public ApiService apiService;
    private Box<Weather> cityBox;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city, container, false);
        ((MyApp) getActivity().getApplication()).getNetComponent().inject(this);
        cityBox = ((MyApp) getActivity().getApplication()).getBoxStore().boxFor(Weather.class);
        unbinder = ButterKnife.bind(this, view);
        init(view);
        return view;
    }

    /**
     * Инициализация
     */
    private void init(View view) {
        cities = Util.getCity(getContext());
        adapter = new RVAdapterSimple<>(cities, R.layout.cardview_city, new Presenter());
        recViewCity.setLayoutManager(new LinearLayoutManager(getContext()));
        recViewCity.setAdapter(adapter);

        LinearLayout llBottomSheet = view.findViewById(R.id.bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //презентер для списка
    public class Presenter {
        public void getWeather(int id) {
            progressBar.setVisibility(View.VISIBLE);
            apiService.info(id)
                    .onErrorReturn(throwable -> findCityInBox(id))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(weatherResponse -> showBottom(getView(), weatherResponse),
                            Throwable::getMessage);
            progressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Отображение bottom sheet с информацией о погоде
     */
    private void showBottom(View view, WeatherResponse weatherResponse) {
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        ViewDataBinding binding = DataBindingUtil.bind(bottomSheet);
        binding.setVariable(BR.item, weatherResponse);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(binding.getRoot());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        addCityToBox(weatherResponse);
    }

    /**
     * Загрузка картинки
     */
    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView imageView, String v) {
        Glide.with(imageView.getContext())
                .load(v)
                .centerCrop()
                .into(imageView);
    }

    /**
     * Изменение цвета подложки от температуры
     */
    @BindingAdapter("bind:colorTemp")
    public static void setColorTemp(LinearLayout linearLayout, double temp) {
        if (temp >= 0) {
            linearLayout.setBackgroundColor(linearLayout.getContext().getResources().getColor(R.color.bottom_sun));
        } else {
            linearLayout.setBackgroundColor(linearLayout.getContext().getResources().getColor(R.color.bottom_cold));
        }
    }

    /**
     * Сохранение в БД погоды для города
     * @param weatherResponse Модель погоды города
     */
    private void addCityToBox(WeatherResponse weatherResponse) {
        cityBox.put(new Weather(weatherResponse.getId(), weatherResponse.getMain().getTemp(), weatherResponse.getName()));
    }

    /**
     * Возвращает погоду города из БД
     *
     * @param city id city
     * @return модель города из БД
     */
    private WeatherResponse findCityInBox(int city) {
        Weather weatherCity = cityBox.get(city);
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setMain(new WeatherResponse.Main());
        weatherResponse.getMain().setTemp(weatherCity.getWeather());
        weatherResponse.setName(weatherCity.getName());
        return weatherResponse;
    }
}
