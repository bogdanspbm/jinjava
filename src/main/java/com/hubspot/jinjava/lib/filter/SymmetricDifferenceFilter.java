package com.hubspot.jinjava.lib.filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.util.ForLoop;
import com.hubspot.jinjava.util.ObjectIterator;

@JinjavaDoc(
    value = "Returns a list containing elements present in only one list.",
    params = {
        @JinjavaParam(value = "value", type = "sequence", desc = "The first list"),
        @JinjavaParam(value = "list", type = "sequence", desc = "The second list")
    },
    snippets = {
        @JinjavaSnippet(
            code = "{{ [1, 2, 3]|symmetric_difference([2, 3, 4]) }}")
    })
public class SymmetricDifferenceFilter implements AdvancedFilter {

  @Override
  public Object filter(Object var, JinjavaInterpreter interpreter, Object[] args, Map<String, Object> kwargs) {

    Set<Object> keysFirst = new HashSet<>();
    Set<Object> keysSecond = new LinkedHashSet<>();

    Set<Object> result = new LinkedHashSet<>();

    Object attr = null;
    if (args.length > 0) {
      attr = args[0];
    }

    ForLoop loop = ObjectIterator.getLoop(attr);
    while (loop.hasNext()) {
      keysSecond.add(loop.next());
    }

    loop = ObjectIterator.getLoop(var);
    while (loop.hasNext()) {
      Object next = loop.next();
      keysFirst.add(next);
      if (!keysSecond.contains(next)) {
        result.add(next);
      }
    }

    for (Object next : keysSecond) {
      if (!keysFirst.contains(next)) {
        result.add(next);
      }
    }

    return new ArrayList<>(result);
  }

  @Override
  public String getName() {
    return "symmetric_difference";
  }
}
