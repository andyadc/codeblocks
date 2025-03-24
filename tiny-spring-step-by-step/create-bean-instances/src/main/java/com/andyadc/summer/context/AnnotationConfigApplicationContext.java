package com.andyadc.summer.context;

import com.andyadc.summer.annotation.Autowired;
import com.andyadc.summer.annotation.Bean;
import com.andyadc.summer.annotation.Component;
import com.andyadc.summer.annotation.ComponentScan;
import com.andyadc.summer.annotation.Configuration;
import com.andyadc.summer.annotation.Import;
import com.andyadc.summer.annotation.Order;
import com.andyadc.summer.annotation.Primary;
import com.andyadc.summer.annotation.Value;
import com.andyadc.summer.exception.BeanCreationException;
import com.andyadc.summer.exception.BeanDefinitionException;
import com.andyadc.summer.exception.BeanNotOfRequiredTypeException;
import com.andyadc.summer.exception.NoSuchBeanDefinitionException;
import com.andyadc.summer.exception.NoUniqueBeanDefinitionException;
import com.andyadc.summer.exception.UnsatisfiedDependencyException;
import com.andyadc.summer.io.PropertyResolver;
import com.andyadc.summer.io.ResourceResolver;
import com.andyadc.summer.utils.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationConfigApplicationContext {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected final PropertyResolver propertyResolver;
	protected final Map<String, BeanDefinition> beans;

	private final Set<String> creatingBeanNames;

	public AnnotationConfigApplicationContext(Class<?> configClass, PropertyResolver propertyResolver) {
		this.propertyResolver = propertyResolver;

		// 扫描获取所有Bean的Class类型:
		final Set<String> beanClassNames = scanForClassNames(configClass);

		// 创建Bean的定义:
		this.beans = createBeanDefinitions(beanClassNames);

		// 创建BeanName检测循环依赖:
		this.creatingBeanNames = new HashSet<>();

		// 创建@Configuration类型的Bean:
		this.beans.values().stream()
			// 过滤出@Configuration:
			.filter(this::isConfigurationDefinition).sorted().map(def -> {
			createBeanAsEarlySingleton(def);
			return def.getName();
		}).collect(Collectors.toList());

		// 创建其他普通Bean:
		createNormalBeans();

		if (logger.isDebugEnabled()) {
			this.beans.values().stream().sorted().forEach(def -> {
				logger.debug("bean initialized: {}", def);
			});
		}
	}

	/**
	 * 创建普通的Bean
	 */
	void createNormalBeans() {
		// 获取BeanDefinition列表:
		List<BeanDefinition> defs = this.beans.values().stream()
			// filter bean definitions by not instantiation:
			.filter(def -> def.getInstance() == null).sorted().collect(Collectors.toList());

		defs.forEach(def -> {
			// 如果Bean未被创建(可能在其他Bean的构造方法注入前被创建):
			if (def.getInstance() == null) {
				// 创建Bean:
				createBeanAsEarlySingleton(def);
			}
		});
	}

	/**
	 * 创建一个Bean，但不进行字段和方法级别的注入。如果创建的Bean不是Configuration，则在构造方法中注入的依赖Bean会自动创建。
	 */
	public Object createBeanAsEarlySingleton(BeanDefinition def) {
		logger.info("Try create bean '{}' as early singleton: {}", def.getName(), def.getBeanClass().getName());
		if (!this.creatingBeanNames.add(def.getName())) {
			throw new UnsatisfiedDependencyException(String.format("Circular dependency detected when create bean '%s'", def.getName()));
		}

		// 创建方式：构造方法或工厂方法:
		Executable createFn = null;
		if (def.getFactoryName() == null) {
			// by constructor:
			createFn = def.getConstructor();
		} else {
			// by factory method:
			createFn = def.getFactoryMethod();
		}

		// 创建参数:
		final Parameter[] parameters = createFn.getParameters();
		final Annotation[][] parametersAnnos = createFn.getParameterAnnotations();
		Object[] args = new Object[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			final Parameter param = parameters[i];
			final Annotation[] paramAnnos = parametersAnnos[i];
			final Value value = ClassUtils.getAnnotation(paramAnnos, Value.class);
			final Autowired autowired = ClassUtils.getAnnotation(paramAnnos, Autowired.class);

			// @Configuration类型的Bean是工厂，不允许使用@Autowired创建:
			final boolean isConfiguration = isConfigurationDefinition(def);
			if (isConfiguration && autowired != null) {
				throw new BeanCreationException(
					String.format("Cannot specify @Autowired when create @Configuration bean '%s': %s.", def.getName(), def.getBeanClass().getName()));
			}

			// 参数需要@Value或@Autowired两者之一:
			if (value != null && autowired != null) {
				throw new BeanCreationException(
					String.format("Cannot specify both @Autowired and @Value when create bean '%s': %s.", def.getName(), def.getBeanClass().getName()));
			}
			if (value == null && autowired == null) {
				throw new BeanCreationException(
					String.format("Must specify @Autowired or @Value when create bean '%s': %s.", def.getName(), def.getBeanClass().getName()));
			}

			// 参数类型:
			final Class<?> type = param.getType();
			if (value != null) {
				// 参数是@Value:
				args[i] = this.propertyResolver.getRequiredProperty(value.value(), type);
			} else {
				// 参数是@Autowired:
				String name = autowired.name();
				boolean required = autowired.value();
				// 依赖的BeanDefinition:
				BeanDefinition dependsOnDef = name.isEmpty() ? findBeanDefinition(type) : findBeanDefinition(name, type);
				// 检测required==true?
				if (required && dependsOnDef == null) {
					throw new BeanCreationException(String.format("Missing autowired bean with type '%s' when create bean '%s': %s.", type.getName(),
						def.getName(), def.getBeanClass().getName()));
				}
				if (dependsOnDef != null) {
					// 获取依赖Bean:
					Object autowiredBeanInstance = dependsOnDef.getInstance();
					if (autowiredBeanInstance == null && !isConfiguration) {
						// 当前依赖Bean尚未初始化，递归调用初始化该依赖Bean:
						autowiredBeanInstance = createBeanAsEarlySingleton(dependsOnDef);
					}
					args[i] = autowiredBeanInstance;
				} else {
					args[i] = null;
				}
			}
		}

		// 创建Bean实例:
		Object instance = null;
		if (def.getFactoryName() == null) {
			// 用构造方法创建:
			try {
				instance = def.getConstructor().newInstance(args);
			} catch (Exception e) {
				throw new BeanCreationException(String.format("Exception when create bean '%s': %s", def.getName(), def.getBeanClass().getName()), e);
			}
		} else {
			// 用@Bean方法创建:
			Object configInstance = getBean(def.getFactoryName());
			try {
				instance = def.getFactoryMethod().invoke(configInstance, args);
			} catch (Exception e) {
				throw new BeanCreationException(String.format("Exception when create bean '%s': %s", def.getName(), def.getBeanClass().getName()), e);
			}
		}
		def.setInstance(instance);

		return def.getInstance();
	}

	/**
	 * 根据扫描的ClassName创建BeanDefinition
	 */
	Map<String, BeanDefinition> createBeanDefinitions(Set<String> classNameSet) {
		Map<String, BeanDefinition> defs = new HashMap<>();
		for (String className : classNameSet) {
			// 获取Class:
			Class<?> clazz = null;
			try {
				clazz = Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new BeanCreationException(e);
			}
			// java version + > || clazz.isRecord()
			if (clazz.isAnnotation() || clazz.isEnum() || clazz.isInterface()) {
				continue;
			}
			// 是否标注@Component?
			Component component = ClassUtils.findAnnotation(clazz, Component.class);
			if (component != null) {
				logger.info("found component: {}", clazz.getName());
				int mod = clazz.getModifiers();
				if (Modifier.isAbstract(mod)) {
					throw new BeanDefinitionException("@Component class " + clazz.getName() + " must not be abstract.");
				}
				if (Modifier.isPrivate(mod)) {
					throw new BeanDefinitionException("@Component class " + clazz.getName() + " must not be private.");
				}

				String beanName = ClassUtils.getBeanName(clazz);
				BeanDefinition def = new BeanDefinition(beanName, clazz, getSuitableConstructor(clazz), getOrder(clazz), clazz.isAnnotationPresent(Primary.class),
					// named init / destroy method:
					null, null,
					// init method:
					ClassUtils.findAnnotationMethod(clazz, PostConstruct.class),
					// destroy method:
					ClassUtils.findAnnotationMethod(clazz, PreDestroy.class));
				addBeanDefinitions(defs, def);
				logger.info("define bean: {}", def);

				Configuration configuration = ClassUtils.findAnnotation(clazz, Configuration.class);
				if (configuration != null) {
					scanFactoryMethods(beanName, clazz, defs);
				}
			}
		}
		return defs;
	}

	/**
	 * Get public constructor or non-public constructor as fallback.
	 */
	Constructor<?> getSuitableConstructor(Class<?> clazz) {
		Constructor<?>[] cons = clazz.getConstructors();
		if (cons.length == 0) {
			cons = clazz.getDeclaredConstructors();
			if (cons.length != 1) {
				throw new BeanDefinitionException("More than one constructor found in class " + clazz.getName() + ".");
			}
		}
		if (cons.length != 1) {
			throw new BeanDefinitionException("More than one public constructor found in class " + clazz.getName() + ".");
		}
		return cons[0];
	}

	/**
	 * Scan factory method that annotated with @Bean:
	 *
	 * <code>
	 * &#64;Configuration
	 * public class Hello {
	 *
	 * @Bean ZoneId createZone() {
	 * return ZoneId.of("Z");
	 * }
	 * }
	 * </code>
	 */
	void scanFactoryMethods(String factoryBeanName, Class<?> clazz, Map<String, BeanDefinition> defs) {
		for (Method method : clazz.getDeclaredMethods()) {
			Bean bean = method.getAnnotation(Bean.class);
			if (bean != null) {
				int mod = method.getModifiers();
				if (Modifier.isAbstract(mod)) {
					throw new BeanDefinitionException("@Bean method " + clazz.getName() + "." + method.getName() + " must not be abstract.");
				}
				if (Modifier.isFinal(mod)) {
					throw new BeanDefinitionException("@Bean method " + clazz.getName() + "." + method.getName() + " must not be final.");
				}
				if (Modifier.isPrivate(mod)) {
					throw new BeanDefinitionException("@Bean method " + clazz.getName() + "." + method.getName() + " must not be private.");
				}
				Class<?> beanClass = method.getReturnType();
				if (beanClass.isPrimitive()) {
					throw new BeanDefinitionException("@Bean method " + clazz.getName() + "." + method.getName() + " must not return primitive type.");
				}
				if (beanClass == void.class || beanClass == Void.class) {
					throw new BeanDefinitionException("@Bean method " + clazz.getName() + "." + method.getName() + " must not return void.");
				}

				BeanDefinition def = new BeanDefinition(ClassUtils.getBeanName(method), beanClass, factoryBeanName, method, getOrder(method),
					method.isAnnotationPresent(Primary.class),
					// init method:
					bean.initMethod().isEmpty() ? null : bean.initMethod(),
					// destroy method:
					bean.destroyMethod().isEmpty() ? null : bean.destroyMethod(),
					// @PostConstruct / @PreDestroy method:
					null, null);
				addBeanDefinitions(defs, def);
				logger.info("define bean: {}", def);
			}
		}
	}

	/**
	 * Do component scan and return class names.
	 */
	protected Set<String> scanForClassNames(Class<?> configClass) {
		// 获取要扫描的package名称:
		ComponentScan scan = ClassUtils.findAnnotation(configClass, ComponentScan.class);
		final String[] scanPackages = scan == null || scan.value().length == 0 ? new String[]{configClass.getPackage().getName()} : scan.value();
		logger.info("component scan in packages: {}", Arrays.toString(scanPackages));

		Set<String> classNameSet = new HashSet<>();
		for (String pkg : scanPackages) {
			// 扫描package:
			logger.info("scan package: {}", pkg);
			ResourceResolver resolver = new ResourceResolver(pkg);
			List<String> classList = resolver.scan(res -> {
				String name = res.name();
				if (name.endsWith(".class")) {
					return name.substring(0, name.length() - 6).replace("/", ".").replace("\\", ".");
				}
				return null;
			});
			if (logger.isDebugEnabled()) {
				classList.forEach((className) -> {
					logger.debug("class found by component scan: {}", className);
				});
			}
			classNameSet.addAll(classList);
		}

		// 查找@Import(Xyz.class):
		Import importConfig = configClass.getAnnotation(Import.class);
		if (importConfig != null) {
			for (Class<?> importConfigClass : importConfig.value()) {
				String importClassName = importConfigClass.getName();
				if (classNameSet.contains(importClassName)) {
					logger.warn("ignore import: " + importClassName + " for it is already been scanned.");
				} else {
					logger.debug("class found by import: {}", importClassName);
					classNameSet.add(importClassName);
				}
			}
		}

		return classNameSet;
	}

	boolean isConfigurationDefinition(BeanDefinition def) {
		return ClassUtils.findAnnotation(def.getBeanClass(), Configuration.class) != null;
	}

	/**
	 * Check and add bean definitions.
	 */
	void addBeanDefinitions(Map<String, BeanDefinition> defs, BeanDefinition def) {
		if (defs.put(def.getName(), def) != null) {
			throw new BeanDefinitionException("Duplicate bean name: " + def.getName());
		}
	}

	/**
	 * 根据Name查找BeanDefinition，如果Name不存在，返回null
	 */
	public BeanDefinition findBeanDefinition(String name) {
		return this.beans.get(name);
	}

	/**
	 * 根据Name和Type查找BeanDefinition，如果Name不存在，返回null，如果Name存在，但Type不匹配，抛出异常。
	 */
	public BeanDefinition findBeanDefinition(String name, Class<?> requiredType) {
		BeanDefinition def = findBeanDefinition(name);
		if (def == null) {
			return null;
		}
		if (!requiredType.isAssignableFrom(def.getBeanClass())) {
			throw new BeanNotOfRequiredTypeException(String.format("Autowire required type '%s' but bean '%s' has actual type '%s'.", requiredType.getName(),
				name, def.getBeanClass().getName()));
		}
		return def;
	}

	/**
	 * 根据Type查找若干个BeanDefinition，返回0个或多个。
	 */
	public List<BeanDefinition> findBeanDefinitions(Class<?> type) {
		return this.beans.values().stream()
			// filter by type and sub-type:
			.filter(def -> type.isAssignableFrom(def.getBeanClass()))
			// 排序:
			.sorted().collect(Collectors.toList());
	}

	/**
	 * 根据Type查找某个BeanDefinition，如果不存在返回null，如果存在多个返回@Primary标注的一个，如果有多个@Primary标注，或没有@Primary标注但找到多个，均抛出NoUniqueBeanDefinitionException
	 */
	public BeanDefinition findBeanDefinition(Class<?> type) {
		List<BeanDefinition> defs = findBeanDefinitions(type);
		if (defs.isEmpty()) {
			return null;
		}
		if (defs.size() == 1) {
			return defs.get(0);
		}
		// more than 1 beans, require @Primary:
		List<BeanDefinition> primaryDefs = defs.stream().filter(BeanDefinition::isPrimary).collect(Collectors.toList());
		if (primaryDefs.size() == 1) {
			return primaryDefs.get(0);
		}
		if (primaryDefs.isEmpty()) {
			throw new NoUniqueBeanDefinitionException(String.format("Multiple bean with type '%s' found, but no @Primary specified.", type.getName()));
		} else {
			throw new NoUniqueBeanDefinitionException(String.format("Multiple bean with type '%s' found, and multiple @Primary specified.", type.getName()));
		}
	}

	public <T> T getBean(String name) {
		BeanDefinition def = this.beans.get(name);
		if (def == null) {
			throw new NoSuchBeanDefinitionException(String.format("No bean defined with name '%s'.", name));
		}
		return (T) def.getRequiredInstance();
	}

	/**
	 * 通过Name和Type查找Bean，不存在抛出NoSuchBeanDefinitionException，存在但与Type不匹配抛出BeanNotOfRequiredTypeException
	 */
	public <T> T getBean(String name, Class<T> requiredType) {
		T t = findBean(name, requiredType);
		if (t == null) {
			throw new NoSuchBeanDefinitionException(String.format("No bean defined with name '%s' and type '%s'.", name, requiredType));
		}
		return t;
	}

	/**
	 * 通过Type查找Beans
	 */
	public <T> List<T> getBeans(Class<T> requiredType) {
		List<BeanDefinition> defs = findBeanDefinitions(requiredType);
		if (defs.isEmpty()) {
			return Collections.emptyList();
		}
		List<T> list = new ArrayList<>(defs.size());
		for (BeanDefinition def : defs) {
			list.add((T) def.getRequiredInstance());
		}
		return list;
	}

	/**
	 * 通过Type查找Bean，不存在抛出NoSuchBeanDefinitionException，存在多个但缺少唯一@Primary标注抛出NoUniqueBeanDefinitionException
	 */
	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> requiredType) {
		BeanDefinition def = findBeanDefinition(requiredType);
		if (def == null) {
			throw new NoSuchBeanDefinitionException(String.format("No bean defined with type '%s'.", requiredType));
		}
		return (T) def.getRequiredInstance();
	}

	/**
	 * 检测是否存在指定Name的Bean
	 */
	public boolean containsBean(String name) {
		return this.beans.containsKey(name);
	}

	// findXxx与getXxx类似，但不存在时返回null

	@SuppressWarnings("unchecked")
	protected <T> T findBean(String name, Class<T> requiredType) {
		BeanDefinition def = findBeanDefinition(name, requiredType);
		if (def == null) {
			return null;
		}
		return (T) def.getRequiredInstance();
	}

	protected <T> T findBean(Class<T> requiredType) {
		BeanDefinition def = findBeanDefinition(requiredType);
		if (def == null) {
			return null;
		}
		return (T) def.getRequiredInstance();
	}

	protected <T> List<T> findBeans(Class<T> requiredType) {
		return findBeanDefinitions(requiredType).stream().map(def -> (T) def.getRequiredInstance()).collect(Collectors.toList());
	}

	/**
	 * Get order by:
	 *
	 * <code>
	 * &#64;Order(100)
	 * &#64;Component
	 * public class Hello {}
	 * </code>
	 */
	int getOrder(Class<?> clazz) {
		Order order = clazz.getAnnotation(Order.class);
		return order == null ? Integer.MAX_VALUE : order.value();
	}

	/**
	 * Get order by:
	 *
	 * <code>
	 * &#64;Order(100)
	 * &#64;Bean
	 * Hello createHello() {
	 * return new Hello();
	 * }
	 * </code>
	 */
	int getOrder(Method method) {
		Order order = method.getAnnotation(Order.class);
		return order == null ? Integer.MAX_VALUE : order.value();
	}

}
