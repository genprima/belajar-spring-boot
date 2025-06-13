# Java Annotations: Quick Reference

## What are Annotations?
- Annotations are metadata that provide information to the compiler or runtime about your code.
- They do not directly affect program logic but can be used by tools, frameworks, or your own code (via reflection).

## Common Built-in Annotations
- `@Override`, `@Deprecated`, `@SuppressWarnings`, etc.
- Frameworks like Spring and Hibernate provide many useful annotations.

## Custom Annotations
You can define your own annotations for custom metadata or processing.

### Example: Defining a Custom Annotation
```java
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyFieldAnnotation {
    String value() default "";
}
```
- `@Target` specifies where the annotation can be used (class, field, method, parameter, etc.).
- `@Retention` specifies when the annotation is available (source, class, or runtime).

## Where Can You Put Annotations?
- **Class-level:** On classes, interfaces, enums
- **Field-level:** On fields
- **Method-level:** On methods
- **Parameter-level:** On method parameters

## Processing Annotations (Plain Java)
- Use reflection to find and process annotations.
- Example: Loop through fields and check for your annotation.

```java
for (Field field : obj.getClass().getDeclaredFields()) {
    if (field.isAnnotationPresent(MyFieldAnnotation.class)) {
        MyFieldAnnotation ann = field.getAnnotation(MyFieldAnnotation.class);
        System.out.println("Field: " + field.getName() + ", Value: " + ann.value());
    }
}
```

## Processing Annotations in Spring
- Spring automatically processes many annotations (e.g., `@Component`, `@Autowired`, `@Controller`).
- For custom processing, you can:
    - Implement `ApplicationContextAware` to get all beans
    - Use `@PostConstruct` to run your processor after context initialization
    - Loop through beans, classes, fields, methods, and parameters to find and process your annotations

### Example: Custom Annotation Processor in Spring
```java
@Component
public class MyAnnotationProcessor implements ApplicationContextAware {
    private ApplicationContext ctx;
    @Override
    public void setApplicationContext(ApplicationContext ctx) { this.ctx = ctx; }
    @PostConstruct
    public void process() {
        for (Object bean : ctx.getBeansOfType(Object.class).values()) {
            Class<?> clazz = bean.getClass();
            // Class-level
            if (clazz.isAnnotationPresent(MyClassAnnotation.class)) { /* ... */ }
            // Field-level
            for (Field field : clazz.getDeclaredFields()) { /* ... */ }
            // Method-level
            for (Method method : clazz.getDeclaredMethods()) { /* ... */ }
        }
    }
}
```

## Parameter Annotations
- You can annotate method parameters and process them via reflection:
```java
for (Parameter param : method.getParameters()) {
    if (param.isAnnotationPresent(MyParamAnnotation.class)) {
        // ...
    }
}
```

## Class vs Field vs Method vs Parameter Annotations
- **Class-level:** Used for marking/configuring the whole class (e.g., `@Entity`, `@Controller`)
- **Field-level:** Used for marking/configuring a specific field (e.g., `@Autowired`, `@NotNull`)
- **Method-level:** Used for marking/configuring a method (e.g., `@PostConstruct`, `@RequestMapping`)
- **Parameter-level:** Used for marking/configuring a method parameter (e.g., `@RequestParam`, `@Valid`)

## Validation Annotations
- Use `@Constraint` and implement `ConstraintValidator` for custom validation.
- Spring and Hibernate Validator will process these automatically.

## Summary Table
| Placement   | What it Annotates | How to Process                |
|-------------|-------------------|-------------------------------|
| Class       | Whole class       | Check class, then fields/methods if needed |
| Field       | Single field      | Check each field              |
| Method      | Single method     | Check each method             |
| Parameter   | Method parameter  | Check each parameter          |

## Best Practices
- Use built-in annotations when possible.
- For custom logic, define your own annotation and process it as needed.
- In Spring, let the framework handle annotation processing when possible; use manual processing only for advanced/custom use cases.

---

**Keep this file as a quick reference for Java annotation concepts, creation, and processing!** 