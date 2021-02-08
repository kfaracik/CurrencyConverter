package data.api;

import data.jsonParer.NetCurrency;

public class NetCompany extends Company{
    private final String companyName;
    private double value = 0;
    private final NetCurrency currency;

    public NetCompany(String companyName, NetCurrency currency) {
        this.companyName = companyName;
        this.currency = currency;
    }

    public final String getFirstName() {
        return companyName;
    }

    public String getLastName() {
        return String.valueOf(value);
    }

    public void update(String value, String primaryCurCode, String finalCurCode) throws Exception {
        double transactionVal = 0;

        try{
            transactionVal = Double.parseDouble(value);
        } catch (Exception exception) {
            System.out.println("Value field have to be number" + exception.getMessage());
        }

//        this.currency.update();   // it makes app slower
        double primaryCur = currency.parseCode(finalCurCode);
        double finalCur = currency.parseCode(primaryCurCode);

        this.value = round(transactionVal*(primaryCur/finalCur), 2);
    }
}