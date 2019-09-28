package api;

public interface ApiRequest {

    String host = "http://localhost:8080/customers/";

    static String getTotalCustomerCreditsAmount(String phone) {

        return host + "v1/customerCreditsAmount/" + phone;
    }

    static String getCustomerAccounts(String phone) {

        return host + "v1/customerAccounts/" + phone;
    }

    static String getCustomerNameAndSurname(String phone) {

        return host + "v1/customerNameAndSurname/" + phone;
    }
}
