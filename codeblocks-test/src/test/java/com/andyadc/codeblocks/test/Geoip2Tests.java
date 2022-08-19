package com.andyadc.codeblocks.test;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.InetAddress;

public class Geoip2Tests {

	@Test
	public void testCountry() throws Exception{
		File database = new File("D:\\Developer\\maxmind\\GeoLite2-Country.mmdb");
		DatabaseReader reader = new DatabaseReader.Builder(database).build();

		InetAddress ipAddress = InetAddress.getByName("101.86.215.146");
		CountryResponse country = reader.country(ipAddress);

		System.out.println(country.getCountry());
	}
}
