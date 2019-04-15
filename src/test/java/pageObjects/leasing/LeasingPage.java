package pageObjects.leasing;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.apache.commons.math3.util.Precision;
import org.assertj.core.api.SoftAssertions;
import pageObjects.Application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.*;

public class LeasingPage extends Application {
    private String vehiclePriceString = "Vehicle price";
    private String downPaymentString = "Downpayment";
    private String residualValueString = "Residual value";
    private String xlsNamePersonType = "personType";
    private String XlsNameLeaseType = "leaseType";
    private String xlsNameVehiclePrice = "vehiclePrice";
    private String xlsNameVatIncluded = "vatIncluded";
    private String xlsNameDownPaymentPercentage = "downpaymentPercentage";
    private String xlsNameDownPaymentEuros = "downpaymentEuros";
    private String xlsNameLeasePeriodYears = "leasePeriodYears";
    private String xlsNameLeasePeriodMonths = "leasePeriodMonths";
    private String xlsNameResidualValuePercentage = "residualValuePercentage";
    private String xlsNameResidualValueEuros = "residualValueEuros";
    private String xlsNameDateOfPayment = "dateOfPayment";

    private SelenideElement
            linkEn = $("[data-language=en]"),
            linkRu = $("[data-language=ru]"),
            iFrame = $("#iframe"),
            radioBtnPrivatePerson = $("#account_type-P"),
            radioBtnLegalPerson = $("#account_type-C"),
            radioBtnFinancialLease = $("#lease_type-HP"),
            radioBtnOperatingLease = $("#lease_type-FL"),
            inputVehiclePrice = $("#origin-price"),
            checkboxVatIncluded = $("#vat_included"),
            inputDownPaymentPercentage = $("#initial_percentage"),
            inputDownPaymentEuros = $("#initial"),
            dropDownLeasePeriodYears = $("#duration_years"),
            dropDownLeasePeriodMonths = $("#duration_months"),
            inputResidualValuePercentage = $("#reminder_percentage"),
            inputResidualValueEuros = $("#reminder"),
            dropDownDateOfPayment = $("#payment_day"),
            buttonNext = $("#form1 button"),
            closeCookieWarningBtn = $("#accept-cookies"),
            labelVehiclePrice = $("[for=price]"),
            labelDownPayment = $("[for=initial_percentage]"),
            labelLeasePeriod = $("[for=duration_years]"),
            labelResidualValue = $("[for=reminder_percentage]"),
            inputError = $(".error");

    private ElementsCollection
            containersErrorMessage = $$(".msg-error"),
            rowsErrorMessage = $$(".row");

    public LeasingPage selectLanguage(String language) {
        closeCookieWarningBtn();
        switch (language.toLowerCase()) {
            case "et":
                break;
            case "en":
                linkEn.click();
                break;
            case "ru":
                linkRu.click();
                break;
            default:
                throw new RuntimeException("There is no language \"" + language + "\"!");
        }

        return this;
    }

    public LeasingPage fillData(Map<String, String> data) {
        switchTo().frame(iFrame);
        selectPersonType(data.get(xlsNamePersonType));
        selectLeasingType(data.get(XlsNameLeaseType));
        inputVehiclePrice.sendKeys(data.get(xlsNameVehiclePrice));
        fillVatIncludedData(data.get(xlsNameVatIncluded));
        inputDownPaymentPercentage.sendKeys(data.get(xlsNameDownPaymentPercentage));
        inputDownPaymentEuros.sendKeys(data.get(xlsNameDownPaymentEuros));
        fillLeasePeriodYears(data.get(xlsNameLeasePeriodYears));
        fillLeasePeriodMonths(data.get(xlsNameLeasePeriodMonths));
        inputResidualValuePercentage.sendKeys(data.get(xlsNameResidualValuePercentage));
        inputResidualValueEuros.sendKeys(data.get(xlsNameResidualValueEuros));
        fillDateOfPayment(data.get(xlsNameDateOfPayment));

        return this;
    }

