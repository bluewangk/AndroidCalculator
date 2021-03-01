package com.example.mycalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtOutput;
    private TextView txtHistory;
    private boolean invalid = false;
    private final String InvalidInputError = "Invalid input";
    private final int[] btnArray = {
            R.id.btnClear,
            R.id.btnBackspace,
            R.id.btnPercent,
            R.id.btnDivide,
            R.id.btnSeven,
            R.id.btnEight,
            R.id.btnNine,
            R.id.btnMultiply,
            R.id.btnFour,
            R.id.btnFive,
            R.id.btnSix,
            R.id.btnSubstract,
            R.id.btnOne,
            R.id.btnTwo,
            R.id.btnThree,
            R.id.btnAdd,
            R.id.btnMinus,
            R.id.btnZero,
            R.id.btnDot,
            R.id.btnEqual};

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClear:
                txtOutput.setText("0");
                txtHistory.setText("");
                break;
            case R.id.btnBackspace:
                String word = txtOutput.getText().toString();
                int input = word.length();
                if (input > 0){
                    if (input == 1)
                        txtOutput.setText("0");
                    else
                        txtOutput.setText(word.substring(0, txtOutput.getText().toString().length()-1));
                }
                break;
            case R.id.btnPercent:
                txtOutput.append("%");
                break;
            case R.id.btnDot:
                txtOutput.append(".");
                break;
            case R.id.btnOne:
                txtOutput.append("1");
                break;
            case R.id.btnTwo:
                txtOutput.append("2");
                break;
            case R.id.btnThree:
                txtOutput.append("3");
                break;
            case R.id.btnFour:
                txtOutput.append("4");
                break;
            case R.id.btnFive:
                txtOutput.append("5");
                break;
            case R.id.btnSix:
                txtOutput.append("6");
                break;
            case R.id.btnSeven:
                txtOutput.append("7");
                break;
            case R.id.btnEight:
                txtOutput.append("8");
                break;
            case R.id.btnNine:
                txtOutput.append("9");
                break;
            case R.id.btnZero:
                txtOutput.append("0");
                break;
            case R.id.btnAdd:
                txtOutput.append("+");
                break;
            case R.id.btnSubstract:
                txtOutput.append("-");
                break;
            case R.id.btnMultiply:
                txtOutput.append("*");
                break;
            case R.id.btnDivide:
                txtOutput.append("/");
                break;
            case R.id.btnEqual:
                String result;
                try {
                    result = String.valueOf(calculate(txtOutput.getText().toString()));
                    txtOutput.append("="+result);
                    txtHistory.append(txtOutput.getText().toString()+"\n");
                } catch (Exception e) {
                    invalid = true;
                    result = InvalidInputError;
                }

                txtOutput.setText(result);
                break;
            default:
                break;
        }

        formatDisplayText(txtOutput);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtOutput = findViewById(R.id.txtOutput);

        txtHistory = findViewById(R.id.txtHistory);
        txtHistory.setMovementMethod(new ScrollingMovementMethod());

        for (int btnId: btnArray) {
            findViewById(btnId).setOnClickListener(this);
        }
    }

    private Double convertToNumber(String input) {
        //return Integer.parseInt(input);
        return Double.valueOf(input);
    }

    private String calculate(String equation) {
        LinkedList<String> equationList = new LinkedList<>();
        double firstNumber;
        double secondNumber;
        String operator = null;

        for (String substr: equation.split("")) {
            if (equationList.isEmpty()) {
                equationList.addFirst(substr);
            } else if (isCharOperator(substr)) {
                operator = substr;
            } else {
                if ("*".equals(operator) || "/".equals(operator)) {
                    firstNumber = convertToNumber(equationList.pop());
                    secondNumber = convertToNumber(substr);

                    equationList.addFirst(String.valueOf(calculate(firstNumber, secondNumber, operator)));
                    operator = null;
                } else if ("+".equals(operator) || "-".equals(operator)) {
                    if (equationList.size() > 1) {
                        secondNumber = convertToNumber(equationList.pop());
                        String newOperator = equationList.pop();
                        firstNumber = convertToNumber(equationList.pop());

                        equationList.addFirst(String.valueOf(calculate(firstNumber, secondNumber, newOperator)));
                        equationList.addFirst(operator);
                        equationList.addFirst(substr);
                        operator = null;
                    } else {
                        equationList.addFirst(operator);
                        operator = null;
                        equationList.addFirst(substr);
                    }
                } else {
                    equationList.addFirst(equationList.pop() + substr);
                }
            }
        }

        if (equationList.size() == 1) {
            return equationList.pop();
        } else {
            secondNumber = convertToNumber(equationList.pop());
            operator = equationList.pop();
            firstNumber = convertToNumber(equationList.pop());

            return calculate(firstNumber, secondNumber, operator);
        }
    }

    private String calculate(double first, double second, String operator) {
        double result;
        BigDecimal firstNo = new BigDecimal(Double.toString(first));
        BigDecimal secondNo = new BigDecimal(Double.toString(second));
        switch (operator) {
            case "add":
            case "+":
                result = firstNo.add(secondNo).doubleValue();
                break;
            case "subtract":
            case "-":
                result = firstNo.subtract(secondNo).doubleValue();
                break;
            case "multiply":
            case "*":
                result = firstNo.multiply(secondNo).doubleValue();
                break;
            case "divide":
            case "/":
                result = first / second;
                //result = firstNo.divide(secondNo).doubleValue();
                break;
            default:
                throw new RuntimeException("Failed to calculate.");
        }

        if (result % 1 == 0)
            return (int)result + "";
        else
            return result + "";
    }

    private void formatDisplayText(TextView txtView) {
        String input = txtView.getText().toString();
        // Remove 'Invalid input'
        if (invalid && input.length() > InvalidInputError.length()) {
            invalid = false;
            input = input.substring(InvalidInputError.length());
        }

        StringBuilder strTxt = new StringBuilder(input);
        if (strTxt.equals("") || strTxt.equals("0"))
            return;
        
        // change 0123 to 123
        while (strTxt.length() > 1 && strTxt.charAt(0) == '0') {
            strTxt.deleteCharAt(0);
        }

        // change 1x+ to 1+
        if (strTxt.length() >= 2 && isCharOperator(strTxt.charAt(strTxt.length()-1)) && isCharOperator(strTxt.charAt(strTxt.length()-2))) {
            strTxt.deleteCharAt(strTxt.length()-2);
        }

        // change 1.. to 1.
        if (strTxt.length() >= 2 && strTxt.charAt(strTxt.length()-1) == '.' && strTxt.charAt(strTxt.length()-2) == '.') {
            strTxt.deleteCharAt(strTxt.length()-1);
        }

        // change +1 to 0+1
        if (isCharOperator(strTxt.charAt(0)) || strTxt.charAt(0) == '.') {
            strTxt.insert(0, '0');
        }

        // Change 1.2. to 1.2
        if (strTxt.indexOf(".") > 0 && strTxt.indexOf(".") < strTxt.lastIndexOf("."))
            strTxt.deleteCharAt(strTxt.length()-1);

        // Handle on '%'
        strTxt = mathCalculate(strTxt);

        // set back formatted text to TextView
        txtView.setText(strTxt);
    }

    private boolean isCharOperator(char ch) {
        if (ch == '+' || ch == '-' || ch == '*' || ch == '/')
            return true;

        return false;
    }

    private boolean isCharOperator(String s) {
        if ("+".equals(s) || "-".equals(s) || "*".equals(s) || "/".equals(s))
            return true;

        return false;
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    
    private StringBuilder mathCalculate(StringBuilder strTxt) {
//        System.out.println(strTxt.substring(0, strTxt.length() - 1));
        if (!isNumeric(strTxt.substring(0, strTxt.length() - 1)))
            return strTxt;

        switch (strTxt.charAt(strTxt.length() - 1)) {
            case '%':
                strTxt = new StringBuilder(String.valueOf(convertToNumber(strTxt.substring(0, strTxt.length() - 1)) / 100));
                break;
            default:
                break;
        }

        return strTxt;
    }

}