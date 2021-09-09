package com.andyadc.codeblocks.interceptor;

import com.andyadc.codeblocks.common.lang.AnnotationUtils;
import com.andyadc.codeblocks.common.util.PriorityComparator;
import com.andyadc.codeblocks.interceptor.util.InterceptorUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.interceptor.ExcludeClassInterceptors;
import javax.interceptor.ExcludeDefaultInterceptors;
import javax.interceptor.InterceptorBinding;
import javax.interceptor.Interceptors;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Default {@link InterceptorManager}
 */
public class DefaultInterceptorManager implements InterceptorManager {

	/**
	 * The supported annotation types of interceptor binding.
	 */
	private final Set<Class<? extends Annotation>> interceptorBindingTypes;

	/**
	 * The {@link InterceptorInfo} Repository
	 */
	private final Map<Class<?>, InterceptorInfo> interceptorInfoRepository;

	/**
	 * The interceptor binding types map the sorted {@link Interceptor @Interceptor} instances
	 */
	private final Map<InterceptorBindings, SortedSet<Object>> bindingInterceptors;

	/**
	 * The cache for {@link Method} or {@link Constructor} mapping the prioritized {@link Interceptor @Interceptor} instances
	 */
	private final Map<Executable, List<Object>> interceptorsCache;

	public DefaultInterceptorManager() {
		this.interceptorBindingTypes = new LinkedHashSet<>();
		this.interceptorInfoRepository = new LinkedHashMap<>();
		this.bindingInterceptors = new LinkedHashMap<>();
		this.interceptorsCache = new LinkedHashMap<>();
		registerDefaultInterceptorBindingType();
	}

	@Override
	public void registerInterceptorClass(Class<?> interceptorClass) {
		interceptorInfoRepository.computeIfAbsent(interceptorClass, InterceptorInfo::new);
	}

	@Override
	public void registerInterceptor(Object interceptor) {
		Class<?> interceptorClass = interceptor.getClass();
		registerInterceptorClass(interceptorClass);
		InterceptorBindings interceptorBindings = getInterceptorBindings(interceptorClass);
		SortedSet<Object> interceptors = bindingInterceptors.computeIfAbsent(
			interceptorBindings,
			t -> new TreeSet<>(PriorityComparator.INSTANCE)
		);
		interceptors.add(interceptor);
	}

	@Override
	public InterceptorInfo getInterceptorInfo(Class<?> interceptorClass) throws IllegalStateException {
		return interceptorInfoRepository.get(interceptorClass);
	}

	@Override
	public List<Object> resolveInterceptors(Executable executable, Object... defaultInterceptors) {
		return interceptorsCache.computeIfAbsent(executable, e -> {

			List<Object> interceptors = new LinkedList<>();

			if (!isExcludedDefaultInterceptors(executable)) {
				// 1. Default interceptors are invoked first
				interceptors.addAll(Arrays.asList(defaultInterceptors));
			}

			// Resolve interceptors using @Interceptors
			// 2. Interceptors declared by applying the Interceptors annotation at class-level to the target
			// class are invoked next.
			// 3. Interceptors declared by applying the Interceptors annotation at method- or constructor-level
			// are invoked next.
			List<Object> annotatedInterceptors = resolveAnnotatedInterceptors(executable);
			interceptors.addAll(annotatedInterceptors);

			// Resolve interceptors using Interceptor Bindings
			// 4. Interceptors declared using interceptor bindings are invoked next.
			SortedSet<Object> bindingInterceptors = resolveBindingInterceptors(executable);
			interceptors.addAll(bindingInterceptors);

			// 5.2.1 Use of the Priority Annotation in Ordering Interceptors
			InterceptorUtils.sortInterceptors(interceptors);

			return Collections.unmodifiableList(interceptors);
		});
	}

	@Override
	public void registerInterceptorBindingType(Class<? extends Annotation> interceptorBindingType) {
		this.interceptorBindingTypes.add(interceptorBindingType);
	}

