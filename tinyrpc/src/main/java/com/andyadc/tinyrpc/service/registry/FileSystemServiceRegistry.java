package com.andyadc.tinyrpc.service.registry;

import com.andyadc.tinyrpc.serializer.Serializer;
import com.andyadc.tinyrpc.service.ServiceInstance;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileSystemServiceRegistry implements ServiceRegistry {

	private final Serializer serializer = Serializer.DEFAULT;

	private File rootDirectory;

	@Override
	public void initialize(Map<String, Object> config) {
		rootDirectory = new File(System.getProperty("java.io.tmpdir"));
	}

	@Override
	public void register(ServiceInstance serviceInstance) {
		String serviceName = serviceInstance.getServiceName();
		File serviceDirectory = new File(rootDirectory, serviceName);
		File serviceInstanceFile = new File(serviceDirectory, serviceInstance.getId());
		try {
			byte[] bytes = serializer.serialize(serviceInstance);
			FileUtils.writeByteArrayToFile(serviceInstanceFile, bytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void deregister(ServiceInstance serviceInstance) {
		String serviceName = serviceInstance.getServiceName();
		File serviceDirectory = new File(rootDirectory, serviceName);
		File serviceInstanceFile = new File(serviceDirectory, serviceInstance.getId());
		FileUtils.deleteQuietly(serviceInstanceFile);
	}

	@Override
	public List<ServiceInstance> getServiceInstances(String serviceName) {
		File serviceDirectory = new File(rootDirectory, serviceName);
		Collection<File> listFiles = FileUtils.listFiles(serviceDirectory, null, false);
		return (List) listFiles.stream().map(file -> {
			try {
				byte[] bytes = FileUtils.readFileToByteArray(file);
				return serializer.deserialize(bytes, ServiceInstance.class);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}).collect(Collectors.toList());
	}

	@Override
	public void close() {
		FileUtils.deleteQuietly(rootDirectory);
	}
}
