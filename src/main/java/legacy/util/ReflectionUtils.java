package legacy.util;

import java.lang.reflect.Field;

public class ReflectionUtils {

  // Set a field value at any depth of super().
  public static void setFieldValue(Object instance, String fieldName, Object value) {
    Class<?> c = instance.getClass();
    while (c.getSuperclass() != null) {
      try {
        Field field = c.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
        return;
      } catch (NoSuchFieldException | IllegalAccessException e) {
        // Don't want to print anything, we expect this to miss sometimes while traversing the super classes.
        // e.printStackTrace();
      }
      c = c.getSuperclass();
    }

    // We always stop one super class short, so we need to check one more time.
    try {
      Field field = c.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(instance, value);
    } catch (NoSuchFieldException | IllegalAccessException e) {}

    return;
  }

  // Get a field value at any depth of super().s
  public static <T> T getFieldValue(Object instance, String fieldName, T defaultValue) {
    Class<?> c = instance.getClass();
    while (c.getSuperclass() != null) {
      try {
        Field field = c.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(instance);
      } catch (NoSuchFieldException | IllegalAccessException e) {
        // Don't want to print anything, we expect this to miss sometimes while traversing the super classes.
        // e.printStackTrace();
      }
      c = c.getSuperclass();
    }

    // We always stop one super class short, so we need to check one more time.
    try {
      Field field = c.getDeclaredField(fieldName);
      field.setAccessible(true);
      return (T) field.get(instance);
    } catch (NoSuchFieldException | IllegalAccessException e) {}

    return defaultValue;
  }

  // Print all fields of an object. This *may* not actually print all fields, as it only gets fields defined by the
  // same level of abstraction the instance is. Fields defined by super classe *won't* get printed.
  public static void printFields(Object instance, Class<?> c) {
    System.out.println("FIELDS FOR CLASS [" + c.getName() + "]");
    for (Field field : c.getDeclaredFields()) {
      try {
        field.setAccessible(true);
        System.out.println("field: " + field.getName() + ", value: " + field.get(instance));
      } catch (IllegalArgumentException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  public static void printFields(Object instance) {
    printFields(instance, instance.getClass());
  }

}
