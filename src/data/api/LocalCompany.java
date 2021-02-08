package data.api;

import data.jsonParer.NbpCurrency;

public class LocalCompany extends Company{
    private final String companyName;
    private double value = 0;
    private NbpCurrency jsonData;

    public LocalCompany(String companyName, NbpCurrency jsonData) {
        this.companyName = companyName;
        this.jsonData = jsonData;
    }

    public final String getFirstName() {
        return companyName;
    }

    public String getLastName() {
        return String.valueOf(value);
    }

    public void update(String value, String primaryCurCode, String finalCurCode) throws Exception {
        double primaryCur = 1;
        double finalCur = 1;
        double transactionVal = 0;

        try {
            transactionVal = Double.parseDouble(value);
        } catch (Exception exception) {
            System.out.println("Value field have to be number" + exception.getMessage());
        }

//        this.jsonData.update();   // it makes app slower
        for (NbpCurrency.Rate item : jsonData.root.rates) {
            if(item.code.equals(primaryCurCode))
                primaryCur = item.ask;
        }

        for (NbpCurrency.Rate item : jsonData.root.rates) {
            if(item.code.equals(finalCurCode))
                finalCur = item.bid;
        }

        this.value = round(transactionVal*(primaryCur/finalCur), 2);
    }
}