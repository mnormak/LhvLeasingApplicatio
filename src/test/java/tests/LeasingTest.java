package tests;

import org.testng.annotations.DataProvider;
import pageObjects.Application;
import org.testng.annotations.Test;
import utils.ExcelUtil;

import java.util.Map;

public class LeasingTest {

    @DataProvider
    Object[][] loadAllPositiveTests() {
        return ExcelUtil.getData("testData.xlsx", "Positive");
    }

    @DataProvider
    Object[][] loadAllNegativeTests() {
        return ExcelUtil.getData("testData.xlsx", "Negative");
    }

    @Test(dataProvider = "loadAllPositiveTests", description = "Fill leasing application first form and click Next")
    public void fillLeasingPageDataAndClickNext(Map<String, String> data) {
        Application
                .openLeasingApplicationPage(data.get("browser"))
                .selectLanguage(data.get("language"))
                .fillData(data)
                .checkCalculationsCorrectness(data)
                .clickNext()
                .checkThatThereIsNoErrorDisplayed();
    }

    @Test(dataProvider = "loadAllNegativeTests", description = "Leasing application first form error message check for mandatory fields")
    public void checkError(Map<String, String> data) {
        Application
                .openLeasingApplicationPage(data.get("browser"))
                .selectLanguage(data.get("language"))
                .fillData(data)
                .clickNext()
                .checkErrorMsgAndInputsAfterWrongInputs(data);
    }
}