    public LeasingPage checkCalculationsCorrectness(Map<String, String> data) {
        if (data.get(xlsNameVehiclePrice).equals("")) {
            throw new RuntimeException(vehiclePriceString + " has to be filled in for checking calculations!");
        }
        double vehiclePrice = Double.parseDouble(data.get(xlsNameVehiclePrice));
        String downPaymentEuros = data.get(xlsNameDownPaymentEuros);
        String downPaymentPercentage = data.get(xlsNameDownPaymentPercentage);
        String residualValueEuros = data.get(xlsNameResidualValueEuros);
        String residualValuePercentage = data.get(xlsNameResidualValuePercentage);
        String downPayment = downPaymentString.toLowerCase();
        String residualValue = residualValueString.toLowerCase();
        SoftAssertions softly = new SoftAssertions();

        if (!downPaymentEuros.equals("")) {
            double actualDownPaymentPercentage = Double.parseDouble(inputDownPaymentPercentage.getAttribute("value"));
            double expectedDownPaymentPercentage = Precision.round(Double.parseDouble(downPaymentEuros) * 100 / vehiclePrice, 2);
            softly.assertThat(expectedDownPaymentPercentage)
                    .withFailMessage("Expected " + downPayment + " %: \"" + expectedDownPaymentPercentage +
                            "\", actual " + downPayment + " %: \"" + actualDownPaymentPercentage + "\"!")
                    .isEqualTo(actualDownPaymentPercentage);
        } else if (!downPaymentPercentage.equals("")) {
            double actualDownPaymentEuros = Double.parseDouble(inputDownPaymentEuros.getAttribute("value"));
            double expectedDownPaymentEuros = Precision.round(Double.parseDouble(downPaymentPercentage) * vehiclePrice / 100, 2);
            softly.assertThat(expectedDownPaymentEuros)
                    .withFailMessage("Expected " + downPayment + " €: \"" + expectedDownPaymentEuros +
                            "\", actual " + downPayment + " €: \"" + actualDownPaymentEuros + "\"!")
                    .isEqualTo(actualDownPaymentEuros);
        }

        if (!residualValueEuros.equals("")) {
            double actualResidualValuePercentage = Double.parseDouble(inputResidualValuePercentage.getAttribute("value"));
            double expectedResidualValuePercentage = Precision.round(Double.parseDouble(residualValueEuros) * 100 / vehiclePrice, 2);
            softly.assertThat(
                    expectedResidualValuePercentage)
                    .withFailMessage("Expected " + residualValue + " %: \"" + expectedResidualValuePercentage +
                            "\", actual " + residualValue + " %: \"" + actualResidualValuePercentage + "\"!")
                    .isEqualTo(actualResidualValuePercentage);
        } else if (!residualValuePercentage.equals("")) {
            double actualResidualValueEuros = Double.parseDouble(inputResidualValueEuros.getAttribute("value"));
            double expectedResidualValueEuros = Precision.round(Double.parseDouble(residualValuePercentage) * vehiclePrice / 100, 2);
            softly.assertThat(
                    expectedResidualValueEuros)
                    .withFailMessage("Expected " + residualValue + " €: \"" + expectedResidualValueEuros +
                            "\", actual " + residualValue + " €: \"" + actualResidualValueEuros + "\"!")
                    .isEqualTo(actualResidualValueEuros);
        }

        softly.assertAll();

        return this;
    }

    public LeasingPage clickNext() {
        buttonNext.waitUntil(Condition.enabled, 3000);
        buttonNext.click();

        return this;
    }

