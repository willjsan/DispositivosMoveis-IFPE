package recife.ifpe.edu.airpower.model.repo.model.weather;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import java.util.ArrayList;
import java.util.List;

public class Weather {
    private WeatherInfo main;
    private String name;
    private List<WeatherDetail> weather = new ArrayList<>();

    public Weather() {
    }

    public WeatherInfo getMain() {
        return main;
    }

    public void setMain(WeatherInfo main) {
        this.main = main;
    }

    /**
     * @return the name of current place
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WeatherDetail> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherDetail> weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "main=" + main +
                ", name='" + name + '\'' +
                ", weather=" + weather +
                '}';
    }
}
