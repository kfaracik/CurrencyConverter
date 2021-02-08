package data.jsonParer;

import com.google.gson.Gson;

public class GlobalCurrency extends JsonParser {
    public GlobalCurrency.Root root;

    public String readUrl(String urlString) throws Exception {
        return super.readUrl(urlString);
    }

    public void update() throws Exception {
        String url = "https://currencyapi.net/api/v1/rates?key=mBqBTTrjanB1DSKI6sgYL06Gh0oXgUZ3cLNj";
        String json = readUrl(url);

        Gson gson = new Gson();
        root = gson.fromJson(json, GlobalCurrency.Root.class);
    }

    public double parseCode(String code) {
        return switch (code) {
            case "PLN" -> root.rates.PLN;
            case "USD" -> root.rates.USD;
            case "AUD" -> root.rates.AUD;
            case "CAD" -> root.rates.CAD;
            case "EUR" -> root.rates.EUR;
            case "HUF" -> root.rates.HUF;
            case "CHF" -> root.rates.CHF;
            case "GBP" -> root.rates.GBP;
            case "JPY" -> root.rates.JPY;
            case "CZK" -> root.rates.CZK;
            case "DKK" -> root.rates.DKK;
            case "NOK" -> root.rates.NOK;
            case "SEK" -> root.rates.SEK;
            case "XRD" -> root.rates.XRD;
            default -> 0;
        };
    }

    public static class Root{
        public String base;
        public Rates rates;
    }

    public static class Rates{
        public double PLN;
        public double USD;
        public double AUD;
        public double CAD;
        public double EUR;
        public double HUF;
        public double CHF;
        public double GBP;
        public double JPY;
        public double CZK;
        public double DKK;
        public double NOK;
        public double SEK;
        public double XRD;
    }
}
