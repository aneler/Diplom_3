package utils;

public class URL {
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site/";
    //api
    public static final String REGISTER_USER = "api/auth/register";
    public static final String LOGIN_USER = "api/auth/login";
    public static final String USER = "api/auth/user";
    public static final String ORDERS = "api/orders";
    public static final String INGREDIENTS = "api/ingredients";
    //stellar burger application
    public static final String REGISTER_PAGE = BASE_URI + "register";
    public static final String RESTORE_PASSWORD_PAGE = BASE_URI + "forgot-password";
    public static final String LOGIN_PAGE = BASE_URI + "login";
    public static final String PROFILE_PAGE = BASE_URI + "account/profile";
    public static final String ACCOUNT_PAGE = BASE_URI + "account";
}
