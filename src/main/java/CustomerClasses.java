import lombok.*;


@Data
class CreditsAmount{
    private int totalCreditsAmountInAZN;
    private int totalCreditsAmountInUSD;

    public String getTotalCreditsAmount() {
        return "AZN-lə olan cəmi kreditlər: " + totalCreditsAmountInAZN + "\nUSD-lə olan cəmi kreditlər: " + totalCreditsAmountInUSD;
    }
}

@Data
class CustomerAccounts{
    private int azn;
    private int usd;
    private int eur;
    private int others;

    public String getAllInfo(){
        return "AZN: " + azn + "\nUSD: " + usd + "\nEUR: " + eur + "\nOthers: " + others;
    }
}

@Data
class CustomerNameAndSurname {
    private String customerName;
    private String customerSurname;
}

