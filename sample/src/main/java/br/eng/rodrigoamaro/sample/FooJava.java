package br.eng.rodrigoamaro.sample;

public class FooJava {

    public String doSomethingAwesome(BarKotlin bar) {
        Level level = bar.calculate(5, 8, 9, 2);
        return "Your level is " + level.name();
    }

}