    public void checkErrorMsgAndInputsAfterWrongInputs(Map<String, String> data) {
        SoftAssertions softly = new SoftAssertions();
        String language = data.get("language");
        List<String> errorMessages = getErrorMessages();
        String expectedGeneralErrorMsg = getGeneralErrorMessage(language);
        String vehiclePrice = data.get(xlsNameVehiclePrice);
        String downPaymentPercentage = data.get(xlsNameDownPaymentPercentage);
        String downPaymentEuros = data.get(xlsNameDownPaymentEuros);
        String residualValuePercentage = data.get(xlsNameResidualValuePercentage);
        String residualValueEuros = data.get(xlsNameResidualValueEuros);
        boolean isVehiclePriceFaulty = !vehiclePrice.matches("\\d+");
        boolean isDownPaymentPercentageFaulty = isPercentageFieldFaulty(downPaymentPercentage);
        boolean isDownPaymentEurosFaulty = isEurosInputFieldFaulty(downPaymentEuros, vehiclePrice);
        boolean isResidualValuePercentageFaulty = isPercentageFieldFaulty(residualValuePercentage);
        boolean isResidualValueEurosFaulty = isEurosInputFieldFaulty(residualValueEuros, vehiclePrice);
        boolean isLeasePeriodFaulty = dropDownLeasePeriodYears.getText().equals("0") && dropDownLeasePeriodMonths.getText().equals("0");

        if (isVehiclePriceFaulty) {
            assertFaultyInputColor(softly, inputVehiclePrice, vehiclePriceString);
            assertFaultyLabelColor(softly, labelVehiclePrice, vehiclePriceString);
        }
        if (isDownPaymentPercentageFaulty) {
            assertFaultyInputColor(softly, inputDownPaymentPercentage, downPaymentString + " %");
            assertFaultyLabelColor(softly, labelDownPayment, downPaymentString);
        }
        if (isDownPaymentEurosFaulty) {
            if (downPaymentPercentage.matches("\\d+") && vehiclePrice.matches("\\d+")) {
                double vehiclePriceDouble = Double.parseDouble(vehiclePrice);
                double downPaymentEurosDouble = Double.parseDouble(downPaymentPercentage);
                if (downPaymentEurosDouble >= vehiclePriceDouble) {
                    softly.assertThat(errorMessages)
                            .withFailMessage("Expected downpayment error message is not displayed!")
                            .contains(getDowPaymentErrorMessage(language));
                }
            }
            assertFaultyInputColor(softly, inputDownPaymentEuros, downPaymentString + " €");
            assertFaultyLabelColor(softly, labelDownPayment, downPaymentString);
        }
        if (isLeasePeriodFaulty) {
            assertFaultyInputColor(softly, dropDownLeasePeriodYears, "Lease period years");
            assertFaultyInputColor(softly, dropDownLeasePeriodMonths, "Lease period months");
            assertFaultyLabelColor(softly, labelLeasePeriod, "Lease period months");
        }
        if (isResidualValuePercentageFaulty) {
            assertFaultyInputColor(softly, inputResidualValuePercentage, residualValueString + " %");
            assertFaultyLabelColor(softly, labelResidualValue, residualValueString);
        }
        if (isResidualValueEurosFaulty) {
            if (residualValueEuros.matches("\\d+") && vehiclePrice.matches("\\d+")) {
                double residualValueEurosDouble = Double.parseDouble(residualValueEuros);
                double vehiclePriceDouble = Double.parseDouble(vehiclePrice);
                if (residualValueEurosDouble >= vehiclePriceDouble) {
                    softly.assertThat(errorMessages)
                            .withFailMessage("Expected residual value error message is not displayed!")
                            .contains(getResidualValueErrorMessage(language));
                }
            }
            assertFaultyInputColor(softly, inputResidualValueEuros, residualValueString + " €");
            assertFaultyLabelColor(softly, labelResidualValue, residualValueString);
        }

        softly.assertThat(errorMessages)
                .withFailMessage("Expected general error message is not displayed!")
                .contains(expectedGeneralErrorMsg);

        softly.assertAll();
    }

    public void checkThatThereIsNoErrorDisplayed() {
        if (containersErrorMessage.size() > 0) {
            throw new RuntimeException("Error message is displayed: " + containersErrorMessage.get(0).getText());
        }
    }

    private void selectPersonType(String personType) {
        if (personType.toLowerCase().contains("private")) {
            if (radioBtnPrivatePerson.getAttribute("checked") == null) {
                radioBtnPrivatePerson.click();
            }
        } else if (personType.toLowerCase().contains("legal")) {
            if (radioBtnLegalPerson.getAttribute("checked") == null) {
                radioBtnLegalPerson.click();
            }
        } else {
            throw new RuntimeException("There is no person type: " + personType);
        }
    }

    private void selectLeasingType(String leasingType) {
        if (leasingType.toLowerCase().contains("financial")) {
            if (radioBtnFinancialLease.getAttribute("checked") == null) {
                radioBtnFinancialLease.click();
            }
        } else if (leasingType.toLowerCase().contains("operating")) {
            if (radioBtnOperatingLease.getAttribute("checked") == null) {
                radioBtnOperatingLease.click();
            }
        } else {
            throw new RuntimeException("There is no leasing type: " + leasingType);
        }
    }

    private void fillVatIncludedData(String vatIncluded) {
        switch (vatIncluded.toLowerCase()) {
            case "yes":
                checkboxVatIncluded.setSelected(true);
                break;
            case "no":
                checkboxVatIncluded.setSelected(false);
                break;
            case "":
                break;
            default:
                throw new RuntimeException("Option \"" + vatIncluded + "\" is not implemented!");
        }
    }

