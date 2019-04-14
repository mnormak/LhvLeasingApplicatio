package pageObjects;

import com.codeborne.selenide.Selenide;
import pageObjects.leasing.LeasingActions;
import utils.ConfigUtil;

public class Application {
    protected static String INVALID_PARAMETERS_ERROR_ET = "Vigased andmed, palun kontrolli märgitud väljasid.";
    protected static String INVALID_PARAMETERS_ERROR_EN = "Invalid parameters - please check the marked fields.";
    protected static String INVALID_PARAMETERS_ERROR_RU = "Неверные данные, пожалуйста, проверьте отмеченные поля.";
    protected static String DOWNPAYMENT_MISMATCH_ERROR_ET = "Sissemakse protsent ja summa ei ole vastavuses";
    protected static String DOWNPAYMENT_MISMATCH_ERROR_EN = "Non-compliance between downpayment percentage and amount";
    protected static String DOWNPAYMENT_MISMATCH_ERROR_RU = "Процент взноса не соответствует сумме";
    protected static String RESIDUAL_VALUE_MISMATCH_ERROR_ET = "Jääkväärtuse protsent ja summa ei ole vastavuses";
    protected static String RESIDUAL_VALUE_MISMATCH_ERROR_EN = "Non-compliance between residual value percentage and amount";
    protected static String RESIDUAL_VALUE_MISMATCH_ERROR_RU = "Процент остаточной стоимости не соответствует сумме";


    public static LeasingActions openLeasingApplicationPage(String browser) {
        createDriver(browser);
        Selenide.open("https://www.lhv.ee/et/liising/taotlus");

        return new LeasingActions();
    }

    private static void createDriver(String browser) {
        ConfigUtil configUtil = new ConfigUtil();
        String driversFolder = configUtil.getPropertyValue("driversFolder");
        switch (browser.toLowerCase()) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", driversFolder + "chromedriver.exe");
                System.setProperty("selenide.browser", "Chrome");
                break;
            case "firefox":
                System.setProperty("webdriver.gecko.driver", driversFolder + "geckodriver.exe");
                System.setProperty("selenide.browser", "Firefox");
                break;
            case "ie":
                System.setProperty("webdriver.ie.driver", driversFolder + "IEDriverServer.exe");
                System.setProperty("selenide.browser", "Internet Explorer");
        }
    }
}
