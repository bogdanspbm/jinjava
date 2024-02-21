package com.hubspot.jinjava.el.ext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.JinjavaConfig;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.util.Collection;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class AdditionOperatorTest {

  private JinjavaInterpreter interpreter;

  @Before
  public void setup() {
    JinjavaConfig config = JinjavaConfig.newBuilder().withMaxStringLength(10).build();
    interpreter = new Jinjava(config).newInterpreter();
    JinjavaInterpreter.pushCurrent(interpreter);
  }

  @After
  public void tearDown() {
    JinjavaInterpreter.popCurrent();
  }

  @Test
  public void itConcatsStrings() {
    assertThat(interpreter.resolveELExpression("'foo' + 'bar'", -1)).isEqualTo("foobar");
  }

  @Test
  public void itAddsNumbers() {
    assertThat(interpreter.resolveELExpression("1 + 2", -1)).isEqualTo(3L);
  }

  @Test
  public void itConcatsNumberWithString() {
    assertThat(interpreter.resolveELExpression("'1' + 2", -1)).isEqualTo("12");
    assertThat(interpreter.resolveELExpression("1 + '2'", -1)).isEqualTo("12");
  }

  @Test
  public void itCombinesTwoLists() {
    assertThat(
      (Collection<Object>) interpreter.resolveELExpression(
        "['foo', 'bar'] + ['other', 'one']",
        -1
      )
    )
      .containsExactly("foo", "bar", "other", "one");
  }

  @Test
  public void itAddsToList() {
    assertThat(
      (Collection<Object>) interpreter.resolveELExpression("['foo'] + 'bar'", -1)
    )
      .containsExactly("foo", "bar");
  }

  @Test
  public void itCombinesTwoDicts() {
    assertThat(
      (Map<Object, Object>) interpreter.resolveELExpression(
        "{'k1':'v1'} + {'k2':'v2'}",
        -1
      )
    )
      .containsOnly(entry("k1", "v1"), entry("k2", "v2"));
  }

  @Test
  public void itConcatsStringsWithLimit() {
    assertThat(interpreter.resolveELExpression("'this string' + 'is too long'", -1))
      .isNull();
    assertThat(interpreter.getErrors()).hasSize(1);
    assertThat(interpreter.getErrors().get(0).getMessage())
      .contains("OutputTooBigException");
  }
}
