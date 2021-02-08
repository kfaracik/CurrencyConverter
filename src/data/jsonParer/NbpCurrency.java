package data.jsonParer;

import com.google.gson.Gson;

import java.util.List;


public class NbpCurrency extends JsonParser {
    public Root root;
    public Root[] item;

    public NbpCurrency() {
    }

    public String readUrl(String urlString) throws Exception {
        return super.readUrl(urlString);
    }

    public void update() throws Exception {
        String url = "http://api.nbp.pl/api/exchangerates/tables/c/?format=json";
        String json = readUrlAsSingleObj(url);

        Gson gson = new Gson();
        root = gson.fromJson(json, Root.class);
    }

    public void updateHistData(String startDate, String endDate) throws Exception {
        String urlDataHist = "http://api.nbp.pl/api/exchangerates/tables/c/" +
                startDate + "/" +
                endDate + "/?format=json";

        String json = readUrl(urlDataHist);

        Gson gson = new Gson();
        item = gson.fromJson(json, Root[].class);
    }

//    public void getCurrencyValHist(String code, String date) {
//
//
//        for(i : item.)
//    }

    public static class Rate{
        public String currency;
        public String code;
        public double bid;
        public double ask;
    }

    public static class Root{
        public String tradingDate;
        public String effectiveDate;
        public List<Rate> rates;
    }
}