package com.xivestudios.paradox.basiccalculator;

import android.animation.Animator;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    TextView result;
    boolean init;
    String curentOprator;

    private void init() {
        result = (TextView) findViewById(R.id.result);
        init = false;
        curentOprator = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    protected void clearAnimation(){
        if(Build.VERSION.SDK_INT >= 21) {
            int x = result.getWidth();
            int y = result.getHeight()/2;

            int startRadius = 0;
            int endRadius = (int) Math.hypot(result.getWidth(), result.getHeight());

            Animator anim = ViewAnimationUtils.createCircularReveal(result, x, y, startRadius, endRadius);
            anim.setDuration(800);
            result.setVisibility(View.VISIBLE);
            anim.start();
        }else{
            result.setVisibility(View.VISIBLE);
        }
    }

    public void resetResult(View view){
        init = false;
        result.setText("0");
        curentOprator = "";
        result.setVisibility(View.INVISIBLE);
        clearAnimation();
    }

    public void appendNumber(View view) {
        if (init == false) {
            result.setText("");
            init = true;
        }
        Button temp = (Button) view;
        if(result.getText().toString().endsWith(".")
                && temp.getText().toString().equals(".")
                && curentOprator==""){
            return;
        }
        result.append(temp.getText().toString());
    }

    public void clearOne(View view){
        if(result.getText().length()>=1){
            result.setText(result.getText().toString().substring(0,result.getText().length()-1));
        }
        if(result.getText().length()==0)
            resetResult(view);
    }

    private boolean isAlreadyInOpration(){
        if(result.getText().toString().endsWith("+")
                || result.getText().toString().endsWith("-")
                || result.getText().toString().endsWith("X")
                || result.getText().toString().endsWith("/")
                ){
            return true;
        }
        return false;
    }

    public void toggleSign(View view){
        try {
            if(Double.parseDouble(result.getText().toString())!=0.0){
                if(result.getText().toString().startsWith("-")){
                    result.setText(result.getText().toString().substring(1));
                }else{
                    result.setText("-"+result.getText().toString());
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String dummifyNumber(String num){
        if(num.equals(".")){
            num = "0.0";
        }
        return num;
    }

    public void appendOprator(View view){
        if(isAlreadyInOpration()){
            return;
        }
        Button temp = (Button) view;
        Log.i("msg",temp.getText().toString());
        if(curentOprator==""){
            result.append(temp.getText().toString());
        }else{
            Log.i("msg",temp.getText().toString()+" "+result.getText().toString());
            StringTokenizer numbers = new StringTokenizer(result.getText().toString(),curentOprator);
            if(numbers.countTokens()==2) {
                String n1 = numbers.nextToken();
                String n2 = numbers.nextToken();
                if(result.getText().toString().startsWith("-") && !n1.startsWith("-")){
                    n1 = "-"+n1;
                }
                try {
                    n1 = dummifyNumber(n1);
                    n2 = dummifyNumber(n2);
                    calculate(n1, n2);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(!temp.getText().toString().equals("="))
            result.append(temp.getText().toString());
        }
        if(!temp.getText().toString().equals("="))
        curentOprator = temp.getText().toString();
    }

    private void calculate(String n1, String n2){
        Log.i("msg",n1+" "+n2);
        switch (curentOprator){
            case "+":
                result.setText(Calculation.add(Double.parseDouble(n1),Double.parseDouble(n2)).toString());
                break;
            case "-":
                result.setText(Calculation.sub(Double.parseDouble(n1),Double.parseDouble(n2)).toString());
                break;
            case "X":
                result.setText(Calculation.mult(Double.parseDouble(n1),Double.parseDouble(n2)).toString());
                break;
            case "/":
                result.setText(Calculation.div(Double.parseDouble(n1),Double.parseDouble(n2)).toString());
                break;
        }
    }


}

class Calculation {

    public static Double add(Double val1, Double val2) {
        return val1 + val2;
    }

    public static Double sub(Double val1, Double val2) {
        return val1 - val2;
    }

    public static Double mult(Double val1, Double val2) {
        return val1 * val2;
    }

    public static Double div(Double val1, Double val2) {
        if (val2 != 0) {
            return val1 / val2;
        } else {
            return 0.0;
        }
    }
}
