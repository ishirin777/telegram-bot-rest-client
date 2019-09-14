class ApiRequest {

    private static String host = "http://localhost:8080/customers/";

    static String getTotalCustomerCreditsAmount(String phone) {

        return host + "customerCreditsAmount/" + phone;
    }

    static String getCustomerAccounts(String phone) {

        return host + "customerAccounts/" + phone;
    }

    static String getCustomerNameAndSurname(String phone) {

        return host + "customerNameAndSurname/" + phone;
    }

    static String getCustomerByCustomerChatId(Long customerChatId) {

        return host + "customer/" + customerChatId;
    }

    static String postCustomerChatIdAndPhoneNumber() {

        return host;
    }
}
