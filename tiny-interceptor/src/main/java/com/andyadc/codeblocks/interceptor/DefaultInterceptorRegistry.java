package com.andyadc.codeblocks.interceptor;

import com.andyadc.codeblocks.common.lang.AnnotationUtils;
import com.andyadc.codeblocks.common.util.PriorityComparator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Default {@link InterceptorRegistry}
 */
@Deprecated
public class DefaultInterceptorRegistry implements InterceptorRegistry {

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
	private final Map<InterceptorBindings, List<Object>> bindingInterceptors;

	public DefaultInterceptorRegistry() {
		this.interceptorBindingTypes = new LinkedHashSet<>();
		this.interceptorInfoRepository = new LinkedHashMap<>();
		this.bindingInterceptors = new LinkedHashMap<>();
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
		List<Object> interceptors = bindingInterceptors.computeIfAbsent(interceptorBindings, t -> new LinkedList<>());
		if (!interceptors.contains(interceptor)) {
			interceptors.add(interceptor);
			interceptors.sort(PriorityComparator.INSTANCE);
		}
	}

	@Override
	public InterceptorInfo getInterceptorInfo(Class<?> interceptorClass) throws IllegalStateException {
		return interceptorInfoRepository.get(interceptorClass);
	}

	@Override
	public List<Object> getInterceptors(AnnotatedElement interceptedElement) {
		InterceptorBindings interceptorBindings = resolveInterceptorBindings(interceptedElement);
		return Collections.unmodifiableList(bindingInterceptors.getOrDefault(interceptorBindings, Collections.emptyList()));
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

	private InterceptorBindings resolveInterceptorBindings(AnnotatedElement interceptedElement) {
		Set<Annotation> interceptorBindings = Stream.of(interceptedElement.getAnnotations())
			.filter(this::isInterceptorBinding)
			.collect(Collectors.toSet());
		return new InterceptorBindings(interceptorBindings);
	}
}