	@Override
	public boolean isInterceptorBindingType(Class<? extends Annotation> annotationType) {
		return AnnotationUtils.isMetaAnnotation(annotationType, InterceptorBinding.class) ||
			interceptorBindingTypes.contains(annotationType);
	}

	public Set<Class<? extends Annotation>> getInterceptorBindingTypes() {
		return Collections.unmodifiableSet(interceptorBindingTypes);
	}

	private void registerDefaultInterceptorBindingType() {
		registerInterceptorBindingType(PostConstruct.class);
		registerInterceptorBindingType(PreDestroy.class);
	}

	private boolean isExcludedDefaultInterceptors(Executable executable) {
		return AnnotationUtils.findAnnotation(executable, ExcludeDefaultInterceptors.class) != null;
	}

	private SortedSet<Object> resolveBindingInterceptors(Executable executable) {
		InterceptorBindings interceptorBindings = resolveInterceptorBindings(executable);
		return Collections.unmodifiableSortedSet(bindingInterceptors.getOrDefault(interceptorBindings, Collections.emptySortedSet()));
	}

	/**
	 * Interceptors declared by applying the Interceptors annotation at class-level to the target
	 * class are invoked next.
	 * <p>
	 * Interceptors declared by applying the Interceptors annotation at method- or constructor-level are invoked next.
	 *
	 * @param executable the intercepted of {@linkplain Method method} or {@linkplain Constructor constructor}
	 * @return non-null
	 * @see Interceptors
	 * @see ExcludeClassInterceptors
	 */
	private List<Object> resolveAnnotatedInterceptors(Executable executable) {
		Class<?> componentClass = executable.getDeclaringClass();

		List<Class<?>> interceptorClasses = new LinkedList<>();

		if (!executable.isAnnotationPresent(ExcludeClassInterceptors.class)) {
			Interceptors classInterceptors = InterceptorUtils.searchAnnotation(componentClass, Interceptors.class);
			if (classInterceptors != null) {
				for (Class<?> interceptorClass : classInterceptors.value()) {
					interceptorClasses.add(interceptorClass);
				}
			}
		}

		Interceptors executableInterceptors = InterceptorUtils.searchAnnotation(executable, Interceptors.class);
		if (executableInterceptors != null) {
			for (Class<?> interceptorClass : executableInterceptors.value()) {
				interceptorClasses.add(interceptorClass);
			}
		}

		return interceptorClasses.stream()
			.map(InterceptorUtils::unwrap)
			.collect(Collectors.toList());
	}


	/**
	 * The set of interceptor bindings for a method or constructor are those applied to the target class
	 * combined with those applied at method level or constructor level.
	 * Note that the interceptor bindings applied to the target class may include those inherited from
	 * its superclasses.
	 *
	 * @param executable {@link Method} or {@link Constructor}
	 * @return non-null
	 */
	private InterceptorBindings resolveInterceptorBindings(Executable executable) {
		Set<Annotation> interceptorBindings = doGetInterceptorBindings(executable);
		if (interceptorBindings.isEmpty()) {
			return resolveInterceptorBindings(executable.getDeclaringClass());
		}
		return new InterceptorBindings(interceptorBindings);
	}

	private InterceptorBindings resolveInterceptorBindings(Class<?> componentClass) {
		Set<Annotation> interceptorBindings;
		Class<?> type = componentClass;
		do {
			interceptorBindings = doGetInterceptorBindings(type);
			type = componentClass.getSuperclass();
		} while (type != Object.class && interceptorBindings.isEmpty());

		return new InterceptorBindings(interceptorBindings);
	}

	private Set<Annotation> doGetInterceptorBindings(AnnotatedElement annotatedElement) {
		Set<Annotation> interceptorBindings = new LinkedHashSet<>();
		for (Annotation annotation : annotatedElement.getAnnotations()) {
			if (isInterceptorBinding(annotation)) {
				interceptorBindings.add(annotation);
			}
		}
		return interceptorBindings;
	}
}