    private void fillLeasePeriodYears(String years) {
        if (years.equals("")) {
            return;
        }
        List<String> allowedYearValues = Arrays.asList("0", "1", "2", "3", "4", "5", "6");
        if (!allowedYearValues.contains(years)) {
            throw new RuntimeException(" Value \"" + years + "\" doesn't exist in Lease period years dropdown!");
        }
        dropDownLeasePeriodYears.selectOption(years);
    }

    private void fillLeasePeriodMonths(String months) {
        if (months.equals("")) {
            return;
        }
        List<String> allowedMonthValues = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
        if (!allowedMonthValues.contains(months)) {
            throw new RuntimeException(" Value \"" + months + "\" doesn't exist in Lease period months dropdown!");
        }
        dropDownLeasePeriodMonths.selectOption(months);
    }

    private void fillDateOfPayment(String dateOfPayment) {
        if (dateOfPayment.equals("")) {
            return;
        }
        List<String> allowedDateOfPaymentValues = Arrays.asList("5", "15", "25");
        if (!allowedDateOfPaymentValues.contains(dateOfPayment)) {
            throw new RuntimeException(" Value \"" + dateOfPayment + "\" doesn't exist in Date of payment dropdown!");
        }
        dropDownDateOfPayment.selectOption(dateOfPayment);
    }

    private boolean isPercentageFieldFaulty(String percentage) {
        if (percentage.equals("")) {
            return false;
        }
        if (!percentage.matches("\\d+")) {
            return true;
        }
        double percentageDouble = Double.parseDouble(percentage);
        return percentageDouble < 0 || percentageDouble > 100;
    }

    private boolean isEurosInputFieldFaulty(String euros, String vehiclePrice) {
        if (euros.equals("")) {
            return false;
        }
        if (!euros.matches("\\d+")) {
            return true;
        } else if (!vehiclePrice.equals("") && !euros.matches("\\d+")) {
            double eurosDouble = Double.parseDouble(euros);
            double vehiclePriceDouble = Double.parseDouble(vehiclePrice);
            return eurosDouble > vehiclePriceDouble;
        }
        return false;
    }

    private void assertFaultyInputColor(SoftAssertions softly, SelenideElement input, String inputName) {
        softly.assertThat(input.closest(inputError.getSearchCriteria()).isDisplayed())
                .withFailMessage(inputName + " field is not marked as red!")
                .isTrue();
    }

    private void assertFaultyLabelColor(SoftAssertions softly, SelenideElement label, String inputName) {
        softly.assertThat(label.getAttribute("class").contains("error"))
                .withFailMessage(inputName + " label is not marked as red!")
                .isTrue();
    }

    private List<String> getErrorMessages() {
        List<String> errorMessages = new ArrayList<>();
        for (SelenideElement errorMessage : rowsErrorMessage) {
            errorMessages.add(errorMessage.getText());
        }
        return errorMessages;
    }

    private String getGeneralErrorMessage(String language) {
        switch (language.toUpperCase()) {
            case "ET":
                return INVALID_PARAMETERS_ERROR_ET;
            case "EN":
                return INVALID_PARAMETERS_ERROR_EN;
            case "RU":
                return INVALID_PARAMETERS_ERROR_RU;
            default:
                throw new RuntimeException("There is no language: " + language);
        }
    }

    private String getDowPaymentErrorMessage(String language) {
        switch (language.toUpperCase()) {
            case "ET":
                return DOWNPAYMENT_MISMATCH_ERROR_ET;
            case "EN":
                return DOWNPAYMENT_MISMATCH_ERROR_EN;
            case "RU":
                return DOWNPAYMENT_MISMATCH_ERROR_RU;
            default:
                throw new RuntimeException("There is no language: " + language);
        }
    }

    private String getResidualValueErrorMessage(String language) {
        switch (language.toUpperCase()) {
            case "ET":
                return RESIDUAL_VALUE_MISMATCH_ERROR_ET;
            case "EN":
                return RESIDUAL_VALUE_MISMATCH_ERROR_EN;
            case "RU":
                return RESIDUAL_VALUE_MISMATCH_ERROR_RU;
            default:
                throw new RuntimeException("There is no language: " + language);
        }
    }

    private void closeCookieWarningBtn() {
        if (closeCookieWarningBtn.isDisplayed()) {
            closeCookieWarningBtn.click();
        }
    }
}