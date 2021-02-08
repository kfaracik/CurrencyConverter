package data.jsonParer;

import com.google.gson.Gson;

public class NetCurrency extends JsonParser {
    public NetCurrency.Root root;
    private final String source = "USD";      // to change source buy premium :(

    public String readUrl(String urlString) throws Exception {
        return super.readUrl(urlString);
    }

    public void update() throws Exception {
        String url = "http://apilayer.net/api/live?access_key=39a8bfbc0aaba75897dcd28de5904fc7&currencies="
                + "PLN,USD,AUD,CAD,EUR,HUF,CHF,GBP,JPY,CZK,DKK,NOK,SEK,XDR"
                + "&source=" + source
                + "&format=1";
        String json = readUrl(url);

        Gson gson = new Gson();
        root = gson.fromJson(json, NetCurrency.Root.class);
    }

    public double parseCode(String code) {
        return switch (source+code) {
            case "USDPLN" -> root.quotes.USDPLN;
            case "USDUSD" -> root.quotes.USDUSD;
            case "USDAUD" -> root.quotes.USDAUD;
            case "USDCAD" -> root.quotes.USDCAD;
            case "USDEUR" -> root.quotes.USDEUR;
            case "USDHUF" -> root.quotes.USDHUF;
            case "USDCHF" -> root.quotes.USDCHF;
            case "USDGBP" -> root.quotes.USDGBP;
            case "USDJPY" -> root.quotes.USDJPY;
            case "USDCZK" -> root.quotes.USDCZK;
            case "USDDKK" -> root.quotes.USDDKK;
            case "USDNOK" -> root.quotes.USDNOK;
            case "USDSEK" -> root.quotes.USDSEK;
            case "USDXRD" -> root.quotes.USDXRD;
            default -> 0;
        };
    }

    public static class Root{
        public String terms;
        public int timestamp;
        public String source;
        public Quotes quotes;
    }

    public static class Quotes{
        public double USDPLN;
        public double USDUSD;
        public double USDAUD;
        public double USDCAD;
        public double USDEUR;
        public double USDHUF;
        public double USDCHF;
        public double USDGBP;
        public double USDJPY;
        public double USDCZK;
        public double USDDKK;
        public double USDNOK;
        public double USDSEK;
        public double USDXRD;
    }
}
