package org.ylzl.eden.commons.scripts;

import lombok.experimental.UtilityClass;
import org.mvel2.MVEL;

import java.io.Serializable;
import java.util.Map;

/**
 * MVEL 脚本语言工具集
 *
 * @author gyl
 * @since 2.0.0
 */
@UtilityClass
public class MvelUtils {

  public static <T> T executeExpression(String expression, Class<T> clazz) {
    return executeExpression(expression, clazz, null);
  }

  public static <T> T executeExpression(
      String expression, Class<T> clazz, Map<String, Object> vars) {
    Serializable compileExpression = MVEL.compileExpression(expression);
    return MVEL.executeExpression(compileExpression, vars, clazz);
  }
}
