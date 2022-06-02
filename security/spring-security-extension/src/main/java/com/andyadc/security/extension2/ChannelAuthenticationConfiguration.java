package com.andyadc.security.extension2;

import com.andyadc.security.extension.JwtTokenGenerator;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

public class ChannelAuthenticationConfiguration {

	public ChannelAuthenticationFilter channelAuthenticationFilter(List<LoginChannelProcessor<? extends AbstractAuthenticationToken>> channelFilters,
																   JwtTokenGenerator jwtTokenGenerator) {
		ChannelAuthenticationFilter channelAuthenticationFilter = new ChannelAuthenticationFilter(channelFilters);

		return channelAuthenticationFilter;
	}
}
